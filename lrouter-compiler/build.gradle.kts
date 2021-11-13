plugins {
    kotlin("jvm")
    kotlin("kapt")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

dependencies {
    implementation(kotlin("stdlib-jdk7"))
    implementation(project(":lrouter-api"))
    implementation(project(":lrouter-compile-api"))
    implementation("com.squareup:javapoet:1.13.0")
    compileOnly("com.google.auto.service:auto-service:1.0-rc7")
    kapt("com.google.auto.service:auto-service:1.0-rc7")
    implementation("com.squareup.okio:okio:2.2.1")
}

apply(from = "../maven_publish.gradle")