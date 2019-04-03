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
 * PseudoPositionerPanel.java
 *
 * Created on Jan 24, 2011, 11:41:04 AM
 */

package ch.psi.fda.ui.ce.panels.model;

import ch.psi.fda.ProcessorFDA;
import ch.psi.fda.ui.ce.panels.ComponentMetadata;
import ch.psi.fda.ui.ce.panels.FieldUtilities;
import ch.psi.fda.ui.ce.panels.PanelSupport;
import ch.psi.fda.model.v1.PseudoPositioner;
import ch.psi.fda.ui.ce.panels.DocumentAdapter;
import ch.psi.fda.ui.ce.panels.EditableComponent;
import ch.psi.fda.ui.ce.panels.IdInputVerifier;
import ch.psi.fda.ui.ce.panels.ObjectProvider;
import ch.psi.fda.ui.ce.panels.model.util.ModelUtil;
import java.awt.Component;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import javax.swing.JFormattedTextField;
import javax.swing.event.DocumentEvent;

/**
 *
 * @author ebner
 */
public class PseudoPositionerPanel extends javax.swing.JPanel implements ObjectProvider<PseudoPositioner>, EditableComponent {

    private boolean modified = false;
    private PseudoPositioner positioner;
    private PanelSupport panelSupport;

    public PseudoPositionerPanel() {
        this(new PseudoPositioner());
    }

    /** Creates new form PseudoPositionerPanel */
    public PseudoPositionerPanel(final PseudoPositioner positioner) {
        if (positioner.getId() == null || positioner.getId().equals("")) {
            positioner.setId(IdGenerator.generateId());
        }
        this.positioner = positioner;

        initComponents();
        
        ProcessorFDA.setIcon(jButton1, getClass().getResource("/ch/psi/fda/ui/ce/icons/plus.png"));                

        HashMap<Component, ComponentMetadata> managedFields = new HashMap<Component,ComponentMetadata>();
        managedFields.put(jFormattedTextFieldCount, new ComponentMetadata(true));
        managedFields.put(jFormattedTextFieldSettlingTime, new ComponentMetadata(false, "0.0"));
        managedFields.put(jTextFieldDone, new ComponentMetadata(false));
        managedFields.put(jFormattedTextFieldDoneValue, new ComponentMetadata(false, "1"));
        managedFields.put(jFormattedTextFieldDoneDelay, new ComponentMetadata(false, "0.0"));
        managedFields.put(jComboBoxType, new ComponentMetadata(false, "Integer"));

        this.panelSupport = new PanelSupport();
        this.panelSupport.analyze(managedFields);


        // Update view
        jTextFieldId.setText(positioner.getId());
        jFormattedTextFieldCount.setText(positioner.getCounts()+"");
        jFormattedTextFieldSettlingTime.setText(positioner.getSettlingTime()+"");
        jTextFieldDone.setText(positioner.getDone());
        jFormattedTextFieldDoneValue.setText(positioner.getDoneValue()+"");
        jFormattedTextFieldDoneDelay.setText(positioner.getDoneDelay()+"");
         if(positioner.getType()!=null){
            jComboBoxType.setSelectedItem(positioner.getType());
        }

        // Establish bindings
        jTextFieldId.getDocument().addDocumentListener(new DocumentAdapter() {
            @Override
            public void valueChange(DocumentEvent de) {
                modified = true;
                positioner.setId(jTextFieldId.getText());
            }
        });
        jTextFieldId.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent fe) {
                // Workaround to update shown ids in visualizations and manipulations
                ModelUtil.getInstance().refreshIds();
            }
        });
        jFormattedTextFieldCount.getDocument().addDocumentListener(new DocumentAdapter() {
            @Override
            public void valueChange(DocumentEvent de) {
                modified = true;
                String value = panelSupport.getValue(jFormattedTextFieldCount);
                if(value != null){
                    try{
                        positioner.setCounts(new Integer(value));
                    }
                    catch(NumberFormatException e){
                        positioner.setCounts(1);
                    }
                }
                else{
                    positioner.setCounts(1);
                }
            }
        });
        jFormattedTextFieldSettlingTime.getDocument().addDocumentListener(new DocumentAdapter() {
            @Override
            public void valueChange(DocumentEvent de) {
                modified = true;
                String value = panelSupport.getValue(jFormattedTextFieldSettlingTime);
                if(value != null){
                    try{
                        positioner.setSettlingTime(new Double(value));
                    }
                    catch(NumberFormatException e){
                        positioner.setSettlingTime(null);
                    }
                }
                else{
                    positioner.setSettlingTime(null);
                }
            }
        });
        jTextFieldDone.getDocument().addDocumentListener(new DocumentAdapter() {
            @Override
            public void valueChange(DocumentEvent de) {
                modified = true;
                String value = panelSupport.getValue(jTextFieldDone);
                positioner.setDone(value);
            }
        });
        jFormattedTextFieldDoneValue.getDocument().addDocumentListener(new DocumentAdapter() {
            @Override
            public void valueChange(DocumentEvent de) {
                modified = true;
                String value = panelSupport.getValue(jFormattedTextFieldDoneValue);
                if(value!=null){
                    try{
                        positioner.setDoneValue(value);
                    }
                    catch(NumberFormatException e){
                        positioner.setDoneValue(null);
                    }
                }
                else{
                    positioner.setDoneValue(null);
                }
            }
        });
        jFormattedTextFieldDoneDelay.getDocument().addDocumentListener(new DocumentAdapter() {
            @Override
            public void valueChange(DocumentEvent de) {
                modified = true;
                String value = panelSupport.getValue(jFormattedTextFieldDoneDelay);
                if(value != null){
                    try{
                        positioner.setDoneDelay(new Double(value));
                    }
                    catch(NumberFormatException e){
                        positioner.setDoneDelay(null);
                    }
                }
                else{
                    positioner.setDoneDelay(null);
                }
            }
        });
        jComboBoxType.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent ie) {
                modified = true;
                positioner.setType((String) jComboBoxType.getSelectedItem());
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
        jLabel2 = new javax.swing.JLabel();
        jTextFieldId = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jFormattedTextFieldCount = new JFormattedTextField(FieldUtilities.getIntegerFormat());
        jLabel3 = new javax.swing.JLabel();
        jFormattedTextFieldSettlingTime = new JFormattedTextField(FieldUtilities.getDecimalFormat());
        jLabel4 = new javax.swing.JLabel();
        jTextFieldDone = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jFormattedTextFieldDoneValue = new JFormattedTextField(FieldUtilities.getIntegerFormat());
        jLabel7 = new javax.swing.JLabel();
        jComboBoxType = new javax.swing.JComboBox();
        jLabel6 = new javax.swing.JLabel();
        jFormattedTextFieldDoneDelay = new JFormattedTextField(FieldUtilities.getDecimalFormat());
        jButton1 = new javax.swing.JButton();

        jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jLabel2.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        jLabel2.setText("Id:");
        jPanel1.add(jLabel2);

        jTextFieldId.setBackground(getBackground());
        jTextFieldId.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        jTextFieldId.setForeground(new java.awt.Color(102, 102, 102));
        jTextFieldId.setText("Id");
        jTextFieldId.setBorder(null);
        jTextFieldId.setInputVerifier(new IdInputVerifier());
        jTextFieldId.setPreferredSize(new java.awt.Dimension(80, 16));
        jPanel1.add(jTextFieldId);

        jLabel1.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        jLabel1.setLabelFor(jFormattedTextFieldCount);
        jLabel1.setText("Count:");
        jPanel1.add(jLabel1);

        jFormattedTextFieldCount.setToolTipText("Number of pseudo steps");
        jFormattedTextFieldCount.setMinimumSize(new java.awt.Dimension(0, 0));
        jFormattedTextFieldCount.setPreferredSize(new java.awt.Dimension(80, 28));
        jPanel1.add(jFormattedTextFieldCount);

        jLabel3.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        jLabel3.setLabelFor(jFormattedTextFieldSettlingTime);
        jLabel3.setText("Settling Time:");
        jPanel1.add(jLabel3);

        jFormattedTextFieldSettlingTime.setName("Settling Time"); // NOI18N
        jFormattedTextFieldSettlingTime.setPreferredSize(new java.awt.Dimension(80, 28));
        jPanel1.add(jFormattedTextFieldSettlingTime);

        jLabel4.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        jLabel4.setLabelFor(jTextFieldDone);
        jLabel4.setText("Done:");
        jPanel1.add(jLabel4);

        jTextFieldDone.setName("Done"); // NOI18N
        jTextFieldDone.setPreferredSize(new java.awt.Dimension(180, 28));
        jPanel1.add(jTextFieldDone);

        jLabel5.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        jLabel5.setLabelFor(jFormattedTextFieldDoneValue);
        jLabel5.setText("Done Value:");
        jPanel1.add(jLabel5);

        jFormattedTextFieldDoneValue.setName("Done Value"); // NOI18N
        jFormattedTextFieldDoneValue.setPreferredSize(new java.awt.Dimension(80, 28));
        jPanel1.add(jFormattedTextFieldDoneValue);

        jLabel7.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        jLabel7.setLabelFor(jComboBoxType);
        jLabel7.setText("Type:");
        jPanel1.add(jLabel7);

        jComboBoxType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Integer", "String", "Double" }));
        jComboBoxType.setName("Done Value Type"); // NOI18N
        jPanel1.add(jComboBoxType);

        jLabel6.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        jLabel6.setLabelFor(jFormattedTextFieldDoneDelay);
        jLabel6.setText("Done Delay:");
        jPanel1.add(jLabel6);

        jFormattedTextFieldDoneDelay.setName("Done Delay"); // NOI18N
        jFormattedTextFieldDoneDelay.setPreferredSize(new java.awt.Dimension(80, 28));
        jPanel1.add(jFormattedTextFieldDoneDelay);

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ch/psi/fda/ui/ce/icons/plus.png"))); // NOI18N
        jButton1.setBorderPainted(false);
        jButton1.setContentAreaFilled(false);
        jPanel1.add(jButton1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 938, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JComboBox jComboBoxType;
    private javax.swing.JFormattedTextField jFormattedTextFieldCount;
    private javax.swing.JFormattedTextField jFormattedTextFieldDoneDelay;
    private javax.swing.JFormattedTextField jFormattedTextFieldDoneValue;
    private javax.swing.JFormattedTextField jFormattedTextFieldSettlingTime;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField jTextFieldDone;
    private javax.swing.JTextField jTextFieldId;
    // End of variables declaration//GEN-END:variables

    @Override
    public PseudoPositioner getObject() {
        return positioner;
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
