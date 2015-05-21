package ua.denysov.logic;

import ua.denysov.network.MyServer;
import ua.denysov.update.ModifyData;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.*;

/**
 * Created by Alex on 07.05.2015.
 */
public class Main implements ModifyData {

    private int port;
    private Map<Integer, MyServer> myServers = new HashMap<Integer, MyServer>();
    private MyServer serverSocket;
    private static int counter;

    public Main(int port) {
        this.port = port;
    }

    public void execute() throws IOException {
        System.out.println("Welcome to Server side!");
        ServerSocket servers = new ServerSocket(port);
        System.out.println("Waiting for a client...");

        while (true) {
            serverSocket = new MyServer(servers.accept(), this);
            serverSocket.setNum(++counter);
            myServers.put(serverSocket.getNum(), serverSocket);
        }
    }

    public void closeClient(int clientId) {
        MyServer myServer = myServers.get(clientId);
        myServers.remove(clientId);
        myServer.closeSocket();
    }
}
