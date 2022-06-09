plugins {
    `maven-publish`
    id("convention.publication")
}

dependencies {
    // https://mvnrepository.com/artifact/org.antlr/antlr4-runtime
    api("org.antlr:antlr4-runtime:4.10.1")
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }
}