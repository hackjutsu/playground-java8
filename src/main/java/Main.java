import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    private static final String SEPARATOR = "\n\n----- ";

    public static void main(String args[]) {
        sortingTest();
        stringTest();
        lambdaTest();
        streamTest();
        streamSupplierTest();
    }

    private static void sortingTest() {
        logger.debug("\n\n----- sorting Test");
        Integer[] arr = new Integer[]{3, 4, 100, 73, 99};
        List<Integer> list = new ArrayList<>(Arrays.asList(arr));

        // ascending
        Collections.sort(list);
        logger.debug(list.toString());

        // descending
        Collections.sort(list, Comparator.reverseOrder());
        logger.debug(list.toString());
    }

    private static void stringTest() {
        String str_a = "a";
        String str_a1 = "a1";
        String str_b = "b";

        logger.debug(String.valueOf(str_a.compareTo(str_a1))); // -1
        logger.debug(String.valueOf(str_a1.compareTo(str_b))); // -1
    }

    private static void lambdaTest() {
        List<String> names = Arrays.asList("peter", "anna", "mike", "xenia");
        Collections.sort(names, (a, b) -> b.compareTo(a));
        Collections.sort(names, (a, b) -> {
            logger.debug(a);
            logger.debug(b);
            return a.compareTo(b);
        });

        logger.debug(names.toString());
    }

    private static void streamTest() {
        logger.debug("\n\n----- stream Test");
        int[] arr = new int[]{1,2,3};
        int[] arr1 = {1,2,3};
//        ArrayList<Integer> arrayList = new ArrayList<>(Arrays.asList(arr));

        Integer[] boxedInts = Arrays.stream(arr).boxed().toArray(Integer[]::new);
        Integer[] boxedInts1 = IntStream.of(arr).boxed().toArray(Integer[]::new);

        // Initialize a List
        List<String> stringCollection = new ArrayList<>();
        stringCollection.add("ddd2");
        stringCollection.add("aaa2");
        stringCollection.add("bbb1");
        stringCollection.add("aaa1");
        stringCollection.add("bbb3");
        stringCollection.add("ccc");
        stringCollection.add("bbb2");
        stringCollection.add("ddd1");

        // Filter(intermediate, predicate) & forEach(terminal, consumer)
        logger.info(SEPARATOR + "Stream Filter");
        stringCollection
                .stream()
                .filter((s) -> s.startsWith("a"))
                .forEach(logger::debug);

        // Sorted(intermediate, comparator)
        logger.info(SEPARATOR + "Stream Sorted (not collected)");
        stringCollection
                .stream()
                .sorted((a, b) -> b.compareTo(a)) // stringCollection's order is untouched
                .forEach(logger::debug);

        logger.info(SEPARATOR + "Stream Sorted (collected, Collectors.toCollection)");
        List<String> newStringCollection = stringCollection
                .stream()
                .sorted((a, b) -> b.compareTo(a)) // stringCollection's order is untouched
                .collect(Collectors.toCollection(ArrayList::new));
        logger.debug(newStringCollection.toString());

        logger.info(SEPARATOR + "Stream Sorted (collected, Collectors.toList)");
        List<String> newStringCollection1 = stringCollection
                .stream()
                .sorted((a, b) -> b.compareTo(a)) // stringCollection's order is untouched
                .collect(Collectors.toList()); // a naive List implementation, neither ArrayList nor LinkedList
        logger.debug(newStringCollection1.toString());

        // Map(intermediate)
        logger.info(SEPARATOR + "Stream Map");
        stringCollection
                .stream()
                .map(String::toUpperCase)
                .forEach(logger::debug);

        // Match(terminal)
        logger.info(SEPARATOR + "Stream Match anyMatch");
        boolean anyStartsWithA = stringCollection
                .stream()
                .anyMatch((a) -> a.startsWith("a"));
        logger.debug("anyStartsWithA is " + anyStartsWithA);

        logger.info(SEPARATOR + "Stream Match allMatch");
        boolean allStartsWithA = stringCollection
                .stream()
                .allMatch((a) -> a.startsWith("a"));
        logger.debug("allStartsWithA is " + allStartsWithA);

        logger.info(SEPARATOR + "Stream Match noneMatch");
        boolean noneStartsWithA = stringCollection
                .stream()
                .noneMatch((a) -> a.startsWith("a"));
        logger.debug("noneStartsWithA is " + noneStartsWithA);

        // Count(terminal)
        logger.info(SEPARATOR + "Stream Count");
        long countStartsWithA = stringCollection
                .stream()
                .filter((a) -> a.startsWith("a"))
                .count();
        logger.debug("countStartsWithA is " + countStartsWithA);

        // Reduce(terminal)
        logger.info(SEPARATOR + "Stream Reduce");
        Optional<String> reduced =
                stringCollection
                    .stream()
                    .filter((a) -> a.startsWith("a"))
                    .reduce((s1, s2) -> s1 + "#" + s2);
        reduced.ifPresent(logger::debug);
        String result = reduced.orElse("Default String");
        logger.debug(result);
    }

    private static void streamSupplierTest() {
        logger.info(SEPARATOR + "Stream Supplier Test");
        Supplier<Stream<String>> streamSupplier =
                () -> Stream.of("d2", "a2", "b1", "b3", "c")
                    .filter(s -> s.startsWith("a"));

        boolean strStartWithD = streamSupplier.get().anyMatch((s) -> s.startsWith("d"));
        boolean strNoStartWithZ = streamSupplier.get().noneMatch((s) -> s.startsWith("z"));

        logger.debug("strStartWithD is " + strStartWithD);
        logger.debug("strNoStartWithZ is " + strNoStartWithZ);
    }
}
