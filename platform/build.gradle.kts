val artifactId = "jgrouse-platform"

ext["publishedArtifactId"] = artifactId
ext["componentsSource"] = "javaPlatform"
ext["publishedArtifactName"] = "jGrouse Bill of Materials"
ext["publishedArtifactDesc"] = "jGrouse Bill of Materials"

plugins {
    `java-platform`
}

javaPlatform {
    allowDependencies()
}

dependencies {
    api(platform("org.springframework:spring-framework-bom:5.2.5.RELEASE"))
    constraints {

        api("javax.validation:validation-api:2.0.1.Final")

        api("org.apache.commons:commons-lang3:3.10")

        val poiVersion = "4.0.0"
        api("org.apache.poi:poi:${poiVersion}")
        api("org.apache.poi:poi-ooxml:${poiVersion}")

        val sl4jVer = "1.7.25"
        api("org.slf4j:slf4j-api:${sl4jVer}")
        api("org.slf4j:log4j-over-slf4j:${sl4jVer}")


        val junitVer = "5.6.0"
        api("org.junit.jupiter:junit-jupiter-api:${junitVer}")
        api("org.junit.jupiter:junit-jupiter-engine:${junitVer}")
        api("org.junit.jupiter:junit-jupiter:${junitVer}")

        api("org.assertj:assertj-core:3.15.0")

        api("org.mockito:mockito-core:3.3.0")

        api("com.h2database:h2:1.4.200")
    }
}

apply(
        from = "$rootDir/gradle/scripts/publishing.gradle.kts"
)

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