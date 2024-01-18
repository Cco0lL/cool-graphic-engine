plugins {
    id("java")
}

allprojects {
    apply(plugin = "java")

    group = "cool.kolya"
    version = "1.0"

    //https://www.lwjgl.org/customize gradle dependencies generator

    val lwjglVersion = "3.3.2"
    val jomlVersion = "1.10.5"
    val lwjglNatives = "natives-windows"

    dependencies {
        implementation(platform("org.lwjgl:lwjgl-bom:$lwjglVersion"))

        implementation("org.lwjgl", "lwjgl")
        implementation("org.lwjgl", "lwjgl-assimp")
        implementation("org.lwjgl", "lwjgl-glfw")
        implementation("org.lwjgl", "lwjgl-openal")
        implementation("org.lwjgl", "lwjgl-opencl")
        implementation("org.lwjgl", "lwjgl-opengl")
        implementation("org.lwjgl", "lwjgl-stb")

        runtimeOnly("org.lwjgl", "lwjgl", classifier = lwjglNatives)
        runtimeOnly("org.lwjgl", "lwjgl-assimp", classifier = lwjglNatives)
        runtimeOnly("org.lwjgl", "lwjgl-glfw", classifier = lwjglNatives)
        runtimeOnly("org.lwjgl", "lwjgl-openal", classifier = lwjglNatives)
        runtimeOnly("org.lwjgl", "lwjgl-opengl", classifier = lwjglNatives)
        runtimeOnly("org.lwjgl", "lwjgl-stb", classifier = lwjglNatives)

        //libs
        implementation("org.joml", "joml", jomlVersion)
        implementation("org.slf4j", "slf4j-api", "2.0.7")
        implementation("org.slf4j","slf4j-log4j12","2.0.7")

        implementation("org.jetbrains","annotations","24.0.1")

        // https://mvnrepository.com/artifact/it.unimi.dsi/fastutil
        implementation("it.unimi.dsi:fastutil:8.5.12")

        testImplementation(platform("org.junit:junit-bom:5.9.1"))
        testImplementation("org.junit.jupiter:junit-jupiter")
    }

    tasks.test {
        useJUnitPlatform()
    }
}



