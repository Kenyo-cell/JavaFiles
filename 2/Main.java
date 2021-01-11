import java.io.IOException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

class Main {
    private static final String PATH = "D:\\progs\\java\\Games\\savegames\\";


    public static void archiveSaves(String path) {
        File dir = new File(PATH);
        File[] files = dir.listFiles();

        try (ZipOutputStream zout = new ZipOutputStream(
             new FileOutputStream(path + "zip_saves.zip"))) {

            for (File file : files) {
                if (file.getName().contains(".dat")) {
                    ZipEntry entry = new ZipEntry(file.getName());
                    zout.putNextEntry(entry);
                    
                    zout.write(insertBuffer(path + file.getName()));
                    zout.closeEntry();

                    file.delete();
                }
            }
        } catch (IOException ex) {
            System.err.println(ex);
        }
    }

    public static byte[] insertBuffer(String path) {
        byte[] buffer = new byte[0];
        try (FileInputStream fis = new FileInputStream(new File(path))) {
            buffer = new byte[fis.available()];
            fis.read(buffer);
        } catch (IOException ex) {
            System.err.println(ex);
        }
        
        return buffer;
    }

    public static void main(String[] args) {
        GameProgress g1 = new GameProgress(100, 2, 4, -124.5);
        GameProgress g2 = new GameProgress(78, 3, 6, -100.3);
        GameProgress g3 = new GameProgress(45, 5, 8, -70.1);
        g1.saveGame(PATH, "save1.dat");
        g2.saveGame(PATH, "save2.dat");
        g3.saveGame(PATH, "save3.dat");
        archiveSaves(PATH);
    }
}