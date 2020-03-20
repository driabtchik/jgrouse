import org.gradle.api.JavaVersion.VERSION_1_8

plugins {
    `java-library`
}

group = "com.jgrouse"
version = "0.1.0-SNAPSHOT"

java {
    sourceCompatibility = VERSION_1_8
}

repositories {
    mavenCentral()
    jcenter()
}

tasks.test {
    useJUnitPlatform()
}

dependencies {
    implementation("javax.validation:validation-api:2.0.1.Final")
    implementation("org.apache.commons:commons-lang3:3.9")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.6.0")

    testImplementation("org.assertj:assertj-core:3.15.0")
    testImplementation("org.mockito:mockito-core:3.3.0")

}
