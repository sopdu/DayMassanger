package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

public class DayServer {

    private static ArrayList streams;

    public static void main(String[] args){
        go();
    }

    private static void go() {
        streams = new ArrayList<PrintWriter>();
        try {
            ServerSocket serverSocket = new ServerSocket(5000);
            while (true){
                Socket socket = serverSocket.accept();
                System.out.println("Пользватель вошел");
                PrintWriter writer = new PrintWriter(socket.getOutputStream());
                streams.add(writer);

                Thread thread = new Thread(new Listener(socket));
                thread.start();
            }
        } catch (Exception e){}
    }

    private static void tellEveryone(String message) {
        int x = message.indexOf(':');
        String login = message.substring(0, x);

        //Iterator iterator = (Iterator) streams.iterator();
        java.util.Iterator<PrintWriter> iterator = streams.iterator();
        while (iterator.hasNext()){
            try {
                PrintWriter writer = iterator.next();
                writer.println(message);
                writer.flush();
            } catch (Exception ex){}
        }
    }

    private static class Listener implements Runnable{

        BufferedReader reader;

        Listener(Socket socket){

            InputStreamReader is;

            try {
               is = new InputStreamReader(socket.getInputStream());
               reader = new BufferedReader(is);
            } catch (Exception e) {}

            /*
            InputStreamReader is = new InputStreamReader(socket.getInputStream());
            reader = new BufferedReader(is);
            */
        }

        public void run(){
             String message;
             try {
                 while (
                    (message = reader.readLine()) != null
                 ){
                     System.out.println(message);
                     tellEveryone(message);
                 }
             } catch (Exception ex){}
        }
    }
}
