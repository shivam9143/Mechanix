package com.androidsolutions.shivam.mechanix;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class MyBookings extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    View rv=null;
    TextView res;
    ProgressDialog dialog3;
    String resultedData4="";
    JsonParser jsonParser;
    JSONObject job3;
    GetServicesAdapter adapter1;
    ArrayList<StoreServices> arraylist1;
    ListView lv1;
    PrefManager pm;
    String name="",mobile="";
    SessionManager s;
    //private OnFragmentInteractionListener mListener;

    public MyBookings() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static MyBookings newInstance(String param1, String param2) {
        MyBookings fragment = new MyBookings();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rv= inflater.inflate(R.layout.fragment_my_bookings, container, false);
        //res=rv.findViewById(R.id.scanres);
        jsonParser=new JsonParser();
        s=new SessionManager();
        dialog3=new ProgressDialog(getActivity());
        arraylist1=new ArrayList<StoreServices>();
        lv1=rv.findViewById(R.id.servicerec);
        pm=new PrefManager(getActivity());
        HashMap<String,String> user=pm.getUserDetails();
        name=user.get("name");
        mobile=user.get("mobile");
        LoadJS3 js=new LoadJS3();
        js.execute("");
        // Bundle b=getActivity().getIntent().getExtras();
        //String xml=b.getString("Details");
        //res.setText();
        return  rv;
    }


    private class LoadJS3 extends AsyncTask<String, String, String>
    {


        @Override
        protected void onPreExecute() {
            dialog3=new ProgressDialog(getActivity());
            dialog3.setMessage("Loading...");
            dialog3.show();
        }
        @Override
        protected void onPostExecute(String r)
        {
            StoreServices SD2=null;
            //Context con1;
            if ( dialog3!=null && dialog3.isShowing() )
            {
                dialog3.dismiss();
                dialog3=null;

            }

            try {

                if ( dialog3!=null && dialog3.isShowing() ){
                    dialog3.dismiss();
                    dialog3=null;
                }

                job3 = new JSONObject();

                JSONArray jarray3 = new JSONArray(r);
                for (int i = 0; i < jarray3.length(); i++)
                {
                    try {
                        job3 = jarray3.getJSONObject(i);
                        Log.e("rrrrrrr1111111111", job3.toString());
                        if (Integer.parseInt(job3.getString("status")) == 0)
                            SD2 = new StoreServices(job3.getString("id"), "Booked/(Not Confirmed)", job3.getString("cname"), job3.getString("cmob"), job3.getString("mech_name"), job3.getString("mech_mob"), job3.getString("uid"), job3.getString("timestamp"));
                        else
                            SD2 = new StoreServices(job3.getString("id"), "Confirmed/(Job Done)", job3.getString("cname"), job3.getString("cmob"), job3.getString("mech_name"), job3.getString("mech_mob"), job3.getString("uid"), job3.getString("timestamp"));
                        arraylist1.add(SD2);
                    }
                    catch (Exception e)
                    {
                        Log.e("Error",e.getMessage().toString());
                    }

                    }
                adapter1=new GetServicesAdapter(getActivity(),arraylist1);
                Log.e("HELLOO IN S222",Integer.toString(adapter1.getCount()));
                lv1.setAdapter(adapter1);
                Log.e("Belooooowll",Integer.toString(adapter1.getCount()));

            }
            catch (Exception ex)
            {
            }
        }



        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            try{
                url u=new url();
                //URL url2 = new URL("http://61.2.24.81:86/calender_details.asmx/getAppliedLevStatus?empid="+emp+"&inisid="+insid+"&enddate="+endate);
                URL url2 = new URL(u.url+"/getAllMechServices?uid="+s.getPreferences(getActivity(),"uid"));
                String h3=url2.toString();
                Log.e("Rrrrrrrr4444444urlllll",h3);

                resultedData4 = jsonParser.getJSON(h3);
                Log.e("Rrrr44444444444444444",resultedData4);
            }
            catch(Exception ex)
            {
                resultedData4 = "There's an error, that's all I know right now.33333333333. :(";

            }

            return resultedData4;
        }

    }


}
