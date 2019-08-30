import java.util.*;

public class PhoneBook{
    TreeMap<String, TreeSet<String>> contacts;  // коллекция: пары "фамилия"- "номера тел"

    public PhoneBook(){
        contacts = new TreeMap<>();
    }

    public void add(String surName, String phoneNumber){
        if (!contacts.containsKey(surName)){
            TreeSet<String> numbers = new TreeSet();
            numbers.add(phoneNumber);
            contacts.put(surName, numbers);
        }else {
            contacts.get(surName).add(phoneNumber); //hm.get(arr[i][0]) возвращает нам элемент из ts по индексу arr[i][0]
        }
    }

    public TreeSet<String> getNumber(String surName){
        Iterator iterator = contacts.keySet().iterator();
        while (iterator.hasNext()){
            String key = (String) iterator.next();
            if (key.equals(surName))
                return contacts.get(surName);
        }//проработать else если нет объекта с такой фамилией

        return contacts.get(surName);
    }

    public TreeMap<String, TreeSet<String>> getContacts(){
            return contacts;
    }

}
