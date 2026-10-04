plugins {
    java
    id("com.zeroc.slice-tools") version "3.8.3"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.zeroc:ice:3.8.3")
}

sourceSets {
    main {
        slice {
            srcDirs("../slice")
        }
    }
}

tasks.register<JavaExec>("runServer") {
    classpath = sourceSets.main.get().runtimeClasspath
    mainClass = "Server"
}

tasks.register<JavaExec>("runClient") {
    classpath = sourceSets.main.get().runtimeClasspath
    mainClass = "Client"
}
