/*
 * This file was generated by the Gradle 'init' task.
 *
 * This generated file contains a sample Java application project to get you started.
 * For more details on building Java & JVM projects, please refer to https://docs.gradle.org/8.10/userguide/building_java_projects.html in the Gradle documentation.
 */

plugins {
    application
    java
    jacoco
    id("com.diffplug.spotless") version "7.0.0.BETA2"
}


repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
}

dependencies {
    // Use JUnit Jupiter for testing.
    testImplementation(libs.junit.jupiter)

    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    // This dependency is used by the application.
    implementation(libs.guava)
    implementation("org.jfree:jfreechart:1.5.3")
}

// Apply a specific Java toolchain to ease working on different environments.
java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

application {
    // Define the main class for the application.
    mainClass = "org.task_1_2.Main"
}


tasks.jacocoTestReport {
    val reportDir = file("../build/reports/jacoco/test")
    reports.xml.outputLocation = reportDir.resolve("jacocoTestReport.xml")
    reports {
        xml.required = true
    }

}

tasks.named<Test>("test") {
    // Use JUnit Platform for unit tests.
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport)
    finalizedBy(tasks.javadoc)
}


tasks.javadoc {
    destinationDir = file("../build/docs/javadoc")
}

tasks.named<JavaExec>("run") {
    standardInput = System.`in`
}

spotless {
    java {
        googleJavaFormat().aosp().reflowLongStrings().formatJavadoc(true).reorderImports(true)
    }
}