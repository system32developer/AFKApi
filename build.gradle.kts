plugins {
    `java-library`
    kotlin("jvm") version "2.2.0"
    id("com.google.devtools.ksp") version "2.2.0-2.0.2"
    id("com.vanniktech.maven.publish") version "0.34.0"
    id("com.system32dev.autoversion") version "1.0.0"
}

group = "com.system32dev.afkapi"


autoversion {
    owner = "system32developer"
    repo = "AFKApi"
}

repositories {
    mavenCentral()
    maven("https://maven.enginehub.org/repo/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.1-R0.1-SNAPSHOT")
    compileOnly("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
}

kotlin {
    jvmToolchain(21)
}

tasks.register<Jar>("sourcesJar") {
    archiveClassifier.set("sources")
    from(sourceSets.main.get().allSource)
}

tasks.javadoc {
    options.encoding = "UTF-8"
    (options as StandardJavadocDocletOptions).apply {
        addStringOption("Xdoclint:none", "-quiet")
        addStringOption("sourcepath", "src/main/java")
    }
}

afterEvaluate {
    mavenPublishing {
        coordinates(group.toString(), name, project.version.toString())
        pom {
            name.set("AFKApi")
            description.set("A library to help you handle AFK interactions from a player.")
            inceptionYear.set("2025")
            url.set("https://github.com/system32developer/AFKApi")
            licenses {
                license {
                    name.set("The Apache License, Version 2.0")
                    url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                    distribution.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                }
            }
            developers {
                developer {
                    id.set("system32developer")
                    name.set("System32")
                    url.set("https://github.com/system32developer")
                }
            }
            scm {
                url.set("https://github.com/system32developer/AFKApi")
                connection.set("scm:git:git://github.com/system32developer/AFKApi.git")
                developerConnection.set("scm:git:ssh://git@github.com/system32developer/AFKApi.git")
            }
        }
    }
}

