plugins {
    kotlin("jvm")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

dependencies {
    api("com.google.code.gson:gson:2.8.8")
    compileOnly("javax.annotation:jsr250-api:1.0")
    api(project(":lrouter-api"))
}