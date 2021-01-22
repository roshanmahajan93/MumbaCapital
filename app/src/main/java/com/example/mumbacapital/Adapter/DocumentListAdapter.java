package com.example.mumbacapital.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.example.mumbacapital.AppController;
import com.example.mumbacapital.Model.Document;
import com.example.mumbacapital.R;

import java.util.List;

/**
 * Created by chetu on 1/6/2019.
 */
public class DocumentListAdapter extends android.widget.BaseAdapter {

    ImageLoader mImageLoader;
    Context context;
    List<Document> JobsList;

    public DocumentListAdapter(Context context, List<Document> modelList) {
        this.context = context;
        this.JobsList = modelList;
        mImageLoader = AppController.getInstance().getImageLoader();
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
            view = mInflater.inflate(R.layout.documentlist_adapter_row, null);

            NetworkImageView document_img = (NetworkImageView) view.findViewById(R.id.document_img);
            TextView document_value_txt = (TextView) view.findViewById(R.id.document_value_txt);

            final Document jobs = JobsList.get(i);
            document_value_txt.setText("" + jobs.getDocumentName());
            document_img.setImageUrl("" + jobs.getImagePath(), mImageLoader);

        }
        return view;
    }
}
