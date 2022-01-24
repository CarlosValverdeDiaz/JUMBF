package JUMBF;

import JPEGXTBox.SuperBox;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.UUID;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 *
 * @author carlos
 */
public class JUMBFUtils {
    protected static class dataStructure {
        private int offset;
        private int type;
        private char[] charArray;
        
        protected dataStructure() {
            
        }
        
        protected int getOffset() {
            return this.offset;
        }
        
        protected int getType() {
            return this.type;
        }
        
        protected char[] getCharArray() {
            return this.charArray;
        }
        
        private void setOffset(int offset) {
            this.offset = offset;
        }
        
        private void setType(int type) {
            this.type = type;
        }
        
        private void setCharArray(char[] charArray) {
            this.charArray = charArray;
        }
    }
    
    private dataStructure parseSegment(Object... param) {
        byte[] data = (byte[]) param[0];
        int dataLength = (int) param[1];
        int offset = (int) param[2];
        
        dataStructure ds = new dataStructure();
        
        char[] buffer = new char[dataLength];
        int boxType = 0;

        for (int i = 0; i < dataLength; i++) {
            buffer[i] = (char) data[offset];
            boxType += (int) ((int) buffer[i] * Math.pow(16, (6 - 2*i)));
            offset++;
        }
        
        ds.setOffset(offset);
        ds.setType(boxType);
        ds.setCharArray(buffer);
        
        return ds;
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
        dataStructure ds = new dataStructure();
        
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
        ds = parseSegment(data, 4, offset);
        
        boxType = ds.getType();
        offset = ds.getOffset() + 12;
        
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
    
    public LinkedList<JUMBFSuperBox> separateJUMBF(byte[] data) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        LinkedList<JUMBFSuperBox> jumbfs = new LinkedList<>();
        int boxLength = 0;
        int offset = 0;
        
        while(offset < data.length) {
            if ((Byte.toUnsignedInt(data[offset]) == 0x6a
                    && Byte.toUnsignedInt(data[offset + 1]) == 0x70
                    && Byte.toUnsignedInt(data[offset + 2]) == 0x32
                    && Byte.toUnsignedInt(data[offset + 3]) == 0x63)
                    || (Byte.toUnsignedInt(data[offset]) == 0x78
                    && Byte.toUnsignedInt(data[offset + 1]) == 0x6d
                    && Byte.toUnsignedInt(data[offset + 2]) == 0x6c
                    && Byte.toUnsignedInt(data[offset + 3]) == 0x20)
                    || (Byte.toUnsignedInt(data[offset]) == 0x6a
                    && Byte.toUnsignedInt(data[offset + 1]) == 0x73
                    && Byte.toUnsignedInt(data[offset + 2]) == 0x6f
                    && Byte.toUnsignedInt(data[offset + 3]) == 0x6e)
                    || (Byte.toUnsignedInt(data[offset]) == 0x75
                    && Byte.toUnsignedInt(data[offset + 1]) == 0x75
                    && Byte.toUnsignedInt(data[offset + 2]) == 0x69
                    && Byte.toUnsignedInt(data[offset + 3]) == 0x64)
                    || (Byte.toUnsignedInt(data[offset]) == 0x62
                    && Byte.toUnsignedInt(data[offset + 1]) == 0x69
                    && Byte.toUnsignedInt(data[offset + 2]) == 0x64
                    && Byte.toUnsignedInt(data[offset + 3]) == 0x62)) {
                boxLength = this.getIntFromBytes(allocateBytes(data[offset - 4], data[offset - 3], data[offset - 2], data[offset - 1]));
                baos.write(data, offset - 4, boxLength);
                offset += boxLength;
                jumbfs.add(this.shapeJUMBFSuperBox(baos.toByteArray()));
            } else {
                offset++;
            }
        }
        return jumbfs;
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
            
            if (image.getIconWidth() > 1500) {
                int width = 1200;
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
        } else if (input.getClass().equals(JUMBFSuperBox.class)) {
            JUMBFSuperBox box = (JUMBFSuperBox) input;
            
            byte[] data = box.getContentBox().getContentData();

            JFrame frame = new JFrame();
            JLabel label = new JLabel(box.getDescriptionBox().getLabel());
            
            ImageIcon image = new ImageIcon(data);
            
            if (image.getIconWidth() > 1500) {
                int width = 1200;
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
        
        File file = new File("/home/carlos/Escritorio/codestream-parser-master/merge.jpeg");
        
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
        String s = new String("/home/carlos/Testfiles/" + fileName + ".jumbf");
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
            
            while (length != 0) {
                numBoxes++;
                if (box.getXTBoxData().length < localSpan) {
                    length = 0;
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
