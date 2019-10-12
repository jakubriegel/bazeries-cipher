import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.3.50"
    id("com.github.johnrengelman.shadow") version "5.1.0"
}

group = "eu.jrie.put.pod"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    // kotlin
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect"))
    compile(kotlin("script-runtime"))

    // algorithm
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.2")
    implementation("pl.allegro.finance:tradukisto:1.8.0")

    // gui
    implementation("no.tornado:tornadofx:1.7.17")
    implementation("org.openjfx:javafx:13")

    // test
    testImplementation("org.junit.jupiter:junit-jupiter:5.5.0")
    testImplementation("io.mockk:mockk:1.9.3")
}

tasks {
    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "1.8"
    }
    withType<Test> {
        useJUnitPlatform()
    }
    shadowJar {
        setProperty("archiveBaseName", "bazeries")
        setProperty("archiveClassifier", "")
        setProperty("version", "")
        mainClasses
        manifest {
            attributes("Main-Class" to "eu.jrie.put.pod.bazeries.AppKt")
        }
    }
}
