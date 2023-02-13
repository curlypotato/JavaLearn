package concurrency;

import static concurrency.LoggerUtils.main;

public class TestThreadState {
    static final Object LOCK = new Object();
    public static void main(String[] args) throws InterruptedException {
        testWaiting();
    }

    private static void testWaiting() {
        Thread t2 = new Thread(() -> {
            synchronized (LOCK) {
                LoggerUtils.logger1.debug("before waiting"); // 1
                try {
                    LOCK.wait(); // 3
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        },"t2");

        t2.start();
        LoggerUtils.main.debug("state: {}", t2.getState()); // 2
        synchronized (LOCK) {
            LoggerUtils.main.debug("state: {}", t2.getState()); // 4
            LOCK.notify(); // 5
            LoggerUtils.main.debug("state: {}", t2.getState()); // 6
        }
        LoggerUtils.main.debug("state: {}", t2.getState()); // 7
    }

    private static void testBlocked() {
        Thread t2 = new Thread(() -> {
            LoggerUtils.logger1.debug("before sync"); // 3
            synchronized (LOCK) {
                LoggerUtils.logger1.debug("in sync"); // 4
            }
        },"t2");

        t2.start();
        LoggerUtils.main.debug("state: {}", t2.getState()); // 1
        synchronized (LOCK) {
            LoggerUtils.main.debug("state: {}", t2.getState()); // 2
        }
        LoggerUtils.main.debug("state: {}", t2.getState()); // 5
    }

    private static void testNewRunnableTerminated() {
        Thread t1 = new Thread(() -> {
            LoggerUtils.logger1.debug("running..."); // 3
        },"t1");

        LoggerUtils.main.debug("state: {}", t1.getState()); // 1
        t1.start();
        LoggerUtils.main.debug("state: {}", t1.getState()); // 2

        LoggerUtils.main.debug("state: {}", t1.getState()); // 4
    }
}
