import deque.MaxArrayDeque;
import org.junit.jupiter.api.*;
import java.util.Comparator;
import static com.google.common.truth.Truth.assertThat;

public class MaxArrayDequeTest {

    @Test
    public void testComparator() {
        MaxArrayDeque<String> deque = new MaxArrayDeque<>(new LengthComparator());

        deque.addLast("first");
        deque.addLast("second");
        deque.addLast("third");
        deque.addLast("fourth");
        deque.addLast("fifth");

        String max = deque.max();

        assertThat(max).isEqualTo("second");
    }
    class LengthComparator implements Comparator<String> {
        @Override
        public int compare(String a, String b) {
            return Integer.compare(a.length(), b.length());
        }
    }
}
