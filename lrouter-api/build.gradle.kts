plugins {
    java
    kotlin("jvm")
    `maven-publish`
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
    compileOnly(project(":lrouter-stub-androidx"))
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
            groupId = "com.billbook.lib"
            artifactId = "lrouter"
            version = "0.0.1"
            from(components["java"])
        }
    }
}