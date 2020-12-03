package data;

import java.util.List;

/**
 * Represents a game record of player on a certain game level
 * @author Shuxiang Hu
 */
public class GameRecord {

    /**
     * User name of the player who created this record
     */
    private final String userName;

    /**
     * Time for the player to complete this level, in seconds
     */
    private long time;

    /**
     * Number of moves for the player to complete this level
     */
    private final int numberOfMoves;

    /**
     * Index the game level which the record belongs to
     */
    private final int levelIndex;

    /**
     * Constructs a GameRecord object
     * @param username User name of the player who created this record
     * @param time Time for the player to complete this level, in seconds
     * @param numberOfMoves Number of moves for the player to complete this level
     * @param levelIndex Index the game level which the record belongs to
     */
    public GameRecord(String username,long time,int numberOfMoves,int levelIndex) {
        this.userName = username;
        this.time = time;
        this.numberOfMoves = numberOfMoves;
        this.levelIndex = levelIndex;
    }

    /**
     * Gets the number of moves for the player to complete this level
     * @return  number of moves for the player to complete this level
     */
    public int getNumberOfMoves() {
        return numberOfMoves;
    }

    /**
     * Gets time for the player to complete this level, in seconds
     * @return time for the player to complete this level, in seconds
     */
    public long getTime() {
        return time;
    }


    /**
     * Sets time for the player to complete this level, in seconds
     * @param time time for the player to complete this level, in seconds
     */
    public void setTime(long time) {
        this.time = time;
    }

    /**
     * Compares current GameRecord instances with another one
     * One record is better than another when:
     * 1. its uses less time
     * 2. both uses same time but it uses less moves
     * @param anotherRecord a GameRecord to be compared to
     * @return true if current instance is better than the input record, false otherwise
     */
    public boolean isBetterThan(GameRecord anotherRecord){
        long anotherTime = anotherRecord.getTime();
        int anotherMovesCount = anotherRecord.getNumberOfMoves();
        return (this.time<anotherTime)||((this.time==anotherTime)&&(numberOfMoves<anotherMovesCount));
    }

    /**
     * Converts a GameRecord Instance to a string in the following format:
     * "Level levelIndex GameRecord:\n
     * user name= userName\n
     * time= time\n
     * number of moves= + numberOfMoves\n"
     * @return a string representing current GameRecord instance
     */
    @Override
    public String toString() {
        return "Level "+levelIndex+" GameRecord:\n" +
                "user name=" + userName + "\n"+
                "time=" + time + "\n"+
                "number of moves=" + numberOfMoves + "\n";
    }

    /**
     * check if a record can be entitled into the top n of a list of records
     * @param records the list of records
     * @param newTime the time of the new record to check
     * @param newNumberOfMoves the number of moves of the new record to check
     * @param n the rank
     * @return true is the new record is within the top n of the list of records
     */
    public static boolean isTopN(List<GameRecord> records, long newTime,int newNumberOfMoves, int n){
        boolean isTopN ;
        if (records.size() < n ) {
            isTopN = true;
        }
        else if(n<=0){
            isTopN = false;
        }
        else{
            long oldRecordTime = records.get(n-1).getTime();
            int oldMovesCount = records.get(n-1).getNumberOfMoves();
            isTopN = newTime < oldRecordTime || (newTime == oldRecordTime && newNumberOfMoves < oldMovesCount);
        }
        return isTopN;
    }

}
