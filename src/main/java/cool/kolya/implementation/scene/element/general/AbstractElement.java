package cool.kolya.implementation.scene.element.general;

import java.util.UUID;

public abstract class AbstractElement {

    protected final UUID id = UUID.randomUUID();

    public UUID getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractElement element = (AbstractElement) o;
        return id.equals(element.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
