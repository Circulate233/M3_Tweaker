
plugins {
    id("com.gtnewhorizons.gtnhconvention")
}

dependencies {
    compileOnly("org.projectlombok:lombok:1.18.32")
    annotationProcessor("org.projectlombok:lombok:1.18.32")
    implementation("org.ow2.asm:asm-commons:9.2")
}
