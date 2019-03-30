package ifmo.rain.kudaiberdieva.student;


import info.kgeorgiy.java.advanced.student.Student;
import info.kgeorgiy.java.advanced.student.StudentQuery;

import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StudentDB implements StudentQuery {
    private Comparator<Student> mainComparator = Comparator.comparing(Student::getLastName)
            .thenComparing(Student::getFirstName)
            .thenComparing(Student::getId);

    private <C extends Collection<String>> C getMappedCollection(Collection<Student> collection, Function<Student, String> mapper, Supplier<C> collectionFactory) {
        return collection.stream()
                .map(mapper)
                .collect(Collectors.toCollection(collectionFactory));
    }


    private Stream<Student> getFilteredStream(Collection<Student> collection, Predicate<Student> predicate) {
        return collection.stream()
                .filter(predicate);
    }

    private <C extends Collection<Student>> C getFilteredCollection(Collection<Student> collection, Predicate<Student> predicate, Supplier<C> collectionFactory) {
        return getFilteredStream(collection,predicate)
                .collect(Collectors.toCollection(collectionFactory));
    }


    @Override
    public List<String> getFirstNames(List<Student> collection) {
        return getMappedCollection(collection, Student::getFirstName, ArrayList::new);
    }

    @Override
    public List<String> getLastNames(List<Student> collection) {
        return getMappedCollection(collection, Student::getLastName, ArrayList::new);
    }

    @Override
    public List<String> getGroups(List<Student> collection) {
        return getMappedCollection(collection, Student::getGroup, ArrayList::new);
    }

    @Override
    public List<String> getFullNames(List<Student> collection) {
        return getMappedCollection(collection,
                s -> s.getFirstName().concat(" ").concat(s.getLastName()),
                ArrayList::new);
    }

    @Override
    public Set<String> getDistinctFirstNames(List<Student> collection) {
        return getMappedCollection(collection, Student::getFirstName, HashSet::new);
    }

    @Override
    public String getMinStudentFirstName(List<Student> collection) {
        return collection
                .stream()
                .min(Student::compareTo)
                .map(Student::getFirstName)
                .orElse(null);
    }

    private <C extends Collection<Student>> C getSortedCollection(Collection<Student> collection, Comparator<Student> comparator, Supplier<C> collectionFactory) {
        return collection.stream()
                .sorted(comparator)
                .collect(Collectors.toCollection(collectionFactory));
    }

    @Override
    public List<Student> sortStudentsById(Collection<Student> collection) {
        return getSortedCollection(collection, Student::compareTo, ArrayList::new);
    }

    @Override
    public List<Student> sortStudentsByName(Collection<Student> collection) {
        return getSortedCollection(collection, mainComparator, ArrayList::new);
    }

    @Override
    public List<Student> findStudentsByFirstName(Collection<Student> collection, String s) {
        return getFilteredCollection(collection, (student) -> student.getFirstName().equals(s), ArrayList::new);
    }

    @Override
    public List<Student> findStudentsByLastName(Collection<Student> collection, String s) {
        return getFilteredCollection(collection, (student) -> student.getLastName().equals(s), ArrayList::new);
    }

    @Override
    public List<Student> findStudentsByGroup(Collection<Student> collection, String s) {
        return getFilteredStream(collection, (student) -> student.getGroup().equals(s))
                .sorted(mainComparator).collect(Collectors.toList());
    }

    @Override
    public Map<String, String> findStudentNamesByGroup(Collection<Student> collection, String s) {
        return getFilteredStream(collection, student -> student.getGroup().equals(s))
                .collect(Collectors.toMap(Student::getLastName, Student::getFirstName, BinaryOperator.minBy(String::compareTo)));
    }
}















































































    /*

    private Stream<Group> getGroupsStream(Collection<Student> collection) {
        return collection.stream()
                .collect(Collectors.groupingBy(Student::getGroup))
                .entrySet().stream()
                .map((group) -> new Group(group.getKey(), group.getValue()));
    }
    private Stream<Group> getSortedGroupStream (Collection<Student> collection, Comparator<Group> groupComparator, Comparator<Student> studentComparator) {
        return collection.stream()
                .collect(Collectors.groupingBy(Student::getGroup))
                .entrySet().stream()
                .map((group) -> new Group(group.getKey(), group.getValue()
                        .stream()
                        .sorted(studentComparator)
                        .collect(Collectors.toList())))
                .sorted(groupComparator);
    }

    @Override
    public List<Group> getGroupsByName(Collection<Student> collection) {
        return getSortedGroupStream(collection, Comparator.comparing(Group::getName), mainComparator)
                .collect(Collectors.toList());
    }

    @Override
    public List<Group> getGroupsById(Collection<Student> collection) {
        return getSortedGroupStream(collection, Comparator.comparing(Group::getName), Comparator.comparing(Student::getId))
                .collect(Collectors.toList());
    }

    @Override
    public String getLargestGroup(Collection<Student> collection) {
        return getGroupsStream(collection)
                .max(Comparator.comparing((group) -> group.getStudents().size()))
                .map(Group::getName)
                .orElse("There is no any group");
    }


    @Override
    public String getLargestGroupFirstName(Collection<Student> collection) {
        return getGroupsStream(collection).max(Comparator.comparing(g -> g.getStudents().size())

        ).map(Group::getName)
                .orElse("There is no any group");
    }

}

*/