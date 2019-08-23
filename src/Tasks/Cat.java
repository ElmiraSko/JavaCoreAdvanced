package Tasks;

public class Cat implements Participantable{
    private String name;
    private float length;
    private float height;

    public Cat(String name, float maxLength, float maxHeight){
        this.name = name;
        length = maxLength;
        height = maxHeight;
    }

    public boolean run(float l){
        System.out.println(name + " пытается пробежать " + l + " метров.");
        return (l<= length) ? true : false;
    }
    public boolean jump(float h){
        System.out.println(name + " пытается прыгнуть на высоту " + h + " метров.");
        return (h<= height) ? true : false;
    }

    public String getName() {
        return name;
    }
}
