package com.example.mumbacapital.Model;

/**
 * Created by chetu on 1/6/2019.
 */
public class Document
{
    public int DId;
    public String DocumentName;
    public int CustId;
    public String ImagePath;

    public Document(int DId, String documentName, int custId, String imagePath) {
        this.DId = DId;
        this.DocumentName = documentName;
        this.CustId = custId;
        this.ImagePath = imagePath;
    }

    public int getDId() {
        return DId;
    }

    public void setDId(int DId) {
        this.DId = DId;
    }

    public String getDocumentName() {
        return DocumentName;
    }

    public void setDocumentName(String documentName) {
        DocumentName = documentName;
    }

    public int getCustId() {
        return CustId;
    }

    public void setCustId(int custId) {
        CustId = custId;
    }

    public String getImagePath() {
        return ImagePath;
    }

    public void setImagePath(String imagePath) {
        ImagePath = imagePath;
    }
}
