import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Client {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 2080);
            new Client_Thread_1(socket).start();
            new Client_Thread_2(socket).start();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
class Client_Thread_1 extends Thread{
    Socket socket_1;
    public Client_Thread_1(Socket sk){
        socket_1=sk;
    }
    @Override
    public void run() {
        int c = 0;
        try (DataInputStream in = new DataInputStream(socket_1.getInputStream())) {
            while (true) {
                String messageFromServer = in.readUTF();
                System.out.println(messageFromServer);
            }
        } catch (Exception ex) {}
    }
}
class Client_Thread_2 extends Thread{
    Socket socket_2;
    public Client_Thread_2(Socket sk){
        socket_2=sk;
    }
    @Override
    public void run() {
        try (DataOutputStream out = new DataOutputStream(socket_2.getOutputStream())) {
            while (true) {
                java.util.Scanner scanner = new java.util.Scanner(System.in);
                String messageForServer = scanner.nextLine();
                if (!messageForServer.trim().isEmpty()) {
                    out.writeUTF(messageForServer);
                    out.flush();
                    System.out.println("Сообщение:  " + messageForServer + " отправлено.");
                    if (messageForServer.equals("q")){
                        System.out.println("Клиент отключился!");
                        out.close();
                        socket_2.close();
                        break;
                    }
                }
            }
        }catch (IOException ex){}
    }
}
