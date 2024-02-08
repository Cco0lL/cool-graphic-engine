package cool.kolya.implementation.scene.element.context2d;

import cool.kolya.implementation.scene.element.general.IPropertyVector2f;

public class TextElement extends RectangleElement {

    private CharElement[] textCharacters = new CharElement[0];

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
            CharElement charElement = new CharElement(chars[i]);

            DrawableProperties2D properties = charElement.getProperties();

            IPropertyVector2f size = properties.getSize();

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
}
