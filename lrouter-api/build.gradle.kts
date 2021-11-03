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
    api("javax.annotation:jsr250-api:1.0")
    compileOnly("com.google.android:android:4.1.1.4")
}