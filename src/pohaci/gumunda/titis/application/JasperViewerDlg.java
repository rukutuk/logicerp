package pohaci.gumunda.titis.application;

/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2006 JasperSoft Corporation http://www.jaspersoft.com
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307, USA.
 * 
 * JasperSoft Corporation
 * 303 Second Street, Suite 450 North
 * San Francisco, CA 94107
 * http://www.jaspersoft.com
 */

/*
 * Contributors:
 * Ryan Johnson - delscovich@users.sourceforge.net
 * Carlton Moore - cmoore79@users.sourceforge.net
 */

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.io.InputStream;
import java.util.Locale;

import javax.swing.JDialog;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JRViewer;


/**
 * @author Yudhi Widyatama (dialog-based version)
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: JasperViewer.java 1259 2006-05-15 13:45:29Z teodord $
 */
public class JasperViewerDlg extends javax.swing.JDialog
{


    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     *
     */
    private JRViewer viewer = null;

    /**
     *
     */
    private boolean isExitOnClose = true;
    
    
    /** Creates new form JasperViewer */
    public JasperViewerDlg(
        String sourceFile, 
        boolean isXMLFile
        ) throws JRException
    {
        this(sourceFile, isXMLFile, true);
    }


    /** Creates new form JasperViewer */
    public JasperViewerDlg(
        InputStream is,
        boolean isXMLFile
        ) throws JRException
    {
        this(is, isXMLFile, true);
    }


    /** Creates new form JasperViewer */
    public JasperViewerDlg(
        JasperPrint jasperPrint, JDialog owner
        )
    {
        this(jasperPrint, true, owner);
    }


    /** Creates new form JasperViewer */
    public JasperViewerDlg(
        String sourceFile,
        boolean isXMLFile,
        boolean isExitOnClose
        )  throws JRException
    {
        this(sourceFile, isXMLFile, isExitOnClose, null);
    }


    /** Creates new form JasperViewer */
    public JasperViewerDlg(
        InputStream is,
        boolean isXMLFile,
        boolean isExitOnClose
        ) throws JRException
    {
        this(is, isXMLFile, isExitOnClose, null);
    }


    /** Creates new form JasperViewer */
    public JasperViewerDlg(JasperPrint jasperPrint,boolean isExitOnClose, JDialog owner){
        this(jasperPrint, isExitOnClose, null, owner);
    }


    /** Creates new form JasperViewer */
    public JasperViewerDlg(
        String sourceFile,
        boolean isXMLFile,
        boolean isExitOnClose,
        Locale locale
        )  throws JRException
    {
        if (locale != null)
            setLocale(locale);

        this.isExitOnClose = isExitOnClose;

        initComponents();

        this.viewer = new JRViewer(sourceFile, isXMLFile, locale);
        this.pnlMain.add(this.viewer, BorderLayout.CENTER);
    }


    /** Creates new form JasperViewer */
    public JasperViewerDlg(
        InputStream is,
        boolean isXMLFile,
        boolean isExitOnClose,
        Locale locale
        ) throws JRException
    {
        if (locale != null)
            setLocale(locale);

        this.isExitOnClose = isExitOnClose;

        initComponents();

        this.viewer = new JRViewer(is, isXMLFile, locale);
        this.pnlMain.add(this.viewer, BorderLayout.CENTER);
    }


    /** Creates new form JasperViewer */
    public JasperViewerDlg(
        JasperPrint jasperPrint,
        boolean isExitOnClose,
        Locale locale, JDialog owner
        )
    {
        super(owner);
        if (locale != null)
            setLocale(locale);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.isExitOnClose = isExitOnClose;

        initComponents();

        this.viewer = new JRViewer(jasperPrint, locale);
        this.pnlMain.add(this.viewer, BorderLayout.CENTER);
    }


    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        pnlMain = new javax.swing.JPanel();

        setTitle("JasperViewer");
        //setIconImage(new javax.swing.ImageIcon(getClass().getResource("/net/sf/jasperreports/view/images/jricon.GIF")).getImage());
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                exitForm();
            }
        });

        pnlMain.setLayout(new java.awt.BorderLayout());

        getContentPane().add(pnlMain, java.awt.BorderLayout.CENTER);

        pack();

        Toolkit toolkit = java.awt.Toolkit.getDefaultToolkit();
        java.awt.Dimension screenSize = toolkit.getScreenSize();
        int screenResolution = toolkit.getScreenResolution();
        float zoom = ((float) screenResolution) / JRViewer.REPORT_RESOLUTION;

        int height = (int) (550 * zoom);
        if (height > screenSize.getHeight())
        {
            height = (int) screenSize.getHeight();
        }
        int width = (int) (750 * zoom);
        if (width > screenSize.getWidth())
        {
            width = (int) screenSize.getWidth();
        }

        java.awt.Dimension dimension = new java.awt.Dimension(width, height);
        setSize(dimension);
        setLocation((screenSize.width-width)/2,(screenSize.height-height)/2);
    }//GEN-END:initComponents

    /** Exit the Application */
    void exitForm() {//GEN-FIRST:event_exitForm

        if (this.isExitOnClose)
        {
            System.exit(0);
        }
        else
        {
            this.setVisible(false);
            this.viewer.clear();
            this.viewer = null;
            this.getContentPane().removeAll();
            this.dispose();
        }

    }//GEN-LAST:event_exitForm


    /**
     *
     */
    public void setZoomRatio(float zoomRatio)
    {
        viewer.setZoomRatio(zoomRatio);
    }


    /**
     *
     */
    public void setFitWidthZoomRatio()
    {
        viewer.setFitWidthZoomRatio();
    }


    /**
     *
     */
    public void setFitPageZoomRatio()
    {
        viewer.setFitPageZoomRatio();
    }


    /**
    * @param args the command line arguments
    */
    public static void main(String args[])
    {
        String fileName = null;
        boolean isXMLFile = false;

        if(args.length == 0)
        {
            usage();
            return;
        }

        int k = 0;
        while ( args.length > k )
        {
            if ( args[k].startsWith("-F") )
                fileName = args[k].substring(2);
            if ( args[k].startsWith("-XML") )
                isXMLFile = true;

            k++;
        }

        try
        {
            viewReport(fileName, isXMLFile);
        }
        catch (JRException e)
        {
            e.printStackTrace();
            System.exit(1);
        }
    }


    /**
     *
     */
    private static void usage()
    {
        System.out.println( "JasperViewer usage:" );
        System.out.println( "\tjava JasperViewer -XML -Ffile" );
    }


    /**
     *
     */
    public static void viewReport(
        String sourceFile,
        boolean isXMLFile
        ) throws JRException
    {
        viewReport(sourceFile, isXMLFile, true, null);
    }

    /**
     *
     */
    public static void viewReport(
        InputStream is,
        boolean isXMLFile
        ) throws JRException
    {
        viewReport(is, isXMLFile, true, null);
    }

    /**
     *
     */
    public static void viewReport(
        JasperPrint jasperPrint
        )
    {
        viewReport(jasperPrint, true, null);
    }

    /**
     *
     */
    public static void viewReport(
        String sourceFile,
        boolean isXMLFile,
        boolean isExitOnClose
        ) throws JRException
    {
        viewReport(sourceFile, isXMLFile, isExitOnClose, null);
    }

    /**
     *
     */
    public static void viewReport(
        InputStream is,
        boolean isXMLFile,
        boolean isExitOnClose
        ) throws JRException
    {
        viewReport(is, isXMLFile, isExitOnClose, null);
    }

    /**
     *
     */
    public static void viewReport(
        JasperPrint jasperPrint,
        boolean isExitOnClose
        )
    {
        viewReport(jasperPrint, isExitOnClose, null);
    }

    /**
     *
     */
    public static void viewReport(
        String sourceFile,
        boolean isXMLFile,
        boolean isExitOnClose,
        Locale locale
        ) throws JRException
    {
        JasperViewerDlg jasperViewer =
            new JasperViewerDlg(
                sourceFile,
                isXMLFile,
                isExitOnClose,
                locale
                );
        jasperViewer.setVisible(true);
    }

    /**
     *
     */
    public static void viewReport(
        InputStream is,
        boolean isXMLFile,
        boolean isExitOnClose,
        Locale locale
        ) throws JRException
    {
        JasperViewerDlg jasperViewer =
            new JasperViewerDlg(
                is,
                isXMLFile,
                isExitOnClose,
                locale
                );
        jasperViewer.setVisible(true);
    }

    /**
     *
     */
    public static void viewReport(
        JasperPrint jasperPrint,
        boolean isExitOnClose,
        Locale locale
        )
    {
        JasperViewerDlg jasperViewer =
            new JasperViewerDlg(
                jasperPrint,
                isExitOnClose,
                locale,null
                );
        jasperViewer.setVisible(true);
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel pnlMain;
    // End of variables declaration//GEN-END:variables
    
}
