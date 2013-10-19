package pohaci.gumunda.titis.accounting.cgui.bypass;

import java.awt.*;
import java.sql.Connection;
import java.util.Enumeration;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;

public abstract class GumundaMainFrame extends JFrame
{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected Connection m_conn;
    protected long m_sessionid;
    static GumundaMainFrame m_owner = null;

    public long getSessionID()
    {
        return m_sessionid;
    }

    public Connection getConnection()
    {
        return m_conn;
    }

    public GumundaMainFrame()
        throws HeadlessException
    {
        m_sessionid = -1L;
        m_owner = this;
        setIconImage(Toolkit.getDefaultToolkit().getImage("../images/gumundaico.gif"));
    }

    public void setFontUIResource()
    {
        try
        {
            FontUIResource f = new FontUIResource("Tahoma", 0, 11);
            Enumeration keys = UIManager.getDefaults().keys();
            do
            {
                if(!keys.hasMoreElements())
                {
                    break;
                }
                Object key = keys.nextElement();
                Object value = UIManager.get(key);
                if(value instanceof FontUIResource)
                {
                    UIManager.put(key, f);
                }
            } while(true);
        }
        catch(Exception ex)
        {
            System.out.println(ex);
        }
    }

    public abstract void init();

    public abstract void deInit();

    public static GumundaMainFrame getMainFrame()
    {
        return m_owner;
    }

}
