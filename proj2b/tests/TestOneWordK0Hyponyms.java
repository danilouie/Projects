import browser.NgordnetQuery;
import browser.NgordnetQueryHandler;
import main.Graph;
import main.WordNet;
import ngrams.NGramMap;
import org.junit.jupiter.api.Test;
import main.AutograderBuddy;
import main.HyponymsHandler;

import java.util.List;

import static com.google.common.truth.Truth.assertThat;

/** Tests the most basic case for Hyponyms where the list of words is one word long, and k = 0.*/
public class TestOneWordK0Hyponyms {
    // this case doesn't use the NGrams dataset at all, so the choice of files is irrelevant
    public static final String WORDS_FILE = "data/ngrams/top_14377_words.csv";
    public static final String TOTAL_COUNTS_FILE = "data/ngrams/total_counts.csv";
    public static final String SMALL_SYNSET_FILE = "data/wordnet/synsets16.txt";
    public static final String SMALL_HYPONYM_FILE = "data/wordnet/hyponyms16.txt";

    @Test
    public void testActK0() {
        NgordnetQueryHandler studentHandler = AutograderBuddy.getHyponymHandler(
                WORDS_FILE, TOTAL_COUNTS_FILE, SMALL_SYNSET_FILE, SMALL_HYPONYM_FILE);
        List<String> words = List.of("act");

        NgordnetQuery nq = new NgordnetQuery(words, 0, 0, 0);
        String actual = studentHandler.handle(nq);
        String expected = "[act, action, change, demotion, human_action, human_activity, variation]";
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void testChangeAndOccurence() {
        WordNet wordNet = new WordNet("./data/wordnet/synsets11.txt", "./data/wordnet/hyponyms11.txt");

        NgordnetQueryHandler studentHandler = new HyponymsHandler(wordNet);
        List<String> words = List.of("action");

        NgordnetQuery nq = new NgordnetQuery(words, 0, 0, 0);
        String actual = studentHandler.handle(nq);
        String expected = "[action, change, demotion]";
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void testIncrease() {
        WordNet wordNet = new WordNet("./data/wordnet/synsets11.txt", "./data/wordnet/hyponyms11.txt");

        NgordnetQueryHandler studentHandler = new HyponymsHandler(wordNet);
        List<String> words = List.of("increase");

        NgordnetQuery nq = new NgordnetQuery(words, 0, 0, 0);
        String actual = studentHandler.handle(nq);
        String expected = "[augmentation, increase, jump, leap]";
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void testActifed() {
        WordNet wordNet = new WordNet("./data/wordnet/synsets11.txt", "./data/wordnet/hyponyms11.txt");

        NgordnetQueryHandler studentHandler = new HyponymsHandler(wordNet);
        List<String> words = List.of("antihistamine");

        NgordnetQuery nq = new NgordnetQuery(words, 0, 0, 0);
        String actual = studentHandler.handle(nq);
        String expected = "[actifed, antihistamine]";
        assertThat(actual).isEqualTo(expected);
    }
    @Test
    public void testAbstractionK6() {
        WordNet wordNet = new WordNet("./data/wordnet/synsets.txt", "./data/wordnet/hyponyms.txt");
        NGramMap nGramMap = new NGramMap("./data/ngrams/top_14377_words.csv", "./data/ngrams/total_counts.csv");

        NgordnetQueryHandler studentHandler = new HyponymsHandler(wordNet, nGramMap);
        List<String> words = List.of("abstraction");

        NgordnetQuery nq = new NgordnetQuery(words, 1470, 2019, 6);
        String actual = studentHandler.handle(nq);
        String expected = "[are, at, he, in, one, will]";
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void testObject() {
        WordNet wordNet = new WordNet("./data/wordnet/synsets.txt", "./data/wordnet/hyponyms.txt");
        NGramMap nGramMap = new NGramMap("./data/ngrams/top_14377_words.csv", "./data/ngrams/total_counts.csv");

        NgordnetQueryHandler studentHandler = new HyponymsHandler(wordNet, nGramMap);
        List<String> words = List.of("object");

        NgordnetQuery nq = new NgordnetQuery(words, 1470, 2019, 2);
        String actual = studentHandler.handle(nq);
        String expected = "[can, have]";
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void testPart() {
        WordNet wordNet = new WordNet("./data/wordnet/synsets.txt", "./data/wordnet/hyponyms.txt");
        NGramMap nGramMap = new NGramMap("./data/ngrams/top_14377_words.csv", "./data/ngrams/total_counts.csv");

        NgordnetQueryHandler studentHandler = new HyponymsHandler(wordNet, nGramMap);
        List<String> words = List.of("part");

        NgordnetQuery nq = new NgordnetQuery(words, 1470, 2019, 4);
        String actual = studentHandler.handle(nq);
        String expected = "[can, do, so, two]";
        assertThat(actual).isEqualTo(expected);
    }

//    @Test
//    public void testOrganization() {
//        WordNet wordNet = new WordNet("./data/wordnet/synsets.txt", "./data/wordnet/hyponyms.txt");
//        NGramMap nGramMap = new NGramMap("./data/ngrams/top_14377_words.csv", "./data/ngrams/total_counts.csv");
//
//        NgordnetQueryHandler studentHandler = new HyponymsHandler(wordNet, nGramMap);
//        List<String> words = List.of("organization");
//
//        NgordnetQuery nq = new NgordnetQuery(words, 1470, 2019, 0);
//        String actual = studentHandler.handle(nq);
//        String expected = "[]";
//        assertThat(actual).isEqualTo(expected);
//    }
}
