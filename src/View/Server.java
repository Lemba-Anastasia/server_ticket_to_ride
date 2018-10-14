package View;

import View.View;
import model.Base;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server implements Runnable{
    private View view;
    private List<MonoThreadClientHandler> threadClientHandlerList;
    private List<Thread> threadList;
    private ServerSocket server;
    private Socket socket;
    private Base base;

    public Server(View view) {
        this.view = view;
        threadClientHandlerList = new ArrayList<>(10);
        threadList = new ArrayList<>();
        try {
            server = new ServerSocket(1357);
        }catch (IOException e){
            view.addComandInArea(e.getMessage());
        }
        base=new Base();
    }

    public void run() {
        view.addComandInArea("Waiting for client connection");
        while (!server.isClosed()) {
            try {
                socket = server.accept();
            } catch (IOException e) {
                view.addComandInArea(e.getMessage());
            }
            MonoThreadClientHandler monoThreadClientHandler = null;
            try {
                monoThreadClientHandler = new MonoThreadClientHandler(socket,
                        new BufferedReader(new InputStreamReader(socket.getInputStream())),
                        new PrintWriter(socket.getOutputStream(), true), base);
            } catch (IOException e) {
                view.addComandInArea(e.getMessage());
            }
            threadClientHandlerList.add(monoThreadClientHandler);
            view.addComandInArea("Someone connected");
            Thread thead = new Thread(monoThreadClientHandler);
            thead.start();
            threadList.add(thead);
        }
    }
}
