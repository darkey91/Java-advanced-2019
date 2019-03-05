package ru.ifmo.rain.kudaiberdieva.arrayset;

import java.util.AbstractList;
import java.util.List;

public class DescendingList<E extends Comparable> extends AbstractList<E> {
    private List<E> data;
    public DescendingList(List<E> data) {
        this.data = data;
    }

    @Override
    public int size() {
        return data.size();
    }

    @Override
    public E get(int index) {
        return data.get(size() - index - 1);
    }

}
