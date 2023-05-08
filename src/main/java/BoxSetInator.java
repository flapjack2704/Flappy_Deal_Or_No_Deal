import java.util.Random;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;

public class BoxSetInator {
    private final Float[] ALL_AMOUNTS_ARRAY = {.01f, .1f, .5f, 1f,5f,10f,50f,100f,250f,500f,750f,
            1000f,3000f,5000f,10000f,15000f,20000f,35000f,50000f,75000f,100000f,250000f};
    private ArrayList<Float> cashAmountsRemaining = new ArrayList<>(Arrays.asList(ALL_AMOUNTS_ARRAY));
    private ArrayList<Box> boxArray = new ArrayList<>();
    private Box userBox = new Box(0, 0);
    public DecimalFormat df = new DecimalFormat("#,##0.00");



    public ArrayList<Float> getCashAmountsRemaining() {
        return cashAmountsRemaining;
    }

    public Box getUserBox() {
        return userBox;
    }

    public Float[] getALL_AMOUNTS_ARRAY() {
        return ALL_AMOUNTS_ARRAY;
    }

    public ArrayList<Box> getBoxArray() {
        return boxArray;
    }


    public void generateBoxArray(){
        // Fyller boxArray med tilfeldige verdier. (ID-ene blir ikke randomisert)
        Random rand = new Random();
        ArrayList<Float> tempCashAmountsArray = new ArrayList<>(Arrays.asList(ALL_AMOUNTS_ARRAY));

        for(int i = 0; i<ALL_AMOUNTS_ARRAY.length; i++){
            int randNum = rand.nextInt(tempCashAmountsArray.size());
            boxArray.add(new Box(tempCashAmountsArray.get(randNum), i+1));
            tempCashAmountsArray.remove(randNum);
        }
    }

    public void chooseUserBox(int input){
        userBox = boxArray.get(input-1);
        System.out.println("Box no. " + userBox.getBoxID() + " is your own box now.");
    }

    public void openBox(int input){
        // Brukeren velger boxID-en, åpner boksen, sletter den fra boxArray og amountsArray
        Box box = new Box(0, 0);
        for(int i = 0; i < boxArray.size(); i++){
            if(boxArray.get(i).getBoxID() == input){
                box = boxArray.get(i);
                break;
            }
        }
        System.out.println("\"£" + df.format(box.getValue()) + "\"");
        cashAmountsRemaining.remove(box.getValue());
        boxArray.remove(box);
    }

    public void printAllBoxIDsLeft(){
        System.out.println("\nYour box: " + userBox.getBoxID());
        System.out.println("\nIDs of boxes remaining:");
        String output = "";
        for(Box b : boxArray){
            if(b.getBoxID()!=userBox.getBoxID()){
                output += b.getBoxID() + " ";
            }
        }
        System.out.println(output);
    }

    public void printAllAmountsLeft(){
        System.out.println("\nPrize amounts remaining:");
        String output = "";
        Boolean lineDowned = false;
        for(int i = 0; i<cashAmountsRemaining.size(); i++){
            if(!lineDowned && cashAmountsRemaining.get(i) > 750){
                output += "\n";
                lineDowned = true;
            }
            output += "£" + df.format(cashAmountsRemaining.get(i)) + " ";
        }
        System.out.println(output+"\n");
    }

    public void swapBoxes(){
        Box otherBox = boxArray.get(0) == userBox ? boxArray.get(1) : boxArray.get(0);
        System.out.println("You decided to take box " + otherBox.getBoxID() + "\n");
        userBox = otherBox;
    }

    public String poundsValueStringInator(float num){
        return "£" + df.format(num);
    }



}
