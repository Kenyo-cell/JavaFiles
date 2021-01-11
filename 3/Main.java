import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import java.util.Random;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


class Main {
    private static final String PATH = "D:\\progs\\java\\Games\\savegames\\";

    private static GameProgress loadGameProgress(String path) {
        extractAllFiles(path);

        File[] files = (new File(PATH)).listFiles();
        File openedFile = files[(new Random()).nextInt(files.length)];
        
        return getRandomSaveFileInfo(openedFile);
    }

    private static void extractAllFiles(String path) {
        File saveArch = new File(PATH + "zip_saves.zip");
        boolean success = false;

        if (!saveArch.exists()) return;

        try (ZipInputStream zin = new ZipInputStream(new FileInputStream(saveArch))) {
            ZipEntry entry;
            while ((entry = zin.getNextEntry()) != null) {
                extractFile(entry.getName(), zin);
            }
            success = true;
        } catch (IOException ex) {
            System.err.println(ex);
            success = false;
        } finally {
            if (success) saveArch.delete();
        }
    }

    private static void extractFile(String name, ZipInputStream zin) {
        try (FileOutputStream fout = new FileOutputStream(PATH + name)) {
            for (int c = zin.read(); c != -1; c = zin.read()) {
                fout.write(c);
            }
            fout.flush();
            zin.closeEntry();
        } catch (IOException ex) {
            System.err.println(ex);
        }
    }

    private static GameProgress getRandomSaveFileInfo(File openedFile) {
        GameProgress gp = null;
        try (FileInputStream fis = new FileInputStream(openedFile);
                ObjectInputStream ois = new ObjectInputStream(fis)) {
                
                gp = (GameProgress) ois.readObject();
            } catch (Exception ex) {
                System.err.println(ex);
            }

        return gp;
    }

    public static void main(String[] args) {
        System.out.println(loadGameProgress(PATH));
    }
}