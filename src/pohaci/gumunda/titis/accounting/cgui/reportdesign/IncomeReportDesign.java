package pohaci.gumunda.titis.accounting.cgui.reportdesign;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

import pohaci.gumunda.titis.accounting.cgui.reportdesign.BalanceReportDesign.TestBalanceReport;
import pohaci.gumunda.titis.accounting.entity.reportdesign.ReportAccountValue;
import pohaci.gumunda.titis.accounting.entity.reportdesign.ReportEmptyRow;
import pohaci.gumunda.titis.accounting.entity.reportdesign.ReportGroup;
import pohaci.gumunda.titis.accounting.entity.reportdesign.ReportSubtotal;
import pohaci.gumunda.titis.accounting.entity.reportdesign.ReportingContext;

public class IncomeReportDesign {
    public static class TestIncomeReport extends TestBalanceReport {
        protected void contextInit(ReportingContext ctx) {
            super.contextInit(ctx);
            ctx.setReportType(ReportingContext.INCOMESTMT);
            ctx.setReportTitle("Income statement");
            //Date dec2007 = new Date(107,12,31);
            Calendar cal = Calendar.getInstance();
            cal.set(2007, 12, 31);
            Date dec2007 = cal.getTime();
            ctx.setDate(null);
            ctx.setToDate(dec2007);
        }
        void addEmptyRow(ReportGroup g)
        {
            g.add(new ReportEmptyRow());
        }
        public void createMockupGraph() throws SQLException
        {   
            //List l1 = new ArrayList();
            ReportGroup labaUmum = new ReportGroup("Laba umum",true,new Vector());
            labaUmum.setViewValue(true);
            ReportGroup labaOperasi = new ReportGroup("Laba operasi",true,new Vector());
            labaOperasi.setViewValue(true);
            ReportGroup p = new ReportGroup("LABA KOTOR",true,new Vector());
            p.setViewValue(true);
            //p.invisible = true;
            
            ReportAccountValue p1 = addAccountRow("PENDAPATAN%",false,p);
            p1.setTextSize(1);
            p1.setBold(true);
            p1.setIndent(0);
            addEmptyRow(p);
            ReportAccountValue p2 = addAccountRow("Beban Pokok Penj%",false,p);
            p2.setTextSize(1);
            p2.setBold(true);
            p2.setIndent(0);
            p2.setLabel("BEBAN POKOK PENJUALAN");
            addEmptyRow(p);
            p.add(new ReportSubtotal("Laba kotor",false,p));
            addEmptyRow(p);
            ReportGroup o = new ReportGroup("BEBAN OPERASIONAL",true,new Vector());
            o.setTextSize(1);
            addAccountRow("Beban Gaji dan %",true,o);
            addAccountRow("Beban Pegawai L%",true,o);
            addAccountRow("Beban Umum%",true,o);
            addAccountRow("Beban Penjualan%",true,o);
            addAccountRow("Beban Kendaraan%",true,o);
            addAccountRow("Beban Penyusutan%",true,o);
            o.add(new ReportSubtotal("Jumlah Beban Operasional",true,o));
            labaOperasi.add(p);
            labaOperasi.add(o);
            labaOperasi.add(new ReportSubtotal("Laba operasi",true,labaOperasi));
            ReportGroup bebanLain2 = new ReportGroup("PENDAPATAN/BEBAN LAIN-LAIN",true,new Vector());
            bebanLain2.setTextSize(1);
            addAccountRow("Pendapatan Lain-%",false,bebanLain2);
            addAccountRow("Beban Lain-%",false,bebanLain2);
            bebanLain2.add(new ReportSubtotal("Jumlah Pendapatan/(Beban) lain-lain",false,bebanLain2));
            labaUmum.add(labaOperasi);
            labaUmum.add(bebanLain2);
            labaUmum.add(new ReportSubtotal("Laba/(rugi) sebelum pajak",false,labaUmum));
            ReportGroup labaRugi = new ReportGroup("LABA/(RUGI)",false,new Vector());
            labaRugi.setViewValue(true);
            labaRugi.add(labaUmum);
            addAccountRow("Pajak Penghasilan%",true,labaRugi);
            labaRugi.add(new ReportSubtotal("Laba/(rugi) bersih", false,labaRugi));
            
            rootList = new ArrayList();
            rootList.add(labaRugi);
            /*
            XStream xstream = new XStream();
            xstream.alias("reporttaccountvalue", ReportAccountValue.class);
            xstream.alias("reportgroup", ReportGroup.class);
            xstream.alias("reportsubtotal", ReportSubtotal.class);
            xstream.alias("reportrow", ReportRow.class);
            xstream.alias("reportemptyrow", ReportEmptyRow.class);
            
            String serializedRootList = xstream.toXML(rootList);
            System.out.println(serializedRootList);
            String[] splittedRootList = split500bytes(serializedRootList);
            String recombined = combine(splittedRootList);
            if (!recombined.equals(serializedRootList))
                System.err.println("kok ga sama sih!");
                */
        }
        
    }

}
