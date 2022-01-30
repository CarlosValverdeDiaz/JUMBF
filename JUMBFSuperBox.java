package JUMBF;

import JPEGXTBox.Box;
import java.io.ByteArrayOutputStream;
import java.util.LinkedList;
import java.util.UUID;
/**
 *
 * @author carlos
 */
public class JUMBFSuperBox extends Box{
    private JUMBFDescriptionBox DescriptionBox;
    private LinkedList<JUMBFContentBox> ContentBoxes;
    
    public JUMBFSuperBox(UUID type, byte toggles, String label, int id) throws Exception {
        super.setType(Common.Values.JUMBF_jumb);
        DescriptionBox = new JUMBFDescriptionBox(type, toggles, label, id);
        ContentBoxes = new LinkedList<>();
        super.setLBox(8 + this.getDescriptionBox().getLBox());

    }
    
    public JUMBFSuperBox() {
        super.setType(Common.Values.JUMBF_jumb);
        super.setLBox(8);
        ContentBoxes = new LinkedList<>();
    }
    
    public void setDescriptionBox(JUMBFDescriptionBox DescriptionBox) {
        this.DescriptionBox = DescriptionBox;
        this.setLBox(super.getLBox() + DescriptionBox.getLBox());
    }

    public JUMBFDescriptionBox getDescriptionBox() {
        return DescriptionBox;
    }
    
    public void addData(Object data) throws Exception {
        UUID boxType = this.DescriptionBox.getContentType();
        //all this Box Types allow exactly 1 Content Box
        if((boxType == Common.Values.TYPE_codestreamContentType || boxType == Common.Values.TYPE_XMLContentType || boxType == Common.Values.TYPE_JSONContentType ||
                boxType == Common.Values.TYPE_UUIDContentType) && !this.ContentBoxes.isEmpty()){
            throw new Exception(boxType.toString() + " box type content is exactly one box");
        } else {
            JUMBFContentBox box = new JUMBFContentBox(data, this.getDescriptionBox().getContentType());
            ContentBoxes.add(box);
            this.setLBox(super.getLBox() + (int) box.getSize());
        }
    }
    
    public void addContentBox(JUMBFContentBox contentBox) {
        this.ContentBoxes.add(contentBox);
        this.setLBox(super.getLBox() + (int) contentBox.getSize());
    }
    
    public LinkedList getNestedBoxes() {
        return this.ContentBoxes;
    }
    
    public JUMBFContentBox getContentBox() {
        return this.ContentBoxes.element();
    }
    
    @Override
    public byte[] getXTBoxData() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        //DescriptionBox Type
        baos.write(allocateData(this.getDescriptionBox().getLBox()));
        baos.write(allocateData(this.getDescriptionBox().getType()));
        // UUID code
        baos.write(allocateData(this.getDescriptionBox().getContentType().getMostSignificantBits()));
        baos.write(allocateData(this.getDescriptionBox().getContentType().getLeastSignificantBits()));
        //Toggles code
        baos.write(allocateData(this.getDescriptionBox().getToggles()));
        //Label code
        baos.write(allocateData(this.getDescriptionBox().getLabel().getBytes()));
        baos.write(0);
        //ID code
        baos.write(allocateData(this.getDescriptionBox().getId()));
        //Signature
        baos.write(allocateData(this.getDescriptionBox().getSignature()));
        //Content Box
        baos.write(allocateData(this.getContentBox().getXTBoxData()));

        return baos.toByteArray();
    }
    
    @Override
    public String boxToString() {
        //This method returns a String representing all JUMBF SUPERBOX content following the format: JUMBF SUPERBOX -> JUMBF DESCRIPTION BOX, JUMBF CONTENT BOX
        StringBuilder sb = new StringBuilder();
        
        sb.append("\n_______________________JUMBF SUPERBOX_______________________\n");

        sb.append(this.DescriptionBox.boxToString());

        for(int i = 0; i < ContentBoxes.size(); i++) {
            sb.append(ContentBoxes.get(i).boxToString());
        }        
        
        return sb.toString();
    }
}