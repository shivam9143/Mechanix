package com.androidsolutions.shivam.mechanix;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.support.v7.app.AlertDialog;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Shivam on 10/5/2018.
 */

public class GetServicesAdapter extends BaseAdapter {


    Context mContext;
    LayoutInflater inflater;
    private List<StoreServices> worldpopulationlist = null;
    private ArrayList<StoreServices> arraylist;

    public GetServicesAdapter(Context context, List<StoreServices> worldpopulationlist) {
        Log.e("Lisstttttttttttttttadap", "Adapterrrrrrr");
        Log.e("count", Integer.toString(worldpopulationlist.size()));
        mContext = context;
        this.worldpopulationlist = worldpopulationlist;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<>();
        this.arraylist.addAll(worldpopulationlist);
    }


    public class ViewHolder {
        TextView cid;
        TextView cname;
        TextView cmob;
        TextView mechname;
        TextView mechmob;
        TextView mechuid;
        TextView time;
        TextView status;
        Button confirm;
    }


    @Override
    public int getCount() {
        return worldpopulationlist.size();
    }

    @Override
    public Object getItem(int i) {
        return worldpopulationlist.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        worldpopulationlist.clear();
        if (charText.length() == 0) {
            worldpopulationlist.addAll(arraylist);
        } else {
            for (StoreServices wp : arraylist) {
                if (wp.getMechname().toLowerCase(Locale.getDefault()).contains(charText)) /*chk*/ {
                    worldpopulationlist.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {

        //  Log.e("txtttttttttttttttholderrrr","viewwwww");
        final ViewHolder holder;
        if (view == null) {
            //    Log.e("txtttttttttttttttholdeeeerrrrrrrr","sadsafjkjkjkjjhkj");
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.service_list, null);
            // Locate the TextViews in listview_item.xml
            holder.cid = view.findViewById(R.id.textid1);
            // Log.e("holder objjjjjjjjj",holder.lev.toString() );
            holder.cname = view.findViewById(R.id.textcname1);
            holder.cmob = view.findViewById(R.id.textcmob1);
            holder.mechname = view.findViewById(R.id.textmechname1);
            holder.mechmob = view.findViewById(R.id.textmechmob1);
            holder.time = view.findViewById(R.id.texttime1);
            holder.status = view.findViewById(R.id.textstatus1);
            holder.mechuid = view.findViewById(R.id.textmechuid1);
            holder.confirm = view.findViewById(R.id.confirm);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextViews
        holder.cid.setText(worldpopulationlist.get(i).getIdd());
        holder.cname.setText(worldpopulationlist.get(i).getCname());
        holder.cmob.setText(worldpopulationlist.get(i).getCmob());
        holder.mechname.setText(worldpopulationlist.get(i).getMechname());
        holder.mechmob.setText(worldpopulationlist.get(i).getMechmob());
        holder.mechuid.setText(worldpopulationlist.get(i).getMechuid());
        holder.status.setText(worldpopulationlist.get(i).getStatus());
        holder.time.setText(worldpopulationlist.get(i).getTimestamp());

        holder.confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              /*  ((MainActivity)mContext);
                fragmentManager.beginTransaction()
                        .replace(R.id.frame_container, new MyBookings()).commit();*/
                String id = worldpopulationlist.get(i).getIdd();
                generateQr.ID = id;
                Time today = new Time(Time.getCurrentTimezone());
                today.setToNow();
                Date date= new Date();
                long time = date.getTime();
                Timestamp ts = new Timestamp(time);
                String timed=String.valueOf(ts);
                timed=timed.trim();
                UserObject uob=new UserObject(worldpopulationlist.get(i).getMechname(),worldpopulationlist.get(i).getMechuid(),worldpopulationlist.get(i).getMechmob(),timed,worldpopulationlist.get(i).getIdd());
                Gson gson = new Gson();
                String  serializeString = gson.toJson(uob);
                Bitmap bitmap = QRCodeHelper.newInstance(mContext).setContent(serializeString).setErrorCorrectionLevel(ErrorCorrectionLevel.Q).setMargin(2).getQRCOde();
                showAlertDialog(bitmap);
                Log.e("Barcode data",serializeString);

            }
        });
        return view;
    }

    private void showAlertDialog(Bitmap bm) {
        ImageView image = new ImageView(mContext);
        image.setImageBitmap(bm);

        AlertDialog.Builder builder =
                new AlertDialog.Builder(mContext).
                        setMessage("Scan this QR Code").
                        setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).
                        setView(image);
        builder.create().show();
    }
}