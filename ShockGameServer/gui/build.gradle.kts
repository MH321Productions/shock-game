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
    implementation("com.miglayout:miglayout-swing:11.4.2")
    implementation(project(":server"))

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(21)
}

application {
    mainClass = "io.github.mh321productions.shockgameserver.gui.MainKt"
}