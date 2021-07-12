package QueriesCreater;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

public class WriteToFile {
    void write(String pSql) {
        //Save file to
        FileNameExtensionFilter filter = new FileNameExtensionFilter("*.txt", "*.txt", "*.TXT", "*.*");
        JFileChooser save_to = new JFileChooser();
        save_to.setFileFilter(filter);
        save_to.setCurrentDirectory(new File
                (System.getProperty("user.home") + System.getProperty("file.separator") + "Desktop"));
        int ret = save_to.showDialog(null, "Save");
        if (ret == JFileChooser.APPROVE_OPTION) {
            try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(save_to.getSelectedFile() + ".txt"), StandardCharsets.UTF_8)) {
                writer.write(pSql);
                writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
