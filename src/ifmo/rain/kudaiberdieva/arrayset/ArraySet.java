package ru.ifmo.rain.kudaiberdieva.arrayset;

import java.util.*;

import java.util.stream.Collectors;


public class ArraySet<E extends Comparable> extends AbstractSet<E> implements SortedSet<E> {
    private List<E> data;
    private Comparator<? super E> comparator;

    public ArraySet(Collection<? extends E> collection, Comparator<? super E> comparator) {
        this.comparator = comparator;
        Set<E> treeSetTmp = new TreeSet<>(comparator);
        treeSetTmp.addAll(collection);
        this.data = new ArrayList<>(treeSetTmp);
    }

    public ArraySet() {
        data = new ArrayList<>();
        comparator = null;
    }

    public ArraySet(Comparator<? super E> comparator) {
        this.comparator = comparator;
        data = new ArrayList<>();
    }

    private ArraySet(List<E> list, Comparator<E> comparator) {
        this.data = list;
        this.comparator = comparator;
    }

    public ArraySet(Collection<? extends E> collection) {
        this.comparator = null;
        data = collection.stream()
                .sorted()
                .distinct()
                .collect(Collectors.toList());

    }

    private int binSearch(E e) {
        int index = Collections.binarySearch(data, e, comparator);
        if (index < 0) {
            index = -index - 1;
        }
        return index;
    }

    private ArraySet<E> subSet(int leftIndex, int rightIndex) {
        if (leftIndex > rightIndex)
            throw new IllegalArgumentException("Can't split arraySet.");
        return new ArraySet<>(data.subList(leftIndex, rightIndex), comparator);
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    @SuppressWarnings("unchecked")
    public boolean contains(Object element) {
        return Collections.binarySearch(data, (E) element, comparator) >= 0;
    }

    @Override
    public Iterator<E> iterator() {
        return Collections.unmodifiableList(data).iterator();
    }

    @Override
    public int size() {
        return data.size();
    }

    @Override
    public Comparator<? super E> comparator() {
        return comparator;
    }

    @Override
    public ArraySet<E> subSet(E fromElement, E toElement) {
        return subSet(binSearch(fromElement), binSearch(toElement));
    }

    @Override
    public ArraySet<E> headSet(E toElement) {
        return subSet(0, binSearch(toElement));
    }

    @Override
    public ArraySet<E> tailSet(E fromElement) {
        return subSet(binSearch(fromElement), size());
    }

    @Override
    public E first() {
        if (size() == 0)
            throw new NoSuchElementException("ArraySet is empty.");
        return data.get(0);
    }

    @Override
    public E last() {
        if (size() == 0)
            throw new NoSuchElementException("ArraySet is empty.");
        return data.get(data.size() - 1);
    }
}

//1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22
// 22 24 2 4 7 9