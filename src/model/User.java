package model;

import java.io.IOException;
import java.net.Socket;

public class User {

    private String userName;
    private Socket socket;
    private User companion;
    private String waitingPutMessages;
    public User(String clientName, Socket socket) {
        userName=clientName;
        this.socket=socket;
        companion=null;
        waitingPutMessages="";
    }

    public void sendMessageToMyself(String message)  {
        try {
            socket.getOutputStream().write((message+"\n").getBytes());
            socket.getOutputStream().flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updaitInfoOfGame(int budget, int points) {
        try {
            socket.getOutputStream().write(("BUDGET:"+budget+",POINTS:"+points+"\n").getBytes());
            socket.getOutputStream().flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isBusy() {
        return (companion!=null);
    }

    public void setCompanion(User companion) {
        this.companion=  companion;
    }

    public Socket getSocket() {
        return socket;
    }

    public User getCompanion(){
        return companion;
    }

    public String getName(){
        return userName;
    }

    public void setBufferMessages(String m){
        waitingPutMessages+=userName+": "+m+"\n";
    }

    public void clearBuffer(){waitingPutMessages="";}

    public String getWaitingMessages() {
        return waitingPutMessages;
    }

    public boolean isWaiting(){
        return !waitingPutMessages.equals("");
    }

    public void updateMission(String mission) {//TODO: missioncreater
        try {
            socket.getOutputStream().write((mission).getBytes());
            socket.getOutputStream().flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updaitGraph(String s) {
    }
}
