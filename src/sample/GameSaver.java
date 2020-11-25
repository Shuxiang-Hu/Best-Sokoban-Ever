package sample;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class GameSaver {
    /**
     * saves the game
     */
    public void writeLevels(List<Level> levels,int startIndex,String mapSetName)  {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String fileName = "Sokoban " + dateFormat.format(new Date()) + ".skb";
        String fileDir = System.getProperty("user.dir") + "/resource/GameLayouts";
        File saveDir = new File(fileDir);

        if(!saveDir.exists()){
            saveDir.mkdir();
        }

        Level levelToSave ;

        try {
            Files.write(Paths.get(fileDir+"/"+fileName),("MapSetName: "+mapSetName+"\n").getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
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
