package com.example.mumbacapital.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mumbacapital.API.APIClient;
import com.example.mumbacapital.API.APIInterface;
import com.example.mumbacapital.Adapter.CustomerTwoAndFiftyDayReportAdapter;
import com.example.mumbacapital.Config;
import com.example.mumbacapital.Model.CustomerTwoAndFiftyReport;
import com.example.mumbacapital.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class TwoDaysAndFiftyDaysReportActivity extends Activity {
    TextView txt_total_customer_Count_Value;
    public ArrayList<CustomerTwoAndFiftyReport> ln = new ArrayList<CustomerTwoAndFiftyReport>();
    public ListView list_view;
    private LinearLayout mainLinear;
    ProgressDialog progress;
    public String rslt = "";
    String ActivityName = "";

    CustomerTwoAndFiftyDayReportAdapter customerTwoAndFiftyDayReportAdapter;

    APIInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two_days_and_fifty_days_report);
        apiInterface = APIClient.getClient().create(APIInterface.class);
        Intent intent = getIntent();
        ActivityName = "" + intent.getStringExtra("ActivityName");
        Log.e("ActivityName", "--->" + ActivityName);

        mainLinear = (LinearLayout) findViewById(R.id.LinearLayout3);

        txt_total_customer_Count_Value = (TextView) findViewById(R.id.txt_total_customer_Count_Value);

        list_view = (ListView) findViewById(R.id.list_view);

        PopUpDataBinding();

    }

    public void PopUpDataBinding() {
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
            delete_last_record_report();
            //new LoadAsyncTask().execute("");

        }
    }

    public void delete_last_record_report()
    {
        progress = new ProgressDialog(TwoDaysAndFiftyDaysReportActivity.this);
        progress.setMessage("Loading. Please wait..");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setCanceledOnTouchOutside(false);
        progress.show();

        Call<ResponseBody> call3 = null;
        if (ActivityName.equalsIgnoreCase("2daysReport")) {
            //httppost = new HttpPost(Config.YOUR_SERVER_URL + "two_days_report.php");
            call3 = apiInterface.two_days_report();
        } else {
            //httppost = new HttpPost(Config.YOUR_SERVER_URL + "fifty_days_report.php");
            call3 = apiInterface.fifty_days_report();
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

                    ln.clear();
                    JSONArray jArray = new JSONArray(rslt);
                    //Log.e("log_tag", "Enters SECOND TRY BLOCK 2");
                    for (int i = 0; i < jArray.length(); i++) {
                        //Log.e("log_tag", "Enters SECOND TRY BLOCK 3");
                        JSONObject json_data = jArray.getJSONObject(i);

                        ln.add(new CustomerTwoAndFiftyReport(json_data.getString("CardNo"), json_data.getString("CustName"), json_data.getString("ContactNo"), json_data.getString("No_Of_Day_Pending"), json_data.getString("DailyAmt"), json_data.getString("Amount"), json_data.getString("GuarantorOrIntroducerName"), json_data.getString("GContactNo")));
                    }

                    if (progress != null && progress.isShowing()) {
                        progress.dismiss();
                    }

                    listShow();

                }catch (Exception e){
                    Toast.makeText(TwoDaysAndFiftyDaysReportActivity.this, "Data Not Found", Toast.LENGTH_LONG).show();
                    if (progress != null && progress.isShowing()) {
                        progress.dismiss();
                    }
                    Log.e("log_tag", "Error parsing data " + e.toString());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(TwoDaysAndFiftyDaysReportActivity.this, ""+t.getMessage(), Toast.LENGTH_LONG).show();
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
            progress = new ProgressDialog(TwoDaysAndFiftyDaysReportActivity.this);
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


                    try {
                        DefaultHttpClient httpclient = new DefaultHttpClient();
                        HttpPost httppost = null;
                        if (ActivityName.equalsIgnoreCase("2daysReport")) {
                            httppost = new HttpPost(Config.YOUR_SERVER_URL + "two_days_report.php");
                        } else {
                            httppost = new HttpPost(Config.YOUR_SERVER_URL + "fifty_days_report.php");
                        }

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
                        // Log.e("def", sb.toString());
                        result = sb.toString();
                        if (sb.toString().equalsIgnoreCase("null") || sb.toString().equalsIgnoreCase("")) {
                            rslt = "Fail";
                        } else {
                            rslt = "";
                        }
                    } catch (Exception e) {
                        Log.e("log_tag", "Error converting result " + e.toString());
                    }

                    try {
                        //Log.e("log_tag", "Enters SECOND TRY BLOCK 1");
                        ln.clear();
                        JSONArray jArray = new JSONArray(result);
                        //Log.e("log_tag", "Enters SECOND TRY BLOCK 2");
                        for (int i = 0; i < jArray.length(); i++) {
                            //Log.e("log_tag", "Enters SECOND TRY BLOCK 3");
                            JSONObject json_data = jArray.getJSONObject(i);

                            ln.add(new CustomerTwoAndFiftyReport(json_data.getString("CardNo"), json_data.getString("CustName"), json_data.getString("ContactNo"), json_data.getString("No_Of_Day_Pending"), json_data.getString("DailyAmt"), json_data.getString("Amount"), json_data.getString("GuarantorOrIntroducerName"), json_data.getString("GContactNo")));
                        }
                    } catch (JSONException e) {

                        Log.e("log_tag", "Error parsing data " + e.toString());
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
                Log.e("Inside_IF", "TwoDaysAndFiftyDays--->");
                Toast.makeText(TwoDaysAndFiftyDaysReportActivity.this, "Data Not Found", Toast.LENGTH_LONG).show();

            } else {
                listShow();
            }

        }

        protected void onProgressUpdate(Void... progress) {
            Log.e("inside progress exe", "inside progress exe");

        }
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) TwoDaysAndFiftyDaysReportActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni == null) {
            // There are no active networks.
            return false;
        } else
            return true;
    }

    private void listShow() {
        txt_total_customer_Count_Value.setText("" + ln.size());
        customerTwoAndFiftyDayReportAdapter = new CustomerTwoAndFiftyDayReportAdapter(TwoDaysAndFiftyDaysReportActivity.this, ln);
        list_view.setAdapter(customerTwoAndFiftyDayReportAdapter);
        customerTwoAndFiftyDayReportAdapter.notifyDataSetChanged();
    }
}
