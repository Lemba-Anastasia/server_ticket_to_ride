package controllers;

import model.Edge;
import model.GraphOfCities;
import model.Node;
import model.User;

import java.awt.*;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;

public class Controller {
    private GraphOfCities graphOfCities;
    private WorkWithFile workWithFile;
    private Deque<Node> queueNodeOfIncidentANode;
    private Node startPunkt;
    private int budget = 50;
    private int poinsForDoingRoad = 0;
    private Node endNodeRoute;
    private Node punktB;
    private User user;

    private String mission;

    public Controller() {
        workWithFile = new WorkWithFile();
        graphOfCities = workWithFile.createGraph();
        queueNodeOfIncidentANode = new LinkedList<>();
    }

    public java.util.List<Point> getNodePoitsList() {
        java.util.List<Point> pointList = new ArrayList<>();
        for (Node n : graphOfCities.getNodeList()) {
            pointList.add(n.getPoint());
        }
        return pointList;
    }

    public java.util.List<Edge> getEdgeList() {
        return graphOfCities.getEdgeList();
    }

    public java.util.List<Node> getNodeList() {
        return graphOfCities.getNodeList();
    }

    public Node activateIntidentNode() {
        if(!queueNodeOfIncidentANode.isEmpty()){
            if (punktB != null)
                user.getCompanion().sendMessageToMyself("C:/doNotActiveNode: " + punktB.getName());
            Node n = queueNodeOfIncidentANode.getFirst();
            n.setActive(true);
            for (Node node : queueNodeOfIncidentANode) {
                if (node != n) node.setActive(false);
            }
            queueNodeOfIncidentANode.removeFirst();
            queueNodeOfIncidentANode.add(n);
            punktB = n;
            user.getCompanion().sendMessageToMyself("C:/doActiveNode: " + punktB.getName());
            user.sendMessageToMyself("/doActiveInzidentNode: "+punktB.getName());
            return punktB;
        }
        return null;
    }

    public void updateQueueOfIntidentNodes() {
        startPunkt.setActive(true);
        Node inzidentNode = null;
        for (Edge edge : graphOfCities.getEdgeList()) {
            if (edge.containNode(startPunkt)) {
                inzidentNode = edge.getAnotherNode(startPunkt);
                queueNodeOfIncidentANode.add(inzidentNode);
            }
        }
        user.sendMessageToMyself("/doUpdaitGraphForActivationNode: " + queueNodeOfIncidentANode.peekFirst().getName() + "," + startPunkt.getName());
        user.getCompanion().sendMessageToMyself("C:/doActiveNode: " + queueNodeOfIncidentANode.peekFirst().getName() + "," + startPunkt.getName());
    }

    public void buidRailWay() {
        int costForASegment = 5;
        if (!graphOfCities.searchEdgeByNodes(startPunkt, punktB).isBuilt()) {
            if (budget >= costForASegment) {
                searchEdgeByNodes(startPunkt, punktB).setBuilt(true);
                user.sendMessageToMyself("/buildRoad: " + startPunkt.getName() + "," + punktB.getName());
                user.getCompanion().sendMessageToMyself("C:/buildRoad: " + startPunkt.getName() + "," + punktB.getName());
                budget -= costForASegment;
                poinsForDoingRoad += 10;
                user.updaitInfoOfGame(budget, poinsForDoingRoad);
                user.getCompanion().updaitInfoOfGame(budget, poinsForDoingRoad);
                if (!isDoingMission())
                    recourseOfTheGame(punktB);
            } else {
                user.sendMessageToMyself("/gameOver");//TODO: LISTENING
                user.getCompanion().sendMessageToMyself("sever: companion finish game");
            }
        } else {
            if (!isDoingMission())
                recourseOfTheGame(punktB);
        }
    }

    private boolean isDoingMission() {
        if (endNodeRoute == punktB) {
            endNodeRoute.setActive(false);
            user.sendMessageToMyself("/doNotActiveNode: " + endNodeRoute.getName());
            user.sendMessageToMyself("/doNotActiveNode: " + startPunkt.getName());
            user.sendMessageToMyself("/showFinishMission");
            user.getCompanion().sendMessageToMyself("C:/doNotActiveNode: " + endNodeRoute.getName());
            user.getCompanion().sendMessageToMyself("C:/doNotActiveNode: " + startPunkt.getName());
            user.getCompanion().sendMessageToMyself("server: companion have doing mission");
            System.out.println(user.getName() + " do mission");
            poinsForDoingRoad += 20;
            user.updaitInfoOfGame(budget, poinsForDoingRoad);
            user.sendMessageToMyself("/updaitMission: " + mission);
            queueNodeOfIncidentANode.clear();
            user.sendMessageToMyself("/clearNodesOfIncidentNode");
            user.sendMessageToMyself("C:/clearNodesOfIncidentNode");
            return true;
        }
        return false;
    }

    private void recourseOfTheGame(Node punktB) {
        user.sendMessageToMyself("/clearNodesOfIncidentNode");
        user.sendMessageToMyself("C:/clearNodesOfIncidentNode");
        startPunkt = punktB;
        queueNodeOfIncidentANode.clear();
        updateQueueOfIntidentNodes();
    }

    public Node getNodeByName(String name) {
        for (Node node : graphOfCities.getNodeList()) {
            if (node.getName().equals(name)) {
                return node;
            }
        }
        return null;
    }

    public Node getStartPunkt() {
        return startPunkt;
    }

    public void setStartPunkt(Node startPunkt) {
        this.startPunkt = startPunkt;
        updateQueueOfIntidentNodes();
    }

    public Edge searchEdgeByNodes(Node a, Node b) {
        return graphOfCities.searchEdgeByNodes(a, b);
    }

    public int getBudget() {
        return budget;
    }

    public void setEndNodeRoute(Node endNodeRoute) {
        this.endNodeRoute = endNodeRoute;
    }

    public int getPoinsForDoingRoad() {
        return poinsForDoingRoad;
    }

    public void setMission(String mission) {
        this.mission = mission;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
