package com.example.mumbacapital.Helper;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.mumbacapital.Model.CustomerDetail;
import com.example.mumbacapital.Model.CustomerFinance;
import com.example.mumbacapital.Model.User;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper

{
    public static final boolean DEBUG = true;
    public final static String DATABASE_PATH = "/data/data/com.example.mumbacapital/databases/";
    public static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "OSRFinance.db";

    //static List<Jobs> JobList = new ArrayList<Jobs>();

    // static List<MediaDetails> MediaDetailsList = new ArrayList<MediaDetails>();

    private static SQLiteDatabase myDataBase;
    private static DatabaseHelper DBHelper = null;
    private final Context mycontext;
    String test;
    private SharedPreferences prefs;
    private String prefName = "UserDetails";

    // public static final int DATABASE_VERSION_old = 1;

    public DatabaseHelper(Context context) throws IOException {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.mycontext = context;
        boolean dbexist = checkdatabase();
        if (dbexist) {
            Log.e("DatabaseHelper", "Database exists");
            opendatabase();
        } else {
            Log.e("DatabaseHelper", "Database doesn't exist");
            createdatabase();
        }
    }

    public static void init(Context context) {
        if (DBHelper == null) {
            if (DEBUG) {
                Log.i("DBAdapter", context.toString());
                try {
                    DBHelper = new DatabaseHelper(context);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static synchronized SQLiteDatabase open() throws SQLException {
        Log.e("open()", "open()--->");
        return DBHelper.getWritableDatabase();
    }


    public static String insertCustomerFinanceDetail(ArrayList<CustomerFinance> CList) {
        String message = "";
        try {

            ArrayList<CustomerFinance> CustomerList = new ArrayList<CustomerFinance>();

            CustomerList = CList;
            Log.e("UserList", "SizeDBHelper------>" + CustomerList.size());

            CustomerFinance customerDetail = null;
            final SQLiteDatabase db = open();

            if (CustomerList.size() > 0) {

                for (int i = 0; i < CustomerList.size(); i++) {
                    customerDetail = CustomerList.get(i);
                    Log.e("customerDetail", "CustName----->" + customerDetail.FDate);

                    /*tempCardNo = isCardNoExist(customerDetail.CardNo);
                    if(tempCardNo == 0)
                    {*/
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("FDate", customerDetail.FDate);
                    contentValues.put("FAmount", customerDetail.FAmount);
                    contentValues.put("FineAndPenalty", customerDetail.FineAndPenalty);
                    contentValues.put("FRemark", customerDetail.FRemark);
                    contentValues.put("CustId", customerDetail.CustId);
                    db.insert("CustomerFinanceDetail", null, contentValues);
                    Log.e("inserting_into_sqlite", "CustomerFinanceDetail");
                    message = "Succesfully added";

                    //}
                    /*else
                    {

                        UpdateCustomerDetailRow(customerDetail.CardNo,customerDetail.CustName,customerDetail.ContactNo,customerDetail.HomeAddress,customerDetail.BusiAddress,customerDetail.Amount,customerDetail.DailyAmt,customerDetail.AmtDate,customerDetail.GuarantorOrIntroducerName,customerDetail.GContactNo,customerDetail.PhotoPath,customerDetail.UserId);
                        message = "Succesfully updated";
                    }*/


                }
            }
            db.close();
        } catch (Throwable t) {
            Log.i("Database", "Exception caught: " + t.getMessage(), t);
        }

        return message;
    }

    public static ArrayList<User> getUserLoginDetail(String LoginId, String password) {

        final SQLiteDatabase db = open();
        ArrayList<User> ln = new ArrayList<User>();

        String countQuery = "select * from user where username = '" + LoginId + "' and userpassword ='" + password + "'";
        Cursor cursor = db.rawQuery(countQuery, null);
        int temp = 0;
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ln.add(new User(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3)));

                Log.e("getUserLoginDetail", "getUserLoginDetail--->");
            } while (cursor.moveToNext());
        } else {
            ln.add(new User(0, "", "", ""));
        }
        cursor.close();

        return ln;
    }

    public static int getCardNo() {

        int cardNo = 0;
        final SQLiteDatabase db = open();
        ArrayList<User> ln = new ArrayList<User>();

        String countQuery = "select Max(cardno) as cardno from Customer";
        Cursor cursor = db.rawQuery(countQuery, null);
        int temp = 0;
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                //ln.add(new User(cursor.getInt(1), cursor.getString(2), cursor.getString(3), cursor.getString(4)));
                cardNo = cursor.getInt(0);
                if (cardNo == 0) {
                    cardNo = 1;
                } else {
                    cardNo = cardNo + 1;
                }
                Log.e("getCardNo", "getCardNo--->" + cardNo);

            } while (cursor.moveToNext());
        } else {
            cardNo = 0;
        }
        cursor.close();

        return cardNo;
    }

    public static int isCardNoExist(int CCardNo) {

        int cardNo = 0;
        final SQLiteDatabase db = open();
        ArrayList<User> ln = new ArrayList<User>();

        String countQuery = "select CardNo from Customer where CardNo = " + CCardNo + "";
        Cursor cursor = db.rawQuery(countQuery, null);
        int temp = 0;
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                //ln.add(new User(cursor.getInt(1), cursor.getString(2), cursor.getString(3), cursor.getString(4)));
                cardNo = cursor.getInt(0);

                Log.e("getCardNo", "getCardNo--->" + cardNo);

            } while (cursor.moveToNext());
        } else {
            cardNo = 0;
        }
        cursor.close();

        return cardNo;
    }

    public static ArrayList<CustomerFinance> getCustomerFinanceDetails(int CustId) {
        int tempCustId = 0;

        final SQLiteDatabase db = open();
        ArrayList<CustomerFinance> ln = new ArrayList<CustomerFinance>();

        String countQuery = "select * from CustomerFinanceDetail where CustId =" + CustId + "";
        Cursor cursor = db.rawQuery(countQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                //ln.add(new Jobs(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5)));
                ln.add(new CustomerFinance(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getInt(5)));
                Log.e("getCustomerFinance", "Customer");
                Log.e("cursor4.getInt(0)", "" + cursor.getInt(0));
                Log.e("cursor4.getString(1)", "" + cursor.getString(1));
                Log.e("cursor4.getString(2)", "" + cursor.getString(2));
                Log.e("cursor4.getString(3)", "" + cursor.getString(3));
                Log.e("cursor4.getString(4)", "" + cursor.getString(4));
                Log.e("cursor4.getInt(5)", "" + cursor.getInt(5));

            } while (cursor.moveToNext());
        } else {
            Log.e("getCustomerFinance", "getCustomerFinance-->");
        }
        cursor.close();

        return ln;
    }

    //---------------------------------Update data Start here----------------------------------
    public static void UpdateJobRow(String SerialNo, String job_name, String ClientName, String ClientEmail, String expiry_date, String job_location, int user_id, String Usr_name, String status, String Remark, String creation_date, String job_type) {
        final SQLiteDatabase db = open();
        db.execSQL("UPDATE Usr_Jobs SET job_name='" + job_name + "',ClientName='" + ClientName + "',ClientEmail='" + ClientEmail + "',expiry_date='" + expiry_date + "',job_location='" + job_location + "',user_id=" + user_id + ",Usr_name='" + Usr_name + "',status='" + status + "',Remark='" + Remark + "',creation_date='" + creation_date + "',job_type='" + job_type + "' WHERE serial_no='" + SerialNo + "'");

        db.close();
    }

    public static void UpdateCustomerDetailRow(int cardNo, String custName, String contactNo, String homeAddress, String busiAddress, String amount, String dailyAmt, String amtDate, String guarantorOrIntroducerName, String GContactNo, String photoPath, int UserId) {
        final SQLiteDatabase db = open();
        db.execSQL("UPDATE Customer SET CustName ='" + custName + "',ContactNo='" + contactNo + "',HomeAddress='" + homeAddress + "',BusiAddress='" + busiAddress + "',Amount='" + amount + "',DailyAmt='" + dailyAmt + "',AmtDate='" + amtDate + "',GuarantorOrIntroducerName='" + guarantorOrIntroducerName + "',GContactNo='" + GContactNo + "',PhotoPath='" + photoPath + "',UserId = " + UserId + " where CardNo=" + cardNo + "");

        db.close();
    }
    //---------------------------------Delete data Start here----------------------------------

    public static void deleteUserData() {

        final SQLiteDatabase db = open();
        // Cursor cursor = db.rawQuery(countQuery, null);
        db.execSQL("delete from Users");

        db.close();

    }

    public void createdatabase() throws IOException {
        //  boolean dbexist = checkdatabase();
        // if(dbexist) {
        //      System.out.println(" Database exists.");
        //  } else {
        Log.e("createdatabase", "createdatabase");

        this.getReadableDatabase();
        try {
            copydatabase();
            opendatabase();
        } catch (IOException e) {
            throw new Error("Error copying database");
        }
        // }
    }

    private boolean checkdatabase() {

        boolean checkdb = false;
        try {
            String myPath = DATABASE_PATH + DATABASE_NAME;
            File dbfile = new File(myPath);
            checkdb = dbfile.exists();
        } catch (SQLiteException e) {
            Log.e("checkdatabase", "Database doesn't exist");
        }
        return checkdb;
    }

    private void copydatabase() throws IOException {
        //Open your local db as the input stream
        Log.e("Inside", "copydatabase()----->");
        InputStream myinput = mycontext.getAssets().open(DATABASE_NAME);

        // Path to the just created empty db
        String outfilename = DATABASE_PATH + DATABASE_NAME;

        //Open the empty db as the output stream
        OutputStream myoutput = new FileOutputStream(outfilename);

        // transfer byte to inputfile to outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myinput.read(buffer)) > 0) {
            myoutput.write(buffer, 0, length);
        }

        //Close the streams
        myoutput.flush();
        myoutput.close();
        myinput.close();
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            db.execSQL("PRAGMA automatic_index = off;");
        }
    }

    public void opendatabase() throws SQLException {
        //Open the database
        Log.e("opendatabase()", "opendatabase()--->");
        String mypath = DATABASE_PATH + DATABASE_NAME;
        myDataBase = SQLiteDatabase.openDatabase(mypath, null, SQLiteDatabase.OPEN_READWRITE);
    }

    public synchronized void close() {
        if (myDataBase != null) {
            Log.e("close()", "close()--->");
            myDataBase.close();
        }
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


}
