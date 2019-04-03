/*
 *  Copyright (C) 2010 Paul Scherrer Institute
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
 * ChannelActionPanel.java
 *
 * Created on Dec 22, 2010, 3:45:38 PM
 */

package ch.psi.fda.ui.ce.panels.model;

import ch.psi.fda.ProcessorFDA;
import ch.psi.fda.ui.ce.panels.ComponentMetadata;
import ch.psi.fda.ui.ce.panels.FieldUtilities;
import ch.psi.fda.ui.ce.panels.PanelSupport;
import ch.psi.fda.ui.ce.panels.WrapLayout;
import ch.psi.fda.model.v1.ChannelAction;
import ch.psi.fda.ui.ce.panels.DocumentAdapter;
import ch.psi.fda.ui.ce.panels.EditableComponent;
import ch.psi.fda.ui.ce.panels.ObjectProvider;
import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.HashMap;
import javax.swing.JFormattedTextField;
import javax.swing.event.DocumentEvent;

/**
 *
 * @author ebner
 */
public class ChannelActionPanel extends javax.swing.JPanel implements ObjectProvider<ChannelAction>, EditableComponent {

    private boolean modified = false;

    private ChannelAction action;
    private WrapLayout manager;

    /**
     * Panel support
     */
    private PanelSupport panelSupport;

    public ChannelActionPanel() {
        this(new ChannelAction());
    }

    /** Creates new form ChannelActionPanel */
    public ChannelActionPanel(final ChannelAction action) {
        this.action = action;

        initComponents();
        
        ProcessorFDA.setIcon(jButton1, getClass().getResource("/ch/psi/fda/ui/ce/icons/plus.png"));        

        HashMap<Component, ComponentMetadata> managedFields = new HashMap<Component,ComponentMetadata>();
        managedFields.put(jFormattedTextFieldChannel, new ComponentMetadata(true));
        managedFields.put(jTextFieldValue, new ComponentMetadata(true));
        managedFields.put(jComboBoxOperation, new ComponentMetadata(false, new ChannelAction().getOperation()));
        managedFields.put(jComboBoxType, new ComponentMetadata(false, new ChannelAction().getType()));
        managedFields.put(jFormattedTextFieldTimeout, new ComponentMetadata(false));
        managedFields.put(jFormattedTextFieldDelay, new ComponentMetadata(false));

        this.panelSupport = new PanelSupport();
        this.panelSupport.analyze(managedFields);


        // Update view
        jFormattedTextFieldChannel.setText(action.getChannel());
        jTextFieldValue.setText(action.getValue());
        if(action.getOperation()!=null){
            jComboBoxOperation.setSelectedItem(action.getOperation());
        }
        if(action.getType()!=null){
            jComboBoxType.setSelectedItem(action.getType());
        }
        if(action.getTimeout()!=null){
            jFormattedTextFieldTimeout.setText(action.getTimeout().toString());
        }
        if(action.getDelay() !=null){
            jFormattedTextFieldDelay.setText(action.getDelay().toString());
        }


        // Establish bindings
        jFormattedTextFieldChannel.getDocument().addDocumentListener(new DocumentAdapter() {
            @Override
            public void valueChange(DocumentEvent de) {
                modified = true;
                action.setChannel(panelSupport.getRealFieldValue(jFormattedTextFieldChannel));
            }
        });
        jTextFieldValue.getDocument().addDocumentListener(new DocumentAdapter() {
            @Override
            public void valueChange(DocumentEvent de) {
                modified = true;
                action.setValue(jTextFieldValue.getText());
            }
        });
        jComboBoxOperation.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent ie) {
                modified = true;
                action.setOperation((String) jComboBoxOperation.getSelectedItem());
            }
        });
        jComboBoxType.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent ie) {
                modified = true;
                action.setType((String) jComboBoxType.getSelectedItem());
            }
        });
        jFormattedTextFieldTimeout.getDocument().addDocumentListener(new DocumentAdapter() {
            @Override
            public void valueChange(DocumentEvent de) {
                modified = true;
                String value = panelSupport.getValue(jFormattedTextFieldTimeout);
                if(value != null){
                    try{
                        action.setTimeout(new Double(value));
                    }
                    catch(NumberFormatException e){
                        action.setTimeout(null);
//                        jFormattedTextFieldTimeout.setText("");
                    }
                }
                else{
                    action.setTimeout(null);
                }
            }
        });
        jFormattedTextFieldDelay.getDocument().addDocumentListener(new DocumentAdapter() {
            @Override
            public void valueChange(DocumentEvent de) {
                modified = true;
                String value = panelSupport.getValue(jFormattedTextFieldDelay);
                if(value != null){
                    try{
                        action.setDelay(new Double(jFormattedTextFieldDelay.getText()));
                    }
                    catch(NumberFormatException e){
                        action.setDelay(null);
//                        jFormattedTextFieldDelay.setText("");
                    }
                }
                else{
                    action.setDelay(null);
                }
            }
        });



        this.panelSupport.manage(this, managedFields, jButton1);


        manager = new WrapLayout(WrapLayout.LEADING);
        this.jPanel1.setLayout(manager);
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
        jFormattedTextFieldChannel = new javax.swing.JFormattedTextField();
        jLabel2 = new javax.swing.JLabel();
        jTextFieldValue = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jComboBoxOperation = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();
        jComboBoxType = new javax.swing.JComboBox();
        jLabel5 = new javax.swing.JLabel();
        jFormattedTextFieldTimeout = new JFormattedTextField(FieldUtilities.getDecimalFormat());
        jLabel6 = new javax.swing.JLabel();
        jFormattedTextFieldDelay = new JFormattedTextField(FieldUtilities.getDecimalFormat());
        jButton1 = new javax.swing.JButton();

        setMinimumSize(new java.awt.Dimension(0, 32));
        setName("CAPan"); // NOI18N
        setPreferredSize(new java.awt.Dimension(460, 28));

        jPanel1.setPreferredSize(new java.awt.Dimension(400, 28));
        jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jFormattedTextFieldChannel.setText("Channel Name");
        jFormattedTextFieldChannel.setToolTipText("Channel name");
        jFormattedTextFieldChannel.setPreferredSize(new java.awt.Dimension(180, 28));
        jPanel1.add(jFormattedTextFieldChannel);

        jLabel2.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        jLabel2.setLabelFor(jTextFieldValue);
        jLabel2.setText("Value:");
        jPanel1.add(jLabel2);

        jTextFieldValue.setToolTipText("Channel value");
        jTextFieldValue.setPreferredSize(new java.awt.Dimension(60, 28));
        jPanel1.add(jTextFieldValue);

        jLabel3.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        jLabel3.setLabelFor(jComboBoxOperation);
        jLabel3.setText("Operation:");
        jPanel1.add(jLabel3);

        jComboBoxOperation.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "put", "putq", "wait", "waitAND", "waitOR", "waitREGEX" }));
        jComboBoxOperation.setToolTipText("Operation to perform");
        jComboBoxOperation.setName("Operation"); // NOI18N
        jPanel1.add(jComboBoxOperation);

        jLabel4.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        jLabel4.setLabelFor(jComboBoxType);
        jLabel4.setText("Type:");
        jPanel1.add(jLabel4);

        jComboBoxType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Integer", "String" }));
        jComboBoxType.setToolTipText("Type of channel");
        jComboBoxType.setName("Type"); // NOI18N
        jPanel1.add(jComboBoxType);

        jLabel5.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        jLabel5.setLabelFor(jFormattedTextFieldTimeout);
        jLabel5.setText("Timeout [s]:");
        jPanel1.add(jLabel5);

        jFormattedTextFieldTimeout.setToolTipText("Timeout in seconds (Optional)");
        jFormattedTextFieldTimeout.setName("Timeout"); // NOI18N
        jFormattedTextFieldTimeout.setPreferredSize(new java.awt.Dimension(60, 28));
        jPanel1.add(jFormattedTextFieldTimeout);

        jLabel6.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        jLabel6.setLabelFor(jFormattedTextFieldDelay);
        jLabel6.setText("Delay [s]:");
        jPanel1.add(jLabel6);

        jFormattedTextFieldDelay.setToolTipText("Delay in seconds");
        jFormattedTextFieldDelay.setName("Delay"); // NOI18N
        jFormattedTextFieldDelay.setPreferredSize(new java.awt.Dimension(60, 28));
        jPanel1.add(jFormattedTextFieldDelay);

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ch/psi/fda/ui/ce/icons/plus.png"))); // NOI18N
        jButton1.setBorderPainted(false);
        jButton1.setContentAreaFilled(false);
        jPanel1.add(jButton1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 665, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JComboBox jComboBoxOperation;
    private javax.swing.JComboBox jComboBoxType;
    private javax.swing.JFormattedTextField jFormattedTextFieldChannel;
    private javax.swing.JFormattedTextField jFormattedTextFieldDelay;
    private javax.swing.JFormattedTextField jFormattedTextFieldTimeout;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField jTextFieldValue;
    // End of variables declaration//GEN-END:variables

    @Override
    public ChannelAction getObject() {
        return action;
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
