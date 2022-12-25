import java.io.*;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class Main {

    public static final String MAIN_DIRECTORY = "/Users/mikhail/Games";

    public static StringBuilder SB = new StringBuilder();

    public static void main(String[] args) {

        createDir(MAIN_DIRECTORY, "src", "res", "savegames", "temp");

        createDir(MAIN_DIRECTORY + File.separator + "src", "main", "test");

        createFile(MAIN_DIRECTORY + File.separator + "src" + File.separator + "main",
                "Main.java", "Utils.java");

        createDir(MAIN_DIRECTORY + File.separator + "res", "drawables", "vectors", "icons");

        createFile(MAIN_DIRECTORY + File.separator + "temp", "temp.txt");

        try (FileWriter fw = new FileWriter(MAIN_DIRECTORY + File.separator
                + "temp" + File.separator + "temp.txt")) {
            fw.write(SB.toString());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        GameProgress gp1 = new GameProgress(100, 1, 3, 6.0);
        GameProgress gp2 = new GameProgress(80, 2, 7, 3.0);
        GameProgress gp3 = new GameProgress(50, 7, 65, 8.0);

        saveGame(MAIN_DIRECTORY + File.separator + "savegames" + File.separator + "save1.dat", gp1);
        saveGame(MAIN_DIRECTORY + File.separator + "savegames" + File.separator + "save2.dat", gp2);
        saveGame(MAIN_DIRECTORY + File.separator + "savegames" + File.separator + "save3.dat", gp3);


        zipFiles(MAIN_DIRECTORY + File.separator + "savegames" + File.separator + "saved_games.zip",
                "save1.dat", "save2.dat", "save3.dat");

        deleteFile(MAIN_DIRECTORY + File.separator + "savegames",
                "save1.dat", "save2.dat", "save3.dat");

        openZip(MAIN_DIRECTORY + File.separator + "savegames" + File.separator + "saved_games.zip",
                MAIN_DIRECTORY + File.separator + "savegames");

        System.out.println(openProgress(MAIN_DIRECTORY + File.separator
                + "savegames" + File.separator + "save1.dat"));

    }

    public static void createDir(String directory, String... dirNames) {
        for (String dirName : dirNames) {
            File folder = new File(directory +
                    File.separator + dirName);
            if (folder.mkdir()) {
                SB.append(new Date())
                        .append(" New directory ")
                        .append(dirName)
                        .append(" in ")
                        .append(directory)
                        .append(" create successful\n");
            } else SB.append(new Date())
                    .append(" This directory ")
                    .append(dirName)
                    .append(" in ")
                    .append(directory)
                    .append(" are already exists\n");
        }
    }

    public static void createFile(String directory, String... fileNames) {

        for (String fileName : fileNames) {
            File file = new File(directory +
                    File.separator + fileName);
            try {
                if (file.createNewFile()) SB.append(new Date())
                        .append(" New file ")
                        .append(fileName)
                        .append(" in ")
                        .append(directory)
                        .append(" create successful\n");
                else SB.append(new Date())
                        .append("New file ")
                        .append(fileName)
                        .append(" in ")
                        .append(directory)
                        .append(" create error\n");
            } catch (IOException e) {
                SB.append(new Date())
                        .append("New file ")
                        .append(fileName)
                        .append(" in ")
                        .append(directory)
                        .append(" create error")
                        .append(e.getMessage())
                        .append(".\n");
            }
        }
    }

    public static void deleteFile(String directory, String... fileNames) {
        for (String fileName : fileNames) {
            File file = new File(directory, fileName);
            if (file.delete()) System.out.printf("Файл %s удален успешно\n", fileName);
            else System.out.printf("Файл %s не был удален\n", fileName);
        }
    }

    public static void saveGame(String filePath, GameProgress gameProgress) {
        try (ObjectOutputStream objOStream = new ObjectOutputStream(new FileOutputStream(filePath))) {
            objOStream.writeObject(gameProgress);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void zipFiles(String zipFilePath, String... fileNames) {
        try (ZipOutputStream zip = new ZipOutputStream(new FileOutputStream(zipFilePath))) {
            for (String fileName : fileNames) {
                try (FileInputStream fis = new FileInputStream(MAIN_DIRECTORY + File.separator + "savegames"
                        + File.separator + fileName)) {
                    zip.putNextEntry(new ZipEntry(fileName));
                    byte[] buffer = new byte[fis.available()];
                    fis.read(buffer);
                    zip.write(buffer);
                    zip.closeEntry();
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void openZip(String zipPath, String unzipDirectory) {
        try (ZipInputStream zin = new ZipInputStream(new FileInputStream(zipPath))) {
            ZipEntry entry;
            while ((entry = zin.getNextEntry()) != null) {
                try (FileOutputStream fos = new FileOutputStream(unzipDirectory + File.separator + entry.getName())) {
                    for (int c = zin.read(); c != -1; c = zin.read()) {
                        fos.write(c);
                    }
                    fos.flush();
                    zin.closeEntry();
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static GameProgress openProgress(String filePath) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            return (GameProgress) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
}