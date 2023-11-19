import browser.NgordnetQuery;
import browser.NgordnetQueryHandler;
import main.AutograderBuddy;
import main.Graph;
import main.HyponymsHandler;
import main.WordNet;
import ngrams.NGramMap;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.google.common.truth.Truth.assertThat;

/** Tests the case where the list of words is length greater than 1, but k is still zero. */
public class TestMultiWordK0Hyponyms {
    // this case doesn't use the NGrams dataset at all, so the choice of files is irrelevant
    public static final String WORDS_FILE = "data/ngrams/top_14377_words.csv";
    public static final String TOTAL_COUNTS_FILE = "data/ngrams/total_counts.csv";
    public static final String SMALL_SYNSET_FILE = "data/wordnet/synsets16.txt";
    public static final String SMALL_HYPONYM_FILE = "data/wordnet/hyponyms16.txt";
    public static final String LARGE_SYNSET_FILE = "data/wordnet/synsets.txt";
    public static final String LARGE_HYPONYM_FILE = "data/wordnet/hyponyms.txt";

    /** This is an example from the spec.*/
    @Test
    public void testOccurrenceAndChangeK0() {
        NgordnetQueryHandler studentHandler = AutograderBuddy.getHyponymHandler(
                WORDS_FILE, TOTAL_COUNTS_FILE, SMALL_SYNSET_FILE, SMALL_HYPONYM_FILE);
        List<String> words = List.of("occurrence", "change");

        NgordnetQuery nq = new NgordnetQuery(words, 0, 0, 0);
        String actual = studentHandler.handle(nq);
        String expected = "[alteration, change, increase, jump, leap, modification, saltation, transition]";
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void testOccurrenceAndChange() {
        WordNet wordNet = new WordNet("./data/wordnet/synsets14.txt", "./data/wordnet/hyponyms14.txt");

        NgordnetQueryHandler studentHandler = new HyponymsHandler(wordNet);
        List<String> words = List.of("change", "occurrence");

        NgordnetQuery nq = new NgordnetQuery(words, 0, 0, 0);
        String actual = studentHandler.handle(nq);
        String expected = "[alteration, change, increase, jump, leap, modification, saltation, transition]";
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void testVideoAndRecording() {
        WordNet wordNet = new WordNet("./data/wordnet/synsets.txt", "./data/wordnet/hyponyms.txt");

        NgordnetQueryHandler studentHandler = new HyponymsHandler(wordNet);
        List<String> words = List.of("video", "recording");

        NgordnetQuery nq = new NgordnetQuery(words, 0, 0, 0);
        String actual = studentHandler.handle(nq);
        String expected = "[video, video_recording, videocassette, videotape]";
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void testPastryAndTart() {
        WordNet wordNet = new WordNet("./data/wordnet/synsets.txt", "./data/wordnet/hyponyms.txt");

        NgordnetQueryHandler studentHandler = new HyponymsHandler(wordNet);
        List<String> words = List.of("pastry", "tart");

        NgordnetQuery nq = new NgordnetQuery(words, 0, 0, 0);
        String actual = studentHandler.handle(nq);
        String expected = "[apple_tart, lobster_tart, quiche, quiche_Lorraine, tart, tartlet]";
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void testFoodAndCakeK5() {
        WordNet wordNet = new WordNet("./data/wordnet/synsets.txt", "./data/wordnet/hyponyms.txt");
        NGramMap nGramMap = new NGramMap("./data/ngrams/top_49887_words.csv", "./data/ngrams/total_counts.csv");

        NgordnetQueryHandler studentHandler = new HyponymsHandler(wordNet, nGramMap);
        List<String> words = List.of("food", "cake");

        NgordnetQuery nq = new NgordnetQuery(words, 1950, 1990, 5);
        String actual = studentHandler.handle(nq);
        String expected = "[biscuit, cake, kiss, snap, wafer]";
        assertThat(actual).isEqualTo(expected);
    }
}
