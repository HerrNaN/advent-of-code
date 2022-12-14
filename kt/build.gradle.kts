import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.0"
    application
}

group = "org.example"
version = "1.0.0"

repositories {
    mavenCentral()
    maven(
        url = uri("https://jitpack.io")
    )
}

val ktorVersion = "2.1.3"
val arrow_version = "1.1.2"

dependencies {
    testImplementation(kotlin("test"))
    implementation("cc.ekblad.konbini:konbini:0.1.2")
    implementation("io.arrow-kt:arrow-core:$arrow_version")
    implementation("io.ktor:ktor-client-core:$ktorVersion")
    implementation("io.ktor:ktor-client-cio:$ktorVersion")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<Test> {
    this.testLogging {
        this.showStandardStreams = true
    }
}
tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "17"
}

tasks.withType<Jar> {
    duplicatesStrategy = DuplicatesStrategy.WARN

    manifest {
        attributes["Main-Class"] = "MainKt"
    }
    configurations["compileClasspath"].forEach { file: File ->
        from(zipTree(file.absoluteFile))
    }
}

application {
    mainClass.set("MainKt")
}
