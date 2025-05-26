import java.util.concurrent.*;

enum Status {
    RUNNING,
    COMPLETED,
    TIMED_OUT,
}

class Pair<T extends Comparable<T>, K> implements Comparable<Pair<T, K>> {
    public T first;
    public K second;

    public Pair(T first, K second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public int compareTo(Pair<T, K> other) {
        return first.compareTo(other.first);
    }
}


class Monitor implements Runnable {
    static final long MAX_TIME = 10000;
    private static Monitor instance = null;
    private ConcurrentHashMap<Long, Status> stats = new ConcurrentHashMap<>();
    private ConcurrentHashMap<Long, Task> tasks = new ConcurrentHashMap<>();
    private PriorityBlockingQueue<Pair<Long, Long>> timeOuts = new PriorityBlockingQueue<>();

    public static Monitor getInstance() {
        if (instance == null)
            instance = new Monitor();
        return instance;
    }

    public void registerTask(Task c) {
        synchronized (this) {
            var id = c.getId();
            System.out.println("[START] " + id + " has started running");
            stats.put(id, Status.RUNNING);
            tasks.put(id, c);
            timeOuts.put(new Pair<Long, Long>(MAX_TIME + System.currentTimeMillis(), id));
        }
    }

    public void stopTask(Task c) {
        synchronized (this) {
            var id = c.getId();
            if (stats.get(id) == Status.TIMED_OUT)
                return;
            System.out.println("[STOP] " + id + " has stopped");
            stats.put(id, Status.COMPLETED);
            tasks.remove(id);
        }
    }

    public void run() { // Timeouts
        while (!Thread.interrupted()) {
            try {
                var c = timeOuts.take();
                if (c.first < System.currentTimeMillis())
                    synchronized (this) {
                        var task = tasks.get(c.second);
                        if (stats.get(c.second) == Status.COMPLETED || !task.cthread.isAlive())
                            continue;
                        task.cthread.interrupt();
                        stats.put(c.second, Status.TIMED_OUT);
                        System.out.println("[FORCERD] timeout " + c.second);
                    }
                else
                    timeOuts.put(c);
            } catch (InterruptedException err) {
                continue;
            }
        }
    }

    public ConcurrentHashMap<Long, Status> getStats() {
        return stats;
    }
}

class Task implements Runnable {
    long id;
    long time;
    Thread cthread;

    public Task(long id, long time) {
        this.id = id;
        this.time = time;
    }

    public void run() {
        this.cthread = Thread.currentThread();
        Monitor.getInstance().registerTask(this);
        main();
        Monitor.getInstance().stopTask(this);
    }

    public long getId() {
        return id;
    }

    void main() {
        long start = System.currentTimeMillis();
        while (start + time > System.currentTimeMillis()) {
            if (Thread.interrupted())
                return;
        }
    }
}

public class tema4_1 {
    public static void main(String[] args) {
        var monitor = Monitor.getInstance();

        var monitor_thread = new Thread(monitor);
        monitor_thread.start();

        Thread watchdog = new Thread(() -> {
            while (!Thread.interrupted()) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    break;
                }

                System.out.println("\n[WATCHDOG] Task statuses:");
                System.out.println("----------------------------");
                for (var entry : monitor.getStats().entrySet()) {
                    System.out.printf("Task %d : %s\n", entry.getKey(), entry.getValue());
                }
                System.out.println("----------------------------");
            }
        });
        watchdog.start();

        long n = 4;
        var array = new int[]{11000, 1000, 4000, 30};
        for (int i = 0; i < n; i++) {
            var t = new Task(i, array[i]);
            var thr = new Thread(t);
            thr.start();
        }
        try {
            monitor_thread.join(15000);
        } catch (InterruptedException err) {
        }
        watchdog.interrupt();
    }


}

