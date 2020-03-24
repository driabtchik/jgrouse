import org.gradle.api.JavaVersion.VERSION_1_8

plugins {
    `java-library`
    `maven-publish`
    jacoco
    id("pl.allegro.tech.build.axion-release") version "1.11.0"
    id("com.jfrog.bintray") version "1.8.4"
}

scmVersion {
}

group = "com.jgrouse"
version = scmVersion.version

java {
    sourceCompatibility = VERSION_1_8
    targetCompatibility = VERSION_1_8
    withSourcesJar()
    withJavadocJar()
}

repositories {
    jcenter()
    mavenCentral()
}


jacoco {
}

tasks.jacocoTestCoverageVerification {
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

tasks.test {
    useJUnitPlatform()
    extensions.configure(JacocoTaskExtension::class) {
    }
    finalizedBy(tasks.jacocoTestReport)
    finalizedBy(tasks.jacocoTestCoverageVerification)
}


dependencies {
    val junitVer = "5.6.0"
    implementation("javax.validation:validation-api:2.0.1.Final")
    implementation("org.apache.commons:commons-lang3:3.9")

    testImplementation("org.junit.jupiter:junit-jupiter-api:${junitVer}")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:${junitVer}")

    testImplementation("org.assertj:assertj-core:3.15.0")
    testImplementation("org.mockito:mockito-core:3.3.0")
}

publishing {
    publications {
        create<MavenPublication>("default") {
            groupId = project.group as String
            artifactId = "jgrouse-utils"
            version = project.version as String
            from(components["java"])
            pom.withXml {
                asNode().apply {
                    appendNode("name", "jGrouse Utils")
                    appendNode("description", "Useful utilities from jGrouse")
                    appendNode("url", "https://github.com/driabtchik/jgrouse-utils")
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
                        appendNode("connection", "scm:git:git://github.com/driabtchik/jgrouse-utils.git")
                        appendNode("developerConnection", "scm:git:ssh://github.com/driabtchik/jgrouse-utils.git")
                        appendNode("url", "https://github.com/driabtchik/jgrouse-utils")
                    }
                }
            }
        }
    }
}



fun findProperty(s: String) = project.findProperty(s) as String?

bintray {
    user = findProperty("bintrayUser")
    key = findProperty("bintrayKey")
    publish = true
    setPublications("default")
    pkg(delegateClosureOf<com.jfrog.bintray.gradle.BintrayExtension.PackageConfig>{
        repo = "jGrouse"
        name = "jgrouse-utils"
        userOrg = "jgrouse"
        vcsUrl = "https://github.com/driabtchik/jgrouse-utils.git"
        setLabels("java")
        setLicenses("Apache-2.0")
    })
}