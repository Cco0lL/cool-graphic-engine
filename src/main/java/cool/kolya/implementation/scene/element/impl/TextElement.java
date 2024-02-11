package cool.kolya.implementation.scene.element.impl;


import cool.kolya.implementation.scene.element.Elements;
import cool.kolya.implementation.scene.element.ParentImpl;
import cool.kolya.implementation.scene.element.property.PropertyVector;
import cool.kolya.implementation.scene.element.property.TransformProperties;
import org.joml.Vector4f;

public class TextElement extends ParentImpl {

    private CharElement[] textCharacters = new CharElement[0];
    protected final Vector4f textColor = new Vector4f(1f);

    public void setText(String text) {
        char[] chars = text.toCharArray();
        int length = chars.length;
        CharElement[] newCharacters = new CharElement[length];

        float width = 0f;
        float maxHeight = 0f;
        float lowestOffset = 0f;

        for (CharElement charElement : textCharacters) {
            removeChild(charElement.getId());
        }

        for (int i = 0; i < length; i++) {
            CharElement charElement = Elements.newCharElement(chars[i]);
            charElement.setColor(textColor);

            TransformProperties properties = charElement.getProperties();

            PropertyVector size = properties.getSize();

            properties.getOrigin().set(0f, 0f);
            properties.getAlign().set(0f, 1f);

            float yOffset = charElement.getCharacter().getYOffset();

            properties.getOffset().set(width, yOffset);

            addChild(charElement);
            newCharacters[i] = charElement;

            width += charElement.getCharacter().getXAdvance();
            if (maxHeight < size.y()) {
                maxHeight = size.y();
            }
            if (charElement.getCharacter().getXOffset() < lowestOffset) {
                lowestOffset = yOffset;
            }
        }

        textCharacters = newCharacters;
        properties.getSize().set(width, 2 * maxHeight + lowestOffset);
        for (CharElement charElement : textCharacters) {
            charElement.getProperties().getOffset().add(0f, - (maxHeight + lowestOffset));
        }
    }

    public Vector4f getTextColor() {
        return textColor;
    }

    public void setTextColor(Vector4f color) {
        color.set(color);
        for (CharElement charElement : textCharacters) {
            charElement.setColor(color);
        }
    }
}
