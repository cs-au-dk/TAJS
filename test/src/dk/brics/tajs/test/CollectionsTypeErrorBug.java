package dk.brics.tajs.test;

import dk.brics.tajs.util.HybridArrayHashMap;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.AbstractMap;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Tests for bug reported at http://bugreport.java.com with internal review ID 9049867 (not public at the moment).
 */
@RunWith(Parameterized.class)
public class CollectionsTypeErrorBug {

    @Parameterized.Parameter
    public Supplier<Map<Integer, Optional<Integer>>> constructor;

    @Parameterized.Parameter(1)
    public String name;

    @Parameterized.Parameters(name = "{1}")
    public static List<Object[]> constructors() {
        Stream<Supplier<Map<Integer, Optional<Integer>>>> constructors = Stream.of(
                LinkedHashMap::new,
                HashMap::new,
                WeakHashMap::new,
                IdentityHashMap::new,
                HybridArrayHashMap::new, // the one that is problematic for the TAJS project
                MyMap::new,
                MyTypeSpecializedMap::new
        );
        return constructors.map(f -> new Object[]{f, f.get().getClass().getCanonicalName()})
                .collect(Collectors.toList());
    }

    @Test
    public void printValue() throws Exception {
        makeMap().forEach((Object k, Object v) -> System.out.println(v.getClass()));
    }

    @Test
    public void forEachUntyped() throws Exception {
        makeMap().forEach((Object k, Object v) -> {
        });
    }

    @Test
    public void forEachTyped() throws Exception {
        makeMap().forEach((Object k, Optional<Integer> v) -> {
        });
    }

    @Test
    public void makeWrapperMap() throws Exception {
        HashMap<Integer, Optional<Integer>> wrapper = new HashMap<>(makeMap());
    }

    @Test
    public void forEachOnWrapperMap() throws Exception {
        new HashMap<>(makeMap())
                .forEach((Object k, Optional<Integer> v) -> {
                });
    }

    @Test
    public void instantite() throws Exception {
        makeMap();
    }

    @Test
    public void untypedIteratorNextOnWrapperMap() throws Exception {
        Map<Integer, Optional<Integer>> mysteryMap = makeMap();
        HashMap<Integer, Optional<Integer>> hashMap = new HashMap<>(mysteryMap);
        Collection<Optional<Integer>> values = hashMap.values();
        Object next = values.iterator().next();
    }

    @Test
    public void typedIteratorNextOnWrapperMap() throws Exception {
        Map<Integer, Optional<Integer>> mysteryMap = makeMap();
        HashMap<Integer, Optional<Integer>> hashMap = new HashMap<>(mysteryMap);
        Collection<Optional<Integer>> values = hashMap.values();
        Optional<Integer> next = values.iterator().next();
    }

    private Map<Integer, Optional<Integer>> makeMap() {
        return Stream.of(0)
                .collect(Collectors.groupingBy(
                        e -> 1,
                        constructor,
                        Collectors.reducing((e1, e2) -> 0)));
    }

    /**
     * A simple variant of HybridArrayHashMap.
     */
    private static class MyMap<K, V> extends AbstractMap<K, V> implements Map<K, V> {

        private K key;

        private V value;

        @Override
        public Set<Entry<K, V>> entrySet() {
            if (key == null) {
                return Collections.emptySet();
            }
            return Collections.singleton(new AbstractMap.SimpleEntry<>(key, value));
        }

        @Override
        public V put(K key, V value) {
            if (this.key == null) {
                this.key = key;
                this.value = value;
                return null;
            }
            throw new UnsupportedOperationException("Can only put once");
        }
    }

    private static class MyTypeSpecializedMap extends HashMap<Integer, Optional<Integer>> {

        @Override
        public Optional<Integer> computeIfAbsent(Integer key, Function<? super Integer, ? extends Optional<Integer>> mappingFunction) {
            Optional<Integer> integer = super.computeIfAbsent(key, mappingFunction); // force runtime type check
            return integer;
        }
    }
}
