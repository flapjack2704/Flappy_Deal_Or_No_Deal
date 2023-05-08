import java.text.DecimalFormat;

public class Box {
    private float value;
    private int boxID;  // (1->22)
    public DecimalFormat df = new DecimalFormat("#,##0.00");


    public Box(float value, int boxID){
        this.value = value;
        this.boxID = boxID;
    }


    public float getValue(){
        return value;
    }

    public int getBoxID(){
        return boxID;
    }


    public String toString(){
        return "£" + df.format(value) + " ID: " + boxID;
    }
}
