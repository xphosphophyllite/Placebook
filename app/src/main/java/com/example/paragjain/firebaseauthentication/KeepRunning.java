package com.example.paragjain.firebaseauthentication;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class KeepRunning extends Service {
    public KeepRunning() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
       return null;
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // do your jobs here
        return super.onStartCommand(intent, flags, startId);
    }
}
