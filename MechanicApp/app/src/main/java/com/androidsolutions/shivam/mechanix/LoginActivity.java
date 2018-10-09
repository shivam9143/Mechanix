package com.androidsolutions.shivam.mechanix;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;


public class LoginActivity extends Activity
{
    Button btn;
    String ssn;
    JsonParser jsonparser;
    JSONObject jobj;
    String username="";
    String Password="";
    String uid1="",shopadd="",shoploc="",mob="";
    EditText empid;
    EditText pwd;
    TextView txtRegister;
    TextView mktUrl;
    int status=0;
    Spinner s;
    ProgressDialog dialog;
    ConnectionDetector cd;
    Boolean isInternetPresent = false;
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String Name = "nameKey";
    public static final String Phone = "phoneKey";
    public static final String Email = "emailKey";
    SessionManager session,session2,session3;
    SessionManager session1;
    url url=new url();
    TextView ins;
    Timer t = new Timer();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#275d7b"));
//        getActionBar().setBackgroundDrawable(colorDrawable);
        btn = (Button) findViewById(R.id.btnLogin);
        empid=(EditText)findViewById(R.id.edit_USername);
        pwd=(EditText)findViewById(R.id.edit_Pwd);
        txtRegister=(TextView)findViewById(R.id.btnSignup);
        empid.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
       // mktUrl=(TextView)findViewById(R.id.myImageViewText);
        session2=new SessionManager();
        session3=new SessionManager();
        session=new SessionManager();
        session1=new SessionManager();
        /*mktUrl.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try {
                    Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://google.com/"));
                    startActivity(myIntent);
                } catch (ActivityNotFoundException e)
                {
                    Toast.makeText(getApplicationContext(), "No application can handle this request."
                            + " Please install a webbrowser",  Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            }
        });*/

        txtRegister.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(LoginActivity.this, scanner.class);
                startActivity(i);
            }
        });

        btn.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String uid=empid.getText().toString();
                String p1=pwd.getText().toString();
                Log.e("Passssss", p1);
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
                else
                {
                    if(uid.matches("") || p1.matches(""))
                    {
                        showAlertDialog(
                                "Please Enter Username and Password");
                    }
                    else
                    {
                        jsonparser = new JsonParser();
                        jobj = new JSONObject();
                        LoadJS js = new LoadJS();
                        Log.e("Before execution", "webbbbbbbb");
                        js.execute("");
                        Log.e("afttttterrrrr", "hello");
                    }
                }
            }
        });
    }



    private class LoadJS extends AsyncTask<String, String, String>
    {
        String resultedData = null;
        @Override
        protected String doInBackground(String... params) {
            try
            {
                Log.e("emp", empid.getText().toString());
                String h = url.url+"/MechloginCheck?empid="+empid.getText();
                resultedData = jsonparser.getJSON(h);
                Log.e("resultttttt", resultedData);
            } catch (Exception ex) {
                resultedData = "There's an error, that's all I know right now.. :(";
            }
            return resultedData.toString();
        }
        @Override
        protected void onPreExecute()
        {
            dialog = ProgressDialog.show(LoginActivity.this, "",
                    "Authenticating...", true);

        }
        @Override
        protected void onPostExecute(String r)
        {
            dialog.dismiss();

            try {
                ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
                JSONArray jarray = new JSONArray(r);
                for (int i = 0; i < jarray.length(); i++)
                {
                    HashMap<String, String> datanum = new HashMap<String, String>();
                    jobj = jarray.getJSONObject(i);

                    username=jobj.getString("name").toString();
                    Password=jobj.getString("password").toString();
                    uid1=jobj.getString("uid").toString();
                    shopadd=jobj.getString("shop_loc").toString();
                    shoploc=jobj.getString("s_locality").toString();
                    mob=jobj.getString("mobile").toString();
                    status=Integer.parseInt(jobj.getString("status"));
                    Log.e("uid",uid1);
                    Log.e("name=",username);
                }
                if(username.matches(""))
                {
                    showAlertDialog(
                            "Invalid User Name");
                }
                else
                {
                    if(pwd.getText().toString().equals(Password))
                    {
                        session.setPreferences(LoginActivity.this, "uid", uid1);
                        session1.setPreferences(LoginActivity.this, "name", username);
                        session.setPreferences(LoginActivity.this, "sadd", shopadd);
                        session.setPreferences(LoginActivity.this, "sloc", shoploc);
                        session.setPreferences(LoginActivity.this, "mob", mob);
                        session.setPreferences(LoginActivity.this, "status", status+"");
                        if(status==1)
                        {
                            Intent i = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(i);
                        }
                        else if(status==0)
                        {
                            Intent i = new Intent(LoginActivity.this, Verification.class);
                            startActivity(i);
                        }
                        pwd.setText("");
                        empid.setText("");
                        finish();

                    }
                    else
                    {
                        showAlertDialog(
                                "Invalid username or Password!");
                    }
                }
            }
            catch (Exception ex)
            {
                Log.e("web service", ex.getMessage().toString());
                Toast.makeText(LoginActivity.this, "Internet Connectivity Issue", Toast.LENGTH_LONG)
                        .show();
            }
        }
    }


    private void showAlertDialog(final String scanContent) {
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);

        builder.setMessage(scanContent)
                .setTitle("Result");
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

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);//***Change Here***
        startActivity(intent);
        finish();
        System.exit(0);
    }


}
