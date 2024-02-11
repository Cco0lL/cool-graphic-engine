package cool.kolya.implementation.scene.element;

import org.joml.Vector2f;

public interface WindowSettingsInterpreter {

    void update();

    boolean isDirty();

    void setDirty(boolean dirty);

    float interpretWidth(float width);

    float interpretHeight(float height);

    float interpretDepth(float depth);

    Vector2f interpretWindowPos(Vector2f pos);

    class Default implements WindowSettingsInterpreter {

        @Override
        public void update() {}

        @Override
        public boolean isDirty() {
            return false;
        }

        public void setDirty(boolean dirty) {}

        @Override
        public float interpretWidth(float width) {
            return width;
        }

        @Override
        public float interpretHeight(float height) {
            return height;
        }

        @Override
        public float interpretDepth(float depth) {
            return depth;
        }

        @Override
        public Vector2f interpretWindowPos(Vector2f pos) {
            return pos;
        }
    }
}
