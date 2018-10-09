package com.androidsolutions.shivam.mechanix.adapter;

/**
 * Created by Shivam on 10/1/2018.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.androidsolutions.shivam.mechanix.R;
import com.androidsolutions.shivam.mechanix.util.UIUtil;
import com.daimajia.androidanimations.library.Techniques;

/**
 * Created by varsovski on 28-Sep-15.
 */
public class EffectsAdapter extends BaseAdapter {

    private Context mContext;

    public EffectsAdapter(Context context) {
        mContext = context;
    }

    @Override
    public int getCount() {
        return Techniques.values().length;
    }

    @Override
    public Object getItem(int position) {
        return Techniques.values()[position].getAnimator();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item, null, false);
        TextView t = (TextView) v.findViewById(R.id.list_item_text);
        t.setText(UIUtil.getTechniqueTitle(getItem(position)));
        v.setTag(Techniques.values()[position]);
        return v;
    }


}