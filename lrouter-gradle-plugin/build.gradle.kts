plugins {
    kotlin("jvm")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

dependencies {
    implementation(kotlin("stdlib-jdk7"))
    implementation(project(":lrouter-api"))
    implementation(project(":lrouter-compile-api"))
    implementation("org.ow2.asm:asm:9.1")
    implementation("org.ow2.asm:asm-commons:9.1")
    implementation("com.android.tools.build:gradle:4.2.2")
    compileOnly(gradleApi())
}