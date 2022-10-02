import java.util.Properties

plugins {
    `maven-publish`
    signing
}

// Stub secrets to let the project sync and build without the publication values set up
ext["signing.keyId"] = null
ext["signing.password"] = null
ext["signing.secretKeyRingFile"] = null
ext["ossrhUsername"] = null
ext["ossrhPassword"] = null

// Grabbing secrets from local.properties file or from environment variables, which could be used on CI
val secretPropsFile = project.rootProject.file("secrets.properties")
if (secretPropsFile.exists()) {
    secretPropsFile.reader().use {
        Properties().apply {
            load(it)
        }
    }.onEach { (name, value) ->
        ext[name.toString()] = value
    }
} else {
    ext["signing.keyId"] = System.getenv("SIGNING_KEY_ID")
    ext["signing.password"] = System.getenv("SIGNING_PASSWORD")
    ext["signing.secretKeyRingFile"] = System.getenv("SIGNING_SECRET_KEY_RING_FILE")
    ext["ossrhUsername"] = System.getenv("OSSRH_USERNAME")
    ext["ossrhPassword"] = System.getenv("OSSRH_PASSWORD")
}

/*val sourcesJar by tasks.registering(Jar::class) {
    archiveClassifier.set("sources")
}
val javadocJar by tasks.registering(Jar::class) {
    archiveClassifier.set("javadoc")
}*/

val javadocJar = tasks.named("javadocJar")

fun getExtraString(name: String) = ext[name]?.toString()

publishing {
    // Configure maven central repository
    repositories {
        maven {
            name = "sonatype"
            setUrl("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
            credentials {
                username = getExtraString("ossrhUsername")
                password = getExtraString("ossrhPassword")
            }
        }
    }

    // Configure all publications
    publications.withType<MavenPublication> {

        artifact(javadocJar.get())

        // Provide artifacts information requited by Maven Central
        pom {
            name.set("Hephaistos")
            description.set("NBT and MCA library")
            url.set("https://github.com/jglrxavpok/Hephaistos")

            licenses {
                license {
                    name.set("MIT")
                    url.set("https://opensource.org/licenses/MIT")
                }
            }
            developers {
                developer {
                    id.set("jglrxavpok")
                    name.set("Xavier Niochaut")
                    email.set("jglrxavpok@gmail.com")
                }
            }
            scm {
                url.set("https://github.com/jglrxavpok/Hephaistos")
            }

        }
    }
}

// Signing artifacts. Signing.* extra properties values will be used

// Gradle will throw an exception if the signing key is not found
if (ext["signing.keyId"] != null) {
    signing {
        sign(publishing.publications)
    }
}
