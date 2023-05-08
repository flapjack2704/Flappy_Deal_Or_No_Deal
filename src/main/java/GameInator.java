public class GameInator {
    public static void main(String[] args){
        GameInator launcher = new GameInator();
        launcher.start();
    }

    private void start(){
        GameRunner game = new GameRunner();
        game.run();  // Begynner spillet
    }
}
