package Common;

import JUMBF.JUMBFSuperBox;
import JUMBF.JUMBFUtils;
import java.io.File;

/**
 *
 * @author carlos
 */

public class Main {
    private static String folderName = "";
    private static String imageName = "";
    private static String jumbfName = "";
    private static String auxFileName = "";

    public static void main(String[] args) throws Exception {
        JUMBFUtils p = new JUMBFUtils();
        File f = new File(auxFileName);
        JUMBFSuperBox sb = new JUMBFSuperBox(Common.Values.TYPE_ContiguousCodestream, (byte) 15, "Test Label", 12345678);
        sb.addData(f);
        
        p.JUMBFToBox(sb, "testJUMBF", (short) 4);
        p.mergeJUMBF(folderName + imageName, folderName + jumbfName);
    }
}