import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Multithreaded_TCP_Server {
    public static void main(String[] args) throws IOException {
        ServerSocket s = new ServerSocket(4000);
        while(true) {
            Socket socket = s.accept();
            Handle_Client c = new Handle_Client(socket);
            Thread t = new Thread(c);
            t.start();
        }
    }
}