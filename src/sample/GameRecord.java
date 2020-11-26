package sample;

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

    @Override
    public String toString() {
        return "GameRecord:\n" +
                "useName=" + useName + "\n"+
                "time=" + time + "\n"+
                "numberOfMoves=" + numberOfMoves + "\n"+
                "levelIndex=" + levelIndex +"\n";
    }
}
