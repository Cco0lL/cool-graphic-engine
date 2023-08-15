package cool.kolya

import com.google.common.reflect.ClassPath.ClassInfo
import org.lwjgl.system.CallbackI

data class WIndowCallbackInfo(
        val classInfo: ClassInfo,
        val jClass: Class<out CallbackI>
) {

    val name = classInfo.simpleName.replace("CallbackI", "")
            .replace("GLFW", "")

    val setFunctionName = "glfwSet${name}Callback"
}