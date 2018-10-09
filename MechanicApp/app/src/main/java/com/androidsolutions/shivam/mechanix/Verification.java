package com.androidsolutions.shivam.mechanix;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

public class Verification extends AppCompatActivity {

    SessionManager s;
    String amob="",otp="",uid="",name="",mmob="";
    url url=new url();
    ProgressDialog dialog;
    JsonParser jsonparser=new JsonParser();
    Button btn,cont;
    TextView o;
    JSONObject jobj=new JSONObject();
  //  LoadJS js=new LoadJS();

    ConnectionDetector cd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);
        s=new SessionManager();
        uid=s.getPreferences(Verification.this,"uid");
        name=s.getPreferences(Verification.this,"name");
        mmob=s.getPreferences(Verification.this,"mob");
        StringBuilder sb=new StringBuilder();
        sb.append(uid.substring(3,6));
        sb.append(mmob.substring(3,5));
        cd=new ConnectionDetector(Verification.this);
        int r=(int)(Math.random()*((99-0)+1))+0;
        sb.append(r);
        otp=new String(sb);
        amob="7897175787";
        dialog=new ProgressDialog(Verification.this);

        o=findViewById(R.id.otp);
        btn=findViewById(R.id.btnCall);
        cont=findViewById(R.id.cont);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                // Send phone number to intent as data
                intent.setData(Uri.parse("tel:" + "7897175787"));
                // Start the dialer app activity with number
                startActivity(intent);
            }
        });
        cont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(o.getText().equals(null)||o.getText().equals("")||o.getText().toString().length()<6)
                {
                    o.setError("Please enter valid OTP");
                }
                else
                {
                if(cd.isConnectingToInternet())
                 {
            LoadJS js=new LoadJS();
            js.execute("");
             }
        else
        {
            showAlertDialog("Not Connected to Internet!");
        }
                }

            }
        });

    }

    private void showAlertDialog(final String scanContent) {
        AlertDialog.Builder builder = new AlertDialog.Builder(Verification.this);

        builder.setMessage(scanContent)
                .setTitle("Message");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // Toast.makeText(UserRegistration.this, "Saved", Toast.LENGTH_SHORT).show();


            }
        });
       /* builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Toast.makeText(UserRegistration.this, "Not Saved", Toast.LENGTH_SHORT).show();
            }
        });*/

        builder.show();
    }
    private class LoadJS extends AsyncTask<String, String, String>
    {
        String resultedData = null;
        @Override
        protected String doInBackground(String... params) {
            try
            {
                Log.e("emp", uid);
                String h = url.url+"/validateMechOtp?otp="+o.getText().toString()+"&uid="+uid;
               resultedData = jsonparser.getJSON(h);
                Log.e("urlsmss", h);
                Log.e("resultttttt", resultedData);
            } catch (Exception ex) {
                resultedData = ex.getMessage()+" ,There's an error, that's all I know right now.. :(";
            }
            return resultedData;
        }
        @Override
        protected void onPreExecute()
        {
            dialog = ProgressDialog.show(Verification.this, "",
                    "Please wait Requesting OTP...", true);



        }
        @Override
        protected void onPostExecute(String r)
        {

            try {


                if ( dialog!=null && dialog.isShowing() ){
                    dialog.dismiss();
                    dialog=null;
                }
                Log.e("Hello", "world");
              //  jobj = new JSONObject(r);

                //ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
//                res=jobj.getJSONArray("results").toString();
                JSONArray jarray1=new JSONArray(r);

                String res=((jarray1.getJSONObject(0)).getString("result"));
                if(res.equals("1"))
                {
                    Intent i=new Intent(Verification.this,MainActivity.class);
                    startActivity(i);
                }
                else
                {
                    showAlertDialog("Invalid OTP, Please enter valid OTP");
                }
            }
            catch (Exception ex)
            {
                Log.e("web service", ex.getMessage().toString());
                Toast.makeText(Verification.this, ex.getMessage().toString(), Toast.LENGTH_LONG)
                        .show();
            }
        }
    }
}
