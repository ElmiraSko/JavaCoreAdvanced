package Tasks;

public class Person implements Participantable{
    private String name;
    private float maxLength;
    private float maxHeight;

    public Person(String name, float maxLength, float maxHeight){
        this.name = name;
        this.maxLength = maxLength;
        this.maxHeight = maxHeight;
    }

    public boolean run(float ln) {
        System.out.println(name + " пытается пробежать дистанцию в " + ln + " метров.");
        return (ln <= maxLength) ? true : false;
    }

    public boolean jump(float h){
        System.out.println(name + " пытается прыгнуть на высоту " + h + " метров.");
        return (h <= maxHeight) ? true : false;
    }

    public String getName() {
        return name;
    }
}
