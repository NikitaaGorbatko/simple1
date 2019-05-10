import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerSide {
    ServerSide() throws IOException {
        createConnection();
        /*
        try (ServerSocket serverSocket = new ServerSocket(8082)) {
            Thread serverThread = new Thread(() -> {
                BufferedReader serverReader;
                Writer serverWriter;
                while (true) {
                    try {
                        Socket connection = serverSocket.accept();
                        //connection = serverSocket.accept();
                        serverReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        serverWriter = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
                        serverWriter.write("hello, " + serverReader.readLine() + "\n");
                        serverWriter.flush();
                        System.out.println("a");
                    } catch (IOException ex) {
                        System.out.println(ex.getMessage() + "sdfgdsb");
                    }
                }
            });
            serverThread.setDaemon(true);
            serverThread.start();
        }

        */
    }

    private void createConnection() throws IOException {
        ServerSocket serverSocket = new ServerSocket(8082);
        Thread serverThread = new Thread(() -> {
            while(true) {
                try {
                    Socket connection = serverSocket.accept();
                    try (BufferedReader serverReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                         Writer serverWriter = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));)
                    {
                        serverWriter.write("hello, " + serverReader.readLine() + "\n");
                        serverWriter.flush();
                    }
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                } catch (Throwable t) {
                    System.out.println(t.getMessage());
                    t.printStackTrace();
                    throw t;
                }
                System.out.println("s");
            }
        });
        serverThread.setDaemon(true);
        serverThread.start();
    }
}
