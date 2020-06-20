package model;

public class BenhNhan {
    private int MaBN;
    private String HoTen;
    private String NgaySinh;
    private String GioiTinh;
    private String DiaChi;
    private String SDT;

    public BenhNhan() {
    }

    public BenhNhan(int MaBN, String HoTen, String NgaySinh, String GioiTinh, String DiaChi, String SDT) {
        this.MaBN = MaBN;
        this.HoTen = HoTen;
        this.NgaySinh = NgaySinh;
        this.GioiTinh = GioiTinh;
        this.DiaChi = DiaChi;
        this.SDT = SDT;
    }

    
    public int getMaBN() {
        return MaBN;
    }

    public void setMaBN(int MaBN) {
        this.MaBN = MaBN;
    }

    public String getHoTen() {
        return HoTen;
    }

    public void setHoTen(String HoTen) {
        this.HoTen = HoTen;
    }

    public String getNgaySinh() {
        return NgaySinh;
    }

    public void setNgaySinh(String NgaySinh) {
        this.NgaySinh = NgaySinh;
    }

    public String getGioiTinh() {
        return GioiTinh;
    }

    public void setGioiTinh(String GioiTinh) {
        this.GioiTinh = GioiTinh;
    }

    public String getDiaChi() {
        return DiaChi;
    }

    public void setDiaChi(String DiaChi) {
        this.DiaChi = DiaChi;
    }

    public String getSDT() {
        return SDT;
    }

    public void setSDT(String SDT) {
        this.SDT = SDT;
    }
    
    
}
