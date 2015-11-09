package co.tashawych.areyousure;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import co.tashawych.areyousure.models.SMS;
import co.tashawych.areyousure.models.SMSListener;


public class SMSService extends Service implements SMSListener {
    private Context context;
    private final IBinder binder = new LocalBinder();
    private int serviceId = -1;
    public static SMSContentObserver contentObserver;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("SMSService", "onStartCommand");
        this.serviceId = startId;
        context = getApplicationContext();
        contentObserver = new SMSContentObserver(null);
        contentObserver.setSMSListener(this);
        contentObserver.start(context);
        return Service.START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Context localContext = getApplicationContext();
        contentObserver.stop(localContext);

        try {
            stopSelf(this.serviceId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void reportIncomingSMS(SMS message) {
        Log.d("SMSService", "Incoming message!");
    }

    @Override
    public void reportOutgoingSMS(SMS message) {
        Log.d("SMSService", "Outgoing message");
        Log.d("SMSService", message.to + ": " + message.message);
    }

    public class LocalBinder extends Binder {
        public LocalBinder() {
        }

        public SMSService getService() {
            return SMSService.this;
        }
    }
}
