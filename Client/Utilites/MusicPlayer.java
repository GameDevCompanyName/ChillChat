package ChillChat.Client.Utilites;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.io.File;

import static ChillChat.Client.Utilites.Constants.MUSIC_VOLUME;
import static ChillChat.Client.Utilites.Constants.PLAY_MUSIC;
import static ChillChat.Client.Utilites.Constants.PLAY_MUSIC_ON_START;

public class MusicPlayer extends Thread{


    private final MediaPlayer soundPlayer;

    public MusicPlayer() {
        String uriString = new File("resources/music/ss13theme.mp3").toURI().toString();
        soundPlayer = new MediaPlayer(new Media(uriString));
        soundPlayer.setVolume(MUSIC_VOLUME);
        soundPlayer.setCycleCount(MediaPlayer.INDEFINITE);
    }

    @Override
    public synchronized void start() {

        if (PLAY_MUSIC_ON_START)
            soundPlayer.play();

    }

    public void slowShutUp() {
        if (PLAY_MUSIC){
            Timeline shutUpAnimation = new Timeline();
            shutUpAnimation.getKeyFrames().add(new KeyFrame(Duration.seconds(0.0), new KeyValue(soundPlayer.volumeProperty(), soundPlayer.getVolume())));
            shutUpAnimation.getKeyFrames().add(new KeyFrame(Duration.seconds(4.0), new KeyValue(soundPlayer.volumeProperty(), 0)));
            shutUpAnimation.play();
            shutUpAnimation.setOnFinished(e -> soundPlayer.stop());
        }

    }

    public void slowPlayFromStart() {
        if (PLAY_MUSIC){
            soundPlayer.play();
            Timeline shutUpAnimation = new Timeline();
            shutUpAnimation.getKeyFrames().add(new KeyFrame(Duration.seconds(0.0), new KeyValue(soundPlayer.volumeProperty(), soundPlayer.getVolume())));
            shutUpAnimation.getKeyFrames().add(new KeyFrame(Duration.seconds(3.0), new KeyValue(soundPlayer.volumeProperty(), MUSIC_VOLUME)));
            shutUpAnimation.play();
        }
    }
}
