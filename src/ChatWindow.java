import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ChatWindow extends JFrame{
    private JPanel northPanel, centerPanel, bottomPanel;
    private JTextArea textArea;
    private JTextField textField;
    private JButton button;
    private JScrollPane scrollPane;

    public ChatWindow(){
        super("ChatWindow");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(900, 100, 350, 400);
        northPanel = new JPanel();
        northPanel.add(new JLabel("******************"));
        centerPanel = new JPanel();
        bottomPanel = new JPanel(new FlowLayout());

        textArea = new JTextArea(15, 25);
        textArea.setEditable(false); //запретили возможность редактировать
        textArea.setLineWrap(true); // перенос строки, если текст не помещается
        textArea.setWrapStyleWord(true);
        scrollPane = new JScrollPane(textArea);


        textField = new JTextField(16);
        textField.setFont(new Font("TimesRoman", Font.PLAIN, 14));

        textField.addActionListener(new MyButtonListener());

        button = new JButton("Отправить");
        button.addActionListener(new MyButtonListener());
        bottomPanel.add(textField);
        bottomPanel.add(button);
        centerPanel.add(scrollPane);

        add(northPanel, "North");
        add(centerPanel, "Center");
        add(bottomPanel, "South");
        setVisible(true);
    }

    class MyButtonListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            String text = textField.getText();
            textArea.append(" - " + text + "\n");
            textField.setText("");
        }
    }
}
