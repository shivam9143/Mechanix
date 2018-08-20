package com.androidsolutions.shivam.mechanix;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


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
    public uprofile() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        s=new SessionManager();
        name=s.getPreferences(getActivity(),"name");
        uid=s.getPreferences(getActivity(),"uid");
        sadd=s.getPreferences(getActivity(),"sadd");
        sloc=s.getPreferences(getActivity(),"sloc");
        mob=s.getPreferences(getActivity(),"mob");
        pn=rv.findViewById(R.id.user_profile_name);
        pbio=rv.findViewById(R.id.user_profile_short_bio);
        shadd=rv.findViewById(R.id.sadd);
        shloc=rv.findViewById(R.id.sloc);
        smob=rv.findViewById(R.id.mobile);
        pn.setText(name);
        pbio.setText(uid);
        shadd.setText(sadd);
        shloc.setText(sloc);
        smob.setText(mob);
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
/*

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
*/

   /* @Override
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
   /* public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }*/
}
