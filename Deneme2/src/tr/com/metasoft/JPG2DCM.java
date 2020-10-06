package tr.com.metasoft;
//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

//package org.dcm4che3.tool.jpg2dcm;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.MessageFormat;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.dcm4che3.data.Attributes;
import org.dcm4che3.data.ElementDictionary;
import org.dcm4che3.data.VR;
import org.dcm4che3.imageio.codec.XPEGParser;
import org.dcm4che3.imageio.codec.jpeg.JPEGParser;
import org.dcm4che3.imageio.codec.mp4.MP4Parser;
import org.dcm4che3.imageio.codec.mpeg.MPEG2Parser;
import org.dcm4che3.io.DicomOutputStream;
import org.dcm4che3.io.SAXReader;
import org.dcm4che3.tool.common.CLIUtils;
import org.dcm4che3.util.StreamUtils;
import org.dcm4che3.util.UIDUtils;
import org.xml.sax.SAXException;
/*
public class Jpg2Dcm {
    private static final int BUFFER_SIZE = 8162;
    private static final ElementDictionary DICT = ElementDictionary.getStandardElementDictionary();
    private static final ResourceBundle rb = ResourceBundle.getBundle("org.dcm4che3.tool.jpg2dcm.messages");
    private static final int[] IUID_TAGS = new int[]{2097165, 2097166, 524312};
    private static final int[] TYPE2_TAGS = new int[]{524323, 524339};
    private boolean noAPPn;
    private boolean photo;
    private Attributes staticMetadata = new Attributes();
    private byte[] buf = new byte[8162];

    public Jpg2Dcm() {
    }

    private void setNoAPPn(boolean noAPPn) {
        this.noAPPn = noAPPn;
    }

    private void setPhoto(boolean photo) {
        this.photo = photo;
    }

    public static void main(String[] args) {
        try {
            CommandLine cl = parseComandLine(args);
            org.dcm4che3.tool.jpg2dcm.Jpg2Dcm main = new org.dcm4che3.tool.jpg2dcm.Jpg2Dcm();
            List<String> argList = cl.getArgList();
            int argc = argList.size();
            if (argc < 2) {
                throw new ParseException(rb.getString("missing"));
            }

            File dest = new File((String)argList.get(argc - 1));
            if ((argc > 2 || (new File((String)argList.get(0))).isDirectory()) && !dest.isDirectory()) {
                throw new ParseException(MessageFormat.format(rb.getString("nodestdir"), dest));
            }

            main.setNoAPPn(cl.hasOption("no-app"));
            main.setPhoto(cl.hasOption("xc"));
            createStaticMetadata(cl, main.staticMetadata);
            main.convert(cl.getArgList());
        } catch (ParseException var6) {
            System.err.println("jpg2dcm: " + var6.getMessage());
            System.err.println(rb.getString("try"));
            System.exit(2);
        } catch (Exception var7) {
            System.err.println("jpg2dcm: " + var7.getMessage());
            var7.printStackTrace();
            System.exit(2);
        }

    }

    private static CommandLine parseComandLine(String[] args) throws ParseException {
        Options opts = new Options();
        CLIUtils.addCommonOptions(opts);
        opts.addOption(Option.builder("m").hasArgs().argName("[seq/]attr=value").desc(rb.getString("metadata")).build());
        opts.addOption(Option.builder("f").hasArg().argName("xml-file").desc(rb.getString("file")).build());
        opts.addOption(Option.builder().longOpt("xc").hasArg(false).desc(rb.getString("xc")).build());
        opts.addOption(Option.builder().longOpt("no-app").hasArg(false).desc(rb.getString("no-app")).build());
        return CLIUtils.parseComandLine(args, opts, rb, org.dcm4che3.tool.jpg2dcm.Jpg2Dcm.class);
    }

    private static void createStaticMetadata(CommandLine cl, Attributes staticMetadata) throws Exception {
        if (cl.hasOption("f")) {
            SAXReader.parse(cl.getOptionValue("f"), staticMetadata);
        }

        CLIUtils.addAttributes(staticMetadata, cl.getOptionValues("m"));
        supplementMissingUIDs(staticMetadata);
        supplementMissingValue(staticMetadata, 2097169, "999");
        supplementMissingValue(staticMetadata, 2097171, "1");
        supplementType2Tags(staticMetadata);
    }

    private void convert(List<String> args) throws Exception {
        int argsSize = args.size();
        Path destPath = Paths.get((String)args.get(argsSize - 1));
        Iterator var4 = args.subList(0, argsSize - 1).iterator();

        while(var4.hasNext()) {
            String src = (String)var4.next();
            Path srcPath = Paths.get(src);
            if (Files.isDirectory(srcPath, new LinkOption[0])) {
                Files.walkFileTree(srcPath, new org.dcm4che3.tool.jpg2dcm.Jpg2Dcm.Jpg2DcmFileVisitor(srcPath, destPath));
            } else if (Files.isDirectory(destPath, new LinkOption[0])) {
                this.convert(srcPath, destPath.resolve(srcPath.getFileName() + ".dcm"));
            } else {
                this.convert(srcPath, destPath);
            }
        }

    }

    private void convert(Path srcFilePath, Path destFilePath) throws Exception {
        org.dcm4che3.tool.jpg2dcm.Jpg2Dcm.ContentType fileType = org.dcm4che3.tool.jpg2dcm.Jpg2Dcm.ContentType.probe(srcFilePath);
        Attributes fileMetadata = SAXReader.parse(StreamUtils.openFileOrURL(fileType.getSampleMetadataFile(this.photo)));
        fileMetadata.addAll(this.staticMetadata);
        supplementMissingValue(fileMetadata, 524310, fileType.getSOPClassUID(this.photo));
        SeekableByteChannel channel = Files.newByteChannel(srcFilePath);

        try {
            DicomOutputStream dos = new DicomOutputStream(destFilePath.toFile());

            try {
                XPEGParser parser = fileType.newParser(channel);
                parser.getAttributes(fileMetadata);
                dos.writeDataset(fileMetadata.createFileMetaInformation(parser.getTransferSyntaxUID()), fileMetadata);
                dos.writeHeader(2145386512, VR.OB, -1);
                dos.writeHeader(-73728, (VR)null, 0);
                if (this.noAPPn && parser.getPositionAfterAPPSegments() > 0L) {
                    this.copyPixelData(channel, parser.getPositionAfterAPPSegments(), dos, -1, -40);
                } else {
                    this.copyPixelData(channel, parser.getCodeStreamPosition(), dos);
                }

                dos.writeHeader(-73507, (VR)null, 0);
            } catch (Throwable var11) {
                try {
                    dos.close();
                } catch (Throwable var10) {
                    var11.addSuppressed(var10);
                }

                throw var11;
            }

            dos.close();
        } catch (Throwable var12) {
            if (channel != null) {
                try {
                    channel.close();
                } catch (Throwable var9) {
                    var12.addSuppressed(var9);
                }
            }

            throw var12;
        }

        if (channel != null) {
            channel.close();
        }

        System.out.println(MessageFormat.format(rb.getString("converted"), srcFilePath, destFilePath));
    }

    private void copyPixelData(SeekableByteChannel channel, long position, DicomOutputStream dos, byte... prefix) throws IOException {
        long codeStreamSize = channel.size() - position + (long)prefix.length;
        dos.writeHeader(-73728, (VR)null, (int)(codeStreamSize + 1L & -2L));
        dos.write(prefix);
        channel.position(position);
        this.copy(channel, dos);
        if ((codeStreamSize & 1L) != 0L) {
            dos.write(0);
        }

    }

    private void copy(ByteChannel in, OutputStream out) throws IOException {
        ByteBuffer bb = ByteBuffer.wrap(this.buf);

        int read;
        while((read = in.read(bb)) > 0) {
            out.write(this.buf, 0, read);
            bb.clear();
        }

    }

    private static void supplementMissingUIDs(Attributes metadata) {
        int[] var1 = IUID_TAGS;
        int var2 = var1.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            int tag = var1[var3];
            if (!metadata.containsValue(tag)) {
                metadata.setString(tag, VR.UI, UIDUtils.createUID());
            }
        }

    }

    private static void supplementMissingValue(Attributes metadata, int tag, String value) {
        if (!metadata.containsValue(tag)) {
            metadata.setString(tag, DICT.vrOf(tag), value);
        }

    }

    private static void supplementType2Tags(Attributes metadata) {
        int[] var1 = TYPE2_TAGS;
        int var2 = var1.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            int tag = var1[var3];
            if (!metadata.contains(tag)) {
                metadata.setNull(tag, DICT.vrOf(tag));
            }
        }

    }

    class Jpg2DcmFileVisitor extends SimpleFileVisitor<Path> {
        private Path srcPath;
        private Path destPath;

        Jpg2DcmFileVisitor(Path srcPath, Path destPath) {
            this.srcPath = srcPath;
            this.destPath = destPath;
        }

        public FileVisitResult visitFile(Path srcFilePath, BasicFileAttributes attrs) throws IOException {
            Path destFilePath = this.resolveDestFilePath(srcFilePath);
            if (!Files.isDirectory(destFilePath, new LinkOption[0])) {
                Files.createDirectories(destFilePath);
            }

            try {
                org.dcm4che3.tool.jpg2dcm.Jpg2Dcm.this.convert(srcFilePath, destFilePath.resolve(srcFilePath.getFileName() + ".dcm"));
            } catch (ParserConfigurationException | SAXException var5) {
                System.out.println(MessageFormat.format(org.dcm4che3.tool.jpg2dcm.Jpg2Dcm.rb.getString("failed"), srcFilePath, var5.getMessage()));
                var5.printStackTrace(System.out);
                return FileVisitResult.TERMINATE;
            } catch (Exception var6) {
                System.out.println(MessageFormat.format(org.dcm4che3.tool.jpg2dcm.Jpg2Dcm.rb.getString("failed"), srcFilePath, var6.getMessage()));
                var6.printStackTrace(System.out);
            }

            return FileVisitResult.CONTINUE;
        }

        private Path resolveDestFilePath(Path srcFilePath) {
            int srcPathNameCount = this.srcPath.getNameCount();
            int srcFilePathNameCount = srcFilePath.getNameCount() - 1;
            return srcPathNameCount == srcFilePathNameCount ? this.destPath : this.destPath.resolve(srcFilePath.subpath(srcPathNameCount, srcFilePathNameCount));
        }
    }

    private static enum ContentType {
        IMAGE_JPEG {
            String getSampleMetadataFile(boolean photo) {
                return photo ? "resource:vlPhotographicImageMetadata.xml" : "resource:secondaryCaptureImageMetadata.xml";
            }

            String getSOPClassUID(boolean photo) {
                return photo ? "1.2.840.10008.5.1.4.1.1.77.1.4" : "1.2.840.10008.5.1.4.1.1.7";
            }

            XPEGParser newParser(SeekableByteChannel channel) throws IOException {
                return new JPEGParser(channel);
            }
        },
        VIDEO_MPEG {
            XPEGParser newParser(SeekableByteChannel channel) throws IOException {
                return new MPEG2Parser(channel);
            }
        },
        VIDEO_MP4 {
            XPEGParser newParser(SeekableByteChannel channel) throws IOException {
                return new MP4Parser(channel);
            }
        };

        private ContentType() {
        }

        static org.dcm4che3.tool.jpg2dcm.Jpg2Dcm.ContentType probe(Path path) throws IOException {
            String type = Files.probeContentType(path);
            if (type == null) {
                throw new IllegalArgumentException(MessageFormat.format(org.dcm4che3.tool.jpg2dcm.Jpg2Dcm.rb.getString("unsupported-file-ext"), path));
            } else {
                String var2 = type.toLowerCase();
                byte var3 = -1;
                switch(var2.hashCode()) {
                    case -1662382439:
                        if (var2.equals("video/mpeg")) {
                            var3 = 2;
                        }
                        break;
                    case -1487394660:
                        if (var2.equals("image/jpeg")) {
                            var3 = 0;
                        }
                        break;
                    case -879264520:
                        if (var2.equals("image/jp2")) {
                            var3 = 1;
                        }
                        break;
                    case -107252314:
                        if (var2.equals("video/quicktime")) {
                            var3 = 4;
                        }
                        break;
                    case 1331848029:
                        if (var2.equals("video/mp4")) {
                            var3 = 3;
                        }
                }

                switch(var3) {
                    case 0:
                    case 1:
                        return IMAGE_JPEG;
                    case 2:
                        return VIDEO_MPEG;
                    case 3:
                    case 4:
                        return VIDEO_MP4;
                    default:
                        throw new IllegalArgumentException(MessageFormat.format(org.dcm4che3.tool.jpg2dcm.Jpg2Dcm.rb.getString("unsupported-content-type"), type, path));
                }
            }
        }

        String getSampleMetadataFile(boolean photo) {
            return "resource:vlPhotographicImageMetadata.xml";
        }

        String getSOPClassUID(boolean photo) {
            return "1.2.840.10008.5.1.4.1.1.77.1.4.1";
        }

        abstract XPEGParser newParser(SeekableByteChannel var1) throws IOException;
    }
}
*/