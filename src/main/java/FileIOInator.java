import java.io.*;
import java.util.LinkedHashMap;

public class FileIOInator{

    public LinkedHashMap<String, Float> readHighScoreFileToMap(){
        LinkedHashMap<String, Float> map = new LinkedHashMap<>();
        try{
            File file = new File("highscores.txt");
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line = br.readLine();
            while((line = br.readLine()) != null){
                String[] arr = line.split(",");
                map.put(arr[0], Float.parseFloat(arr[1]));
            }
            br.close();

        }catch (FileNotFoundException e) {
            System.out.println("Error: file not found");
        }catch(IOException e){
            e.printStackTrace();
        }
        return map;
    }

    public void writeMapToHighScoreFile(LinkedHashMap<String, Float> map){
        try{
            File file = new File("highscores.txt");
            PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));
            pw.println("# playerName,value");
            String[] keys = map.keySet().toArray(new String[0]);
            for(int i = 0; i<map.size(); i++){
                pw.println(keys[i] + "," + map.get(keys[i]));
            }
            pw.close();

        }catch (FileNotFoundException e) {
            System.out.println("Error: file not found");
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
