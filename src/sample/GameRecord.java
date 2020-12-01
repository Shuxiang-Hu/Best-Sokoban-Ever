package sample;

import java.util.List;

public class GameRecord {


    private final String userName;
    private long time;
    private final int numberOfMoves;
    private final int levelIndex;

    public GameRecord(String username,long time,int numberOfMoves,int levelIndex) {
        this.userName = username;
        this.time = time;
        this.numberOfMoves = numberOfMoves;
        this.levelIndex = levelIndex;
    }

    public int getNumberOfMoves() {
        return numberOfMoves;
    }

    public long getTime() {
        return time;
    }

    public String getUserName() {
        return userName;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public boolean isBetterThan(GameRecord anotherRecord){
        long anotherTime = anotherRecord.getTime();
        int anotherMovesCount = anotherRecord.getNumberOfMoves();
        return (this.time<anotherTime)||((this.time==anotherTime)&&(numberOfMoves<anotherMovesCount));
    }

    @Override
    public String toString() {
        return "Level "+levelIndex+" GameRecord:\n" +
                "user name=" + userName + "\n"+
                "time=" + time + "\n"+
                "number of moves=" + numberOfMoves + "\n";
    }

    public static boolean isTopN(List<GameRecord> records, long newTime,int newNumberOfMoves, int n){
        boolean isTopN ;
        if (records.size() < n) {
            isTopN = true;
        }
        else{
            long oldRecordTime = records.get(n-1).getTime();
            int oldMovesCount = records.get(n-1).getNumberOfMoves();
            isTopN = newTime < oldRecordTime || (newTime == oldRecordTime && newNumberOfMoves < oldMovesCount);
        }
        return isTopN;
    }

}
