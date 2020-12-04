package model;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.io.File;

/**
 * This singleton class represents the music player in the game
 * @author COMP2013
 * @author Shuxiang Hu
 */
public class MusicPlayer {
    /**
     * the player of game music
     */
    private final MediaPlayer PLAYER;

    /**
     * The unique instance of MusicPlayer class
     */
    private static MusicPlayer musicPlayer = new MusicPlayer();

    /**
     * Constructs a MusicPlayer instance that plays the following music:
     * "/resource/GameMusic/puzzle_theme.wav"
     */
    private MusicPlayer() {
        File filePath = new File(System.getProperty("user.dir")+"/resource/GameMusic/puzzle_theme.wav");
        Media music = new Media(filePath.toURI().toString());
        this.PLAYER = new MediaPlayer(music);
        this.PLAYER.setOnEndOfMedia(() -> PLAYER.seek(Duration.ZERO));
    }

    /**
     * Checks if the play is playing
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

    /**
     * Gets the unique instance of MusicPlayer class
     * @return the unique instance of MusicPlayer class
     */
    public static MusicPlayer getUniqueInstance(){
        if(musicPlayer == null){
            musicPlayer = new MusicPlayer();
        }
        return musicPlayer;
    }
}
