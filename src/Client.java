import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/* Реализовать личные сообщения так: если клиент пишет «/w nick3 Привет»,
то только клиенту с ником nick3 должно прийти сообщение «Привет».*/
public class Client extends JFrame {
    private static boolean flag_exit = false;
    private boolean unAuthorised;
    private final String SERVER_ADDR = "localhost";
    private final int SERVER_PORT = 8189;
    private String myNick;

    private JTextField msgInputField, login, password;
    private JTextArea chatArea;

    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;

    public Client() {
        try {
            openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        prepareGUI();
    }

    public void openConnection() throws IOException {
        try {
        socket = new Socket(SERVER_ADDR, SERVER_PORT);
        in = new DataInputStream(socket.getInputStream());
        out = new DataOutputStream(socket.getOutputStream());
        unAuthorised = true;
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        String strFromServer;
                        if (!(strFromServer = in.readUTF()).trim().isEmpty()) {
                            if (strFromServer.startsWith("/authok")) {
                                myNick = strFromServer.split("\\s")[1];//клиент получил свой ник
                                unAuthorised = false;
                                break;
                            }
                            chatArea.append(strFromServer + "\n");
                        }
                    }
                    while (true) {
                        String strFromServer;
                        if (!(strFromServer = in.readUTF()).trim().isEmpty()) {
                            if (strFromServer.equalsIgnoreCase("/end")) {
                                System.out.println(strFromServer);
                                flag_exit = true;
                                break;
                            }
                            chatArea.append(strFromServer + "\n");
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Ошибка при закрытии окна клиента");
                    e.printStackTrace();
                }
            }
        });
            t.setDaemon(true);
            t.start();
        }catch (Exception ee){}
    }
//=====================================
    public void closeConnection() {
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

    public void sendMessage() {
        if (!msgInputField.getText().trim().isEmpty()) {
            try {
                out.writeUTF(msgInputField.getText());
                msgInputField.setText("");
                msgInputField.grabFocus();
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Ошибка отправки сообщения");
            }
        }
    }
    void sendLoginAndPassword() { // используем в слушателе кнопки "Войти"
        if (!login.getText().trim().isEmpty()) {
            if (!password.getText().trim().isEmpty()) {
                try{
                String message = "/auth " + login.getText() + " " + password.getText();
                System.out.println(message);
                 out.writeUTF(message);
                login.setText("");
                password.setText("");
                if (myNick != null) {
                    login.setEditable(false);
                    password.setEditable(false);
                      }
                }catch (IOException e){JOptionPane.showMessageDialog(this, "Ошибка передачи данных.");}
                } else JOptionPane.showMessageDialog(this, "Введите пароль!");
            } else JOptionPane.showMessageDialog(this, "Введите логин!");
        }

    public void prepareGUI() {
        // Параметры окна
        setBounds(600, 150, 500, 500);
        setTitle("Клиент");
        // Текстовое поле для вывода сообщений
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setLineWrap(true);
        add(new JScrollPane(chatArea), BorderLayout.CENTER);
//Верхняяпанель для ввода логина и пароля и ника
        JPanel topPanel = new JPanel(new FlowLayout());
        JButton btnEnter = new JButton("Войти");
        btnEnter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendLoginAndPassword();
            }
        });
        JButton btnReg = new JButton("Регистрация");
        login = new JTextField("Логин:", 11);

        login.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                login.setText("");
            }
        });
        password = new JTextField("Пароль:",11);
        password.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                password.setText("");
            }
        });

        topPanel.add(login);
        topPanel.add(password);
        topPanel.add(btnEnter);
        topPanel.add(btnReg);


        // Нижняя панель с полем для ввода сообщений и кнопкой отправки сообщений
        JPanel bottomPanel = new JPanel(new BorderLayout());
        JButton btnSendMsg = new JButton("Отправить");
        bottomPanel.add(btnSendMsg, BorderLayout.EAST);
        msgInputField = new JTextField();
        add(bottomPanel, BorderLayout.SOUTH);
        add(topPanel, BorderLayout.NORTH);
        bottomPanel.add(msgInputField, BorderLayout.CENTER);
        btnSendMsg.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });
        msgInputField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });

        // Настраиваем действие на закрытие окна
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (flag_exit == true) {
                    System.exit(0);
                } else {new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            out.writeUTF("/end");
                            out.flush();
                        } catch (IOException exc) {
                            exc.printStackTrace();
                        }
                    }
                }).start();
                System.exit(0);
                }
            }
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Client();
            }
        });
    }
}