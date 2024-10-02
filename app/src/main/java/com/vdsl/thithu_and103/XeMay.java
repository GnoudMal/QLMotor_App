package com.vdsl.thithu_and103;

public class XeMay {
    private String _id;
    private String ten_xe_PH46164;
    private String mau_sac_PH46164;
    private double gia_ban_PH46164;
    private String mo_ta_PH46164;
    private String hinh_anh_PH46164;

    public XeMay(String _id, String ten_xe_PH46164, String mau_sac_PH46164, double gia_ban_PH46164, String mo_ta_PH46164, String hinh_anh_PH46164) {
        this._id = _id;
        this.ten_xe_PH46164 = ten_xe_PH46164;
        this.mau_sac_PH46164 = mau_sac_PH46164;
        this.gia_ban_PH46164 = gia_ban_PH46164;
        this.mo_ta_PH46164 = mo_ta_PH46164;
        this.hinh_anh_PH46164 = hinh_anh_PH46164;
    }

    public XeMay() {
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getTen_xe_PH46164() {
        return ten_xe_PH46164;
    }

    public void setTen_xe_PH46164(String ten_xe_PH46164) {
        this.ten_xe_PH46164 = ten_xe_PH46164;
    }

    public String getMau_sac_PH46164() {
        return mau_sac_PH46164;
    }

    public void setMau_sac_PH46164(String mau_sac_PH46164) {
        this.mau_sac_PH46164 = mau_sac_PH46164;
    }

    public double getGia_ban_PH46164() {
        return gia_ban_PH46164;
    }

    public void setGia_ban_PH46164(double gia_ban_PH46164) {
        this.gia_ban_PH46164 = gia_ban_PH46164;
    }

    public String getMo_ta_PH46164() {
        return mo_ta_PH46164;
    }

    public void setMo_ta_PH46164(String mo_ta_PH46164) {
        this.mo_ta_PH46164 = mo_ta_PH46164;
    }

    public String getHinh_anh_PH46164() {
        return hinh_anh_PH46164;
    }

    public void setHinh_anh_PH46164(String hinh_anh_PH46164) {
        this.hinh_anh_PH46164 = hinh_anh_PH46164;
    }
}
