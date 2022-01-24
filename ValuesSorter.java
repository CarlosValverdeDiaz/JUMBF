/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Common;

import java.util.HashMap;

/**
 *
 * @author carlos
 */
public class ValuesSorter {
    protected static HashMap<Integer, String> JPEGValues = null;
    
    protected void initMap(){   
        //System.out.println("\nInit map\n");
        JPEGValues = new HashMap<>();
        
        JPEGValues.put(Values.START_OF_FRAME0, "start of frame 0");
        JPEGValues.put(Values.START_OF_FRAME1, "start of frame 1");
        JPEGValues.put(Values.START_OF_FRAME2, "start of frame 2");
        JPEGValues.put(Values.START_OF_FRAME3, "start of frame 3");
        JPEGValues.put(Values.START_OF_FRAME5, "start of frame 5");
        JPEGValues.put(Values.START_OF_FRAME6, "start of frame 6");
        JPEGValues.put(Values.START_OF_FRAME7, "start of frame 7");
        JPEGValues.put(Values.START_OF_FRAME9, "start of frame 9");
        JPEGValues.put(Values.START_OF_FRAME10, "start of frame 10");
        JPEGValues.put(Values.START_OF_FRAME11, "start of frame 11");
        JPEGValues.put(Values.START_OF_FRAME13, "start of frame 13");
        JPEGValues.put(Values.START_OF_FRAME14, "start of frame 14");
        JPEGValues.put(Values.START_OF_FRAME15, "start of frame 15");
        
        JPEGValues.put(Values.START_OF_IMAGE, "start of image");
        JPEGValues.put(Values.END_OF_IMAGE, "end of image");
        JPEGValues.put(Values.START_OF_SCAN, "start of scan");
        
        JPEGValues.put(Values.DEFINE_HUFFMAN_TABLE, "define Huffman table");
        JPEGValues.put(Values.DEFINE_QUANTIZATION_TABLE, "define quantization table");
        JPEGValues.put(Values.DEFINE_NUMBER_OF_LINES, "define numbr of lines");
        JPEGValues.put(Values.DEFINE_RESTART_INTERVAL, "define restart interval");
        JPEGValues.put(Values.DEFINE_HIERARCHICAL_PROGRESSION, "define hierarchical progression");
        JPEGValues.put(Values.EXPAND_REFERENCE_COMPONENT, "expand reference component");
    
        JPEGValues.put(Values.APPLICATION_SEGMENT_0, "application segment 0");
        JPEGValues.put(Values.APPLICATION_SEGMENT_1, "application segment 1");
        JPEGValues.put(Values.APPLICATION_SEGMENT_2, "application segment 2");
        JPEGValues.put(Values.APPLICATION_SEGMENT_3, "application segment 3");
        JPEGValues.put(Values.APPLICATION_SEGMENT_4, "application segment 4");
        JPEGValues.put(Values.APPLICATION_SEGMENT_5, "application segment 5");
        JPEGValues.put(Values.APPLICATION_SEGMENT_6, "application segment 6");
        JPEGValues.put(Values.APPLICATION_SEGMENT_7, "application segment 7");
        JPEGValues.put(Values.APPLICATION_SEGMENT_8, "application segment 8");
        JPEGValues.put(Values.APPLICATION_SEGMENT_9, "application segment 9");
        JPEGValues.put(Values.APPLICATION_SEGMENT_10, "application segment 10");
        JPEGValues.put(Values.APPLICATION_SEGMENT_11, "application segment 11");
        JPEGValues.put(Values.APPLICATION_SEGMENT_12, "application segment 12");
        JPEGValues.put(Values.APPLICATION_SEGMENT_13, "application segment 13");
        JPEGValues.put(Values.APPLICATION_SEGMENT_14, "application segment 14");
        JPEGValues.put(Values.APPLICATION_SEGMENT_15, "application segment 15");
        
        JPEGValues.put(Values.JPEG_EXTENSION_0, "JPEG extension 0");
        JPEGValues.put(Values.JPEG_EXTENSION_1, "JPEG extension 1");
        JPEGValues.put(Values.JPEG_EXTENSION_2, "JPEG extension 2");
        JPEGValues.put(Values.JPEG_EXTENSION_3, "JPEG extension 3");
        JPEGValues.put(Values.JPEG_EXTENSION_4, "JPEG extension 4");
        JPEGValues.put(Values.JPEG_EXTENSION_5, "JPEG extension 5");
        JPEGValues.put(Values.JPEG_EXTENSION_6, "JPEG extension 6");
        JPEGValues.put(Values.JPEG_EXTENSION_7, "JPEG extension 7");
        JPEGValues.put(Values.JPEG_EXTENSION_8, "JPEG extension 8");
        JPEGValues.put(Values.JPEG_EXTENSION_9, "JPEG extension 9");
        JPEGValues.put(Values.JPEG_EXTENSION_10, "JPEG extension 11");
        JPEGValues.put(Values.JPEG_EXTENSION_11, "JPEG extension 12");
        JPEGValues.put(Values.JPEG_EXTENSION_12, "JPEG extension 13");
        JPEGValues.put(Values.JPEG_EXTENSION_13, "JPEG extension 14");
        
        JPEGValues.put(Values.COMMENT, "Comment");     
    }
    
    protected String sortByValue(int value){
        if(JPEGValues == null){
            initMap();
        }
        
        return JPEGValues.get(value);
    }
}
