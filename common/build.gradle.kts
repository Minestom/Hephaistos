plugins {
    `maven-publish`
}

dependencies {
    // https://mvnrepository.com/artifact/org.antlr/antlr4-runtime
    implementation("org.antlr:antlr4-runtime:4.8-1")
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

sourceSets.create("antlr") {
    java.srcDir("src/antlr/gen")
    java.srcDir("build/generated/source/apt/antlr")
}

sourceSets.main.get().compileClasspath += sourceSets.getByName("antlr").output + sourceSets.main.get().compileClasspath
sourceSets.main.get().runtimeClasspath += sourceSets.getByName("antlr").output + sourceSets.main.get().runtimeClasspath
