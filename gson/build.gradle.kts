plugins {
    `maven-publish`
    id("convention.publication")
}

dependencies {
    implementation("com.google.code.gson:gson:2.9.1")

    implementation(project(":common"))
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }
}