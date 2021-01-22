package com.example.mumbacapital.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mumbacapital.Model.CustomerDetail;
import com.example.mumbacapital.R;

import java.util.List;

/**
 * Created by chetu on 12/23/2018.
 */
public class DocumentDetailAdapter extends android.widget.BaseAdapter {

    Context context;
    List<CustomerDetail> JobsList;

    public DocumentDetailAdapter(Context context, List<CustomerDetail> modelList) {
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
            view = mInflater.inflate(R.layout.searchlist_adapter_row, null);

            TextView serialNo_value_txt = (TextView) view.findViewById(R.id.serialNo_value_txt);
            TextView jobName_value_txt = (TextView) view.findViewById(R.id.jobName_value_txt);
            TextView expiryDate_value_txt = (TextView) view.findViewById(R.id.expiryDate_value_txt);


            final CustomerDetail jobs = JobsList.get(i);
            serialNo_value_txt.setText("" + jobs.CardNo);
            jobName_value_txt.setText("" + jobs.CustName);
            expiryDate_value_txt.setText("" + jobs.Amount);

            // click listiner for remove button

        }
        return view;
    }
}
