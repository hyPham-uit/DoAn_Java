/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author Admin
 */
public class Thuoc {
    private int maThuoc;
    private String loaiThuoc;
    private String donVi;
    private int donGia;
    private String noiSX;

    public Thuoc() {
    }

    public Thuoc(int maThuoc, String loaiThuoc, String donVi, int donGia, String noiSX) {
        this.maThuoc = maThuoc;
        this.loaiThuoc = loaiThuoc;
        this.donVi = donVi;
        this.donGia = donGia;
        this.noiSX = noiSX;
    }

    public int getMaThuoc() {
        return maThuoc;
    }

    public void setMaThuoc(int maThuoc) {
        this.maThuoc = maThuoc;
    }

    public String getLoaiThuoc() {
        return loaiThuoc;
    }

    public void setLoaiThuoc(String loaiThuoc) {
        this.loaiThuoc = loaiThuoc;
    }

    public String getDonVi() {
        return donVi;
    }

    public void setDonVi(String donVi) {
        this.donVi = donVi;
    }

    public int getDonGia() {
        return donGia;
    }

    public void setDonGia(int donGia) {
        this.donGia = donGia;
    }

    public String getNoiSX() {
        return noiSX;
    }

    public void setNoiSX(String noiSX) {
        this.noiSX = noiSX;
    }

    
}
