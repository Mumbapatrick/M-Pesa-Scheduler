import org.jetbrains.kotlin.gradle.dsl.JvmTarget
plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    id("org.springframework.boot") version "3.5.4"
    id("io.spring.dependency-management") version "1.1.7"
    id("com.netflix.dgs.codegen") version "7.0.3"
    id("org.graalvm.buildtools.native") version "0.10.6"
}

group = "com.fintech"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

extra["springCloudGcpVersion"] = "7.1.0"
extra["springCloudVersion"] = "2025.0.0"

dependencies {
    // Spring Boot
    implementation("org.springframework.boot:spring-boot-starter-oauth2-authorization-server")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-web-services")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.session:spring-session-data-redis")

    // Kotlin
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    implementation("org.springframework.boot:spring-boot-starter-actuator")

    // Database
    runtimeOnly("org.postgresql:postgresql")

    // Development
    developmentOnly("org.springframework.boot:spring-boot-devtools")

    // ✅ Add this for Spring Boot test support
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }

    // Testing
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")

    // Constraint for GraphQL Java Dataloader
    constraints {
        implementation("com.graphql-java:java-dataloader:5.0.0") {
            because("graphql-java 24.1 requires java-dataloader 5.0.0")
        }
    }
}

dependencyManagement {
    imports {
        mavenBom("com.google.cloud:spring-cloud-gcp-dependencies:${property("springCloudGcpVersion")}")
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
        mavenBom("com.netflix.graphql.dgs:graphql-dgs-platform-dependencies:10.0.0")
    }
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
        jvmTarget.set(JvmTarget.JVM_17)
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

// Configure Spring Boot main class for GraalVM native
tasks.withType<org.springframework.boot.gradle.tasks.bundling.BootJar> {
    mainClass.set("com.fintech.ApplicationKt")
}

// GraalVM Native Image configuration
graalvmNative {
    binaries {
        named("main") {
            mainClass.set("com.fintech.ApplicationKt")
        }
    }
}
