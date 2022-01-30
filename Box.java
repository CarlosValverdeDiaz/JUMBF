package JPEGXTBox;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;

/**
 *
 * @author carlos
 */
public class Box implements JPEGXTBox{
    private int LBox;
    private int TBox;
    private long XLBox;
    private byte[] data;
    
    public Box(int LBox, int TBox, byte[] data, long... XLBox) {
        this.LBox = LBox;
        this.TBox = TBox;
        this.data = data;
        this.XLBox = XLBox[0];
    }
    
    public Box(int LBox, int TBox, byte[] data) {
        this.LBox = LBox;
        this.TBox = TBox;
        this.data = data;
    }
    
    public Box() {
        
    }

    @Override
    public void setLBox(int LBox) {
        this.LBox = LBox;
    }

    @Override
    public int getLBox() {
        return this.LBox;
    }

    @Override
    public void setType(int TBox) {
        this.TBox = TBox;
    }

    @Override
    public int getType() {
        return this.TBox;
    }

    @Override
    public void setXLBox(long XLBox) {
        this.XLBox = XLBox;
    }

    @Override
    public long getXLBox() {
        return this.XLBox;
    }

    @Override
    public void setPayloadData(byte[] data) {
        this.data = data;
    }

    @Override
    public byte[] getPayloadData() {
        return this.data;
    }

    @Override
    public byte[] getXTBoxData() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        
        baos.write(allocateData(this.LBox));
        baos.write(allocateData(this.TBox));
        if (this.LBox == 1) {
            baos.write(allocateData(this.XLBox));
        }
        baos.write(allocateData(this.data));
        
        return baos.toByteArray();
    }

    @Override
    public byte[] allocateData(Object data) {
        if (data.getClass().equals(Short.class)) {
            return ByteBuffer.allocate(2).putShort((short) data).array();
        } else if (data.getClass().equals(Integer.class)) {
            return ByteBuffer.allocate(4).putInt((int) data).array();
        } else if (data.getClass().equals(Long.class)) {
            return ByteBuffer.allocate(8).putLong((long) data).array();
        } else if (data.getClass().equals(byte[].class)) {
            return ByteBuffer.allocate(((byte[]) data).length).put((byte[]) data).array();
        } else if (data.getClass().equals(Byte.class)) {
            return ByteBuffer.allocate(1).put((byte) data).array();
        }
        else {
            return null;
        }
    }

    @Override
    public String boxToString() {
        StringBuilder sb = new StringBuilder();       
        int counter = 0;
        
        sb.append(" LBox: " + this.LBox);
        sb.append(" TBox: " + this.TBox);
        if (LBox == 1)
            sb.append(" XLBox: " + this.XLBox);
        sb.append("\n");
        
        for(byte b:this.data) {
            if (counter == 40) {
                sb.append('\n');
                counter = 0;
            }
            sb.append(b);
            counter++;
        }
        
        return sb.toString();
    }
}
