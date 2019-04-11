package ifmo.rain.kudaiberdieva.concurrent;

import info.kgeorgiy.java.advanced.concurrent.ListIP;
import info.kgeorgiy.java.advanced.mapper.ParallelMapper;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Implementation of {@link ListIP}
 *
 * @author darkey
 */
public class IterativeParallelism implements ListIP {
    private ParallelMapper parallelMapper;

    public IterativeParallelism() {
        parallelMapper = null;
    }

    public IterativeParallelism(ParallelMapper parallelMapper) {
        this.parallelMapper = parallelMapper;
    }

    /**
     * Join values to string.
     *
     * @param threads number or concurrent threads.
     * @param values values to join.
     *
     * @return values of joined result of {@link #toString()} call on each value.
     *
     * @throws InterruptedException if executing thread was interrupted.
     */
    @Override
    public String join(int threads, List<?> values) throws InterruptedException {
        //return common(threads, values, stream -> stream.toString(), stringStream -> stringStream.collect(Collectors.joining()));

        return common(threads, values,
                l -> l.stream().map(Object::toString).collect(Collectors.joining()),
                stringStream -> stringStream.collect(Collectors.joining()));
    }

    /**
     * Filters values by predicate.
     *
     * @param threads number or concurrent threads.
     * @param values values to filter.
     * @param predicate filter predicate.
     *
     * @return values of values satisfying given predicated. Order of values is preserved.
     *
     * @throws InterruptedException if executing thread was interrupted.
     */
    @Override
    public <T> List<T> filter(int threads, List<? extends T> values, Predicate<? super T> predicate) throws InterruptedException {
        return common(threads, values,
                l -> l.stream().filter(predicate).collect(Collectors.toList()),
                valuesStream -> valuesStream.flatMap(Collection::stream).collect(Collectors.toList()));
    }

    /**
     * Mas values.
     *
     * @param threads number or concurrent threads.
     * @param values values to filter.
     * @param function function.
     *
     * @return values of values mapped by given function.
     *
     * @throws InterruptedException if executing thread was interrupted.
     */

    @Override
    public <T, U> List<U> map(int threads, List<? extends T> values, Function<? super T, ? extends U> function) throws InterruptedException {
        return common(threads, values,
                l -> l.stream().map(function).collect(Collectors.toList()),
                valuesStream -> valuesStream.flatMap(Collection::stream).collect(Collectors.toList()));
    }


    private void joinThreads(List<Thread> threads) throws InterruptedException {
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                throw new InterruptedException("Thread has interrupted the current thread");
            }
        }
    }

    private <T, E> E common(int threads, List<? extends T> values,
                            Function<List<? extends T>, E> threadFunction,
                            Function<Stream<E>, E> resultingFunction) throws InterruptedException {
        threads = Math.min(values.size(), threads);
        int size = values.size() / threads;
        int left = values.size() % threads;

        List<List<? extends T>> subLists = new ArrayList<>();

        for (int i = 0; i < values.size(); ) {
            int sizeSub = size + (left-- > 0 ? 1 : 0);
            subLists.addAll(Collections.singletonList(values.subList(i, i + sizeSub)));
            i = i + sizeSub;
        }

        if (parallelMapper == null) {
            final List<Thread> threadList = new ArrayList<>(Collections.nCopies(threads, null));

            //T , i.e . Optional.get() returns T
            List<E> resultOfThreads = new ArrayList<>(Collections.nCopies(threads, null));

            for (int i = 0; i < threads; i++) {
                final int position = i;
                threadList.set(position, new Thread(() -> {
                    resultOfThreads.set(position, threadFunction.apply(subLists.get(position)));
                }));
                threadList.get(i).start();
            }

            joinThreads(threadList);

            return resultingFunction.apply(resultOfThreads.stream());
        } else {
             return resultingFunction.apply(parallelMapper.map(threadFunction, subLists).stream());
        }
    }

    /**
     * Returns maximum value.
     *
     * @param threads number or concurrent threads.
     * @param values values to get maximum of.
     * @param comparator value comparator.
     * @param <T> value type.
     *
     * @return maximum of given values
     *
     * @throws InterruptedException if executing thread was interrupted.
     * @throws {@link NoSuchElementException} if not values are given.
     */
    @Override
    public <T> T maximum(int threads, List<? extends T> values, Comparator<? super T> comparator) throws InterruptedException, NoSuchElementException {
        if (values.isEmpty())
            throw new NoSuchElementException("Can't find maximum element in empty list");
        return common(threads, values, l -> l.stream().max(comparator).get(), stream -> stream.max(comparator).get());
    }


    /**
     * Returns minimum value.
     *
     * @param threads number or concurrent threads.
     * @param values values to get minimum of.
     * @param comparator value comparator.
     * @param <T> value type.
     *
     * @return minimum of given values
     *
     * @throws InterruptedException if executing thread was interrupted.
     * @throws java.util.NoSuchElementException if not values are given.
     */
    @Override
    public <T> T minimum(int threads, List<? extends T> values, Comparator<? super T> comparator) throws InterruptedException, NoSuchElementException {
        return maximum(threads, values, comparator.reversed());
        //  return common(threads, values, stream -> stream.min(comparator).get(), stream -> stream.min(comparator).get());
    }
    /**
     * Returns whether all values satisfies predicate.
     *
     * @param threads number or concurrent threads.
     * @param values values to test.
     * @param predicate test predicate.
     * @param <T> value type.
     *
     * @return whether all values satisfies predicate or {@code true}, if no values are given.
     *
     * @throws InterruptedException if executing thread was interrupted.
     */
    @Override
    public <T> boolean all(int threads, List<? extends T> values, Predicate<? super T> predicate) throws InterruptedException {
       return common(threads, values, l -> l.stream().allMatch(predicate), stream -> stream.allMatch(p -> p));
    }

    /**
     * Returns whether any of values satisfies predicate.
     *
     * @param threads number or concurrent threads.
     * @param values values to test.
     * @param predicate test predicate.
     * @param <T> value type.
     *
     * @return whether any value satisfies predicate or {@code false}, if no values are given.
     *
     * @throws InterruptedException if executing thread was interrupted.
     */
    @Override
    public <T> boolean any(int threads, List<? extends T> values, Predicate<? super T> predicate) throws InterruptedException {
        return !all(threads, values, predicate.negate());
    }

}
