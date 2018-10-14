package model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;

public class Base {
    private final ArrayList<User> usersList;
    private final Deque<User> queueOfWaitingUser;

    public Base() {
        usersList = new ArrayList<>();
        queueOfWaitingUser = new LinkedList<>();
    }

    public ArrayList<User> getUsersList() {
        return usersList;
    }

    public void addUser(User user) {
        synchronized (usersList) {
            usersList.add(user);
        }
    }

    public void addWaitingUser(User user) {
        synchronized (queueOfWaitingUser) {
            queueOfWaitingUser.add(user);
        }
    }

    public void tryToCreateChat() {
        synchronized (queueOfWaitingUser) {
            User freeUser = queueOfWaitingUser.peekFirst();
            if (freeUser != null) {
                queueOfWaitingUser.removeFirst();
                User freeUser2 = queueOfWaitingUser.peekFirst();
                if (freeUser2 != null) {
                    freeUser.setCompanion(freeUser2);
                    freeUser2.setCompanion(freeUser);
                    queueOfWaitingUser.removeFirst();
                    synchronized (usersList) {
                        usersList.add(freeUser);
                        usersList.add(freeUser2);
                    }
                    try {
                        freeUser.sendMessageToMyself("server: You are connected to " + freeUser2.getName());
                        freeUser2.sendMessageToMyself("server: You are connected to " + freeUser.getName());
                        freeUser2.getSocket().getOutputStream().flush();
                        freeUser.clearBuffer();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    synchronized (queueOfWaitingUser){
                        queueOfWaitingUser.add(freeUser);
                        freeUser.sendMessageToMyself("server: waiting a companion");
                    }
                }
            }
        }
    }

    public void exit(User user) {
        queueOfWaitingUser.add(user.getCompanion());
        user.getCompanion().setCompanion(null);
        tryToCreateChat();
        System.out.println(user.getName() + " left the chat");
        user.getCompanion().sendMessageToMyself("server: Companion disconnected");
        try {
            user.getSocket().close();
            remove(user);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void remove(User client) {
        for (User u : usersList) {
            if (u == client) {
                usersList.remove(u);
                return;
            }
        }
    }
}
