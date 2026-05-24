package com.example.projectopoopm05202300903;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.media.AudioClip;
import javafx.util.Duration;

import java.util.HashMap;
import java.util.Map;

public class SoundManager {

    public enum SoundType {
        OPENING,
        BUTTON_CLICK,
        PLAY_CARD,
        WIN,
        DEFEAT
    }

    private static final Map<SoundType, AudioClip> sounds = new HashMap<>();
    private static boolean loaded = false;

    private static AudioClip loadSound(String fileName) {
        var resource = SoundManager.class.getResource(
                "/com/example/projectopoopm05202300903/sounds/" + fileName
        );

        if (resource == null) {
            System.out.println("Sound file not found: " + fileName);
            return null;
        }

        return new AudioClip(resource.toExternalForm());
    }

    public static void loadSounds() {
        if (loaded) return;

        sounds.put(SoundType.OPENING, loadSound("opening_sound.mp3"));
        sounds.put(SoundType.BUTTON_CLICK, loadSound("click_effect.mp3"));
        sounds.put(SoundType.PLAY_CARD, loadSound("play_card_sound.mp3"));
        sounds.put(SoundType.WIN, loadSound("winner_sound.mp3"));
        sounds.put(SoundType.DEFEAT, loadSound("defeat_sound.mp3"));

        loaded = true;
    }

    public static void playSound(SoundType soundType, double secondsToPlay) {
        loadSounds();

        AudioClip sound = sounds.get(soundType);

        if (sound == null) {
            System.out.println("Sound is null: " + soundType);
            return;
        }

        sound.play();

        if (secondsToPlay > 0) {
            Timeline stopTimer = new Timeline(
                    new KeyFrame(Duration.seconds(secondsToPlay), event -> sound.stop())
            );

            stopTimer.play();
        }
    }
}