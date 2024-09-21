plugins {
    kotlin("jvm") version "2.0.20"
}

group = "io.github.MH321Productions.ShockGameServer"
version = properties["mainVersion"] as String

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}