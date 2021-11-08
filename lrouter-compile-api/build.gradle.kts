plugins {
    kotlin("jvm")
    `maven-publish`
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

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "com.billbook.lib"
            artifactId = "lrouter-compile-api"
            version = "0.0.1"
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
