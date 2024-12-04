import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "3.2.4"
    id("io.spring.dependency-management") version "1.1.4"
    kotlin("jvm") version "1.9.23"
    kotlin("plugin.spring") version "1.9.23"
}
val springCloudVersion by extra("2023.0.1")

group = "dev.loudbook"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_21
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
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("me.paulschwarz:spring-dotenv:4.0.0")
    implementation("commons-validator:commons-validator:1.8.0")
    implementation("com.amazonaws:aws-java-sdk-s3:1.12.717")
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:$springCloudVersion")
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs += "-Xjsr305=strict"
        jvmTarget = "21"
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
