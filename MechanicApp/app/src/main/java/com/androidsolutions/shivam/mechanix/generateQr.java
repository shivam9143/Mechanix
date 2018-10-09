package com.androidsolutions.shivam.mechanix;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;


public class generateQr extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    View rv=null;
    SessionManager s;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    static List<Contact> contacts;
    //private OnFragmentInteractionListener mListener;
    static String st = "";
    static String ID="";

    ImageView im;
    public generateQr() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment generateQr.
     */
    // TODO: Rename and change types and number of parameters
    public static generateQr newInstance(String param1, String param2) {
        generateQr fragment = new generateQr();
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
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        rv= inflater.inflate(R.layout.fragment_generate_qr, container, false);
        s=new SessionManager();
        im=rv.findViewById(R.id.qrCodeImageView);
        Button button1 = (Button) rv.findViewById(R.id.generateQrCodeButton);
       final String name=s.getPreferences(getActivity(),"name");
        final String uid=s.getPreferences(getActivity(),"uid");
        final String mob=s.getPreferences(getActivity(),"mob");

         /*button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
          */      Time today = new Time(Time.getCurrentTimezone());
                today.setToNow();
                Date date= new Date();
                long time = date.getTime();
                Timestamp ts = new Timestamp(time);
                String timed=String.valueOf(ts);
                timed=timed.trim();
                UserObject uob=new UserObject(name,uid,mob,timed,ID);
                Gson gson = new Gson();
                String  serializeString = gson.toJson(uob);
                Log.e("Barcode data",serializeString);
               // String  encryptedString = EncryptionHelper.getInstance().encryptionString(serializeString).encryptMsg();
                setImageBitmap(serializeString);
        /*    }
        });*/
        return  rv;
    }
    private void setImageBitmap(String encryptedString) {
        Bitmap bitmap = QRCodeHelper.newInstance(getActivity()).setContent(encryptedString).setErrorCorrectionLevel(ErrorCorrectionLevel.Q).setMargin(2).getQRCOde();
        im.setImageBitmap(bitmap);
    }
    // TODO: Rename method, update argument and hook method into UI event
  /*  public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
*/
    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    /*public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }*/
}
