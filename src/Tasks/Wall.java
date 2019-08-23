package Tasks;

    public class Wall implements Resistuble {
    private float length;

    public Wall(float length){
        this.length = length;
    }

    public float getLength(){
        return length;
    }
}
