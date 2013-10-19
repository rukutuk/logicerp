package pohaci.gumunda.titis.application;

import java.io.File;
import java.io.IOException;

import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;

public class ReportUtils {
    public static String compileReport(String reportName) throws JRException{
    	// current dir
    	//final String dir = System.getProperty("user.dir");
        //System.out.println("current dir = " + dir);
        // ==========
        String rptFilename = "../report/" + reportName + ".jrxml";
        String compiledRptFilename = "../report/" + reportName + ".jasper";
        File compiledRptFile = new File(compiledRptFilename);
        File xmlRptFile = new File(rptFilename);
        
        if (xmlRptFile.exists() && compiledRptFile.exists()){
        	if (xmlRptFile.lastModified() > compiledRptFile.lastModified()){
        		compiledRptFile.delete();
        		JasperCompileManager.compileReportToFile(rptFilename,compiledRptFilename);
        	}
        }
        if (!compiledRptFile.exists()){
            if (!xmlRptFile.exists()){
                JOptionPane.showMessageDialog(null,"File " + rptFilename + " not found");
                return "";
            }
            JasperCompileManager.compileReportToFile(rptFilename,compiledRptFilename);
        }
        if (!compiledRptFile.exists()){
            JOptionPane.showMessageDialog(null,"File " + compiledRptFilename + " not found");
            return "";
        }
        return compiledRptFilename;
    }
}
