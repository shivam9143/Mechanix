package com.androidsolutions.shivam.mechanix;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.net.URL;
import java.util.Timer;

public class NotificationService {

    Timer t = new Timer();
    //	TimerTask task=new TimerTask() {
//		
//		@Override
//		public void run() {
//			// TODO Auto-generated method stub
//			LoadJS4 js =new LoadJS4();
//
//	    	js.execute();
//		}
//	};
    String str,uid;
    public  NotificationService(String st,String uid)
    {
        this.str=st;
        this.uid=uid;

        LoadJS4 js=new LoadJS4();
        js.execute("");
    }
    String h3,resultedData4=null;
    String[] name=null;
    JSONObject job=new JSONObject();
    String emp;
    JsonParser jsonparser=new JsonParser();
    SessionManager session1=new SessionManager();
   String session;
    String inisid;
  /*  @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }
*/
//	private void addNotification() {
//		NotificationCompat.Builder mBuilder =
//		        new NotificationCompat.Builder(this)
//		        .setSmallIcon(R.drawable.ssn)
//		        .setContentTitle("My notification")
//		        .setContentText("Leave Requests!");
//		NotificationCompat.InboxStyle inboxStyle =
//		        new NotificationCompat.InboxStyle();
//		
//		// Sets a title for the Inbox in expanded layout
//		inboxStyle.setBigContentTitle("Event tracker details:");
//		// Moves events into the expanded layout
//		for (int i=0; i < name.length; i++) {
//
//		    inboxStyle.addLine(name[i]);
//		}
//		// Moves the expanded layout object into the notification object.
//		mBuilder.setStyle(inboxStyle);
//		// Creates an explicit intent for an Activity in your app
//		Intent resultIntent = new Intent(this, NotificationService.class);
//
//		// The stack builder object will contain an artificial back stack for the
//		// started Activity.
//		// This ensures that navigating backward from the Activity leads out of
//		// your application to the Home screen.
//		TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
//		// Adds the back stack for the Intent (but not the Intent itself)
//		stackBuilder.addParentStack(NotificationService.class);
//		// Adds the Intent that starts the Activity to the top of the stack
//		stackBuilder.addNextIntent(resultIntent);
//		PendingIntent resultPendingIntent =
//		        stackBuilder.getPendingIntent(
//		            0,
//		            PendingIntent.FLAG_UPDATE_CURRENT
//		        );
//		mBuilder.setContentIntent(resultPendingIntent);
//		NotificationManager mNotificationManager =
//		    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//		// mId allows you to update the notification later on.
//		mNotificationManager.notify(0, mBuilder.build());
//	}



/*
    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        // TODO Auto-generated method stub

                                      LoadJS4 js =new LoadJS4();

                                      js.execute();

        super.onStart(intent, startId);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO Auto-generated method stub
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub

        super.onDestroy();
    }*/
    public class LoadJS4 extends AsyncTask<String, String, String>
    {


        @Override
        protected void onPreExecute() {


        }
        @Override
        protected void onPostExecute(String r)
        {
        Log.e("peeeeee","executeeeeee");

        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            try{
                int index=0;
                String l="";
                Log.e("String sent=",str);
                URL url2 = new URL(Config.URL_VERIFY_OTP+"?contacts="+str+"&uid="+uid);

                h3=url2.toString();
                Log.e("Rrrrrrrrrrrrrrr urlllll",h3);
                resultedData4 = jsonparser.getJSON(h3);
                Log.e("Rrrrrrrrrrrrrrr urlllll",h3);
                Log.e("Rrr444",resultedData4);
            }
            catch(Exception ex)
            {
                resultedData4 = "There's an error, that's all I know right now.33333333333. :(";

            }
            return resultedData4;
        }

    }


}