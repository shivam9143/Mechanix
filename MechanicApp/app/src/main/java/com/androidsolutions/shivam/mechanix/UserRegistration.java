package com.androidsolutions.shivam.mechanix;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

public class UserRegistration extends AppCompatActivity
{
    EditText uid,name,gender,mob,add,co,yob,loc,dist,pc,dob,pswd,cpswd;
    AadhaarCard newCard;
    ProgressBar p;
    static String rslt=null;
    JsonParser jsonparser=new JsonParser();
    JSONObject job;
    JSONArray jr;
    Button sbt;
    JSONObject jresult=new JSONObject();
    ConnectionDetector cd;
    Boolean isInternetPresent;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userreg);
        Bundle b=getIntent().getExtras();
        String xml=b.getString("Details");
        jr=new JSONArray();
        sbt=findViewById(R.id.btnSubmit);
        p=findViewById(R.id.progressBar);
        uid=findViewById(R.id.uid);
        name=findViewById(R.id.name);
        gender=findViewById(R.id.gender);
        mob=findViewById(R.id.mob);
        add=findViewById(R.id.address);
        co=findViewById(R.id.co);
        yob=findViewById(R.id.yob);
        loc=findViewById(R.id.loc);
        dist=findViewById(R.id.dist);
        pc=findViewById(R.id.pc);
        dob=findViewById(R.id.dob);
        pswd=findViewById(R.id.pswd);
        cpswd=findViewById(R.id.cpswd);
        try {
             newCard = new AadhaarXMLParser().parse(xml);
            Toast.makeText(UserRegistration.this,newCard.getFormattedUID()+","+newCard.getAddress(),Toast.LENGTH_LONG).show();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        uid.setText(newCard.uid);
        uid.setFocusable(false);
        name.setText(newCard.name);
        name.setFocusable(false);
        gender.setText(newCard.gender);
        gender.setFocusable(false);
        add.setText(newCard.getAddress());
        add.setFocusable(false);
        co.setText(newCard.co);
        co.setFocusable(false);
        yob.setText(newCard.yob);
        yob.setFocusable(false);
        loc.setText(newCard.loc);
        loc.setFocusable(false);
        dist.setText(newCard.dist);
        dist.setFocusable(false);
        pc.setText(newCard.pincode);
        pc.setFocusable(false);
        dob.setText(newCard.dob);
        dob.setFocusable(false);
        //pwd.setError("Password too short, enter minimum 6 characters!");

        sbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cd = new ConnectionDetector(getApplicationContext());
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
                    job = new JSONObject();
                    try {
                        job.put("uid", newCard.uid);
                        job.put("name", newCard.name);
                        job.put("gender", newCard.gender);
                        job.put("address", newCard.getAddress());
                        job.put("co", newCard.co);
                        job.put("yob", newCard.yob);
                        job.put("loc", newCard.loc);
                        job.put("dist", newCard.dist);
                        job.put("pc", newCard.pincode);
                        job.put("dob", newCard.dob);
                        job.put("mob", mob.getText().toString());
                        if (!pswd.getText().toString().equals(cpswd.getText().toString())) {
                            pswd.setError("Passwords do not match, Please re-enter!");
                            pswd.setText("");
                            cpswd.setText("");
                        } else {
                            job.put("pswd", pswd.getText().toString());
                            jr.put(job);
                            jresult.put("results", jr);
                            demo(jresult);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });


    }

    private void showAlertDialog(final String scanContent) {
        AlertDialog.Builder builder = new AlertDialog.Builder(UserRegistration.this);

        builder.setMessage(scanContent)
                .setTitle("Result");
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
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

    public void demo(JSONObject a)
    {
        try
        {
            rslt="START";
            Caller c=new Caller();
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
            if(rslt.contains("java.net.ConnectException: Network is unreachable"))
            {
                showAlertDialog(
                        "Network Error!! Please Check Your Connection and try again!!");
            }
            else {
                showAlertDialog(
                        "Submitted Successfully");
            }
        }
        catch(Exception ex)
        {
            Log.e("ddddd", ex.getMessage().toString());
        }
    }
}
