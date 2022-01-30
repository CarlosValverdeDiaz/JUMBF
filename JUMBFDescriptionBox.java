package JUMBF;

import JPEGXTBox.Box;
import java.security.MessageDigest;
import java.util.UUID;

/**
 *
 * @author carlos
 */
public class JUMBFDescriptionBox extends Box {
    private UUID ContentType; //The value of this UUID specifies the format of the Content Boxes embedded in the JUMBF box
    private byte Toggles; //This 1-byte field signals the values of options related to the JUMBF box
    private String Label; //This optional field contains a variable length textual label that can be used to reference or request the content of the JUMBF
    private int Id; //This optional field contains a user assigned unique 4-byte Id that can be used for binary references to this JUMBF box.
    private byte[] Signature; //This optional field shall contain an SHA-256 (FIPS PUB 180-4) checksum of the JUMBF Content.
    private int Size = 25; //Size calculation
    
    public JUMBFDescriptionBox(UUID type, byte toggles, String label, int id) throws Exception {
        super.setType(Common.Values.JUMBF_jumd);
        ContentType = type;
        Toggles = toggles;
        if((Toggles | 0b0001) == Toggles) {
        }
        if((Toggles | 0b0010) == Toggles) {
            this.Label = label;
            Size += label.getBytes().length + 1;
        }
        if((Toggles | 0b0100) == Toggles) {
            this.Id = id;
            Size += 4;
        }
        if((Toggles | 0b1000) == Toggles) {
            this.Signature = MessageDigest.getInstance("SHA-256").digest();
            Size += this.Signature.length;
        }
        super.setLBox(Size);
    }
    
    public JUMBFDescriptionBox(UUID type, byte toggles) {
        super.setType(Common.Values.JUMBF_jumd);
        ContentType = type;
        Toggles = toggles;
    }

    public UUID getContentType() {
        return ContentType;
    }

    public void setContentType(UUID Type) {
        this.ContentType = Type;
    }

    public byte getToggles() {
        return Toggles;
    }

    public void setToggles(byte Toggles) {
        this.Toggles = Toggles;
    }

    public String getLabel() {
        return Label;
    }

    public void setLabel(String Label) {
        this.Label = Label;
    }

    public int getId() {
        return Id;
    }

    public void setId(int Id) {
        this.Id = Id;
    }
    
    public byte[] getSignature() {
        return Signature;
    }

    public void setSignature(MessageDigest Signature) {
        this.Signature = Signature.digest();
    }
    
    public void setSignature(byte[] Signature) {
        this.Signature = Signature;
    }
    
    public String boxToString() {
        //This method returns a String representing all JUMBF DESCRIPTION BOX content: 
        StringBuilder sb = new StringBuilder();

        sb.append("_______________________JUMBF DESCRIPTION BOX_______________________\n");

        sb.append("Type: " + this.ContentType);
        sb.append(" Toggles: " + this.Toggles);
        sb.append(" Label: " + this.Label);
        sb.append(" Id: " + this.Id);
        sb.append(" Signature: ");
        for (byte b:Signature)
            sb.append(b);
        sb.append("\n");
        
        return sb.toString();
    }
}
