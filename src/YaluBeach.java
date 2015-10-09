import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.text.DefaultCaret;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

public class YaluBeach {

    public static void main(String[] args) {
        
        new YaluBeach();
        
    }

    public YaluBeach() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (ClassNotFoundException ex) {
                } catch (InstantiationException ex) {
                } catch (IllegalAccessException ex) {
                } catch (UnsupportedLookAndFeelException ex) {
                }

                JFrame frame = new JFrame("Testing");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.add(new TestPane());
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }

    public class TestPane extends JPanel {

        public JTextArea DisplayArea;
        private JTextField input;
        private JPanel bottomPanel;
        JButton jButton1;

        private Process process;

        public TestPane() {
            setLayout(new BorderLayout());

            bottomPanel = new JPanel();
            bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
            jButton1 = new JButton("JailbreakMe!");
            bottomPanel.add(jButton1);
            JButton jButton2 = new JButton("JailbreakMe!");
            bottomPanel.add(jButton2);
            
            DisplayArea = new JTextArea(10, 10);
            input = new JTextField(10);

            DisplayArea.setLineWrap(false);
            DisplayArea.setWrapStyleWord(false);
            DisplayArea.setEditable(false);
            DisplayArea.setFocusable(false);

            add(new JScrollPane(DisplayArea), BorderLayout.NORTH);
            add(input, BorderLayout.CENTER);
            add(bottomPanel, BorderLayout.SOUTH);

            input.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String cmd = input.getText() + "\n";
                    input.setText(null);
                    
                    if (process == null) {
                        ProcessBuilder pb = new ProcessBuilder("bash");
                        pb.directory(new File("."));
                        try {
                            process = pb.start();
                            InputStreamWorker isw = new InputStreamWorker(DisplayArea, process.getInputStream());
                            isw.execute();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                            input.setEnabled(false);
                        }

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                int exit = -1;
                                try {
                                    exit = process.waitFor();
                                } catch (InterruptedException ex) {
                                }
                                System.out.println("Exited with " + exit);
                                input.setEnabled(false);
                            }
                        }).start();

                    }
                    OutputStream os = process.getOutputStream();
                    try {
                        os.write(cmd.getBytes());
                        os.flush();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        input.setEnabled(false);
                    }
                }
            });
        }

    }

    public class InputStreamWorker extends SwingWorker<Void, Character> {

        private InputStream is;
        private JTextArea output;

        public InputStreamWorker(JTextArea output, InputStream is) {
            this.is = is;
            this.output = output;
        }

        @Override
        protected void process(List<Character> chunks) {
            StringBuilder sb = new StringBuilder(chunks.size());
            for (Character c : chunks) {
                sb.append(c);
            }
            output.append(sb.toString());
        }

        @Override
        protected Void doInBackground() throws Exception {
            int in = -1;
            while ((in = is.read()) != -1) {
                publish((char)in);
            }
            return null;
        }
        
    }
    
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {                                         
        File f = new File("/Users/michaeldvinci/Downloads/yalu-master.zip");
        if (f.exists()) {
            System.out.println("Yalu version exists, deleting and downloading newest version...\n");
            try {
                deleteOldVersion();
            } catch (IOException | net.lingala.zip4j.exception.ZipException | InterruptedException ex) {
                Logger.getLogger(YaluShore.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else {
            //DisplayArea.append("Yalu version doesn't exist, downloading newest version.....\n");
            try {
                downloadNewestVersion();
            } catch (IOException | ZipException | InterruptedException ex) {
                Logger.getLogger(YaluShore.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        //DisplayArea.append("\n");
    }                                        

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {                                         
        
    }                                        

    private void deleteOldVersion() throws IOException, net.lingala.zip4j.exception.ZipException, InterruptedException {
        Path path = Paths.get("/Users/michaeldvinci/Downloads/yalu-master.zip");
        File path2 = new File("/Users/michaeldvinci/Downloads/yalu-master");
        try {
            Files.delete(path);
            //DisplayArea.append("File deleted\n");
        }
        catch (NoSuchFileException x) { 
            //DisplayArea.append("Trouble deleting, try again!\n"); 
        }
        try {
            File[] contents = path2.listFiles();
            if (contents != null) {
                for (File f : contents) {
                    deleteDir(path2);
                }
            }
            path2.delete();
            //DisplayArea.append("Old Folder deleted\n");
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
        String localFilename = "/Users/michaeldvinci/Downloads/yalu-master.zip";
        
        //DisplayArea.append("Downloading!\n");
        downloadFromUrl(url, localFilename);
        //DisplayArea.append("Downloaded!\n");
        
        String source = "/Users/michaeldvinci/Downloads/yalu-master.zip";

        ZipFile zipf = new ZipFile(source);
        zipf.extractAll("/Users/michaeldvinci/Downloads/");
        
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
        //DisplayArea.append("Setting permissions...\n");
        Runtime.getRuntime().exec( "chmod -R 777 /Users/michaeldvinci/Downloads/yalu-master/" );
        //DisplayArea.append("Starting Yalu's run.sh\n");
        
        ProcessBuilder pb = new ProcessBuilder("/Users/michaeldvinci/Downloads/yalu-master/stage0.sh");
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
                        //DisplayArea.setText(line);
                        //DisplayArea.append("\n");
                        //DisplayArea.update(DisplayArea.getGraphics());
                        //isplayArea.setText(DisplayArea.getText() + line );
                        //DisplayArea.setCaretPosition(DisplayArea.getDocument().getLength());
                        //DefaultCaret caret = (DefaultCaret)DisplayArea.getCaret();
                        //caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
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
}