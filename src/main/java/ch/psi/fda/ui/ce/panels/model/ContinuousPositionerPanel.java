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
 * ContinuousPositionerPanel.java
 *
 * Created on Jan 24, 2011, 11:48:07 AM
 */

package ch.psi.fda.ui.ce.panels.model;

import ch.psi.fda.ProcessorFDA;
import ch.psi.fda.ui.ce.panels.ComponentMetadata;
import ch.psi.fda.ui.ce.panels.FieldUtilities;
import ch.psi.fda.ui.ce.panels.PanelSupport;
import ch.psi.fda.ui.ce.panels.WrapLayout;
import ch.psi.fda.model.v1.ContinuousPositioner;
import ch.psi.fda.ui.ce.panels.DocumentAdapter;
import ch.psi.fda.ui.ce.panels.EditableComponent;
import ch.psi.fda.ui.ce.panels.IdInputVerifier;
import ch.psi.fda.ui.ce.panels.ObjectProvider;
import ch.psi.fda.ui.ce.panels.model.util.ModelUtil;
import java.awt.Component;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.HashMap;
import javax.swing.JFormattedTextField;
import javax.swing.event.DocumentEvent;

/**
 *
 * @author ebner
 */
public class ContinuousPositionerPanel extends javax.swing.JPanel implements ObjectProvider<ContinuousPositioner>, EditableComponent {

    private boolean modified = false;
    private ContinuousPositioner positioner;
    private PanelSupport panelSupport;
    private WrapLayout manager;

    public ContinuousPositionerPanel() {
        this(new ContinuousPositioner());
    }

    /** Creates new form ContinuousPositionerPanel */
    public ContinuousPositionerPanel(final ContinuousPositioner positioner) {
        if (positioner.getId() == null || positioner.getId().equals("")) {
            positioner.setId(IdGenerator.generateId());
        }
        this.positioner = positioner;

        initComponents();
        
        ProcessorFDA.setIcon(jButton1, getClass().getResource("/ch/psi/fda/ui/ce/icons/plus.png"));        

        HashMap<Component, ComponentMetadata> managedFields = new HashMap<Component,ComponentMetadata>();
        managedFields.put(jTextFieldName, new ComponentMetadata(true));
        managedFields.put(jTextFieldReadback, new ComponentMetadata(false));
        managedFields.put(jFormattedTextFieldStart, new ComponentMetadata(true));
        managedFields.put(jFormattedTextFieldEnd, new ComponentMetadata(true));
        managedFields.put(jFormattedTextFieldStepSize, new ComponentMetadata(true));
        managedFields.put(jFormattedTextFieldIntegrationTime, new ComponentMetadata(true));
        managedFields.put(jFormattedTextFieldBacklash, new ComponentMetadata(false));

        this.panelSupport = new PanelSupport();
        this.panelSupport.analyze(managedFields);

        
        // Update view
        jTextFieldId.setText(positioner.getId());
        jTextFieldName.setText(positioner.getName());
        jTextFieldReadback.setText(positioner.getReadback());
        jFormattedTextFieldStart.setText(positioner.getStart()+"");
        jFormattedTextFieldEnd.setText(positioner.getEnd()+"");
        jFormattedTextFieldStepSize.setText(positioner.getStepSize()+"");
        jFormattedTextFieldIntegrationTime.setText(positioner.getIntegrationTime()+"");
        if(positioner.getAdditionalBacklash()!=null){
            jFormattedTextFieldBacklash.setText(positioner.getAdditionalBacklash()+"");
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
        jTextFieldName.getDocument().addDocumentListener(new DocumentAdapter() {
            @Override
            public void valueChange(DocumentEvent de) {
                modified = true;
                positioner.setName(panelSupport.getValue(jTextFieldName));
            }
        });
        jTextFieldReadback.getDocument().addDocumentListener(new DocumentAdapter() {
            @Override
            public void valueChange(DocumentEvent de) {
                modified = true;
                positioner.setReadback(panelSupport.getValue(jTextFieldReadback));
            }
        });
        jFormattedTextFieldStart.getDocument().addDocumentListener(new DocumentAdapter() {
            @Override
            public void valueChange(DocumentEvent de) {
                modified = true;
                String value = panelSupport.getValue(jFormattedTextFieldStart);
                if(value != null){
                    try{
                        positioner.setStart(new Double(value));
                    }
                    catch(NumberFormatException e){
                        positioner.setStart(0);
                    }
                }
                else{
                    positioner.setStart(0);
                }
            }
        });
        jFormattedTextFieldEnd.getDocument().addDocumentListener(new DocumentAdapter() {
            @Override
            public void valueChange(DocumentEvent de) {
                modified = true;
                String value = panelSupport.getValue(jFormattedTextFieldEnd);
                if(value != null){
                    try{
                        positioner.setEnd(new Double(value));
                    }
                    catch(NumberFormatException e){
                        positioner.setEnd(0);
                    }
                }
                else{
                    positioner.setEnd(0);
                }
            }
        });
        jFormattedTextFieldStepSize.getDocument().addDocumentListener(new DocumentAdapter() {
            @Override
            public void valueChange(DocumentEvent de) {
                modified = true;
                String value = panelSupport.getValue(jFormattedTextFieldStepSize);
                if(value != null){
                    try{
                        positioner.setStepSize(new Double(value));
                    }
                    catch(NumberFormatException e){
                        positioner.setStepSize(0);
                        
                    }
                }
                else{
                    positioner.setStepSize(0);
                }
            }
        });
        jFormattedTextFieldIntegrationTime.getDocument().addDocumentListener(new DocumentAdapter() {
            @Override
            public void valueChange(DocumentEvent de) {
                modified = true;
                String value = panelSupport.getValue(jFormattedTextFieldIntegrationTime);
                if(value != null){
                    try{
                        positioner.setIntegrationTime(new Double(value));
                    }
                    catch(NumberFormatException e){
                        positioner.setIntegrationTime(0);
                    }
                }
                else{
                    positioner.setIntegrationTime(0);
                }
            }
        });
        jFormattedTextFieldBacklash.getDocument().addDocumentListener(new DocumentAdapter() {
            @Override
            public void valueChange(DocumentEvent de) {
                modified = true;
                String value = panelSupport.getValue(jFormattedTextFieldBacklash);
                if(value != null){
                    try{
                        positioner.setAdditionalBacklash(new Double(value));
                    }
                    catch(NumberFormatException e){
                        positioner.setAdditionalBacklash(null);
                    }
                }
                else{
                    positioner.setAdditionalBacklash(null);
                }
            }
        });


        this.panelSupport.manage(this, managedFields, jButton1);


        manager = new WrapLayout(WrapLayout.LEADING);
        jPanel1.setLayout(manager);
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
        jTextFieldId = new javax.swing.JTextField();
        jTextFieldName = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jTextFieldReadback = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jFormattedTextFieldStart = new JFormattedTextField(FieldUtilities.getDecimalFormat());
        jLabel4 = new javax.swing.JLabel();
        jFormattedTextFieldEnd = new JFormattedTextField(FieldUtilities.getDecimalFormat());
        jLabel5 = new javax.swing.JLabel();
        jFormattedTextFieldStepSize = new JFormattedTextField(FieldUtilities.getDecimalFormat());
        jLabel6 = new javax.swing.JLabel();
        jFormattedTextFieldIntegrationTime = new JFormattedTextField(FieldUtilities.getDecimalFormat());
        jLabel7 = new javax.swing.JLabel();
        jFormattedTextFieldBacklash = new JFormattedTextField(FieldUtilities.getDecimalFormat());
        jButton1 = new javax.swing.JButton();

        jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jLabel1.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        jLabel1.setLabelFor(jTextFieldId);
        jLabel1.setText("Id:");
        jPanel1.add(jLabel1);

        jTextFieldId.setBackground(getBackground());
        jTextFieldId.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        jTextFieldId.setForeground(new java.awt.Color(102, 102, 102));
        jTextFieldId.setText("Id");
        jTextFieldId.setBorder(null);
        jTextFieldId.setInputVerifier(new IdInputVerifier());
        jTextFieldId.setPreferredSize(new java.awt.Dimension(80, 16));
        jPanel1.add(jTextFieldId);

        jTextFieldName.setText("Channel Name");
        jTextFieldName.setToolTipText("Channel Name");
        jTextFieldName.setName("Name"); // NOI18N
        jTextFieldName.setPreferredSize(new java.awt.Dimension(180, 28));
        jPanel1.add(jTextFieldName);

        jLabel2.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        jLabel2.setLabelFor(jTextFieldReadback);
        jLabel2.setText("Readback:");
        jPanel1.add(jLabel2);

        jTextFieldReadback.setToolTipText("Readback");
        jTextFieldReadback.setName("Readback"); // NOI18N
        jTextFieldReadback.setPreferredSize(new java.awt.Dimension(180, 28));
        jPanel1.add(jTextFieldReadback);

        jLabel3.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        jLabel3.setLabelFor(jFormattedTextFieldStart);
        jLabel3.setText("Start:");
        jPanel1.add(jLabel3);

        jFormattedTextFieldStart.setToolTipText("Start");
        jFormattedTextFieldStart.setName("Start"); // NOI18N
        jFormattedTextFieldStart.setPreferredSize(new java.awt.Dimension(80, 28));
        jPanel1.add(jFormattedTextFieldStart);

        jLabel4.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        jLabel4.setLabelFor(jFormattedTextFieldEnd);
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("ch/psi/fda/ui/ce/panels/model/Bundle"); // NOI18N
        jLabel4.setText(bundle.getString("ContinuousPositionerPanel.jLabel4.text")); // NOI18N
        jPanel1.add(jLabel4);

        jFormattedTextFieldEnd.setToolTipText("End");
        jFormattedTextFieldEnd.setName("End"); // NOI18N
        jFormattedTextFieldEnd.setPreferredSize(new java.awt.Dimension(80, 28));
        jPanel1.add(jFormattedTextFieldEnd);

        jLabel5.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        jLabel5.setLabelFor(jFormattedTextFieldStepSize);
        jLabel5.setText("Step Size:");
        jPanel1.add(jLabel5);

        jFormattedTextFieldStepSize.setToolTipText("Step Size");
        jFormattedTextFieldStepSize.setName("Step Size"); // NOI18N
        jFormattedTextFieldStepSize.setPreferredSize(new java.awt.Dimension(80, 28));
        jPanel1.add(jFormattedTextFieldStepSize);

        jLabel6.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        jLabel6.setLabelFor(jFormattedTextFieldIntegrationTime);
        jLabel6.setText("Integration Time:");
        jPanel1.add(jLabel6);

        jFormattedTextFieldIntegrationTime.setToolTipText("Integration Time");
        jFormattedTextFieldIntegrationTime.setName("Integration Time"); // NOI18N
        jFormattedTextFieldIntegrationTime.setPreferredSize(new java.awt.Dimension(80, 28));
        jPanel1.add(jFormattedTextFieldIntegrationTime);

        jLabel7.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        jLabel7.setLabelFor(jFormattedTextFieldBacklash);
        jLabel7.setText("Additional Backlash:");
        jPanel1.add(jLabel7);

        jFormattedTextFieldBacklash.setText(bundle.getString("ContinuousPositionerPanel.jFormattedTextFieldBacklash.text")); // NOI18N
        jFormattedTextFieldBacklash.setToolTipText("Additional backlash");
        jFormattedTextFieldBacklash.setName("Additional backlash"); // NOI18N
        jFormattedTextFieldBacklash.setPreferredSize(new java.awt.Dimension(80, 28));
        jPanel1.add(jFormattedTextFieldBacklash);

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ch/psi/fda/ui/ce/icons/plus.png"))); // NOI18N
        jButton1.setToolTipText("Add optional item");
        jButton1.setBorderPainted(false);
        jButton1.setContentAreaFilled(false);
        jPanel1.add(jButton1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 735, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JFormattedTextField jFormattedTextFieldBacklash;
    private javax.swing.JFormattedTextField jFormattedTextFieldEnd;
    private javax.swing.JFormattedTextField jFormattedTextFieldIntegrationTime;
    private javax.swing.JFormattedTextField jFormattedTextFieldStart;
    private javax.swing.JFormattedTextField jFormattedTextFieldStepSize;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField jTextFieldId;
    private javax.swing.JTextField jTextFieldName;
    private javax.swing.JTextField jTextFieldReadback;
    // End of variables declaration//GEN-END:variables

    @Override
    public ContinuousPositioner getObject() {
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
