package co.tashawych.areyousure;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;

import java.util.Date;

import co.tashawych.areyousure.models.SMS;
import co.tashawych.areyousure.models.SMSListener;

/**
 * Created from http://stackoverflow.com/questions/5589395/intercept-read-and-cancel-sms-message
 */
public class SMSContentObserver extends ContentObserver {
    public static final int MESSAGE_TYPE_ALL = 0;
    public static final int MESSAGE_TYPE_DRAFT = 3;
    public static final int MESSAGE_TYPE_FAILED = 5;
    public static final int MESSAGE_TYPE_INBOX = 1;
    public static final int MESSAGE_TYPE_OUTBOX = 4;
    public static final int MESSAGE_TYPE_QUEUED = 6;
    public static final int MESSAGE_TYPE_SENT = 2;

    private SMSListener smsListener;
    private ContentObserver contentObserver;

    public SMSContentObserver(Handler handler) {
        super(handler);
    }

    public void start(Context context) {
        Log.d("SMSContentObserver", "start");
        registerContentObserver(context);
    }

    public void stop(Context context) {
        Log.d("SMSContentObserver", "stop");
        context.getContentResolver().unregisterContentObserver(this.contentObserver);
    }

    public void setSMSListener(SMSListener smsListener) {
        this.smsListener = smsListener;
    }

    private void readFromOutgoingSMS(Context context) {
        Cursor localCursor = context.getContentResolver().query(
                Uri.parse("content://sms"), null, null, null, null);
        if (localCursor.moveToNext()) {
            long id = localCursor.getLong(localCursor.getColumnIndex("_id"));
            String protocol = localCursor.getString(localCursor.getColumnIndex("protocol"));
            int type = localCursor.getInt(localCursor.getColumnIndex("type"));
            if (protocol != null || type != MESSAGE_TYPE_QUEUED) {
                localCursor.close();
            }
            if (type == MESSAGE_TYPE_QUEUED) {
                String body = localCursor.getString(localCursor.getColumnIndex("body"));
                String address = localCursor.getString(localCursor.getColumnIndex("address"));
                Date date = new Date(localCursor.getLong(localCursor.getColumnIndex("date")));
                localCursor.close();

                SMS localSMS = new SMS("", address, body, date);
                this.smsListener.reportOutgoingSMS(localSMS);
            }
        }
    }

    private void registerContentObserver(final Context context) {
        this.contentObserver = new ContentObserver(null) {
            @Override
            public void onChange(boolean selfChange) {
                super.onChange(selfChange);
                readFromOutgoingSMS(context);
            }
        };
        context.getContentResolver().registerContentObserver(
                Uri.parse("content://sms"), true, this.contentObserver);
    }
}
