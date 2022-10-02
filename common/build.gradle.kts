plugins {
    `maven-publish`
    id("convention.publication")
}

dependencies {
    api(project(":antlr"))
    implementation("it.unimi.dsi:fastutil:8.5.8")
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