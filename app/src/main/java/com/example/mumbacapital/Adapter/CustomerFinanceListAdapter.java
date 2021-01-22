package com.example.mumbacapital.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.example.mumbacapital.Model.CustomerFinance;
import com.example.mumbacapital.R;

import java.util.List;

/**
 * Created by Vinayak on 6/6/2018.
 */

public class CustomerFinanceListAdapter extends android.widget.BaseAdapter {

    Context context;
    List<CustomerFinance> JobsList;


    public CustomerFinanceListAdapter(Context context, List<CustomerFinance> modelList) {
        this.context = context;
        this.JobsList = modelList;

    }


    @Override
    public int getCount() {
        return JobsList.size();

    }

    @Override
    public Object getItem(int i) {
        return JobsList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }


    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        view = null;

        if (view == null) {

            LayoutInflater mInflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            view = mInflater.inflate(R.layout.customer_finance_adapter_row, null);

            TextView serialNo_value_txt = (TextView) view.findViewById(R.id.serialNo_value_txt);
            TextView jobName_value_txt = (TextView) view.findViewById(R.id.jobName_value_txt);
            TextView expiryDate_value_txt = (TextView) view.findViewById(R.id.expiryDate_value_txt);
            TextView fine_value_txt = (TextView) view.findViewById(R.id.fine_value_txt);
            TextView remark_value_txt = (TextView) view.findViewById(R.id.remark_value_txt);

            final CustomerFinance jobs = JobsList.get(i);
            serialNo_value_txt.setText("" + (i + 1));
            jobName_value_txt.setText("" + jobs.FDate);
            expiryDate_value_txt.setText("" + jobs.FAmount);
            fine_value_txt.setText("" + jobs.FineAndPenalty);
            remark_value_txt.setText("" + jobs.FRemark);
            // click listiner for remove button

        }
        return view;
    }


}
