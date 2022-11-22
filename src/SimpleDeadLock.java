import java.util.concurrent.TimeUnit;

public class SimpleDeadLock {

    private final Object lockA = new Object();
    private final Object lockB = new Object();

    public void doSomethingA() {
        synchronized (lockA) {
            System.out.println("doSomethingA: Lock A is acquired");
            System.out.println("doSomethingA: Will try to acquire Lock B now...");
            sleep(1); // sleep is used to ensure that the deadlock will happen. comment this line and run again.
            synchronized (lockB) {
                System.out.println("doSomethingA: Lock B is acquired");
            }
        }
    }

    public void doSomethingB() {
        synchronized (lockB) {
            System.out.println("doSomethingB: Lock B is acquired");
            System.out.println("doSomethingB: Will try to acquire Lock A now...");
            synchronized (lockA) {
                System.out.println("doSomethingB: Lock A is acquired");
            }
        }
    }

    public static void main(String[] args) {

        SimpleDeadLock sdl = new SimpleDeadLock();
        Thread a = new Thread(() -> sdl.doSomethingA(), "Thread A");
        Thread b = new Thread(() -> sdl.doSomethingB(), "Thread B");

        a.start();
        b.start();

        printThreadStatus(a, b);
    }

    private static void printThreadStatus(Thread a, Thread b) {
        while (a.getState() != Thread.State.TERMINATED && b.getState() != Thread.State.TERMINATED) {
            System.out.println("Thread A state is " + a.getState());
            System.out.println("Thread B state is " + b.getState());
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static void sleep(int seconds) {
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
