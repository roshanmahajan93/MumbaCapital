package com.example.mumbacapital.Activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mumbacapital.API.APIClient;
import com.example.mumbacapital.API.APIInterface;
import com.example.mumbacapital.Config;
import com.example.mumbacapital.Model.CustomerFinance;
import com.example.mumbacapital.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class DailyCustomerFinanceEntryActivity extends Activity implements View.OnClickListener {

    private String title;
    private String[] list;
    EditText et_date, et_penalty, et_remark, et_amount, et_cardNo, et_custName, et_basicamount,et_interestamount;
    Button btn_yes;
    private RadioGroup radioGroup1;
    public int radioTemp = 0;
    private TextInputLayout Til_amount,Til_basicamount,Til_interestamount;
    private AlphaAnimation alphaAnimation;
    private int mYear, mMonth, mDay;

    CustomerFinanceDetailActivity customerFinanceDetailActivity;
    String Amount = "", AmtDate = "";
    int CustId = 0;
    ArrayList<CustomerFinance> ln = new ArrayList<CustomerFinance>();
    List<String> categories3 = new ArrayList<String>();

    int listItemClick;

    private TextView textView3;

    private String searchmode;
    ProgressDialog barProgressDialog;
    private LinearLayout dailyvehanaLinearLayoutForCardNo, dailyvehanaLinearLayoutForCustName;
    private ListView lstDailyVehParaCardNo, lstDailyVehParaVehNameCustName;
    ProgressDialog progress;
    public String rslt = "";
    private LinearLayout linearLayoutmain;
    private SharedPreferences prefs;
    private String prefName = "UserDetails";
    String UserId = "";
    String UserName = "";
    String SelectedDate = "";
    APIInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_customer_finance_entry);

        apiInterface = APIClient.getClient().create(APIInterface.class);

        String customFont = "fonts/arlrdbd.ttf";
        Typeface typeface = Typeface.createFromAsset(getAssets(), customFont);
        textView3 = (TextView) findViewById(R.id.textView3);
        textView3.setTypeface(typeface);

        // DatabaseHelper.init(DailyCustomerFinanceEntryActivity.this);
        alphaAnimation = new AlphaAnimation(3.0F, 0.4F);

        prefs = getSharedPreferences(prefName, Context.MODE_PRIVATE);

        UserId = prefs.getString("UId", "");
        UserName = prefs.getString("UserName", "");
        Log.e("DailyEntryActivity", "DailyEntryActivity---->" + UserId);

        searchmode = "normal";

        //barProgressDialog = ProgressDialog.show(DailyCustomerFinanceEntryActivity.this, "Please wait ...", "Downloading...", true);
        //barProgressDialog.setCancelable(false);

        linearLayoutmain = (LinearLayout) findViewById(R.id.LinearLayoutmain);

        dailyvehanaLinearLayoutForCardNo = (LinearLayout) findViewById(R.id.dailyvehanaLinearLayoutForCardNo);
        dailyvehanaLinearLayoutForCustName = (LinearLayout) findViewById(R.id.dailyvehanaLinearLayoutForCustName);
        radioGroup1 = (RadioGroup) findViewById(R.id.radioGroup);
        Til_amount = (TextInputLayout) findViewById(R.id.Til_amount);
        Til_basicamount = (TextInputLayout) findViewById(R.id.Til_basicamount);
        Til_interestamount = (TextInputLayout) findViewById(R.id.Til_interestamount);

        Til_amount.setVisibility(View.VISIBLE);
        Til_basicamount.setVisibility(View.GONE);
        Til_interestamount.setVisibility(View.GONE);

        lstDailyVehParaCardNo = (ListView) findViewById(R.id.lstDailyVehParaCardNo);
        lstDailyVehParaVehNameCustName = (ListView) findViewById(R.id.lstDailyVehParaVehNameCustName);

        et_amount = (EditText) findViewById(R.id.et_amount);
        et_cardNo = (EditText) findViewById(R.id.et_cardNo);
        et_date = (EditText) findViewById(R.id.et_date);
        et_penalty = (EditText) findViewById(R.id.et_penalty);
        et_custName = (EditText) findViewById(R.id.et_custName);

        et_basicamount = (EditText) findViewById(R.id.et_basicamount);
        et_interestamount = (EditText) findViewById(R.id.et_interestamount);

        et_date.setInputType(InputType.TYPE_NULL);
        et_date.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                hideKeyboard(v);
            }
        });

        et_date.setOnClickListener(this);

        radioGroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                RadioButton rb = (RadioButton) radioGroup.findViewById(i);
                if (null != rb) {
                    if (rb.getText().toString().equalsIgnoreCase("Daily")) {
                        radioTemp = 0;
                        Toast.makeText(DailyCustomerFinanceEntryActivity.this, rb.getText(), Toast.LENGTH_SHORT).show();
                        Til_amount.setVisibility(View.VISIBLE);
                        Til_basicamount.setVisibility(View.GONE);
                        Til_interestamount.setVisibility(View.GONE);

                    } else {
                        radioTemp = 1;
                        Toast.makeText(DailyCustomerFinanceEntryActivity.this, rb.getText(), Toast.LENGTH_SHORT).show();
                        Til_amount.setVisibility(View.GONE);
                        Til_basicamount.setVisibility(View.VISIBLE);
                        Til_interestamount.setVisibility(View.VISIBLE);
                    }

                }


            }
        });

        btn_yes = (Button) findViewById(R.id.btn_yes);
        // btn_no=(Button)findViewById(R.id.btn_no);

        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // ln.add(new CustomerFinance(""+et_date.getText().toString(),""+et_amount.getText().toString(),""+et_penalty.getText().toString(),""+et_remark.getText().toString(),CustId));
                //String temp = DatabaseHelper.insertCustomerFinanceDetail(ln);
                //Toast.makeText(DailyCustomerFinanceEntryActivity.this, "" + temp, Toast.LENGTH_LONG).show();

                if (!isNetworkConnected()) {
                    Snackbar snackbar = Snackbar
                            .make(linearLayoutmain, "No internet connection!", Snackbar.LENGTH_LONG)
                            .setAction("RETRY", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                }
                            });
                    // Changing message text color
                    snackbar.setActionTextColor(Color.RED);
                    // Changing action button text color
                    View sbView = snackbar.getView();
                    //TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                    //textView.setTextColor(Color.YELLOW);
                    snackbar.show();
                } else {
                    if (validate()) {
                        update_customer_finance_detail();
                        //new LoadAsyncTask().execute("");
                    }

                }

            }
        });
    }

    public boolean validate() {
        boolean valid = true;

        SelectedDate = et_date.getText().toString();


        if (SelectedDate.isEmpty()) {
            et_date.setError("Date should not left blank");
            valid = false;
        } else {
            et_date.setError(null);
        }


        return valid;
    }

    public void update_customer_finance_detail()
    {
        progress = new ProgressDialog(DailyCustomerFinanceEntryActivity.this);
        progress.setMessage("Loading. Please wait..");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setCanceledOnTouchOutside(false);
        progress.show();

        RequestBody requestBody;
        if(radioTemp == 0)
        {
            requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("CardNo", et_cardNo.getText().toString() + "")
                    .addFormDataPart("FDate", et_date.getText().toString()+"")
                    .addFormDataPart("FAmount", et_amount.getText().toString()+"")
                    .addFormDataPart("FineAndPenaltys", et_penalty.getText().toString()+"")
                    .addFormDataPart("UserId", UserId)
                    .addFormDataPart("UserName", UserName)
                    .build();
        }
        else
        {

            requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("CardNo", et_cardNo.getText().toString() + "")
                    .addFormDataPart("FDate", et_date.getText().toString()+"")
                    .addFormDataPart("FBasicAmount", et_basicamount.getText().toString()+"")
                    .addFormDataPart("FInterestAmount", et_interestamount.getText().toString()+"")
                    .addFormDataPart("FineAndPenaltys", et_penalty.getText().toString()+"")
                    .addFormDataPart("UserId", UserId)
                    .addFormDataPart("UserName", UserName)
                    .build();

        }

        Call<ResponseBody> call3 = null;
        if(radioTemp == 0)
        {
            call3 = apiInterface.update_customer_finance_detail(requestBody);
        }
        else
        {
            call3 = apiInterface.update_customer_finance_detail_monthly(requestBody);
        }

        call3.enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                try {

                    String responseRecieved = response.body().string();
                    Log.e("responseRecieved","===>"+responseRecieved);

                    if (responseRecieved.equalsIgnoreCase("null") || responseRecieved.equalsIgnoreCase("")) {
                        rslt = "Fail";
                    } else {
                        rslt = "" + responseRecieved;
                    }

                    if (progress != null && progress.isShowing()) {
                        progress.dismiss();
                    }

                    if (rslt.equalsIgnoreCase("Fail")) {
                        Log.e("Inside_IF", "LoginActivity--->");
                        Toast.makeText(DailyCustomerFinanceEntryActivity.this, "Error While Inserting/Updating", Toast.LENGTH_LONG).show();

                    } else {

                        Toast.makeText(DailyCustomerFinanceEntryActivity.this, "" + rslt, Toast.LENGTH_LONG).show();
                    }


                }catch (Exception e){
                    Toast.makeText(DailyCustomerFinanceEntryActivity.this, "Error While Inserting/Updating", Toast.LENGTH_LONG).show();
                    if (progress != null && progress.isShowing()) {
                        progress.dismiss();
                    }
                    Log.e("log_tag", "Error parsing data " + e.toString());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(DailyCustomerFinanceEntryActivity.this, ""+t.getMessage(), Toast.LENGTH_LONG).show();
                if (progress != null && progress.isShowing()) {
                    progress.dismiss();
                }
            }

        });
    }

    private class LoadAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            Log.e("inside onpre exe", "inside onpre exe");
            super.onPreExecute();
            progress = new ProgressDialog(DailyCustomerFinanceEntryActivity.this);
            progress.setMessage("Loading. Please wait..");
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setCanceledOnTouchOutside(false);
            progress.show();
        }

        @Override
        protected String doInBackground(String... params) {
            Log.e("inside doinback exe", "inside doinback exe");


            Thread thread = new Thread(new Runnable() {
                public void run() {
                    InputStream is = null;
                    String result = "";


                    ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

                    if(radioTemp == 0)
                    {
                        nameValuePairs.add(new BasicNameValuePair("CardNo", et_cardNo.getText().toString() + ""));
                        nameValuePairs.add(new BasicNameValuePair("FDate", et_date.getText().toString() + ""));
                        nameValuePairs.add(new BasicNameValuePair("FAmount", et_amount.getText().toString() + ""));
                        nameValuePairs.add(new BasicNameValuePair("FineAndPenaltys", et_penalty.getText().toString() + ""));
                        nameValuePairs.add(new BasicNameValuePair("UserId", UserId));
                        nameValuePairs.add(new BasicNameValuePair("UserName", UserName));
                    }
                    else
                    {
                        nameValuePairs.add(new BasicNameValuePair("CardNo", et_cardNo.getText().toString() + ""));
                        nameValuePairs.add(new BasicNameValuePair("FDate", et_date.getText().toString() + ""));
                        nameValuePairs.add(new BasicNameValuePair("FBasicAmount", et_basicamount.getText().toString() + ""));
                        nameValuePairs.add(new BasicNameValuePair("FInterestAmount", et_interestamount.getText().toString() + ""));
                        nameValuePairs.add(new BasicNameValuePair("FineAndPenalty", et_penalty.getText().toString() + ""));
                        nameValuePairs.add(new BasicNameValuePair("UserId", UserId));
                        nameValuePairs.add(new BasicNameValuePair("UserName", UserName));

                    }
                    try {
                        DefaultHttpClient httpclient = new DefaultHttpClient();
                        HttpPost httppost = null;
                        if(radioTemp == 0)
                        {
                            httppost = new HttpPost(Config.YOUR_SERVER_URL + "update_customer_finance_detail.php");
                        }
                        else
                        {
                            httppost = new HttpPost(Config.YOUR_SERVER_URL + "update_customer_finance_detail_monthly.php");
                        }

                        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                        //Log.e("log_tag", "Enters TRY BLOCK 1");
                        HttpResponse response = httpclient.execute(httppost);
                        //Log.e("log_tag", "Enters TRY BLOCK 2");
                        HttpEntity entity = response.getEntity();
                        //Log.e("log_tag", "Enters TRY BLOCK 3");
                        is = entity.getContent();
                        //Log.e("log_tag", "Enters TRY BLOCK 4");

                    } catch (Exception e) {
                        Log.e("abc", "Enters FIRST CATCH BLOCK");
                        Log.e("xyz", "Error in http connection " + e.toString());
                    }

                    //convert response to string
                    try {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
                        StringBuilder sb = new StringBuilder();
                        String line = null;
                        while ((line = reader.readLine()) != null) {
                            sb.append(line);
                        }
                        is.close();
                        Log.e("def", sb.toString());
                        if (sb.toString().equalsIgnoreCase("null") || sb.toString().equalsIgnoreCase("")) {
                            rslt = "Fail";
                        } else {
                            rslt = "" + sb.toString();
                        }
                    } catch (Exception e) {
                        Log.e("log_tag", "Error converting result " + e.toString());
                    }
                }
            });
            thread.start();

            try {
                thread.join();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.e("inside post exe", "inside post exe");
            if (progress != null && progress.isShowing()) {
                progress.dismiss();
            }

            if (rslt.equalsIgnoreCase("Fail")) {
                Log.e("Inside_IF", "LoginActivity--->");
                Toast.makeText(DailyCustomerFinanceEntryActivity.this, "Error While Inserting/Updating", Toast.LENGTH_LONG).show();

            } else {

                Toast.makeText(DailyCustomerFinanceEntryActivity.this, "" + rslt, Toast.LENGTH_LONG).show();
            }

        }

        protected void onProgressUpdate(Void... progress) {
            Log.e("inside progress exe", "inside progress exe");

        }
    }


    @Override
    public void onClick(View v) {
        if (v == et_date) {
            v.startAnimation(alphaAnimation);
            et_date.setError(null);

            // Process to get Current Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);


            // Launch Date Picker Dialog
            final DatePickerDialog dpd = new DatePickerDialog(DailyCustomerFinanceEntryActivity.this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            monthOfYear++;
                            String d, m;

                            if (monthOfYear < 10) {
                                m = "0" + monthOfYear;
                            } else {
                                m = "" + monthOfYear;
                            }

                            if (dayOfMonth < 10) {
                                d = "0" + dayOfMonth;
                            } else {
                                d = "" + dayOfMonth;
                            }


                            et_date.setText(d + "-" + m + "-" + year);


                        }
                    }, mYear, mMonth, mDay);
            dpd.show();


        }
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) DailyCustomerFinanceEntryActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni == null) {
            // There are no active networks.
            return false;
        } else
            return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) DailyCustomerFinanceEntryActivity.this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}