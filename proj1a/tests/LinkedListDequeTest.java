import jh61b.utils.Reflection;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.List;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth.assertWithMessage;

/** Performs some basic linked list tests. */
public class LinkedListDequeTest {

     @Test
     @DisplayName("LinkedListDeque has no fields besides nodes and primitives")
     void noNonTrivialFields() {
         Class<?> nodeClass = NodeChecker.getNodeClass(LinkedListDeque.class, true);
         List<Field> badFields = Reflection.getFields(LinkedListDeque.class)
                 .filter(f -> !(f.getType().isPrimitive() || f.getType().equals(nodeClass) || f.isSynthetic()))
                 .toList();

         assertWithMessage("Found fields that are not nodes or primitives").that(badFields).isEmpty();
     }

     @Test
     /** In this test, we have three different assert statements that verify that addFirst works correctly. */
     public void addFirstTestBasic() {
         Deque<String> lld1 = new LinkedListDeque<>();

         lld1.addFirst("back"); // after this call we expect: ["back"]
         assertThat(lld1.toList()).containsExactly("back").inOrder();

         lld1.addFirst("middle"); // after this call we expect: ["middle", "back"]
         assertThat(lld1.toList()).containsExactly("middle", "back").inOrder();

         lld1.addFirst("front"); // after this call we expect: ["front", "middle", "back"]
         assertThat(lld1.toList()).containsExactly("front", "middle", "back").inOrder();

         /* Note: The first two assertThat statements aren't really necessary. For example, it's hard
            to imagine a bug in your code that would lead to ["front"] and ["front", "middle"] failing,
            but not ["front", "middle", "back"].
          */
     }

     @Test
     /** In this test, we use only one assertThat statement. IMO this test is just as good as addFirstTestBasic.
      *  In other words, the tedious work of adding the extra assertThat statements isn't worth it. */
     public void addLastTestBasic() {
         Deque<String> lld1 = new LinkedListDeque<>();

         lld1.addLast("front"); // after this call we expect: ["front"]
         lld1.addLast("middle"); // after this call we expect: ["front", "middle"]
         lld1.addLast("back"); // after this call we expect: ["front", "middle", "back"]
         assertThat(lld1.toList()).containsExactly("front", "middle", "back").inOrder();
     }

     @Test
     /** This test performs interspersed addFirst and addLast calls. */
     public void addFirstAndAddLastTest() {
         Deque<Integer> lld1 = new LinkedListDeque<>();

         /* I've decided to add in comments the state after each call for the convenience of the
            person reading this test. Some programmers might consider this excessively verbose. */
         lld1.addLast(0);   // [0]
         lld1.addLast(1);   // [0, 1]
         lld1.addFirst(-1); // [-1, 0, 1]
         lld1.addLast(2);   // [-1, 0, 1, 2]
         lld1.addFirst(-2); // [-2, -1, 0, 1, 2]

         assertThat(lld1.toList()).containsExactly(-2, -1, 0, 1, 2).inOrder();
     }

    // Below, you'll write your own tests for LinkedListDeque.

    @Test
    public void toList() {
        LinkedListDeque<Integer> deque = new LinkedListDeque<>();
        assertThat(deque.toList()).isEmpty();

        deque.addFirst(1);
        deque.addLast(2);
        deque.addLast(3);

        assertThat(deque.toList()).containsExactly(1, 2, 3).inOrder();
    }

    @Test
    public void isEmptyTest() {
         LinkedListDeque<Integer> deque = new LinkedListDeque<>();
         assertThat(deque.isEmpty()).isTrue();

         deque.addFirst(0);
         deque.addFirst(1);
         deque.addLast(2);
         deque.addLast(3);

         assertThat(deque.isEmpty()).isFalse();
    }

    @Test
    public void sizeTest() {
         LinkedListDeque<Integer> deque = new LinkedListDeque<>();
         assertThat(deque.size()).isEqualTo(0);

         deque.addFirst(0);
         deque.addFirst(1);
         deque.addLast(2);
         deque.addLast(3);
         deque.addFirst(-1);

         assertThat(deque.size()).isEqualTo(5);
    }

    @Test
    public void getTest() {
         LinkedListDeque<Integer> deque = new LinkedListDeque<>();
         assertThat(deque.get(0)).isEqualTo(null);

         deque.addFirst(23);
         assertThat(deque.get(-1)).isEqualTo(null);

         deque.addFirst(0);
         deque.addLast(2);
         deque.addLast(7);
         assertThat(deque.get(2)).isEqualTo(2);
    }

    @Test
    public void getRecursiveTest() {
        LinkedListDeque<Integer> deque = new LinkedListDeque<>();
        assertThat(deque.getRecursive(0)).isEqualTo(null);

        deque.addFirst(23);
        assertThat(deque.getRecursive(-1)).isEqualTo(null);

        deque.addFirst(0);
        deque.addLast(2);
        deque.addLast(7);
        assertThat(deque.getRecursive(2)).isEqualTo(2);
    }

    @Test
    public void removeFirstTest() {
        Deque<Integer> deque = new LinkedListDeque<>();
        assertThat(deque.removeFirst()).isEqualTo(null);

        deque.addFirst(5);
        assertThat(deque.removeFirst()).isEqualTo(5);

        deque.addLast(1);
        deque.addLast(2);
        deque.addFirst(0);
        assertThat(deque.removeFirst()).isEqualTo(0);

        deque.addFirst(-1);
        assertThat(deque.toList()).containsExactly(-1, 1, 2).inOrder();

        assertThat(deque.removeFirst()).isEqualTo(-1);
        assertThat(deque.removeFirst()).isEqualTo(1);
        assertThat(deque.toList()).containsExactly(2).inOrder();
    }

    @Test
    public void removeLastTest() {
        Deque<Integer> deque = new LinkedListDeque<>();
        assertThat(deque.removeLast()).isEqualTo(null);

        deque.addLast(1);
        assertThat(deque.removeLast()).isEqualTo(1);

        deque.addLast(2);
        deque.addFirst(0);
        deque.addLast(3);
        assertThat(deque.removeLast()).isEqualTo(3);

        deque.addLast(4);
        assertThat(deque.toList()).containsExactly(0, 2, 4).inOrder();

        assertThat(deque.removeLast()).isEqualTo(4);
        assertThat(deque.removeLast()).isEqualTo(2);
        assertThat(deque.toList()).containsExactly(0).inOrder();
    }
}