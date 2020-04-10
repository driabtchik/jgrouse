val artifactId = "jgrouse-utils"

ext["publishedArtifactId"] = artifactId
ext["publishedArtifactName"] = "jGrouse utilities"
ext["publishedArtifactDesc"] = "Useful utilities from jGrouse"

apply (
    from = "$rootDir/gradle/scripts/publishing.gradle.kts"
)

fun findProperty(s: String) = project.findProperty(s) as String?

bintray {
    user = findProperty("bintrayUser")
    key = findProperty("bintrayKey")
    publish = true
    setPublications("default")
    pkg(delegateClosureOf<com.jfrog.bintray.gradle.BintrayExtension.PackageConfig>{
        repo = "jGrouse"
        name = artifactId
        userOrg = "jgrouse"
        vcsUrl = "https://github.com/driabtchik/jgrouse.git"
        setLabels("java")
        setLicenses("Apache-2.0")
    })
}
