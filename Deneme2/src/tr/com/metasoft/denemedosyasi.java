package tr.com.metasoft;

import org.dcm4che3.data.Attributes;
import org.dcm4che3.data.Tag;
import org.dcm4che3.data.VR;
import org.dcm4che3.io.DicomInputStream;
import org.dcm4che3.io.DicomOutputStream;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferUShort;
import java.io.*;
import java.nio.ByteBuffer;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import java.util.Date;
import java.util.GregorianCalendar;

public class denemedosyasi  {
   /* public static void main(String[] args) throws IOException {

        /*
        BufferedImage jpg = ImageIO.read(new File("C:\\Users\\gokcan\\Downloads\\dcm4che-5.22.2-bin\\dcm4che-5.22.2\\bin\\myjpg.jpg"));

        //Convert the image to a byte array
        DataBufferByte buff = (DataBufferByte) jpg.getRaster().getDataBuffer();
        byte[] data = buff.getData();
        ByteBuffer byteBuf = ByteBuffer.allocate(2 * data.length);
        int i = 0;
        while (data.length > i) {
            byteBuf.putShort(data[i]);
            i++;
        }


        ////////////////////
        BufferedImage bImage = ImageIO.read(new File("C:\\\\Users\\\\gokcan\\\\Downloads\\\\dcm4che-5.22.2-bin\\\\dcm4che-5.22.2\\\\bin\\\\myjpg.jpg"));
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ImageIO.write(bImage, "jpg", bos );
        byte [] data = bos.toByteArray();
        ByteBuffer byteBuf = ByteBuffer.allocate(2 * data.length);
        int i = 0;
        while (data.length > i) {
            byteBuf.putShort(data[i]);
            i++;
        }
        byte[] arr = new byte[byteBuf.remaining()];
        byteBuf.get(arr);
        ByteArrayInputStream bis = new ByteArrayInputStream(arr);
        BufferedImage bImage2 = ImageIO.read(bis);
        ImageIO.write(bImage2, "jpg", new File("outputmeta.jpg") );
        System.out.println("image created");

        ////////////////////

        //Copy a header

        DicomInputStream dis = new DicomInputStream(new File("C:\\Users\\gokcan\\Downloads\\dcm4che-5.22.2-bin\\dcm4che-5.22.2\\bin\\mynewdic.dcm"));
        Attributes meta = dis.readFileMetaInformation();
        Attributes attribs = dis.readDataset(-1, Tag.PixelData);
        dis.close();


        //Change the rows and columns
        File f =new File("deneme.dcm");
        f.createNewFile();
        DicomOutputStream dos = new DicomOutputStream(f);


        //dos.writeAttribute(Tag.StudyDate,VR.DA, 524320,524320);
        //dos(Tag.Rows, VR.US, jpg.getHeight());
        attribs.setInt(Tag.Rows, VR.US, bImage.getHeight());
        attribs.setInt(Tag.Columns, VR.US, bImage.getWidth());
        System.out.println(byteBuf.array().length);
        dos.writeAttribute(Tag.PatientName,VR.PN,"ondeneme dosyasi".getBytes());
        //Attributes attribs = new Attributes();

        //Write the file
        attribs.setBytes(Tag.PixelData, VR.OW, byteBuf.array());
        DicomOutputStream dcmo = new DicomOutputStream(new File("myDicom5.dcm"));
        dcmo.writeFileMetaInformation(meta);
        attribs.writeTo(dcmo);
        dcmo.close();


    }
*/

    }
