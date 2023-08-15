package cool.kolya.engine.util;

public class MemUtil {

    public static int LONG_SIZE = asBytesSize(Long.SIZE);
    public static int DOUBLE_SIZE = asBytesSize(Double.SIZE);
    public static int INT_SIZE = asBytesSize(Integer.SIZE);
    public static int FLOAT_SIZE = asBytesSize(Float.SIZE);

    public static int RGBA_PIXEL_SIZE = 4;

    public static int asBytesSize(int bitsSize) {
        return bitsSize / Byte.SIZE;
    }
}
