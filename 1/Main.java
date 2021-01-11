import java.io.IOException;
import java.io.File;
import java.io.FileWriter;

import java.util.LinkedList;
import java.util.List;


public class Main {
    private static class Pair<F, S> {
        F first;
        S second;

        Pair(F first, S second) {
            this.first = first;
            this.second = second;
        }
    }


    private static final boolean FILE = true;
    private static final boolean NOT_FILE = false;
    private static final String logFileRelativePath = "temp\\temp.txt";


    private static boolean install(String corePath) {
        List< Pair< Pair<Boolean, String>, String[] > > catalogs = createCatalogMap(corePath);
        StringBuilder log = new StringBuilder();

        for (Pair< Pair<Boolean, String>, String[] > pair : catalogs) {
            Pair<Boolean, String> nameBoolPair = pair.first;
            for (String name : pair.second) {
                log.append(logProgress(nameBoolPair.first, nameBoolPair.second + name));
            }
        }

        File logFile = new File(corePath + logFileRelativePath);
        try (FileWriter writer = new FileWriter(logFile)) {
            writer.write(log.toString());
        } catch (IOException ex) {
            System.err.println(ex + " -> Cannot log.");
        }

        return true;
    }

    private static List< Pair< Pair<Boolean, String>, String[] > > createCatalogMap(String corePath) {
        String[] coreCatalog = { "src", "res", "savegames", "temp" };
        String[] srcCatalog = { "main", "test" };
        String[] resCatalog = { "drawables", "vectors", "icons" };

        String[] mainCatalog = { "Main.java", "Utils.java" };
        String[] tempCatalog = { "temp.txt" };

        List< Pair< Pair<Boolean, String>, String[] > > catalogs = new LinkedList<>();
        catalogs.add(new Pair<>(new Pair<>(NOT_FILE, corePath), coreCatalog));
        catalogs.add(new Pair<>(new Pair<>(NOT_FILE, corePath + "src\\"), srcCatalog));
        catalogs.add(new Pair<>(new Pair<>(NOT_FILE, corePath + "res\\"), resCatalog));
        catalogs.add(new Pair<>(new Pair<>(FILE, corePath + "src\\main\\"), mainCatalog));
        catalogs.add(new Pair<>(new Pair<>(FILE, corePath + "temp\\"), tempCatalog));
        
        return catalogs;
    }

    private static String logProgress(boolean first, String second) {
        StringBuilder log = new StringBuilder();

        log.append(second + (first ? " file " : " catalog ") + "creating: ");
        boolean success = first ? createFile(second) : mkdir(second);
        log.append(success ? "success\n" : "failed\n");

        return log.toString();
    }

    private static boolean mkdir(String pathName) {
        return (new File(pathName)).mkdir();
    }

    private static boolean createFile(String fullPath){
        boolean success = false;
        try {
            (new File(fullPath)).createNewFile();
            success = true;
        } catch (IOException ex) {
            System.err.println(ex);
            success = false;
        }
        return success;
    }

    public static void main(String[] args) {
        System.out.println("Installing " + (install("D:\\progs\\java\\Games\\") ? "success" : " failed"));
    }
}