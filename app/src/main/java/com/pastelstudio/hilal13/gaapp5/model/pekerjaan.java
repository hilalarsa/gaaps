package com.pastelstudio.hilal13.gaapp5.model;

import com.google.firebase.database.IgnoreExtraProperties;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by hilal13 on 08/02/18.
 */

public class pekerjaan {
    @SerializedName("deskripsi")
    @Expose
    private String deskripsi;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("tujuan")
    @Expose
    private String tujuan;

    public pekerjaan(String deskripsi, String status, String tujuan) {
        this.deskripsi = deskripsi;
        this.status = status;
        this.tujuan = tujuan;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public String getStatus() {
        return status;
    }

    public String getTujuan() {
        return tujuan;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setTujuan(String tujuan) {
        this.tujuan = tujuan;
    }




}
