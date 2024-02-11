package cool.kolya.implementation.scene.element.callback;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Callback {

    private Runnable callbackRunnable = () -> {};

    public void add(Runnable callbackRunnable) {
        Runnable prev = this.callbackRunnable;
        this.callbackRunnable = () -> {
            prev.run();
            callbackRunnable.run();
        };
    }

    public void run() {
        callbackRunnable.run();
    }

    public enum InteractType {
        HOVER,
        LEFT_PRESS,
        RIGHT_PRESS,
        MID_PRESS,
        LEFT_RELEASE,
        RIGHT_RELEASE,
        MID_RELEASE,
        SCROLL;

        public static final List<InteractType> VALUES = List.of(values());

        public static final int PRESS_OFFSET = 1;
        public static final int RELEASE_OFFSET = 4;

        public static InteractType getPressByKeyNum(int keyNum) {
            return VALUES.get(PRESS_OFFSET + keyNum);
        }

        public static InteractType getReleaseByKeyNum(int keyNum) {
            return VALUES.get(RELEASE_OFFSET + keyNum);
        }
    }
}
