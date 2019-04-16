package com.mtpv.mobilee_ticket_services;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;

import com.mtpv.mobilee_ticket.R;

public class RingToneService extends Service {
    static Ringtone r;

    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        r = RingtoneManager.getRingtone(getBaseContext(), Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.alertsound));
        r.play();

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        r.stop();
    }
}

