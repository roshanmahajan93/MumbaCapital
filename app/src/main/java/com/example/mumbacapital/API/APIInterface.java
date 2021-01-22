package com.example.mumbacapital.API;



import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by anupamchugh on 09/01/17.
 */

public interface APIInterface {

    @GET("/osrfinance_hundred_days/getCustomerDetails.php")
    Call<ResponseBody> getStringResponse();

    @POST("/osrfinance_hundred_days/get_customerfinancedetail.php")
    Call<ResponseBody> postStringResponse(@Body RequestBody body);

    @POST("/osrfinance_hundred_days/get_customermonthlyfinancedetail.php")
    Call<ResponseBody> get_customermonthlyfinancedetail(@Body RequestBody body);

    @POST("/osrfinance_hundred_days/get_login_data.php")
    Call<ResponseBody> getLoginData(@Body RequestBody body);

    @POST("/osrfinance_hundred_days/insert_and_update_customer_detail_test.php")
    Call<ResponseBody> insert_and_update_customer_detail(@Body RequestBody body);

    @GET("/osrfinance_hundred_days/getCardNo.php")
    Call<ResponseBody> getCardNo();

    @POST("/osrfinance_hundred_days/update_customer_finance_detail.php")
    Call<ResponseBody> update_customer_finance_detail(@Body RequestBody body);

    @POST("/osrfinance_hundred_days/update_customer_finance_detail_monthly_new.php")
    Call<ResponseBody> update_customer_finance_detail_monthly(@Body RequestBody body);

    @GET("/osrfinance_hundred_days/user_daily_entry_report.php")
    Call<ResponseBody> user_daily_entry_report();

    @POST("/osrfinance_hundred_days/create-dynamic-pdf-send-as-attachment-with-email-in-php-demo/delete_old_record.php")
    Call<ResponseBody> delete_old_record(@Body RequestBody body);

    @POST("/osrfinance_hundred_days/delete_last_customer_finance_detail_record_new.php")
    Call<ResponseBody> delete_last_customer_finance_detail_record(@Body RequestBody body);

    @GET("/osrfinance_hundred_days/delete_last_record_report.php")
    Call<ResponseBody> delete_last_record_report();

    @GET("/osrfinance_hundred_days/two_days_report.php")
    Call<ResponseBody> two_days_report();

    @GET("/osrfinance_hundred_days/fifty_days_report.php")
    Call<ResponseBody> fifty_days_report();

    @POST("/osrfinance_hundred_days/getdocumentdetail.php")
    Call<ResponseBody> getdocumentdetail(@Body RequestBody body);

    @POST("/osrfinance_hundred_days/insert_and_update_document_detail.php")
    Call<ResponseBody> insert_and_update_document_detail(@Body RequestBody body);





}
