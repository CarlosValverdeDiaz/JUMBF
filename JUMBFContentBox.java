package JUMBF;

import JPEGXTBox.Box;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author carlos
 */
public class JUMBFContentBox extends Box{
    private long Size = 8;
    private UUID Id;
    private UUID uuidTypeContent;
    private byte binarytoggle;
    private String binaryMediaType;
    private String fileName;
    private Object Content;
    
    public JUMBFContentBox(Object data, Object ... id) throws Exception {
        Content = data;
        System.out.println(id.length);
        
        if (id.length == 0) {
            
        } else {
            if (id[0].getClass().equals(int.class)) {
                super.setType((int) id[0]);
            } else if (id[0].getClass().equals(String.class)) {
                switch ((String) id[0]) {
                    case Common.Values.char_JUMBF_jp2c:
                        super.setType(Common.Values.JUMBF_jp2c);
                        break;
                    case Common.Values.char_JUMBF_xml:
                        super.setType(Common.Values.JUMBF_xml);
                        break;
                    case Common.Values.char_JUMBF_json:
                        super.setType(Common.Values.JUMBF_json);
                        break;
                    case Common.Values.char_JUMBF_uuid:
                        super.setType(Common.Values.JUMBF_uuid);
                        break;
                    case Common.Values.char_JUMBF_bidb:
                        super.setType(Common.Values.JUMBF_bidb);
                        break;
                    case Common.Values.char_JUMBF_link:
                        super.setType(Common.Values.JUMBF_link);
                        break;
                }
            } else if (id[0].getClass().equals(UUID.class)) {
                this.Id = (UUID) id[0];
                switch (((UUID) id[0]).toString().toUpperCase()) {
                    case Common.Values.String_TYPE_ContiguousCodestream:
                        super.setType(Common.Values.JUMBF_jp2c);
                        break;
                    case Common.Values.String_TYPE_codestreamContentType:
                        super.setType(Common.Values.JUMBF_jp2c);
                        break;
                    case Common.Values.String_TYPE_XMLContentType:
                        super.setType(Common.Values.JUMBF_xml);
                        break;
                    case Common.Values.String_TYPE_JSONContentType:
                        super.setType(Common.Values.JUMBF_json);
                        break;
                    case Common.Values.String_TYPE_UUIDContentType:
                        super.setType(Common.Values.JUMBF_uuid);
                        break;
                    case Common.Values.String_TYPE_EmbeddedFile:
                        super.setType(Common.Values.JUMBF_bidb);
                        break;
                    case Common.Values.String_TYPE_JLINK:
                        super.setType(Common.Values.JUMBF_link);
                        break;
                }
            } else {
                throw new Exception("Unsupported Data Type");
            }
        }
        
        if (Content != null) {
            if (Content.getClass() == File.class) {
                Size += ((File) Content).length();
            } else if (Content.getClass() == byte[].class) {
                Size += ((byte[]) Content).length;
            } 
        }
        
        if (id.length == 2) {
            this.uuidTypeContent = UUID.fromString((String) id[1]);
            System.out.println(this.uuidTypeContent.toString());
            Size += 32;
        }
        if (id.length == 5) {
            this.binarytoggle = (byte) id[2];
            Size += 1;
            this.binaryMediaType = (String) id[3];
            Size += ((String) id[3]).length() + 1;
            this.fileName = (String) id[4];
            Size += ((String) id[4]).length() + 1;
        }
        
        
        if (Size < Math.pow(2, 32)) {
            super.setLBox((int) Size);
        } else {
            super.setLBox(1);
            Size += 8;
            super.setXLBox(Size);
        }
    }
    
    public byte[] getContentData() throws Exception {
        if (Content.getClass() == byte[].class) {
            return (byte[]) Content;
        } else if (Content.getClass() == File.class) {
            if (((File) Content).getName().endsWith(".json") || ((File) Content).getName().endsWith(".xml")
                        || ((File) Content).getName().endsWith(".xsd") || ((File) Content).getName().endsWith(".txt")
                        || ((File) Content).getName().endsWith(".jpeg") || ((File) Content).getName().endsWith(".jpg")) {
                return Files.readAllBytes(Paths.get(((File) Content).getPath()));
            } else {
                throw new Exception("Invalid File type");
            }
            
        } else if (Content.getClass() == JUMBFSuperBox.class) {
            return ((JUMBFSuperBox) Content).getXTBoxData();
            
        } else {
            throw new Exception("Invalid data type: " + this.Content.getClass());
        }
    }
    
    public String boxToString() {
        //This method returns a String representing all JUMBF CONTENT BOX Content: 
        StringBuilder sb = new StringBuilder();
        
        sb.append("_______________________JUMBF CONTENT BOX_______________________\n");
        sb.append("Type: ");
        sb.append(String.format("%X",super.getType()));
        sb.append(" size: " + Size);
        sb.append(" UUID: " + this.Id + "\n\n");
       
        try {
            for (byte b:this.getContentData())
                sb.append((char) b);
        } catch (Exception ex) {
            Logger.getLogger(JUMBFContentBox.class.getName()).log(Level.SEVERE, null, ex);
        }

        return sb.toString();
    }

    public long getSize() {
        return Size;
    }

    public void setSize(long Size) {
        this.Size = Size;
    }

    public UUID getId() {
        return Id;
    }

    public void setId(UUID Id) {
        this.Id = Id;
    }

    public Object getContent() {
        return Content;
    }

    public void setContent(Object Content) {
        this.Content = Content;
    }

    public UUID getUuidTypeContent() {
        return uuidTypeContent;
    }

    public void setUuidTypeContent(UUID uuidTypeContent) {
        this.uuidTypeContent = uuidTypeContent;
    }

    public byte getBinarytoggle() {
        return binarytoggle;
    }

    public void setBinarytoggle(byte binarytoggle) {
        this.binarytoggle = binarytoggle;
    }

    public String getBinaryMediaType() {
        return binaryMediaType;
    }

    public void setBinaryMediaType(String binaryMediaType) {
        this.binaryMediaType = binaryMediaType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    
    
    
    public byte[] getXTBoxData() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        
        baos.write(allocateData(this.getLBox()));
        baos.write(allocateData(this.getType()));
        if (this.getLBox() == 1) {
            baos.write(allocateData(this.getXLBox()));
        }
        
        if (this.getType() == Common.Values.JUMBF_uuid) {
            baos.write(allocateData(this.uuidTypeContent.getMostSignificantBits()));
            baos.write(allocateData(this.uuidTypeContent.getLeastSignificantBits()));
        }
        
        if (this.getType() == Common.Values.JUMBF_bidb) {
            baos.write(allocateData(this.binarytoggle));
            if ((this.binarytoggle | 0b0001) == this.binarytoggle) {
                baos.write(allocateData(this.binaryMediaType.getBytes()));
                baos.write(0);
            }
            if ((this.binarytoggle | 0b0010) == this.binarytoggle) {
                baos.write(allocateData(this.fileName.getBytes()));
                baos.write(0);
            }
        }
        
        baos.write(allocateData(this.getContentData()));
        
        return baos.toByteArray();
    }
}
