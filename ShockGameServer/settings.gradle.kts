plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}
rootProject.name = "ShockGameServer"
include("server")
include("gui")
include("cli")
