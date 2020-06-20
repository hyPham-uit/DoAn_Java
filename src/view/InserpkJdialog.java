package view;

import com.fasterxml.jackson.databind.type.MapLikeType;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import model.BenhNhan;
import model.PhieuKham;

public class InserpkJdialog extends javax.swing.JDialog {

    private Connection conn;
    private BaseFrame home;
    private int maPK=0;
    public InserpkJdialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        this.setLocationRelativeTo(null);
        home = (BaseFrame) parent;
        this.setTitle("Nhập thông tin phiếu khám");
        this.setLocationRelativeTo(null);
        setMaPK();
        txtMaPK.setEditable(false);
        txtMaPK.setText(maPK+"");
        txtMaBN.setEditable(false);
        txtMaBN.setText(home.getMaBN()+"");
        
    }

    public void connect(){
        try {
            String url="jdbc:oracle:thin:@localhost:1521:orcl";
            conn=DriverManager.getConnection(url,"DOAN_ORACLE","admin");
        } catch (SQLException ex) {
            Logger.getLogger(BaseFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setMaPK(){
        try {
            connect();
            String query="SELECT MAHD FROM HOADON ORDER BY MAHD DESC FETCH FIRST 1 ROW ONLY";
            Statement stm=conn.createStatement();
            ResultSet rs=stm.executeQuery(query);
            while(rs.next()){
                maPK=rs.getInt(1)+1;
            }
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(BaseFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    

    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtMaPK = new javax.swing.JTextField();
        txtMaBN = new javax.swing.JTextField();
        btThem = new javax.swing.JButton();
        btThoat = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        txtLiDoKham = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setModal(true);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 20)); // NOI18N
        jLabel1.setText("NHẬP THÔNG TIN PHIẾU KHÁM");

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel2.setText("Mã phiếu khám");

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel4.setText("Mã bệnh nhân");

        btThem.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        btThem.setText("Thêm");
        btThem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btThemActionPerformed(evt);
            }
        });

        btThoat.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        btThoat.setText("Thoát");
        btThoat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btThoatActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel6.setText("Lí do khám");

        txtLiDoKham.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel4))
                        .addGap(40, 40, 40)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtMaBN, javax.swing.GroupLayout.DEFAULT_SIZE, 148, Short.MAX_VALUE)
                            .addComponent(txtMaPK)
                            .addComponent(txtLiDoKham)))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(126, 126, 126)
                        .addComponent(btThem)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btThoat))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel1)))
                .addContainerGap(18, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addGap(10, 10, 10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtMaPK, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtMaBN, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txtLiDoKham, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(35, 35, 35)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btThem)
                    .addComponent(btThoat))
                .addContainerGap(21, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btThoatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btThoatActionPerformed
        this.dispose();
    }//GEN-LAST:event_btThoatActionPerformed

    private void btThemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btThemActionPerformed
        String liDoKham=txtLiDoKham.getText();
        if(txtMaPK.getText().equals("")||txtMaBN.getText().equals("")||txtLiDoKham.getText().equals("")){
            JOptionPane.showMessageDialog(this, "Bạn hãy điền đầy đủ thông tin");
        }
        else{
            try {
                java.util.Date today = new java.util.Date();
                DateFormat dateFormat = new SimpleDateFormat("MMM d, yyyy");
                String ngayKham = dateFormat.format(today);
                java.util.Date date2 = new SimpleDateFormat("MMM d, yyyy").parse(ngayKham);
                java.sql.Date sqlDate = new java.sql.Date(date2.getTime());
                
                connect();
                try {
                    String insert = "insert into HOADON values(?,?,?,?,?,?,?,?,?)";
                    PreparedStatement st = conn.prepareStatement(insert);
                    st.setInt(1, maPK);
                    st.setInt(2, home.getMaBN());
                    st.setInt(3, 0);
                    st.setString(4, liDoKham);
                    st.setString(5, "");
                    st.setString(6, "");
                    st.setInt(7, 30000);
                    st.setInt(8, 30000);
                    st.setDate(9, sqlDate);
                    
                    int x = st.executeUpdate();
                    if(x > 0){
                        JOptionPane.showMessageDialog(this, "Thêm phiếu khám thành công ");
                    }else{
                        JOptionPane.showMessageDialog(this, "Không thêm được vào dữ liệu");
                    }
                    this.dispose();
                }catch (SQLException ex) {
                    Logger.getLogger(BaseFrame.class.getName()).log(Level.SEVERE, null, ex);
                }

            }catch (ParseException ex) {
                Logger.getLogger(BaseFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
            this.dispose();
        }  
    }//GEN-LAST:event_btThemActionPerformed

    
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
            java.util.logging.Logger.getLogger(InserpkJdialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(InserpkJdialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(InserpkJdialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(InserpkJdialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                InserpkJdialog dialog = new InserpkJdialog(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton btThem;
    private javax.swing.JButton btThoat;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JTextField txtLiDoKham;
    private javax.swing.JTextField txtMaBN;
    private javax.swing.JTextField txtMaPK;
    // End of variables declaration//GEN-END:variables
}
