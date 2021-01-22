package com.example.mumbacapital.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.example.mumbacapital.AppController;
import com.example.mumbacapital.R;



@SuppressLint("ValidFragment")
public class CustomerDetailFragment extends Fragment
{
    String CustId = "", CardNo = "", CustName = "", ContactNo = "", Amount = "", DailyAmt = "", AmtDate = "",PhotoPath = "",totalBalance = "";
    public String RateOfInterest = "";
    public String InterestAmountPerMonth = "";
    public String LoanCompletionDate = "";
    TextView txt_card_no, txt_CustName, txt_ContactNo, txt_Amount, txt_AmtDate,txt_DailyAmt_lable,txt_DailyAmt,txt_monthlyIA_lable,txt_monthlyIA,txt_loan_completion_date_lable,txt_loan_completion_date,txt_balanceAmt_lable,txt_balanceAmt;

    private NetworkImageView img_cust_img;
    private ImageLoader mImageLoader;


    @SuppressLint("ValidFragment")
    public CustomerDetailFragment(String custId, String cardNo, String custName, String contactNo, String amount, String dailyAmt, String amtDate, String photoPath, String totalBalance, String rateOfInterest, String interestAmountPerMonth, String loanCompletionDate) {
        this.CustId = custId;
        this.CardNo = cardNo;
        this.CustName = custName;
        this.ContactNo = contactNo;
        this.Amount = amount;
        this.DailyAmt = dailyAmt;
        this.AmtDate = amtDate;
        this.PhotoPath = photoPath;
        this.totalBalance = totalBalance;
        this.RateOfInterest = rateOfInterest;
        this.InterestAmountPerMonth = interestAmountPerMonth;
        this.LoanCompletionDate = loanCompletionDate;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.customer_detail_fragment, container, false);

        //Toast.makeText(getActivity(),"PartyId Fragment ===> "+this.PartyId,Toast.LENGTH_SHORT).show();

        //root_layout = rootView.findViewById(R.id.root_layout);
        txt_card_no = rootView.findViewById(R.id.txt_card_no);
        txt_CustName = rootView.findViewById(R.id.txt_CustName);
        txt_ContactNo = rootView.findViewById(R.id.txt_ContactNo);
        txt_AmtDate = rootView.findViewById(R.id.txt_AmtDate);
        txt_Amount = rootView.findViewById(R.id.txt_Amount);
        txt_DailyAmt_lable = rootView.findViewById(R.id.txt_DailyAmt_lable);
        txt_DailyAmt = rootView.findViewById(R.id.txt_DailyAmt);

        txt_monthlyIA_lable = rootView.findViewById(R.id.txt_monthlyIA_lable);
        txt_monthlyIA = rootView.findViewById(R.id.txt_monthlyIA);
        txt_loan_completion_date_lable = rootView.findViewById(R.id.txt_loan_completion_date_lable);
        txt_loan_completion_date = rootView.findViewById(R.id.txt_loan_completion_date);
        txt_balanceAmt_lable = rootView.findViewById(R.id.txt_balanceAmt_lable);
        txt_balanceAmt = rootView.findViewById(R.id.txt_balanceAmt);

        img_cust_img = rootView.findViewById(R.id.img_cust_img);

        mImageLoader = AppController.getInstance().getImageLoader();
        Log.e("Fragment","RateOfInterest===>"+RateOfInterest);
        if(RateOfInterest.equalsIgnoreCase("") || RateOfInterest.equalsIgnoreCase("0"))
        {
            txt_DailyAmt_lable.setText("Daily Amount");
            txt_DailyAmt.setVisibility(View.VISIBLE);
            txt_DailyAmt.setText(DailyAmt);

            txt_monthlyIA_lable.setVisibility(View.GONE);
            txt_monthlyIA.setVisibility(View.GONE);
            txt_loan_completion_date_lable.setVisibility(View.GONE);
            txt_loan_completion_date.setVisibility(View.GONE);
            txt_balanceAmt_lable.setVisibility(View.GONE);
            txt_balanceAmt.setVisibility(View.GONE);

            //LinearLayout3.setVisibility(View.VISIBLE);
           // LinearLayout4.setVisibility(View.GONE);
        }
        else
        {
            txt_DailyAmt_lable.setText("Rate of Interest ");
            txt_DailyAmt.setVisibility(View.VISIBLE);
            txt_DailyAmt.setText(RateOfInterest);

            txt_monthlyIA_lable.setVisibility(View.VISIBLE);
            txt_monthlyIA.setVisibility(View.VISIBLE);
            txt_monthlyIA.setText(InterestAmountPerMonth);
            txt_loan_completion_date_lable.setVisibility(View.VISIBLE);
            txt_loan_completion_date.setVisibility(View.VISIBLE);
            txt_loan_completion_date.setText(LoanCompletionDate);
            txt_balanceAmt_lable.setVisibility(View.VISIBLE);
            txt_balanceAmt.setVisibility(View.VISIBLE);
            txt_balanceAmt.setText(totalBalance);

            //LinearLayout3.setVisibility(View.GONE);
           // LinearLayout4.setVisibility(View.VISIBLE);
        }
        txt_card_no.setText(CardNo);
        txt_CustName.setText(CustName);
        txt_ContactNo.setText(ContactNo);
        txt_Amount.setText(Amount);
        //txt_DailyAmt.setText(DailyAmt);
        txt_AmtDate.setText(AmtDate);
        if (!PhotoPath.equalsIgnoreCase("")) {
            img_cust_img.setImageUrl("" + PhotoPath, mImageLoader);
        }



        return rootView;
    }
}
