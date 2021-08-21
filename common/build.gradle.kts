import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `maven-publish`
    id("convention.publication")
}

dependencies {
    implementation(project(":antlr"))
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