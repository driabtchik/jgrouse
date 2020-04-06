import org.gradle.api.JavaVersion.VERSION_1_8

plugins {
    id("pl.allegro.tech.build.axion-release") version "1.11.0"
    id("com.jfrog.bintray") version "1.8.4" apply false
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

subprojects {
    apply(plugin = "java-library")
    apply(plugin = "java")

    apply(plugin = "jacoco")
    apply(plugin = "pl.allegro.tech.build.axion-release")
    apply(plugin = "com.jfrog.bintray")
    apply(plugin = "org.ajoberstar.grgit")


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


    version = scmVersion.version

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

    tasks.register("artifactsUpload") {
        dependsOn("build")
        if (ext["uploadAllowed"] as Boolean) {
            val publishedArtifactId = ext["publishedArtifactId"] as String
            val publishedArtifactName = ext["publishedArtifactName"] as String
            val publishedArtifactDesc = ext["publishedArtifactDesc"] as String
            val directorySuffix = ext["directorySuffix"] as String
            setupPublishing(publishedArtifactId, publishedArtifactName, publishedArtifactDesc, directorySuffix)
            finalizedBy(tasks.getByName("bintrayUpload"))
        } else {
            logger.info("No released artifacts are eligible for upload for ${project.name}")
        }
    }

    ext["uploadAllowed"] = artifactUploadAllowed()
}


fun findProperty(s: String) = project.findProperty(s) as String?

fun setupPublishing(publishedArtifactId: String,
                    publishedArtifactName: String,
                    publishedArtifactDesc: String,
                    directorySuffix: String) {
    plugins.withType<MavenPublishPlugin> {
        extensions.configure<PublishingExtension> {
            publications {
                create<MavenPublication>("default") {
                    groupId = project.group as String
                    artifactId = publishedArtifactId
                    version = project.version as String
                    from(components["java"])
                    pom.withXml {
                        asNode().apply {
                            appendNode("name", publishedArtifactName)
                            appendNode("description", publishedArtifactDesc)
                            appendNode("url", "https://github.com/driabtchik/jgrouse/$directorySuffix")
                            appendNode("licenses").appendNode("license").apply {
                                appendNode("name", "The Apache Software License, Version 2.0")
                                appendNode("url", "http://www.apache.org/licenses/LICENSE-2.0.txt")
                                appendNode("distribution", "repo")
                            }
                            appendNode("developers").appendNode("developer").apply {
                                appendNode("name", "Denis Riabtchik")
                                appendNode("email", "denis.riabtchik@gmail.com")
                                appendNode("organization", "jGrouse")
                                appendNode("organizationUrl", "http://jGrouse.com")
                            }
                            appendNode("scm").apply {
                                appendNode("connection", "scm:git:git://github.com/driabtchik/jgrouse.git")
                                appendNode("developerConnection", "scm:git:ssh://github.com/driabtchik/jgrouse.git")
                                appendNode("url", "https://github.com/driabtchik/jgrouse/$directorySuffix")
                            }
                        }
                    }
                }
            }
        }
    }


    plugins.withType<com.jfrog.bintray.gradle.BintrayPlugin> {
        extensions.configure<com.jfrog.bintray.gradle.BintrayExtension> {
            user = findProperty("bintrayUser")
            key = findProperty("bintrayKey")
            publish = true
            setPublications("default")
            pkg(delegateClosureOf<com.jfrog.bintray.gradle.BintrayExtension.PackageConfig> {
                repo = "jGrouse"
                name = publishedArtifactId
                userOrg = "jgrouse"
                vcsUrl = "https://github.com/driabtchik/jgrouse.git"
                setLabels("java")
                setLicenses("Apache-2.0")
            })
        }
    }

}

tasks.register("artifactsUpload") {
}

tasks.register("ciBuild") {
    dependsOn("artifactsUpload")
}


fun artifactUploadAllowed(): Boolean {
    val grGit = org.ajoberstar.grgit.Grgit.open(mapOf("dir" to project.rootDir))
    val isClean = grGit.status().isClean
    return (isClean && !scmVersion.version.contains("SNAPSHOT"))
}

