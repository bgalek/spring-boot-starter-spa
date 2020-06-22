import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    `java-library`
    `maven-publish`
    signing
    id("org.springframework.boot") version "2.3.1.RELEASE"
    id("io.spring.dependency-management") version "1.0.9.RELEASE"
    id("com.adarshr.test-logger") version "2.0.0"
    id("pl.allegro.tech.build.axion-release") version "1.11.0"
}

group = "com.github.bgalek.spring.boot"
version = scmVersion.version

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
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
    mainClassName = "com.github.bgalek.spring.boot.starter.spa.autoconfigure.SinglePageAppController"
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

tasks.register<Jar>("sourcesJar") {
    from(sourceSets.main.get().allJava)
    archiveClassifier.set("sources")
}

publishing {
    publications {
        create<MavenPublication>("sonatype") {
            artifactId = "spring-boot-starter-spa"
            from(components["java"])
            artifact(tasks["sourcesJar"])
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
        repositories {
            maven {
                credentials {
                    username = rootProject.properties["ossrhUsername"] as String?
                    password = rootProject.properties["ossrhPassword"] as String?
                }
                val releasesRepoUrl = uri("https://oss.sonatype.org/service/local/staging/deploy/maven2/")
                val snapshotsRepoUrl = uri("https://oss.sonatype.org/content/repositories/snapshots/")
                url = if (version.toString().endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl
            }
        }
    }
}

signing {
    useGpgCmd()
    sign(publishing.publications["sonatype"])
}
