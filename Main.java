package Common;

import JUMBF.JUMBFSuperBox;
import JUMBF.JUMBFUtils;
import java.io.File;
import java.nio.file.Paths;

/**
 *
 * @author carlos
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        JUMBFUtils p = new JUMBFUtils();
        File f = new File("/home/carlos/Escritorio/600x400.jpeg");
        JUMBFSuperBox sb = new JUMBFSuperBox(Common.Values.TYPE_ContiguousCodestream, (byte) 15, "Test Label", 12345678);
        sb.addData(f);
        
        p.JUMBFToBox(sb, "qazwsx", (short) 4);
        p.mergeJUMBF("/home/carlos/NetBeansProjects/JUMBF/src/Test_JUMBF/test.jpeg", "/home/carlos/Testfiles/qwer.jumbf");//TestJlink1
    }
    
}
