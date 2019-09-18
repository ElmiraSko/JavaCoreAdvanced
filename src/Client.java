import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;

public class Client extends JFrame {
    private static boolean flag_exit = false;
    private final String SERVER_ADDR = "localhost";
    private final int SERVER_PORT = 8189;
    private String myNick;
    private JButton btnEnter, btnAuth;
    private JTextField msgInputField, login, password;
    private JTextArea chatArea;
    AuthWindow wind;
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
//=================================================================
     class AuthWindow extends JFrame{
         AuthWindow(){
            super("Авторизация участника чата");
            JPanel panelButton = new JPanel();
            panelButton.setLayout(new GridLayout(5,1));
            panelButton.setBorder(BorderFactory.createEmptyBorder(3,3,3,3));
            add(panelButton);
            JLabel label_login = new JLabel("Введите логин:");
            JLabel label_pass = new JLabel("Введите пароль:");
            login = new JTextField(11);
            password = new JTextField(11);
            btnEnter = new JButton("Войти");
            panelButton.add(label_login);
            panelButton.add(login);
            panelButton.add(label_pass);
            panelButton.add(password);
            panelButton.add(btnEnter);
            btnEnter.addActionListener(new ActionListener() {
                @Override
            public void actionPerformed(ActionEvent e) {
                sendLoginAndPassword();
            }
        });
            setBounds(650, 250 , 400, 200);
            setResizable(false);
            setVisible(true);
        }
}
    //================================
    public void openConnection() throws IOException {
        try {
        socket = new Socket(SERVER_ADDR, SERVER_PORT);
        in = new DataInputStream(socket.getInputStream());
        out = new DataOutputStream(socket.getOutputStream());
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        String strFromServer;
                        if (!(strFromServer = in.readUTF()).trim().isEmpty()) {
                            System.out.println(strFromServer + " - проверка");
                            if (strFromServer.startsWith("/authok")) {
                                myNick = strFromServer.split("\\s")[1];//клиент получил свой ник
                                if (myNick != null) {
                                    wind.dispose();
                                    msgInputField.setEditable(true);
                                    btnAuth.setEnabled(false);
                                }
                                break;
                            }
                            JOptionPane.showMessageDialog(wind, strFromServer);
                        }
                    }
                    while (true) {
                        String strFromServer;
                        if (!(strFromServer = in.readUTF()).trim().isEmpty() || (in.read()>0 && in.read()>0)) {
                            if (strFromServer.equalsIgnoreCase("/end")) {
                                System.out.println(strFromServer);
                                flag_exit = true;
                                break;
                            }
                            chatArea.append(strFromServer + "\n");
                        }
                    }
                } catch (EOFException ex){System.out.println("Ошибка при чтении");}
                catch (Exception e) {
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
   private void sendLoginAndPassword() { // используем в слушателе кнопки "Войти"
        if (!login.getText().trim().isEmpty()) {
            if (!password.getText().trim().isEmpty()) {
                try{
                String message = "/auth " + login.getText() + " " + password.getText();
                System.out.println(message);
                 out.writeUTF(message);
                 out.flush();
                login.setText("");
                password.setText("");

                }catch (IOException e){JOptionPane.showMessageDialog(this, "Ошибка передачи данных.");}
                } else JOptionPane.showMessageDialog(this, "Введите пароль!");
            } else JOptionPane.showMessageDialog(this, "Введите логин!");
        }

    public void prepareGUI() {
        setBounds(600, 150, 500, 500);
        setTitle("Клиент");
        // Текстовое поле для вывода сообщений
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setLineWrap(true);
        add(new JScrollPane(chatArea), BorderLayout.CENTER);
//Верхняя панель для ввода логина и пароля и ника
        JPanel topPanel = new JPanel(new FlowLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(3,3,3,3));
        btnAuth = new JButton("Войти в чат");
        btnAuth.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                wind = new AuthWindow();
            }
        });
        JButton btnReg = new JButton("Регистрация");
        topPanel.add(btnAuth);
        topPanel.add(btnReg);


        // Нижняя панель с полем для ввода сообщений и кнопкой отправки сообщений
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(3,3,3,3));
        JButton btnSendMsg = new JButton("Отправить");
        bottomPanel.add(btnSendMsg, BorderLayout.EAST);
        msgInputField = new JTextField();
        msgInputField.setEditable(false);
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
        setResizable(false);
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