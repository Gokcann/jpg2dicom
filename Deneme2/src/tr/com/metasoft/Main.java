package tr.com.metasoft;

import org.dcm4che3.tool.getscu.GetSCU;

import java.io.IOException;

public class Main {
    //private static DcmGonder gonder;


    public static void main(String[] args) throws IOException {

//        //creating a File object for directory
//        File directoryPath = new File("\\directoryhere");
//        //list of all files and directories
//        File filesList[] = directoryPath.listFiles();
//        String path;
//        String[] pathSub;
//        for(File file : filesList) {
//            path = String.valueOf(file.getAbsolutePath());
//            pathSub = path.split("\\.");
//            pathSub[0] = pathSub[0] + ".dcm";
//            File myOutputFile = new File(pathSub[0]);
//            File f = new File(file.getAbsolutePath());
//            new Jpgdcm(f,myOutputFile);
//        }


        String[] command = new String[3];
        //command[0] = "storescu";
        command[0] = "-c";
        command[1] = "DCM4CHEE@192.168.12.113:11112";
        command[2] = "D:\\a\\asd2.dcm";
        //send dcm files for dcm4chee
//      DcmGonder gonder = new DcmGonder();
//      gonder.DcmSend(command);

        //StoreSCU.main(command);


        String[] command2 = new String[1];
        //command2[0] = "https://192.168.12.44:8080/dcm4chee-arc/aets/DCM4CHEE/rs/studies/1.2.826.0.1.3680043.8.784.55013.5501357395862";
            command2[0] = "http://192.168.12.44:8080/dcm4chee-arc/aets/DCM4CHEE/rs/studies/2.25.136291433045103039131152633540194462488/";
        //WadoRS.main(command2);

        //WadoRS.main(command2);

        //get dicom file
        String[] command3 = new String[6];
        command3[0] = "-c";
        command3[1] = "DCM4CHEE@192.168.12.44:11112";
        command3[2] = "-m";
        command3[3] = "StudyInstanceUID=2.25.136291433045103039131152633540194462488";
        command3[4] = "--directory";
        command3[5] = "D:\\a\\a";

        GetSCU.main(command3);

        //findscu -c DCM4CHEE@192.168.12.44:11112 -M MWL -- C:\Users\Metasoft\Downloads\dcm4che-5.22.5\etc\findscu\mwl.xml
        //findscu -c DCM4CHEE@192.168.12.44:11112 -M MWL --out-file mwl.xml
        String[] command4 = new String[8];
        command4[0] = "-c";
        command4[1] = "DCM4CHEE@192.168.12.44:11112";
        command4[2] = "-M";
        command4[3] = "MWL";
        command4[4] = "--out-file";
        command4[5] = "mwl.xml";
        command4[6] = "--out-file";
        command4[7] = "aa.xml";

        //FindSCU.main(command4);

    }
}