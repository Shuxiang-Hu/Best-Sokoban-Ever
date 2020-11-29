package sample;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class GameLogger extends Logger {

    private static Logger m_logger = Logger.getLogger("GameLogger");
    private DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    private Calendar calendar = Calendar.getInstance();



    /**
     * This constructor creates a directory and a .log file,
     * adds the handler to that file to the logger, and
     * sets the format.
     * @throws IOException on access issues
     */
    public GameLogger() throws IOException {
        super("GameLogger", null);

        //create a directory
        File directory = new File(System.getProperty("user.dir") + "/" + "logs");
        directory.mkdirs();

        FileHandler fh = new FileHandler(directory + "/" + GameModel.M_GAMENAME + ".log");
        m_logger.addHandler(fh);
        SimpleFormatter formatter = new SimpleFormatter();
        fh.setFormatter(formatter);
    }

    public static Logger getM_logger() {
        return m_logger;
    }

    /**
     * This method converts a String into given format
     * @param message  is the String to be formatted
     * @return a String in the format "dd/MM/yyyy HH:mm:ss -- message"
     */
    private String createFormattedMessage(String message) {
        return dateFormat.format(calendar.getTime()) + " -- " + message;
    }

    /**
     * This method logs INFO in the format "dd/MM/yyyy HH:mm:ss -- INFO"
     * @param message  is the message to be logged
     */
    public void info(String message) {
        m_logger.info(createFormattedMessage(message));
    }

    /**
     * This method logs warning message in the format "dd/MM/yyyy HH:mm:ss -- WARNING"
     * @param message  is the message to be logged
     */
    public void warning(String message) {
        m_logger.warning(createFormattedMessage(message));
    }

    /**
     * This method logs severe message in the format "dd/MM/yyyy HH:mm:ss -- SEVERE"
     * @param message  is the message to be logged
     */
    public void severe(String message) {
        m_logger.severe(createFormattedMessage(message));
    }
}