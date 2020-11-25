package sample;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.io.File;

public class MusicPlayer {
    private final MediaPlayer PLAYER;

    public MusicPlayer() {
        File filePath = new File("src/sample/puzzle_theme.wav");
        Media music = new Media(filePath.toURI().toString());
        this.PLAYER = new MediaPlayer(music);
        this.PLAYER.setOnEndOfMedia(() -> PLAYER.seek(Duration.ZERO));
    }

    /**
     * checks if the play is playing
     * @return true if music is on, otherwise false
     */
    public boolean isPlayingMusic() {
        return PLAYER.getStatus() == MediaPlayer.Status.PLAYING;

    }

    /**
     * makes the player play music
     */
    public void playMusic() {
        PLAYER.play();
    }

    /**
     * stop the player from playing music
     */
    public void stopMusic() {
        PLAYER.stop();
    }

    /**
     * toggles music
     */
    public void toggleMusic() {
        if(isPlayingMusic()){
            stopMusic();
        }
        else {
            playMusic();
        }
    }
}
