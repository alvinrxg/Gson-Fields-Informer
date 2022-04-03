plugins {
    java
    kotlin("jvm") version "1.5.31"
}

group = "eu.xiaoguang.lib"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    api("com.google.code.gson:gson:2.9.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.0")
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.2")
    implementation(kotlin("reflect"))
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}