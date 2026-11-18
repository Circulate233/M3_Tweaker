
plugins {
    id("com.gtnewhorizons.gtnhconvention")
}

minecraft {
    extraRunJvmArguments.add("-noverify")
}

dependencies {
    compileOnly("org.jetbrains:annotations:26.0.1")
    compileOnly("org.projectlombok:lombok:1.18.32")
    annotationProcessor("org.projectlombok:lombok:1.18.32")
    testImplementation("junit:junit:4.13.2")
    // GTNHExtLib loads this at game startup; the Gradle test JVM does not run its coremod.
    testRuntimeOnly("it.unimi.dsi:fastutil:8.5.18")
}

tasks.test {
    val testRunDirectory = layout.buildDirectory.dir("test-run")
    workingDir(testRunDirectory)
    doFirst { testRunDirectory.get().asFile.mkdirs() }
}
