import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    `java-library`
    `maven-publish`
    signing
    jacoco
    id("org.springframework.boot") version "3.3.5"
    id("io.spring.dependency-management") version "1.1.6"
    id("com.adarshr.test-logger") version "4.0.0"
    id("pl.allegro.tech.build.axion-release") version "1.18.16"
}

group = "com.github.bgalek.spring.boot"
version = scmVersion.version

java {
    withSourcesJar()
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

repositories {
    mavenCentral()
}

dependencies {
    api("org.springframework.boot:spring-boot-starter")
    api("org.springframework.boot:spring-boot-starter-web")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-starter-web")
    testImplementation("org.springframework.boot:spring-boot-starter-thymeleaf")
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<BootJar> {
    mainClass = "com.github.bgalek.spring.boot.starter.spa.autoconfigure.SinglePageAppController"
}

tasks.withType<Jar> {
    enabled = true
}

tasks.withType<AbstractArchiveTask> {
    setProperty("archiveBaseName", rootProject.name)
}

tasks.jar {
    manifest {
        attributes(mapOf("Implementation-Title" to rootProject.name, "Implementation-Version" to rootProject.name))
    }
}

publishing {
    publications {
        create<MavenPublication>("sonatype") {
            artifactId = "spring-boot-starter-spa"
            from(components["java"])
            versionMapping {
                usage("java-api") {
                    fromResolutionOf("runtimeClasspath")
                }
                usage("java-runtime") {
                    fromResolutionResult()
                }
            }
            pom {
                name.set("spring-boot-starter-spa")
                description.set("Zero configuration single page app configuration for spring boot")
                url.set("https://github.com/bgalek/spring-boot-starter-spa")
                licenses {
                    license {
                        name.set("The Apache License, Version 2.0")
                        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }
                developers {
                    developer {
                        id.set("bgalek")
                        name.set("Bartosz Gałek")
                        email.set("bartosz@galek.com.pl")
                    }
                    scm {
                        connection.set("scm:git:git://github.com/bgalek/spring-boot-starter-spa.git")
                        developerConnection.set("scm:git:ssh://github.com:bgalek/spring-boot-starter-spa.git")
                        url.set("https://github.com/bgalek/spring-boot-starter-spa")
                    }
                }
            }
        }
    }
    repositories {
        maven {
            credentials {
                username = System.getenv("SONATYPE_USERNAME")
                password = System.getenv("SONATYPE_PASSWORD")
            }
            val releasesRepoUrl = uri("https://oss.sonatype.org/service/local/staging/deploy/maven2/")
            val snapshotsRepoUrl = uri("https://oss.sonatype.org/content/repositories/snapshots/")
            url = if (version.toString().endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl
        }
    }
}

System.getenv("GPG_KEY_ID")?.let {
    signing {
        useInMemoryPgpKeys(
                System.getenv("GPG_KEY_ID"),
                System.getenv("GPG_PRIVATE_KEY"),
                System.getenv("GPG_PRIVATE_KEY_PASSWORD")
        )
        sign(publishing.publications)
    }
}
