import org.gradle.api.JavaVersion.VERSION_1_8

plugins {
    java
    jacoco
    id("pl.allegro.tech.build.axion-release") version "1.11.0"
    id("com.jfrog.bintray") version "1.8.5"
    id("org.ajoberstar.grgit") version "3.1.1" apply false
}

allprojects {
    group = "com.jgrouse"
    repositories {
        jcenter()
        mavenCentral()
    }
}


scmVersion {}

tasks.register<JacocoReport>("jacocoRootTestReport") {
}

tasks.named("jar") {
    enabled = false
}

tasks.register("artifactsUpload") {
    dependsOn("build")
}

fun artifactUploadAllowed(): Boolean {
    val grGit = org.ajoberstar.grgit.Grgit.open(mapOf("dir" to project.rootDir))
    val isClean = grGit.status().isClean
    return (isClean && !scmVersion.version.contains("SNAPSHOT"))
}


subprojects {
    apply(plugin = "java-library")
    apply(plugin = "java")
    apply(plugin = "maven-publish")

    apply(plugin = "jacoco")
    apply(plugin = "com.jfrog.bintray")
    apply(plugin = "org.ajoberstar.grgit")

    ext["uploadAllowed"] = artifactUploadAllowed()


    plugins.withType<JavaLibraryPlugin> {
        extensions.configure<JavaPluginExtension> {
            sourceCompatibility = VERSION_1_8
            targetCompatibility = VERSION_1_8
            withSourcesJar()
            withJavadocJar()

            dependencies {
                val junitVer = "5.6.0"
                "implementation"("javax.validation:validation-api:2.0.1.Final")
                "implementation"("org.apache.commons:commons-lang3:3.9")

                "testImplementation"("org.junit.jupiter:junit-jupiter-api:${junitVer}")
                "testRuntimeOnly"("org.junit.jupiter:junit-jupiter-engine:${junitVer}")

                "testImplementation"("org.assertj:assertj-core:3.15.0")
                "testImplementation"("org.mockito:mockito-core:3.3.0")
            }
        }
    }


    version = rootProject.scmVersion.version

    val rootJacocoReport = rootProject.tasks.getByName<JacocoReport>("jacocoRootTestReport")

    rootJacocoReport.sourceSets(sourceSets.main.get())
    rootJacocoReport.sourceSets(sourceSets.test.get())

    tasks.named<Test>("test") {
        useJUnitPlatform()
        extensions.configure(JacocoTaskExtension::class) {
        }
        finalizedBy(tasks.named("jacocoTestReport"))
        finalizedBy(tasks.named("jacocoTestCoverageVerification"))
    }

    tasks.named<JacocoCoverageVerification>("jacocoTestCoverageVerification") {
        violationRules {
            rule {
                excludes = listOf(
                        "com.jgrouse.util.io.IoRuntimeException"
                )
                limit {
                    minimum = "0.9".toBigDecimal()
                }
            }
        }
    }


    tasks.named<JacocoReport>("jacocoTestReport") {
        reports {
            xml.isEnabled = true
        }
    }

    rootJacocoReport.dependsOn(project.tasks.build)

    val rootArtifactsUpload = rootProject.tasks.getByName("artifactsUpload")
    tasks.register("artifactsUpload") {
        dependsOn("build")
        if (ext["uploadAllowed"] as Boolean) {
            finalizedBy(tasks.getByName("bintrayUpload"))
            finalizedBy(tasks.getByName("publish"))
        } else {
            logger.info("No released artifacts are eligible for upload for ${project.name}")
        }
    }
    rootArtifactsUpload.dependsOn("${project.name}:artifactsUpload")

}



tasks.register("ciBuild") {
    dependsOn("artifactsUpload")
}

tasks.named("build") {
    finalizedBy("jacocoRootTestReport")
}

tasks.named<JacocoReport>("jacocoRootTestReport") {
    reports {
        xml.isEnabled = true
        xml.destination = file("${buildDir}/reports/jacoco/jacoco-jGrouse.xml")
        html.isEnabled = false
    }
    executionData(fileTree(rootProject.rootDir).include("/*/build/jacoco/*.exec"))

}
