plugins {
    kotlin("jvm") version "2.1.21"
    application
}

group = "kitchen.ratatouille"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("ai.koog:koog-agents:0.3.0")
    implementation("org.slf4j:slf4j-simple:2.0.9")

}

application {
    mainClass.set("kitchen.ratatouille.MainKt")
}

tasks.test {
    useJUnitPlatform()
}

tasks.jar {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    manifest {
        attributes("Main-Class" to "kitchen.ratatouille.MainKt")
    }
    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
}

kotlin {
    jvmToolchain(23)
}