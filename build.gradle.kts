plugins {
    kotlin("jvm") version "1.9.0"
    id("maven-publish")
}

group = "studio.hcmc"
version = "0.0.12"

repositories {
    mavenCentral()
    maven { setUrl("https://jitpack.io") }
}

kotlin {
    jvmToolchain(17)
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "studio.hcmc"
            artifactId = "ktor-client-protocol-extension"
            version = "0.0.12"
            from(components["java"])
        }
    }
}

dependencies {
    implementation("com.github.hcmc-studio:kotlin-protocol-extension:0.0.4-release")

    implementation("io.ktor:ktor-client-core:2.3.2")
}