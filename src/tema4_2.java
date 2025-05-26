import java.util.*;
import java.util.List;
import java.util.concurrent.locks.*;

public class tema4_2 {

    private static final List<Integer> bufferA = Collections.synchronizedList(new ArrayList<>());
    private static final List<Integer> bufferB = Collections.synchronizedList(new ArrayList<>());

    private static final ReentrantLock lockA = new ReentrantLock();
    private static final ReentrantLock lockB = new ReentrantLock();

    public static void main(String[] args) {
        Runnable producer = () -> {
            while (true) {
                try {
                    lockA.lock();
                    System.out.println(Thread.currentThread().getName() + " lock A");
                    Thread.sleep(10);
                    lockB.lock();
                    System.out.println(Thread.currentThread().getName() + " lock B");

                    bufferA.add(1);
                    bufferB.add(1);

                    lockB.unlock();
                    System.out.println(Thread.currentThread().getName() + " release B");
                    lockA.unlock();
                    System.out.println(Thread.currentThread().getName() + " release A");

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        Runnable consumer = () -> {
            while (true) {
                try {
                    //Working
                    lockA.lock();
                    System.out.println(Thread.currentThread().getName() + " lock A");
                    Thread.sleep(10);
                    lockB.lock();
                    System.out.println(Thread.currentThread().getName() + " lock B");

                    //DEADLOCK
                    //  lockB.lock();
                    //  System.out.println(Thread.currentThread().getName()+" lock B");
                    //  Thread.sleep(10);
                    //  lockA.lock();
                    //  System.out.println(Thread.currentThread().getName()+" lock A");

                    if (!bufferA.isEmpty() && !bufferB.isEmpty()) {
                        bufferA.remove(0);
                        bufferB.remove(0);
                    }

                    lockB.unlock();
                    System.out.println(Thread.currentThread().getName() + " release B");
                    lockA.unlock();
                    System.out.println(Thread.currentThread().getName() + " release A");

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        new Thread(producer, "P1").start();
        new Thread(producer, "P2").start();
        new Thread(consumer, "C1").start();
        new Thread(consumer, "C2").start();
    }
}
