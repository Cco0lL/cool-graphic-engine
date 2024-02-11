package cool.kolya.implementation.scene.element;

public abstract class AbstractDrawingModule implements DrawingModule {

    protected Element boundedElement;
    protected boolean elementChanged;

    protected abstract boolean isNeedUpdate();

    public abstract void updateExact();

    @Override
    public Element boundedElement() {
        return boundedElement;
    }

    @Override
    public void setBoundedElement(Element element) {
        this.boundedElement = element;
        elementChanged = true;
    }

    @Override
    public void update() {
        boolean needUpdate = false;
        if (elementChanged) {
            needUpdate = true;
            elementChanged = false;
        }
        needUpdate |= isNeedUpdate();
        if (needUpdate) {
            updateExact();
        }
    }
}
