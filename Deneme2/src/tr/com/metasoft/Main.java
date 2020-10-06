package tr.com.metasoft;

import java.io.File;
import java.io.IOException;

public class Main {
/*
    public  Attributes DicomOlustur(Attributes attrs,String imagePath){
        return null;
    }


 */

    public static void main(String[] args) throws IOException {


        //Jpgdcm classina iki adet degisken gonderiyoruz bunlarin ilki jpten dicoma aktarilacak olan jpg goruntusu
        //digeride olusturalacak olan dicom dosyasinin pathi ve adi
        //File f = new File("C:\\Users\\CAGDAS\\Desktop\\deneme\\20200316-09062104.jpg");
        //File myOutputFile = new File("C:\\Users\\CAGDAS\\Desktop\\deneme\\20200316-09062104.dcm");
        //new Jpgdcm(f,myOutputFile);

        //Creating a File object for directory
        File directoryPath = new File("C:\\Users\\CAGDAS\\Desktop\\deneme");
        //List of all files and directories
        File filesList[] = directoryPath.listFiles();
        String path;
        String name;
        String[] pathSub;
        String[] nameSub;
        for(File file : filesList) {
            path = String.valueOf(file.getAbsolutePath());
            pathSub = path.split("\\.");
            pathSub[0] = pathSub[0] + ".dcm";
            File myOutputFile = new File(pathSub[0]);
            File f = new File(file.getAbsolutePath());
            new Jpgdcm(f,myOutputFile);
        }


/*
        BufferedImage jpg = ImageIO.read(new File("C:\\Users\\gokcan\\Desktop\\denemedosya.jpg"));

        //Convert the image to a byte array
        //DataBufferUShort buff = (DataBufferUShort) jpg.getData().getDataBuffer();
        DataBufferByte buff = (DataBufferByte) jpg.getData().getDataBuffer();
        byte[] data = buff.getData();
        ByteBuffer byteBuf = ByteBuffer.allocate(2*data.length);
        int i = 0;
        while (data.length > i) {
            byteBuf.putShort(data[i]);
            i++;
        }



 */
        /*
        File f = new File("C:\\Users\\gokcan\\Desktop\\abc.jpg");
        BufferedImage jpg = ImageIO.read(new File("C:\\Users\\gokcan\\Desktop\\abc.jpg"));
        InputStream in = null;
        try {
            in = new FileInputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //my byte array
        byte[] buff = new byte[8000];
        int bytesRead = 0;
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        int x =0;
        while(true) {

            x++;
            try {
                //
                if (!((bytesRead = in.read(buff)) != -1)) break;
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println(x);
            bao.write(buff, 0, bytesRead);
        }
        byte[] data = bao.toByteArray();
/////////////////////////////
        ByteArrayInputStream bin = new ByteArrayInputStream(data);
        BufferedImage bi = ImageIO.read(bin);
        //Copy a header
        File f1 =new File("deneme.dcm");
        f.createNewFile();
        DicomOutputStream dos = new DicomOutputStream(f1);
        Attributes attribs = new Attributes();
        attribs.setString(Tag.PatientName,VR.PN,"calis");
        attribs.setString(Tag.SOPInstanceUID,VR.PN,"calis");
        attribs.setInt(Tag.Rows, VR.US, jpg.getHeight());
        attribs.setInt(Tag.Columns, VR.US, jpg.getWidth());
        attribs.setBytes(Tag.PixelData, VR.OW, data);
        attribs.writeTo(dos);
        byte[] h = ByteBuffer.allocate(4).putInt(jpg.getHeight()).array();
        byte[] w = ByteBuffer.allocate(4).putInt(jpg.getWidth()).array();

        dos.writeAttribute(Tag.PatientName,VR.PN,"calis32323".getBytes());
        dos.writeAttribute(Tag.SOPInstanceUID,VR.PN,"calis".getBytes());
        dos.writeAttribute(Tag.Rows, VR.US, h );
        dos.writeAttribute(Tag.Columns, VR.US, w);
        dos.writeAttribute(Tag.PixelData, VR.OW, data);
        //dos.writeHeader(Tag.PixelData, VR.OB, -1);

        dos.flush();
        dos.close();


         */
//        Attributes meta = dis.readFileMetaInformation();
//        Attributes attribs = dis.readDataset(-1, Tag.PixelData);
//        dis.close();
//
//        //Change the rows and columns
//        attribs.setInt(Tag.Rows, VR.US, jpg.getHeight());
//        attribs.setInt(Tag.Columns, VR.US, jpg.getWidth());
//        System.out.println(byteBuf.array().length);
//        Attributes attribs = new Attributes();
//
//        //Write the file
//        attribs.setBytes(Tag.PixelData, VR.OW, data);
//        DicomOutputStream dcmo = new DicomOutputStream(new File("myDicom.dcm"));
//        dcmo.writeFileMetaInformation(meta);
//        attribs.writeTo(dcmo);
//        dcmo.close();
    }
}