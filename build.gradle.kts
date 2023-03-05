val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val exposed_version : String by project
val h2_version : String by project

plugins {

    kotlin("jvm") version "1.8.10"
    id("io.ktor.plugin") version "2.2.4"
                id("org.jetbrains.kotlin.plugin.serialization") version "1.8.10"

}

group = "com.silverbullet"
version = "0.0.1"

application {
    mainClass.set("io.ktor.server.netty.EngineMain")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

dependencies {

    // Ktor Core
    implementation("io.ktor:ktor-server-core-jvm:$ktor_version")

    // Ktor Engine
    implementation("io.ktor:ktor-server-netty-jvm:$ktor_version")

    // Ktor Auth
    implementation("io.ktor:ktor-server-auth-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-auth-jwt-jvm:$ktor_version")

    // Ktor Websockets
    implementation("io.ktor:ktor-server-websockets-jvm:$ktor_version")

    // Ktor Json
    implementation("io.ktor:ktor-server-content-negotiation-jvm:$ktor_version")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm:$ktor_version")

    // Ktor Default Headers
    implementation("io.ktor:ktor-server-default-headers-jvm:$ktor_version")

    // Ktor Status Pages
    implementation("io.ktor:ktor-server-status-pages-jvm:$ktor_version")
    implementation("ch.qos.logback:logback-classic:$logback_version")

    // Ktor Logging
    implementation("io.ktor:ktor-server-call-logging-jvm:$ktor_version")

    // Ktor Utils ( not sure what this dep is used for yet)
    implementation("io.ktor:ktor-server-host-common-jvm:$ktor_version")

    // Exposed
    implementation("org.jetbrains.exposed:exposed-core:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposed_version")

    // h2 database
    implementation("com.h2database:h2:$h2_version")

    // Ktor Testing
    testImplementation("io.ktor:ktor-server-tests-jvm:$ktor_version")

    // Kotlin Testing
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
}