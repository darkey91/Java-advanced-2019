package ifmo.rain.kudaiberdieva.mapper;

import info.kgeorgiy.java.advanced.mapper.ParallelMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class ParallelMapperImpl implements ParallelMapper {
    private int threads_amount;
    private List<Thread> threadList = new ArrayList<>();
    public ParallelMapperImpl(int threads) {

        for (int i = 0; i < threads; ++i) {

        }
    }


    @Override
    public <T, R> List<R> map(Function<? super T, ? extends R> function, List<? extends T> list) throws InterruptedException {
        return null;
    }

    @Override
    public void close() {

    }

}
