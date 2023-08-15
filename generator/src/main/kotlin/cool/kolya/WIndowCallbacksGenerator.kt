package cool.kolya

import com.google.common.reflect.ClassPath
import com.squareup.javapoet.*
import org.lwjgl.glfw.GLFW
import org.lwjgl.system.CallbackI
import javax.lang.model.element.Modifier

class WIndowCallbacksGenerator : CodeGenerator() {

    companion object {
        //not window callbacks
        val globalCallbacks = arrayOf("Error", "Joystick", "Monitor")
    }

    override fun generate() {
        val callbackInfoSet = ClassPath.from(Thread.currentThread().contextClassLoader)
                .getTopLevelClasses(GLFW::class.java.packageName)
                .filter {
                    it.simpleName.contains("CallbackI")
                }.map {
                    @Suppress("UNCHECKED_CAST")
                    WIndowCallbackInfo(it, Class.forName(it.name) as Class<out CallbackI>)
                }.filter {
                    GLFW::class.java.methods.firstOrNull { m -> m.name == it.setFunctionName } != null &&
                            !globalCallbacks.contains(it.name)
                }
                .toSet()

        val fields = callbackInfoSet.map {
            FieldSpec.builder(
                    ParameterizedTypeName.get(ClassName.get(Class::class.java), TypeName.get(it.jClass)),
                    it.name, Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                    .initializer("${it.classInfo.simpleName}.class")
                    .build()
        }

        val initMethodBuilder = MethodSpec.methodBuilder("initialize")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addParameter(ParameterSpec.builder(
                        ClassName.get("", "WindowCallbackListener"),
                        "callbackListener")
                        .build())
                .addParameter(ParameterSpec.builder(TypeName.LONG, "windowPointer").build())

        val glfwType = ClassName.get(GLFW::class.java)
        callbackInfoSet.forEach {
            val parameters = it.jClass.declaredMethods.first { m -> m.name == "invoke" }
                    .parameters.joinToString(", ") { p -> p.name }

            initMethodBuilder.addCode(CodeBlock.builder()
                    .addStatement("""$1T.glfwSet$2LCallback(windowPointer, ((${parameters}) ->
                        |    callbackListener.listenCallback($2L, handler -> handler.invoke(${parameters}))))"""
                            .trimMargin(), glfwType, it.name).build()
            )
        }

        val callbackClass = TypeSpec.classBuilder("Callbacks")
                .addModifiers(Modifier.PUBLIC)
                .addFields(fields)
                .addMethod(initMethodBuilder.build())
                .build()

        writeFile(callbackClass, EngineDirectPath, "$EngineDirectPackage.engine.window.callback")
    }

}