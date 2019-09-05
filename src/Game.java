import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Game {
    MinesField field;
    JButton butNew, butEx;
    public Game(){
        JFrame frame = new JFrame("Сапер");
        field = new MinesField();
        frame.setBounds(800, 100, field.getCell()*11, field.getCell()*13);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        butNew = new JButton("Новая игра");
        butNew.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                field.newFeald();
            }
        });
        butEx = new JButton("Выйти");
        butEx.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        JPanel panel = new JPanel();
        panel.add(butNew);
        panel.add(butEx);
        frame.add(panel, "North");
        frame.add(field, "Center");
        frame.setVisible(true);
    }

}
