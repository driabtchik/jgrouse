group 'com.jgrouse'
version '0.1.0-SNAPSHOT'

apply plugin: 'java-library'

sourceCompatibility = 1.8

repositories {
    mavenCentral()
    jcenter()
}

test {
    useJUnitPlatform()
}

dependencies {
    implementation group: 'javax.validation', name: 'validation-api', version: '2.0.1.Final'
    implementation group: 'org.apache.commons', name: 'commons-lang3', version: '3.9'

    testImplementation group: 'org.junit.jupiter', name: 'junit-jupiter-api', version: '5.6.0'
    testRuntimeOnly group: 'org.junit.jupiter', name: 'junit-jupiter-engine', version: '5.6.0'

    testImplementation group: 'org.assertj', name: 'assertj-core', version: '3.15.0'
    testImplementation group: 'org.mockito', name: 'mockito-core', version: '3.3.0'

}
