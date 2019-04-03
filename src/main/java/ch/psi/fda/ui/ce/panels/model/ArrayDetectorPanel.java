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
 * ArrayDetectorPanel.java
 *
 * Created on Jan 24, 2011, 10:57:29 AM
 */

package ch.psi.fda.ui.ce.panels.model;

import ch.psi.fda.ProcessorFDA;
import ch.psi.fda.ui.ce.panels.CollapsibleListContainer;
import ch.psi.fda.ui.ce.panels.ComponentMetadata;
import ch.psi.fda.ui.ce.panels.FieldUtilities;
import ch.psi.fda.ui.ce.panels.PanelSupport;
import ch.psi.fda.model.v1.Action;
import ch.psi.fda.model.v1.ArrayDetector;
import ch.psi.fda.ui.ce.panels.DocumentAdapter;
import ch.psi.fda.ui.ce.panels.EditableComponent;
import ch.psi.fda.ui.ce.panels.IdInputVerifier;
import ch.psi.fda.ui.ce.panels.ObjectProvider;
import ch.psi.fda.ui.ce.panels.model.util.ModelUtil;
import java.awt.Component;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import javax.swing.JFormattedTextField;
import javax.swing.event.DocumentEvent;

/**
 *
 * @author ebner
 */
public class ArrayDetectorPanel extends javax.swing.JPanel implements ObjectProvider<ArrayDetector>, EditableComponent {

    private boolean modified;

    private ArrayDetector detector;
    private PanelSupport panelSupport;

    public ArrayDetectorPanel(){
        this(new ArrayDetector());
    }

    /** Creates new form ArrayDetectorPanel */
    public ArrayDetectorPanel(final ArrayDetector detector) {
        if (detector.getId() == null || detector.getId().equals("")) {
            detector.setId(IdGenerator.generateId());
        }
        this.detector = detector;

        initComponents();

        ProcessorFDA.setIcon(jButton1, getClass().getResource("/ch/psi/fda/ui/ce/icons/plus.png"));        
        
        HashMap<Component, ComponentMetadata> managedFields = new HashMap<Component,ComponentMetadata>();
        managedFields.put(jTextFieldName, new ComponentMetadata(true));
        managedFields.put(jFormattedTextFieldArraySize, new ComponentMetadata(true));
        managedFields.put(collapsibleListContainer1, new ComponentMetadata(false));

        this.panelSupport = new PanelSupport();
        this.panelSupport.analyze(managedFields);

        // Update view
        jTextFieldId.setText(detector.getId());
        jTextFieldName.setText(detector.getName());
        jFormattedTextFieldArraySize.setText(detector.getArraySize()+"");


        // Establish bindings
        jTextFieldId.getDocument().addDocumentListener(new DocumentAdapter() {
            @Override
            public void valueChange(DocumentEvent de) {
                modified=true;
                detector.setId(jTextFieldId.getText());
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
                modified=true;
                detector.setName(jTextFieldName.getText());
            }
        });
        jFormattedTextFieldArraySize.getDocument().addDocumentListener(new DocumentAdapter() {
            @Override
            public void valueChange(DocumentEvent de) {
                modified=true;
                String value = panelSupport.getValue(jFormattedTextFieldArraySize);
                if(value != null){
                    try{
                        detector.setArraySize(new Integer(value));
                    }
                    catch(NumberFormatException e){
                        detector.setArraySize(0);
//                        jFormattedTextFieldArraySize.setText("0");
                    }
                }
                else{
                    detector.setArraySize(0);
//                    jFormattedTextFieldArraySize.setText("0");
                }
            }
        });


        collapsibleListContainer1.setHeader("Pre Actions");
        collapsibleListContainer1.setName("Pre Actions");

        this.panelSupport.manage(this, managedFields, jButton1);

        collapsibleListContainer1.setCollapsed(DefaultSettings.getInstance().isCollapsed());
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        collapsibleListContainer1 = new CollapsibleListContainer<Action>(new ActionListItemProvider(detector.getPreAction()));
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jTextFieldId = new javax.swing.JTextField();
        jTextFieldName = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jFormattedTextFieldArraySize = new JFormattedTextField(FieldUtilities.getIntegerFormat());
        jButton1 = new javax.swing.JButton();

        setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 204, 204)));

        collapsibleListContainer1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 0, 204)));

        jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jLabel1.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
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
        jTextFieldName.setPreferredSize(new java.awt.Dimension(180, 28));
        jPanel1.add(jTextFieldName);

        jLabel2.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        jLabel2.setLabelFor(jFormattedTextFieldArraySize);
        jLabel2.setText("Array Size:");
        jPanel1.add(jLabel2);

        jFormattedTextFieldArraySize.setToolTipText("Array Size");
        jFormattedTextFieldArraySize.setPreferredSize(new java.awt.Dimension(40, 28));
        jPanel1.add(jFormattedTextFieldArraySize);

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ch/psi/fda/ui/ce/icons/plus.png"))); // NOI18N
        jButton1.setBorderPainted(false);
        jButton1.setContentAreaFilled(false);
        jPanel1.add(jButton1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(collapsibleListContainer1, javax.swing.GroupLayout.DEFAULT_SIZE, 514, Short.MAX_VALUE)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 514, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(collapsibleListContainer1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private ch.psi.fda.ui.ce.panels.CollapsibleListContainer collapsibleListContainer1;
    private javax.swing.JButton jButton1;
    private javax.swing.JFormattedTextField jFormattedTextFieldArraySize;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField jTextFieldId;
    private javax.swing.JTextField jTextFieldName;
    // End of variables declaration//GEN-END:variables

    @Override
    public ArrayDetector getObject() {
        return detector;
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
