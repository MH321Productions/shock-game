plugins {
    kotlin("jvm") version "2.0.20"
    id("application")
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

application {
    mainClass = "io.github.mh321productions.shockgameserver.cli.MainKt"
}