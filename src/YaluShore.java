
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.lingala.zip4j.core.*;
import net.lingala.zip4j.exception.ZipException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import javax.swing.text.DefaultCaret;

 class YaluShore extends javax.swing.JFrame {

    public YaluShore() {
        initComponents();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton1 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        DisplayArea = new javax.swing.JTextArea();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jButton1.setText("JailbreakMe!");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Lucida Grande", 0, 24)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("JUST A CONCEPT!!");
        jLabel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jLabel1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        DisplayArea.setColumns(20);
        DisplayArea.setRows(5);
        jScrollPane1.setViewportView(DisplayArea);

        jScrollPane2.setViewportView(jScrollPane1);

        jButton2.setText("After Restore");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jScrollPane2)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 254, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 232, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        File f = new File("./yalu-master.zip");
        if (f.exists()) {
            DisplayArea.append("Yalu version exists, deleting and downloading newest version...\n");
            try {
                deleteOldVersion();
            } catch (IOException | net.lingala.zip4j.exception.ZipException | InterruptedException ex) {
                Logger.getLogger(YaluShore.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else {
            DisplayArea.append("Yalu version doesn't exist, downloading newest version.....\n");
            try {
                downloadNewestVersion();
            } catch (IOException | ZipException | InterruptedException ex) {
                Logger.getLogger(YaluShore.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        DisplayArea.append("\n");
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        
    }//GEN-LAST:event_jButton2ActionPerformed

    private void deleteOldVersion() throws IOException, net.lingala.zip4j.exception.ZipException, InterruptedException {
        Path path = Paths.get("./yalu-master.zip");
        File path2 = new File("./yalu-master");
        try {
            Files.delete(path);
            DisplayArea.append("File deleted\n");
        }
        catch (NoSuchFileException x) { DisplayArea.append("Trouble deleting, try again!\n"); }
        try {
            File[] contents = path2.listFiles();
            if (contents != null) {
                for (File f : contents) {
                    deleteDir(path2);
                }
            }
            path2.delete();
            DisplayArea.append("Old Folder deleted\n");
        }
        catch ( Exception e ) {}
        downloadNewestVersion();
    }
    
    void deleteDir(File file) {
        File[] contents = file.listFiles();
        if (contents != null) {
            for (File f : contents) {
                deleteDir(f);
            }
        }
        file.delete();
    }
    
    private void downloadNewestVersion() throws IOException, net.lingala.zip4j.exception.ZipException, InterruptedException {
        URL url = new URL("https://github.com/kpwn/yalu/archive/master.zip");
        String localFilename = "./yalu-master.zip";
        
        DisplayArea.append("Downloading!\n");
        downloadFromUrl(url, localFilename);
        DisplayArea.append("Downloaded!\n");
        
        String source = "./yalu-master.zip";

        ZipFile zipf = new ZipFile(source);
        zipf.extractAll("./");
        
        executeScript();
    }
    
    void downloadFromUrl(URL url, String localFilename) throws IOException {
        
        try {
            URLConnection conn = url.openConnection();
            InputStream in = conn.getInputStream();
            FileOutputStream out = new FileOutputStream(localFilename);
            byte[] b = new byte[1024];
            int count;
            while ((count = in.read(b)) >= 0) {
                out.write(b, 0, count);
            }
            out.flush(); out.close(); in.close();                   

        } catch (IOException e) {
        }
    }
    
    public void executeScript() throws IOException, InterruptedException {
        DisplayArea.append("Setting permissions...\n");
        String currDir = System.getProperty("user.dir");
        String execCmd = "";
        Runtime.getRuntime().exec("chmod -R 777 " + currDir + "/yalu-master/");
        DisplayArea.append("Starting Yalu's run.sh\n");
        
        ProcessBuilder pb = new ProcessBuilder("./yalu-master/stage0.sh").inheritIO();
        //pb.redirectOutput(Redirect.INHERIT);
        //pb.redirectError(Redirect.INHERIT);
        
        pb.redirectErrorStream();
        Process p = pb.start();
                    
        final Thread ioThread = new Thread() {
            @Override
            public void run() {
                try {
                    InputStreamReader isr = new InputStreamReader(p.getInputStream());
                    final BufferedReader reader = new BufferedReader(isr);
                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        System.out.println(line);
                        if(!line.contains("%")) {
                            DisplayArea.append("\n");
                        }
                        if (line.contains("100%") && line.contains("=")) {
                            DisplayArea.append("\n");
                        }
                            DisplayArea.update(DisplayArea.getGraphics());
                        //isplayArea.setText(DisplayArea.getText() + line );
                        DisplayArea.setCaretPosition(DisplayArea.getDocument().getLength());
                        DefaultCaret caret = (DefaultCaret)DisplayArea.getCaret();
                        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
                    }
                    reader.close();
                } catch (final Exception e) {
                }
            }
        };
        ioThread.start();
        p.waitFor();
        System.out.println("Script executed successfully");
    }
    
    
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
            java.util.logging.Logger.getLogger(YaluShore.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(YaluShore.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(YaluShore.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(YaluShore.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new YaluShore().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea DisplayArea;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    // End of variables declaration//GEN-END:variables
}
