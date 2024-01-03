import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.buildpack.platform.build.PullPolicy.IF_NOT_PRESENT
import org.springframework.boot.gradle.tasks.bundling.BootBuildImage

plugins {
    id("org.springframework.boot") version "3.2.1"
    id("io.spring.dependency-management") version "1.1.4"
    kotlin("jvm") version "1.9.21"
    kotlin("plugin.spring") version "1.9.21"
}

group = "che.vv"
version = "1.0"

repositories {
    mavenCentral()
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

dependencies {
    //Spring
    implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    //Liquibase
    implementation("org.liquibase:liquibase-core:4.24.0")
    //Postgres
    runtimeOnly("org.postgresql:postgresql")

    //CSV
    implementation("com.opencsv:opencsv:5.9")

    //Logging
    implementation("io.github.microutils:kotlin-logging-jvm:3.0.5")

    //Kotlin
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    //Test
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs += "-Xjsr305=strict"
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<BootBuildImage>{
    imageName.set("vchetverikov/social-network-mono:latest")
    pullPolicy.set(IF_NOT_PRESENT)
}