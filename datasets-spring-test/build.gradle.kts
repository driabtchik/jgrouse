val artifactId = "jgrouse-datasets-spring-test"

ext["publishedArtifactId"] = artifactId
ext["publishedArtifactName"] = "jGrouse Datasets for Spring Integration Testing"
ext["publishedArtifactDesc"] = "jGrouse Datasets for Spring Integration Testing"

apply(
        from = "$rootDir/gradle/scripts/publishing.gradle.kts"
)

dependencies {
    "implementation"(project(":utils"))
    "implementation"(project(":datasets"))

    testImplementation("com.h2database:h2")

}


fun findProperty(s: String) = project.findProperty(s) as String?

bintray {
    user = findProperty("bintrayUser")
    key = findProperty("bintrayKey")
    publish = true
    setPublications("default")
    pkg(delegateClosureOf<com.jfrog.bintray.gradle.BintrayExtension.PackageConfig> {
        repo = "jGrouse"
        name = artifactId
        userOrg = "jgrouse"
        vcsUrl = "https://github.com/driabtchik/jgrouse.git"
        setLabels("java")
        setLicenses("Apache-2.0")
    })
}
