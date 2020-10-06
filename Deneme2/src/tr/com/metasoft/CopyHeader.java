/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

//package org.dicom;
package tr.com.metasoft;
import org.dcm4che3.data.Attributes;
import org.dcm4che3.data.Tag;
import org.dcm4che3.data.UID;
import org.dcm4che3.data.VR;
import org.dcm4che3.io.DicomOutputStream;
import org.dcm4che3.util.UIDUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.Date;

/**
 * Create jpeg to dcm
 * @author Gokcan
 */
class Jpgdcm {
    private static int  jpgLen;
    private String transferSyntax = UID.JPEGLossless;
    public  Jpgdcm(File file, File fileOutput) {
        try {
            //Mainden gelen jpg dosyasi jpegImage ismiyle bufferlaniyor
            jpgLen = (int) file.length();
            BufferedImage jpegImage = ImageIO.read(file);
            /*
             * We open a try block and then read our Jpeg into a BufferedImage through ImageIO.read() method. If this results in an invalid image so we may throw a new exception.
             * Else, we’ve got a valid image and therefore valuable information about it.
             * The BufferedImage class has a lot of useful methods for retrieving image data, then let’s save the number of color components (samples per pixel) of our image, the bits
             *  per pixel and the bits allocated:

             */
            //exception kontrolu
            if (jpegImage == null) throw new Exception("Invalid file.");

            /* We open a try block and then read our Jpeg into a BufferedImage through ImageIO.read() method.
             * If this results in an invalid image so we may throw a new exception.
             * Else, we’ve got a valid image and therefore valuable information about it.
             * The BufferedImage class has a lot of useful methods for retrieving image data,
             * then let’s save the number of color components (samples per pixel) of our image,
             * the bits per pixel and the bits allocated: */

            //jpg dosyamizin ozellikleri aliniyor
            int colorComponents = jpegImage.getColorModel().getNumColorComponents();
            int bitsPerPixel = jpegImage.getColorModel().getPixelSize();
            int bitsAllocated = (bitsPerPixel / colorComponents);
            int samplesPerPixel = colorComponents;

            /*It’s time to start building our Dicom dataset:*/
            //** Dicom dosyasinin verilerini (metadata) olusturmaya basliyoruz... **//
            Attributes dicom = new Attributes();
            dicom.setString(Tag.SpecificCharacterSet, VR.CS, "ISO_IR 100");
            dicom.setString(Tag.PhotometricInterpretation, VR.CS, samplesPerPixel == 3 ? "YBR_FULL_422" : "MONOCHROME2");

            /*The first line creates a new basic Dicom object defined by dcm4che2 toolkit.
	        *The next one puts header information for Specific Character Set: ISO_IR 100 – it’s the same for ISO-8859-1 – the code for Latin alphabet.
	        *Finally, the last line puts header information for photometric interpretation (read with or without colors).
	        *So if our image has samples per pixel equals to 3, it has colors (YBR_FULL_422), else it’s a grayscale image (MONOCHROME2).
	        The following lines add integer values to our Dicom header. Note that all of them comes from BufferedImage methods.
	        These values are mandatory when encapsulating. For more information you can check Part 3.5 of Dicom Standard. */

            //Bu datalar jpg dosyasinin propertylerine gore sekilleniyor (orn: jpg dosyasinin yuksekligi, genisligi vs.)
            dicom.setInt(Tag.SamplesPerPixel, VR.US, samplesPerPixel);
            dicom.setInt(Tag.Rows, VR.US, jpegImage.getHeight());
            dicom.setInt(Tag.Columns, VR.US, jpegImage.getWidth());
            dicom.setInt(Tag.BitsAllocated, VR.US, bitsAllocated);
            dicom.setInt(Tag.BitsStored, VR.US, bitsAllocated);
            dicom.setInt(Tag.HighBit, VR.US, bitsAllocated-1);
            dicom.setInt(Tag.PixelRepresentation, VR.US, 0);

            /*Also, our Dicom header needs information about date and time of creation:*/
            dicom.setDate(Tag.InstanceCreationDate, VR.DA, new Date());
            dicom.setDate(Tag.InstanceCreationTime, VR.TM, new Date());
            /* Every Dicom file has a unique identifier.
             * Here we’re generating study, series and Sop instances UIDs.
             * You may want to modify these values, but you should to care about their uniqueness.
             */
            /**
             * ^^^^^^^^^^^^^^^^^^^^^^^^^^^^!!!!!!!ONEMLİ!!!!!!!!^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
             *
             * Burasi cok onemli: SOPCLassUID ile MediaStorageSOPClassUID ayni olmali!!!!!
             * Ayni sekilde SOPInstanceUID ile MediaStorageSOPInstanceUID ayni olmali!!!!!
             *
             * ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
             */
            dicom.setString(Tag.StudyInstanceUID, VR.UI, "1.2.840.20200805.162417.198.0.192168.010.10.20418");
            dicom.setString(Tag.SeriesInstanceUID, VR.UI, "1.2.840.20200805.162417.204.0.192168.010.10.20421");
            dicom.setString(Tag.SOPInstanceUID, VR.UI, "2.25.119224185080231306154911093447145543566");
            //dicom.setString(Tag.StudyInstanceUID, VR.UI, UIDUtils.createUID());
            //dicom.setString(Tag.SeriesInstanceUID, VR.UI, UIDUtils.createUID());
            //dicom.setString(Tag.SOPInstanceUID, VR.UI, UIDUtils.createUID());
            dicom.setInt(Tag.Rows, VR.US, jpegImage.getHeight());
            dicom.setInt(Tag.Columns, VR.US, jpegImage.getWidth());

            //Bu datalar disardan gelecek olan datalar (orn: tc kimlik numarasi, kisinin adi soyadi, study date/time vs..)
            dicom.setString(Tag.PatientID, VR.LO,"12345678900");
            dicom.setString(Tag.PatientName,VR.PN,"lionel messi");
            dicom.setString(Tag.PatientBirthDate, VR.DA, "19520812");
            dicom.setString(Tag.PatientSex, VR.CS, "M");
            dicom.setString(Tag.StudyDate, VR.DA, "20200612");
            dicom.setString(Tag.StudyTime, VR.TM, "103009.000000");
            dicom.setString(Tag.ReferringPhysicianName, VR.PN, "eskisehir2ADSM");
            dicom.setString(Tag.StudyID, VR.SH, "10");
            dicom.setString(Tag.AccessionNumber, VR.SH, "1498305");
            dicom.setString(Tag.SeriesNumber, VR.IS, "1");
            dicom.setString(Tag.Manufacturer, VR.LO, "eskisehirdeneme");
            dicom.setString(Tag.InstanceNumber, VR.IS, "2");
            dicom.setString(Tag.Modality, VR.CS, "XC");
            dicom.setString(Tag.ImageType, VR.CS, "ORIGINAL/PRIMARY//0001");
            dicom.setString(Tag.LossyImageCompression, VR.CS);
            dicom.setString(Tag.PatientOrientation, VR.CS,"LE");
            dicom.setString(Tag.QueryRetrieveLevel, VR.CS,"STUDY");
            //dicom.setString(Tag.AcquisitionContextSequence, VR.SQ, "0");

            //new attributes  // FileMetaInformationVersion arastirilacak string degil binary veri aliyor...
            //dicom.setString(Tag.FileMetaInformationVersion, VR.OB,"AAE=");
            dicom.setInt(Tag.FileMetaInformationVersion, VR.OB, 00020002);
            dicom.setString(Tag.SOPClassUID, VR.UI, "1.2.840.10008.5.1.4.1.1.7");
            dicom.setString(Tag.SeriesDate, VR.DA, "20200805");
            dicom.setString(Tag.ContentDate, VR.DA, "20200805");
            dicom.setString(Tag.ContentTime, VR.TM, "162417.174000");
            dicom.setString(Tag.InstitutionName, VR.LO, "BALGATADSM");
            dicom.setString(Tag.PlanarConfiguration, VR.US, "0");






            /*
            DataBufferByte buff = (DataBufferByte) jpegImage.getData().getDataBuffer();
            byte[] data = buff.getData();
            dicom.setBytes(Tag.PixelData, VR.OB, data);
            dicom.writeTo(dos);
             */

            //dicom.setString(Tag.PerformingPhysicianName, VR.PN, "Jean");
            //dicom.setString(Tag.AdmittingDiagnosesDescription, VR.LO, "CHU");
            //Sequence seq= dicom.newSequence(Tag.AnatomicRegionSequence,0);
            //Attributes dicom2 = new Attributes();

            //dicom2.setString(Tag.CodingSchemeDesignator, VR.SH, "SRT");
            //dicom2.setString(Tag.CodeValue, VR.SH, "T-AA000");
            //dicom2.setString(Tag.CodeMeaning, VR.LO, "Eye");
            //seq.add(dicom2);





            /*Our Dicom header is almost done.
             * The following command initiates Dicom metafile information considering JPEGBaseline1 as transfer syntax.
             * This means this file has Jpeg data encapsulated instead common medical image pixel data.
             * The most common Jpeg files use a subset of the Jpeg standard called baseline Jpeg.
             * A baseline Jpeg file contains a single image compressed with the baseline discrete cosine transformation (DCT) and Huffman encoding.
             */
            // dicom.initFileMetaInformation(UID.JPEGBaseline1);
            /*After initiate the header we can open an output stream for saving our Dicom dataset as follows:*/

            Attributes fmi = new Attributes();

            /**
             * .createUID olanlari dcm4che kendisi otomatik yaratiyor bunun yerine bu veriler
             * "Media Storage Standard SOP Classes" tarafindan atanmali yani buyuk ihtimalle modalite cihazindan gelecek bu veri.
             */
            //fmi.setString(Tag.ImplementationVersionName, VR.SH, "dcm4che-5.22.2");
            //fmi.setString(Tag.ImplementationClassUID, VR.UI, "1.2.40.0.13.1.3");
            //fmi.setString(Tag.TransferSyntaxUID, VR.UI, "1.2.840.10008.1.2.1");
            fmi.setString(Tag.MediaStorageSOPClassUID, VR.UI, "1.2.840.10008.5.1.4.1.1.7");
            fmi.setString(Tag.MediaStorageSOPInstanceUID, VR.UI, "2.25.119224185080231306154911093447145543566");
            //fmi.setString(Tag.FileMetaInformationVersion, VR.OB, "1");
            //fmi.setInt(Tag.FileMetaInformationGroupLength, VR.UL, dicom.size()+fmi.size());
            fmi.setString(Tag.ImplementationVersionName, VR.SH, "DCM4CHE3");
            fmi.setString(Tag.ImplementationClassUID, VR.UI, UIDUtils.createUID());
            fmi.setString(Tag.TransferSyntaxUID, VR.UI, transferSyntax);
            //fmi.setString(Tag.MediaStorageSOPClassUID, VR.UI, transferSyntax);
            //fmi.setString(Tag.MediaStorageSOPInstanceUID, VR.UI,UIDUtils.createUID());
            fmi.setString(Tag.FileMetaInformationVersion, VR.OB, "1");
            fmi.setInt(Tag.FileMetaInformationGroupLength, VR.UL, dicom.size()+fmi.size());

            DicomOutputStream dos = new DicomOutputStream(fileOutput);
            dos.writeDataset(fmi, dicom);
            dos.writeHeader(Tag.PixelData, VR.OB, -1);

            //ekleme
            //dicom.writeTo(dos);
            /*
             * The Item Tag (FFFE,E000) is followed by a 4 byte item length field encoding the explicit number of bytes of the item.
             * The first item in the sequence of items before the encoded pixel data stream shall be a basic item with length equals to zero:
             */
            dos.writeHeader(Tag.Item, null, 0);
            /*The next Item then keeps the length of our Jpeg file. */
	     /* 
	     According to Gunter from dcm4che team we have to take care that  
	     the pixel data fragment length containing the JPEG stream has  
	     an even length. 
	     */

            dos.writeHeader(Tag.Item, null, (jpgLen+1)&~1);
            /* Now all we have to do is to fill this item with bytes taken from our Jpeg file*/
            FileInputStream fis = new FileInputStream(file);
            BufferedInputStream bis = new BufferedInputStream(fis);
            DataInputStream dis = new DataInputStream(bis);

            byte[] buffer = new byte[65536];
            int b;
            while ((b = dis.read(buffer)) > 0) {
                dos.write(buffer, 0, b);
            }
            /*Finally, the Dicom Standard tells that we have to put a last Tag:
             * a Sequence Delimiter Item (FFFE,E0DD) with length equals to zero.*/


            if ((jpgLen&1) != 0) dos.write(0);
            dos.writeHeader(Tag.SequenceDelimitationItem, null, 0);
            dos.close();




        } catch (Exception e) {

            e.printStackTrace();
        }

    }


}