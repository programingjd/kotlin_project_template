import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
  repositories {
    jcenter()
  }
}

plugins {
  kotlin("jvm") version "1.3.41"
}

group = "com.example"
version = "1.0.0"

repositories {
  jcenter()
}

dependencies {
  ("kotlin").also {
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect"))
  }
  ("kotlinx" to "1.3.0-RC2").also {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${it.second}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:${it.second}")
  }
  ("junit5" to "5.5.1").also {
    testImplementation(kotlin("test-junit5"))
    testImplementation("org.junit.jupiter:junit-jupiter-api:${it.second}")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:${it.second}")
  }
}

configure<JavaPluginConvention> {
  sourceCompatibility = JavaVersion.VERSION_11
  targetCompatibility = JavaVersion.VERSION_11
}

tasks.withType<KotlinCompile> {
  kotlinOptions {
    jvmTarget = "11"
  }
}

tasks.withType<Test> {
  useJUnitPlatform()
  testLogging {
    events("passed", "skipped", "failed")
  }
}

tasks.register<JavaExec>("start") {
  dependsOn(tasks.named("classes"))
  debug = false
  group = "run"
  main = "com.example.Main"
  classpath = sourceSets["main"].runtimeClasspath
}

tasks.register<JavaExec>("debug") {
  dependsOn(tasks.named("classes"))
  debug = true
  group = "run"
  main = "com.example.Main"
  classpath = sourceSets["main"].runtimeClasspath
  jvmArgs("-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005")
}
