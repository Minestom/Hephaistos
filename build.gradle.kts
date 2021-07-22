plugins {
    kotlin("jvm") version "1.5.10"
    java
    `maven-publish`
}

group = "org.jglrxavpok.nbt"
version = "1.1.8"

tasks {
    test { useJUnitPlatform() }
}

allprojects {
    apply(plugin = "java")
    apply(plugin = "kotlin")

    repositories {
        mavenCentral()
    }

    dependencies {
        // Use the Kotlin JDK 8 standard library.
        implementation(kotlin("stdlib"))

        // Use the JUpiter test library.
        testImplementation("org.junit.jupiter:junit-jupiter:5.7.2")
    }
}

dependencies {
    // https://mvnrepository.com/artifact/org.antlr/antlr4-runtime
    api("org.antlr:antlr4-runtime:4.8-1")
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

val compileKotlin: org.jetbrains.kotlin.gradle.tasks.KotlinCompile by tasks
compileKotlin.kotlinOptions.jvmTarget = JavaVersion.VERSION_11.toString()

compileKotlin.kotlinOptions {
    freeCompilerArgs = listOf("-Xuse-experimental=kotlin.MultiPlatform")
}

configurations {
    testImplementation {
        extendsFrom(configurations.compileOnly.get())
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }
}