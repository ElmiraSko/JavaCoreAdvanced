public class Main {
    static final int size = 10000000;
    static final int h = size / 2;

    public static void main(String[] args) {
        Main obj = new Main();
        obj.method_1();
        obj.method_2();
    }

    void method_1() {
        float[] array_1 = new float[size]; // одномерный массив
        for (int i = 0; i < array_1.length; i++) { // заполняем массив единицами
            array_1[i] = 1;
        }
        long a = System.currentTimeMillis(); //засекаем время выполнения
        for (int i = 0; i < array_1.length; i++) { // для каждой ячейки устанавливаем новое значение
            array_1[i] = (float) (array_1[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));
        }
        System.out.println((System.currentTimeMillis() - a) + " - время работы первого метода"); //выводим время работы
    }

    void method_2() {
        float[] array_2 = new float[size];
        for (int i = 0; i < array_2.length; i++) { // Заполнили 2-й массив единицами
            array_2[i] = 1;
        }
        long a = System.currentTimeMillis(); //засекаем время выполнения

//деление массива array_2 на два массива: array_2_copy, array_2_copy_
        float[] array_2_copy = new float[h];
        System.arraycopy(array_2, 0, array_2_copy, 0, h);

        float[] array_2_copy_ = new float[h];
        System.arraycopy(array_2, h, array_2_copy_, 0, h);
//создание классов потоков Thread_1, Thread_2
        class Thread_1 implements Runnable {
            @Override
            public void run() {
                for (int i = 0; i < array_2_copy.length; i++) {
                    array_2_copy[i] = (float)(array_2_copy[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));
                }
            }
        }
        class Thread_2 implements Runnable {
            @Override
            public void run() {
                for (int i = 0; i < array_2_copy_.length; i++) {
                    array_2_copy_[i] = (float)(array_2_copy_[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));
                }
            }
        }
        new Thread(new Thread_1()).start(); //запуск потока
        new Thread(new Thread_2()).start();
//обратная склейка массивов
        System.arraycopy(array_2_copy, 0, array_2, 0, h);
        System.arraycopy(array_2_copy_, 0, array_2, h, h);
//выводим время работы
        System.out.println((System.currentTimeMillis() - a) + " - время работы второго метода");
    }
}
