package Tasks;

public class Robot implements Participantable{
    private String name;
    private float maxLength;
    private float maxHeight;

    public Robot(String name, float maxLength, float maxHeight){
        this.name = name;
        this.maxLength = maxLength;
        this.maxHeight = maxHeight;
    }
    public boolean run(float l){

        System.out.println(name + " пытается пробежать " + l + " метров.");
        return (l <= maxLength) ? true : false;
    }
    public boolean jump(float h){

        System.out.println(name + " пытается прыгнуть на высоту " + h + " метров.");
        return (h <= maxHeight) ? true : false;
    }

    @Override
    public String toString() {
        return name;
    }
}
