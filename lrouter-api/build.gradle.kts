plugins {
    java
    kotlin("jvm")
}

sourceSets {
    main {
        java {
            exclude("com/billbook/lib/router/internal/Modules.java")
        }
    }
}

dependencies {
    implementation(kotlin("stdlib-jdk7"))
    api("javax.inject:javax.inject:1@jar")
}