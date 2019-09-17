import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class MyServer {
        private final int PORT = 8189;

        private List<ClientHandler> clients; //clients- ссылка на список(ArrayList) клиентов, пока  null
        private AuthService authService;    // ссылка на объект AuthService, пока null

        public AuthService getAuthService() { // получение объекта AuthService
            return authService;
        }

        public MyServer() {
            try (ServerSocket server = new ServerSocket(PORT)) {
                authService = new BaseAuthService();      // создание объекта AuthService, база ников и паролей
                // содержащего список объектов "логин-пароль-ник" сразу добавляет три объекта
                authService.start();              //  вывод сообщения "Сервис аутентификации запущен"
                clients = new ArrayList<>(); //создается список clients, для хранения объектов типа ClientHandler
                while (true) {
                    System.out.println("Сервер ожидает подключения");
                    Socket socket = server.accept();
                    System.out.println("Клиент подключился");
                    new ClientHandler(this, socket); //создали держателя
                }
            } catch (IOException e) {
                System.out.println("Ошибка в работе сервера");
            } finally {
                if (authService != null) {
                    authService.stop();
                }
            }
        }
    //проверяем, есть ли среди подключившихся клиентов этот ник
        public synchronized boolean isNickBusy(String nick) {
            for (ClientHandler o : clients) {
                if (o.getName().equals(nick)) {
                    return true;
                }
            }
            return false;
        }
    //отправка сообщения всем клиентам подряд
        public synchronized void broadcastMsg(String msg) {
            for (ClientHandler o : clients) {
                o.sendMsg(msg);
            }
        }
        //отправка сообщения (msg) конкретному клиенту с именем forClient от from
        public synchronized void sendOnly(String msg, String forClient, String from){
            for (ClientHandler o : clients) {
                if (o.getName().equals(forClient)){
                    o.sendMsg("От " + from + ": " + msg);
                }
            }

        }

        public synchronized void unsubscribe(ClientHandler o) { //удаление клиента
            clients.remove(o);
        }

        public synchronized void subscribe(ClientHandler o) { //добавление клиента
            clients.add(o);
        }
}