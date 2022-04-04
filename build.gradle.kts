plugins {
    `java-library`
    kotlin("jvm") version "1.5.31"
    `maven-publish`
}

group = "eu.xiaoguang.lib.gson"
version = "0.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(kotlin("reflect"))
    api("com.google.code.gson:gson:2.9.0")
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.2")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}