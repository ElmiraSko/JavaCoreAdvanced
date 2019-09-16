import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler {
    private MyServer myServer;
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private static boolean flag = true;

    private String name;
    public String getName() {
        return name;
    }

    public ClientHandler(MyServer myServer, Socket socket) {
        try {
            this.myServer = myServer;
            this.socket = socket;
            this.in = new DataInputStream(socket.getInputStream());
            this.out = new DataOutputStream(socket.getOutputStream());
            this.name = "";
            new Thread(() -> {   //в отдельном потоке выполняем:
                try {
                    System.out.println(socket.isConnected() + " соединение установлено");
                    authentication();  //авторизация
                    readMessages();  // чтение сообщений от клиента
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    closeConnection();
                }
            }).start();
        } catch (IOException e) {
            throw new RuntimeException("Проблемы при создании обработчика клиента");
        }
    }

    public void authentication() throws IOException {
        while (true) {
            try{
               String str = in.readUTF(); //ожидаем текст от клиента
                if (str.startsWith("/auth")) {
                    String[] parts = str.split("\\s");
                    String nick = myServer.getAuthService().getNickByLoginPass(parts[1], parts[2]);
                    if (nick != null) {
                        if (!myServer.isNickBusy(nick)) {
                            sendMsg("/authok " + nick);
                            name = nick;
                            myServer.broadcastMsg(name + " зашел в чат");
                            myServer.subscribe(this);
                            return;
                        } else {
                            sendMsg("Учетная запись уже используется");
                        }
                    } else {
                        sendMsg("Неверные логин/пароль");
                    }
                }
            }catch (IOException ex){
                flag = false; //если клиент закрыл приложение не пройдя авторизацию
                break;
            }
        }
    }

    public void readMessages() throws IOException {//метод чтения из потока от клиента с которым связан
        while (flag) {
            System.out.println(socket.isConnected());
            String strFromClient = in.readUTF();
            System.out.println("от " + name + ": " + strFromClient);
            if (strFromClient.equals("/end")) {
                sendMsg("/end");
                socket.close();
                return;
            }

            if (strFromClient.startsWith("/w")){
                String[] words = strFromClient.split("\\s");
                String textForName = words[1];
                myServer.sendOnly(strFromClient, textForName);
            } else
            myServer.broadcastMsg(name + ": " + strFromClient);//отправка сообщения всем клиентам через сервер
        }
    }

    public void sendMsg(String msg) { //отправляет сообщение клиенту с которым связан
        try {
            out.writeUTF(msg);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closeConnection() { // закрытие соединения, удаляет ClientHandler- объект из списка
        myServer.unsubscribe(this);
        myServer.broadcastMsg(name + " вышел из чата"); //всем клиентам отправка сообщения
        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}