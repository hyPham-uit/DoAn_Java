/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;


import com.placeholder.PlaceHolder;
import java.awt.Color;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.RowFilter;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import model.BenhNhan;
import model.NhanVien;
import model.PhieuKham;
import model.Thuoc;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;


/**
 *
 * @author MyPC
 */
public class BaseFrame extends javax.swing.JFrame {
    int location=-1;
    private PlaceHolder p1, p2, p3;
    public int getlocation(){
        return location;
    }
    
    public void resetLocation(){
        location=-1;
    }
//--------------------------------------------------------------------------Nhân viên--------------------------------------------
    ArrayList<NhanVien> listNV= nhanVienList();
    private int maNhanVien=0;
    public int getMaNhanVien(){
        return maNhanVien;
    }
    //nhập các nhân viên từ database vào list
    public ArrayList<NhanVien> nhanVienList(){
        ArrayList<NhanVien> nhanViensList=new ArrayList<>();
        String url="jdbc:oracle:thin:@localhost:1521:orcl";
        try{
            Connection conn=DriverManager.getConnection(url,"DOAN_ORACLE","admin");
            String query1="SELECT * FROM NHANVIEN";
            Statement stm=conn.createStatement();
            ResultSet rs=stm.executeQuery(query1);
            NhanVien nv;
            while(rs.next()){
                Date ngaySinh=rs.getDate("NGAYSINH");
                //Date date=(Date) new SimpleDateFormat("yyyy-mm-dd hh:mm:ss").parse(ngaySinh);
                DateFormat dateFormat = new SimpleDateFormat("MMM d, yyyy");  
                String strDate = dateFormat.format(ngaySinh);  
                nv=new NhanVien(rs.getInt("MANV"), rs.getString("HOTEN"),strDate,rs.getString("DIACHI"),rs.getString("GIOITINH"),
                    rs.getString("SDT"), rs.getString("CHUCDANH"), rs.getString("MAPHONG"), rs.getLong("LUONG"));
                nhanViensList.add(nv);
            }
            conn.close();
        }catch(SQLException e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Không connect được database");
        }
//        catch (ParseException ex) {
//            Logger.getLogger(BaseFrame.class.getName()).log(Level.SEVERE, null, ex);
//        }
        return nhanViensList;
        //conn.close();
    }
    
    private DefaultTableModel tblModelNhanVien;
    public ArrayList<NhanVien> getList(){
        return listNV;
    }
    
    //lấy nhân viên sau khi tìm kiếm
    public NhanVien getNhanVien(){
        return listNV.get(location);
    }
    
    public Thuoc getThuoc(){
        return listThuoc.get(location);
    }
    
    public void findNhanVien(int maNV){
        for(int i=0; i<listNV.size(); i++){
            if(listNV.get(i).getMaNV()==maNV){
                location=i;
                //JOptionPane.showMessageDialog(panelKhamBenh, i);
            }
        }
    }
    
    public BenhNhan getBenhNhan(){
        return listBN.get(location);
    }
    
    
    public void findBenhNhan(int maBN){
        for(int i=0; i<listBN.size(); i++){
            if(listBN.get(i).getMaBN() == maBN){
                location = i;
            }
        }
    }
    
    public void findThuoc(int mathuoc){
        for(int i=0; i<listThuoc.size(); i++){
            if(listThuoc.get(i).getMaThuoc() == mathuoc){
                location = i;
            }
        }
    }
    
    private Connection conn;
    public void connect(){
        try {
            String url="jdbc:oracle:thin:@localhost:1521:orcl";
            conn=DriverManager.getConnection(url,"DOAN_ORACLE","admin");
        } catch (SQLException ex) {
            Logger.getLogger(BaseFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
        
    
    //tìm kiếm vị trí nhân viên
    public int searchNhanVien(ArrayList<NhanVien> arr, long target) {
        for(int i=0; i<arr.size(); i++){
            int x=arr.get(i).getMaNV();
            if (x==target) {
                return i;
            }
        }
        return -1;
    }
    
    
    
    //sau khi nhập database và list, ta fetch data vào table để hiển thị ra màn hình
    public void show_nhanVien(){
        tblModelNhanVien=(DefaultTableModel)tblNhanVien.getModel();
        //tblNhanVien.setModel(tblModelNhanVien);
        int j=1;
        for(int i=0; i<listNV.size();i++){
            Object[] row={j, listNV.get(i).getMaNV(),listNV.get(i).getTenNV(),listNV.get(i).getNgSinh(),listNV.get(i).getDiaChi(),
                listNV.get(i).getGioiTinh(),listNV.get(i).getSoDT(),listNV.get(i).getChucDanh(),listNV.get(i).getMaPhong(),listNV.get(i).getLuong()};
            tblModelNhanVien.addRow(row);
            j++;
        }
        setVisible(true);
    }
    
    //sau khi thêm thông tin nhân viên ở insertNhanVienDialog, ta insert dữ liệu đó vào list và database, sau đó add nhân viên vào table
    public int addNhanVien(){
        try {
            DefaultTableModel tblModelNhanVien=(DefaultTableModel)tblNhanVien.getModel();
            int i=listNV.size()-1;
            int STT=listNV.size();
            int MaNV=listNV.get(i).getMaNV();
            String TenNV=listNV.get(i).getTenNV();
            //String NgSinh=listNV.get(i).getNgSinh();
            String NgSinh=listNV.get(i).getNgSinh();
            
            String date=listNV.get(i).getNgSinh();
            
            java.util.Date date2 = new SimpleDateFormat("MMMM d, yyyy").parse(date);
            java.sql.Date sqlDate = new java.sql.Date(date2.getTime());
            
            String DiaChi=listNV.get(i).getDiaChi();
            String GioiTinh=listNV.get(i).getGioiTinh();
            String SoDT=listNV.get(i).getSoDT();
            String ChucDanh=listNV.get(i).getChucDanh();
            int MaPhong=Integer.parseInt(listNV.get(i).getMaPhong());
            long Luong=listNV.get(i).getLuong();
            //thêm vô csdl
            connect();
            String insert="insert into NHANVIEN values(?,?,?,?,?,?,?,?,?,?,?,?)";
            PreparedStatement pre=conn.prepareStatement(insert);
            int phanQuyen=1;
            if(ChucDanh.equalsIgnoreCase("nhan vien"))
                phanQuyen=1;
            else if (ChucDanh.equalsIgnoreCase("quan ly"))
                phanQuyen=2;
            else{
                JOptionPane.showMessageDialog(panelKhamBenh, "Nhập chức danh là nhan vien hoặc quan ly");
                return 0;
            }
            
            //Nhập dữ liệu vô csdl
            pre.setInt(1, MaNV);
            pre.setString(2, TenNV);
            pre.setDate(3, sqlDate);
            pre.setString(4, GioiTinh);
            pre.setString(5, DiaChi);
            pre.setString(6, SoDT);
            pre.setString(7, "");
            pre.setString(8, "");
            pre.setInt(9, phanQuyen);
            pre.setString(10, ChucDanh);
            pre.setInt(11, MaPhong);
            pre.setLong(12, Luong);
            int x=pre.executeUpdate();
            JOptionPane.showMessageDialog(panelKhamBenh, x+" dòng đã được thêm vào csdl");
            
            Object[] objs={STT, MaNV, TenNV, NgSinh, DiaChi, GioiTinh ,SoDT, ChucDanh, MaPhong,Luong};
            tblModelNhanVien.addRow(objs);
            conn.close();
            return 1;
        }catch (SQLException ex) {
            Logger.getLogger(BaseFrame.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(panelKhamBenh, "Lỗi kết nối csdl");
            return 0;
        }catch (ParseException ex){
            //Logger.getLogger(BaseFrame.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, "Lỗi convert dữ liệu Ngày sinh");
            return 0;
        }
    }
    
    public int deleteNhanVien(int manv){
        try {
            tblModelNhanVien.removeRow(location);
            listNV.remove(location);
            location=-1;
//            String url="jdbc:oracle:thin:@localhost:1521:orcl";
//            Connection conn=DriverManager.getConnection(url,"DOAN_ORACLE","admin");
            connect();
            String delete="delete from NHANVIEN where MANV="+manv;
            Statement stm=conn.createStatement();
            int x=stm.executeUpdate(delete);
            txtTimMaNV.setText(""); 
            conn.close();
            return x;
        } catch (SQLException ex) {
            Logger.getLogger(BaseFrame.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, "Lỗi kết nối csdl");
            return 0;
        }  
    }
    
    public int deleteBenhNhan(int mabn){
        try {
            tblModelBN.removeRow(location);
            listBN.remove(location);
            location=-1;
//            String url="jdbc:oracle:thin:@localhost:1521:orcl";
//            Connection conn=DriverManager.getConnection(url,"DOAN_ORACLE","admin");
            connect();
            String delete="delete from BENHNHAN where MABN="+mabn;
            Statement stm=conn.createStatement();
            int x=stm.executeUpdate(delete);
            conn.close();
            return x;
        } catch (SQLException ex) {
            Logger.getLogger(BaseFrame.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, "Lỗi kết nối csdl");
            return 0;
        }  
    }
    
    public int deleteThuoc(int maThuoc){
        try {
            tblModelThuoc.removeRow(location);
            listThuoc.remove(location);
            location=-1;
//            String url="jdbc:oracle:thin:@localhost:1521:orcl";
//            Connection conn=DriverManager.getConnection(url,"DOAN_ORACLE","admin");
            connect();
            String delete="delete from THUOC where MATHUOC="+maThuoc;
            Statement stm=conn.createStatement();
            int x=stm.executeUpdate(delete);
            conn.close();
            return x;
        } catch (SQLException ex) {
            Logger.getLogger(BaseFrame.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, "Lỗi kết nối csdl");
            return 0;
        }  
    }
    
    public int deleteNhanVienMouseClick(int manv){
        try {
            tblModelNhanVien.removeRow(location);
            listNV.remove(location);
            location=-1;
            connect();
            String delete="delete from NHANVIEN where MANV="+manv;
            Statement stm=conn.createStatement();
            int x=stm.executeUpdate(delete);
            conn.close();
            return x;
        } catch (SQLException ex) {
            Logger.getLogger(BaseFrame.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(panelKhamBenh, "Lỗi kết nối csdl");
            return 0;   
        }  
    }
    
    public void updateNhanVienJtable(int loca){
        tblModelNhanVien.setValueAt(listNV.get(loca).getTenNV(), loca, 2);
        tblModelNhanVien.setValueAt(listNV.get(loca).getNgSinh(), loca, 3);
        tblModelNhanVien.setValueAt(listNV.get(loca).getDiaChi(), loca, 4);
        tblModelNhanVien.setValueAt(listNV.get(loca).getGioiTinh(), loca, 5);
        tblModelNhanVien.setValueAt(listNV.get(loca).getSoDT(), loca, 6);
        tblModelNhanVien.setValueAt(listNV.get(loca).getChucDanh(), loca, 7);
        tblModelNhanVien.setValueAt(listNV.get(loca).getMaPhong(), loca, 8);
        tblModelNhanVien.setValueAt(listNV.get(loca).getLuong(), loca, 9);
    }
    
    public int updateNhanVien(){
        try {
            //Gán thông tin cho biến
            int i=location;
            //int STT=listNV.size();
            int MaNV=listNV.get(i).getMaNV();
            String TenNV=listNV.get(i).getTenNV();
            //String NgSinh=listNV.get(i).getNgSinh();
            //String NgSinh=listNV.get(i).getNgSinh();
            
            String date=listNV.get(i).getNgSinh();
            java.util.Date date2 = new SimpleDateFormat("MMMM d, yyyy").parse(date);
            java.sql.Date sqlDate = new java.sql.Date(date2.getTime());
            
            String DiaChi=listNV.get(i).getDiaChi();
            String GioiTinh=listNV.get(i).getGioiTinh();
            
            String SoDT=listNV.get(i).getSoDT();
            String ChucDanh=listNV.get(i).getChucDanh();
            int MaPhong=Integer.parseInt(listNV.get(i).getMaPhong());
            long Luong=listNV.get(i).getLuong();
            connect();
            //update dtb
            String update="update NHANVIEN set HOTEN=?, NGAYSINH=?, GIOITINH=?, DIACHI=?,"
                    + "SDT=?, PHANQUYEN=?, CHUCDANH=?, MAPHONG=?,"
                    + "LUONG=? WHERE MANV="+MaNV;
            PreparedStatement pre=conn.prepareStatement(update);
            int phanQuyen=1;
            if(ChucDanh.equalsIgnoreCase("nhan vien"))
                phanQuyen=1;
            else if (ChucDanh.equalsIgnoreCase("quan ly"))
                phanQuyen=2;
            else{
                JOptionPane.showMessageDialog(panelKhamBenh, "Nhập chức danh là nhan vien hoặc quan ly");
                return 0;
            }
            
            //Nhập dữ liệu vô csdl
            
            pre.setString(1, TenNV);
            pre.setDate(2, sqlDate);
            pre.setString(3, GioiTinh);
            pre.setString(4, DiaChi);
            pre.setString(5, SoDT);
            pre.setInt(6, phanQuyen);
            pre.setString(7, ChucDanh);
            pre.setInt(8, MaPhong);
            pre.setLong(9, Luong);
            //pre.setInt(12, MaNV);
            int x=pre.executeUpdate();
            JOptionPane.showMessageDialog(panelKhamBenh, x+" dòng đã được cập nhật");
            conn.close();
            //update Jtable
            updateNhanVienJtable(location);
            location=-1;
            return 1;
        } catch (SQLException ex) {
            Logger.getLogger(BaseFrame.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(panelKhamBenh, "Lỗi kết nối csdl");
            return 0;
        } catch (ParseException ex){
            //Logger.getLogger(BaseFrame.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(panelKhamBenh, "Lỗi convert dữ liệu Ngày sinh");
            return 0;
        }
    }
    
    public int getMaHD(){
        int x=Integer.parseInt(txtTimMaHD.getText());
        return x;
    }
    
    
 //-------------------------------------------------------------------------Bệnh nhân--------------------------------------   
    private DefaultTableModel tblModelBN;
    public int deleteBenhNhanMouseClick(int mabn){
        try {
            tblModelBN=(DefaultTableModel)tblBenhNhan.getModel();
            tblModelBN.removeRow(location);
            tblModel.removeRow(location);
            listBN.remove(location);
            location=-1;
            connect();
            String delete="delete from BENHNHAN where MABN="+mabn;
            Statement stm=conn.createStatement();
            int x=stm.executeUpdate(delete);
            
            conn.close();
            return x;
        } catch (SQLException ex) {
            Logger.getLogger(BaseFrame.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(panelKhamBenh, "Lỗi kết nối csdl");
            return 0;
        }  //catch (IndexOutOfBoundsException e){
//            JOptionPane.showMessageDialog(panelKhamBenh, "Lỗi out of bound index");
//            return 0;
//        }
    }
       
    public void updateBenhNhanJtable(int loca){
        tblModelBN.setValueAt(listBN.get(loca).getHoTen(), loca, 1);
        tblModelBN.setValueAt(listBN.get(loca).getNgaySinh(), loca, 2);
        tblModelBN.setValueAt(listBN.get(loca).getGioiTinh(), loca, 3);
        tblModelBN.setValueAt(listBN.get(loca).getDiaChi(), loca, 4);
        tblModelBN.setValueAt(listBN.get(loca).getSDT(), loca, 5);
    }
    
    public void updateBnTiepNhanJtable(int loca){
        tblModel.setValueAt(listBN.get(loca).getHoTen(), loca, 2);
        tblModel.setValueAt(listBN.get(loca).getNgaySinh(), loca, 3);
        tblModel.setValueAt(listBN.get(loca).getDiaChi(), loca, 4);
        tblModel.setValueAt(listBN.get(loca).getGioiTinh(), loca, 5);
        tblModel.setValueAt(listBN.get(loca).getSDT(), loca, 6);
    }
    
    public void show_BenhNhan(){
        tblModelBN =(DefaultTableModel)tblBenhNhan.getModel();
        int j = 1;
        for(int i=0; i<listBN.size(); i++){
            Object[] row={listBN.get(i).getMaBN(), listBN.get(i).getHoTen(), listBN.get(i).getNgaySinh(), 
                         listBN.get(i).getGioiTinh(), listBN.get(i).getDiaChi(), listBN.get(i).getSDT()};
            tblModelBN.addRow(row);
        }
    }
    
    public int updateBenhNhan(){
        try {
            //Gán thông tin cho biến
            int i=location;
            int MaBN=listBN.get(i).getMaBN();
            String TenBN=listBN.get(i).getHoTen();
            
            String date=listBN.get(i).getNgaySinh();
            java.util.Date date2 = new SimpleDateFormat("MMMM d, yyyy").parse(date);
            java.sql.Date sqlDate = new java.sql.Date(date2.getTime());
            
            String DiaChi=listBN.get(i).getDiaChi();
            String GioiTinh=listBN.get(i).getGioiTinh();
            
            String SoDT=listBN.get(i).getSDT();
            connect();
            //update dtb
            String update="update BENHNHAN set HOTEN=?, NGAYSINH=?, GIOITINH=?, DIACHI=?,"
                    + "SDT=? WHERE MABN="+MaBN;
            PreparedStatement pre=conn.prepareStatement(update);
            
            //Nhập dữ liệu vô csdl
            
            pre.setString(1, TenBN);
            pre.setDate(2, sqlDate);
            pre.setString(3, GioiTinh);
            pre.setString(4, DiaChi);
            pre.setString(5, SoDT);
            int x=pre.executeUpdate();
            JOptionPane.showMessageDialog(this, x+" dòng đã được cập nhật");
            conn.close();
            //update Jtable
            updateBenhNhanJtable(location);
            updateBnTiepNhanJtable(location);
            location=-1;
            return 1;
        } catch (SQLException ex) {
            Logger.getLogger(BaseFrame.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(panelKhamBenh, "Lỗi kết nối csdl");
            return 0;
        } catch (ParseException ex){
            //Logger.getLogger(BaseFrame.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(panelKhamBenh, "Lỗi convert dữ liệu Ngày sinh");
            return 0;
        }
    }
    
//--------------------------------------------------------------------------Tiếp nhận-----------------------------------------
    //private Connection conn;
    private int maBN;
    private DefaultTableModel tblModel;
    ArrayList<BenhNhan> listBN = getBenhNhanList(); 
        
    public ArrayList<BenhNhan> getBenhNhanList(){
        ArrayList<BenhNhan> BenhNhansList = new ArrayList<>();
        try{
            connect();
            String query2 = "SELECT * FROM BENHNHAN";
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(query2);
            BenhNhan bn;
            while(rs.next()){          
                Date ngaySinh = rs.getDate("NGAYSINH");
                DateFormat dateFormat = new SimpleDateFormat("MMM d, yyyy");  
                String NgaySinh = dateFormat.format(ngaySinh); 
                bn = new BenhNhan(rs.getInt("MABN"),rs.getString("HOTEN"),NgaySinh,rs.getString("GIOITINH"),rs.getString("DIACHI"),rs.getString("SDT"));
                BenhNhansList.add(bn);
            }
            conn.close();
        }catch(SQLException e){
            e.printStackTrace();
        }
        return BenhNhansList;
    }
   
    
    public void addBenhNhan(BenhNhan s){
        listBN.add(s);
        tblModel = (DefaultTableModel)tblBN.getModel();
        tblModel.setRowCount(0);
        tblModelBN.setRowCount(0);
        int i = 1;
        for(BenhNhan bn: listBN){
            tblModel.addRow(new Object[]{i,bn.getMaBN(),bn.getHoTen(),bn.getNgaySinh(),bn.getGioiTinh(),bn.getDiaChi(),bn.getSDT()});
            tblModelBN.addRow(new Object[]{i,bn.getMaBN(),bn.getHoTen(),bn.getNgaySinh(),bn.getGioiTinh(),bn.getDiaChi(),bn.getSDT()});
            i++;
        }
    }
    
    public void setMaBN(){
        try {
            connect();
            int count=0;
            String query="SELECT MABN FROM BENHNHAN ORDER BY MABN DESC FETCH FIRST 1 ROW ONLY";
            Statement stm=conn.createStatement();
            ResultSet rs=stm.executeQuery(query);
            while(rs.next()){
                count=rs.getInt(1)+1;
            }
            String x=String.valueOf(count);
            txtMaBN.setText(x);
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(BaseFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public int getMaBN(){
        return maBN;
    }
   
    public void show_BN(){
        //ArrayList<BenhNhan> list = getBenhNhanList();
        tblModel =(DefaultTableModel)tblBN.getModel();
        int j = 1;
        for(int i=0; i<listBN.size(); i++){
            Object[] row={j, listBN.get(i).getMaBN(), listBN.get(i).getHoTen(), listBN.get(i).getNgaySinh(), 
                         listBN.get(i).getGioiTinh(), listBN.get(i).getDiaChi(), listBN.get(i).getSDT()};
            tblModel.addRow(row);
            j++;
        }
        setMaBN();
        txtMaBN.setEditable(false);
        updateCBBchuanDoan();
        updateCBBbacSi();
    }
    
    public void updateCBBchuanDoan(){
        try{
            connect();
            String query2 = "SELECT LOAIBENH FROM LOAIBENH";
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(query2);
            while(rs.next()){          
                cbbChuanDoan.addItem(rs.getString(1));
            }
            conn.close();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
    
    public void updateCBBbacSi(){
        try{
            connect();
            String query2 = "SELECT * FROM NHANVIEN";
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(query2);
            while(rs.next()){          
                cbbBacSi.addItem(rs.getString(2));
            }
            conn.close();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
    //----------------------------------------------------------------------THUỐC---------------------------------------------------
    ArrayList<Thuoc> listThuoc = getThuocList();
    public ArrayList<Thuoc> getThuocList(){
        ArrayList<Thuoc> ThuocsList = new ArrayList<>();
        try{
            connect();
            String query2 = "SELECT * FROM THUOC";
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(query2);
            Thuoc thuoc;
            while(rs.next()){
                thuoc = new Thuoc(rs.getInt(1), rs.getString(2), rs.getString(3),rs.getInt(4) , rs.getString(5));
                ThuocsList.add(thuoc);
            }
            conn.close();
        }catch(SQLException e){
            e.printStackTrace();
        }
        return ThuocsList;
    }
    private int maHoaDon=0;
    public int getMaHoaDon(){
        return maHoaDon;
    }
    
    private int idThuocAuto;
    
    private DefaultTableModel tblModelThuoc;
    public void show_Thuoc(){
        tblModelThuoc =(DefaultTableModel)tblThuoc.getModel();
        for(int i=0; i<listThuoc.size(); i++){
            Object[] row={listThuoc.get(i).getMaThuoc(), listThuoc.get(i).getLoaiThuoc(), listThuoc.get(i).getDonVi(), 
                         listThuoc.get(i).getDonGia(), listThuoc.get(i).getNoiSX()};
            tblModelThuoc.addRow(row);
        }
        
        int id=1;
        boolean flag;
        while(true){
            flag=false;
            for(Thuoc t:listThuoc){
                if(id==t.getMaThuoc()){
                    flag=true;
                    break;
                }
            }
            if(!flag) break;
            ++id;
        }
        idThuocAuto=id;
    }
    
    public int addThuoc(){
        try {
            int i=listThuoc.size()-1;
            int STT=listThuoc.size();
            int MaThuoc=listThuoc.get(i).getMaThuoc();
            String TenThuoc=listThuoc.get(i).getLoaiThuoc();
            String DonVi=listThuoc.get(i).getDonVi();
            int DonGia=listThuoc.get(i).getDonGia();
            String NoiSX=listThuoc.get(i).getNoiSX();
            Object[] objs={STT, MaThuoc, TenThuoc, DonVi, DonGia,NoiSX};
            
            //thêm vô csdl
            connect();
            String insert="insert into THUOC values(?,?,?,?,?)";
            PreparedStatement pre=conn.prepareStatement(insert);
           
            //Nhập dữ liệu vô csdl
            pre.setInt(1, MaThuoc);
            pre.setString(2, TenThuoc);
            pre.setString(3, DonVi);
            pre.setInt(4, DonGia);
            pre.setString(5, NoiSX);
            int x=pre.executeUpdate();
            JOptionPane.showMessageDialog(this, x+" dòng đã được thêm vào csdl");
            
            tblModelThuoc.addRow(objs);
            conn.close();
            return 1;
        }catch (SQLException ex) {
            Logger.getLogger(BaseFrame.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, "Lỗi kết nối csdl");
            return 0;
        }
    }
    
    public int returnIdThuoc(){
        return idThuocAuto;
    }
    
    public void updateThuocJtable(int loca){
        tblModelThuoc.setValueAt(listThuoc.get(loca).getLoaiThuoc(), loca, 1);
        tblModelThuoc.setValueAt(listThuoc.get(loca).getDonVi(), loca, 2);
        tblModelThuoc.setValueAt(listThuoc.get(loca).getDonGia(), loca, 3);
        tblModelThuoc.setValueAt(listThuoc.get(loca).getNoiSX(), loca, 4);
    }
    
    public int updateThuoc(){
        try {
            //Gán thông tin cho biến
            int i=location;
            //int STT=listNV.size();
            int MaThuoc=listThuoc.get(i).getMaThuoc();
            String TenThuoc=listThuoc.get(i).getLoaiThuoc();
            String DonVi=listThuoc.get(i).getDonVi();
            int DonGia=listThuoc.get(i).getDonGia();
            String NoiSX=listThuoc.get(i).getNoiSX();
            connect();
            //update dtb
            String update="update THUOC set LOAITHUOC=?, DONVI=?, DONGIA=?, NOISX=?  WHERE MATHUOC="+MaThuoc;
            PreparedStatement pre=conn.prepareStatement(update);
            //Nhập dữ liệu vô csdl
            
            pre.setString(1, TenThuoc);
            pre.setString(2, DonVi);
            pre.setInt(3, DonGia);
            pre.setString(4, NoiSX);
            int x=pre.executeUpdate();
            JOptionPane.showMessageDialog(this, x+" dòng đã được cập nhật");
            conn.close();
            //update Jtable
            updateThuocJtable(location);
            location=-1;
            return 1;
        } catch (SQLException ex) {
            Logger.getLogger(BaseFrame.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, "Lỗi kết nối csdl");
            return 0;
        } 
    }
//--------------------------------------------------------------------------Khám bệnh-------------------------------------
    private DefaultTableModel tblModelToaThuoc;
    
    public void updateCBBloaiThuoc(){
        try{
            connect();
            String query2 = "SELECT LOAITHUOC FROM THUOC";
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(query2);
            while(rs.next()){          
                cbbLoaiThuoc.addItem(rs.getString(1));
            }
            conn.close();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
    
    public void updateCBBTienSu(){
        for(int i=cbbTienSu.getItemCount()-1;i>=0;i--){
        cbbTienSu.removeItemAt(i);
}
        try{
            connect();
            String query2 = "SELECT DISTINCT CHUANDOAN FROM HOADON WHERE MABN="+txtMaBNKham.getText();
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(query2);
            while(rs.next()){          
                cbbTienSu.addItem(rs.getString(1));
            }
            conn.close();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
    
    public void inputThuocToToa(){
        try{
        tblModelToaThuoc=(DefaultTableModel)tblToaThuoc.getModel();
        int maHD=Integer.parseInt(txtMaHD.getText());
        int maThuoc=1;
        String loaiThuoc=cbbLoaiThuoc.getSelectedItem().toString();
        int soLuong=(Integer)this.soLuong.getValue();
        String cachDung=txtCachDung.getText();
        if(cachDung.equals("")==true){
            JOptionPane.showMessageDialog(this, "Vui lòng nhập cách dùng");
        }
        else{
            try{
                connect();
                String getMaThuoc="select MATHUOC FROM THUOC WHERE LOAITHUOC='"+loaiThuoc+"'";
                Statement stm=conn.createStatement();
                ResultSet rs=stm.executeQuery(getMaThuoc);
                while(rs.next()){
                    maThuoc=rs.getInt(1);
                }
                
                String insert="insert into TOATHUOC values(?,?,?,?,?,?)";
                PreparedStatement pre=conn.prepareStatement(insert);
                
                //Nhập dữ liệu vô csdl
                pre.setInt(1, maHD);
                pre.setInt(2, maThuoc);
                pre.setInt(3, soLuong);
                pre.setInt(4, 0);
                pre.setString(5, cachDung);
                pre.setInt(6, 0);
                int x=pre.executeUpdate();

                Object[] objs={loaiThuoc, soLuong, cachDung};
                tblModelToaThuoc.addRow(objs);
                conn.close();
            }catch (SQLException ex) {
                Logger.getLogger(BaseFrame.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(panelKhamBenh, "Lỗi kết nối csdl");
            }
        }
        } catch(Exception e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(panelKhamBenh, "Vui lòng tìm phiếu khám trước khi thêm hóa đơn");
        }
    }
    
  
    
    public void show_KhamBenh(){
        txtMaBNKham.setEditable(false);
        txtHoTen.setEditable(false);
        txtLiDoKham.setEditable(false);
        updateCBBloaiThuoc();
        txtDonVi.setEditable(false);
        txtMaHD.setEditable(false);
    }
    
//------------------------------------------------------------------------Thống kê-----------------------------------------------------------------------------------
    public void updateTblHoaDon(){
        try {
            DefaultTableModel tblModelHoaDon=(DefaultTableModel)tblHoaDon.getModel();
            connect();
            String query="select MAHD, MABN, MANV, THANHTIEN FROM HOADON";
            Statement stm=conn.createStatement();
            ResultSet rs=stm.executeQuery(query);
            while(rs.next()){
                Object[] obj={rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getInt(4)};
                tblModelHoaDon.addRow(obj);
            }
        } catch (SQLException ex) {
            Logger.getLogger(BaseFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void updateTblDoanhSoThuoc(){
        try {
            DefaultTableModel tblModelDoanhSoThuoc=(DefaultTableModel)tblDoanhSoThuoc.getModel();
            connect();
            String query="select MATHUOC, DOANHSO FROM THUOC";
            Statement stm=conn.createStatement();
            ResultSet rs=stm.executeQuery(query);
            while(rs.next()){
                Object[] obj={rs.getInt(1), rs.getInt(2)};
                tblModelDoanhSoThuoc.addRow(obj);
            }
        } catch (SQLException ex) {
            Logger.getLogger(BaseFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public long tinhTongDoanhThu(){
        long money=0;
        for(int i=0;i<tblHoaDon.getRowCount();i++){
            money=Long.parseLong(tblHoaDon.getValueAt(i, 3).toString())+money;
        } 
        return money;
    }
    
    public String rutGonDoanhThu(long money){
        ArrayList<String> arr=new ArrayList<>();
        while(money>999){
            long temp=money%1000;
            money/=1000;
            if(temp==0){
                arr.add(String.valueOf(temp)+"00");
            }else if(temp<=9){
                arr.add("00"+String.valueOf(temp));
            }else if(temp<=99){
                arr.add("0"+String.valueOf(temp));
            }else 
                arr.add(String.valueOf(temp));
        }
        arr.add(String.valueOf(money));
        String tien=" ";
        for(int j=arr.size()-1; j>0; j--){
            tien=tien+arr.get(j)+".";
        }
        tien=tien+arr.get(0);
        return tien;
    }
    
    public void showThongKe(){
        updateTblHoaDon();
        updateTblDoanhSoThuoc();
        long money=tinhTongDoanhThu();
        System.out.println(money);
        txtTongDoanhThu.setText(rutGonDoanhThu(money)+" VND");
        txtTongDoanhThu.setEditable(false);
    }
    
//--------------------------------------------------------------------hàm khởi tạo mặc định------------------------------
    //-------------------set color khi click button
    public void setColor(JPanel panel){
        panel.setBackground(new Color(240, 240, 240));
    }
    public void resetColor(JPanel panel){
        panel.setBackground(new Color(204, 204, 255));
    }
    //lấy hình từ folder src/images để chèn hình vào frame
    public void editImageFrame(){
        ImageIcon myimage = new ImageIcon("src/images/da1.png");
        Image img1 = myimage.getImage();
        Image img2=myimage.getImage().getScaledInstance(jLabel1.getWidth(), jLabel1.getHeight(), Image.SCALE_SMOOTH);
        jLabel1.setIcon(new ImageIcon(img2));
        
        
    }
    //hàm khởi tạo mặc định
    public BaseFrame() {
        initComponents();
              
        Border jpanel_title_border = BorderFactory.createMatteBorder(2, 2, 2, 2, Color.black);
        jPanel_title.setBorder(jpanel_title_border);
        //chèn hình
        editImageFrame();
        setTitle("Quản lí phòng mạch tư");
        show_nhanVien();
        show_BenhNhan();
        show_BN();
        show_Thuoc();
        show_KhamBenh();
        showThongKe();
        setVisible(true);
        setColor(pnBttNhanVien);
    }
   
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        PopUpMenu = new javax.swing.JPopupMenu();
        deleteNV = new javax.swing.JMenuItem();
        updateNV = new javax.swing.JMenuItem();
        PopUpMenu1 = new javax.swing.JPopupMenu();
        deleteBN = new javax.swing.JMenuItem();
        updateBN = new javax.swing.JMenuItem();
        jPanel2 = new javax.swing.JPanel();
        jPanel_title = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        pnBttBenhNhan = new javax.swing.JPanel();
        jButtonBenhNhan1 = new javax.swing.JButton();
        pnBttKhamBenh = new javax.swing.JPanel();
        jButtonKhamBenh = new javax.swing.JButton();
        pnBttThuoc = new javax.swing.JPanel();
        jButtonThuoc = new javax.swing.JButton();
        pnBttNhanVien = new javax.swing.JPanel();
        jButtonNhanvien = new javax.swing.JButton();
        pnBttThongKe = new javax.swing.JPanel();
        jButtonThongke = new javax.swing.JButton();
        pnBttTiepNhan = new javax.swing.JPanel();
        jButtonTiepNhan = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jLayeredPane1 = new javax.swing.JLayeredPane();
        panelNhanVien = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblNhanVien = new javax.swing.JTable();
        btThemNV = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        txtTimMaNV = new javax.swing.JTextField();
        btTimNV = new javax.swing.JButton();
        panelThongKe = new javax.swing.JPanel();
        jLabel34 = new javax.swing.JLabel();
        txtTongDoanhThu = new javax.swing.JTextField();
        jLabel46 = new javax.swing.JLabel();
        jLabel43 = new javax.swing.JLabel();
        jScrollPane8 = new javax.swing.JScrollPane();
        tblHoaDon = new javax.swing.JTable();
        jScrollPane9 = new javax.swing.JScrollPane();
        tblDoanhSoThuoc = new javax.swing.JTable();
        jLabel12 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        ttTimHoaDon = new javax.swing.JTextField();
        jLabel44 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel45 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        panelBenhNhan = new javax.swing.JPanel();
        jLabel32 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tblBenhNhan = new javax.swing.JTable();
        jLabel9 = new javax.swing.JLabel();
        txtTimBenhNhan = new javax.swing.JTextField();
        btTimMaBN = new javax.swing.JButton();
        panelKhamBenh = new javax.swing.JPanel();
        btToaThuoc = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        txtHoTen = new javax.swing.JTextField();
        txtLiDoKham = new javax.swing.JTextField();
        txtTrieuChung = new javax.swing.JTextField();
        cbbChuanDoan = new javax.swing.JComboBox<>();
        cbbBacSi = new javax.swing.JComboBox<>();
        jLabel28 = new javax.swing.JLabel();
        txtMaBNKham = new javax.swing.JTextField();
        btThemBenhAn = new javax.swing.JButton();
        jLabel35 = new javax.swing.JLabel();
        cbbLoaiThuoc = new javax.swing.JComboBox<>();
        jLabel39 = new javax.swing.JLabel();
        soLuong = new javax.swing.JSpinner();
        jLabel40 = new javax.swing.JLabel();
        txtCachDung = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblToaThuoc = new javax.swing.JTable();
        jLabel41 = new javax.swing.JLabel();
        btThemThuocToToa = new javax.swing.JButton();
        jLabel42 = new javax.swing.JLabel();
        txtTimMaPK = new javax.swing.JTextField();
        btTimPK = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        txtDonVi = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtMaHD = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        cbbTienSu = new javax.swing.JComboBox<>();
        panelTiepNhan = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblBN = new javax.swing.JTable();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        txtMaBN = new javax.swing.JTextField();
        txtTenBN = new javax.swing.JTextField();
        txtDiaChi = new javax.swing.JTextField();
        txtSDT = new javax.swing.JTextField();
        txtNgaySinh = new com.toedter.calendar.JDateChooser();
        jMale = new javax.swing.JRadioButton();
        jFemale = new javax.swing.JRadioButton();
        btThemBN = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        txtTimMaBN = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        panelThuoc = new javax.swing.JPanel();
        jLabel36 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        txtTimMaThuoc = new javax.swing.JTextField();
        jLabel38 = new javax.swing.JLabel();
        btThemThuoc = new javax.swing.JButton();
        btTimMatoa = new javax.swing.JButton();
        txtTimMaHD = new javax.swing.JTextField();
        jScrollPane6 = new javax.swing.JScrollPane();
        tblThuoc = new javax.swing.JTable();
        jLabel10 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jLabel33 = new javax.swing.JLabel();

        deleteNV.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        deleteNV.setText("Xóa");
        deleteNV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteNVActionPerformed(evt);
            }
        });
        PopUpMenu.add(deleteNV);

        updateNV.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        updateNV.setText("Sửa ");
        updateNV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateNVActionPerformed(evt);
            }
        });
        PopUpMenu.add(updateNV);

        deleteBN.setText("Xóa");
        deleteBN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteBNActionPerformed(evt);
            }
        });
        PopUpMenu1.add(deleteBN);

        updateBN.setText("Sửa");
        updateBN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateBNActionPerformed(evt);
            }
        });
        PopUpMenu1.add(updateBN);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel_title.setBackground(new java.awt.Color(69, 123, 179));

        jLabel2.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("QUẢN LÍ PHÒNG MẠCH TƯ");

        jLabel1.setText("jLabel1");

        javax.swing.GroupLayout jPanel_titleLayout = new javax.swing.GroupLayout(jPanel_title);
        jPanel_title.setLayout(jPanel_titleLayout);
        jPanel_titleLayout.setHorizontalGroup(
            jPanel_titleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_titleLayout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(269, 269, 269)
                .addComponent(jLabel2)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel_titleLayout.setVerticalGroup(
            jPanel_titleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_titleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 65, Short.MAX_VALUE))
        );

        jPanel1.setBackground(new java.awt.Color(204, 204, 255));

        pnBttBenhNhan.setBackground(new java.awt.Color(204, 204, 255));

        jButtonBenhNhan1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButtonBenhNhan1.setIcon(new javax.swing.ImageIcon("D:\\Java\\JavaPhongMT\\src\\images\\patients-icon.png")); // NOI18N
        jButtonBenhNhan1.setText("BỆNH NHÂN");
        jButtonBenhNhan1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonBenhNhan1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnBttBenhNhanLayout = new javax.swing.GroupLayout(pnBttBenhNhan);
        pnBttBenhNhan.setLayout(pnBttBenhNhanLayout);
        pnBttBenhNhanLayout.setHorizontalGroup(
            pnBttBenhNhanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 169, Short.MAX_VALUE)
            .addGroup(pnBttBenhNhanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pnBttBenhNhanLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jButtonBenhNhan1)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        pnBttBenhNhanLayout.setVerticalGroup(
            pnBttBenhNhanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
            .addGroup(pnBttBenhNhanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pnBttBenhNhanLayout.createSequentialGroup()
                    .addGap(5, 5, 5)
                    .addComponent(jButtonBenhNhan1, javax.swing.GroupLayout.DEFAULT_SIZE, 42, Short.MAX_VALUE)
                    .addGap(6, 6, 6)))
        );

        pnBttKhamBenh.setBackground(new java.awt.Color(204, 204, 255));

        jButtonKhamBenh.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButtonKhamBenh.setIcon(new javax.swing.ImageIcon("D:\\Java\\JavaPhongMT\\src\\images\\doctor-icon.png")); // NOI18N
        jButtonKhamBenh.setText("KHÁM BỆNH");
        jButtonKhamBenh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonKhamBenhActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnBttKhamBenhLayout = new javax.swing.GroupLayout(pnBttKhamBenh);
        pnBttKhamBenh.setLayout(pnBttKhamBenhLayout);
        pnBttKhamBenhLayout.setHorizontalGroup(
            pnBttKhamBenhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnBttKhamBenhLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButtonKhamBenh)
                .addContainerGap())
        );
        pnBttKhamBenhLayout.setVerticalGroup(
            pnBttKhamBenhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnBttKhamBenhLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButtonKhamBenh, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pnBttThuoc.setBackground(new java.awt.Color(204, 204, 255));

        jButtonThuoc.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButtonThuoc.setIcon(new javax.swing.ImageIcon("D:\\Java\\JavaPhongMT\\src\\images\\drugs-icon.png")); // NOI18N
        jButtonThuoc.setText("THUỐC");
        jButtonThuoc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonThuocActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnBttThuocLayout = new javax.swing.GroupLayout(pnBttThuoc);
        pnBttThuoc.setLayout(pnBttThuocLayout);
        pnBttThuocLayout.setHorizontalGroup(
            pnBttThuocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnBttThuocLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButtonThuoc, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnBttThuocLayout.setVerticalGroup(
            pnBttThuocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnBttThuocLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButtonThuoc, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pnBttNhanVien.setBackground(new java.awt.Color(204, 204, 255));

        jButtonNhanvien.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButtonNhanvien.setIcon(new javax.swing.ImageIcon("D:\\Java\\JavaPhongMT\\src\\images\\employee-icon.png")); // NOI18N
        jButtonNhanvien.setText("NHÂN VIÊN");
        jButtonNhanvien.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonNhanvienActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnBttNhanVienLayout = new javax.swing.GroupLayout(pnBttNhanVien);
        pnBttNhanVien.setLayout(pnBttNhanVienLayout);
        pnBttNhanVienLayout.setHorizontalGroup(
            pnBttNhanVienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnBttNhanVienLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButtonNhanvien)
                .addContainerGap())
        );
        pnBttNhanVienLayout.setVerticalGroup(
            pnBttNhanVienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnBttNhanVienLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButtonNhanvien, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pnBttThongKe.setBackground(new java.awt.Color(204, 204, 255));

        jButtonThongke.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButtonThongke.setIcon(new javax.swing.ImageIcon("D:\\Java\\JavaPhongMT\\src\\images\\thongKeIcon.png")); // NOI18N
        jButtonThongke.setText("THỐNG KÊ");
        jButtonThongke.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonThongkeActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnBttThongKeLayout = new javax.swing.GroupLayout(pnBttThongKe);
        pnBttThongKe.setLayout(pnBttThongKeLayout);
        pnBttThongKeLayout.setHorizontalGroup(
            pnBttThongKeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnBttThongKeLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButtonThongke)
                .addContainerGap())
        );
        pnBttThongKeLayout.setVerticalGroup(
            pnBttThongKeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnBttThongKeLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButtonThongke, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pnBttTiepNhan.setBackground(new java.awt.Color(204, 204, 255));

        jButtonTiepNhan.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButtonTiepNhan.setIcon(new javax.swing.ImageIcon("D:\\Java\\JavaPhongMT\\src\\images\\register-icon.png")); // NOI18N
        jButtonTiepNhan.setText("TIẾP NHẬN");
        jButtonTiepNhan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonTiepNhanActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnBttTiepNhanLayout = new javax.swing.GroupLayout(pnBttTiepNhan);
        pnBttTiepNhan.setLayout(pnBttTiepNhanLayout);
        pnBttTiepNhanLayout.setHorizontalGroup(
            pnBttTiepNhanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnBttTiepNhanLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButtonTiepNhan)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnBttTiepNhanLayout.setVerticalGroup(
            pnBttTiepNhanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnBttTiepNhanLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButtonTiepNhan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(pnBttTiepNhan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(pnBttBenhNhan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(pnBttKhamBenh, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(pnBttThuoc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(pnBttNhanVien, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(pnBttThongKe, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnBttBenhNhan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnBttKhamBenh, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pnBttThuoc, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pnBttNhanVien, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pnBttThongKe, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(pnBttTiepNhan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jLayeredPane1.setLayout(new java.awt.CardLayout());

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 20)); // NOI18N
        jLabel8.setText("DANH SÁCH NHÂN VIÊN");

        tblNhanVien.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "STT", "Mã NV", "Họ và tên", "Ngày sinh", "Địa chỉ", "Giới tính", "SĐT", "Chức danh", "Mã phòng", "Lương"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblNhanVien.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tblNhanVienMouseReleased(evt);
            }
        });
        jScrollPane1.setViewportView(tblNhanVien);
        if (tblNhanVien.getColumnModel().getColumnCount() > 0) {
            tblNhanVien.getColumnModel().getColumn(0).setMinWidth(20);
            tblNhanVien.getColumnModel().getColumn(0).setPreferredWidth(20);
            tblNhanVien.getColumnModel().getColumn(1).setPreferredWidth(30);
            tblNhanVien.getColumnModel().getColumn(2).setPreferredWidth(135);
            tblNhanVien.getColumnModel().getColumn(2).setMaxWidth(140);
            tblNhanVien.getColumnModel().getColumn(3).setPreferredWidth(40);
            tblNhanVien.getColumnModel().getColumn(4).setPreferredWidth(160);
            tblNhanVien.getColumnModel().getColumn(4).setMaxWidth(200);
            tblNhanVien.getColumnModel().getColumn(5).setPreferredWidth(30);
            tblNhanVien.getColumnModel().getColumn(6).setPreferredWidth(40);
            tblNhanVien.getColumnModel().getColumn(7).setPreferredWidth(40);
            tblNhanVien.getColumnModel().getColumn(8).setPreferredWidth(30);
            tblNhanVien.getColumnModel().getColumn(9).setPreferredWidth(45);
        }

        btThemNV.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btThemNV.setText("Thêm nhân viên");
        btThemNV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btThemNVActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel6.setText("Tìm mã nhân viên");

        txtTimMaNV.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        btTimNV.setText("TÌM");
        btTimNV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btTimNVActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelNhanVienLayout = new javax.swing.GroupLayout(panelNhanVien);
        panelNhanVien.setLayout(panelNhanVienLayout);
        panelNhanVienLayout.setHorizontalGroup(
            panelNhanVienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelNhanVienLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelNhanVienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelNhanVienLayout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 947, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(panelNhanVienLayout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtTimMaNV, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btTimNV)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btThemNV)
                        .addGap(23, 23, 23))))
            .addGroup(panelNhanVienLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel8)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelNhanVienLayout.setVerticalGroup(
            panelNhanVienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelNhanVienLayout.createSequentialGroup()
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panelNhanVienLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btThemNV)
                    .addComponent(jLabel6)
                    .addComponent(txtTimMaNV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btTimNV))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 16, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jLayeredPane1.add(panelNhanVien, "card2");

        jLabel34.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel34.setText("TỔNG DOANH THU");

        txtTongDoanhThu.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        jLabel46.setFont(new java.awt.Font("Tahoma", 1, 22)); // NOI18N
        jLabel46.setText("THỐNG KÊ DOANH THU");

        jLabel43.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel43.setText("HÓA ĐƠN BỆNH ÁN");

        tblHoaDon.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mã hóa đơn", "Mã bệnh nhân", "Mã nhân viên", "Tổng tiền"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                true, true, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane8.setViewportView(tblHoaDon);
        if (tblHoaDon.getColumnModel().getColumnCount() > 0) {
            tblHoaDon.getColumnModel().getColumn(3).setMinWidth(120);
            tblHoaDon.getColumnModel().getColumn(3).setPreferredWidth(130);
            tblHoaDon.getColumnModel().getColumn(3).setMaxWidth(150);
        }

        tblDoanhSoThuoc.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mã thuốc", "Doanh số"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane9.setViewportView(tblDoanhSoThuoc);
        if (tblDoanhSoThuoc.getColumnModel().getColumnCount() > 0) {
            tblDoanhSoThuoc.getColumnModel().getColumn(1).setMinWidth(120);
            tblDoanhSoThuoc.getColumnModel().getColumn(1).setPreferredWidth(140);
            tblDoanhSoThuoc.getColumnModel().getColumn(1).setMaxWidth(200);
        }

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel12.setText("DOANH SỐ THUỐC");

        jLabel21.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel21.setText("TÌM KIẾM");

        jLabel27.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel27.setText("Mã hóa đơn");

        ttTimHoaDon.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        jLabel44.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel44.setText("Mã nhân viên");

        jTextField2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        jLabel45.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel45.setText("Mã bệnh nhân");

        jTextField3.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        jButton2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jButton2.setText("Tìm");

        jButton4.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jButton4.setText("Tìm");

        jButton5.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jButton5.setText("Tìm");

        javax.swing.GroupLayout panelThongKeLayout = new javax.swing.GroupLayout(panelThongKe);
        panelThongKe.setLayout(panelThongKeLayout);
        panelThongKeLayout.setHorizontalGroup(
            panelThongKeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelThongKeLayout.createSequentialGroup()
                .addGroup(panelThongKeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelThongKeLayout.createSequentialGroup()
                        .addGap(117, 117, 117)
                        .addComponent(jLabel43, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelThongKeLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 415, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(panelThongKeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelThongKeLayout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addComponent(jLabel12)
                        .addContainerGap())
                    .addGroup(panelThongKeLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(panelThongKeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelThongKeLayout.createSequentialGroup()
                                .addGroup(panelThongKeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(panelThongKeLayout.createSequentialGroup()
                                        .addComponent(jLabel44, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jTextField3))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelThongKeLayout.createSequentialGroup()
                                        .addComponent(jLabel45, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jTextField2))
                                    .addGroup(panelThongKeLayout.createSequentialGroup()
                                        .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(ttTimHoaDon)))
                                .addGap(18, 18, 18)
                                .addGroup(panelThongKeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jButton2)
                                    .addComponent(jButton4)
                                    .addComponent(jButton5))
                                .addGap(64, 64, 64))
                            .addGroup(panelThongKeLayout.createSequentialGroup()
                                .addGroup(panelThongKeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(panelThongKeLayout.createSequentialGroup()
                                        .addComponent(jLabel21)
                                        .addGap(0, 0, Short.MAX_VALUE))
                                    .addGroup(panelThongKeLayout.createSequentialGroup()
                                        .addComponent(jLabel34, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(txtTongDoanhThu, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap())))))
            .addGroup(panelThongKeLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel46)
                .addGap(287, 287, 287))
        );
        panelThongKeLayout.setVerticalGroup(
            panelThongKeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelThongKeLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel46, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31)
                .addGroup(panelThongKeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel43)
                    .addComponent(jLabel12))
                .addGap(18, 18, 18)
                .addGroup(panelThongKeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(panelThongKeLayout.createSequentialGroup()
                        .addComponent(jLabel21)
                        .addGap(18, 18, 18)
                        .addGroup(panelThongKeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel27)
                            .addComponent(ttTimHoaDon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton2))
                        .addGap(18, 18, 18)
                        .addGroup(panelThongKeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel44)
                            .addComponent(jButton4)
                            .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(panelThongKeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel45)
                            .addComponent(jButton5)
                            .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 47, Short.MAX_VALUE)
                        .addGroup(panelThongKeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel34)
                            .addComponent(txtTongDoanhThu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );

        jLayeredPane1.add(panelThongKe, "card8");

        jLabel32.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel32.setText("THÔNG TIN BỆNH NHÂN");

        tblBenhNhan.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mã BN", "Họ và tên", "Ngày sinh", "Giới tính", "Địa chỉ", "Số điện thoại"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblBenhNhan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tblBenhNhanMouseReleased(evt);
            }
        });
        jScrollPane4.setViewportView(tblBenhNhan);
        if (tblBenhNhan.getColumnModel().getColumnCount() > 0) {
            tblBenhNhan.getColumnModel().getColumn(0).setPreferredWidth(60);
            tblBenhNhan.getColumnModel().getColumn(0).setMaxWidth(70);
            tblBenhNhan.getColumnModel().getColumn(1).setPreferredWidth(150);
            tblBenhNhan.getColumnModel().getColumn(1).setMaxWidth(160);
            tblBenhNhan.getColumnModel().getColumn(2).setPreferredWidth(100);
            tblBenhNhan.getColumnModel().getColumn(2).setMaxWidth(120);
            tblBenhNhan.getColumnModel().getColumn(3).setPreferredWidth(60);
            tblBenhNhan.getColumnModel().getColumn(3).setMaxWidth(70);
            tblBenhNhan.getColumnModel().getColumn(4).setPreferredWidth(170);
            tblBenhNhan.getColumnModel().getColumn(4).setMaxWidth(200);
            tblBenhNhan.getColumnModel().getColumn(5).setPreferredWidth(130);
            tblBenhNhan.getColumnModel().getColumn(5).setMaxWidth(140);
        }

        jLabel9.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel9.setText("Tìm mã bệnh nhân");

        txtTimBenhNhan.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtTimBenhNhan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTimBenhNhanActionPerformed(evt);
            }
        });
        txtTimBenhNhan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtTimBenhNhanKeyReleased(evt);
            }
        });

        btTimMaBN.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btTimMaBN.setText("Tìm");
        btTimMaBN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btTimMaBNActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelBenhNhanLayout = new javax.swing.GroupLayout(panelBenhNhan);
        panelBenhNhan.setLayout(panelBenhNhanLayout);
        panelBenhNhanLayout.setHorizontalGroup(
            panelBenhNhanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBenhNhanLayout.createSequentialGroup()
                .addGap(86, 86, 86)
                .addGroup(panelBenhNhanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 787, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelBenhNhanLayout.createSequentialGroup()
                        .addComponent(jLabel32)
                        .addGap(273, 273, 273)
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtTimBenhNhan, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btTimMaBN, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(94, Short.MAX_VALUE))
        );
        panelBenhNhanLayout.setVerticalGroup(
            panelBenhNhanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBenhNhanLayout.createSequentialGroup()
                .addContainerGap(21, Short.MAX_VALUE)
                .addGroup(panelBenhNhanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel32)
                    .addComponent(jLabel9)
                    .addComponent(txtTimBenhNhan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btTimMaBN))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 258, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jLayeredPane1.add(panelBenhNhan, "card3");

        btToaThuoc.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        btToaThuoc.setIcon(new javax.swing.ImageIcon("D:\\Java\\JavaPhongMT\\src\\images\\drugsList-icon.png")); // NOI18N
        btToaThuoc.setText("THÊM TOA THUỐC");
        btToaThuoc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btToaThuocActionPerformed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel7.setText("THÔNG TIN BỆNH ÁN");

        jLabel22.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel22.setText("Họ tên");

        jLabel23.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel23.setText("Lí do khám");

        jLabel24.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel24.setText("Triệu chứng");

        jLabel25.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel25.setText("Chuẩn đoán");

        jLabel26.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel26.setText("Bác sĩ khám");

        txtHoTen.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        txtLiDoKham.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        txtTrieuChung.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        cbbChuanDoan.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        cbbChuanDoan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbbChuanDoanActionPerformed(evt);
            }
        });

        cbbBacSi.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        cbbBacSi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbbBacSiActionPerformed(evt);
            }
        });

        jLabel28.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel28.setText("Mã BN");

        txtMaBNKham.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        btThemBenhAn.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        btThemBenhAn.setIcon(new javax.swing.ImageIcon("D:\\Java\\JavaPhongMT\\src\\images\\save-icon.png")); // NOI18N
        btThemBenhAn.setText("THÊM BỆNH ÁN");
        btThemBenhAn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btThemBenhAnActionPerformed(evt);
            }
        });

        jLabel35.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel35.setText("Loại thuốc");

        cbbLoaiThuoc.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        cbbLoaiThuoc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbbLoaiThuocActionPerformed(evt);
            }
        });

        jLabel39.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel39.setText("Số lượng");

        jLabel40.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel40.setText("Cách dùng");

        txtCachDung.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        tblToaThuoc.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Loại thuốc", "Số lượng", "Cách dùng"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane3.setViewportView(tblToaThuoc);
        if (tblToaThuoc.getColumnModel().getColumnCount() > 0) {
            tblToaThuoc.getColumnModel().getColumn(0).setPreferredWidth(140);
            tblToaThuoc.getColumnModel().getColumn(0).setMaxWidth(150);
            tblToaThuoc.getColumnModel().getColumn(1).setPreferredWidth(60);
            tblToaThuoc.getColumnModel().getColumn(1).setMaxWidth(80);
        }

        jLabel41.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel41.setText("TOA THUỐC");

        btThemThuocToToa.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        btThemThuocToToa.setIcon(new javax.swing.ImageIcon("D:\\Java\\JavaPhongMT\\src\\images\\add-icon.png")); // NOI18N
        btThemThuocToToa.setText("THÊM THUỐC");
        btThemThuocToToa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btThemThuocToToaActionPerformed(evt);
            }
        });

        jLabel42.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel42.setText("Mã phiếu khám");

        txtTimMaPK.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        btTimPK.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btTimPK.setText("TÌM");
        btTimPK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btTimPKActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel3.setText("Đơn vị");

        txtDonVi.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel4.setText("Mã bệnh án");

        txtMaHD.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel5.setText("Tiền sử bệnh án");

        javax.swing.GroupLayout panelKhamBenhLayout = new javax.swing.GroupLayout(panelKhamBenh);
        panelKhamBenh.setLayout(panelKhamBenhLayout);
        panelKhamBenhLayout.setHorizontalGroup(
            panelKhamBenhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelKhamBenhLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel7)
                .addGap(194, 194, 194))
            .addGroup(panelKhamBenhLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelKhamBenhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelKhamBenhLayout.createSequentialGroup()
                        .addGroup(panelKhamBenhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(panelKhamBenhLayout.createSequentialGroup()
                                .addComponent(btToaThuoc)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btThemThuocToToa))
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                        .addGap(15, 15, 15))
                    .addGroup(panelKhamBenhLayout.createSequentialGroup()
                        .addGroup(panelKhamBenhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(panelKhamBenhLayout.createSequentialGroup()
                                .addComponent(jLabel40)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtCachDung))
                            .addGroup(panelKhamBenhLayout.createSequentialGroup()
                                .addGap(2, 2, 2)
                                .addComponent(jLabel35, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(panelKhamBenhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel41)
                                    .addComponent(cbbLoaiThuoc, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(60, 60, 60)))
                        .addGap(22, 22, 22)
                        .addGroup(panelKhamBenhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(panelKhamBenhLayout.createSequentialGroup()
                                .addComponent(jLabel39)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(soLuong, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(33, 33, 33))
                            .addGroup(panelKhamBenhLayout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtDonVi)
                                .addGap(15, 15, 15)))))
                .addGroup(panelKhamBenhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel26, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel23, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel24, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel22, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel25, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel28, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(panelKhamBenhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtTrieuChung, javax.swing.GroupLayout.DEFAULT_SIZE, 173, Short.MAX_VALUE)
                    .addComponent(txtHoTen, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbbChuanDoan, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cbbBacSi, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtMaBNKham, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtLiDoKham))
                .addGap(37, 37, 37)
                .addGroup(panelKhamBenhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelKhamBenhLayout.createSequentialGroup()
                        .addGroup(panelKhamBenhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(btThemBenhAn, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(panelKhamBenhLayout.createSequentialGroup()
                                .addComponent(jLabel42)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(txtTimMaPK, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btTimPK)))
                        .addGap(128, 128, 128))
                    .addGroup(panelKhamBenhLayout.createSequentialGroup()
                        .addGroup(panelKhamBenhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelKhamBenhLayout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addGap(27, 27, 27)
                                .addComponent(txtMaHD, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel5)
                            .addComponent(cbbTienSu, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(193, 193, 193))))
        );
        panelKhamBenhLayout.setVerticalGroup(
            panelKhamBenhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelKhamBenhLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(panelKhamBenhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jLabel41))
                .addGap(18, 18, 18)
                .addGroup(panelKhamBenhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelKhamBenhLayout.createSequentialGroup()
                        .addGroup(panelKhamBenhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelKhamBenhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel28)
                                .addComponent(txtMaBNKham, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panelKhamBenhLayout.createSequentialGroup()
                                .addGap(3, 3, 3)
                                .addGroup(panelKhamBenhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel4)
                                    .addComponent(txtMaHD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(13, 13, 13)
                        .addGroup(panelKhamBenhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel22)
                            .addComponent(txtHoTen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel42)
                            .addComponent(txtTimMaPK, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btTimPK))
                        .addGap(19, 19, 19))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelKhamBenhLayout.createSequentialGroup()
                        .addGroup(panelKhamBenhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cbbLoaiThuoc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel35)
                            .addComponent(jLabel3)
                            .addComponent(txtDonVi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(panelKhamBenhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtCachDung, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel40)
                            .addComponent(jLabel39)
                            .addComponent(soLuong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(8, 8, 8)))
                .addGroup(panelKhamBenhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(panelKhamBenhLayout.createSequentialGroup()
                        .addGroup(panelKhamBenhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtLiDoKham, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel23)
                            .addComponent(jLabel5))
                        .addGap(20, 20, 20)
                        .addGroup(panelKhamBenhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel24)
                            .addComponent(txtTrieuChung, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cbbTienSu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(12, 12, 12)
                        .addGroup(panelKhamBenhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel25)
                            .addComponent(cbbChuanDoan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(16, 16, 16)
                        .addGroup(panelKhamBenhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cbbBacSi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel26)))
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panelKhamBenhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btThemBenhAn)
                    .addComponent(btThemThuocToToa)
                    .addComponent(btToaThuoc, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(40, 40, 40))
        );

        jLayeredPane1.add(panelKhamBenh, "card7");

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel13.setText("TÌM KIẾM BỆNH NHÂN");

        tblBN.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "STT", "Mã bệnh nhân", "Tên bệnh nhân", "Ngày Sinh", "Giới tính", "Địa chỉ", "SĐT"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, true, true, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblBN.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tblBNMouseReleased(evt);
            }
        });
        jScrollPane2.setViewportView(tblBN);

        jLabel14.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel14.setText("THÔNG TIN BỆNH NHÂN ĐĂNG KÝ KHÁM");

        jLabel15.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel15.setText("Mã bệnh nhân");

        jLabel16.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel16.setText("Tên bệnh nhân");

        jLabel17.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel17.setText("Ngày Sinh");

        jLabel18.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel18.setText("Giới tính");

        jLabel19.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel19.setText("Địa chỉ");

        jLabel20.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel20.setText("SĐT");

        jMale.setText("MALE");

        jFemale.setText("FEMALE");

        btThemBN.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btThemBN.setText("Thêm bệnh nhân");
        btThemBN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btThemBNActionPerformed(evt);
            }
        });

        jLabel11.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel11.setText("Tìm mã bệnh nhân");

        txtTimMaBN.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        jButton1.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jButton1.setText("Tìm kiếm");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelTiepNhanLayout = new javax.swing.GroupLayout(panelTiepNhan);
        panelTiepNhan.setLayout(panelTiepNhanLayout);
        panelTiepNhanLayout.setHorizontalGroup(
            panelTiepNhanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTiepNhanLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelTiepNhanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelTiepNhanLayout.createSequentialGroup()
                        .addGroup(panelTiepNhanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 684, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(panelTiepNhanLayout.createSequentialGroup()
                                .addComponent(jLabel11)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtTimMaBN, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton1)))
                        .addGroup(panelTiepNhanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelTiepNhanLayout.createSequentialGroup()
                                .addGap(38, 38, 38)
                                .addGroup(panelTiepNhanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(panelTiepNhanLayout.createSequentialGroup()
                                        .addComponent(jLabel15)
                                        .addGap(18, 18, 18)
                                        .addComponent(txtMaBN))
                                    .addGroup(panelTiepNhanLayout.createSequentialGroup()
                                        .addGroup(panelTiepNhanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel16)
                                            .addComponent(jLabel17))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(panelTiepNhanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(txtSDT)
                                            .addComponent(txtDiaChi)
                                            .addComponent(txtNgaySinh, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(txtTenBN)))
                                    .addGroup(panelTiepNhanLayout.createSequentialGroup()
                                        .addGroup(panelTiepNhanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel20)
                                            .addGroup(panelTiepNhanLayout.createSequentialGroup()
                                                .addGroup(panelTiepNhanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(jLabel19)
                                                    .addComponent(jLabel18))
                                                .addGap(53, 53, 53)
                                                .addComponent(jMale)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(jFemale)))
                                        .addGap(0, 0, Short.MAX_VALUE))))
                            .addGroup(panelTiepNhanLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btThemBN))))
                    .addGroup(panelTiepNhanLayout.createSequentialGroup()
                        .addComponent(jLabel13)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel14)
                        .addGap(51, 51, 51)))
                .addGap(35, 35, 35))
        );
        panelTiepNhanLayout.setVerticalGroup(
            panelTiepNhanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTiepNhanLayout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(panelTiepNhanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(24, 24, 24)
                .addGroup(panelTiepNhanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(txtMaBN, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11)
                    .addComponent(txtTimMaBN, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1))
                .addGroup(panelTiepNhanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelTiepNhanLayout.createSequentialGroup()
                        .addGap(9, 9, 9)
                        .addGroup(panelTiepNhanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel16)
                            .addComponent(txtTenBN, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(panelTiepNhanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel17)
                            .addComponent(txtNgaySinh, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(panelTiepNhanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel18)
                            .addComponent(jMale)
                            .addComponent(jFemale))
                        .addGap(18, 18, 18)
                        .addGroup(panelTiepNhanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel19)
                            .addComponent(txtDiaChi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(panelTiepNhanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel20)
                            .addComponent(txtSDT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btThemBN))
                    .addGroup(panelTiepNhanLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(2, 2, 2))
        );

        jLayeredPane1.add(panelTiepNhan, "card6");

        jLabel36.setFont(new java.awt.Font("Tahoma", 1, 20)); // NOI18N
        jLabel36.setText("DANH SÁCH THUỐC");

        jLabel37.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel37.setText("Mã thuốc");

        txtTimMaThuoc.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtTimMaThuoc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTimMaThuocActionPerformed(evt);
            }
        });

        jLabel38.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel38.setText("Mã hóa đơn");

        btThemThuoc.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        btThemThuoc.setIcon(new javax.swing.ImageIcon("D:\\Java\\JavaPhongMT\\src\\images\\add-icon.png")); // NOI18N
        btThemThuoc.setText("Thêm thuốc mới");
        btThemThuoc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btThemThuocActionPerformed(evt);
            }
        });

        btTimMatoa.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btTimMatoa.setText("Tìm");
        btTimMatoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btTimMatoaActionPerformed(evt);
            }
        });

        txtTimMaHD.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtTimMaHD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTimMaHDActionPerformed(evt);
            }
        });

        tblThuoc.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mã thuốc", "Loại thuốc", "Đơn vị", "Dơn giá", "Nơi sản xuất"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane6.setViewportView(tblThuoc);
        if (tblThuoc.getColumnModel().getColumnCount() > 0) {
            tblThuoc.getColumnModel().getColumn(0).setPreferredWidth(80);
            tblThuoc.getColumnModel().getColumn(0).setMaxWidth(60);
            tblThuoc.getColumnModel().getColumn(2).setMinWidth(60);
            tblThuoc.getColumnModel().getColumn(2).setPreferredWidth(80);
            tblThuoc.getColumnModel().getColumn(2).setMaxWidth(120);
        }

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel10.setText("TÌM KIẾM THUỐC");

        jButton3.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jButton3.setText("Tìm");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jLabel33.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel33.setText("TÌM TOA THUỐC");

        javax.swing.GroupLayout panelThuocLayout = new javax.swing.GroupLayout(panelThuoc);
        panelThuoc.setLayout(panelThuocLayout);
        panelThuocLayout.setHorizontalGroup(
            panelThuocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelThuocLayout.createSequentialGroup()
                .addGap(106, 106, 106)
                .addGroup(panelThuocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelThuocLayout.createSequentialGroup()
                        .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 528, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addGroup(panelThuocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelThuocLayout.createSequentialGroup()
                                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(95, 95, 95))
                            .addGroup(panelThuocLayout.createSequentialGroup()
                                .addGroup(panelThuocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(panelThuocLayout.createSequentialGroup()
                                        .addComponent(jLabel38)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(panelThuocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addGroup(panelThuocLayout.createSequentialGroup()
                                                .addComponent(txtTimMaHD, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(btTimMatoa))
                                            .addGroup(panelThuocLayout.createSequentialGroup()
                                                .addComponent(jLabel33, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addGap(17, 17, 17))))
                                    .addGroup(panelThuocLayout.createSequentialGroup()
                                        .addComponent(jLabel37, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtTimMaThuoc, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jButton3))
                                    .addComponent(btThemThuoc))
                                .addGap(79, 79, 79))))
                    .addGroup(panelThuocLayout.createSequentialGroup()
                        .addGap(92, 92, 92)
                        .addComponent(jLabel36, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );
        panelThuocLayout.setVerticalGroup(
            panelThuocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelThuocLayout.createSequentialGroup()
                .addGroup(panelThuocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelThuocLayout.createSequentialGroup()
                        .addComponent(jLabel36, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelThuocLayout.createSequentialGroup()
                        .addGap(45, 45, 45)
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(panelThuocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel37, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtTimMaThuoc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 74, Short.MAX_VALUE)
                        .addComponent(jLabel33)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelThuocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel38, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelThuocLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(txtTimMaHD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(btTimMatoa, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(60, 60, 60)
                        .addComponent(btThemThuoc, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        jLayeredPane1.add(panelThuoc, "card4");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLayeredPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLayeredPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 332, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel_title, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel_title, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonThuocActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonThuocActionPerformed
        // TODO add your handling code here:
        panelBenhNhan.setVisible(false);
        panelNhanVien.setVisible(false);
        panelKhamBenh.setVisible(false);
        panelTiepNhan.setVisible(false);
        panelThuoc.setVisible(true);
        panelThongKe.setVisible(false);
        
        resetColor(pnBttBenhNhan);
        resetColor(pnBttNhanVien);
        resetColor(pnBttKhamBenh);
        resetColor(pnBttTiepNhan);
        setColor(pnBttThuoc);
        resetColor(pnBttThongKe);
    }//GEN-LAST:event_jButtonThuocActionPerformed

    private void btToaThuocActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btToaThuocActionPerformed
        // TODO add your handling code here:
        insertToaThuocDialog insetTT=new insertToaThuocDialog(this, true);
        insetTT.setVisible(true);
    }//GEN-LAST:event_btToaThuocActionPerformed

    private void jButtonNhanvienActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonNhanvienActionPerformed
        // TODO add your handling code here:
        panelBenhNhan.setVisible(false);
        panelNhanVien.setVisible(true);
        panelKhamBenh.setVisible(false);
        panelTiepNhan.setVisible(false);
        panelThuoc.setVisible(false);
        panelThongKe.setVisible(false);
        
        resetColor(pnBttBenhNhan);
        setColor(pnBttNhanVien);
        resetColor(pnBttKhamBenh);
        resetColor(pnBttTiepNhan);
        resetColor(pnBttThuoc);
        resetColor(pnBttThongKe);
    }//GEN-LAST:event_jButtonNhanvienActionPerformed

    private void jButtonTiepNhanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonTiepNhanActionPerformed
        // TODO add your handling code here:
        panelBenhNhan.setVisible(false);
        panelNhanVien.setVisible(false);
        panelKhamBenh.setVisible(false);
        panelTiepNhan.setVisible(true);
        panelThuoc.setVisible(false);
        panelThongKe.setVisible(false);
        
        resetColor(pnBttBenhNhan);
        resetColor(pnBttNhanVien);
        resetColor(pnBttKhamBenh);
        setColor(pnBttTiepNhan);
        resetColor(pnBttThuoc);
        resetColor(pnBttThongKe);
        
    }//GEN-LAST:event_jButtonTiepNhanActionPerformed

    private void jButtonKhamBenhActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonKhamBenhActionPerformed
        // TODO add your handling code here:
        panelBenhNhan.setVisible(false);
        panelNhanVien.setVisible(false);
        panelKhamBenh.setVisible(true);
        panelTiepNhan.setVisible(false);
        panelThuoc.setVisible(false);
        panelThongKe.setVisible(false);
        
        resetColor(pnBttBenhNhan);
        resetColor(pnBttNhanVien);
        setColor(pnBttKhamBenh);
        resetColor(pnBttTiepNhan);
        resetColor(pnBttThuoc);
        resetColor(pnBttThongKe);
    }//GEN-LAST:event_jButtonKhamBenhActionPerformed

    private void btThemNVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btThemNVActionPerformed
        // TODO add your handling code here:
        insertNhanVienJDialog insertNV= new insertNhanVienJDialog(this, rootPaneCheckingEnabled);
        insertNV.setVisible(true);
    }//GEN-LAST:event_btThemNVActionPerformed

    private void tblNhanVienMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblNhanVienMouseReleased
        // TODO add your handling code here:
        if(evt.getButton()==MouseEvent.BUTTON3){
            if(evt.isPopupTrigger()&&tblNhanVien.getSelectedRowCount()!=0){
                PopUpMenu.show(evt.getComponent(), evt.getX(), evt.getY());
            }
        }
    }//GEN-LAST:event_tblNhanVienMouseReleased

    private void deleteNVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteNVActionPerformed
        // TODO add your handling code here:
        int row=tblNhanVien.getSelectedRow();
        
        if(row!=-1){
            location=row;
            int manv=Integer.parseInt(tblNhanVien.getValueAt(row, 1).toString());
            String name=tblNhanVien.getValueAt(row, 2).toString();
            int response=JOptionPane.showConfirmDialog(rootPane, "Bạn có chắc muốn xóa "+name+" không ?", "Xác nhận", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if(response==JOptionPane.YES_OPTION){
                if(deleteNhanVienMouseClick(manv)==1){
                        JOptionPane.showMessageDialog(rootPane, "Nhân viên "+name+" đã bị xóa");
                }
                else{
                    JOptionPane.showMessageDialog(panelKhamBenh, "Xóa không thành công!");
                }
            }
        }
    }//GEN-LAST:event_deleteNVActionPerformed

    private void updateNVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateNVActionPerformed
        // TODO add your handling code here:
        int row=tblNhanVien.getSelectedRow();
        location=row;
        showNhanVienDialog show=new showNhanVienDialog(this, rootPaneCheckingEnabled);
        show.setVisible(rootPaneCheckingEnabled);
    }//GEN-LAST:event_updateNVActionPerformed

    private void btThemBNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btThemBNActionPerformed
        int maBN = 0;
        boolean flag = true;

        if(txtMaBN.getText().equals("")||txtTenBN.getText().equals("")||txtNgaySinh.getDate().equals("")|| txtDiaChi.getText().equals("")||txtSDT.getText().equals("")){
            JOptionPane.showMessageDialog(this, "Bạn hãy điền đầy đủ thông tin");
        }
        else{
            try{
                try{
                    maBN = Integer.parseInt(txtMaBN.getText());
                }catch(Exception e){
                    JOptionPane.showMessageDialog(rootPane, "Mã bệnh nhân phải là số và không chứa kí tự khác");
                    flag = false;
                }

                String tenBN = txtTenBN.getText();
                DateFormat dateFormat = new SimpleDateFormat("MMM d, yyyy");
                String ngaySinh = dateFormat.format(txtNgaySinh.getDate());
                java.util.Date date2 = new SimpleDateFormat("MMM d, yyyy").parse(ngaySinh);
                java.sql.Date sqlDate = new java.sql.Date(date2.getTime());

                String gioiTinh = "";
                if(jMale.isSelected()){
                    gioiTinh += "MALE";
                }
                if(jFemale.isSelected()){
                    gioiTinh += "FEMALE";
                }

                String diaChi = txtDiaChi.getText();
                String soDT = txtSDT.getText();

                if(flag == true){
                    BenhNhan bn = new BenhNhan(maBN,tenBN,ngaySinh,gioiTinh,diaChi,soDT);
                    addBenhNhan(bn);
                    JOptionPane.showMessageDialog(this, "Thêm thành công");

                    java.util.Date today = new java.util.Date();
                    txtMaBN.setText("");
                    txtTenBN.setText("");
                    txtNgaySinh.setDate(today);
                    txtDiaChi.setText("");
                    txtSDT.setText("");
                    
                    connect();
                    try {
                        String insert = "insert into BENHNHAN values(?,?,?,?,?,?)";
                        PreparedStatement st = conn.prepareStatement(insert);
                        st.setInt(1, maBN);
                        st.setString(2, tenBN);
                        st.setDate(3, sqlDate);
                        st.setString(4, gioiTinh);
                        st.setString(5, diaChi);
                        st.setString(6, soDT);
                        int a = st.executeUpdate();

                        setMaBN();
                        this.maBN=maBN;
                        InserpkJdialog show=new InserpkJdialog(this, true);
                        show.setVisible(true);
                    } catch (SQLException ex) {
                        Logger.getLogger(BaseFrame.class.getName()).log(Level.SEVERE, null, ex);
                        JOptionPane.showMessageDialog(this, "Lỗi csdl");
                    }
                }
                //String url = "jdbc:oracle:thin:@localhost:1521:orcl";
                
            }catch (ParseException ex){
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Lỗi convert dữ liệu Ngày sinh");
            }

        }

    }//GEN-LAST:event_btThemBNActionPerformed

    private void tblBenhNhanMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblBenhNhanMouseReleased
        // TODO add your handling code here:
        if(evt.getButton()==MouseEvent.BUTTON3){
            if(evt.isPopupTrigger()&&tblBenhNhan.getSelectedRowCount()!=0){
                PopUpMenu1.show(evt.getComponent(), evt.getX(), evt.getY());
            }
        }
    }//GEN-LAST:event_tblBenhNhanMouseReleased

    private void deleteBNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteBNActionPerformed
        int row=tblBenhNhan.getSelectedRow();
        
        if(row!=-1){
            location=row;
            int mabn=Integer.parseInt(tblBenhNhan.getValueAt(row, 0).toString());
            String name=tblBenhNhan.getValueAt(row, 1).toString();
            int response=JOptionPane.showConfirmDialog(rootPane, "Bạn có chắc muốn xóa "+name+" không ?", "Xác nhận", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if(response==JOptionPane.YES_OPTION){
                if(deleteBenhNhanMouseClick(mabn)==1){
                        JOptionPane.showMessageDialog(rootPane, "Bệnh nhân "+name+" đã bị xóa");
                }
                else{
                    JOptionPane.showMessageDialog(panelKhamBenh, "Xóa không thành công!");
                }
            }
        }
    }//GEN-LAST:event_deleteBNActionPerformed

    private void btThemThuocActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btThemThuocActionPerformed
        // TODO add your handling code here:
        insertThuocJDialog insertThuoc= new insertThuocJDialog(this, rootPaneCheckingEnabled);
        insertThuoc.setVisible(true);
    }//GEN-LAST:event_btThemThuocActionPerformed

    private void txtTimMaHDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTimMaHDActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTimMaHDActionPerformed

    private void cbbChuanDoanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbbChuanDoanActionPerformed
        // TODO add your handling code here:
        
    }//GEN-LAST:event_cbbChuanDoanActionPerformed

    private void cbbBacSiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbbBacSiActionPerformed
        // TODO add your handling code here:
        
    }//GEN-LAST:event_cbbBacSiActionPerformed

    private void btTimMatoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btTimMatoaActionPerformed
        try {
            // TODO add your handling code here:
            //String maHD= txtTimMaHD.getText();
            int mahd=Integer.parseInt(txtTimMaHD.getText());
            String query="select MAHD from TOATHUOC WHERE MAHD="+mahd;
            
            connect();
            Statement stt=conn.createStatement();
            ResultSet rs=stt.executeQuery(query);
            
            int temp=0;
            while(rs.next()){
                temp=rs.getInt(1);
            }
            
            if(mahd<1){
                JOptionPane.showMessageDialog(panelKhamBenh, "Không tồn tại mã hóa đơn");
            }
            else{
                //tạo biến maHoaDon để dialog truy vấn và xuất csdl ra màn hình dialog
                maHoaDon=Integer.parseInt(txtTimMaHD.getText());
                //chạy dialog xuất thông tin
                showToaThuocJDialog showTT=new showToaThuocJDialog(this, rootPaneCheckingEnabled);
                showTT.setVisible(true);
            }
        } catch (SQLException ex) {
            Logger.getLogger(BaseFrame.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }//GEN-LAST:event_btTimMatoaActionPerformed

    private void btThemBenhAnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btThemBenhAnActionPerformed
        try {                                             
            // TODO add your handling code here:
            String trieuChung=txtTrieuChung.getText();
            String chuanDoan=cbbChuanDoan.getSelectedItem().toString();
            String bacSiKham=cbbBacSi.getSelectedItem().toString();
            int mahd=Integer.parseInt(txtMaHD.getText());
            connect();
            int manv=0;
            String getMaNV="select MANV FROM NHANVIEN WHERE HOTEN='"+bacSiKham+"'";
            Statement st=conn.createStatement();
            ResultSet rs=st.executeQuery(getMaNV);
            while(rs.next()){
                manv=rs.getInt(1);
            }
            conn.close();
            
            connect();
            String query="UPDATE HOADON SET MANV=?, TRIEUCHUNG=?, CHUANDOAN=?  WHERE MAHD="+mahd;
            PreparedStatement pre=conn.prepareStatement(query);
            pre.setInt(1, manv);
            pre.setString(2, trieuChung);
            pre.setString(3, chuanDoan);
            
            int a=pre.executeUpdate();
            JOptionPane.showMessageDialog(panelKhamBenh, "Cập nhật thành công "+a+" dòng");
            conn.close();
//            
//            try {
//                JasperReport jasperReport = JasperCompileManager.compileReport("D:\\Java\\JavaPhongMT\\src\\report\\report1.jrxml");
//                JRDataSource datasource =new JREmptyDataSource();
//                
//                Map<String, Object> parameters=new HashMap<String, Object>();
//                //parameters.put("ngayKham", txtNgayKham.getDate().toString());
//                parameters.put("hoTen", txtHoTen.getText());
//                parameters.put("maBN", txtMaBNKham.getText());
//                parameters.put("gioiTinh", txtMaBNKham.getText());
//                parameters.put("diaChi", txtMaBNKham.getText());
//                parameters.put("chuanDoan", cbbChuanDoan.getSelectedItem().toString());
//                parameters.put("bacSi", cbbBacSi.getSelectedItem().toString());
//            } catch (Exception e) {
//                JOptionPane.showMessageDialog(panelKhamBenh, "Lỗi tạo hóa đơn");
//                e.printStackTrace();
//            }
//            
        } catch (SQLException ex) {
            Logger.getLogger(BaseFrame.class.getName()).log(Level.SEVERE, null,ex);
        }
        
    }//GEN-LAST:event_btThemBenhAnActionPerformed

    private void tblBNMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblBNMouseReleased
        // TODO add your handling code here:
//        if(evt.getButton()==MouseEvent.BUTTON3){
//            if(evt.isPopupTrigger()&&tblBN.getSelectedRowCount()!=0){
//                PopUpMenu.show(evt.getComponent(), evt.getX(), evt.getY());
//            }
//        }
    }//GEN-LAST:event_tblBNMouseReleased

    private void txtTimMaThuocActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTimMaThuocActionPerformed
        DefaultTableModel table= (DefaultTableModel)tblThuoc.getModel();
        String search= txtTimMaThuoc.getText().toUpperCase();
        TableRowSorter<DefaultTableModel> tr=new TableRowSorter<DefaultTableModel>(table);
        tblThuoc.setRowSorter(tr);
        tr.setRowFilter(RowFilter.regexFilter(search));
    }//GEN-LAST:event_txtTimMaThuocActionPerformed

    private void btThemThuocToToaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btThemThuocToToaActionPerformed
        // TODO add your handling code here:
        inputThuocToToa();
    }//GEN-LAST:event_btThemThuocToToaActionPerformed

    private void btTimPKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btTimPKActionPerformed
        try {
            // TODO add your handling code here:
            connect();
            String query="SELECT H.MABN, B.HOTEN, H.LIDOKHAM FROM HOADON H, BENHNHAN B WHERE H.MABN=B.MABN AND H.MAHD="+txtTimMaPK.getText();
            Statement stm=conn.createStatement();
            ResultSet rs=stm.executeQuery(query);
            while(rs.next()){
                String maBN=String.valueOf(rs.getInt(1));
                txtMaBNKham.setText(maBN);
                txtHoTen.setText(rs.getString(2));
                txtLiDoKham.setText(rs.getString(3));
            }
            txtMaHD.setText(txtTimMaPK.getText());
            updateCBBTienSu();
        } catch (SQLException ex) {
            Logger.getLogger(BaseFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }//GEN-LAST:event_btTimPKActionPerformed

    private void updateBNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateBNActionPerformed
int row=tblBenhNhan.getSelectedRow();
        location=row;
        showBenhNhanDialog show=new showBenhNhanDialog(this, true);
        show.setVisible(true);
    }//GEN-LAST:event_updateBNActionPerformed

    private void txtTimBenhNhanKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTimBenhNhanKeyReleased
        // TODO add your handling code here:
//        DefaultTableModel table= (DefaultTableModel)tblBenhNhan.getModel();
//        String search= txtTimBenhNhan.getText().toUpperCase();
//        TableRowSorter<DefaultTableModel> tr=new TableRowSorter<DefaultTableModel>(table);
//        tblBenhNhan.setRowSorter(tr);
//        tr.setRowFilter(RowFilter.regexFilter(search));
    }//GEN-LAST:event_txtTimBenhNhanKeyReleased

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
       // boolean flag = true;
        try {
                int maThuoc = Integer.parseInt(txtTimMaThuoc.getText());
                String sql="SELECT MATHUOC FROM THUOC WHERE MATHUOC="+maThuoc;

                connect();
                Statement stm=conn.createStatement();
                ResultSet rs=stm.executeQuery(sql);
                //tạo biến tạm thời chứa mã nv là temp
                int temp=0;
                while(rs.next()){
                    temp=rs.getInt(1);
                }
                if(temp<1){
                    JOptionPane.showMessageDialog(this, "Không tồn tại thuốc");
                    txtTimMaThuoc.setText("");
                }
                else{
                    findThuoc(temp);
                    showThuocDialog show=new showThuocDialog(this, true);
                    show.setVisible(true);
                    txtTimMaThuoc.setText("");
                }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(panelKhamBenh, "Mã thuốc là số");
            txtTimMaThuoc.setText("");
        }  
    }//GEN-LAST:event_jButton3ActionPerformed

    private void btTimNVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btTimNVActionPerformed
        try {
            int maNV=Integer.parseInt(txtTimMaNV.getText());

            String sql="SELECT MANV FROM NHANVIEN WHERE MANV="+maNV;
            connect();
            Statement stm=conn.createStatement();
            ResultSet rs=stm.executeQuery(sql);
            //tạo biến tạm thời chứa mã nv là temp
            int temp=0;
            while(rs.next()){
                temp=rs.getInt(1);
            }
            if(temp<1){
                JOptionPane.showMessageDialog(panelKhamBenh, "Không tồn tại nhân viên");
            }
            else{
                //chạy hàm findNhanVien để set location của nhân viên cần show
                findNhanVien(temp);
                showNhanVienDialog show=new showNhanVienDialog(this, true);
                show.setVisible(true);
                txtTimMaNV.setText("");
            }
        } catch (SQLException ex) {
            Logger.getLogger(BaseFrame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(rootPane, "Mã phiếu khám phải là số và không chứa kí tự khác");
            txtTimMaNV.setText("");
        }
    }//GEN-LAST:event_btTimNVActionPerformed

    private void btTimMaBNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btTimMaBNActionPerformed
        //boolean flag = true;
        try {
                int maBN = Integer.parseInt(txtTimBenhNhan.getText());
                String sql="SELECT MABN FROM BENHNHAN WHERE MABN="+maBN;

                connect();
                Statement stm=conn.createStatement();
                ResultSet rs=stm.executeQuery(sql);
                //tạo biến tạm thời chứa mã nv là temp
                int temp=0;
                while(rs.next()){
                    temp=rs.getInt(1);
                }
                if(temp<1){
                    JOptionPane.showMessageDialog(panelKhamBenh, "Không tồn tại mã bệnh nhân");
                }
                else{
                    findBenhNhan(temp);
                    showBenhNhanDialog show=new showBenhNhanDialog(this, true);
                    show.setVisible(true);
                    txtTimBenhNhan.setText("");
                }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(panelKhamBenh, "Mã bệnh nhân là số");
        }  
    }//GEN-LAST:event_btTimMaBNActionPerformed

    private void txtTimBenhNhanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTimBenhNhanActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTimBenhNhanActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        boolean flag = true;
        try {
            if(flag == true){
                int maBN = Integer.parseInt(txtTimMaBN.getText());
                String sql="SELECT MABN FROM BENHNHAN WHERE MABN="+maBN;

                connect();
                Statement stm=conn.createStatement();
                ResultSet rs=stm.executeQuery(sql);
                //tạo biến tạm thời chứa mã nv là temp
                int temp=0;
                while(rs.next()){
                    temp=rs.getInt(1);
                }
                if(temp<1){
                    JOptionPane.showMessageDialog(this, "Không tồn tại mã bệnh nhân");
                }
                else{
                    int response=JOptionPane.showConfirmDialog(rootPane, "Bạn có muốn thêm phiếu khám cho bệnh nhân này không ?", "Thêm phiếu khám", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(response==JOptionPane.YES_OPTION){
                        this.maBN=temp;
                        InserpkJdialog show=new InserpkJdialog(this, true);
                        show.setVisible(true);
                    }
                    txtTimMaBN.setText("");
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(panelKhamBenh, "Mã nhân viên là số");
            flag = false;
        }  
    }//GEN-LAST:event_jButton1ActionPerformed

    private void cbbLoaiThuocActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbbLoaiThuocActionPerformed
        try {
            // TODO add your handling code here:
            String loaiThuoc=cbbLoaiThuoc.getSelectedItem().toString();
            connect();
            String donvi="";
            String getMaThuoc="select DONVI FROM THUOC WHERE LOAITHUOC='"+loaiThuoc+"'";
            Statement stm=conn.createStatement();
            ResultSet rs=stm.executeQuery(getMaThuoc);
            while(rs.next()){
                donvi=rs.getString(1);
            }
            txtDonVi.setText(donvi);
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(BaseFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_cbbLoaiThuocActionPerformed

    private void jButtonThongkeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonThongkeActionPerformed
        // TODO add your handling code here:
        panelBenhNhan.setVisible(false);
        panelNhanVien.setVisible(false);
        panelKhamBenh.setVisible(false);
        panelTiepNhan.setVisible(false);
        panelThuoc.setVisible(false);
        panelThongKe.setVisible(true);
        
        resetColor(pnBttBenhNhan);
        resetColor(pnBttNhanVien);
        resetColor(pnBttKhamBenh);
        resetColor(pnBttTiepNhan);
        resetColor(pnBttThuoc);
        setColor(pnBttThongKe);
    }//GEN-LAST:event_jButtonThongkeActionPerformed

    private void jButtonBenhNhan1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonBenhNhan1ActionPerformed
        // TODO add your handling code here:
        panelBenhNhan.setVisible(true);
        panelNhanVien.setVisible(false);
        panelKhamBenh.setVisible(false);
        panelTiepNhan.setVisible(false);
        panelThuoc.setVisible(false);
        panelThongKe.setVisible(false);
        
        setColor(pnBttBenhNhan);
        resetColor(pnBttNhanVien);
        resetColor(pnBttKhamBenh);
        resetColor(pnBttTiepNhan);
        resetColor(pnBttThuoc);
        resetColor(pnBttThongKe);
    }//GEN-LAST:event_jButtonBenhNhan1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) throws ClassNotFoundException, SQLException{
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
//        try {
//            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//                if ("Nimbus".equals(info.getName())) {
//                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//            }
//        } catch (ClassNotFoundException ex) {
//            java.util.logging.Logger.getLogger(JFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(JFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(JFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(JFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        //</editor-fold>
//
//        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(() -> {
//            new JFrame().setVisible(true);
//        });
        
//        Connection con=DriverManager.getConnection(
//                "jdbc:oracle:thin:@localhost:1521:orcl","DOAN_ORACLE","minhhy");
//        //Connection con=DriverManger
//        Statement stmt=con.createStatement();
//        //thực hiện câu truy vấn
//        ResultSet rs=stmt.executeQuery("select * from NHANVIEN");
//        while(rs.next())
//            System.out.print(rs.getInt(1)+"\t"+ rs.getString(2)+"\t"+ rs.getString(3)+"\t"+ rs.getString(4)+"\t"+ 
//                        rs.getString(5)+"\t"+ rs.getString(6)+"\t"+ rs.getString(7)+"\t"+ rs.getString(8)+"\t"+ rs.getLong(9));
//        con.close();
        BaseFrame frame=new BaseFrame();
        
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPopupMenu PopUpMenu;
    private javax.swing.JPopupMenu PopUpMenu1;
    private javax.swing.JButton btThemBN;
    private javax.swing.JButton btThemBenhAn;
    private javax.swing.JButton btThemNV;
    private javax.swing.JButton btThemThuoc;
    private javax.swing.JButton btThemThuocToToa;
    private javax.swing.JButton btTimMaBN;
    private javax.swing.JButton btTimMatoa;
    private javax.swing.JButton btTimNV;
    private javax.swing.JButton btTimPK;
    private javax.swing.JButton btToaThuoc;
    private javax.swing.JComboBox<String> cbbBacSi;
    private javax.swing.JComboBox<String> cbbChuanDoan;
    private javax.swing.JComboBox<String> cbbLoaiThuoc;
    private javax.swing.JComboBox<String> cbbTienSu;
    private javax.swing.JMenuItem deleteBN;
    private javax.swing.JMenuItem deleteNV;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButtonBenhNhan1;
    private javax.swing.JButton jButtonKhamBenh;
    private javax.swing.JButton jButtonNhanvien;
    private javax.swing.JButton jButtonThongke;
    private javax.swing.JButton jButtonThuoc;
    private javax.swing.JButton jButtonTiepNhan;
    private javax.swing.JRadioButton jFemale;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLayeredPane jLayeredPane1;
    private javax.swing.JRadioButton jMale;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel_title;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JPanel panelBenhNhan;
    private javax.swing.JPanel panelKhamBenh;
    private javax.swing.JPanel panelNhanVien;
    private javax.swing.JPanel panelThongKe;
    private javax.swing.JPanel panelThuoc;
    private javax.swing.JPanel panelTiepNhan;
    private javax.swing.JPanel pnBttBenhNhan;
    private javax.swing.JPanel pnBttKhamBenh;
    private javax.swing.JPanel pnBttNhanVien;
    private javax.swing.JPanel pnBttThongKe;
    private javax.swing.JPanel pnBttThuoc;
    private javax.swing.JPanel pnBttTiepNhan;
    private javax.swing.JSpinner soLuong;
    private javax.swing.JTable tblBN;
    private javax.swing.JTable tblBenhNhan;
    private javax.swing.JTable tblDoanhSoThuoc;
    private javax.swing.JTable tblHoaDon;
    private javax.swing.JTable tblNhanVien;
    private javax.swing.JTable tblThuoc;
    private javax.swing.JTable tblToaThuoc;
    private javax.swing.JTextField ttTimHoaDon;
    private javax.swing.JTextField txtCachDung;
    private javax.swing.JTextField txtDiaChi;
    private javax.swing.JTextField txtDonVi;
    private javax.swing.JTextField txtHoTen;
    private javax.swing.JTextField txtLiDoKham;
    private javax.swing.JTextField txtMaBN;
    private javax.swing.JTextField txtMaBNKham;
    private javax.swing.JTextField txtMaHD;
    private com.toedter.calendar.JDateChooser txtNgaySinh;
    private javax.swing.JTextField txtSDT;
    private javax.swing.JTextField txtTenBN;
    private javax.swing.JTextField txtTimBenhNhan;
    private javax.swing.JTextField txtTimMaBN;
    private javax.swing.JTextField txtTimMaHD;
    private javax.swing.JTextField txtTimMaNV;
    private javax.swing.JTextField txtTimMaPK;
    private javax.swing.JTextField txtTimMaThuoc;
    private javax.swing.JTextField txtTongDoanhThu;
    private javax.swing.JTextField txtTrieuChung;
    private javax.swing.JMenuItem updateBN;
    private javax.swing.JMenuItem updateNV;
    // End of variables declaration//GEN-END:variables
}
