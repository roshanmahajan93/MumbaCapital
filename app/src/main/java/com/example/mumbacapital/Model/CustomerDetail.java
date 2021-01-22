package com.example.mumbacapital.Model;

/**
 * Created by chetu on 6/5/2018.
 */
public class CustomerDetail {
    public int CustId;
    public int CardNo;
    public String CustName;
    public String ContactNo;
    public String HomeAddress;
    public String BusiAddress;
    public String Amount;
    public String DailyAmt;
    public String AmtDate;
    public String GuarantorOrIntroducerName;
    public String GContactNo;
    public String PhotoPath;
    public String UserName;

    public String RateOfInterest;
    public String InterestAmountPerMonth;
    public String LoanCompletionDate;
    public String totalBalance;

    public CustomerDetail(int custId, int cardNo, String custName, String contactNo, String homeAddress, String busiAddress, String amount, String dailyAmt, String amtDate, String guarantorOrIntroducerName, String GContactNo, String photoPath, String userName, String rateOfInterest, String interestAmountPerMonth, String loanCompletionDate, String totalBalance) {
        this.CustId = custId;
        this.CardNo = cardNo;
        this.CustName = custName;
        this.ContactNo = contactNo;
        this.HomeAddress = homeAddress;
        this.BusiAddress = busiAddress;
        this.Amount = amount;
        this.DailyAmt = dailyAmt;
        this.AmtDate = amtDate;
        this.GuarantorOrIntroducerName = guarantorOrIntroducerName;
        this.GContactNo = GContactNo;
        this.PhotoPath = photoPath;
        this.UserName = userName;
        this.RateOfInterest = rateOfInterest;
        this.InterestAmountPerMonth = interestAmountPerMonth;
        this.LoanCompletionDate = loanCompletionDate;
        this.totalBalance = totalBalance;
    }
}
