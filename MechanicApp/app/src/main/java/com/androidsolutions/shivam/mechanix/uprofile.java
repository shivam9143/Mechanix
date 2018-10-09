package com.androidsolutions.shivam.mechanix;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link uprofile#newInstance} factory method to
 * create an instance of this fragment.
 */
public class uprofile extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    SessionManager s;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    View rv;
    //private OnFragmentInteractionListener mListener;
    TextView pn,pbio,shadd,shloc,smob;
    String name="",uid="",sadd="",sloc="",mob="";
    JsonParser jsonparser=new JsonParser();
    String resultedData="";
    ProgressDialog dialog;
    JSONObject jobj=new JSONObject();
    public uprofile() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        s=new SessionManager();
        dialog=new ProgressDialog(getActivity());
        name=s.getPreferences(getActivity(),"name");
        uid=s.getPreferences(getActivity(),"uid");
        ConnectionDetector cd=new ConnectionDetector(getActivity());
        if(cd.isConnectingToInternet())
        {
            LoadJS js=new LoadJS();
            js.execute("");
        }
        else
        {
            Toast.makeText(getActivity(),"Internet Connection Problem,Please Retry!",Toast.LENGTH_SHORT).show();
        }
        pn=rv.findViewById(R.id.user_profile_name);
        pbio=rv.findViewById(R.id.user_profile_short_bio);
        shadd=rv.findViewById(R.id.sadd);
        shloc=rv.findViewById(R.id.sloc);
        smob=rv.findViewById(R.id.mobile);
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment uprofile.
     */
    // TODO: Rename and change types and number of parameters
    public static uprofile newInstance(String param1, String param2) {
        uprofile fragment = new uprofile();
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
        rv= inflater.inflate(R.layout.profile_screen_xml_ui_design, container, false);
        return rv;
    }

    private class LoadJS extends AsyncTask<String, String, String>
    {
        String resultedData = null;
        url url=new url();
        @Override
        protected String doInBackground(String... params) {
            try
            {
                Log.e("emp", uid);
                String h = url.url+"/MechloginCheck?empid="+uid;
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
            dialog = ProgressDialog.show(getActivity(), "",
                    "Updating Profile...", true);

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
                    sadd=jobj.getString("shop_loc").toString();
                    sloc=jobj.getString("s_locality").toString();
                    mob=jobj.getString("mobile").toString();
                }
                        s.setPreferences(getActivity(), "sadd", sadd);
                        s.setPreferences(getActivity(), "sloc", sloc);

                sadd=s.getPreferences(getActivity(),"sadd");
                sloc=s.getPreferences(getActivity(),"sloc");
                mob=s.getPreferences(getActivity(),"mob");
                pn.setText(name);
                pbio.setText(uid);
                shadd.setText(sadd);
                shloc.setText(sloc);
                smob.setText(mob);

            }
            catch (Exception ex)
            {
                Log.e("web service", ex.getMessage().toString());
                Toast.makeText(getActivity(), ex.getMessage().toString(), Toast.LENGTH_LONG)
                        .show();
            }
        }
    }
}
