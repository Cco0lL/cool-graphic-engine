package cool.kolya;

import cool.kolya.engine.event.*;
import cool.kolya.engine.event.bus.EventBus;
import cool.kolya.implementation.TestElement;
import cool.kolya.implementation.TestPlane;
import cool.kolya.engine.opengl.shader.Shader;
import cool.kolya.engine.opengl.shader.ShaderProgram;
import cool.kolya.engine.opengl.uniform.Matrix4fUniform;
import cool.kolya.engine.opengl.uniform.Vector4fUniform;
import cool.kolya.engine.scene.Scene;
import cool.kolya.engine.util.ResourceUtil;
import org.lwjgl.glfw.GLFW;

public class Main {

    public static void main(String[] args) {
        Engine.initialize();

        ShaderProgram testProgram = testShaderProgram();

        TestPlane plane = new TestPlane(testProgram);
        plane.getColor().set(1f);

        TestElement first = new TestElement(testProgram);
        TestElement second = new TestElement(testProgram);

        first.getOffset().set(-1.0f, 0.0f, 0.0f);
        second.getOffset().set(1.0f, 0.0f, 0.0f);

        first.getColor().set(1f, (float) 215 / 255, 0f, 1.0f);
        second.getColor().set(0.5f,  0.5f, 0.5f, 1.0f);

        first.getRotation().z(-90f);
        second.getRotation().z(90f);
        plane.getRotation().x(-90);

        Scene scene = Engine.getScene();
        scene.addElement(plane);
        scene.addElement(first);
        scene.addElement(second);

        EventBus.getInstance().registerListener(new DebugListener());
        Engine.start();
    }

    public static ShaderProgram testShaderProgram() {
        try {
            ShaderProgram program = new ShaderProgram();

            Shader vertexShader = new Shader(ResourceUtil.readResource("cool/kolya/vertex_shader.vs"),
                    Shader.Type.VERTEX);
            Shader fragmentShader = new Shader(ResourceUtil.readResource("cool/kolya/fragment_shader.fs"),
                    Shader.Type.FRAGMENT);

            program.addShader(vertexShader);
            program.addShader(fragmentShader);

            program.link();

            program.createUniform("projectionMatrix", Matrix4fUniform::new);
            program.createUniform("incolor", Vector4fUniform::new);
            program.createUniform("elementMatrix", Matrix4fUniform::new);
            program.createUniform("cameraMatrix", Matrix4fUniform::new);
            return program;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

    public static class DebugListener implements EventListener {

        @EventHandler
        void handle(KeyTypeEvent keyTypeEvent) {
            System.out.println("typed");
        }

        @EventHandler
        void handle(ClickEvent clickEvent) {
            System.out.println("clicked");
        }

        @EventHandler
        void handle(CursorEnterEvent event) {
            System.out.println("cursor entered");
        }
    }
}