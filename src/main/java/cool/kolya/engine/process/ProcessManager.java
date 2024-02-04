package cool.kolya.engine.process;

import java.util.concurrent.CopyOnWriteArrayList;

public class ProcessManager {

    private static final CopyOnWriteArrayList<EngineProcess> ACTIVE_PROCESSES = new CopyOnWriteArrayList<>();
    private static final ThreadLocal<EngineProcess> PROCESS_THREAD_LOCAL = new ThreadLocal<>();
    private static volatile boolean NOT_ACTIVE_PROCESSES;

    public static EngineProcess getContextProcess() {
        return PROCESS_THREAD_LOCAL.get();
    }

    public static boolean isNotActiveProcessors() {
        return NOT_ACTIVE_PROCESSES;
    }

    static void setActiveContextProcess(EngineProcess process) {
        PROCESS_THREAD_LOCAL.set(process);
        ACTIVE_PROCESSES.add(process);
    }

    static void removeActiveContextProcess() {
        EngineProcess process = PROCESS_THREAD_LOCAL.get();
        ACTIVE_PROCESSES.remove(process);
        PROCESS_THREAD_LOCAL.remove();
        NOT_ACTIVE_PROCESSES = ACTIVE_PROCESSES.isEmpty();
    }
}
