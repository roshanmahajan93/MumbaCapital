package com.example.mumbacapital.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mumbacapital.API.APIClient;
import com.example.mumbacapital.API.APIInterface;
import com.example.mumbacapital.Fragment.CstomerFinanceDetailFragment;
import com.example.mumbacapital.Fragment.CustomerDetailFragment;
import com.example.mumbacapital.Model.CustomerFinance;
import com.example.mumbacapital.Model.CustomerMonthlyFinance;
import com.example.mumbacapital.R;
import com.example.mumbacapital.Service.CustomerReportPDF;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class CustomerFinanceDetailActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener{

    String CustId = "", CardNo = "", CustName = "", ContactNo = "", Amount = "", DailyAmt = "", AmtDate = "",PhotoPath = "",totalBalance = "";
    public String RateOfInterest = "";
    public String InterestAmountPerMonth = "";
    public String LoanCompletionDate = "";

    public ArrayList<CustomerFinance> ln = new ArrayList<CustomerFinance>();
    public ArrayList<CustomerMonthlyFinance> monthlyln = new ArrayList<CustomerMonthlyFinance>();

    private LinearLayout mainLinear;
    ProgressDialog progress;
    public String rslt = "";
    Button btn_share;

    public CustomerReportPDF customerReportPDF;

    TabLayout tabLayout;
    ViewPager viewPager;
    APIInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_customer_finance_detail_new);

        apiInterface = APIClient.getClient().create(APIInterface.class);
        //DatabaseHelper.init(CustomerFinanceDetailActivity.this);
        customerReportPDF = new CustomerReportPDF();

        Intent intent = getIntent();
        CustId = ""+ intent.getStringExtra("CustId");
        CardNo = ""+ intent.getIntExtra("CardNo", 0);
        CustName="" + intent.getStringExtra("CustName");
        ContactNo="" + intent.getStringExtra("ContactNo");
        Amount="" + intent.getStringExtra("Amount");
        DailyAmt="" + intent.getStringExtra("DailyAmt");
        AmtDate="" + intent.getStringExtra("AmtDate");
        PhotoPath = ""+ intent.getStringExtra("PhotoPath");
        totalBalance = ""+ intent.getStringExtra("totalBalance");
        RateOfInterest = ""+intent.getStringExtra("RateOfInterest");
        InterestAmountPerMonth = "" + intent.getStringExtra("InterestAmountPerMonth");
        LoanCompletionDate = "" + intent.getStringExtra("LoanCompletionDate");

        Log.e("RateOfInterest","===>"+RateOfInterest);


        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        mainLinear = (LinearLayout) findViewById(R.id.mainLinear);
        btn_share = (Button) findViewById(R.id.btn_share);

        PopUpDataBinding();

        btn_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (customerReportPDF.write(CardNo, ln, CardNo, CustName, ContactNo, Amount, DailyAmt, AmtDate)) {
                    File outputFile = new File(Environment.getExternalStorageDirectory() + "/OSRFinanceFile/", "PPR_" + CardNo + ".pdf");
                    Uri uri = Uri.fromFile(outputFile);

                    Intent share = new Intent();
                    share.setAction(Intent.ACTION_SEND);
                    share.setType("application/pdf");
                    share.putExtra(Intent.EXTRA_STREAM, uri);
                    share.setPackage("com.whatsapp");
                    startActivity(share);

                } else {
                    Toast.makeText(CustomerFinanceDetailActivity.this, "File Not Created", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void PopUpDataBinding()
    {
        monthlyln.clear();
        ln.clear();
        if (!isNetworkConnected()) {
            Snackbar snackbar = Snackbar
                    .make(mainLinear, "No internet connection!", Snackbar.LENGTH_LONG)
                    .setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                        }
                    });
            // Changing message text color
            snackbar.setActionTextColor(Color.RED);
            // Changing action button text color
            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(com.google.android.material.R.id.snackbar_text);
            textView.setTextColor(Color.YELLOW);
            snackbar.show();
        } else {
            getCustomerData();
            //new LoadAsyncTask().execute("");
        }
    }

    public void getCustomerData()
    {
        progress = new ProgressDialog(CustomerFinanceDetailActivity.this);
        progress.setMessage("Loading. Please wait..");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setCanceledOnTouchOutside(false);
        progress.show();



        RequestBody requestBody;
        requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("CustId", CustId+"")
                .build();

        Call<ResponseBody> call3=null;

        if(RateOfInterest.equalsIgnoreCase("") || RateOfInterest.equalsIgnoreCase("0"))
        {
            call3 = apiInterface.postStringResponse(requestBody);
        }
        else
        {
            call3 = apiInterface.get_customermonthlyfinancedetail(requestBody);
        }



        call3.enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                try {

                    String responseRecieved = response.body().string();
                    Log.e("responseRecieved","===>"+responseRecieved);

                    ln.clear();
                    monthlyln.clear();
                    JSONArray jArray = new JSONArray(responseRecieved);
                    //Log.e("log_tag", "Enters SECOND TRY BLOCK 2");
                    for (int i = 0; i < jArray.length(); i++) {
                        //Log.e("log_tag", "Enters SECOND TRY BLOCK 3");
                        JSONObject json_data = jArray.getJSONObject(i);

                        if(RateOfInterest.equalsIgnoreCase("") || RateOfInterest.equalsIgnoreCase("0"))
                        {
                            ln.add(new CustomerFinance(json_data.getString("FDate"), json_data.getString("FAmount"), json_data.getString("FineAndPenalty"), json_data.getString("FRemark")));
                        }
                        else
                        {
                            monthlyln.add(new CustomerMonthlyFinance(json_data.getInt("CustFinId"),json_data.getString("FDate"), json_data.getString("FBasicAmount"),json_data.getString("FInterestAmount"), json_data.getString("FineAndPenalty"),json_data.getInt("CustId"),json_data.getInt("IsCompleted"),json_data.getInt("isExtra"), json_data.getInt("InterestValue"),json_data.getString("TotalsMonthlyValue"),json_data.getInt("UserId"), json_data.getString("cal_date"), json_data.getString("SDate")));
                        }
                    }

                    if (progress != null && progress.isShowing()) {
                        progress.dismiss();
                    }



                }catch (Exception e){
                    Toast.makeText(CustomerFinanceDetailActivity.this, "Data Not Found", Toast.LENGTH_LONG).show();
                    if (progress != null && progress.isShowing()) {
                        progress.dismiss();
                    }
                    Log.e("log_tag", "Error parsing data " + e.toString());
                }

                listShow();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(CustomerFinanceDetailActivity.this, ""+t.getMessage(), Toast.LENGTH_LONG).show();
                if (progress != null && progress.isShowing()) {
                    progress.dismiss();
                }
            }

        });
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter=  new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new CustomerDetailFragment(CustId, CardNo, CustName, ContactNo, Amount, DailyAmt, AmtDate, PhotoPath, totalBalance, RateOfInterest, InterestAmountPerMonth, LoanCompletionDate), "Details");
        adapter.addFragment(new CstomerFinanceDetailFragment(InterestAmountPerMonth, AmtDate,RateOfInterest,ln,monthlyln), "Finance");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) CustomerFinanceDetailActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni == null) {
            // There are no active networks.
            return false;
        } else
            return true;
    }

    private void listShow() {
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);

    }

}
