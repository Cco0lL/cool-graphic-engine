package cool.kolya.implementation.scheduler;

import cool.kolya.api.util.SearchBitSet;
import cool.kolya.engine.Engine;
import cool.kolya.engine.event.EventHandler;
import cool.kolya.engine.event.EventListener;
import cool.kolya.engine.event.UpdateEvent;

import java.util.*;

public class TaskScheduler {

    public static final ThreadLocal<TaskScheduler> SCHEDULER_THREAD_LOCAL = new ThreadLocal<>();
    private final List<ScheduledTask> tasks = new TaskList(1 << 8);
    public final SearchBitSet acquiredIdBitSet = new SearchBitSet(1 << 8);

    public TaskScheduler() {
        Engine.getContextProcess().getEventBus()
                .registerListener(new EventListener() {
                    @EventHandler
                    void update(UpdateEvent e) {
                        updateTasks();
                    }
                });
    }

    public static void createContext() {
        if (SCHEDULER_THREAD_LOCAL.get() == null) {
            SCHEDULER_THREAD_LOCAL.set(new TaskScheduler());
        }
    }

    public static TaskScheduler getContext() {
        return SCHEDULER_THREAD_LOCAL.get();
    }

    public void updateTasks() {
        for (ScheduledTask task : tasks) {
            if (task == null) {
                continue;
            }
            if (task.isNeedStop()) {
                removeTask(task);
                return;
            }
            task.tickDelay();
            if (task.isDelayZero()) {
                task.run();
            }
        }
    }

    public int addTaskWithDelay(int delay, Runnable taskRunnable) {
        ScheduledTask task = new ScheduledTask(taskRunnable, delay);
        return addTask(task);
    }

    public int addTaskOnNextTick(Runnable runnable) {
        return addTaskWithDelay(0, runnable);
    }

    public int addRepeatableTask(int delay, int cooldown, Runnable runnable) {
        RepeatableScheduledTask task = new RepeatableScheduledTask(runnable, delay, cooldown);
        return addTask(task);
    }

    public boolean isRunning(int taskId) {
        if (!isIdValid(taskId)) {
            return false;
        }
        return tasks.get(taskId) != null;
    }

    public synchronized int addTask(ScheduledTask task) {
        int taskId = acquiredIdBitSet.firstClear();
        task.setId(taskId);
        tasks.add(task);
        acquiredIdBitSet.set(taskId);
        return taskId;
    }

    public void removeTask(ScheduledTask task) {
        removeTask(task.getId());
    }

    public synchronized void removeTask(int taskId) {
        tasks.remove(taskId);
        acquiredIdBitSet.clearIndex(taskId);
    }

    private boolean isIdValid(int id) {
        return id >= 0 && id < tasks.size();
    }

    private void assertTaskValid(int id) {
        if (!isIdValid(id)) {
            throw new IllegalStateException("id " + id + " is not valid");
        }
    }

    /**
     * Специальный лист для тасков.
     * 1) add ставит элемент не в конец, а в индекс, который равен ади таска
     * 2) remove(index) не сужает массив, а ставит в индекс массива null.
     * массив в целом невозможно укоротить
     * 3) add(index) работает как set(index). Если добавление происходит в последний
     * индекс, то, даже при наличии свободных (null элементов),
     * размер массива увеличится на 50%
     * 4) итератор fail-safe, из него возможно добавление и удаление элеметов листа,
     * он копирует массив элементов на создании и не видит последующих изменений во
     * время итерации
     * 5) indexOf(task) всегда вернет айди таска,
     * даже если таска нет в листе, остальные indexOf методы либо не поддерживаются,
     * либо имеют стандартную неоптмиальную реализацию
     * 6) айди таска всегда соответствует оффсету элемента массива
     * 7) операции, основанные на removeRange(start, end), не поддерживаются
     * 8) ListIterator и Spliterator не поддерживаются
     * 9) хешкод всегда 0, equals проверяется по ссылке (так как на данный момент
     * может существовать только один объект данного листа.
     * 10) SubList не поддерживается
     * 12) contains вернет true, если индекс валиден, меньше size() и значение под
     * индексом не null
     */
    private static final class TaskList extends AbstractList<ScheduledTask> {

        private ScheduledTask[] elements;
        private int size;

        public TaskList(int initCapacity) {
            elements = new ScheduledTask[initCapacity];
        }

        @Override
        public ScheduledTask get(int index) {
            return elements[index];
        }

        @Override
        public boolean add(ScheduledTask scheduledTask) {
            add(scheduledTask.getId(), scheduledTask);
            return true;
        }

        @Override
        public void add(int index, ScheduledTask element) {
            set(index, element);
        }

        @Override
        public ScheduledTask set(int index, ScheduledTask element) {
            assertRange(index);
            if (index > size) {
                size = index + 1;
            }
            if (size == elements.length) {
                elements = grow();
            }
            ScheduledTask prev = elements[index];
            elements[index] = element;
            return prev;
        }

        @Override
        public ScheduledTask remove(int index) {
            ScheduledTask previous = elements[index];
            elements[index] = null;
            return previous;
        }

        @Override
        public boolean remove(Object o) {
            remove(((ScheduledTask) o).getId());
            return true;
        }

        @Override
        public int indexOf(Object o) {
            ScheduledTask task = (ScheduledTask) o;
            return task.getId();
        }

        @Override
        public int size() {
            return size;
        }

        @Override
        public Iterator<ScheduledTask> iterator() {
            return new Iterator<>() {

                private final ScheduledTask[] snapshot = Arrays.copyOf(elements, size);
                private int current;
                private int cursor;

                @Override
                public boolean hasNext() {
                    return cursor < snapshot.length;
                }

                @Override
                public ScheduledTask next() {
                    return snapshot[current = cursor++];
                }

                @Override
                public void remove() {
                    TaskList.this.remove(current);
                }
            };
        }

        private ScheduledTask[] grow() {
            int oldCapacity = elements.length;
            //TODO не рассчитан на очень большие размеры и не имеет софткапа до максимального инта.
            // нужно поправить потом, мб
            return elements = Arrays.copyOf(elements, oldCapacity + (oldCapacity >> 1));
        }

        private boolean isIndexInRange(int index) {
            return index >= 0 && index < elements.length;
        }

        private void assertRange(int index) {
            if (!isIndexInRange(index)) {
                throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
            }
        }
    }
}
