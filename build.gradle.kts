import org.gradle.api.JavaVersion.VERSION_1_8

plugins {
    java
    jacoco
    id("pl.allegro.tech.build.axion-release") version "1.11.0"
    id("com.jfrog.bintray") version "1.8.5"
    id("org.ajoberstar.grgit") version "3.1.1" apply false
    id("ru.vyarus.quality") version "4.1.0"
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

tasks.register<TestReport>("testReport") {
}

tasks.named("build") {
    finalizedBy("testReport")
}

subprojects {

    apply(plugin = "maven-publish")
    apply(plugin = "com.jfrog.bintray")
    apply(plugin = "org.ajoberstar.grgit")

    ext["uploadAllowed"] = artifactUploadAllowed()
    tasks.withType<Jar> {
        archiveBaseName.set("jgrouse-${project.name}")
    }

    version = rootProject.scmVersion.version

    if (this.name != "platform") {
        setupLibrarySubprojects()
    }

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

tasks.named<TestReport>("testReport") {
    destinationDir = file("$buildDir/reports/allTests")
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
        html.isEnabled = true
    }
    executionData(fileTree(rootProject.rootDir).include("/*/build/jacoco/*.exec"))

}

fun Project.setupLibrarySubprojects() {
    apply(plugin = "java-library")
    apply(plugin = "java")


    apply(plugin = "jacoco")
    apply(plugin = "ru.vyarus.quality")


    quality {
        spotbugsVersion = "4.0.1"
        checkstyle = false
        spotbugs = true
        codenarc = true
        pmd = true
    }

    dependencies {
        implementation(platform(project(":platform")))
        implementation("javax.validation:validation-api")
        implementation("org.apache.commons:commons-lang3")

        implementation("org.slf4j:slf4j-api")
        implementation("org.slf4j:log4j-over-slf4j")


        testImplementation("org.junit.jupiter:junit-jupiter-api")
        testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
        testImplementation("org.junit.jupiter:junit-jupiter")
        testImplementation("org.assertj:assertj-core")
        testImplementation("org.mockito:mockito-core")
    }

    plugins.withType<JavaLibraryPlugin> {
        extensions.configure<JavaPluginExtension> {
            sourceCompatibility = VERSION_1_8
            targetCompatibility = VERSION_1_8
            withSourcesJar()
            withJavadocJar()

            dependencies {
                "api"(platform(project(":platform")))
            }
        }
    }


    val rootTestReport = rootProject.tasks.getByName<TestReport>("testReport")

    tasks.named<Test>("test") {
        useJUnitPlatform()
        reports.html.isEnabled = false
        rootTestReport.reportOn(binaryResultsDirectory)

        extensions.configure(JacocoTaskExtension::class) {
        }
        finalizedBy(tasks.named("jacocoTestReport"))
        finalizedBy(tasks.named("jacocoTestCoverageVerification"))
        finalizedBy(rootTestReport.path)
    }


    tasks.named<JacocoCoverageVerification>("jacocoTestCoverageVerification") {
        violationRules {
            rule {
                element = "CLASS"
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

    val rootJacocoReport = rootProject.tasks.getByName<JacocoReport>("jacocoRootTestReport")
    rootJacocoReport.sourceSets(sourceSets.main.get())
    rootJacocoReport.dependsOn(project.tasks.build)
}