plugins {
    `kotlin-dsl`
    kotlin("jvm")
    `maven-publish`
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

val android_plugin_version: String by project

dependencies {
    implementation(kotlin("stdlib-jdk7"))
    embeddedKotlin(project(":lrouter-api"))
    embeddedKotlin(project(":lrouter-compile-api"))
    implementation("org.ow2.asm:asm:9.1")
    implementation("org.ow2.asm:asm-commons:9.1")
    implementation("com.android.tools.build:gradle:$android_plugin_version")
    compileOnly(kotlin("gradle-plugin"))
    compileOnly(gradleApi())
}

publishing {
    //配置maven仓库
    repositories {
        maven {
            //当前项目根目录
            url = uri("../repo")
        }
    }
    publications {
        create<MavenPublication>("maven") {
            groupId = "com.billbook.gradle.plugin"
            artifactId = "lrouter"
            version = "0.0.2"
            from(components["java"])
        }
    }
}
