package sample;

import java.util.List;

public class GameRecord {
    private String useName;
    private long time;
    private int numberOfMoves;
    private int levelIndex;

    public GameRecord(String username,long time,int numberOfMoves,int levelIndex) {
        this.useName = username;
        this.time = time;
        this.numberOfMoves = numberOfMoves;
        this.levelIndex = levelIndex;
    }

    public int getLevelIndex() {
        return levelIndex;
    }

    public void setLevelIndex(int levelIndex) {
        this.levelIndex = levelIndex;
    }

    public int getNumberOfMoves() {
        return numberOfMoves;
    }

    public void setNumberOfMoves(int numberOfMoves) {
        this.numberOfMoves = numberOfMoves;
    }

    public long getTime() {
        return time;
    }



    public void setTime(long time) {
        this.time = time;
    }

    public String getUseName() {
        return useName;
    }

    public void setUseName(String useName) {
        this.useName = useName;
    }

    public boolean isBetterThan(GameRecord anotherRecord){
        long anotherTime = anotherRecord.getTime();
        int anotherMovesCount = anotherRecord.getNumberOfMoves();
        return (this.time<anotherTime)||((this.time==anotherTime)&&(numberOfMoves<anotherMovesCount));
    }

    @Override
    public String toString() {
        return "Level "+levelIndex+" GameRecord:\n" +
                "user name=" + useName + "\n"+
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
