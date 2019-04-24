package ifmo.rain.kudaiberdieva.mapper;

import info.kgeorgiy.java.advanced.mapper.ParallelMapper;

import java.util.*;
import java.util.function.Function;

public class ParallelMapperImpl implements ParallelMapper {
    private Thread[] workers;
    private final Queue<Runnable> queue;

    private class Counter {
        private int counter = 0;
        private int get() {
            return counter;
        }
        private void increment() {
            ++counter;
        }
    }

    /**
     * Constructor creates int:tread threads
     * @param threads amount of threads
     */
    public ParallelMapperImpl(int threads) {
        int threads_amount = Math.max(threads, 1);
        queue = new LinkedList<>();
        workers = new Thread[threads_amount];

        for (int i = 0; i < threads; ++i) {
            workers[i] = new Thread(() -> {
                Runnable runnable;
                while (!Thread.currentThread().isInterrupted()) {
                    synchronized (queue) {
                        while (queue.isEmpty()) {
                            try {
                                queue.wait();
                            } catch (InterruptedException e) {
                               // e.printStackTrace();
                                return;
                            }
                        }
                        runnable = queue.remove();
                    }
                    runnable.run();
            }});
            workers[i].start();
        }
    }

    /**
     * Maps function {@code f} over specified {@code args}.
     * Mapping for each element performs in parallel.
     *
     * @throws InterruptedException if calling thread was interrupted
     */

    @Override
    public <T, R> List<R> map(Function<? super T, ? extends R> function, List<? extends T> list) throws InterruptedException {
        List<R> result = new ArrayList<>(list.size());

        final Counter counter = new Counter();

        for (int i = 0; i < list.size(); ++i) {
            result.add(null);
        }

        for (int i = 0; i < list.size(); ++i) {
            final int index = i;
            Runnable runnable = () -> {
                result.set(index, function.apply(list.get(index)));
                synchronized (counter) {
                    counter.increment();
                    if (counter.get() == list.size()) {
                        counter.notify();
                    }
                }
            };

            synchronized (queue) {
                queue.add(runnable);
                queue.notify();
            }
        }

        synchronized (counter) {
            if (counter.get() < list.size()) {
                counter.wait();
            }
        }

        return result;
    }

    /** Stops all threads. All unfinished mappings leave in undefined state. */
    @Override
    public void close() {
        for (Thread thread : workers) {
            thread.interrupt();
        }

        for (Thread thread : workers) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                //e.printStackTrace();
            }
        }
    }
}
