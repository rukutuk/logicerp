package pohaci.gumunda.titis.application;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company:PT. Pohaci Kreasi Informatika </p>
 * @author dani's
 * @version 1.0
 */

import java.awt.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.tree.*;

public class StructuredTable extends JTable {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
Vector m_vector = new Vector();
  DefaultMutableTreeNode m_root;
  int m_depth = 0, m_count = 0;

  public StructuredTable(DefaultMutableTreeNode root) {
    m_root = root;
    m_depth = m_root.getDepth();
    setColumnCount(m_root);

    setModel(new StructuredTableModel());
    buildTable();
  }

  void setColumnCount(DefaultMutableTreeNode parent){
    int iNodeIndex = parent.getChildCount();
    for(int i = 0; i < iNodeIndex; i++){
      DefaultMutableTreeNode node = (DefaultMutableTreeNode)parent.getChildAt(i);
      if(node.getDepth() == 0){
        m_vector.addElement(node);
        m_count ++;
      }
      else
        setColumnCount(node);
    }
  }

  void buildTable(){
    getTableHeader().setDefaultRenderer(new TableHeaderRenderer());
    //int originalWidth ;
    //originalWidth = getTableHeader().getPreferredSize().width;
    //getTableHeader().setPreferredSize( new Dimension( 1, (m_depth * getRowHeight()) ) );
    getTableHeader().setReorderingAllowed(false);
    
    DefaultTableModel model = (DefaultTableModel)getModel();
    model.setRowCount(0);
    model.setColumnCount(0);
    for(int i = 0; i < m_count; i++)
      model.addColumn("COL" + i);
  }

  public void rebuildTable(){
    m_vector.clear();
    m_count = 0;

    m_depth = m_root.getDepth();
    setColumnCount(m_root);
    buildTable();
  }

  public DefaultMutableTreeNode getTableHeaderRoot(){
    return m_root;
  }

  class StructuredTableModel extends DefaultTableModel{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public boolean isCellEditable(int row, int column){
      return false;
    }
  }

  class TableHeaderRenderer extends DefaultTableCellRenderer {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected JTable m_table;
    protected Dimension m_dimension = new Dimension();
    protected Font m_font = null;
    protected FontMetrics m_metrics = null;
    protected int m_column;
    protected int m_textHeight;
    protected int m_otherColumnWidth;
    protected int m_subColumnWidth;
    protected int m_firstColumn;
    protected int m_lastColumn;
    protected int m_addHeight;

    TableHeaderRenderer(){
      setHorizontalAlignment(JLabel.CENTER);
      int h= 5* StructuredTable.this.getRowHeight();
      setPreferredSize(new Dimension(1,h));
    }

    public Component getTableCellRendererComponent( JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
      setValue(value);
      setFont(table.getFont());

      m_table = table;
      m_column = column;
      return this;
    }

    public void paintComponent(Graphics graphics) {
      
      if(m_font == null) {
        m_font = getFont();
        m_metrics = graphics.getFontMetrics(m_font);
        m_textHeight = m_metrics.getAscent() + m_metrics.getDescent();
      }
      else {
        if(!m_font.equals(getFont())) {
          m_font = getFont();
          m_metrics = graphics.getFontMetrics(m_font);
          m_textHeight = m_metrics.getAscent() + m_metrics.getDescent();
        }
      }

      getSize(m_dimension);
      //graphics.setColor(Color.GREEN);
      //graphics.fillRect(0,0,m_dimension.width,m_dimension.height);
      
      m_addHeight = (m_depth - 1) * (m_dimension.height / m_depth);

      DefaultMutableTreeNode node = (DefaultMutableTreeNode)m_vector.elementAt(m_column);
      if(node.getParent() == m_root){
        
    	graphics.setColor(getBackground()); 
        graphics.fillRect(0,0,m_dimension.width,m_dimension.height);
        graphics.setColor(getForeground());
        graphics.drawString(node.toString(),
                            (m_dimension.width - m_metrics.stringWidth(node.toString())) >> 1,
                            ((m_dimension.height + m_textHeight / 2) >> 1));
        
        
        graphics.setColor(Color.WHITE);
        graphics.drawLine(0, m_dimension.height - 2, 0, 0);
        graphics.drawLine(0, 0, m_dimension.width - 1, 0);

        graphics.setColor(Color.GRAY);
        graphics.drawLine(m_dimension.width - 1, 1, m_dimension.width - 1, m_dimension.height - 1);
        graphics.drawLine(0, m_dimension.height - 1, m_dimension.width - 1, m_dimension.height - 1);
      }
      else
        paintNode(node, graphics);
    }

    void paintNode(DefaultMutableTreeNode node, Graphics graphics){
      Color colorOriginal = graphics.getColor();
      graphics.setColor(colorOriginal);//warna font dari title kolom

      m_firstColumn = m_vector.indexOf(node.getFirstLeaf());
      m_lastColumn = m_vector.indexOf(node.getLastLeaf());

      for(int i = m_firstColumn; i < m_lastColumn + 1; i ++){
        if(i != m_column)
          m_otherColumnWidth += m_table.getColumnModel().getColumn(i).getWidth();
      }

      for(int i = m_firstColumn; i < m_column; i++)
        m_subColumnWidth += m_table.getColumnModel().getColumn(i).getWidth();

      DefaultMutableTreeNode parent = (DefaultMutableTreeNode)node.getParent();
      if(parent == m_root){
        graphics.drawString(node.toString(),
                            (((m_dimension.width + m_otherColumnWidth) - m_metrics.stringWidth(node.toString())) >> 1 ) - m_subColumnWidth,
                            ((m_dimension.height / m_depth + m_textHeight / 2) >> 1));

        DefaultMutableTreeNode child = (DefaultMutableTreeNode)m_vector.elementAt(m_column);
        if(child == node.getFirstLeaf()) {
          graphics.setColor(Color.WHITE);
          graphics.drawLine(0, m_dimension.height / m_depth - 2, 0, 0);
        }

        if(child == node.getLastLeaf()){
          graphics.setColor(Color.GRAY);
          //graphics.setColor(Color.RED);
          graphics.drawLine(m_dimension.width - 1, 1, m_dimension.width - 1, m_dimension.height / m_depth - 1);
        }

        graphics.setColor(Color.WHITE);
        graphics.drawLine(0, 0, m_dimension.width , 0);
        graphics.setColor(Color.GRAY);
        graphics.drawLine(0, m_dimension.height / m_depth - 1,
                          m_dimension.width - 1, m_dimension.height / m_depth - 1);
        
        
      }else {
        if(node.getChildCount() != 0){
          graphics.drawString(node.toString(),
                              (((m_dimension.width + m_otherColumnWidth) - m_metrics.stringWidth(node.toString())) >> 1 ) - m_subColumnWidth,
                              ((m_dimension.height / m_depth + m_textHeight / 2) >> 1) + m_addHeight);


          DefaultMutableTreeNode child = (DefaultMutableTreeNode)m_vector.elementAt(m_column);
          if(child == node.getFirstLeaf()){
            graphics.setColor(Color.WHITE);
            graphics.drawLine(0, (m_addHeight + m_dimension.height / m_depth) - 2, 0, m_addHeight);
          }

          if(child == node.getLastLeaf()){
            graphics.setColor(Color.GRAY);
            graphics.drawLine(m_dimension.width - 1, (m_addHeight + 1), m_dimension.width - 1, (m_addHeight+m_dimension.height/m_depth) - 1);
          }

          graphics.setColor(Color.WHITE );
          graphics.drawLine(0, m_addHeight, m_dimension.width - 1, m_addHeight );
          graphics.setColor(Color.GRAY );
          graphics.drawLine(0, (m_addHeight + m_dimension.height / m_depth) - 1,
                            m_dimension.width - 1, (m_addHeight + m_dimension.height / m_depth) - 1 );
        }else {
          if(node.getLevel() < m_depth){
            int count = getParentCount(node);

            graphics.drawString(node.toString(),
                                (m_dimension.width - m_metrics.stringWidth(node.toString())) >> 1,
                                ((m_dimension.height / m_depth + m_textHeight / 2) >> 1) + m_addHeight - ((m_depth - count) * (m_dimension.height / m_depth)) / 2);

            graphics.setColor(Color.WHITE);
            graphics.drawLine(0, (m_addHeight + ((m_depth - count) * (m_dimension.height / m_depth))) - 2,
                              0, m_addHeight - ((m_depth - count) * (m_dimension.height / m_depth)) );
            graphics.drawLine(0, m_addHeight -((m_depth - count) * (m_dimension.height / m_depth)),
                              m_dimension.width - 1, m_addHeight - ((m_depth - count) * (m_dimension.height / m_depth)));

            graphics.setColor(Color.GRAY);
            graphics.drawLine(m_dimension.width - 1, (m_addHeight - ((m_depth - count) * (m_dimension.height / m_depth)) + 1),
                              m_dimension.width - 1, (m_addHeight + ((m_depth - count) * (m_dimension.height / m_depth))) - 1);
            graphics.drawLine(0, (m_addHeight + m_dimension.height / m_depth) - 1,
                              m_dimension.width - 1, (m_addHeight + m_dimension.height / m_depth) - 1);

            m_addHeight -= (m_depth - count) * (m_dimension.height / m_depth);
          }else{
            graphics.setColor(getBackground());
            graphics.fillRect(0, m_addHeight, m_dimension.width- 1, (m_addHeight + m_dimension.height / m_depth)- 2 );
            graphics.setColor(getForeground());  
            graphics.drawString(node.toString(),
                                (m_dimension.width - m_metrics.stringWidth(node.toString())) >> 1,
                                ((m_dimension.height / m_depth + m_textHeight / 2 ) >> 1) + m_addHeight);

            graphics.setColor(Color.WHITE);
            graphics.drawLine(0, (m_addHeight + m_dimension.height / m_depth)- 2, 0, m_addHeight);
            graphics.drawLine(0, m_addHeight, m_dimension.width - 1, m_addHeight);

            graphics.setColor(Color.GRAY);
            graphics.drawLine(m_dimension.width - 1, (m_addHeight + 1),
                              m_dimension.width - 1, (m_addHeight + m_dimension.height / m_depth) - 1);
            graphics.drawLine(0, (m_addHeight + m_dimension.height / m_depth) - 1,
                              m_dimension.width - 1, (m_addHeight + m_dimension.height / m_depth) - 1 );
          }
        }

        graphics.setColor(colorOriginal);
        m_addHeight -= m_dimension.height / m_depth;
        m_otherColumnWidth = 0;
        m_subColumnWidth = 0;

        paintNode(parent, graphics);
      }
    }

    int getParentCount(DefaultMutableTreeNode node){
      DefaultMutableTreeNode parent = (DefaultMutableTreeNode)node.getParent();
      int count = 0;
      if(parent != null){
        count ++;
        count += getParentCount(parent);
      }

      return count;
    }
  }
}