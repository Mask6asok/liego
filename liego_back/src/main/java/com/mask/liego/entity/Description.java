package com.mask.liego.entity;

public class Description {
    private int descriptionID;
    private String descrip_text;
    private String descrip_img1;
    private String descrip_img2;
    private String descrip_img3;

    public Description() {

    }

    public Description(String descrip_text, String descrip_img1, String descrip_img2, String descrip_img3) {
        this.descrip_text = descrip_text;
        this.descrip_img1 = descrip_img1;
        this.descrip_img2 = descrip_img2;
        this.descrip_img3 = descrip_img3;
    }

    public int getDescriptionID() {
        return descriptionID;
    }

    public void setDescriptionID(int descriptionID) {
        this.descriptionID = descriptionID;
    }

    public String getDescrip_text() {
        return descrip_text;
    }

    public void setDescrip_text(String descrip_text) {
        this.descrip_text = descrip_text;
    }

    public String getDescrip_img1() {
        return descrip_img1;
    }

    public void setDescrip_img1(String descrip_img1) {
        this.descrip_img1 = descrip_img1;
    }

    public String getDescrip_img2() {
        return descrip_img2;
    }

    public void setDescrip_img2(String descrip_img2) {
        this.descrip_img2 = descrip_img2;
    }

    public String getDescrip_img3() {
        return descrip_img3;
    }

    public void setDescrip_img3(String descrip_img3) {
        this.descrip_img3 = descrip_img3;
    }
}

