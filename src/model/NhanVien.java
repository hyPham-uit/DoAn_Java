/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author HP
 */
public class NhanVien {
    private int maNV;
    private String tenNV;
    private String ngSinh;
    private String diaChi;
    private long luong; 
    private String gioiTinh;
    private String soDT;
    private String chucDanh;
    private String maPhong;

    public NhanVien(int maNV, String tenNV, String ngSinh,String diaChi, String gioiTinh, String soDT, String chucDanh, String maPhong,long luong) {
        this.maNV = maNV;
        this.tenNV = tenNV;
        this.ngSinh = ngSinh;
        this.diaChi = diaChi;
        this.luong = luong;
        this.gioiTinh = gioiTinh;
        this.soDT = soDT;
        this.chucDanh = chucDanh;
        this.maPhong = maPhong;
    }

    public int getMaNV() {
        return maNV;
    }

    public void setMaNV(int maNV) {
        this.maNV = maNV;
    }

    public String getTenNV() {
        return tenNV;
    }

    public void setTenNV(String tenNV) {
        this.tenNV = tenNV;
    }

    public String getNgSinh() {
        return ngSinh;
    }

    public void setNgSinh(String ngSinh) {
        this.ngSinh = ngSinh;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public long getLuong() {
        return luong;
    }

    public void setLuong(long luong) {
        this.luong = luong;
    }

    public String getGioiTinh() {
        return gioiTinh;
    }

    public void setGioiTinh(String gioiTinh) {
        this.gioiTinh = gioiTinh;
    }

    public String getSoDT() {
        return soDT;
    }

    public void setSoDT(String soDT) {
        this.soDT = soDT;
    }

    public String getChucDanh() {
        return chucDanh;
    }

    public void setChucDanh(String chucDanh) {
        this.chucDanh = chucDanh;
    }

    public String getMaPhong() {
        return maPhong;
    }

    public void setMaPhong(String maPhong) {
        this.maPhong = maPhong;
    }
}
