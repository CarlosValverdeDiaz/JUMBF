package JUMBF;

import JPEGXTBox.SuperBox;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 *
 * @author carlos
 */
public class JUMBFUtils {
    private final String propertiesPath = "/home/carlos/NetBeansProjects/JUMBFLibrary/src/Common/";
    Properties properties = new Properties();
    public final String folderName;
    public final String mergeFileName;
    public final int ImageMaxWidth;
    public final int ImageWidth;
    public final int ImageMaxHeight;
    public final int ImageHeight;
    
    public JUMBFUtils() {
        try {
            properties.load(new BufferedReader(new FileReader(propertiesPath + "Properties.properties")));
        } catch (IOException ex) {
            Logger.getLogger(JUMBFUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        folderName = properties.getProperty("TestFolderPath");
        mergeFileName = properties.getProperty("MergeFile");
        ImageWidth = Integer.parseInt(properties.getProperty("ImageWidth"));
        ImageHeight = Integer.parseInt(properties.getProperty("ImageHeight"));
        ImageMaxWidth = Integer.parseInt(properties.getProperty("ImageMaxWidth"));
        ImageMaxHeight = Integer.parseInt(properties.getProperty("ImageMaxHeight"));
        
        System.out.println(folderName);
        System.out.println(mergeFileName);
        System.out.println(ImageWidth);
        System.out.println(ImageHeight);
        System.out.println(ImageMaxWidth);
        System.out.println(ImageMaxHeight);
    }
        
    private int mergeInt(int radix, int shift, int... a) {
        int result = 0;
        for(int i = a.length - 1; i >= 0; i--) {
            result += (int) (a[i] * Math.pow(radix, shift*(a.length - 1 - i)));
        }
        return result;
    }
    
    private int[] splitInt(int a, int numInt, int radix, int shift) {
        int[] result = new int[numInt];
        
        for(int i = 0; i < numInt; i++) {
            result[i] = ((a%(int) Math.pow(radix, (2 + i*shift))) - (a%(int) Math.pow(radix, (i*shift))))/(int) Math.pow(radix, i*shift);
        }
        
        return result;
    }
    
    public int getIntFromBytes(byte[] bytes) {
        int[] result = new int[4];
        StringBuilder sb = new StringBuilder();
        
        for(int i = 0; i < 4; i++) {
            result[i] = Byte.toUnsignedInt(bytes[i]);
        }
        
        return this.mergeInt(16, 2, result);
    }
    
    public byte[] allocateBytes(byte... bytes){
        return bytes;
    }
    
    public JUMBFSuperBox shapeJUMBFSuperBox(byte[] data) throws Exception {       
        StringBuilder sb = new StringBuilder();
        StringBuilder sb1 = new StringBuilder();
        StringBuilder sb2 = new StringBuilder();
        
        int boxLength;
        int boxType;
        long boxXLength;
        
        boolean isUUID = false;
        boolean isBIDB = false;
        
        String uuid;
        byte toggles;
        String label;
        int id;
        byte[] signature;
        
        int offset = 0;
        
        JUMBFContentBox jcb;
        
        boxLength = this.getIntFromBytes(allocateBytes(data[offset], data[offset + 1], data[offset + 2], data[offset + 3]));
        offset += 4;
        boxType= this.getIntFromBytes(allocateBytes(data[offset], data[offset + 1], data[offset + 2], data[offset + 3]));
        offset += 4;
        
        if (boxType == Common.Values.JUMBF_jumb) {
            System.out.println("Reading JUMBFSuperBox");
        } else {
            throw new Exception("Not JUMBFSuperBox " + boxType);
        }
        
        boxLength = this.getIntFromBytes(allocateBytes(data[offset], data[offset + 1], data[offset + 2], data[offset + 3]));
        offset += 4;
        boxType= this.getIntFromBytes(allocateBytes(data[offset], data[offset + 1], data[offset + 2], data[offset + 3]));
        offset += 4;
                
        if (boxType == Common.Values.JUMBF_jumd) {
            System.out.println("Reading JUMBFDescriptionBox");
        } else {
            throw new Exception("Not JUMBFDescriptionBox " + boxType);
        }
        
        //UUID        
        boxType = this.getIntFromBytes(allocateBytes(data[offset], data[offset + 1], data[offset + 2], data[offset + 3]));
        offset += 16;
        
        switch (boxType) {
            case Common.Values.JUMBF_xml:
                uuid = Common.Values.String_TYPE_XMLContentType;
                System.out.println("Reading XML Box");
                break;
            case Common.Values.JUMBF_json:
                uuid = Common.Values.String_TYPE_JSONContentType;
                System.out.println("Reading JSON Box");
                break;
            case Common.Values.JUMBF_jp2c:
                uuid = Common.Values.String_TYPE_ContiguousCodestream;
                System.out.println("Reading JP2C Box");
                break;
            case Common.Values.JUMBF_uuid:
                uuid = Common.Values.String_TYPE_UUIDContentType;
                System.out.println("Reading UUID Box");
                isUUID = true;
                break;
            case Common.Values.JUMBF_bidb:
                uuid = Common.Values.String_TYPE_EmbeddedFile;
                System.out.println("Reading BIDB Box");
                isBIDB = true;
                break;
            case Common.Values.JUMBF_link:
                uuid = Common.Values.String_TYPE_JLINK;
                System.out.println("Reading JLINK Box");
                break;
            default:
                throw new Exception("Invalid Type");
        }
        //TOGGLES        
        toggles = data[offset];
        offset++;
        
        JUMBFDescriptionBox descriptionBox = new JUMBFDescriptionBox(UUID.fromString(uuid), toggles);

        if((toggles | 0b0010) == toggles) {
            //LABEL
            while(data[offset] != '\0') {
                sb.append((char) data[offset]);
                offset++;
            }
            label = sb.toString();
            offset++;
            descriptionBox.setLabel(label);
            System.out.println(label);
        }
        if((toggles | 0b0100) == toggles) {
            //ID
            byte[] num = new byte[4];

            for(int i = 0; i < 4; i++) {
                num[i] = data[offset + i];
            }
            id = getIntFromBytes(num);
            offset += 4;
            descriptionBox.setId(id);
        }
        if((toggles | 0b1000) == toggles) {
            //Signature
            signature = new byte[32];
            for(int i = 0; i < signature.length; i++) {
                signature[i] = data[offset + i];
            }
            descriptionBox.setSignature(signature);
            offset += 32;
        }
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        if (UUID.fromString(uuid) == Common.Values.TYPE_UUIDContentType) {
            baos.write(data, offset + 8, offset + 24);
            UUID aux = UUID.fromString(baos.toString());
            
            baos.reset();
            
            baos.write(data, offset + 8, data.length - offset - 8);
            jcb = new JUMBFContentBox(baos.toByteArray(), UUID.fromString(uuid), aux);
        } else if (UUID.fromString(uuid) == Common.Values.TYPE_EmbeddedFile) {
            //boxLength = this.getIntFromBytes(allocateBytes(data[offset], data[offset + 1], data[offset + 2], data[offset + 3]));
            offset += 9;
            while(data[offset] != '\0') {
                sb1.append((char) data[offset]);
                offset++;
            }
            String mediaType = sb.toString();
            offset++;
            while(data[offset] != '\0') {
                sb1.append((char) data[offset]);
                offset++;
            }
            String fileName = sb.toString();
            offset++;
            baos.write(data, offset + 8, data.length - offset - 8);
            jcb = new JUMBFContentBox(baos.toByteArray(), UUID.fromString(uuid), null, (byte) 0b11, mediaType, fileName);
        } else {
            baos.write(data, offset + 8, data.length - offset - 8);
            jcb = new JUMBFContentBox(baos.toByteArray(), UUID.fromString(uuid));

        }
                
        JUMBFSuperBox box = new JUMBFSuperBox();
        box.setDescriptionBox(descriptionBox);
        box.addContentBox(jcb);

        
        box.addData(baos.toByteArray());
                
        return box;
    }
    
    public byte[] getBoxesFromFile(String s) throws Exception {
        byte[] data = Files.readAllBytes(Paths.get(s));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        
        int boxCounter = 0;
        int offset = 0;
        int le;
        short boxInstance = 0;
        int Z;
        
        for (int i = 0; i < data.length - 1; i++) {
            if (Byte.toUnsignedInt(data[i]) == 0xff 
               && Byte.toUnsignedInt(data[i + 1]) == 0xeb) {
                boxCounter++;
            } 
        }  
        
        for (int i = 0; i < boxCounter; i++) {
            offset = baos.size();
            while (offset < data.length) {
                if (Byte.toUnsignedInt(data[offset]) == 0xff 
                && Byte.toUnsignedInt(data[offset + 1]) == 0xeb) {
                   if (i == 0) {
                        offset += 2;
                        le = this.getIntFromBytes(allocateBytes((byte) 0, (byte) 0, data[offset], data[offset + 1]));
                        offset += 4;
                        boxInstance = (short) this.getIntFromBytes(allocateBytes((byte) 0, (byte) 0, data[offset], data[offset + 1]));
                        offset += 2;
                        Z = this.getIntFromBytes(allocateBytes(data[offset], data[offset + 1], data[offset + 2], data[offset + 3]));
                        offset += 4;
                        baos.write(data, offset, le - 10);
                    } else {
                        offset += 2;
                        le = this.getIntFromBytes(allocateBytes((byte) 0, (byte) 0, data[offset], data[offset + 1]));
                        offset += 4;
                        boxInstance = (short) this.getIntFromBytes(allocateBytes((byte) 0, (byte) 0, data[offset], data[offset + 1]));
                        offset += 2;
                        Z = this.getIntFromBytes(allocateBytes(data[offset], data[offset + 1], data[offset + 2], data[offset + 3]));
                        offset += 4 + 8;


                        baos.write(data, offset, le - 18);  
                    }
                    break;
                } else {
                    offset++;
                }
            }
        }
        return baos.toByteArray();
    }
    
    public BufferedImage getImageFromBox(Object input) throws Exception {
        if (input.getClass().equals(String.class)) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            baos.write(this.getBoxesFromFile((String) input));
            //System.out.println(box.boxToString());
            byte[] data = baos.toByteArray();

            JUMBFSuperBox box = shapeJUMBFSuperBox(data);
            

            JFrame frame = new JFrame();
            JLabel label = new JLabel(box.getDescriptionBox().getLabel());
            
            ImageIcon image = new ImageIcon(data);
            
            if (image.getIconHeight() > this.ImageMaxHeight || image.getIconWidth() > this.ImageMaxWidth) {
                if (image.getIconWidth() > image.getIconHeight()) {
                    int width = this.ImageWidth;
                    int height = image.getIconHeight()*width/image.getIconWidth();
                    Image aux = new ImageIcon(data).getImage();
                    Image scaled = aux.getScaledInstance(width, height , java.awt.Image.SCALE_SMOOTH);
                    label.setIcon(new ImageIcon(scaled));
                } else {
                    int height = this.ImageHeight;
                    int width = image.getIconWidth()*height/image.getIconHeight();
                    Image aux = new ImageIcon(data).getImage();
                    Image scaled = aux.getScaledInstance(width, height , java.awt.Image.SCALE_SMOOTH);
                    label.setIcon(new ImageIcon(scaled));

                }
            } else {
                label.setIcon(image);
            }
            
            frame.add(label);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();

            frame.setVisible(true);

            return null;
        } else if (input.getClass().equals(JUMBFSuperBox.class)) {
            JUMBFSuperBox box = (JUMBFSuperBox) input;
            
            byte[] data = box.getContentBox().getContentData();

            JFrame frame = new JFrame(box.getDescriptionBox().getLabel());
            JLabel label = new JLabel(box.getDescriptionBox().getLabel());
            
            ImageIcon image = new ImageIcon(data);
            
            if (image.getIconWidth() > this.ImageMaxWidth) {
                int width = this.ImageWidth;
                int height = image.getIconHeight()*width/image.getIconWidth();
                Image aux = new ImageIcon(data).getImage();
                Image scaled = aux.getScaledInstance(width, height , java.awt.Image.SCALE_SMOOTH);
                label.setIcon(new ImageIcon(scaled));
            } else {
                label.setIcon(image);
            }
            
            
            frame.add(label);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();

            frame.setVisible(true);
            return null;
        } else {
            return null;
        }
    }
    
    public void mergeJUMBF(String imageName, String fileName) throws Exception {
        byte[] image = Files.readAllBytes(Paths.get(imageName));
        byte[] jumbf = Files.readAllBytes(Paths.get(fileName));
        
        File file = new File(mergeFileName);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        
        int counter = 0;
        
        for(byte b:image){
            if (String.format("%x", Byte.toUnsignedInt(image[counter])).equals("ff") && String.format("%x", Byte.toUnsignedInt(image[counter + 1])).equals("d8")) {
                break;
            }
            counter++;
        }
        counter += 2;
        
        byte[] placeholder = new byte[counter];
        
        for(int i = 0; i < counter; i++) {
            placeholder[i] = image[i];
        }
        
        baos.write(placeholder);
        
        placeholder = new byte[jumbf.length];
        
        for(int i = 0; i < jumbf.length; i++) {
            placeholder[i] = jumbf[i];
        }
        
        baos.write(placeholder);
        baos.write(image, counter, image.length - counter);
                
        Files.write(file.toPath(), baos.toByteArray());
    }
    
    public void JUMBFToBox(JUMBFSuperBox superBox, String fileName, short boxInstance) throws Exception {        
        String s = new String(folderName + fileName + ".jumbf");
        File file = new File(s);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        
        SuperBox[] xtBoxes = this.getBoxes(superBox, boxInstance);
        
        for (SuperBox box:xtBoxes) {
            baos.write(box.getXTBoxData());
        }
        Files.write(file.toPath(), baos.toByteArray());
    }
    
    public SuperBox[] getBoxes(JUMBFSuperBox box, short boxInstance) throws Exception {
        long length = box.getXTBoxData().length;
        int localSpan;
        int offset = 0;
        int numBoxes = 0;
        boolean lastBox = false;
                
        SuperBox[] contentBoxes = null;
        ByteArrayOutputStream contentBuilder = new ByteArrayOutputStream();
            
        if (length > Math.pow(2, 32)) {
            //XLBox length
            localSpan = Common.Values.XT_BOX_MAX_DATA - Common.Values.XT_BOX_HEADER_LENGTH - 1 - 8;
            
            while (!lastBox) {
                numBoxes++;
                if (length < localSpan) {
                    lastBox = !lastBox;
                } else {
                    length -= localSpan;
                }
            }
            
            contentBoxes = new SuperBox[numBoxes];
            length = box.getXTBoxData().length;
            
            for (int i = 0; i < numBoxes; i++) {
                if (i != numBoxes - 1) {
                    contentBuilder.write(box.getXTBoxData(), offset, localSpan);
                    contentBoxes[i] = new SuperBox((short) (Common.Values.XT_BOX_MAX_DATA - 1), boxInstance, i, 1, box.getType(), contentBuilder.toByteArray(), length);
                    contentBuilder.reset();
                    offset += localSpan;
                } else {
                    contentBuilder.write(box.getXTBoxData(), offset, (int) box.getXTBoxData().length);
                    contentBoxes[i] = new SuperBox((short) (box.getXTBoxData().length - offset + Common.Values.XT_BOX_HEADER_LENGTH), boxInstance, i, 1, box.getType(), contentBuilder.toByteArray(), length);
                    contentBuilder.reset();
                }
                
            }
            
        } else {
            //LBox length
            localSpan = Common.Values.XT_BOX_MAX_DATA - Common.Values.XT_BOX_HEADER_LENGTH - 1;
            
            while (!lastBox) {
                numBoxes++;
                if (length < localSpan) {
                    lastBox = !lastBox;
                } else {
                    length -= localSpan;
                }
            }
            
            contentBoxes = new SuperBox[numBoxes];
            length = box.getXTBoxData().length;
            
            for (int i = 0; i < numBoxes; i++) {
                if (i < numBoxes - 1) {
                    contentBuilder.write(box.getXTBoxData(), offset, localSpan);
                    contentBoxes[i] = new SuperBox((short) (Common.Values.XT_BOX_MAX_DATA - 1), boxInstance, i, (int) length + 8, box.getType(), contentBuilder.toByteArray());
                    contentBuilder.reset();
                    offset += localSpan;
                    System.out.println("Box Instance: " + boxInstance + " Packet Sequence: " + i);

                } else {
                    contentBuilder.write(box.getXTBoxData(), offset, (int) box.getXTBoxData().length - offset);
                    contentBoxes[i] = new SuperBox((short) (box.getXTBoxData().length - offset + Common.Values.XT_BOX_HEADER_LENGTH), boxInstance, i, (int) length + 8, box.getType(), contentBuilder.toByteArray());
                    contentBuilder.reset();
                }
            }
        }       
        return contentBoxes;
    }
    
}