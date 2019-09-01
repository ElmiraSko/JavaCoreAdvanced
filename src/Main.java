import java.util.*;

public class Main {
    public static void main(String[] args) {

// Массив из 15 слов, есть повторяющиеся
        String[] array = {"Car", "Bike", "Train", "Taxi", "Car", "Bus",
                "Ship", "Bus", "Motorbike", "Train", "Plane", "Car", "Car", "Bike", "Bus"};
// Образовали список уникальных слов, из которых состоит массив и вывели на консоль
        Set<String> setList = new HashSet();
        for (String st : array){
            setList.add(st);
        }
        System.out.println(setList);

//Отсортировали массив array. Посчитали, сколько раз встречается каждое слово и вывели на консоль
Arrays.sort(array);
HashMap<String, Integer> hm = new HashMap<>();
        String element = array[0];
        Integer count = new Integer(1);
    for (int i = 1; i < array.length; i++){
        if (!element.equals(array[i])){
            hm.put(element, count);
            element = array[i];
            count = 1;
        }else {
            count++;
            if (i == array.length-1){
                hm.put(element, count);
            }
        }
    }
        System.out.println(hm);
        System.out.println("=============== Задание 2 =======================");

        PhoneBook book = new PhoneBook();
        book.add("Васечкин", "89113443443");
        book.add("Иванов", "89112645660");
        book.add("Иванов", "89112645663");
        book.add("Иванов", "89112645661");
        book.add("Петров", "89112322666");

        System.out.println();
        System.out.println(book.get("Иванов") + " это все номера по указанной фамилии.");
        System.out.println("Все записи:");
        System.out.println(book.getContacts());
    }
}
