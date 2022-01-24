package JPEGXTBox;

/**
 *
 * @author carlos
 */
public interface JPEGXTBox {
    
    void setLBox(int LBox);
    public int getLBox();
    
    void setType(int TBox);
    public int getType();
    
    void setXLBox(long XLBox);
    public long getXLBox();
    
    void setPayloadData(byte[] data);
    public byte[] getPayloadData();
    
    public byte[] getXTBoxData() throws Exception;
    
    public byte[] allocateData(Object data);
    
    public String boxToString();
}
