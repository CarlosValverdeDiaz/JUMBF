package Common;

import java.util.HashMap;
import java.util.UUID;
/**
 *
 * @author carlos
 */
public class Values {
    //Box properties
    
    //Box values
    public static final short BOX_MARKER = 0x4A50;
    
    //Defined boxes
    public static final int JUMBF_jumb = 0x6A756D62;
    public static final int JUMBF_jumd = 0x6A756D64;
    public static final int JUMBF_jp2c = 0x6A703263;
    public static final int JUMBF_xml = 0x786D6C20;
    public static final int JUMBF_json = 0x6A736F6E;
    public static final int JUMBF_uuid = 0x75756964;
    public static final int JUMBF_link = 0x4C494E4B;
    public static final int JUMBF_bidb = 0x62696462;
    
    public static final String char_JUMBF_jumb = "jumb";
    public static final String char_JUMBF_jumd = "jumd";
    public static final String char_JUMBF_jp2c = "jp2c";
    public static final String char_JUMBF_xml = "xml ";
    public static final String char_JUMBF_json = "json";
    public static final String char_JUMBF_uuid = "uuid";
    public static final String char_JUMBF_link = "link";
    public static final String char_JUMBF_bidb = "bidb";
    
    public static final String CodestreamContentType = "CodestreamContentType";    
    public static final String XMLContentType = "XMLContentType";
    public static final String JSONContentType = "JSONContentType";
    public static final String UUIDContentType = "UUIDContentType";
    public static final String ContiguousCodestream = "ContiguousCodestream";
    public static final String EmbeddedFile = "EmbeddedFile";

    public static final String String_TYPE_codestreamContentType = "6579D6FB-DBA2-446B-B2AC-1B82FEEB89D1";    
    public static final String String_TYPE_XMLContentType = "786D6C20-0011-0010-8000-00AA00389B71";
    public static final String String_TYPE_JSONContentType = "6A736F6E-0011-0010-8000-00AA00389B71";
    public static final String String_TYPE_UUIDContentType = "75756964-0011-0010-8000-00AA00389B71";
    public static final String String_TYPE_ContiguousCodestream = "6A703263-0011-0010-8000-00AA00389B71";
    public static final String String_TYPE_EmbeddedFile = "40CB0C32-BB8A-489D-A70B-2AD6F47F4369";
    public static final String String_TYPE_JLINK = "4C494E4B-0011-0010-8000-00AA00389B71";
    //Description Box types
    public static final UUID TYPE_codestreamContentType = UUID.fromString(String_TYPE_codestreamContentType);    
    public static final UUID TYPE_XMLContentType = UUID.fromString(String_TYPE_XMLContentType);
    public static final UUID TYPE_JSONContentType = UUID.fromString(String_TYPE_JSONContentType);
    public static final UUID TYPE_UUIDContentType = UUID.fromString(String_TYPE_UUIDContentType);
    public static final UUID TYPE_ContiguousCodestream = UUID.fromString(String_TYPE_ContiguousCodestream);
    public static final UUID TYPE_EmbeddedFile = UUID.fromString(String_TYPE_EmbeddedFile);
    
    public static final int XT_BOX_MAX_DATA = 65536;
    public static final int XT_BOX_HEADER_LENGTH = 18;
    
    //JLINK values
    public static final UUID TYPE_JLINK_Metadata_Elements = UUID.fromString("4C494E4B-0011-0010-8000-00AA00389B71");
    
    //JPEG markers
    public static final int START_OF_FRAME0 = 0xFFC0; //Basline DCT
    public static final int START_OF_FRAME1 = 0xFFC1; //Extended Sequential DCT
    public static final int START_OF_FRAME2 = 0xFFC2; //Progressive DCT
    public static final int START_OF_FRAME3 = 0xFFC3; //Lossless (sequential)
    public static final int START_OF_FRAME5 = 0xFFC5; //Differential sequential DCT
    public static final int START_OF_FRAME6 = 0xFFC6; //Differential progressive DCT
    public static final int START_OF_FRAME7 = 0xFFC7; //Differential lossless (sequential)
    public static final int START_OF_FRAME9 = 0xFFC9; //Extended sequential DCT, Arithmetic coding
    public static final int START_OF_FRAME10 = 0xFFCA; //Progressive DCT, Arithmetic coding
    public static final int START_OF_FRAME11 = 0xFFCB; //Lossless (sequential), Arithmetic coding
    public static final int START_OF_FRAME13 = 0xFFCD; //Differential sequential DCT, Arithmetic coding
    public static final int START_OF_FRAME14 = 0xFFCE; //Differential progressive DCT, Arithmetic coding
    public static final int START_OF_FRAME15 = 0xFFCF; //Differential lossless (sequential), Arithmetic coding
    
    public static final int DEFINE_HUFFMAN_TABLE  = 0xFFC4;
    public static final int JPEG_EXTENSIONS = 0xFFC8;
    public static final int DEFINE_ARITHMETIC_CODING = 0xFFCC;
            
    public static final int RESTART_MARKER_0 = 0xFFD0;
    public static final int RESTART_MARKER_1 = 0xFFD1;
    public static final int RESTART_MARKER_2 = 0xFFD2;
    public static final int RESTART_MARKER_3 = 0xFFD3;
    public static final int RESTART_MARKER_4 = 0xFFD4;
    public static final int RESTART_MARKER_5 = 0xFFD5;
    public static final int RESTART_MARKER_6 = 0xFFD6;
    public static final int RESTART_MARKER_7 = 0xFFD7;

    public static final int START_OF_IMAGE = 0xFFD8;
    public static final int END_OF_IMAGE = 0xFFD9;
    public static final int START_OF_SCAN = 0xFFDA;
            
    public static final int DEFINE_QUANTIZATION_TABLE = 0xFFDB;
    public static final int DEFINE_NUMBER_OF_LINES = 0xFFDC;
    public static final int DEFINE_RESTART_INTERVAL = 0xFFDD;
    public static final int DEFINE_HIERARCHICAL_PROGRESSION = 0xFFDE;
            
    public static final int EXPAND_REFERENCE_COMPONENT = 0xFFDF;
            
    public static final int APPLICATION_SEGMENT_0 = 0xFFE0;
    public static final int APPLICATION_SEGMENT_1 = 0xFFE1;
    public static final int APPLICATION_SEGMENT_2 = 0xFFE2;
    public static final int APPLICATION_SEGMENT_3 = 0xFFE3;
    public static final int APPLICATION_SEGMENT_4 = 0xFFE4;
    public static final int APPLICATION_SEGMENT_5 = 0xFFE5;
    public static final int APPLICATION_SEGMENT_6 = 0xFFE6;
    public static final int APPLICATION_SEGMENT_7 = 0xFFE7;
    public static final int APPLICATION_SEGMENT_8 = 0xFFE8;
    public static final int APPLICATION_SEGMENT_9 = 0xFFE9;
    public static final int APPLICATION_SEGMENT_10 = 0xFFEA;
    public static final int APPLICATION_SEGMENT_11 = 0xFFEB;
    public static final int APPLICATION_SEGMENT_12 = 0xFFEC;
    public static final int APPLICATION_SEGMENT_13 = 0xFFED;
    public static final int APPLICATION_SEGMENT_14 = 0xFFEE;
    public static final int APPLICATION_SEGMENT_15 = 0xFFEF;

    public static final int JPEG_EXTENSION_0 = 0xFFF0;
    public static final int JPEG_EXTENSION_1 = 0xFFF1;
    public static final int JPEG_EXTENSION_2 = 0xFFF2;
    public static final int JPEG_EXTENSION_3 = 0xFFF3;
    public static final int JPEG_EXTENSION_4 = 0xFFF4;
    public static final int JPEG_EXTENSION_5 = 0xFFF5;
    public static final int JPEG_EXTENSION_6 = 0xFFF6;
    public static final int JPEG_EXTENSION_7 = 0xFFF7;
    public static final int JPEG_EXTENSION_8 = 0xFFF8;
    public static final int JPEG_EXTENSION_9 = 0xFFF9;
    public static final int JPEG_EXTENSION_10 = 0xFFFA;
    public static final int JPEG_EXTENSION_11 = 0xFFFB;
    public static final int JPEG_EXTENSION_12 = 0xFFFC;
    public static final int JPEG_EXTENSION_13 = 0xFFFD;
    
    public static final int COMMENT = 0xFFFE;
    
    public static HashMap<String, UUID> BoxTypes = new HashMap<>();
    
    public static void setMap() {
        BoxTypes.put(Values.CodestreamContentType, Values.TYPE_codestreamContentType);
        BoxTypes.put(Values.ContiguousCodestream, Values.TYPE_ContiguousCodestream);
        BoxTypes.put(Values.EmbeddedFile, Values.TYPE_EmbeddedFile);
        BoxTypes.put(Values.JSONContentType, Values.TYPE_JSONContentType);
        BoxTypes.put(Values.UUIDContentType, Values.TYPE_UUIDContentType);
        BoxTypes.put(Values.XMLContentType, Values.TYPE_XMLContentType);        
    }
}
