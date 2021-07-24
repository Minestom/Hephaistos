plugins {
    kotlin("jvm") version "1.5.10"
    java
    `maven-publish`
}

group = "org.jglrxavpok.nbt"
version = "2.0.0"

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

    tasks {
        test { useJUnitPlatform() }
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
}