package com.example.mumbacapital.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.content.ContextCompat;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.example.mumbacapital.API.APIClient;
import com.example.mumbacapital.API.APIInterface;
import com.example.mumbacapital.Adapter.DocumentListAdapter;
import com.example.mumbacapital.AppController;
import com.example.mumbacapital.Config;
import com.example.mumbacapital.Model.Document;
import com.example.mumbacapital.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class DocumentDetailActivity extends Activity {
    String CustId = "",PhotoPath = "";
    private TextView textView3;
    private ListView documentListView;
    private Button btn_add;
    public ArrayList<Document> documentList = new ArrayList<Document>();
    ImageLoader mImageLoader;
    public int BtnClickView = 0;
    public final int REQUEST_EXTERNAL_STOREAGE_RESULT = 1;

    private NetworkImageView img_cust_img;
    private ImageView img_cust_img2;
    private Button btn_camera;
    private Bitmap bitmap;
    private Uri filePath;
    String data = "", decodeString = "";
    public String rslt = "";

    ProgressDialog progress;
    RelativeLayout mainRelative;

    APIInterface apiInterface;

    DocumentListAdapter documentListAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_document_detail);

        apiInterface = APIClient.getClient().create(APIInterface.class);

        mImageLoader = AppController.getInstance().getImageLoader();

        Intent intent = getIntent();
        CustId =""+ intent.getStringExtra("CustId");
        PhotoPath =""+ intent.getStringExtra("PhotoPath");
        Log.e("CustId", "CustId--->" + CustId);
        Log.e("PhotoPath", "PhotoPath--->" + PhotoPath);

        mainRelative = (RelativeLayout)findViewById(R.id.mainRelative);

        String customFont = "fonts/arlrdbd.ttf";
        Typeface typeface = Typeface.createFromAsset(getAssets(), customFont);
        textView3 = (TextView) findViewById(R.id.textView3);
        textView3.setTypeface(typeface);

        documentListView = (ListView)findViewById(R.id.documentListView);
        documentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Document document = documentList.get(position);
                showNoteDialog(1, document.getDId(), document.getDocumentName(), document.getImagePath());
            }
        });
        documentListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
            {
                Document document = documentList.get(position);
                if (!isNetworkConnected()) {
                    Snackbar snackbar = Snackbar
                            .make(mainRelative, "No internet connection!", Snackbar.LENGTH_LONG)
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

                    new MyAsync().execute(""+document.getImagePath());

                }

                return false;
            }
        });
        btn_add = (Button)findViewById(R.id.btn_add);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNoteDialog(0, 0, "", "");
            }
        });

        if (!isNetworkConnected()) {
            Snackbar snackbar = Snackbar
                    .make(mainRelative, "No internet connection!", Snackbar.LENGTH_LONG)
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
            getdocumentdetail();
            //new LoadAsyncTask().execute("");

        }

    }

    public void getdocumentdetail()
    {
        progress = new ProgressDialog(DocumentDetailActivity.this);
        progress.setMessage("Loading. Please wait..");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setCanceledOnTouchOutside(false);
        progress.show();

        RequestBody requestBody;
        requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("CustId", CustId+"")
                .build();

        Call<ResponseBody> call3 = apiInterface.getdocumentdetail(requestBody);
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

                    documentList.clear();
                    JSONArray jArray = new JSONArray(rslt);
                    //Log.e("log_tag", "Enters SECOND TRY BLOCK 2");
                    for (int i = 0; i < jArray.length(); i++) {
                        //Log.e("log_tag", "Enters SECOND TRY BLOCK 3");
                        JSONObject json_data = jArray.getJSONObject(i);


                        //"CustId":"12","CardNo":"1","CustName":"roshan","ContactNo":"865223655","HomeAddress":"mumbai","BusiAddress":"mumbai","Amount":"1000","DailyAmt":"25","AmtDate":"15-6-2018","GuarantorOrIntroducerName":"xyz","GContactNo":"2445225522","PhotoPath":"http:\/\/petalsinfotech.com\/osrfinance\/images\/15289592361.JPEG","UserId":"1"
                        documentList.add(new Document(json_data.getInt("DId"), json_data.getString("DocumentName"), json_data.getInt("CustId"), json_data.getString("ImagePath")));
                    }

                    if (progress != null && progress.isShowing()) {
                        progress.dismiss();
                    }

                    listShow();


                }catch (Exception e){
                    Toast.makeText(DocumentDetailActivity.this, "Data Not Found", Toast.LENGTH_LONG).show();
                    if (progress != null && progress.isShowing()) {
                        progress.dismiss();
                    }
                    Log.e("log_tag", "Error parsing data " + e.toString());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(DocumentDetailActivity.this, ""+t.getMessage(), Toast.LENGTH_LONG).show();
                if (progress != null && progress.isShowing()) {
                    progress.dismiss();
                }
            }

        });
    }

    public void insert_and_update_document_detail(String params1, String params2, String params3, String params4)
    {
        progress = new ProgressDialog(DocumentDetailActivity.this);
        progress.setMessage("Loading. Please wait..");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setCanceledOnTouchOutside(false);
        progress.show();

        RequestBody requestBody;
        requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("DId", params1+"")
                .addFormDataPart("DocumentName", params2+"")
                .addFormDataPart("CustId", CustId+"")
                .addFormDataPart("ImagePath", params3+"")
                .addFormDataPart("DecodeString", decodeString+"")
                .addFormDataPart("shouldUpdate", params4+"")

                .build();

        Call<ResponseBody> call3 = apiInterface.insert_and_update_document_detail(requestBody);
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
                        Toast.makeText(DocumentDetailActivity.this, "Problem With Update/Add", Toast.LENGTH_LONG).show();

                    } else {

                        Toast.makeText(DocumentDetailActivity.this, "" + rslt, Toast.LENGTH_LONG).show();
                        //new LoadAsyncTask().execute("");
                        getdocumentdetail();
                    }


                }catch (Exception e){
                    Toast.makeText(DocumentDetailActivity.this, "Problem With Update/Add", Toast.LENGTH_LONG).show();
                    if (progress != null && progress.isShowing()) {
                        progress.dismiss();
                    }
                    Log.e("log_tag", "Error parsing data " + e.toString());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(DocumentDetailActivity.this, ""+t.getMessage(), Toast.LENGTH_LONG).show();
                if (progress != null && progress.isShowing()) {
                    progress.dismiss();
                }
            }

        });
    }

    public class MyAsync extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... params) {

            try {
                URL url = new URL(""+params[0].toString());
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                return myBitmap;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }

        }

        @SuppressLint("WrongThread")
        @Override
        protected void onPostExecute(Bitmap bmp) {
            super.onPostExecute(bmp);

            Bitmap bm = bmp;

            /*URL url = new URL(""+document.getImagePath());
            Bitmap image = BitmapFactory.decodeStream(url.openConnection().getInputStream());*/

            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            String path = MediaStore.Images.Media.insertImage(DocumentDetailActivity.this.getContentResolver(), bm, "Title", null);
            Uri uri = Uri.parse(path);

            Intent share = new Intent();
            share.setAction(Intent.ACTION_SEND);
            share.setDataAndType(uri, "image/jpeg");
            share.putExtra(Intent.EXTRA_STREAM, uri);
            share.setPackage("com.whatsapp");
            startActivity(share);





        }

    }

    private class LoadAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            Log.e("inside onpre exe", "inside onpre exe");
            super.onPreExecute();
            progress = new ProgressDialog(DocumentDetailActivity.this);
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
                    nameValuePairs.add(new BasicNameValuePair("CustId", CustId));

                    try {
                        DefaultHttpClient httpclient = new DefaultHttpClient();
                        HttpPost httppost = new HttpPost(Config.YOUR_SERVER_URL + "getdocumentdetail.php");
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
                        documentList.clear();
                        JSONArray jArray = new JSONArray(result);
                        //Log.e("log_tag", "Enters SECOND TRY BLOCK 2");
                        for (int i = 0; i < jArray.length(); i++) {
                            //Log.e("log_tag", "Enters SECOND TRY BLOCK 3");
                            JSONObject json_data = jArray.getJSONObject(i);


                            //"CustId":"12","CardNo":"1","CustName":"roshan","ContactNo":"865223655","HomeAddress":"mumbai","BusiAddress":"mumbai","Amount":"1000","DailyAmt":"25","AmtDate":"15-6-2018","GuarantorOrIntroducerName":"xyz","GContactNo":"2445225522","PhotoPath":"http:\/\/petalsinfotech.com\/osrfinance\/images\/15289592361.JPEG","UserId":"1"
                            documentList.add(new Document(json_data.getInt("DId"), json_data.getString("DocumentName"), json_data.getInt("CustId"), json_data.getString("ImagePath")));
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
                Log.e("Inside_IF", "LoginActivity--->");
                Toast.makeText(DocumentDetailActivity.this, "Data Not Found", Toast.LENGTH_LONG).show();

            } else {
                listShow();
            }

        }

        protected void onProgressUpdate(Void... progress) {
            Log.e("inside progress exe", "inside progress exe");

        }
    }

    private class LoadAsyncTaskInserUpdate extends AsyncTask<String, Void, String>
    {

        @Override
        protected void onPreExecute() {
            Log.e("inside onpre exe", "inside onpre exe");
            super.onPreExecute();
            progress = new ProgressDialog(DocumentDetailActivity.this);
            progress.setMessage("Loading. Please wait..");
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setCanceledOnTouchOutside(false);
            progress.show();
        }

        @Override
        protected String doInBackground(final String... params) {
            Log.e("inside doinback exe", "inside doinback exe");
            Log.e("params[0]", "==>" + params[0].toString());
            Log.e("params[1]", "==>"+params[1].toString());
            Log.e("params[2]", "==>" + params[2].toString());
            Log.e("params[3]", "==>" + params[3].toString());


            Thread thread = new Thread(new Runnable() {
                public void run() {
                    InputStream is = null;
                    String result = "";

                    ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                    //nameValuePairs.add(new BasicNameValuePair("CustId", CustId));
                    nameValuePairs.add(new BasicNameValuePair("DId", ""+params[0].toString()));
                    nameValuePairs.add(new BasicNameValuePair("DocumentName", ""+params[1].toString()));
                    nameValuePairs.add(new BasicNameValuePair("CustId", ""+CustId));
                    nameValuePairs.add(new BasicNameValuePair("ImagePath", ""+params[2].toString()));
                    nameValuePairs.add(new BasicNameValuePair("DecodeString", ""+decodeString));
                    nameValuePairs.add(new BasicNameValuePair("shouldUpdate", ""+params[3].toString()));

                    /*$DId = (int)$_POST['DId'];
                    $DocumentName = $_POST['DocumentName'];
                    $ImagePath = $_POST['ImagePath'];
                    $DecodeString = $_POST['DecodeString'];
                    $shouldUpdate = (int)$_POST['shouldUpdate'];*/

                    try {
                        DefaultHttpClient httpclient = new DefaultHttpClient();
                        HttpPost httppost = new HttpPost(Config.YOUR_SERVER_URL + "insert_and_update_document_detail.php");
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
                            rslt = "";
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
                Toast.makeText(DocumentDetailActivity.this, "Problem With Update/Add", Toast.LENGTH_LONG).show();

            } else {

                Toast.makeText(DocumentDetailActivity.this, "" + rslt, Toast.LENGTH_LONG).show();
                new LoadAsyncTask().execute("");
            }

        }

        protected void onProgressUpdate(Void... progress) {
            Log.e("inside progress exe", "inside progress exe");

        }
    }

    private void listShow() {

        documentListAdapter = new DocumentListAdapter(DocumentDetailActivity.this, documentList);
        documentListView.setAdapter(documentListAdapter);
        documentListAdapter.notifyDataSetChanged();

    }

    private void showNoteDialog(final int shouldUpdate, final int DId, String DocumentName, final String ImagePath) {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getApplicationContext());
        View view = layoutInflaterAndroid.inflate(R.layout.note_dialog, null);

        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(DocumentDetailActivity.this);
        alertDialogBuilderUserInput.setView(view);

        final EditText inputNote = (EditText)view.findViewById(R.id.note_et);
        TextView dialogTitle = (TextView)view.findViewById(R.id.title_txt);
        img_cust_img = (NetworkImageView)view.findViewById(R.id.img_cust_img);
        img_cust_img2 = (ImageView)view.findViewById(R.id.img_cust_img2);
        btn_camera = (Button)view.findViewById(R.id.btn_camera);

        btn_camera.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                BtnClickView = 1;

                if (ContextCompat.checkSelfPermission(DocumentDetailActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    selectImage();
                } else {
                    if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        Toast.makeText(DocumentDetailActivity.this, "External Storage Permission Required To Store Image", Toast.LENGTH_LONG).show();
                    }
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_EXTERNAL_STOREAGE_RESULT);
                }
            }
        });

        dialogTitle.setText(shouldUpdate==0 ? "Add Document" : "Update Document");

        if (shouldUpdate==1 && DId > 0) {
            inputNote.setText(""+DocumentName);
            img_cust_img.setVisibility(View.VISIBLE);
            img_cust_img.setImageUrl("" + ImagePath, mImageLoader);
            img_cust_img2.setVisibility(View.GONE);

        }
        else
        {
            inputNote.setText("");
            img_cust_img.setVisibility(View.GONE);
            img_cust_img2.setVisibility(View.VISIBLE);
        }

        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton(shouldUpdate==1 ? "update" : "save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {

                    }
                })
                .setNegativeButton("cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                            }
                        });

        final AlertDialog alertDialog = alertDialogBuilderUserInput.create();
        alertDialog.show();

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show toast message when no text is entered
                if (TextUtils.isEmpty(inputNote.getText().toString())) {
                    Toast.makeText(DocumentDetailActivity.this, "Enter Document Name!", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    alertDialog.dismiss();
                }

                if (!isNetworkConnected()) {
                    Snackbar snackbar = Snackbar
                            .make(mainRelative, "No internet connection!", Snackbar.LENGTH_LONG)
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
                    insert_and_update_document_detail(""+DId,""+inputNote.getText().toString(),""+ImagePath,""+shouldUpdate);
                    //new LoadAsyncTaskInserUpdate().execute(""+DId,""+inputNote.getText().toString(),""+ImagePath,""+shouldUpdate);

                }
/*
                // check if user updating note
                if (shouldUpdate==1) {
                    // update note by it's id
                    //updateNote(note.getId(), inputNote.getText().toString(), position);
                } else {
                    // create new note
                    createNote(inputNote.getText().toString());
                }*/
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        if (requestCode == REQUEST_EXTERNAL_STOREAGE_RESULT) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                selectImage();
            } else {
                Toast.makeText(DocumentDetailActivity.this, "External storage permission has not been granted, cannot be saved images ", Toast.LENGTH_LONG).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        }
    }

    //....to choose images....
    private void selectImage() {

        final CharSequence[] options = {"Take Photo", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(DocumentDetailActivity.this);

        //builder.setTitle("Add Photo!");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override

            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Take Photo")) {

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, 1);

                } /*else if (options[item].equals("Choose from Gallery")) {
                    //Intent i = new Intent(Action.ACTION_MULTIPLE_PICK);
                    //startActivityForResult(i, 200);
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);

                }*/ else if (options[item].equals("Cancel")) {

                    //profile_pic.setImageResource(R.drawable.ic_user_bg_white);
                    dialog.dismiss();

                }

            }

        });

        builder.show();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

//        Log.e("data","---->"+data.getExtras().get("data"));

        if (resultCode == DocumentDetailActivity.this.RESULT_OK) {

            if (requestCode == 1) {
                bitmap = (Bitmap) data.getExtras().get("data");
                if (BtnClickView == 1) {

                    img_cust_img.setVisibility(View.GONE);
                    img_cust_img2.setVisibility(View.VISIBLE);

                    img_cust_img2.setImageBitmap(bitmap);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 85, stream);
                    byte[] image = stream.toByteArray();

                    decodeString = Base64.encodeToString(image, Base64.DEFAULT);

                }
            }
        }
    }


    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) DocumentDetailActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
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
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }






}
