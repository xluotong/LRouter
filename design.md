## 设计&原理

1. 注解模块路由信息和Service信息；
2. APT收集模块路由，Service注解元信息并生成模块路由容器类并写入META-INF；
3. Gradle插件从META-INF文件读取注解信息和APT生成容器类信息，将信息写入一个类中；

