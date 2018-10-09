package com.androidsolutions.shivam.mechanix;

/**
 * Created by Shivam on 9/19/2018.
 */

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RegisterWoutScan extends AppCompatActivity
{
    EditText uid,name,gender,mob,add,co,yob,loc,dist,pc,dob,pswd,cpswd;
    AadhaarCard newCard;
    ProgressBar p;
    static String result=null;
    JsonParser jsonparser=new JsonParser();
    JSONObject job;
    JSONArray jr;
    Button sbt;
    JSONObject jresult=new JSONObject();
    ConnectionDetector cd;
    Boolean isInternetPresent;
    String uid1="",name1="",mmob1="",otp1="",amob="";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_wout_scan);
        jr=new JSONArray();
        sbt=findViewById(R.id.btnSubmit2);
       // p=findViewById(R.id.progressBar2);
        uid=findViewById(R.id.uid2);
        name=findViewById(R.id.name2);
        mob=findViewById(R.id.mob2);
        pswd=findViewById(R.id.pswd2);
        cpswd=findViewById(R.id.cpswd2);
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
                        uid1=uid.getText().toString();
                        name1=name.getText().toString();
                        mmob1=mob.getText().toString();
                        boolean flag=false;
                        if(uid1.matches("")||uid1.length()<12)
                        {
                            showAlertDialog("Please enter a valid Aadhaar Number!");
                            //uid.setError();
                            uid.setText("");
                            flag=true;
                        }
                        if(mmob1.matches("")||mmob1.length()<10)
                        {
                            showAlertDialog("Please enter a valid Mobile Number!");
                                //mob.setError();
                                mob.setText("");
                                flag=true;
                        }
                        if(name1.matches("")||name1.length()<5)
                        {
                            showAlertDialog("Please enter a valid Full Name!");
                            //name.setError();
                            name.setText("");
                            flag=true;
                        }
                        if(!flag)
                        {
                        StringBuilder sb=new StringBuilder();
                        sb.append(mmob1.substring(3,5));
                        sb.append(mmob1.substring(8,10));
                        int r=(int)(Math.random()*((99-0)+1))+0;
                        sb.append(r);
                        otp1=new String(sb);
                        amob="7897175787";
                        job.put("uid", uid1);
                        job.put("name", name1);
                        job.put("gender", "Male");
                        job.put("address", "NA");
                        job.put("co", "NA");
                        job.put("yob", "NA");
                        job.put("loc", "NA");
                        job.put("dist", "NA");
                        job.put("pc", "NA");
                        job.put("dob", "NA");
                        job.put("mob", mob.getText().toString());
                        job.put("otp",otp1);
                        job.put("amob",amob);
                        if (!pswd.getText().toString().equals(cpswd.getText().toString())) {
                            pswd.setError("Passwords do not match, Please re-enter!");
                            pswd.setText("");
                            cpswd.setText("");
                        } else {
                            job.put("pswd", pswd.getText().toString());
                            jr.put(job);
                            jresult.put("results", jr);
                            demo(jresult);
                        }}
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void showAlertDialog(final String scanContent) {
        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterWoutScan.this);

        builder.setMessage(scanContent)
                .setTitle("Result");
        if(scanContent.equals("Registration Successful"))
        {
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // Toast.makeText(RegisterWoutScan.this, "Saved", Toast.LENGTH_SHORT).show();
                    Intent i=new Intent(RegisterWoutScan.this,LoginActivity.class);
                    startActivity(i);
                }
            });
        }
        else
        {
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // Toast.makeText(RegisterWoutScan.this, "Saved", Toast.LENGTH_SHORT).show();
               /*     Intent i=new Intent(RegisterWoutScan.this,LoginActivity.class);
                    startActivity(i);*/
                }
            });
        }

       /* builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Toast.makeText(RegisterWoutScan.this, "Not Saved", Toast.LENGTH_SHORT).show();
            }
        });*/

        builder.show();
    }

    public void demo(JSONObject a)
    {
        try
        {
            result="START";
            Caller1 c=new Caller1();
            c.json=a;
            c.join();
            c.start();
            while(result=="START")
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
            if(result.contains("java.net.ConnectException: Network is unreachable"))
            {
                showAlertDialog(
                        "Network Error!! Please Check Your Connection and try again!!");
            }
            else {
                showAlertDialog(
                        "Registration Successful");
            }
        }
        catch(Exception ex)
        {
            Log.e("ddddd", ex.getMessage().toString());
        }
    }
}
