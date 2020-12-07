package model;

import data.GameRecord;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a GameRecorder that helps a GameLevel object to save game records
 * @author Shuxiang Hu
 */
public class GameRecorder {
    /**
     * The index of the GameLevel that the GameRecorder is responsible for
     */
    private int levelIndex;
    /**
     * The file path to save records
     */
    private String recordPath;

    /**
     * Constructs a GameRecorder object by assigning it a GameLevel
     * @param levelIndex The index of the GameLevel that the GameRecorder is responsible for
     */
    public GameRecorder(int levelIndex){
        this.levelIndex = levelIndex;
        this.recordPath = System.getProperty("user.dir")+ "/GameRecords/Level "+levelIndex+" Record.txt";
    }

    /**
     * Loads all the game records of the GameLevel specified by levelIndex in to a list of GameRecord objects
     * @return List of GameRecord objects of the GameLevel specified by levelIndex
     */
    public List<GameRecord> loadRecords(){
        List<GameRecord> recordsToLoad = new ArrayList<GameRecord>();
        InputStream recordFile;
        try {
            recordFile = new FileInputStream(recordPath);
            BufferedReader reader = new BufferedReader(new InputStreamReader(recordFile));
            String username = "";
            int numberOfMoves = 0;
            long time = 0;
            //read the record file line after line
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

                //a new record is found
                if (line.contains("GameRecord")) {
                    continue;
                }
                //user name if found
                if (line.contains("user name=")) {
                    username = line.replace("user name=", "").trim();
                    continue;
                }

                //time is found
                if (line.contains("time=")) {
                    time = Integer.parseInt(line.replace("time=", "").trim());
                    continue;
                }

                //moves count if found
                if (line.contains("number of moves=")) {
                    numberOfMoves = Integer.parseInt(line.replace("number of moves=", "").trim());
                    //create a new record and add it to the return list
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


    /**
     * Sorts a list of GameRecords by their time, if two records have the same time, then compare
     * @param unsortedRecords The list of GameRecord objects to be sorted
     */
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

    /**
     * Saves a list of records in "resource/GameRecords" directory
     * @param levelRecords List of records to be saved
     */
    public void saveRecord(List<GameRecord> levelRecords) {
        //converts the list of GameRecord objects to a string
        String recordsString = "";
        for (int i=0;i<levelRecords.size();i++){
            recordsString += levelRecords.get(i).toString();
        }

        //write them into file
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(recordPath));
            out.write(recordsString);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
