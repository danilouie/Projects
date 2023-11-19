package main;

import org.checkerframework.checker.units.qual.A;

import java.util.*;

public class Graph {

    private int verticesNum;
    private HashMap<Integer, LinkedList<Integer>> adjList;
    private HashMap<Integer, Boolean> marked;
    private int size;
    // synset id --> list of words in synset
    private HashMap<Integer, List<String>> synsetStore = new HashMap<Integer, List<String>>();

    // word --> List<ids in synsetStore>
    private HashMap<String, List<Integer>> words = new HashMap<String, List<Integer>>();

    // id --> String of ids (parse when needed)
    private HashMap<Integer, List<Integer>> relations = new HashMap<>();


    public Graph(int verticesNum) {
        this.verticesNum = verticesNum;
        adjList = new HashMap<Integer, LinkedList<Integer>>();
    }

    public void addToStore(int id, List<String> lst) {
        synsetStore.put(id, lst);
    }

    public void addToWords(String word, Integer id) {
        if (!words.containsKey(word)) {
            words.put(word, new ArrayList<>());
        }
        words.get(word).add(id);
//        words.put(word, id);
    }

    public void addToRelations(Integer id, List<Integer> relation) {
        relations.put(id, relation);
    }

    public void addEdge(int v, int w) {
        if (!adjList.containsKey(v)) {
            LinkedList<Integer> newList = new LinkedList<>();
            newList.add(w);
            adjList.put(v, newList);
        } else {
            adjList.get(v).add(w);
        }
    }

    public List<Integer> adjVertices(int v) {
        return adjList.get(v);
    }

    public List<String> findHyponyms(String word) {
//        ArrayList<String> hyponyms = new ArrayList<>(); // jump straight to synset instead of traversing through entire synset
//        marked = new HashMap<>();
//        for (Map.Entry<Integer, LinkedList<Integer>> entry : adjList.entrySet()) { // for every id in adjList
//            int synsetsID = entry.getKey();
//            if (!marked.containsKey(synsetsID)) {
//                dfs(synsetsID, word, false, hyponyms, marked);
//            }
//        } // timeout exception call in findHyponyms

        List<Integer> synsetIDs = words.get(word);
        if (synsetIDs == null) {
            synsetIDs = new ArrayList<>();
        }

        ArrayList<String> hyponyms = new ArrayList<>();
        for (int id : synsetIDs) {
            marked = new HashMap<>();
            marked.put(id, true);
            dfs(id, word, true, hyponyms, marked);
        }

        Set<String> set = new HashSet<>(hyponyms);
        hyponyms.clear();
        hyponyms.addAll(set);

        return hyponyms;
    }

    private void dfs(int v, String word, boolean indicator, ArrayList<String> result, HashMap<Integer, Boolean> mark) {
        boolean wordChecker = false;

        if (!indicator) {
            for (String i : synsetStore.get(v)) {
                if (i.equals(word)) {
                    mark.put(v, true);
                    wordChecker = true; // timeout exception call here
                    break;
                }
            }
        }

//        // retrieve synset id(s) corresponding to word
//        List<Integer> wordSynset = words.get(word);
//        mark.put(v, true);
//        wordChecker = true; // timeout exception call here

        // if word is in value add value to result
        if (indicator || wordChecker) {
            result.addAll(synsetStore.get(v));
//            System.out.println(synsetStore.get(v));
        }
//        result.add(word);

        if (adjVertices(v) != null) {
            for (int i : adjVertices(v)) {
                if (!mark.containsKey(i)) {
                    dfs(i, word, indicator || wordChecker, result, mark);
                }
            }
        }
    }
}
