import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;


public class Client {
    public static void main(String[] args) {
       try {
           Socket socket = new Socket("localhost", 80);
           DataInputStream in = new DataInputStream(socket.getInputStream());
           DataOutputStream out = new DataOutputStream(socket.getOutputStream());
           while (true){
               java.util.Scanner scanner = new java.util.Scanner(System.in);
               String messageForServer = scanner.nextLine();
               if (!messageForServer.trim().isEmpty()) {
               out.writeUTF(messageForServer);
                   out.flush();
                   System.out.println("Сообщение отправлено! " + messageForServer);
               if (messageForServer.equals("q")){
                   System.out.println("Клиент отключился!");
                   out.close();
                   in.close();
                   socket.close();
                   break;
               }
               if(in.available() > 0){
                   System.out.println("Нижний текст от сервера");
                   System.out.println(in.readUTF());}

               }
           }
       }catch (IOException ex){ex.printStackTrace();
           System.out.println("error");}

    }
}
