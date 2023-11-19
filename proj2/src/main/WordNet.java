package main;

import edu.princeton.cs.algs4.In;

import java.util.*;

public class WordNet {
    private Graph graph;

    public WordNet(String synsetsFilename, String hyponymsFilename) {
        int verticesNum = countVertices(synsetsFilename);
        graph = new Graph(verticesNum);
        synsetsFilenameReader(synsetsFilename);
        hyponymsFilenameReader(hyponymsFilename);
    }
    public Graph graphBuilder() {
        return graph;
    }

    private void synsetsFilenameReader(String synsetsFilename) {
        In synsetsFile = new In(synsetsFilename);
        while (synsetsFile.hasNextLine()) {
            String currentLine = synsetsFile.readLine();
            String[] data = currentLine.split(",");
            int synsetsID = Integer.parseInt(data[0]);
            String[] synsetsWords = data[1].split(" ");
            String synsetsDefinition = data[2];
//            HashMap<Integer, String[]> synsetStore = new HashMap<Integer, String[]>();
//            synsetStore.put(synsetsID, synsetsWord); // useless
            for (String word : synsetsWords) {
                graph.addToWords(word, synsetsID);
            }
            graph.addToStore(synsetsID, List.of(synsetsWords));
        }
        synsetsFile.close();
    }

    private void hyponymsFilenameReader(String hyponymsFilename) {
        In hyponymsFile = new In(hyponymsFilename);
        while (hyponymsFile.hasNextLine()) {
            String currentLine = hyponymsFile.readLine();
            String[] data = currentLine.split(",");
            int synsetsID = Integer.parseInt(data[0]);

            List<Integer> relation = new ArrayList<>();
            for (int i = 1; i < data.length; i++) {
                int hyponymID = Integer.parseInt(data[i]);
                graph.addEdge(synsetsID, hyponymID);
                relation.add(hyponymID);
            }
            graph.addToRelations(synsetsID, relation);
        }
        hyponymsFile.close();
    }

    private int countVertices(String synsetsFilename) {
        In synsetsFile = new In(synsetsFilename);
        int verticesNum = 0;
        while (synsetsFile.hasNextLine()) {
            verticesNum++;
            synsetsFile.readLine();
        }
        synsetsFile.close();
        return verticesNum;
    }
}
