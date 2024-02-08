package cool.kolya.engine.opengl.text;

import org.lwjgl.stb.STBTTAlignedQuad;

public class Character {

    private final char c;
    private final float width, height;
    private final float xOffset, yOffset, xAdvance;
    private final float[] texVertices;

    public Character(char c, STBTTAlignedQuad alignedQuad, float xAdvance) {
        this.c = c;

        this.xOffset = alignedQuad.x0();
        this.yOffset = alignedQuad.y0();
        this.xAdvance = xAdvance;

        width = alignedQuad.x1() - xOffset;
        height = alignedQuad.y1() - yOffset;

        float s0 = alignedQuad.s0(), t0 = alignedQuad.t0(),
              s1 = alignedQuad.s1(), t1 = alignedQuad.t1();

        texVertices = new float[] {
                s0, t0,
                s1, t0,
                s1, t1,
                s1, t1,
                s0, t1,
                s0, t0,
        };
    }

    public char getC() {
        return c;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    //IMPORTANT
    public float getXAdvance() {
        return xAdvance;
    }

    public float getXOffset() {
        return xOffset;
    }

    public float getYOffset() {
        return yOffset;
    }

    public float[] getTexVertices() {
        return texVertices;
    }
}
