import deque.ArrayDeque;
import deque.Deque;
import deque.LinkedListDeque;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import static com.google.common.truth.Truth.assertThat;

public class ArrayDequeTest {
    @Test
    public void addLastTestBasicWithoutToList() {
        Deque<String> lld1 = new ArrayDeque<>();

        lld1.addLast("front"); // after this call we expect: ["front"]
        lld1.addLast("middle"); // after this call we expect: ["front", "middle"]
        lld1.addLast("back"); // after this call we expect: ["front", "middle", "back"]
        assertThat(lld1).containsExactly("front", "middle", "back");
    }

    @Test
    public void testEqualDeques() {
        Deque<String> lld1 = new ArrayDeque<>();
        Deque<String> lld2 = new ArrayDeque<>();

        lld1.addLast("front");
        lld1.addLast("middle");
        lld1.addLast("back");

        lld2.addLast("front");
        lld2.addLast("middle");
        lld2.addLast("back");

        assertThat(lld1.equals(lld2)).isTrue();

        Deque<Integer> deque1 = new LinkedListDeque<>();
        deque1.addFirst(0);
        deque1.addLast(1);
        deque1.addLast(2);

        Deque<Integer> deque2 = new ArrayDeque<>();
        deque2.addFirst(0);
        deque2.addLast(1);
        deque2.addLast(2);

        Deque<Integer> deque3 = new ArrayDeque<>();
        deque3.addFirst(3);
        deque3.addLast(4);
        deque3.addLast(5);

        assertThat(deque1.equals(deque2)).isTrue();
        assertThat(deque2.equals(deque1)).isTrue();
        assertThat(deque3.equals(deque2)).isFalse();
    }

    @Test
    public void toStringTest() {
        Deque<String> lld1 = new ArrayDeque<>();

        lld1.addLast("front");
        lld1.addLast("middle");
        lld1.addLast("back");

        System.out.println(lld1);
    }
}
