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
                Map<Integer, List<Integer>> graphToProcess = new HashMap<Integer, List<Integer>>(graph);
                int cut = contract(graphToProcess);
                cuts.add(cut);
            }

            int minCut = Integer.MAX_VALUE;
            for (int cut : cuts) {
                if (cut < minCut) {
                    minCut = cut;
                }
            }
            return minCut;
        }
        return -1;
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
        List<Integer> edge = new ArrayList<Integer>();

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

        //for 2’s adjacent nodes 1,3,4, scan their lists and replace all occurrence of 2 as 1
        for (Integer endVertexRelation : endVertexRelations) {
            List<Integer> endVertexRelationRelations = graph.get(endVertexRelation);
            for (int i = 0; i < endVertexRelationRelations.size() - 1; i++ ) {
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


//    private static class Graph {
//        private final Map<Integer, Vertex> vertices = new TreeMap<Integer, Vertex>( new Comparator<Integer>() {
//            //for pretty printing
//            @Override
//            public int compare( Integer arg0, Integer arg1 ) {
//                return arg0.compareTo( arg1 );
//            }
//        });
//        private final List<Edge> edges = new ArrayList<Edge>();
//        public void addVertex( Vertex v ) {
//            vertices.put( v.lbl, v );
//        }
//        public Vertex getVertex( int lbl ) {
//            Vertex v;
//            if( ( v = vertices.get( lbl )) == null ) {
//                v = new Vertex( lbl );
//                addVertex( v );
//            }
//            return v;
//        }
//    }
//
//    private static class Vertex<T> {
//        private final T entity;
//        private final Set<Edge> edges = new HashSet<Edge>();
//
//        public Vertex(T entity) {
//            this.entity = entity;
//        }
//        public void addEdge( Edge edge ) {
//            edges.add( edge );
//        }
//        public Edge getEdgeTo( Vertex v2 ) {
//            for ( Edge edge : edges ) {
//                if( edge.contains( this, v2 ) )
//                    return edge;
//            }
//            return null;
//        }
//    }
//
//    private static class Edge {
//        private final List<Vertex> ends = new ArrayList<Vertex>();
//        public Edge( Vertex fst, Vertex snd ) {
//            if( fst == null || snd == null ) {
//                throw new IllegalArgumentException( "Both vertices are required" );
//            }
//            ends.add( fst );
//            ends.add( snd );
//        }
//        public boolean contains( Vertex v1, Vertex v2 ) {
//            return ends.contains( v1 ) && ends.contains( v2 );
//        }
//        public Vertex getOppositeVertex( Vertex v ) {
//            if( !ends.contains( v ) ) {
//                throw new IllegalArgumentException( "Vertex " + v.lbl );
//            }
//            return ends.get( 1 - ends.indexOf( v ) );
//        }
//        public void replaceVertex( Vertex oldV, Vertex newV ) {
//            if( !ends.contains( oldV ) ) {
//                throw new IllegalArgumentException( "Vertex " + oldV.lbl );
//            }
//            ends.remove( oldV );
//            ends.add( newV );
//        }
//    }


}
