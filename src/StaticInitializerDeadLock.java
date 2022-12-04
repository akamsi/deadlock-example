public class StaticInitializerDeadLock {

    private static final String str = new String("Static initializer deadlock");

    static class MyThread extends Thread {
        @Override
        public void run() {
            System.out.println("Inside run() of MyThread");
            System.out.println(str);
        }
    }

    static {
        MyThread thread = new MyThread();
        thread.start();
        System.out.println("start() called");
        try {
            System.out.println("Trying to join()...");
            thread.join();
            System.out.println("Joined...");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        System.out.println("Join should be call from main to avoid deadlock");
    }
}
