public class Main {

    public static void main(String[] args) {

        String[][] array = {{"2", "3", "5", "8"},      //двумерный строковый массив размером 4х4
                            {"7", "6", "1", "4"},
                            {"1", "3", "5", "9"},
                            {"5", "4", "3", "9"}};

        try {
            System.out.println("Сумма чисел массива: " + twoDimensionalArray(array)); //вызов метода и вывод на консоль
        }catch (MyArraySizeException ex){
            System.out.println(ex.getMessage());
        }catch (MyArrayDataException ex){
            System.out.println(ex.getMessage() + "\nПроверьте массив!");
        }

    }

    static int twoDimensionalArray(String[][] array) throws MyArraySizeException, MyArrayDataException{
        if (array.length ==4 && array[0].length==4 && array[1].length==4 && array[2].length==4 && array[3].length==4){
            int[][] newArray = new int[4][4];
            int summa = 0;
            for (int i = 0; i< array.length; i++){
                for (int j = 0; j < array[i].length; j++){
              //пытаемся преобразовать в int, если не получается,
                    // то отлавливаем NumberFormatException выбрасываемый методом parseInt
                    // и пробрасываем свой собственный MyArrayDataException
                    try {
                        newArray[i][j] = Integer.parseInt(array[i][j]);
                        summa += newArray[i][j];
                    } catch (NumberFormatException ex){
                        throw new MyArrayDataException("Не удалось преобразовать в число элемент массива на позиции (" + (i+1) +", " + (j+1) + ")." );
                    }
                }
            }
            return summa;
        } else {
            throw new MyArraySizeException("Размер массива должен быть 4х4!");
        }
    }
}
