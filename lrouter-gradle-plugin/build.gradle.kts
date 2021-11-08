plugins {
    `kotlin-dsl`
    kotlin("jvm")
}

apply(from = "../publish.gradle.kts")

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

val android_plugin_version: String by project

dependencies {
    implementation(kotlin("stdlib-jdk7"))
    implementation(project(":lrouter-api"))
    implementation(project(":lrouter-compile-api"))
    implementation("org.ow2.asm:asm:9.1")
    implementation("org.ow2.asm:asm-commons:9.1")
    implementation("com.android.tools.build:gradle:$android_plugin_version")
    compileOnly(kotlin("gradle-plugin"))
    compileOnly(gradleApi())
}

