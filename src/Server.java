import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static void main(String[] args) throws IOException {

        try (ServerSocket serv = new ServerSocket(2080)) {
            System.out.println("Сервер доступен!");
            while(true){
                Socket socket = serv.accept();
                System.out.println("Клиент подключился!");
                new Thread_2(socket).start();
                new Thread_1(socket).start();
            }
        }
    }
}
class Thread_1 extends Thread{
    Socket socket_1;
    public Thread_1(Socket sk){
        socket_1=sk;
    }
    @Override
    public void run() {
        int c = 0;
        try (DataInputStream in = new DataInputStream(socket_1.getInputStream())) {
            while (true) {
                String messageFromClient = in.readUTF();
                if (messageFromClient.equals("q")) { //проверяем, не хочет ли клиент отключиться
                    System.out.println("Сервер не доступен, т.к. клиент отключился!");
                    in.close();
                    socket_1.close();
                } else {
                    System.out.println("Клиент: " + messageFromClient); //выводим на консоль данные от клиента
                }
            }

        } catch (Exception ex) {}
    }
}
class Thread_2 extends Thread{
    Socket socket_2;
    public Thread_2(Socket sk){
        socket_2=sk;
    }
    @Override
    public void run() {
        try (DataOutputStream out = new DataOutputStream(socket_2.getOutputStream())) {
            while (true) {
                java.util.Scanner scanner = new java.util.Scanner(System.in);
                String str = scanner.nextLine();
                if (!str.trim().isEmpty()) {
                    try {
                        out.writeUTF("Сервер: " + str);
                        out.flush();
                    } catch (IOException ex) {
                    }
                } else {
                    out.close();
                }
            }
        }catch (IOException ex){}
    }
}