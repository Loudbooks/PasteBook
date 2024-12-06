import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "3.4.0"
    id("io.spring.dependency-management") version "1.1.6"
    kotlin("jvm") version "2.1.0"
    kotlin("plugin.spring") version "2.1.0"
}
val springCloudVersion by extra("2023.0.1")

group = "dev.loudbook"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_23
    targetCompatibility = JavaVersion.VERSION_23
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("com.github.vladimir-bukhtoyarov:bucket4j-core:8.0.1")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    implementation("com.google.code.gson:gson:2.11.0")
    implementation("me.paulschwarz:spring-dotenv:4.0.0")
    implementation("commons-validator:commons-validator:1.8.0")
    implementation("software.amazon.awssdk:s3:2.29.29")
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:$springCloudVersion")
    }
}

tasks.withType<KotlinCompile> {
    compilerOptions {
        freeCompilerArgs.add("-Xjsr305=strict")
        jvmTarget.set(JvmTarget.JVM_23)
    }
}

tasks.bootJar {
    archiveBaseName.set("pastebook")
    archiveVersion.set("")
    archiveClassifier.set("")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

configurations.implementation {
    exclude(group = "commons-logging", module = "commons-logging")
}