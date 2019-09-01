import java.util.*;

public class PhoneBook{
    TreeMap<String, TreeSet<String>> contacts;
        public PhoneBook(){
        contacts = new TreeMap<>();
    }

//метод add() добавляет записи в телефонный справочник
    public void add(String surName, String phoneNumber){
        if (!contacts.containsKey(surName)){
            TreeSet<String> numbers = new TreeSet();
            numbers.add(phoneNumber);
            contacts.put(surName, numbers);
        }else {
            contacts.get(surName).add(phoneNumber);
        }
    }
// метод get() позволяет найти номер телефона по фамилии
    public TreeSet<String> get(String surName){
        for (String key : contacts.keySet()){
            if (key.equals(surName))
                return contacts.get(surName);
        }
        return contacts.get(surName);
    }
// метод getContacts() позволяет вывести все записи
    public TreeMap<String, TreeSet<String>> getContacts(){
            return contacts;
    }

}
