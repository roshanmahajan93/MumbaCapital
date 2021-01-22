package com.example.mumbacapital.Model;

public class CustomerMonthlyFinance
{
    public int CustFinId;
    public String FDate;
    public String FBasicAmount;
    public String FInterestAmount;
    public String FineAndPenalty;
    public int CustId;
    public int IsCompleted;
    public int isExtra;
    public int InterestValue;
    public String TotalsMonthlyValue;
    public int UserId;
    public String cal_date;
    public String SDate;

    public CustomerMonthlyFinance(int custFinId, String FDate, String FBasicAmount, String FInterestAmount, String fineAndPenalty, int custId, int isCompleted, int isExtra, int interestValue, String totalsMonthlyValue, int userId, String cal_Date, String SDate) {
        this.CustFinId = custFinId;
        this.FDate = FDate;
        this.FBasicAmount = FBasicAmount;
        this.FInterestAmount = FInterestAmount;
        this.FineAndPenalty = fineAndPenalty;
        this.CustId = custId;
        this.IsCompleted = isCompleted;
        this.isExtra = isExtra;
        this.InterestValue = interestValue;
        this.TotalsMonthlyValue = totalsMonthlyValue;
        this.UserId = userId;
        this.cal_date = cal_Date;
        this.SDate = SDate;
    }
}
