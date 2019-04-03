/*
 *  Copyright (C) 2011 Paul Scherrer Institute
 * 
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 * 
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/*
 * LinePlotPanel.java
 *
 * Created on Feb 3, 2011, 10:50:49 AM
 */

package ch.psi.fda.ui.ce.panels.model;

import ch.psi.fda.ProcessorFDA;
import ch.psi.fda.ui.ce.panels.ComponentMetadata;
import ch.psi.fda.ui.ce.panels.PanelSupport;
import ch.psi.fda.ui.ce.panels.model.util.ModelUtil;
import ch.psi.fda.model.v1.MatrixPlotArray;
import ch.psi.fda.ui.ce.panels.DocumentAdapter;
import ch.psi.fda.ui.ce.panels.EditableComponent;
import ch.psi.fda.ui.ce.panels.ObjectProvider;
import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.HashMap;
import java.util.List;
import javax.swing.event.DocumentEvent;

/**
 *
 * @author ebner
 */
public class MatrixPlotArrayPanel extends javax.swing.JPanel implements ObjectProvider<MatrixPlotArray>, EditableComponent {

    private boolean modified = false;
    private MatrixPlotArray plot;
    private PanelSupport panelSupport;

    public MatrixPlotArrayPanel(){
        this(new MatrixPlotArray());
    }

    /** Creates new form LinePlotPanel */
    public MatrixPlotArrayPanel(final MatrixPlotArray plot) {
        this.plot = plot;

        initComponents();

        ProcessorFDA.setIcon(jButton1, getClass().getResource("/ch/psi/fda/ui/ce/icons/plus.png"));        
        
        HashMap<Component, ComponentMetadata> managedFields = new HashMap<Component,ComponentMetadata>();
        managedFields.put(jTextFieldTitle, new ComponentMetadata(false));
        managedFields.put(jComboBoxY, new ComponentMetadata(true));
        managedFields.put(jComboBoxZ, new ComponentMetadata(true));
        managedFields.put(jTextFieldOffset, new ComponentMetadata(false, "0"));
        managedFields.put(jTextFieldSize, new ComponentMetadata(false, "0"));
        managedFields.put(jComboBoxType, new ComponentMetadata(false, new MatrixPlotArray().getType()));


        this.panelSupport = new PanelSupport();
        this.panelSupport.analyze(managedFields);

        // Update view
        jTextFieldTitle.setText(plot.getTitle());
        jComboBoxY.setModel(new javax.swing.DefaultComboBoxModel(new String[]{ModelUtil.getInstance().getId(plot.getY()) }));
        jComboBoxZ.setModel(new javax.swing.DefaultComboBoxModel(new String[]{ModelUtil.getInstance().getId(plot.getZ()) }));
        jTextFieldOffset.setText(plot.getOffset()+"");
        jTextFieldSize.setText(plot.getSize()+"");
        if(plot.getType()!=null){
            jComboBoxType.setSelectedItem(plot.getType());
        }
        

        // Establish bindings
        jTextFieldTitle.getDocument().addDocumentListener(new DocumentAdapter() {
            @Override
            public void valueChange(DocumentEvent de) {
                modified = true;
                plot.setTitle(jTextFieldTitle.getText());
            }
        });
        jComboBoxY.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent ie) {
                modified = true;
                plot.setY(ModelUtil.getInstance().getObject((String)jComboBoxY.getSelectedItem()));
            }
        });
        jComboBoxZ.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent ie) {
                modified = true;
                plot.setZ(ModelUtil.getInstance().getObject((String)jComboBoxZ.getSelectedItem()));
            }
        });
        jTextFieldOffset.getDocument().addDocumentListener(new DocumentAdapter() {
            @Override
            public void valueChange(DocumentEvent de) {
                modified = true;
                int off = 0;
                try{
                    off = Integer.parseInt(jTextFieldOffset.getText());
                }
                catch(NumberFormatException e){
                }
                plot.setOffset(off);
            }
        });
        jTextFieldSize.getDocument().addDocumentListener(new DocumentAdapter() {
            @Override
            public void valueChange(DocumentEvent de) {
                modified = true;
                int si = 0;
                try{
                    si = Integer.parseInt(jTextFieldSize.getText());
                }
                catch(NumberFormatException e){
                }
                plot.setSize(si);
            }
        });
        jComboBoxType.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent ie) {
                modified = true;
                plot.setType((String) jComboBoxType.getSelectedItem());
            }
        });


        this.panelSupport.manage(this, managedFields, jButton1);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jTextFieldTitle = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jComboBoxY = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();
        jComboBoxZ = new javax.swing.JComboBox();
        jLabel5 = new javax.swing.JLabel();
        jTextFieldOffset = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jTextFieldSize = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jComboBoxType = new javax.swing.JComboBox();
        jButton1 = new javax.swing.JButton();

        jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jLabel1.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        jLabel1.setLabelFor(jTextFieldTitle);
        jLabel1.setText("Title:");
        jPanel1.add(jLabel1);

        jTextFieldTitle.setToolTipText("Title of the plot");
        jTextFieldTitle.setName("Title"); // NOI18N
        jTextFieldTitle.setPreferredSize(new java.awt.Dimension(120, 28));
        jPanel1.add(jTextFieldTitle);

        jLabel3.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        jLabel3.setLabelFor(jComboBoxY);
        jLabel3.setText("Y:");
        jPanel1.add(jLabel3);

        jComboBoxY.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "none" }));
        jComboBoxY.setToolTipText("y-Axis");
        jComboBoxY.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
                jComboBoxYPopupMenuWillBecomeVisible(evt);
            }
        });
        jPanel1.add(jComboBoxY);

        jLabel4.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        jLabel4.setLabelFor(jComboBoxZ);
        jLabel4.setText("Z:");
        jPanel1.add(jLabel4);

        jComboBoxZ.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "none" }));
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("ch/psi/fda/ui/ce/panels/model/Bundle"); // NOI18N
        jComboBoxZ.setToolTipText(bundle.getString("MatrixPlotArrayPanel.jComboBoxZ.toolTipText")); // NOI18N
        jComboBoxZ.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
                jComboBoxZPopupMenuWillBecomeVisible(evt);
            }
        });
        jPanel1.add(jComboBoxZ);

        jLabel5.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        jLabel5.setLabelFor(jTextFieldOffset);
        jLabel5.setText("Offset:");
        jPanel1.add(jLabel5);

        jTextFieldOffset.setName("Offset"); // NOI18N
        jTextFieldOffset.setPreferredSize(new java.awt.Dimension(40, 28));
        jPanel1.add(jTextFieldOffset);

        jLabel2.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        jLabel2.setLabelFor(jTextFieldSize);
        jLabel2.setText("Size:");
        jPanel1.add(jLabel2);

        jTextFieldSize.setToolTipText("Size of array specified as Z");
        jTextFieldSize.setName("Size"); // NOI18N
        jTextFieldSize.setPreferredSize(new java.awt.Dimension(40, 28));
        jPanel1.add(jTextFieldSize);

        jLabel6.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        jLabel6.setLabelFor(jComboBoxType);
        jLabel6.setText("Type:");
        jPanel1.add(jLabel6);

        jComboBoxType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "2D", "3D" }));
        jComboBoxType.setToolTipText("Operation to perform");
        jComboBoxType.setName("Type"); // NOI18N
        jPanel1.add(jComboBoxType);

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ch/psi/fda/ui/ce/icons/plus.png"))); // NOI18N
        jButton1.setBorderPainted(false);
        jButton1.setContentAreaFilled(false);
        jPanel1.add(jButton1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jComboBoxYPopupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_jComboBoxYPopupMenuWillBecomeVisible
        // Get selected item
        Object o = jComboBoxY.getSelectedItem();
        // Update the possible options
        List<String> li = ModelUtil.getInstance().getIds();
        jComboBoxY.setModel(new javax.swing.DefaultComboBoxModel(li.toArray(new String[li.size()])));
        // Set the item selected that was selected before
        jComboBoxY.setSelectedItem(o);
    }//GEN-LAST:event_jComboBoxYPopupMenuWillBecomeVisible

    private void jComboBoxZPopupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_jComboBoxZPopupMenuWillBecomeVisible
        // Get selected item
        Object o = jComboBoxZ.getSelectedItem();
        // Update the possible options
        List<String> li = ModelUtil.getInstance().getIds();
        jComboBoxZ.setModel(new javax.swing.DefaultComboBoxModel(li.toArray(new String[li.size()])));
        // Set the item selected that was selected before
        jComboBoxZ.setSelectedItem(o);
    }//GEN-LAST:event_jComboBoxZPopupMenuWillBecomeVisible

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JComboBox jComboBoxType;
    private javax.swing.JComboBox jComboBoxY;
    private javax.swing.JComboBox jComboBoxZ;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField jTextFieldOffset;
    private javax.swing.JTextField jTextFieldSize;
    private javax.swing.JTextField jTextFieldTitle;
    // End of variables declaration//GEN-END:variables

    @Override
    public MatrixPlotArray getObject() {
        return plot;
    }

    /**
     * Update ID's shown in the combo box
     */
    public void updateIds(){
        jComboBoxY.setModel(new javax.swing.DefaultComboBoxModel(new String[]{ModelUtil.getInstance().getId(plot.getY()) }));
        jComboBoxZ.setModel(new javax.swing.DefaultComboBoxModel(new String[]{ModelUtil.getInstance().getId(plot.getZ()) }));
    }

    @Override
    public boolean modified() {
        boolean m = modified;
        modified = false;
        return m;
    }

    @Override
    public void clearModified() {
        modified = false;
    }
}
