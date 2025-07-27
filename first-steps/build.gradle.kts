plugins {
    kotlin("jvm") version "2.1.21"
    application
}

group = "first.steps"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("ai.koog:koog-agents:0.3.0")
    implementation("org.slf4j:slf4j-simple:2.0.9")
}

application {
    mainClass.set("first.steps.MainKt")
}


tasks.jar {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    manifest {
        attributes("Main-Class" to "first.steps.MainKt")
    }
    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
}

kotlin {
    jvmToolchain(23)
}