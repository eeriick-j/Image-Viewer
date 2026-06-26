package presenter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CircularIteratorTest {

    private CircularIterator<String> iterator;

    @BeforeEach
    void setUp() {
        iterator = new CircularIterator<>();
    }

    @Nested
    class WhenEmpty {
        @Test
        void isEmpty_returnsTrue() {
            assertTrue(iterator.isEmpty());
        }

        @Test
        void current_returnsNull() {
            assertNull(iterator.current());
        }

        @Test
        void next_returnsNull() {
            assertNull(iterator.next());
        }

        @Test
        void prev_returnsNull() {
            assertNull(iterator.prev());
        }
    }

    @Nested
    class WithOneElement {
        @BeforeEach
        void setup() {
            iterator.add("a");
        }

        @Test
        void isEmpty_returnsFalse() {
            assertFalse(iterator.isEmpty());
        }

        @Test
        void current_returnsElement() {
            assertEquals("a", iterator.current());
        }

        @Test
        void next_wrapsToItself() {
            assertEquals("a", iterator.next());
        }

        @Test
        void prev_wrapsToItself() {
            assertEquals("a", iterator.prev());
        }
    }

    @Nested
    class Navigation {
        @BeforeEach
        void setup() {
            iterator.addAll(List.of("a", "b", "c"));
        }

        @Test
        void next_advancesToSecond() {
            assertEquals("b", iterator.next());
        }

        @Test
        void prev_fromFirstWrapsToLast() {
            assertEquals("c", iterator.prev());
        }

        @Test
        void next_wrapsAroundAfterLast() {
            iterator.next(); // b
            iterator.next(); // c
            iterator.next(); // a (wrap)
            assertEquals("a", iterator.current());
        }

        @Test
        void prev_wrapsAroundBeforeFirst() {
            iterator.prev(); // c
            iterator.prev(); // b
            iterator.prev(); // a
            assertEquals("a", iterator.current());
        }

        @Test
        void nextThenPrev_returnsToStart() {
            iterator.next(); // b
            iterator.prev(); // a
            assertEquals("a", iterator.current());
        }

        @Test
        void fullForwardCycle_returnsToStart() {
            int size = 3;
            for (int i = 0; i < size; i++) iterator.next();
            assertEquals("a", iterator.current());
        }

        @Test
        void fullBackwardCycle_returnsToStart() {
            int size = 3;
            for (int i = 0; i < size; i++) iterator.prev();
            assertEquals("a", iterator.current());
        }
    }

    @Nested
    class Add {
        @Test
        void addToEmpty_becomesCurrentAndNotEmpty() {
            iterator.add("x");
            assertFalse(iterator.isEmpty());
            assertEquals("x", iterator.current());
        }

        @Test
        void addToExisting_jumpsToCurrent() {
            iterator.addAll(List.of("a", "b"));
            iterator.add("c");
            assertEquals("c", iterator.current());
        }

        @Test
        void addedElement_isReachableWithNext() {
            iterator.addAll(List.of("a", "b"));
            iterator.add("c");
            iterator.next(); // wrap → "a"
            assertEquals("a", iterator.current());
        }

        @Test
        void addedElement_isReachableWithPrev() {
            iterator.addAll(List.of("a", "b"));
            iterator.add("c");
            iterator.prev(); // "b"
            assertEquals("b", iterator.current());
        }

        @Test
        void multipleAdds_eachBecomesCurrentInOrder() {
            iterator.add("a");
            assertEquals("a", iterator.current());
            iterator.add("b");
            assertEquals("b", iterator.current());
            iterator.add("c");
            assertEquals("c", iterator.current());
        }
    }

    @Nested
    class AddAll {
        @Test
        void addAll_setsCurrentToFirst() {
            iterator.addAll(List.of("a", "b", "c"));
            assertEquals("a", iterator.current());
        }

        @Test
        void addAll_emptyList_remainsEmpty() {
            iterator.addAll(List.of());
            assertTrue(iterator.isEmpty());
        }

        @Test
        void addAll_singleElement_behavesSameAsAdd() {
            iterator.addAll(List.of("only"));
            assertEquals("only", iterator.current());
            assertFalse(iterator.isEmpty());
        }
    }
}