package ru.ifmo.rain.kudaiberdieva.arrayset;

import java.util.*;
import java.util.stream.Collectors;


public class TempSet<E extends Comparable> extends AbstractSet<E> implements NavigableSet<E> {
    private List<E> data;
    private Comparator<? super E> comparator;

    public TempSet(Collection<? extends E> collection, Comparator<? super E> comparator) {
        this.comparator = comparator;
        Set<E> treeSetTmp = new TreeSet<>(comparator);
        treeSetTmp.addAll(collection);
        this.data = new ArrayList<>(treeSetTmp);
    }

    public TempSet() {
        data = new ArrayList<>();
        comparator = null;
    }

    public TempSet(Comparator<? super E> comparator) {
        this.comparator = comparator;
        data = new ArrayList<>();
    }

    public TempSet(Collection<? extends E> collection) {
        this.comparator = null;
        data = collection.stream()
                .sorted()
                .distinct()
                .collect(Collectors.toList());
    }

    private boolean validIndex(int index) {
        return index < data.size() && index >= 0;
    }

    //lessEqGreat = 1, if greater, -1 if less

   /* private int binSearch(E e, boolean isStrictly, int lessOrGreater) {
        int index = Collections.binarySearch(data, e, comparator);
        if (index < 0) {
            index = -index - 1;
            if (lessOrGreater == -1)
                return validIndex(index + lessOrGreater) ? index + lessOrGreater : -1;
            return validIndex(index) ? index : -1;
        }

        if (!isStrictly)
            return index;

        return validIndex(index + lessOrGreater) ? index + lessOrGreater : -1;
    }
*/

   private int binSearch(E e, boolean isStrictly, int lessOrGreater) {
       return -1;
   }

    private E get(int index) {
        return validIndex(index) ? data.get(index) : null;
    }

    @Override
    public Iterator<E> iterator() {
        return Collections.unmodifiableList(data).iterator();
    }

    @Override
    public int size() {
        return data.size();
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public E pollFirst() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("ArraySet is unmodifiable. Operation 'pollFirst' is undefined.");
    }

    @Override
    public E pollLast() {
        throw new UnsupportedOperationException("ArraySet is unmodifiable. Operation 'pollLast' is undefined.");
    }

    @Override
    public E lower(E e) {
        return get(binSearch(e, true, -1));
    }

    @Override
    public E floor(E e) {
        return get(binSearch(e, false, -1));
    }

    @Override
    public E ceiling(E e) {
        return get(binSearch(e, false, 1));
    }

    @Override
    public E higher(E e) {
        return get(binSearch(e, true, 1));
    }

    @Override
    public NavigableSet<E> descendingSet() {
        List<E> descendingData = new DescendingList<>(data);
        return new TempSet<>(descendingData, comparator.reversed());
    }

    @Override
    public Iterator<E> descendingIterator() {
        return descendingSet().iterator();
    }
    @Override
    public NavigableSet<E> subSet(E fromElement, boolean fromInclusive, E toElement, boolean toInclusive) {
        print();

        int leftIndex =  binSearch(fromElement, !fromInclusive, 1);
        int rightIndex = binSearch(fromElement, !toInclusive, -1);

        if (leftIndex > rightIndex || leftIndex == -1 || rightIndex == -1)
            return new TempSet<>(new ArrayList<>(), comparator);

        return new TempSet<>(data.subList(leftIndex, rightIndex + 1), comparator);
    }

    @Override
    public NavigableSet<E> headSet(E toElement, boolean inclusive) {
        if (first() == null || toElement == null) {
            return new TempSet<>(new ArrayList<>(), comparator);
        }
        return subSet(first(), true, toElement, inclusive);
    }

    @Override
    public NavigableSet<E> tailSet(E fromElement, boolean inclusive) {
        return subSet(fromElement, inclusive, size() == 0 ? null : last(), true);
    }

  /*  @Override
    public NavigableSet<E> tailSet(E fromElement, boolean inclusive) {
        if (size() == 0 ) {
            return new ArraySet<>(new ArrayList<>(), comparator);
        }
        return subSet(fromElement, inclusive, last(), true);
    }
*/
    @Override
    public SortedSet<E> subSet(E fromElement, E toElement) {
        return subSet(fromElement, true, toElement, false);
    }

    @Override
    public SortedSet<E> headSet(E toElement) {
        return headSet(toElement, false);
    }

    @Override
    public SortedSet<E> tailSet(E fromElement) {
        return tailSet(fromElement, true);
    }

    @Override
    public Comparator<? super E> comparator() {
        return comparator;
    }

    @Override
    public E first() {
        if (size() == 0)
            throw new NoSuchElementException("...");
        return data.get(0);
    }

    @Override
    public E last() {
        if (size() == 0)
            throw new NoSuchElementException("...");
        return data.get(data.size() - 1);
    }

    @SuppressWarnings("unchecked")
    public boolean contains(Object e) {
        return Collections.binarySearch(data,(E) e, comparator) >= 0;
    }

    private void print() {
        System.out.print("Start...\n");

        for (E e: data) {
            System.out.print(e);
            System.out.print(' ');
        }
        System.out.print("END...\n");

    }
}

//1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22
// 22 24 2 4 7 9