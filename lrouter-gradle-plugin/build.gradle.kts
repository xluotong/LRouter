plugins {
    `kotlin-dsl`
    `maven-publish`
    kotlin("jvm")
}

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

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "com.billbook.gradle.plugin"
            artifactId = "lrouter"
            version = "0.0.13"
            from(components["java"])
        }
    }

    repositories {
        maven {
            // change to point to your repo, e.g. http://my.org/repo
            url = uri("../repo")
        }
    }
}

