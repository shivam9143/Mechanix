package com.androidsolutions.shivam.mechanix;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;

public class submit extends AppCompatActivity {
    static String rslt=null;
    Button sbt;
    JSONObject job;
    JSONObject jresult;
    JSONArray jr;
    TextView js1;
    ConnectionDetector cd;
    boolean isInternetPresent;
    String lat,lon,add,fadd;
    SessionManager s;
    String resultedData="";
    LoadJS2 js;
    JsonParser jsonparser=new JsonParser();
    ProgressDialog dialog;
    String uid;
    String h1="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sbt=findViewById(R.id.btnSub);
        js1=findViewById(R.id.json);
        dialog=new ProgressDialog(submit.this);
        Bundle b=getIntent().getExtras();
        lat=b.getString("lat");
        lon=b.getString("lon");
        add=b.getString("add");
        fadd=b.getString("fadd");
        s=new SessionManager();
        uid=s.getPreferences(submit.this,"uid");
        sbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cd = new ConnectionDetector(submit.this);
                isInternetPresent = cd.isConnectingToInternet();

                if (!isInternetPresent)
                {
                    Log.e("innnnnnnn", "innnnnn");
                    // Internet Connection is Present
                    // make HTTP requests
                    showAlertDialog(
                            "You don't have internet connection");
                    return;
                }
                else {

                    js = new LoadJS2();
                    js.execute("");
                }

            }
        });
    }

    private void showAlertDialog( String scanContent) {
        AlertDialog.Builder builder = new AlertDialog.Builder(submit.this);

        builder.setMessage(scanContent)
                .setTitle("Result");
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // Toast.makeText(UserRegistration.this, "Saved", Toast.LENGTH_SHORT).show();


            }
        });
        builder.show();
    }
    public void demo(JSONObject a)
    {
        Toast.makeText(submit.this,"demo",Toast.LENGTH_SHORT).show();

        try
        {
            rslt="START";
            Caller1 c=new Caller1();
            c.json=a;
            c.join();
            c.start();
            while(rslt=="START")
            {
                try
                {
                    Thread.sleep(10);
                }
                catch(Exception ex)
                {
                }
            }
            // Log.e("rslt", rslt);
            if(rslt.contains("(Network is unreachable)"))
            {
                showAlertDialog(
                        "Network Error!! Please Check Your Connection!!");
            }
            else {
                showAlertDialog(
                        "Submitted Successfully,"+rslt.toString());
            }
        }
        catch(Exception ex)
        {
            Toast.makeText(submit.this,ex.getMessage().toString(),Toast.LENGTH_SHORT).show();
            Log.e("ddddd", ex.getMessage().toString());
        }
    }
      class LoadJS2 extends AsyncTask<String, String, String>
    {

        protected String doInBackground(String... params)
        {
            try
            {
//                Toast.makeText(submit.this,"iside back",Toast.LENGTH_SHORT).show();
  //              Toast.makeText(submit.this, "Message", Toast.LENGTH_SHORT).show();
                url ur=new url();
                String lat1=lat;
                //lat1= URLEncoder.encode(lat1, java.nio.charset.StandardCharsets.UTF_8.toString());
                String lon1=lon;
               // lon1=URLEncoder.encode(lon1, java.nio.charset.StandardCharsets.UTF_8.toString());
                //Toast.makeText(getActivity(),uid.toString(),Toast.LENGTH_SHORT).show();
                add=URLEncoder.encode(add, "UTF-8");

                fadd=URLEncoder.encode(fadd, "UTF-8");
                //et.setText(lat1+","+lon1+","+uid+","+add+","+add2);
                 h1=ur.url+"/UpdateLocation?lat="+lat1+"&lon="+lon1+"&safond="+add+"&saentered="+fadd+"&uid="+uid.toString();
                Log.e("errorrrr",h1);
                //            js1.setText(h1);
                resultedData = jsonparser.getJSON(h1);
                // Log.e("url=",URLEncoder.encode(h1, java.nio.charset.StandardCharsets.UTF_8.toString())+"");
                Log.e("result",resultedData);
            }
            catch (Exception ex)
            {
                Log.e("url=",fadd+","+add);
                Log.e("url=",h1);
      //          Toast.makeText(submit.this, ex.getMessage()+resultedData+"fuckedup", Toast.LENGTH_SHORT).show();
                resultedData = "There's an error, that's all I know right now.. :("+ex.getMessage();
            }
            return resultedData;
        }

        @Override
        protected void onPreExecute()
        {
          //  Toast.makeText(submit.this,"Preexecute",Toast.LENGTH_SHORT).show();

            dialog = ProgressDialog.show(submit.this, "Wait",
                    "Updating Location...", true);
        }

        @Override
        protected void onPostExecute(String r)
        {
            Context con1;
            try {
                if ( dialog!=null && dialog.isShowing() ){
                    dialog.dismiss();
                    dialog=null;
                }
                if(resultedData=="")
                showAlertDialog("Location Updated Successfully");
                else
                    showAlertDialog("There is an Error"+",Error Code="+resultedData);


            }
            catch (Exception ex)
            {
				Toast.makeText(submit.this, "error", Toast.LENGTH_LONG)
						.show();
            }
        }
    }
}
