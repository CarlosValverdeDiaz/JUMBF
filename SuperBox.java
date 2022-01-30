package JPEGXTBox;

import java.io.ByteArrayOutputStream;

/**
 *
 * @author carlos
 */
public class SuperBox extends Box{
    private short APP11; //JPEG marker segment
    private short Le; //Le field is the size of the marker segment, not including the marker. It measures the size from the Le field up to the end of the marker segment.
    private short CI; //Common Identifier is a 16 bit field that allows decoders to identify an APP11 marker segment as a JPEG XT marker segment. Its value shall be 0x4A50.
    private short BoxInstance; //Box Instance Number is a 16-bit field that disambiguates between JPEG XT marker segments carrying boxes of identical type, but differing content.
    private int Z; //Packet Sequence Number is a 32-bit field that specifies the order in which payload data shall be merged.   
    
    public SuperBox(short Le, short BoxInstance, int Z, int LBox, int TBox, byte[] data, long... XLBox) {
        super(LBox, TBox, data, XLBox[0]);
        this.APP11 = (short) Common.Values.APPLICATION_SEGMENT_11;
        this.Le = Le;
        this.CI = (short) Common.Values.BOX_MARKER;
        this.BoxInstance = BoxInstance;
        this.Z = Z;
    }
    
    public SuperBox(short Le, short BoxInstance, int Z, int LBox, int TBox, byte[] data) {
        super(LBox, TBox, data);
        this.APP11 = (short) Common.Values.APPLICATION_SEGMENT_11;
        this.Le = Le;
        this.CI = (short) Common.Values.BOX_MARKER;
        this.BoxInstance = BoxInstance;
        this.Z = Z;
    }
    
    public SuperBox() {
        
    }
    
    @Override
    public String boxToString() {
        StringBuilder sb = new StringBuilder();
        
        sb.append("\n_______________________JPEG XT BOX_______________________\n");

        sb.append("\nJPEG marker segment: " + String.format("%X", APP11));
        sb.append(" Le: " + Le);
        sb.append(" CI: " + String.format("%X", CI));
        sb.append(" BoxInstance: " + BoxInstance);
        sb.append(" Z: " + Z);
        sb.append(super.boxToString());

        return sb.toString();
    }

    @Override
    public byte[] allocateData(Object data) {
        return super.allocateData(data); 
    }

    @Override
    public byte[] getXTBoxData() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        baos.write(allocateData(this.APP11));
        baos.write(allocateData(this.Le));
        System.out.println(Le);
        baos.write(allocateData(this.CI));
        baos.write(allocateData(this.BoxInstance));
        baos.write(allocateData(this.Z));
        baos.write(super.getXTBoxData());
        
        return baos.toByteArray();
    }

    @Override
    public byte[] getPayloadData() {
        return super.getPayloadData(); 
    }

    @Override
    public void setPayloadData(byte[] data) {
        super.setPayloadData(data); 
    }

    @Override
    public long getXLBox() {
        return super.getXLBox(); 
    }

    @Override
    public void setXLBox(long XLBox) {
        super.setXLBox(XLBox);
    }

    @Override
    public int getType() {
        return super.getType(); 
    }

    @Override
    public void setType(int TBox) {
        super.setType(TBox);
    }

    @Override
    public int getLBox() {
        return super.getLBox();
    }

    @Override
    public void setLBox(int LBox) {
        super.setLBox(LBox);
    }

    public short getBoxInstance() {
        return BoxInstance;
    }
    
    public void setBoxInstance(short BoxInstance) {
        this.BoxInstance = BoxInstance;
    }

    public int getZ() {
        return Z;
    }    
    
    public void setZ(int Z) {
        this.Z = Z;
    }
}
