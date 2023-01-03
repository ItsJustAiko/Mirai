import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.21"
    application
}

group = "me.aikoo"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))

    // JDA and Logger
    implementation("net.dv8tion:JDA:5.0.0-alpha.9")
    implementation("ch.qos.logback:logback-classic:1.4.5")
    implementation("ch.qos.logback:logback-core:1.4.5")
    implementation("org.reflections:reflections:0.10.2")
    implementation("com.google.code.gson:gson:2.10")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

application {
    mainClass.set("MainKt")
}