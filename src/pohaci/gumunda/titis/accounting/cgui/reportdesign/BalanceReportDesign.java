package pohaci.gumunda.titis.accounting.cgui.reportdesign;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.swing.JOptionPane;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import pohaci.gumunda.titis.accounting.dbapi.AccountingSQLSAP;
import pohaci.gumunda.titis.accounting.entity.Account;
import pohaci.gumunda.titis.accounting.entity.reportdesign.ReportAccountValue;
import pohaci.gumunda.titis.accounting.entity.reportdesign.ReportGroup;
import pohaci.gumunda.titis.accounting.entity.reportdesign.ReportRow;
import pohaci.gumunda.titis.accounting.entity.reportdesign.ReportSubtotal;
import pohaci.gumunda.titis.accounting.entity.reportdesign.ReportingContext;
import pohaci.gumunda.titis.accounting.entity.reportdesign.RowVisitor;
import pohaci.gumunda.titis.application.PrintingViewer;
import pohaci.gumunda.titis.application.ReportUtils;

public class BalanceReportDesign {
    public static class TestBalanceReport {
        ReportAccountValue addAccountRow(String namePattern, boolean normalDebit, ReportGroup g) throws SQLException
        {
            Account acct = sqlSap.findFirstAccount(namePattern,conn);
            ReportAccountValue retval;
            g.add(retval = new ReportAccountValue(normalDebit,acct));
            return retval;
        }
        public AccountingSQLSAP sqlSap = new AccountingSQLSAP();
        public java.sql.Connection conn;
        public List rootList;
        public void createMockupGraph() throws SQLException
        {   
            List l1 = new ArrayList();
            ReportGroup aktiva = new ReportGroup("AKTIVA",true,new Vector());
            aktiva.setBold(true);
            aktiva.setTextSize(2);
            aktiva.setGroupLevel(1);
            ReportGroup aktivaLancar = new ReportGroup("Aktiva Lancar",true,new Vector());
            aktivaLancar.setUnderline(true);
            aktivaLancar.setTextSize(1);
            addAccountRow("Kas dan%",true,aktivaLancar);
            addAccountRow("Investasi Jangka%",true,aktivaLancar);
            addAccountRow("Piutang%",true,aktivaLancar);
            addAccountRow("Persediaan%",true,aktivaLancar);
            addAccountRow("Uang Muka%",true,aktivaLancar);
            addAccountRow("Pajak Dibayar%",true,aktivaLancar);
            addAccountRow("Biaya Dibayar%",true,aktivaLancar);
            aktivaLancar.add(new ReportSubtotal("Jumlah Aktiva Lancar",true,aktivaLancar));
            
            ReportGroup aktivaTakLancar = new ReportGroup("Aktiva Tidak Lancar",true,new Vector());
            addAccountRow("Penyertaan%",true,aktivaTakLancar);
            addAccountRow("Aktiva Tetap%",true,aktivaTakLancar);
            addAccountRow("Aktiva Lain%",true,aktivaTakLancar);
            aktivaTakLancar.add(new ReportSubtotal("Jumlah Kewajiban Lancar",true,aktivaTakLancar));
            
            aktiva.add(aktivaLancar);
            aktiva.add(aktivaTakLancar);
            aktiva.add(new ReportSubtotal("Jumlah Aktiva",true,aktiva));
            
            ReportGroup kewajiban = new ReportGroup("Kewajiban",false,new Vector());
            kewajiban.setGroupLevel(1);
            kewajiban.setTextSize(2);
            kewajiban.setBold(true);
            ReportGroup kewajibanLancar = new ReportGroup("Kewajiban Lancar", false, new Vector());
            kewajibanLancar.setUnderline(true);
            kewajibanLancar.setTextSize(1);
            
            addAccountRow("Hutang%saha",false,kewajibanLancar);
            addAccountRow("Uang Muka Penjualan%",false,kewajibanLancar);
            addAccountRow("Biaya Masih Harus Dibayar%",false,kewajibanLancar);
            // ...
            kewajibanLancar.add(new ReportSubtotal("Jumlah Kewajiban Lancar",false,kewajibanLancar));
            kewajiban.add(kewajibanLancar);
            kewajiban.add(new ReportSubtotal("Jumlah Kewajiban",false,kewajiban));
            
            l1.add(aktiva);
            l1.add(kewajiban);
            rootList = l1;
            /*
            XStream xstream = new XStream();
            xstream.alias("balanceaccountvalue", ReportAccountValue.class);
            xstream.alias("balancegroup", ReportGroup.class);
            xstream.alias("balancesubtotal", ReportSubtotal.class);
            xstream.alias("balancereportrow", ReportRow.class);
            
            String serializedRootList = xstream.toXML(rootList);
            System.out.println(serializedRootList);
            String[] splittedRootList = split500bytes(serializedRootList);
            String recombined = combine(splittedRootList);
            if (!recombined.equals(serializedRootList))
                System.err.println("kok ga sama sih!");
                */
        }
        String[] split500bytes(String x)
        {
            int frags = ((x.length()-1) / 500) + 1;
            String[] result = new String[frags];
            int i;
            for (i=0; (i+1)<frags; i++)
                result[i] = x.substring(i*500,i*500+500);
            result[frags-1] = x.substring((frags-1)*500);
            return result;
        }
        String combine(String[] strs)
        {
            StringBuffer b = new StringBuffer();
            int i=0;
            while (i<strs.length)
            {
                b.append(strs[i]);
                i++;
            }
            return b.toString();
        }
        protected void contextInit(ReportingContext ctx)
        {
            ctx.setAccountingSQLSAP(this.sqlSap);
            ctx.setConnection(this.conn);
            ctx.setNumberFormat(NumberFormat.getInstance());
            DecimalFormat dformat = (DecimalFormat) ctx.getNumberFormat();
            dformat.setMinimumFractionDigits(2);
            dformat.setNegativePrefix("(");
            dformat.setNegativeSuffix(")");
             
            ctx.setDate(Calendar.getInstance().getTime());
            ctx.setReportTitle("Balance Sheet");
        }
        ReportingContext ctx = new ReportingContext();
        
        void calculate()
        {
            CalculateStrategy strat = new CalculateStrategy();
            contextInit(ctx);
            
            
            strat.calculate(ctx,this.rootList);
            
        }
        public void exec()
        {
            calculate();
            final ArrayList reportRows = new ArrayList();
            RowVisitor collectRows = new RowVisitor() {
                public void visit(ReportRow rw) { rw.storeOutput(reportRows);};
            };
            for (Iterator iter = rootList.iterator(); iter.hasNext();) {
                ReportRow element = (ReportRow) iter.next();
                element.preOrder(collectRows);
            }
            try {
                String compiledFileName = ReportUtils.compileReport("Balance");
                if (compiledFileName=="")
                    return;
                JRDataSource ds = new JRBeanCollectionDataSource(reportRows);
                HashMap parameters = new HashMap();
                parameters.put("","");
                parameters.put("param_logo", "../images/TS.gif");
                parameters.put("judul",ctx.getReportTitle());
                
                JasperPrint jasperPrint = JasperFillManager.fillReport(compiledFileName,
                        parameters, ds);
                PrintingViewer view = new PrintingViewer(jasperPrint);
                view.setTitle(ctx.getReportTitle());
                view.setVisible(true);
            } 
            catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null,e.toString());                
            }
        }

    }    
    static class CalculateStrategy {
        public void calculate(final ReportingContext ctx, List rootRowList)
        {
            RowVisitor v = new RowVisitor() {
                public void visit(ReportRow rw) {
                    //rw.setContext(ctx);
                    //rw.calculate(ctx);
                }
            };
            for (Iterator iter = rootRowList.iterator(); iter.hasNext();) {
                ReportRow rootRow = (ReportRow) iter.next();
                rootRow.postOrder(v);
                rootRow.postOrder(v);// two phase..
            }
        }
    }
}

