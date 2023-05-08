import java.util.ArrayList;
import java.util.Scanner;

public class GameRunner {
    private BoxSetInator bsi = new BoxSetInator();
    private Scanner sc = new Scanner(System.in);
    private Float dealtAmount = 0f;  // Blir ikke null hvis avtalen har blitt tatt
    private int roundsPlayed = 0;  // 5 runder
    private ArrayList<Float> offerHistory = new ArrayList<>();


    public void run(){
        bsi.generateBoxArray();

        int input = inputInteger("To choose your box, input a box I.D (1 -> 22):");
        bsi.chooseUserBox(input);

        for(int i = 0; i < 5; i++){
            doRoundOfPicking(6-roundsPlayed);  // 6 -> 5 -> 4 -> 3 -> 2

            bsi.printAllBoxIDsLeft();
            bsi.printAllAmountsLeft();

            bankerWankerOffer();
        }

        finalRound();

        closeScanner();
    }


    public void doRoundOfPicking(int picks){
        for(int i = 0; i<picks; i++){
            bsi.printAllBoxIDsLeft();
            bsi.printAllAmountsLeft();
            bsi.openBox(inputInteger("Input a box I.D:"));
        }
        roundsPlayed++;
    }

    public void bankerWankerOffer(){
        float offer = 0;
        float kValue = (float) (0.2 + (0.11 * roundsPlayed));  // 0.31 -> 0.75
        //System.out.println("test K: " + kValue);

        //får forventet verdi:
        int numOfBoxes = bsi.getCashAmountsRemaining().size();
        for(float i : bsi.getCashAmountsRemaining()){
            offer += i;
        }
        offer /= numOfBoxes;

        offer *= kValue;
        offerHistory.add(offer);

        if(dealtAmount == 0){

            System.out.println("Banker offers: " + bsi.poundsValueStringInator(offer));
            System.out.println("Type \"y\" to accept, anything else to decline:");

            String input = sc.nextLine().toLowerCase();
            if(input.equals("y")){
                System.out.println("You accepted the offer of " + bsi.poundsValueStringInator(offer) + "\n");
                dealtAmount = offer;
            }
            else{
                System.out.println("You declined the offer\n");
            }
        }
        else{
            System.out.println("Dealt at: " + bsi.poundsValueStringInator(dealtAmount));
            System.out.println("Banker would've offered: " + bsi.poundsValueStringInator(offer));
            System.out.println("Moving on....");
        }
    }

    public void finalRound(){
        System.out.println("Type \"y\" to swap your box with the remaining box, anything else to decline:");
        String input = sc.nextLine().toLowerCase();
        if(input.equals("y")){
            bsi.swapBoxes();
        }
        else{
            System.out.println("You declined to swap, and kept box " + bsi.getUserBox().getBoxID() + "\n");
        }

        System.out.println("Opening box....");
        try {
            Thread.sleep(1000);
            System.out.println("* rips tape off and opens box *");
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        float amount = bsi.getUserBox().getValue();
        System.out.println("Box contained: " + bsi.poundsValueStringInator(amount));

        if(dealtAmount == 0) {
            System.out.println("You took home " + bsi.poundsValueStringInator(amount));
        }
        else{
            if(amount > dealtAmount){
                System.out.println("Tough luck, you won " + bsi.poundsValueStringInator(dealtAmount) + ", but you could have earned more");
            }
            else{
                System.out.println("Congrats, you beat the banker and took home " + bsi.poundsValueStringInator(dealtAmount));
            }
        }
    }


    public int inputInteger(String msg){
        int input = 0;
        while(true){
            try{
                System.out.println(msg);
                input = Integer.parseInt(sc.nextLine());

                for(int i = 0; i < bsi.getBoxArray().size(); i++){
                    if(bsi.getBoxArray().get(i).getBoxID()  ==  input  &&  bsi.getBoxArray().get(i).getBoxID()  !=  bsi.getUserBox().getBoxID()){
                        System.out.println("You selected box no. " + input);
                        return input;
                    }
                }
                System.out.println("No box with that ID is available, try again...");

            }
            catch(Exception e){
                System.out.println("Not an integer, input an integer...");
            }
        }
    }

    public void closeScanner(){
        sc.close();
    }
}
