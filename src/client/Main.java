package client;

import graph.Vertex;
import util.DijkstraGraphUtil;
import util.FileUtil;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        List<String> fileData = FileUtil.readStringListFileByPath("src/distancias.txt");
        DijkstraGraphUtil.addConnectionsByList(fileData);

        if (DijkstraGraphUtil.isGraphEmpty()) {
            System.out.println("\nNo vertex found");
        } else {
            Scanner scanner = new Scanner(System.in);
            int selectedOption;

            do {
                System.out.println("Enter the number to select an option: ");
                System.out.println("1 - Calculate the distance between two points");
                System.out.println("2 - Create a stack of vertex");
                System.out.println("3 - Create a queue of vertex");
                System.out.println("4 - Leave");
                selectedOption = scanner.nextInt();

                switch (selectedOption) {
                    case 1: calculateSmallestDistance(scanner); break;
                    case 2: buildStack(); break;
                    case 3: buildQueue(); break;
                    case 4: System.out.println("The system will shut down...");break;
                    default:
                        System.out.println("Invalid option."); break;
                }

            } while (selectedOption != 4);
            scanner.close();
        }
    }

    /**
     * Option 1 - Calculate the distance between two points
     */
    private static void calculateSmallestDistance(Scanner scanner) {
        String initialVertexName;
        String finalVertexName;

        initialVertexName = readVertexEnteredWhileNonexistent(scanner, "Initial vertex:");
        finalVertexName = readVertexEnteredWhileNonexistent(scanner, "Final vertex:");
        if (initialVertexName.equals(finalVertexName)) {
            System.out.println("Initial vertex and Final vertex are the same.");
            return;
        }

        Vertex initialVertex = DijkstraGraphUtil.getVertex(initialVertexName);
        Vertex finalVertex = DijkstraGraphUtil.getVertex(finalVertexName);

        String path = DijkstraGraphUtil.getSmallestDistanceBetweenVertices(initialVertex, finalVertex);

        System.out.println("Smallest path: " + path);
    }

    private static String readVertexEnteredWhileNonexistent(Scanner scanner, String message) {
        String vertexName;
        do {
            System.out.println(message);
            vertexName = scanner.next().toUpperCase();
        } while (!DijkstraGraphUtil.isVertexExistent(vertexName));

        return vertexName;
    }

    /**
     * Option 2 - Create a stack of vertex
     */
    private static void buildStack() {
        showCollectionValues("Stack", DijkstraGraphUtil.buildStackByWeights());
    }

    /**
     * Option 3 - Create a queue of vertex
     */
    private static void buildQueue() {
        showCollectionValues("Queue", DijkstraGraphUtil.buildQueueByWeights());
    }

    private static void showCollectionValues(String structureType, List<Double> collection) {
        System.out.println(structureType + ": ");
        if (!collection.isEmpty()) {
            StringBuilder stringBuilder = new StringBuilder();
            collection.forEach(value -> stringBuilder.append(" ").append(value).append("\n"));
            System.out.println(stringBuilder);
        }
    }




}