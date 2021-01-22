package com.example.mumbacapital.Adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.example.mumbacapital.Model.CustomerMonthlyFinance;
import com.example.mumbacapital.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class CustomerMonthlyFinanceAdapter extends android.widget.BaseAdapter {

    Context context;
    List<CustomerMonthlyFinance> JobsList;


    public CustomerMonthlyFinanceAdapter(Context context, List<CustomerMonthlyFinance> modelList) {
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
            view = mInflater.inflate(R.layout.customer_monthly_finance_adapter_row, null);

            TextView txt_SDate = view.findViewById(R.id.txt_SDate);
            TextView txt_AmtDate_Day = view.findViewById(R.id.txt_AmtDate_Day);
            TextView txt_AmtDate_Month = view.findViewById(R.id.txt_AmtDate_Month);
            TextView txt_AmtDate_Year = view.findViewById(R.id.txt_AmtDate_Year);
            TextView txt_TotlAmtVal = view.findViewById(R.id.txt_TotlAmtVal);
            TextView txt_STimeVal = view.findViewById(R.id.txt_STimeVal);
            TextView txt_interstLableVal = view.findViewById(R.id.txt_interstLableVal);
            TextView txt_BasicAmtVal = view.findViewById(R.id.txt_BasicAmtVal);
            TextView txt_InterestVal = view.findViewById(R.id.txt_InterestVal);
            TextView txt_FineVal = view.findViewById(R.id.txt_FineVal);
            TextView txt_FineLable = view.findViewById(R.id.txt_FineLable);
            LinearLayout date_linear = view.findViewById(R.id.date_linear);

            //android:background="@drawable/light_red_bg"

            final CustomerMonthlyFinance jobs = JobsList.get(i);
            //Log.e("Finance Fragment","SDate===>"+jobs.SDate);
            if(jobs.isExtra == 1)
            {
                date_linear.setBackgroundResource(R.drawable.light_green_bg);
                txt_interstLableVal.setTextColor(context.getResources().getColor(R.color.color12));
            }
            else if(jobs.isExtra == 0 && jobs.InterestValue ==0)
            {
                date_linear.setBackgroundColor(context.getResources().getColor(R.color.white_color));
                txt_interstLableVal.setTextColor(context.getResources().getColor(R.color.black_color));
            }
            else
            {
                date_linear.setBackgroundResource(R.drawable.light_red_bg);
                txt_interstLableVal.setTextColor(context.getResources().getColor(R.color.holo_red));
            }

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat format2 = new SimpleDateFormat("dd-MMM-yyyy");
            SimpleDateFormat format3 = new SimpleDateFormat("HH:mm:ss");


            SimpleDateFormat format4 = new SimpleDateFormat("dd");
            SimpleDateFormat format5 = new SimpleDateFormat("MMM");
            SimpleDateFormat format6 = new SimpleDateFormat("yyyy");
            SimpleDateFormat format7 = new SimpleDateFormat("dd-MM-yyyy");

            try {
                Date date = format.parse(""+jobs.SDate);

                String dateTime = format2.format(date);
                txt_SDate.setText("" + dateTime);
                //Log.e("Finance Fragment","dateTime===>"+dateTime);

                String dateTime2 = format3.format(date);
                txt_STimeVal.setText("" + dateTime2);
                //Log.e("Finance Fragment","dateTime2===>"+dateTime2);

                Date date2 = format7.parse(""+jobs.FDate);

                String dateTime3 = format4.format(date2);
                txt_AmtDate_Day.setText("" + dateTime3);
                Log.e("Finance Fragment","dateTime3===>"+dateTime3);

                String dateTime4 = format5.format(date2);
                txt_AmtDate_Month.setText("" + dateTime4);
                Log.e("Finance Fragment","dateTime4===>"+dateTime4);

                String dateTime5 = format6.format(date2);
                txt_AmtDate_Year.setText("" + dateTime5);
                Log.e("Finance Fragment","dateTime5===>"+dateTime5);

            } catch (ParseException e) {
                e.printStackTrace();
            }


            int totalVal = 0,tempFBasicAmount = 0,tempFInterestAmount=0,tempFineAndPenalty=0;
            if(!jobs.FBasicAmount.equalsIgnoreCase(""))
            {
                tempFBasicAmount = Integer.parseInt(""+jobs.FBasicAmount);
            }
            if(!jobs.FInterestAmount.equalsIgnoreCase(""))
            {
                tempFInterestAmount = Integer.parseInt(""+jobs.FInterestAmount);
            }
            if(!jobs.FineAndPenalty.equalsIgnoreCase(""))
            {
                txt_FineLable.setVisibility(View.VISIBLE);
                txt_FineVal.setVisibility(View.VISIBLE);
                tempFineAndPenalty = Integer.parseInt(""+jobs.FineAndPenalty);
                txt_FineVal.setText("" + jobs.FineAndPenalty);
            }
            else
            {
                txt_FineLable.setVisibility(View.GONE);
                txt_FineVal.setVisibility(View.GONE);
            }

            //totalVal  = tempFBasicAmount + tempFInterestAmount + tempFineAndPenalty;
            txt_TotlAmtVal.setText("" + jobs.TotalsMonthlyValue);

            txt_interstLableVal.setText("" + jobs.InterestValue);
            if(!jobs.FBasicAmount.equalsIgnoreCase(""))
            {
                txt_BasicAmtVal.setText("" + jobs.FBasicAmount);
            }
            else
            {
                txt_BasicAmtVal.setText("0");
            }

            txt_InterestVal.setText("" + jobs.FInterestAmount);


        }
        return view;
    }


}
