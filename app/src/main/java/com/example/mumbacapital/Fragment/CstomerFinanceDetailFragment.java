package com.example.mumbacapital.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.mumbacapital.Adapter.CustomerFinanceListAdapter;
import com.example.mumbacapital.Adapter.CustomerMonthlyFinanceAdapter;
import com.example.mumbacapital.Model.CustomerFinance;
import com.example.mumbacapital.Model.CustomerMonthlyFinance;
import com.example.mumbacapital.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

@SuppressLint("ValidFragment")
public class CstomerFinanceDetailFragment extends Fragment {

    public List<CustomerFinance> ln = new ArrayList<CustomerFinance>();
    public List<CustomerMonthlyFinance> monthlyln = new ArrayList<CustomerMonthlyFinance>();

    public ListView list_view,monthlylist_view;
    private LinearLayout LinearLayout3,LinearLayout4,linear_previous_amt;
    public TextView txt_loan_completion_date_value,txt_MonthlyInterstAmount;

    private CustomerFinanceListAdapter customerFinanceListAdapter;
    private CustomerMonthlyFinanceAdapter customerMonthlyFinanceAdapter;

    String RateOfInterest="";
    String InterestAmountPerMonth="";
    String AmtDate="";

    @SuppressLint("ValidFragment")
    public CstomerFinanceDetailFragment(String InterestAmountPerMonth, String AmtDate,String RateOfInterest, List<CustomerFinance> modelList, List<CustomerMonthlyFinance> modelList2) {
        this.InterestAmountPerMonth = InterestAmountPerMonth;
        this.AmtDate = AmtDate;
        this.RateOfInterest = RateOfInterest;
        this.ln = modelList;
        this.monthlyln = modelList2;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.customer_finance_detail_fragment, container, false);

        //Toast.makeText(getActivity(),"PartyId Fragment ===> "+this.PartyId,Toast.LENGTH_SHORT).show();


        //root_layout = rootView.findViewById(R.id.root_layout);
        LinearLayout3 = rootView.findViewById(R.id.LinearLayout3);
        LinearLayout4 = rootView.findViewById(R.id.LinearLayout4);
        linear_previous_amt  = rootView.findViewById(R.id.linear_previous_amt);

        txt_loan_completion_date_value = rootView.findViewById(R.id.txt_loan_completion_date_value);
        txt_MonthlyInterstAmount  = rootView.findViewById(R.id.txt_MonthlyInterstAmount);

        list_view = rootView.findViewById(R.id.list_view);
        monthlylist_view = rootView.findViewById(R.id.monthlylist_view);

        if(RateOfInterest.equalsIgnoreCase("") || RateOfInterest.equalsIgnoreCase("0"))
        {
            customerFinanceListAdapter = new CustomerFinanceListAdapter(getActivity(),ln);
            list_view.setAdapter(customerFinanceListAdapter);
            customerFinanceListAdapter.notifyDataSetChanged();

            LinearLayout3.setVisibility(View.VISIBLE);
            LinearLayout4.setVisibility(View.GONE);
            linear_previous_amt.setVisibility(View.GONE);
        }
        else
        {
            customerMonthlyFinanceAdapter = new CustomerMonthlyFinanceAdapter(getActivity(), monthlyln);
            monthlylist_view.setAdapter(customerMonthlyFinanceAdapter);
            customerMonthlyFinanceAdapter.notifyDataSetChanged();

            LinearLayout3.setVisibility(View.GONE);
            LinearLayout4.setVisibility(View.VISIBLE);
            linear_previous_amt.setVisibility(View.VISIBLE);

            txt_loan_completion_date_value.setText("Loan Date = "+AmtDate);

            CustomerMonthlyFinance jobs = null;
            if(monthlyln.size() > 0)
            {
                jobs = monthlyln.get((monthlyln.size()-1));
            }
            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
            try {
                Date date = null;
                int interetsAmount = 0,interetsValue = 0;

                if (monthlyln.size() > 0) {
                    date = format.parse("" + jobs.cal_date);
                    interetsValue = jobs.InterestValue;
                } else {
                    date = format.parse("" + AmtDate);
                }
                Date date2 = Calendar.getInstance().getTime();

                long diff = date2.getTime() - date.getTime();

                int ToatalDays = (int)TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);

                int TatalDiff = (int)(ToatalDays / 31);

                interetsAmount = Integer.parseInt(""+InterestAmountPerMonth);
                interetsAmount = interetsAmount * TatalDiff;

                if(interetsAmount > 0)
                {
                    if(interetsValue > 0)
                    {
                        txt_MonthlyInterstAmount.setText("Interst = "+(interetsAmount+interetsValue));
                    }
                    else
                    {
                        if(interetsValue==0)
                        {
                            interetsValue = (-interetsAmount);
                            txt_MonthlyInterstAmount.setText("Interst = "+(interetsValue));
                        }
                        else
                        {
                            txt_MonthlyInterstAmount.setText("Interst = "+(interetsAmount-interetsValue));
                        }

                    }

                }
                else
                {
                    txt_MonthlyInterstAmount.setText("Interst = "+interetsValue);
                }



            } catch (ParseException e) {
                e.printStackTrace();
            }




        }

        return rootView;
    }

    public int getCountOfDays(String createdDateString, String expireDateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

        Date createdConvertedDate = null, expireCovertedDate = null, todayWithZeroTime = null;
        try {
            createdConvertedDate = dateFormat.parse(createdDateString);
            expireCovertedDate = dateFormat.parse(expireDateString);

            Date today = new Date();

            todayWithZeroTime = dateFormat.parse(dateFormat.format(today));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        int cYear = 0, cMonth = 0, cDay = 0;

        if (createdConvertedDate.after(todayWithZeroTime)) {
            Calendar cCal = Calendar.getInstance();
            cCal.setTime(createdConvertedDate);
            cYear = cCal.get(Calendar.YEAR);
            cMonth = cCal.get(Calendar.MONTH);
            cDay = cCal.get(Calendar.DAY_OF_MONTH);

        } else {
            Calendar cCal = Calendar.getInstance();
            cCal.setTime(todayWithZeroTime);
            cYear = cCal.get(Calendar.YEAR);
            cMonth = cCal.get(Calendar.MONTH);
            cDay = cCal.get(Calendar.DAY_OF_MONTH);
        }

        Calendar eCal = Calendar.getInstance();
        eCal.setTime(expireCovertedDate);

        int eYear = eCal.get(Calendar.YEAR);
        int eMonth = eCal.get(Calendar.MONTH);
        int eDay = eCal.get(Calendar.DAY_OF_MONTH);

        Calendar date1 = Calendar.getInstance();
        Calendar date2 = Calendar.getInstance();

        date1.clear();
        date1.set(cYear, cMonth, cDay);
        date2.clear();
        date2.set(eYear, eMonth, eDay);

        long diff = date2.getTimeInMillis() - date1.getTimeInMillis();

        float dayCount = (float) diff / (24 * 60 * 60 * 1000);

        return (int) dayCount ;
    }

}
