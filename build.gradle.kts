import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.3.72"
    `java-gradle-plugin`
}

group = "cz.richter.david"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    compileOnly(group = "org.junit.jupiter", name = "junit-jupiter-engine", version = "5.3.1")
    compileOnly(group = "org.junit.platform", name = "junit-platform-launcher", version = "1.3.1")
    testImplementation(group = "org.junit.jupiter", name = "junit-jupiter-engine", version = "5.3.1")
    testImplementation(group = "org.junit.platform", name = "junit-platform-launcher", version = "1.3.1")
}

project.configurations.runtimeClasspath
gradlePlugin {
    plugins {
        create("distributed-test-runner-plugin") {
            id = "cz.richter.david.gradle.plugin.distributed-test-runner"
            implementationClass = "cz.richter.david.distributedTestRunner.DistributedTestRunnerPlugin"
        }
    }
}


tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}