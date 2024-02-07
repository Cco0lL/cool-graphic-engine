package cool.kolya.engine.process.loop;

import cool.kolya.engine.process.EngineProcess;
import cool.kolya.api.util.ThreadScopeUtil;

public class DefaultProcessLoopProvider {

    public static ProcessLoop newProcessLoop(EngineProcess process) {
        return ThreadScopeUtil.isCurrentThreadMain() ?
                new MainThreadProcessLoop(process) :
                new ProcessLoopImpl(process);
    }
}
