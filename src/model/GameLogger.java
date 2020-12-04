package model;

import viewer.GameModel;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * This singleton class represents a logger of the game to write logs in the "/logs/" directory
 * @author COMP2013
 */
public class GameLogger extends Logger {

    /**
     * The Logger that writes and saves game logs
     */
    private Logger logger;

    /**
     * The standard date format used in game logs
     */
    private DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    /**
     * The calendar to get time
     */
    private Calendar calendar = Calendar.getInstance();

    /**
     * The unique instance of GameLogger class
     */
    private static GameLogger gameLogger;
    static {
        try {
            gameLogger = new GameLogger();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Constructs a directory and a .log file,
     * adds the handler to that file to the logger, and
     * sets the format.
     * @throws IOException on access issues
     */
    private GameLogger() throws IOException {
        super("GameLogger", null);
        logger = Logger.getLogger("GameLogger");
        //create a directory
        File directory = new File(System.getProperty("user.dir") + "/" + "logs");
        directory.mkdirs();

        FileHandler fh = new FileHandler(directory + "/" + GameModel.getM_GAMENAME() + ".log");
        logger.addHandler(fh);
        SimpleFormatter formatter = new SimpleFormatter();
        fh.setFormatter(formatter);
    }

    /**
     * Gets the unique instance of GameLogger class
     * @return the unique instance of GameLogger class
     */
    public static GameLogger getUniqueInstance(){
        if(gameLogger == null){
            try {
                gameLogger = new GameLogger();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return gameLogger;
    }

    /**
     * Converts a String into given format
     * @param message  is the String to be formatted
     * @return a String in the format "dd/MM/yyyy HH:mm:ss -- message"
     */
    private String createFormattedMessage(String message) {
        return dateFormat.format(calendar.getTime()) + " -- " + message;
    }

    /**
     * Logs INFO in the format "dd/MM/yyyy HH:mm:ss -- INFO"
     * @param message  is the message to be logged
     */
    public void info(String message) {
        logger.info(createFormattedMessage(message));
    }

    /**
     * Logs warning message in the format "dd/MM/yyyy HH:mm:ss -- WARNING"
     * @param message  is the message to be logged
     */
    public void warning(String message) {
        logger.warning(createFormattedMessage(message));
    }

    /**
     * Logs severe message in the format "dd/MM/yyyy HH:mm:ss -- SEVERE"
     * @param message  is the message to be logged
     */
    public void severe(String message) {
        logger.severe(createFormattedMessage(message));
    }
}