package View;

import controllers.Controller;
import model.Base;
import model.User;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class MonoThreadClientHandler implements Runnable{
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private User user;
    private Base base;
    private Controller controller;

    public MonoThreadClientHandler(Socket socket, BufferedReader in, PrintWriter out, Base base) {
        this.socket = socket;
        this.in = in;
        this.out = out;
        this.base=base;
        controller=new Controller();
    }

    @Override
    public void run() {
        System.out.println("Server reads the channel");
        try {
            String message;
            while (!socket.isClosed() && (message = in.readLine()) != null) {
                handlingCommandsMessage(message);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());//TODO:log
        }
    }

    private void handlingCommandsMessage(String m) throws IOException {
        if (m.matches("/reg(\\s+)\\w+")) {
            if (user != null) {
                out.println("server: You are aleady registered");
                return;
            }
            String clientName = m.split("/reg(\\s+)")[1];
                System.out.println("UserNAME:" + clientName);
            User user = new User(clientName, socket);
            base.addWaitingUser(user);
            this.user = user;
            base.tryToCreateChat();
            user.sendMessageToMyself("server: You have registred");
            controller.setUser(user);
        } else if (m.substring(0,8).equals("mission:")) {
            if(user!=null){
                m=m.substring(8,m.length());
                String startPoint=m.split(" - ")[0];
                String endPoint=m.split(" - ")[1];
                controller.setMission(m);
                controller.setStartPunkt(controller.getNodeByName(startPoint));
                controller.setEndNodeRoute(controller.getNodeByName(endPoint));
            }
        } else if(m.equals("/activate intident node")){
            controller.activateIntidentNode();
        } else if(m.equals("/buid a segment a rail way")){
            controller.buidRailWay();
        }
        else if (m.matches("/close")) {
            user.sendMessageToMyself("server: You have left");
            base.exit(user);
        }
    }
}
