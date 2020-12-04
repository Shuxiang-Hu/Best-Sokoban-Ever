package component;

import data.GameLevel;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * This singleton class helps to save uncompleted games in to
 * /resource/GameLayouts/ directory
 * @author Shuxiang Hu
 */
public class GameSaver {
    /**
     * The unique instance of GameSaver class
     */
    private static GameSaver gameSaver = new GameSaver();

    /**
     * Constructs the unique instance of GameSaver class
     */
    private GameSaver() {}


    /**
     * Gets the unique instance of GameSaver class
     * @return the unique instance of GameSaver class
     */
    public static GameSaver getInstance(){
        if(gameSaver == null){
            gameSaver = new GameSaver();
        }
        return gameSaver;
    }
    /**
     * Saves the game, including all uncompleted game levels, into
     * "resource/GameLayouts/Sokoban YY-MM-DD HH-MM-SS.skb"
     */
    public void writeLevels(List<GameLevel> levels, int startIndex, String mapSetName)  {
        //specify file path and name
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String fileName = "Sokoban " + dateFormat.format(new Date()) + ".skb";
        String fileDir = System.getProperty("user.dir") + "/resource/GameLayouts";
        File saveDir = new File(fileDir);

        //creates directory if no exist
        if(!saveDir.exists()){
            saveDir.mkdir();
        }

        GameLevel levelToSave ;

        //save the map name
        try {
            Files.write(Paths.get(fileDir+"/"+fileName),("MapSetName: "+mapSetName+"\n").getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

        //save game levels one by one
        for(int i = startIndex-1;i<levels.size();i++)
        {
            try {
                levelToSave = levels.get(i);
                Files.write(Paths.get(fileDir+"/"+fileName),("LevelName: "+levelToSave.getName()+"\n").getBytes(), StandardOpenOption.APPEND);
                Files.write(Paths.get(fileDir+"/"+fileName),levelToSave.toString().getBytes(), StandardOpenOption.APPEND);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
