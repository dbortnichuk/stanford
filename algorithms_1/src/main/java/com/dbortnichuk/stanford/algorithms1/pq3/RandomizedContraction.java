package com.dbortnichuk.stanford.algorithms1.pq3;

import com.dbortnichuk.exception.PerformanceEvaluatorException;
import com.dbortnichuk.utils.PerformanceEvaluator;
import com.dbortnichuk.utils.Utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * User: dbortnichuk
 * Date: 8/14/14
 */
public class RandomizedContraction {


    Map<Integer, List<Integer>> graph;
    //List<Integer> vertices;

    public RandomizedContraction(String filePath) {
        BufferedReader reader = null;
        try {
            URL fileURL = getClass().getResource(filePath);
            String absoluteFilePath = fileURL.getPath();
            reader = new BufferedReader(new FileReader(absoluteFilePath));
            String line = null;
            int lineNumber = 0;
            graph = new HashMap<Integer, List<Integer>>();
            //vertices = new ArrayList<Integer>();
            List<String> invalidData = new ArrayList<String>();
            while ((line = reader.readLine()) != null) {
                try {
                    List<String> lineComponents = Arrays.asList(line.split("\\s+"));
                    Integer vertex = Integer.parseInt(lineComponents.get(0));
                    //vertices.add(vertex);
                    List<Integer> vertexRelations = new CopyOnWriteArrayList<Integer>();
                    for (int i = 1; i < lineComponents.size(); i++) {
                        Integer vertexRelation = Integer.parseInt(lineComponents.get(i));
                        vertexRelations.add(vertexRelation);
                    }
                    graph.put(vertex, vertexRelations);
                } catch (IllegalArgumentException e) {
                    invalidData.add(line);
                }
                lineNumber++;
            }
            System.out.println("Valid input size: " + graph.size());
            System.out.println("Valid input: " + graph);
            System.out.println();
            System.out.println("Invalid input size: " + invalidData.size());
            System.out.println("Invalid input: " + invalidData);
            System.out.println();
            System.out.println("Total input size: " + lineNumber);
            System.out.println();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public Map<Integer, List<Integer>> getGraph() {
        return graph;
    }

    public static void main(String[] args) throws PerformanceEvaluatorException {
        PerformanceEvaluator performanceEvaluator = PerformanceEvaluator.newInstance();

        RandomizedContraction randomizedContraction = new RandomizedContraction("/testInput.txt");
        Map<Integer, List<Integer>> inputData = randomizedContraction.getGraph();

        performanceEvaluator.start();
        int minCut = randomizedContraction.findMinCut(inputData);
        performanceEvaluator.stop();

        System.out.println("Min cut: " + minCut);

    }

    public int findMinCut(Map<Integer, List<Integer>> graph) {
        int verticesNumber = graph.size();

        if (verticesNumber > 1) {
            int contractionTries = getContractionTries(verticesNumber);
            List<Integer> cuts = new ArrayList<Integer>();

            for (int i = 1; i <= contractionTries; i++) {
                Map<Integer, List<Integer>> graphToProcess = getInputCopy(graph);
                int cut = contract(graphToProcess);
                cuts.add(cut);
            }
            return Utils.getMin(cuts);
        }
        return -1;
    }

    private Map<Integer, List<Integer>> getInputCopy(Map<Integer, List<Integer>> input) {
        Map<Integer, List<Integer>> inputCopy = new HashMap<Integer, List<Integer>>();
        for (Map.Entry<Integer, List<Integer>> entry : input.entrySet()) {
            List<Integer> value = new CopyOnWriteArrayList<Integer>(entry.getValue());
            inputCopy.put(entry.getKey(), value);
        }
        return inputCopy;
    }

    private int getContractionTries(int verticesNumber) {
        int n = verticesNumber;
        return (int) (((n * (n - 1)) / 2) * Math.log(n));
    }


    private int contract(Map<Integer, List<Integer>> graph) {

        while (graph.size() > 2) {
            List<Integer> randomEdge = getRandomEdge(graph);
            merge(graph, randomEdge);
        }
        List<Integer> vertices = new ArrayList<Integer>(graph.keySet());
        int cut = graph.get(vertices.get(0)).size();
        return cut;
    }

    private List<Integer> getRandomEdge(Map<Integer, List<Integer>> graph) {
        List<Integer> vertices = new ArrayList<Integer>(graph.keySet());
        List<Integer> edge = new ArrayList<Integer>(2);

        int random = Utils.getRandomInt(0, vertices.size() - 1);
        Integer edgeStart = vertices.get(random);
        List<Integer> edgeRelations = graph.get(edgeStart);

        random = Utils.getRandomInt(0, edgeRelations.size() - 1);
        Integer edgeEnd = edgeRelations.get(random);

        edge.add(edgeStart);
        edge.add(edgeEnd);

        return edge;
    }

    private void merge(Map<Integer, List<Integer>> graph, List<Integer> edge) {

        Integer startVertex = edge.get(0);
        Integer endVertex = edge.get(1);
        List<Integer> startVertexRelations = graph.get(startVertex);
        List<Integer> endVertexRelations = graph.get(endVertex);

        //attach node 2’s list to node 1’s
        for (Integer endVertexRelation : endVertexRelations) {
            startVertexRelations.add(endVertexRelation);
        }

        //for 2’s adjacent nodes, scan their lists and replace all occurrence of 2 as 1
        for (Integer endVertexRelation : endVertexRelations) {
            List<Integer> endVertexRelationRelations = graph.get(endVertexRelation);
            for (int i = 0; i < endVertexRelationRelations.size() - 1; i++) {
                Integer endVertexRelationRelation = endVertexRelationRelations.get(i);
                if (endVertexRelationRelation.equals(endVertex)) {
                    endVertexRelationRelations.set(i, startVertex);
                    //endVertexRelationRelation = startVertex;
                }
            }

        }

        //remove self-loops
        for (Integer startVertexRelation : startVertexRelations) {
            if (startVertexRelation.equals(startVertex)) {
                startVertexRelations.remove(startVertexRelation);
            }
        }

        graph.remove(endVertex);
    }


}
