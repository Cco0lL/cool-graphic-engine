package cool.kolya.implementation.scene.element;

public class ContextElement extends ParentImpl {

    protected WindowSettingsInterpreter windowSettingsInterpreter;

    public void updateState() {
        windowSettingsInterpreter.update();
        transformMatrix.setDirty(true);
    }

    public WindowSettingsInterpreter getWindowSettingsInterpreter() {
        return windowSettingsInterpreter;
    }

    public void setWindowSettingsInterpreter(WindowSettingsInterpreter windowSettingsInterpreter) {
        this.windowSettingsInterpreter = windowSettingsInterpreter;
        windowSettingsInterpreter.update();
    }
}
