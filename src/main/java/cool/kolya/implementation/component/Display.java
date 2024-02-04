package cool.kolya.implementation.component;

import cool.kolya.engine.Engine;
import cool.kolya.engine.data.FrameBufferSize;
import cool.kolya.engine.data.WindowSize;
import cool.kolya.engine.process.ProcessSettings;

public class Display {

    private static final ThreadLocal<DisplayState> STATE_THREAD_LOCAL = new ThreadLocal<>();

    public static DisplayState createContextDisplayState() {
        DisplayState displayState = new DisplayState();

        ProcessSettings settings = Engine.getContextProcess().getSettings();
        displayState.setFrameBufferSize(settings.getFrameBufferSize());
        displayState.setWindowSize(settings.getWindowSize());

        STATE_THREAD_LOCAL.set(displayState);

        return displayState;
    }

    public static WindowSize getWindowSize() {
        return getState().getWindowSize();
    }

    public static FrameBufferSize getFrameBufferSize() {
        return getState().getFrameBufferSize();
    }

    private static DisplayState getState() {
        return STATE_THREAD_LOCAL.get();
    }

    public static class DisplayState {

        private WindowSize windowSize = new WindowSize(0,0);
        private FrameBufferSize frameBufferSize = new FrameBufferSize(0, 0);

        private DisplayState() {}

        public WindowSize getWindowSize() {
            return windowSize;
        }

        public void setWindowSize(WindowSize windowSize) {
            this.windowSize = windowSize;
        }

        public FrameBufferSize getFrameBufferSize() {
            return frameBufferSize;
        }

        public void setFrameBufferSize(FrameBufferSize frameBufferSize) {
            this.frameBufferSize = frameBufferSize;
        }
    }
}
