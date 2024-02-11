package cool.kolya.implementation.scene.element.property;

import cool.kolya.api.func.FloatBiFunction;

public class MergePropertyVector extends ElementPropertyVector {

    private float actualX, actualY, actualZ;
    private LinkablePropertyVector linkVec;
    private final FloatBiFunction mergeFunc;

    public MergePropertyVector(float d, ElementTransformProperties properties, int propertyOffset,
                               MergeStrategy mergeStrategy) {
        this(d, d, d, properties, propertyOffset, mergeStrategy);
    }

    public MergePropertyVector(float x, float y, float z, ElementTransformProperties properties,
                               int propertyOffset, MergeStrategy mergeStrategy) {
        super(x, y, z, properties, propertyOffset);
        actualX = x;
        actualY = y;
        actualZ = z;
        this.mergeFunc = mergeStrategy.getMergeFunc();
    }

    @Override
    public void x(float x) {
        actualX = x;
        super.x(getMergedX());
    }

    @Override
    public void y(float y) {
        actualY = y;
        super.y(getMergedY());
    }

    @Override
    public void z(float z) {
        actualZ = z;
        super.z(getMergedZ());
    }

    @Override
    public void set(float x, float y) {
        actualX = x;
        actualY = y;
        mergeXY();
    }

    @Override
    public void set(float x, float y, float z) {
        actualX = x;
        actualY = y;
        actualZ = z;
        mergeXYZ();
    }

    @Override
    public void add(float x, float y) {
        actualX += x;
        actualY += y;
        mergeXY();
    }

    @Override
    public void add(float x, float y, float z) {
        actualX += x;
        actualY += y;
        actualZ += z;
        mergeXYZ();
    }

    @Override
    public void remove(float x, float y) {
        actualX -= x;
        actualY -= y;
        mergeXY();
    }

    @Override
    public void remove(float x, float y, float z) {
        actualX -= x;
        actualY -= y;
        actualZ -= z;
        mergeXYZ();
    }

    protected void setLinkVec(LinkablePropertyVector linkVec) {
        this.linkVec = linkVec;
        mergeXYZ();
    }

    public float getActualX() {
        return actualX;
    }

    public float getActualY() {
        return actualY;
    }

    public float getActualZ() {
        return actualZ;
    }

    protected float getMergedX() {
        return isLinked() ? mergeFunc.apply(linkVec.x(), actualX) : actualX;
    }

    protected float getMergedY() {
        return isLinked() ? mergeFunc.apply(linkVec.y(), actualY) : actualY;
    }

    protected float getMergedZ() {
        return isLinked() ? mergeFunc.apply(linkVec.z, actualZ) : actualZ;
    }

    protected void mergeXY() {
        super.set(getMergedX(), getMergedY());
    }

    protected void mergeXYZ() {
        super.set(getMergedX(), getMergedY(), getMergedZ());
    }

    protected boolean isLinked() {
        return linkVec != null;
    }

    public enum MergeStrategy {
        ADD(Float::sum),
        MULTIPLY((first, second) -> first * second);

        private final FloatBiFunction mergeFunc;

        MergeStrategy(FloatBiFunction mergeFunc) {
            this.mergeFunc = mergeFunc;
        }

        public FloatBiFunction getMergeFunc() {
            return mergeFunc;
        }
    }
}
