package component;

import data.GameRecord;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class GameRecorder {
    private int levelIndex;
    private String recordPath;

    public GameRecorder(int levelIndex){
        this.levelIndex = levelIndex;
        this.recordPath = System.getProperty("user.dir")+ "/resource/GameRecords/Level "+levelIndex+" Record.txt";
    }

    public List<GameRecord> loadRecords(){
        List<GameRecord> recordsToLoad = new ArrayList<GameRecord>();
        InputStream recordFile;
        try {
            recordFile = new FileInputStream(recordPath);
            BufferedReader reader = new BufferedReader(new InputStreamReader(recordFile));
            String username = "";
            int numberOfMoves = 0;
            long time = 0;
            while (true) {
                String line = null;
                try {
                    line = reader.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // Break the loop if EOF is reached
                if (line == null) {
                    break;
                }

                if (line.contains("GameRecord")) {
                    continue;
                }
                if (line.contains("user name=")) {
                    username = line.replace("user name=", "").trim();
                    continue;
                }

                if (line.contains("time=")) {
                    time = Integer.parseInt(line.replace("time=", "").trim());
                    continue;
                }

                if (line.contains("number of moves=")) {
                    numberOfMoves = Integer.parseInt(line.replace("number of moves=", "").trim());
                    GameRecord gameRecord = new GameRecord(username,time,numberOfMoves,levelIndex);
                    recordsToLoad.add(gameRecord);
                    continue;
                }


            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return recordsToLoad;
    }


    public static void sortRecords(List<GameRecord> unsortedRecords){
        int length = unsortedRecords.size();
        if(length > 2){
            for(int i=0;i<=length-2;i++) {
                for(int j=0;j<=length-2;j++){
                    if(unsortedRecords.get(j+1).isBetterThan(unsortedRecords.get(j))) {
                        GameRecord temp = unsortedRecords.get(j);
                        unsortedRecords.set(j, unsortedRecords.get(j+1));
                        unsortedRecords.set(j+1,temp);
                    }
                }
            }
        }
    }

    public void saveRecord(List<GameRecord> levelRecords) {
        String recordsString = "";
        for (int i=0;i<levelRecords.size();i++){
            recordsString += levelRecords.get(i).toString();
        }
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(recordPath));
            out.write(recordsString);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
