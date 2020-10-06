package tr.com.metasoft;

import java.io.File;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {

        //creating a File object for directory
        File directoryPath = new File("\\directoryhere");
        //list of all files and directories
        File filesList[] = directoryPath.listFiles();
        String path;
        String[] pathSub;
        for(File file : filesList) {
            path = String.valueOf(file.getAbsolutePath());
            pathSub = path.split("\\.");
            pathSub[0] = pathSub[0] + ".dcm";
            File myOutputFile = new File(pathSub[0]);
            File f = new File(file.getAbsolutePath());
            new Jpgdcm(f,myOutputFile);
        }
    }
}