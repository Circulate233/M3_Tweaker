
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
    testImplementation("it.unimi.dsi:fastutil:8.5.18")
}

tasks.test {
    javaLauncher.set(javaToolchains.launcherFor { languageVersion.set(JavaLanguageVersion.of(8)) })
    val testRunDirectory = layout.buildDirectory.dir("test-run")
    workingDir(testRunDirectory)
    doFirst { testRunDirectory.get().asFile.mkdirs() }
}
