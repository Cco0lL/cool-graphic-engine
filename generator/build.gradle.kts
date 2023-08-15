plugins {
    id("java")
    kotlin("jvm") version "1.9.0"
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    // https://mvnrepository.com/artifact/com.squareup/javapoet
    implementation("com.squareup", "javapoet", "1.13.0")
    // https://mvnrepository.com/artifact/com.google.guava/guava
    implementation("com.google.guava:guava:32.1.1-jre")
}

kotlin {
    jvmToolchain(11)
}