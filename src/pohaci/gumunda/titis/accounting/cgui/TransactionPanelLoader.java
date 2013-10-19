/**
 * 
 */
package pohaci.gumunda.titis.accounting.cgui;

import java.awt.Container;
import java.awt.Cursor;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JPanel;

import pohaci.gumunda.cgui.GumundaMainFrame;
import pohaci.gumunda.titis.accounting.cgui.panelloader.ITabbedTransactionPanel;
import pohaci.gumunda.titis.accounting.dbapi.IDBConstants;
import pohaci.gumunda.titis.accounting.entity.TransactionPanelMap;
import pohaci.gumunda.titis.accounting.logic.TransactionMapper;
import pohaci.gumunda.titis.accounting.logic.TransactionPanelMapper;
import pohaci.gumunda.titis.application.FrameMain;
import pohaci.gumunda.titis.application.InternalFrame;
import pohaci.gumunda.titis.application.db.GenericMapper;
import pohaci.gumunda.titis.application.db.MasterMap;

/**
 * @author dark-knight
 *
 */
public class TransactionPanelLoader {
	
	private Connection conn;
	private JPanel source;
	private long sessionid;

	public TransactionPanelLoader(Connection conn, JPanel source, long sessionid) {
		this.conn = conn;
		this.source = source;
		this.sessionid = sessionid;
	}

	public void openPanel(Transaction transaction) throws Exception {
		// get standard journal setting
		GenericMapper mapper = MasterMap.obtainMapperFor(JournalStandardSetting.class);
		mapper.setActiveConn(conn);
		List list = 
			mapper.doSelectWhere("JOURNAL=" + transaction.getJournalStandard().getIndex());
		
		if(list.size()==0)
			throw new Exception("There is no standard journal setting related to this transaction");
		
		// get the panel list based on standard journal setting
		Iterator iterator = list.iterator();
		boolean isNotMapped = false;
		while(iterator.hasNext()){
			JournalStandardSetting setting = (JournalStandardSetting) iterator.next();
			
			String appName = setting.getApplication();
			List mapList = getTransactionPanelMap(appName);
			
			if(mapList.size()==0)
				isNotMapped = true;
			
			Iterator iter = mapList.iterator();
			while(iter.hasNext()){
				TransactionPanelMap map = (TransactionPanelMap) iter.next();
				createAndLoadPanel(map, transaction);
			}
			if(iterator.hasNext())
				isNotMapped = false;
		}
		
		if(isNotMapped)
			throw new Exception("There is no panel mapper related to this transaction");
	}

	private void createAndLoadPanel(TransactionPanelMap map, Transaction transaction) throws Exception {
		Class panelClass = map.getPanelClass();
		
		// open it
		source.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		
		FrameMain mainFrame = (FrameMain) GumundaMainFrame.getMainFrame();
		
		InternalFrame frame = new InternalFrame(map.getTitle(), mainFrame);
		
		Constructor constructor;
		//RevTransactionPanel panel = null;
		Container object = null;
		try {
			constructor = panelClass.getConstructor(new Class[]{Connection.class, long.class});
			object = (Container) constructor.newInstance(new Object[]{conn, new Long(sessionid)});
			frame.setContentPane((Container) object);
		} catch (SecurityException e) {
			source.setCursor( Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			throw new Exception("There is an error while loading panel");
		} catch (NoSuchMethodException e) {
			source.setCursor( Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			throw new Exception("There is an error while loading panel");
		} catch (IllegalArgumentException e) {
			source.setCursor( Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			throw new Exception("There is an error while loading panel");
		} catch (InstantiationException e) {
			source.setCursor( Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			throw new Exception("There is an error while loading panel");
		} catch (IllegalAccessException e) {
			source.setCursor( Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			throw new Exception("There is an error while loading panel");
		} catch (InvocationTargetException e) {
			source.setCursor( Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			throw new Exception("There is an error while loading panel");
		}

		frame.setSize(map.getWidth(), map.getHeight());
		mainFrame.m_desktopPane.add(frame);
		frame.setVisible(true);
		try {
			frame.setSelected( true );
		}
		catch( Exception exception ) {
			source.setCursor( Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			throw new Exception("There is an error while loading panel");
		}

		// load object
		List list = getClassList(map.getApplication());
		
		Iterator iterator = list.iterator();
		while(iterator.hasNext()){
			Class clazz = (Class) iterator.next();
			GenericMapper mapper = MasterMap.obtainMapperFor(clazz);
			mapper.setActiveConn(conn);
			List objectList = (List) mapper.doSelectWhere(IDBConstants.ATTR_TRANSACTION + "=" + transaction.getIndex());
			
			// okay, it's time to load object
			Iterator iterator1 = objectList.iterator();
			while(iterator1.hasNext()){
				StateTemplateEntity obj = (StateTemplateEntity) iterator1.next();
				
				if (object instanceof ITabbedTransactionPanel) {
					ITabbedTransactionPanel panel = (ITabbedTransactionPanel) object;
					panel.openAndLoadObject(obj);
				}else{
					RevTransactionPanel panel = (RevTransactionPanel) object;
					panel.doLoad(obj);
				}
			}
		}
		
		source.setCursor( Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}

	private List getTransactionPanelMap(String appName) {
		ArrayList list = new ArrayList();
		Iterator iter = TransactionPanelMapper.getList().iterator();
		while(iter.hasNext()){
			TransactionPanelMap map = (TransactionPanelMap) iter.next();
			if(map.getApplication().equals(appName))
				
				list.add(map);
		}
		return list;
	}
	
	private List getClassList(String appName) {
		ArrayList list = new ArrayList();
		Iterator iter = TransactionMapper.getList().iterator();
		while(iter.hasNext()){
			List myList = (List) iter.next();
			if(((String)myList.get(0)).equals(appName))
				list.add((Class)myList.get(1));
		}
		return list;
	}
}
