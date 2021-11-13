plugins {
    java
    kotlin("jvm")
}

dependencies {
    implementation(kotlin("stdlib-jdk7"))
    api("javax.inject:javax.inject:1@jar")
    api("javax.annotation:jsr250-api:1.0")
    compileOnly("com.google.android:android:4.1.1.4")
    compileOnly(project(":lrouter-stub-androidx"))
}

apply(from = "../maven_publish.gradle")