package sample;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.io.File;

public class MusicPlayer {
    private final MediaPlayer PLAYER;
    private static MusicPlayer musicPlayer = new MusicPlayer();
    private MusicPlayer() {
        File filePath = new File(System.getProperty("user.dir")+"/resource/GameMusic/puzzle_theme.wav");
        Media music = new Media(filePath.toURI().toString());
        this.PLAYER = new MediaPlayer(music);
        this.PLAYER.setOnEndOfMedia(() -> PLAYER.seek(Duration.ZERO));
    }

    /**
     * checks if the play is playing
     * @return true if music is on, otherwise false
     */
    private boolean isPlayingMusic() {
        return PLAYER.getStatus() == MediaPlayer.Status.PLAYING;

    }

    /**
     * makes the player play music
     */
    private void playMusic() {
        PLAYER.play();
    }

    /**
     * stop the player from playing music
     */
    private void stopMusic() {
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

    public static MusicPlayer getUniqueInstance(){
        if(musicPlayer == null){
            musicPlayer = new MusicPlayer();
        }
        return musicPlayer;
    }
}
