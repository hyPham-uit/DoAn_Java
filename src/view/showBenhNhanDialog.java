/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import model.BenhNhan;

/**
 *
 * @author HP
 */
public class showBenhNhanDialog extends javax.swing.JDialog {
    private String gioiTinh;
    /**
     * Creates new form insertBNDialog
     */
    public void showBenhNhanInfo(BenhNhan bn){
        try{
            txtMaBN.setText(bn.getMaBN()+"");
            txtHoTen.setText(bn.getHoTen());
            //txtNgaySinh.setText(nv.getNgSinh());
            String date=bn.getNgaySinh();
            Date date2 = new SimpleDateFormat("MMMM d, yyyy").parse(date);
            txtNgaySinh.setDate(date2);
            if(bn.getGioiTinh().equalsIgnoreCase("MALE")){
                Male.setSelected(true);
                Female.setSelected(false);
                gioiTinh="MALE";
            }
            else{
                Male.setSelected(false);
                Female.setSelected(true);
                gioiTinh="FEMALE";
            }
            txtDiaChi.setText(bn.getDiaChi());
            
            txtSDT.setText(bn.getSDT());
        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(rootPane, "Lỗi hiện thông tin");
            Logger.getLogger(BaseFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    public int updateBenhNhan(int location){
        //kiểm tra thông tin có đúng chuẩn ko
        String tenBN=txtHoTen.getText();
        try {
            SimpleDateFormat dateFormat=new SimpleDateFormat("MMMM d, y");
            String ngaySinh=dateFormat.format(txtNgaySinh.getDate());
            String diaChi=txtDiaChi.getText();
            String soDT=txtSDT.getText();
            
            //String chucDanh=txtChucDanh.getText();
            if(tenBN.equals("")==true || ngaySinh.equals("")==true || diaChi.equals("")==true  || soDT.equals("")==true ){
                JOptionPane.showMessageDialog(this, "Nhập thiếu giá trị");
                return 0;
            }
            else{
                //home.listNV.add(new NhanVien(maNV, tenNV, ngaySinh, diaChi, gioiTinh, soDT, chucDanh, maPhong, luong));
                home.listBN.get(location).setHoTen(tenBN);
                home.listBN.get(location).setNgaySinh(ngaySinh);
                home.listBN.get(location).setGioiTinh(gioiTinh);
                home.listBN.get(location).setDiaChi(diaChi);
                
                home.listBN.get(location).setSDT(soDT);
            }
       }catch(NullPointerException e){
           JOptionPane.showMessageDialog(this, "Nhập sai định dạng giá trị Ngày sinh");
           return 0;
       }
       return 1;
    }
    
    private BaseFrame home;
    public showBenhNhanDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        setLocationRelativeTo(null);
        home=(BaseFrame) parent;
        txtMaBN.setEditable(false);
        this.setTitle("Thông tin bệnh nhân");
        BenhNhan bn=home.getBenhNhan();
        showBenhNhanInfo(bn);
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        txtMaBN = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtHoTen = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        txtDiaChi = new javax.swing.JTextField();
        jLabel32 = new javax.swing.JLabel();
        txtSDT = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        txtNgaySinh = new com.toedter.calendar.JDateChooser();
        btThem = new javax.swing.JButton();
        btThoat = new javax.swing.JButton();
        btXoa = new javax.swing.JButton();
        Male = new javax.swing.JRadioButton();
        Female = new javax.swing.JRadioButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(69, 123, 157));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("THÔNG TIN BỆNH NHÂN");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(23, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addContainerGap(18, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(204, 204, 204));

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel4.setText("Mã bệnh nhân");

        txtMaBN.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel5.setText("Họ và tên");

        txtHoTen.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel6.setText("Giới tính");

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel9.setText("Địa chỉ");

        txtDiaChi.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        jLabel32.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel32.setText("Số ĐT");

        txtSDT.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel10.setText("Ngày sinh");

        txtNgaySinh.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        btThem.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        btThem.setIcon(new javax.swing.ImageIcon("D:\\Java\\JavaPhongMT\\src\\images\\Pencil-icon.png")); // NOI18N
        btThem.setText("CẬP NHẬT");
        btThem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btThemActionPerformed(evt);
            }
        });

        btThoat.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        btThoat.setIcon(new javax.swing.ImageIcon("D:\\Java\\JavaPhongMT\\src\\images\\Exit-icon.png")); // NOI18N
        btThoat.setText("THOÁT");
        btThoat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btThoatActionPerformed(evt);
            }
        });

        btXoa.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        btXoa.setIcon(new javax.swing.ImageIcon("D:\\Java\\JavaPhongMT\\src\\images\\Trash-can-icon.png")); // NOI18N
        btXoa.setText("XÓA");
        btXoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btXoaActionPerformed(evt);
            }
        });

        Male.setText("Male");

        Female.setText("Female");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btThem)
                .addGap(15, 15, 15)
                .addComponent(btXoa)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 15, Short.MAX_VALUE)
                .addComponent(btThoat)
                .addContainerGap())
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtSDT, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtMaBN, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtDiaChi, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(txtHoTen, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(Male)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(Female))
                                    .addComponent(txtNgaySinh, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtMaBN, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtHoTen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel10)
                    .addComponent(txtNgaySinh, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jLabel6))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(Male)
                            .addComponent(Female))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtSDT, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9)
                            .addComponent(txtDiaChi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(jLabel32)))
                .addGap(18, 41, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btXoa)
                    .addComponent(btThoat)
                    .addComponent(btThem))
                .addGap(22, 22, 22))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btXoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btXoaActionPerformed
        // TODO add your handling code here:
        int mabn=Integer.parseInt(txtMaBN.getText());
        if(home.deleteBenhNhan(mabn)==1){
            JOptionPane.showMessageDialog(rootPane, "Một dòng đã bị xóa");
            this.dispose();
        }
        
    }//GEN-LAST:event_btXoaActionPerformed

    private void btThemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btThemActionPerformed
        // TODO add your handling code here:
        if(updateBenhNhan(home.getlocation())==1){//update trong arraylist
            if(home.updateBenhNhan()==1){//update trong database
                this.dispose();;
            }
            else{
                JOptionPane.showMessageDialog(rootPane, "Lỗi update trong baseframe");
            }
        }
        else{
            JOptionPane.showMessageDialog(rootPane, "Không có vị trí");
        }
    }//GEN-LAST:event_btThemActionPerformed

    private void btThoatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btThoatActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_btThoatActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(showBenhNhanDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(showBenhNhanDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(showBenhNhanDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(showBenhNhanDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                showBenhNhanDialog dialog = new showBenhNhanDialog(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JRadioButton Female;
    private javax.swing.JRadioButton Male;
    private javax.swing.JButton btThem;
    private javax.swing.JButton btThoat;
    private javax.swing.JButton btXoa;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JTextField txtDiaChi;
    private javax.swing.JTextField txtHoTen;
    private javax.swing.JTextField txtMaBN;
    private com.toedter.calendar.JDateChooser txtNgaySinh;
    private javax.swing.JTextField txtSDT;
    // End of variables declaration//GEN-END:variables
}
