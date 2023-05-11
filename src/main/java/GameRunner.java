//import java.util.ArrayList;
import java.util.*;

public class GameRunner {
    private BoxSetInator bsi = new BoxSetInator();
    private FileIOInator fio = new FileIOInator();
    private Scanner sc = new Scanner(System.in);
    private Float dealtAmount = 0f;  // Blir ikke null hvis avtalen har blitt tatt
    private int roundsPlayed = 0;  // 5 runder
    private String playerName= "";
    private float amountWon = 0f;
    private LinkedHashMap<String, Float> highScoresMap = new LinkedHashMap<>();
    //private ArrayList<Float> offerHistory = new ArrayList<>();  // Skal brukes senere i produksjon, bare vent ^_^


    public void run(){
        highScoresMap = fio.readHighScoreFileToMap();


        System.out.println("Welcome to Flappy Deal Or No Deal\n");
        setPlayerName();
        System.out.println("Hello " + playerName + "!");
        bsi.generateBoxArray();

        int input = inputInteger("To choose your box, input a box I.D (1 -> 22):");
        bsi.chooseUserBox(input);

        for(int i = 0; i < 5; i++){
            doRoundOfPicking(6-roundsPlayed);  // 6 -> 5 -> 4 -> 3 -> 2

            bsi.printAllBoxIDsLeft();
            bsi.printAllAmountsLeft();

            bankerOffer();
        }

        finalRound();  // "Endre boks? Eller nei?"

        addNewScoreToMap();
        fio.writeMapToHighScoreFile(highScoresMap);
        printHighScores();

        closeScanner();
    }


    public void doRoundOfPicking(int picks){
        for(int i = 0; i<picks; i++){
            bsi.printAllBoxIDsLeft();
            bsi.printAllAmountsLeft();
            int input = inputInteger("Input a box I.D:");
            bsi.openBox(input);
            System.out.println("You selected box no. " + input);
        }
        roundsPlayed++;
    }

    public void bankerOffer(){
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
        //offerHistory.add(offer);

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
            amountWon = amount;
        }
        else{
            if(amount > dealtAmount){
                System.out.println("Tough luck, you won " + bsi.poundsValueStringInator(dealtAmount) + ", but you could have earned more");
            }
            else{
                System.out.println("Congrats, you beat the banker and took home " + bsi.poundsValueStringInator(dealtAmount));
            }
            amountWon = dealtAmount;
        }
    }


    public void setPlayerName(){
        System.out.println("Please input your player name");
        System.out.println("Be sure to only include letters and numbers, and be less than 14 characters:");
        while(true){
            playerName = sc.nextLine();
            if(playerName.matches("^[\\p{L}0-9']+$") && playerName.length()<14){
                break;
            }
            else{
                System.out.println("Name was invalid, please try again:");
            }
        }
    }

    public void printHighScores(){
        System.out.printf("\n\t----- High Scores -----\n");
        String[] players = highScoresMap.keySet().toArray(new String[0]);
        for(int i = 0; i<highScoresMap.size(); i++){
            System.out.printf("%-14s %16s\n", players[i], bsi.poundsValueStringInator(highScoresMap.get(players[i])));
        }
    }

    public void addNewScoreToMap(){
        // Se hvis finns det noen høyscore på ledertavlen med players navn
        if(!highScoresMap.containsKey(playerName)){
            if(highScoresMap.size() == 10){  // Tall sier hvor mange resultater kan ligge på ledertavlen
                Set<Map.Entry<String, Float>> entries = highScoresMap.entrySet();
                Iterator<Map.Entry<String, Float>> iterator = entries.iterator();
                Map.Entry<String, Float> entry, last = null;

                while(iterator.hasNext()){
                    entry = iterator.next();
                    last = entry;
                }
                if(amountWon > last.getValue()){
                    System.out.println("Congrats " + playerName + " , you made the leaderboard!");
                    highScoresMap.remove(last.getKey());
                    highScoresMap.put(playerName, amountWon);
                }
                else{
                    System.out.println("You didn't make it onto the leaderboard");
                }

            }
            else{
                System.out.println("You made the leaderboard, but there was space free anyway...");
                highScoresMap.put(playerName, amountWon);
            }

        }
        else{
            // Endre poengsum hvis spilleren har slått sin høyscore
            if(amountWon > highScoresMap.get(playerName) ){
                System.out.println("You beat your high score!");
                highScoresMap.replace(playerName, amountWon);
            }
            else{
                System.out.println("You have a score on the leaderboard, but you didn't beat it.");
            }
        }
        highScoresMap = sortHighScoreMap();
    }

    public LinkedHashMap<String, Float> sortHighScoreMap(){
        LinkedHashMap<String, Float> outMap = new LinkedHashMap<>();
        highScoresMap.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).
                forEach(entry -> outMap.put(entry.getKey(), entry.getValue()));
        return outMap;
    }


    public int inputInteger(String promptMsg){
        int input = 0;
        while(true){
            try{
                System.out.println(promptMsg);
                input = Integer.parseInt(sc.nextLine());

                for(int i = 0; i < bsi.getBoxArray().size(); i++){
                    if(bsi.getBoxArray().get(i).getBoxID()  ==  input  &&  bsi.getBoxArray().get(i).getBoxID()  !=  bsi.getUserBox().getBoxID()){
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
