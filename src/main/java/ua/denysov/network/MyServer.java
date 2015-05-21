package ua.denysov.network;

import ua.denysov.weather.Yahoo;
import ua.denysov.update.ModifyData;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


/**
 * Created by Alex on 28.04.2015.
 */
public class MyServer extends Thread {

    private Socket fromClient = null;
    private int num;
    private PrintWriter out;
    private ModifyData md;

    public MyServer(Socket fromClient, ModifyData md) {
        this.fromClient = fromClient;
        this.md = md;
        start();
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public void closeSocket() {
        try {
            fromClient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(fromClient.getInputStream()));
            out = new PrintWriter(fromClient.getOutputStream(), true);

            String input;

            System.out.println("Waiting for messages");

            while ((input = in.readLine()) != null) {
                System.out.println(input);
                writeToClient();
            }
        } catch (IOException e) {
            System.out.println("Connection is lost with " + num + " client!");
        } finally {
            try {
                System.out.println("End of connection " + num +" client");
                md.closeClient(num);
                out.close();
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void writeToClient() {
        try {
            out.println(new Yahoo().getWeather());
            out.println("----------------------------------------");
            out.println("exit");
        } catch (Exception e) {
            out.println("Unable to receive weather data.");
        }

    }
}
