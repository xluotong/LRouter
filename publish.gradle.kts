plugins {
    `maven-publish`
}

val lrouter_version by rootProject

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
            version = "0.0.3"
            from(components["java"])
        }
    }
}