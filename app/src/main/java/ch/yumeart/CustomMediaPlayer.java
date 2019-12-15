package ch.yumeart;

import android.content.Context;
import android.media.MediaPlayer;

public class CustomMediaPlayer {

    private MediaPlayer mediaPlayer;
    public static boolean isPlaying = false;

    public static int sound_bad = R.raw.sound_pretty_bad;
    public static int sound_finally = R.raw.sound_finally;
    public static int sound_congrats = R.raw.sound_congrats;
    public static int sound_well_done = R.raw.sound_well_done_congrats;
    public static int sound_great_job = R.raw.sound_great_job;


    public CustomMediaPlayer(Context context, int sound) {
        mediaPlayer = MediaPlayer.create(context, sound);
        mediaPlayer.start();

        isPlaying = true;

        while(mediaPlayer.isPlaying()) {
            return;
        }
        mediaPlayer.release();
        mediaPlayer = null;
        isPlaying = false;
    }

    public void stopPlaying() {
        mediaPlayer.stop();
    }
}
