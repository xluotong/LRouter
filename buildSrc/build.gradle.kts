plugins {
    `kotlin-dsl`
}

buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
    }
}

repositories {
    google()
    mavenCentral()
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

dependencies {
    implementation(kotlin("stdlib-jdk7"))
    compileOnly(gradleApi())
}
