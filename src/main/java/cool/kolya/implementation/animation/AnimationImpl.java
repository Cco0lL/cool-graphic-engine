package cool.kolya.implementation.animation;

import cool.kolya.engine.Engine;
import cool.kolya.implementation.scene.element.Element;
import cool.kolya.implementation.scene.element.property.Property;
import cool.kolya.implementation.scene.element.property.PropertyVector;
import cool.kolya.implementation.scene.element.property.TransformProperties;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class AnimationImpl implements Animation {

    private final List<ChangeProperty> changePropertyList = new ArrayList<>();
    private final int startTickTime = Engine.getContextProcess().currentTick();
    private final Element element;
    private final Easing easing;
    private final int duration;
    private boolean done;

    public AnimationImpl(Element element, Easing easing, int duration,
                         Animation.ChangeType changeType, TransformProperties changeProperties) {
        this.element = element;
        this.easing = easing;
        this.duration = duration;
        TransformProperties elementProperties = element.getProperties();
        for (int i = 0; i < Property.LENGTH; i++) {
            Property property = Property.get(i);
            if (!changeProperties.isPropertyDirty(property)) {
                continue;
            }
            Vector3f changeValueVec = changeProperties.getProperty(property).toVector3f();
            Vector3f startValueVec = elementProperties.getProperty(property).toVector3f();
            changePropertyList.add(new ChangeProperty(property, startValueVec, changeValueVec,
                    changeType));
        }
    }

    @Override
    public void update(int currentTime) {
        float progress = (float) (currentTime - startTickTime) / duration;
        if (progress > 1.0) {
            done = true;
            return;
        }
        progress = (float) easing.ease(progress);
        for (ChangeProperty changeProperty : changePropertyList) {
            PropertyVector propertyVector = element.getProperties()
                    .getProperty(changeProperty.getProperty());

            Vector3f outerChangesVec = propertyVector.toVector3f()
                    .sub(changeProperty.getLastValueVec());

            Vector3f newValue = outerChangesVec
                    .add(changeProperty.getStartValueVec())
                    .add(changeProperty.getDifferenceVec().mul(progress, new Vector3f()));

            propertyVector.set(newValue);
            changeProperty.getLastValueVec().set(newValue);
        }
    }

    @Override
    public boolean isDone() {
        return done;
    }

    public static class ChangeProperty {

        private final Property property;
        private final Vector3f startValueVec;
        private final Vector3f resultValueVec;
        private final Vector3f differenceVec;
        private final Vector3f lastValueVec = new Vector3f();

        public ChangeProperty(Property property, Vector3f startValueVec,
                              Vector3f changeValueVec, Animation.ChangeType changeType) {
            this.property = property;
            this.startValueVec = startValueVec;
            if (changeType == Animation.ChangeType.SET) {
                this.resultValueVec = changeValueVec;
                this.differenceVec = resultValueVec.sub(startValueVec, new Vector3f());
            } else {
                this.differenceVec = changeValueVec;
                this.resultValueVec = startValueVec.add(differenceVec, new Vector3f());
            }
            lastValueVec.set(startValueVec);
        }

        public Property getProperty() {
            return property;
        }

        public Vector3f getLastValueVec() {
            return lastValueVec;
        }

        public Vector3f getStartValueVec() {
            return startValueVec;
        }

        public Vector3f getDifferenceVec() {
            return differenceVec;
        }
    }
}
