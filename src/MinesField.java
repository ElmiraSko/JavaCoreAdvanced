import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class MinesField extends JPanel {
    //игровое поле, внешний вид
    Color color;
    Random random = new Random();
    private int cell = 40;
    private  int fSize = 10;
    int[][] gameArray = new int[fSize][fSize];// матрица игрового поля, будет зеркально отображаться на кнопочное поле
    int[] mines = new int[15];  //массив мин
    JButton[] gameButton = new JButton[fSize*fSize]; //создали массив кнопок

    public MinesField(){
        super();
        setSize(cell*fSize, cell*fSize);
        setLayout(new GridLayout(fSize, fSize));
        ButtonListener butListener = new ButtonListener();

        for (int i = 0; i < gameButton.length; i++){
            gameButton[i] = new JButton(); // заполняем массив реальными кнопками
            gameButton[i].setActionCommand("" + i);
            gameButton[i].addActionListener(butListener);
            this.add(gameButton[i]); //устанавливаем кнопку на панель
        }
        byZero();
        putMines();
        minesCount();
        color = gameButton[0].getBackground();
    }
    public int getCell(){
        return cell;
    }
    //метод обновления игрального поля
    public void newFeald(){
        byZero();
        putMines(); //заложили мины
        minesCount();
    }
    //обнуление матрицы  и востановление кнопок
    void byZero(){
        for (int i = 0; i < fSize; i++){
            for (int j = 0; j < fSize; j++){
                gameArray[i][j] = 0; //обнулили все ячейки
            }
        }
        for (int t = 0; t< gameButton.length; t++){
            gameButton[t].setText("");
            gameButton[t].setBackground(color);
            gameButton[t].setEnabled(true);
        }
    }
    //метод для установки мин на поле
    void putMines(){
        int x, y;
        for (int i = 0; i < mines.length; i++){
            do {
                x = random.nextInt(10);
                y = random.nextInt(10);
            }
            while (!isPut(x, y));
            gameArray[y][x] = -1;
        }
    }
    //проверка на доступность ячейки
    boolean isPut(int x, int y) {
        return (gameArray[y][x] == 0)? true : false;
    }
    //проставление количества мин
    void minesCount(){

        for (int i = 0; i < fSize; i++) {
            for (int j = 0; j < fSize; j++) {
                if (gameArray[i][j] == 0){
                    int count = 0;
                    if ((j-1 > 0) && gameArray[i][j-1] == -1){count++;} //слева
                    if ((i-1>0) && (j-1 > 0) && gameArray[i-1][j-1] == -1){count++;} // слева-сверху
                    if ((i-1 > 0) && gameArray[i-1][j] == -1){count++;}         //сверху
                    if ((i-1 > 0) && (j+1 <fSize) && gameArray[i-1][j+1] == -1){count++;} //сверху-справа
                    if ((j+1 < fSize) && gameArray[i][j+1] == -1){count++;}    // справа
                    if ((i + 1 < fSize) && (j+1 < fSize) && gameArray[i+1][j+1] == -1){count++;}  // справа-снизу
                    if ((i+1 <fSize) && gameArray[i+1][j] == -1){count++;}                         //снизу
                    if ((i + 1 < fSize) && (j-1 > 0) && gameArray[i+1][j-1] == -1){count++;} //снизу - слева
                    gameArray[i][j] = count;
                }
            }
        }
        //       print(); //для проверки состояния матрицы
    }

//    void print(){
//        for (int i = 0; i < fSize; i++) {
//            for (int j = 0; j < fSize; j++) {
//                System.out.print(gameArray[i][j] + " | ");
//            }
//            System.out.println("  ");
//        }
//    }

    class ButtonListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            String str = e.getActionCommand();
            int k = Integer.parseInt(str);
            int x = k / (fSize);
            int y = k % (fSize);
            if (gameArray[y][x] == -1){
                gameButton[k].setBackground(Color.RED);
                gameButton[k].setText("!");
                gameButton[k].setEnabled(false);
                JOptionPane.showMessageDialog(MinesField.this, "Игра окончена!");
            }else
            if (gameArray[y][x] > 0){
                int number = gameArray[y][x];
                String str_number = String.valueOf(number);
                gameButton[k].setBackground(Color.BLUE);
                gameButton[k].setText(str_number);
                gameButton[k].setEnabled(false);
            }else   gameButton[k].setEnabled(false);
        }
    }

}


