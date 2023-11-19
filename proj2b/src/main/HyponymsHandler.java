package main;

import browser.NgordnetQuery;
import browser.NgordnetQueryHandler;
import ngrams.NGramMap;
import ngrams.TimeSeries;

import java.util.*;

public class HyponymsHandler extends NgordnetQueryHandler {

    private WordNet wn;
    private Graph graph;
    private int startYear;
    private int endYear;
    private int k;
    private NGramMap ngramMap;

    public HyponymsHandler(WordNet wn) {
        this.wn = wn;
        this.graph = wn.graphBuilder();
    }

    public HyponymsHandler(WordNet wn, NGramMap nGramMap) {
        this.wn = wn;
        this.graph = wn.graphBuilder();
        this.ngramMap = nGramMap;
    }

    @Override
    public String handle(NgordnetQuery q) {
        List<String> words = q.words();
        startYear = q.startYear();
        endYear = q.endYear();
        k = q.k();

        TreeMap<String, Integer> hyponymResult = new TreeMap<>();

        for (String word : words) {
            List<String> hyponyms = graph.findHyponyms(word); // timeout exception call here
            for (String h : hyponyms) {
                if (hyponymResult.containsKey(h)) {
                    hyponymResult.put(h, hyponymResult.get(h) + 1);
                } else {
                    hyponymResult.put(h, 1);
                }
            }
        }
        List<String> hyponymList = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : hyponymResult.entrySet()) { // for every id in adjList
            String h = entry.getKey();
            int counter = entry.getValue();
            if (counter == q.words().size()) {
                hyponymList.add(h);
            }
        }

        List<String> resultList = new ArrayList<>();
        TreeMap<Double, ArrayList<String>> hCountSorted = new TreeMap<>(Collections.reverseOrder());
        if (k != 0) {
            for (String word : hyponymList) {
                TimeSeries count = ngramMap.countHistory(word, startYear, endYear);
                double sum = 0;
                for (double value : count.values()) {
                    sum += value; // occurrences
                }
                // compare and sort
                if (sum != 0) {
                    if (hCountSorted.containsKey(sum)) {
                        hCountSorted.get(sum).add(word);
                    } else {
                        ArrayList<String> wordInput = new ArrayList<>();
                        wordInput.add(word);
                        hCountSorted.put(sum, wordInput);
                    }
                }
            }
            for (ArrayList<String> v : hCountSorted.values()) {
                resultList.addAll(v);
            }

            List<String> topKHyponyms;
            if (resultList.size() >= k) {
                topKHyponyms = resultList.subList(0, k);
            } else {
                topKHyponyms = resultList;
            }

            TreeSet<String> kHyponyms = new TreeSet<>();
            for (int i = 0; i < topKHyponyms.size(); i++) {
                kHyponyms.add(topKHyponyms.get(i));
            }
            resultList.clear();
            resultList.addAll(kHyponyms);
        } else {
            resultList = hyponymList;
        }

        StringBuilder result = new StringBuilder("[");
        int index = 0;
        for (String hyponym : resultList) {
            result.append(hyponym);
            if (index < resultList.size() - 1) {
                result.append(", ");
            }
            index++;
        }
        result.append("]");
        return result.toString();
    }
}
