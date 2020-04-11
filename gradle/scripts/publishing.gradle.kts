
val Project.ext: ExtraPropertiesExtension
    get() =
        (this as ExtensionAware).extensions.getByName("ext") as ExtraPropertiesExtension


val publishedArtifactId = ext["publishedArtifactId"]
val publishedArtifactName = ext["publishedArtifactName"]
val publishedArtifactDesc = ext["publishedArtifactDesc"]


plugins.withType<MavenPublishPlugin> {
    extensions.configure<PublishingExtension> {
        repositories {
            maven {
                name = "GitHubPackages"
                url = uri("https://maven.pkg.github.com/driabtchik/jgrouse")
                credentials {
                    username = project.findProperty("gprUser") as String? ?: System.getenv("GITHUB_ACTOR")
                    password = project.findProperty("gprKey") as String? ?: System.getenv("GITHUB_TOKEN")
                }
            }
        }
        publications {
            create<MavenPublication>("default") {
                groupId = project.group as String
                artifactId = publishedArtifactId as String
                version = project.version as String
                from(components["java"])
                pom.withXml {
                    asNode().apply {
                        appendNode("name", publishedArtifactName)
                        appendNode("description", publishedArtifactDesc)
                        appendNode("url", "https://github.com/driabtchik/jgrouse")
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
                            appendNode("url", "https://github.com/driabtchik/jgrouse")
                        }
                    }
                }
            }
        }

    }
}
