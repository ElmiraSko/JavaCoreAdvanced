import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler {
    private MyServer myServer; // ссылка на сервер, пока null
    private Socket socket;     // ссылка на сокет, пока null
    private DataInputStream in;   // поток ввода-чтения в программу
    private DataOutputStream out;  // поток вывода - запись из программы

    private String name;  // имя

    public String getName() {
        return name;
    }

    public ClientHandler(MyServer myServer, Socket socket) { // конструктор, получает ссылку на сервер и сокет
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
            String str;
            if (!(str = in.readUTF()).trim().isEmpty()) {
            //String str = in.readUTF(); //ожидаем текст от клиента
            if (str.startsWith("/auth")) { // если текст начинается с auth, тогда
                String[] parts = str.split("\\s"); // создаем текстовый массив из строки str, по пробелам
                String nick = myServer.getAuthService().getNickByLoginPass(parts[1], parts[2]);
                //пытаемся получить ник из базы ников по логину и паролю
                if (nick != null) { //если ник не пустой, т.е. существует
                    if (!myServer.isNickBusy(nick)) { //проверяем,
                        sendMsg("/authok " + nick); // отправка сообщения клиенту
                        name = nick;
                        myServer.broadcastMsg(name + " зашел в чат"); //отправка сообщения всем клиентам через сервер
                        myServer.subscribe(this); // добавление в список
                        return;
                    } else {
                        sendMsg("Учетная запись уже используется");
                    }
                } else {
                    sendMsg("Неверные логин/пароль");
                }
            }
        }else break;
        }
    }

    public void readMessages() throws IOException {//метод чтения из потока от клиента с которым связан
        while (true) {
            String strFromClient = in.readUTF(); //считываем текст от клиента
            System.out.println("от " + name + ": " + strFromClient);
            if (strFromClient.equals("/end")) {
                sendMsg("/end");
                closeConnection();
                System.out.println("- Я все закрыл");
                return;
            }

            if (strFromClient.startsWith("/w")){
                String[] words = strFromClient.split("\\s");
                String textForName = words[1];
                System.out.println(textForName + " llllllllllllllllll");
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