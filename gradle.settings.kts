import com.diffplug.gradle.spotless.SpotlessPlugin
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.gradle.api.tasks.wrapper.Wrapper
import org.jetbrains.dokka.gradle.DokkaTask

buildscript {
    repositories {
        mavenCentral()
        jcenter()
    }
    dependencies {
        classpath("org.jetbrains.dokka:dokka-gradle-plugin:0.9.13")    
    }
}

plugins {
    id("com.diffplug.gradle.spotless") version "3.7.0"
    id("org.jetbrains.kotlin.jvm") version "1.2.0"
}

apply {
    plugin("kotlin")
    plugin("org.jetbrains.dokka")
}

group = "io.github.carlomicieli"
version = "1.0.0-SNAPSHOT"

configure<JavaPluginConvention> {
    setSourceCompatibility(1.8)
    setTargetCompatibility(1.8)
}

spotless {
    kotlin {
        ktlint()
        licenseHeaderFile("HEADER")
        trimTrailingWhitespace()
        indentWithSpaces()
        endWithNewline()
    }
}

tasks {
    withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = "1.8"
            freeCompilerArgs = listOf("-Xjsr305=strict")
        }
    }

    withType<Test> {
        testLogging {
            exceptionFormat = TestExceptionFormat.FULL
        }
    }

    withType<DokkaTask> {
        includes = listOf("README.md")
        outputFormat = "html"
        outputDirectory = "$buildDir/javadoc"
    }
}

repositories {
    mavenCentral()
}

dependencies {
    compile("org.jetbrains.kotlin:kotlin-stdlib")
    compile("org.jetbrains.kotlin:kotlin-stdlib")

    testCompile("io.kotlintest:kotlintest:2.0.5")
}

task(name = "wrapper", type = Wrapper::class) {
    gradleVersion = "4.4"
    distributionUrl = "http://services.gradle.org/distributions/gradle-$gradleVersion-bin.zip"
}
