package com.mtpv.mobilee_ticket_services;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;

import com.mtpv.mobilee_ticket.R;

public class VibratorUtils {

    Context context;
    Vibrator vibrator;
    Uri notification;
    MediaPlayer mp;
    Ringtone ringtone;
    long[] pattern = {0, 100, 1000, 300, 200, 100, 500, 200, 100};

    public VibratorUtils(Context context) {
        this.context = context;
        vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);


    }

    public void vibratePhone(long vibrateMilliSeconds) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(vibrateMilliSeconds, VibrationEffect.DEFAULT_AMPLITUDE));
            mp = MediaPlayer.create(context, R.raw.musicm);
            mp.start();
        } else {
            vibrator.vibrate(pattern, 0);
            mp = MediaPlayer.create(context, R.raw.musicm);
            mp.start();
        }
    }

    public void vibrateStopPhone() {
        try {
            vibrator.cancel();
            if (mp.isPlaying()) {
                mp.release();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
