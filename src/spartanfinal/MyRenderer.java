/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spartanfinal;

import java.awt.Component;
import javax.swing.ImageIcon;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author hchapa
 */
public class MyRenderer extends DefaultTableCellRenderer{
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if(isSelected)
        {
           
            this.setBackground(table.getSelectionBackground());
            this.setForeground(table.getSelectionForeground());
        }
        else{
            this.setBackground(table.getBackground());
            this.setForeground(table.getForeground());
        }
       
        if(value != null)
        {
            setIcon(new ImageIcon(value.toString()));
        }
        return this; 
    }
    
    
    
}
