package com.example.mumbacapital.Adapter;

/**
 * Created by Narendra on 11/3/2014.
 */

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mumbacapital.R;


public class ImageAdapter extends android.widget.BaseAdapter {
    private Context context;
    private final String[] mobileValues;

    public ImageAdapter(Context context, String[] mobileValues) {
        this.context = context;
        this.mobileValues = mobileValues;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = null;

        if (convertView == null) {

            LayoutInflater mInflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.mobile, null);

            // set value into textview
            TextView textView = (TextView) convertView
                    .findViewById(R.id.grid_item_label);
            textView.setText(mobileValues[position]);
            String customFont = "fonts/arlrdbd.ttf";
            Typeface typeface = Typeface.createFromAsset(context.getAssets(), customFont);
            textView.setTypeface(typeface);
            // set image based on selected text
            ImageView imageView = (ImageView) convertView
                    .findViewById(R.id.grid_item_image);

            String mobile = mobileValues[position];

            if (mobile.equals("New Customer")) {
                imageView.setImageResource(R.drawable.add_cust_icon);
            } else if (mobile.equals("Edit Customer")) {
                imageView.setImageResource(R.drawable.edit_cust_icon);
            } else if (mobile.equals("Search Customer")) {
                imageView.setImageResource(R.drawable.search_cust_icon);
            } else if (mobile.equals("Customer Daily Entry")) {
                imageView.setImageResource(R.drawable.customer_daily_entry);
            } else if (mobile.equals("2 days Report")) {
                imageView.setImageResource(R.drawable.two_days_report);
            } else if (mobile.equals("100 Day Complete Report")) {
                imageView.setImageResource(R.drawable.hundreddaysreport);
            }else if (mobile.equals("1 days Before Report")) {
                imageView.setImageResource(R.drawable.one_day_before);
            } else if (mobile.equals("Loan Complete Report")) {
                imageView.setImageResource(R.drawable.loan_complate_day);
            }else if (mobile.equals("Delete Account Detail")) {
                imageView.setImageResource(R.drawable.delete_customer_detail);
            } else if (mobile.equals("Daily Entry Report")) {
                imageView.setImageResource(R.drawable.daily_entry_icon);
            } else if (mobile.equals("Delete Last Report")) {
                imageView.setImageResource(R.drawable.delete_last_record_report);
            } else if (mobile.equals("Document Detail")) {
                imageView.setImageResource(R.drawable.document_detail);
            }
        }
        return convertView;
    }

    @Override
    public int getCount() {
        return mobileValues.length;
    }

    @Override
    public Object getItem(int position) {
        return mobileValues[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

}