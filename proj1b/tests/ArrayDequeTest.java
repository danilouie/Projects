import jh61b.utils.Reflection;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.List;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth.assertWithMessage;

public class ArrayDequeTest {

    // @Test
    // @DisplayName("ArrayDeque has no fields besides backing array and primitives")
    // void noNonTrivialFields() {
    //     List<Field> badFields = Reflection.getFields(ArrayDeque.class)
    //             .filter(f -> !(f.getType().isPrimitive() || f.getType().equals(Object[].class) || f.isSynthetic()))
    //             .toList();

    //     assertWithMessage("Found fields that are not array or primitives").that(badFields).isEmpty();
    // }
    @Test
    public void addFirstTestBasic() {
        Deque<String> deque = new ArrayDeque<>();

        deque.addFirst("eighth");
        assertThat(deque.toList()).containsExactly("eighth").inOrder();

        deque.addFirst("seventh");
        assertThat(deque.toList()).containsExactly("seventh", "eighth").inOrder();

        deque.addFirst("sixth");
        assertThat(deque.toList()).containsExactly("sixth", "seventh", "eighth").inOrder();

        deque.addFirst("fifth");
        deque.addFirst("fourth");
        deque.addFirst("third");
        deque.addFirst("second");
        deque.addFirst("first");
        deque.addFirst("zero");
        assertThat(deque.toList()).containsExactly("zero", "first", "second", "third", "fourth", "fifth", "sixth", "seventh", "eighth").inOrder();
    }

    @Test
    public void addFirstAndAddLastTest() {
        Deque<Integer> deque = new ArrayDeque<>();

        deque.addLast(8);
        deque.addLast(27);
        deque.addFirst(19);
        deque.addLast(3);
        deque.addFirst(5);

        assertThat(deque.toList()).containsExactly(5, 19, 8, 27, 3).inOrder();

        deque.addFirst(0);
        deque.addLast(21);
        deque.addLast(50);
        deque.addLast(100);

        assertThat(deque.toList()).containsExactly(0, 5, 19, 8, 27, 3, 21, 50, 100).inOrder();
    }

    @Test
    public void isEmptyTest() {
        ArrayDeque<Integer> deque = new ArrayDeque<>();
        assertThat(deque.isEmpty()).isTrue();

        deque.addFirst(19);
        deque.addFirst(20);
        deque.addLast(27);
        deque.addLast(0);

        assertThat(deque.isEmpty()).isFalse();
    }

    @Test
    public void sizeTest() {
        ArrayDeque<Integer> deque = new ArrayDeque<>();
        assertThat(deque.size()).isEqualTo(0);

        deque.removeFirst();
        assertThat(deque.removeFirst()).isEqualTo(null);
        assertThat(deque.size()).isEqualTo(0);

        deque.addFirst(100);
        deque.removeLast();
        assertThat(deque.size()).isEqualTo(0);

        deque.addFirst(-100);
        deque.addLast(200);
        deque.addLast(300);
        deque.addFirst(-101);

        assertThat(deque.size()).isEqualTo(4);
    }

    @Test
    public void toList() {
        ArrayDeque<Integer> deque = new ArrayDeque<>();
        assertThat(deque.toList()).isEmpty();

        deque.addFirst(1);
        deque.addLast(2);
        deque.addLast(3);

        assertThat(deque.toList()).containsExactly(1, 2, 3).inOrder();
    }

    @Test
    public void getTest() {
        ArrayDeque<Integer> deque = new ArrayDeque<>();
        assertThat(deque.get(0)).isEqualTo(null);

        deque.addFirst(17);
        assertThat(deque.get(-1)).isEqualTo(null);

        deque.addFirst(18);
        deque.addLast(19);
        deque.addLast(20);
        assertThat(deque.get(2)).isEqualTo(19);

        deque.addLast(21);
        deque.addLast(22);
        deque.addFirst(16);
        deque.addFirst(15);
        assertThat(deque.get(0)).isEqualTo(15);
    }

    @Test
    public void removeFirstTest() {
        Deque<Integer> deque = new ArrayDeque<>();
        assertThat(deque.removeFirst()).isEqualTo(null);

        deque.addFirst(1);
        assertThat(deque.removeFirst()).isEqualTo(1);

        deque.addFirst(1);
        deque.addFirst(-7);
        assertThat(deque.removeFirst()).isEqualTo(-7);

        deque.addLast(25);
        deque.addLast(8);
        deque.addFirst(-10);
        assertThat(deque.toList()).containsExactly(-10, 1, 25, 8).inOrder();

        for (int i = 0; i < 21; i++) {
            deque.addLast(i);
        }

        for (int i = 0; i < 20; i++) {
            deque.removeFirst();
        }

        assertThat(deque.toList()).containsExactly(16, 17, 18, 19, 20);
    }

    @Test
    public void removeLastTest() {
        Deque<Integer> deque = new ArrayDeque<>();
        assertThat(deque.removeLast()).isEqualTo(null);

        deque.addLast(77);
        assertThat(deque.removeLast()).isEqualTo(77);

        deque.addLast(78);
        deque.addFirst(76);
        deque.addLast(79);
        assertThat(deque.removeLast()).isEqualTo(79);

        deque.addLast(80);
        assertThat(deque.toList()).containsExactly(76, 78, 80).inOrder();

        assertThat(deque.removeLast()).isEqualTo(80);
        assertThat(deque.removeLast()).isEqualTo(78);
        assertThat(deque.toList()).containsExactly(76).inOrder();

        for (int i = 0; i < 24; i++) {
            deque.addLast(i);
        }

        for (int i = 0; i < 20; i++) {
            deque.removeLast();
        }

        assertThat(deque.toList()).containsExactly(76, 0, 1, 2, 3);
    }
}
