package controllers;

import model.Edge;
import model.GraphOfCities;
import model.Node;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Integer.parseInt;

public class WorkWithFile {
    private GraphOfCities graphOfCities;
    private List<String> listMission;

    public WorkWithFile(){
        graphOfCities=new GraphOfCities();
        listMission=new ArrayList<>();
    }

    public GraphOfCities createGraph(){
        loadNode();
        loadBond();
        return graphOfCities;
    }
    private void loadNode(){
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream("src\\cities_coordinate.txt"), StandardCharsets.UTF_8))){
            String line;
            while ((line = reader.readLine()) != null) {
                String[] array=line.split(",");
                Node node=new Node(array[0],parseInt(array[1]),parseInt(array[2]));
                graphOfCities.addNode(node);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadBond(){
        String[] arrayOfNodeEdge;
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream("src\\engeOfVertexes.txt"), StandardCharsets.UTF_8))){
            String line; int i=0;
            while ((line = reader.readLine()) != null ) {
                listMission.add(line);
                arrayOfNodeEdge=line.split("~");
                Node n1=graphOfCities.getNodeByName(arrayOfNodeEdge[0].trim());
                Node n2=graphOfCities.getNodeByName(arrayOfNodeEdge[1].trim());
                Edge edge=new Edge(n1,n2);
                graphOfCities.addEdge(edge);
                if(i<5){
                    edge.setBuilt(true);
                }
                i++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
