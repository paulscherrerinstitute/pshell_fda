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
 * ConfiguratioPanel.java
 *
 * Created on Jan 25, 2011, 12:35:59 PM
 */

package ch.psi.fda.ui.ce.panels.model;

import ch.psi.fda.ProcessorFDA;
import ch.psi.fda.ui.ce.panels.CollapsibleListContainer;
import ch.psi.fda.ui.ce.panels.ComponentMetadata;
import ch.psi.fda.ui.ce.panels.FieldUtilities;
import ch.psi.fda.ui.ce.panels.ObjectProvider;
import ch.psi.fda.ui.ce.panels.PanelSupport;
import ch.psi.fda.model.v1.Configuration;
import ch.psi.fda.model.v1.Data;
import ch.psi.fda.model.v1.Variable;
import ch.psi.fda.model.v1.Visualization;
import ch.psi.fda.ui.ce.panels.DocumentAdapter;
import ch.psi.fda.ui.ce.panels.EditableComponent;
import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFormattedTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;

/**
 *
 * @author ebner
 */
public class ConfigurationPanel extends javax.swing.JPanel implements ObjectProvider<Configuration>, EditableComponent {

    private boolean modified = false;
    private PanelSupport panelSupport;
    private PanelSupport panelSupport2;

    private Configuration configuration;

    public ConfigurationPanel() {
        this(new Configuration());
    }

    /** Creates new form ConfiguratioPanel */
    public ConfigurationPanel(final Configuration configuration) {
        if(configuration.getScan()==null){
            configuration.setScan(configuration.getScan());
        }
        // Correct number of execution if number of execution <=0
        if(configuration.getNumberOfExecution()<=0){
            configuration.setNumberOfExecution(1);
        }
        if(configuration.getData()==null){
            configuration.setData(new Data());
        }
        this.configuration = configuration;
        

        initComponents();
        
        ProcessorFDA.setIcon(jButton1, getClass().getResource("/ch/psi/fda/ui/ce/icons/plus.png"));        
        ProcessorFDA.setIcon(jButton2, getClass().getResource("/ch/psi/fda/ui/ce/icons/plus.png"));        


        HashMap<Component, ComponentMetadata> managedFields = new HashMap<Component,ComponentMetadata>();
        managedFields.put(collapsibleListContainerVisualization, new ComponentMetadata(false));

        this.panelSupport = new PanelSupport();
        this.panelSupport.analyze(managedFields);


        HashMap<Component, ComponentMetadata> managedFields2 = new HashMap<Component,ComponentMetadata>();
        managedFields2.put(collapsibleListContainerNotification, new ComponentMetadata(false));
        managedFields2.put(collapsibleListContainerVariables, new ComponentMetadata(false));
        managedFields2.put(collapsibleListContainerDescription, new ComponentMetadata(false));

        this.panelSupport2 = new PanelSupport();
        this.panelSupport2.analyze(managedFields2);


        // Update view
        jFormattedTextFieldNumber.setText(configuration.getNumberOfExecution()+"");
        jCheckBoxFailOnSensorError.setSelected(configuration.isFailOnSensorError());
        jTextFieldFileName.setText(configuration.getData().getFileName());
        jComboBoxFormat.setSelectedItem(configuration.getData().getFormat());


        // Establish bindings
        jFormattedTextFieldNumber.getDocument().addDocumentListener(new DocumentAdapter() {
            @Override
            public void valueChange(DocumentEvent de) {
                String str = jFormattedTextFieldNumber.getText().trim();
                int value;
                try{
                    value = new Integer(str);
                    configuration.setNumberOfExecution(new Integer(value));
                }
                catch(NumberFormatException e){
                    value = 1;
                }
                    
                if (value == configuration.getNumberOfExecution()){
                    return;
                }
                modified = true;                
                configuration.setNumberOfExecution(new Integer(value));
            }
        });
        jCheckBoxFailOnSensorError.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent ce) {
                modified = true;
                configuration.setFailOnSensorError(jCheckBoxFailOnSensorError.isSelected());
            }
        });
        jTextFieldFileName.getDocument().addDocumentListener(new DocumentAdapter() {
            @Override
            public void valueChange(DocumentEvent de) {
                modified = true;
                configuration.getData().setFileName(jTextFieldFileName.getText());
            }
        });
        jComboBoxFormat.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent ie) {
                modified = true;
                configuration.getData().setFormat((String)jComboBoxFormat.getSelectedItem());
            }
        });


        collapsibleListContainerVisualization.setHeader("Visualizations");
        collapsibleListContainerVisualization.setName("Visualizations");
        collapsibleListContainerVisualization.setIcon("chart_line");

        collapsibleListContainerNotification.setHeader("Notifications");
        collapsibleListContainerNotification.setName("Notifications");

        collapsibleListContainerDescription.setHeader("Description");
        collapsibleListContainerDescription.setName("Description");

        collapsibleListContainerVariables.setHeader("Variables");
        collapsibleListContainerVariables.setName("Variables");

        this.panelSupport.manage(this, managedFields, jButton1);
        this.panelSupport2.manage(this, managedFields2, jButton2);

        collapsibleListContainerVisualization.setCollapsed(DefaultSettings.getInstance().isCollapsed());
        collapsibleListContainerNotification.setCollapsed(DefaultSettings.getInstance().isCollapsed());
//        collapsibleListContainerVariables.setCollapsed(DefaultSettings.getInstance().isCollapsed());
        collapsibleListContainerDescription.setCollapsed(DefaultSettings.getInstance().isCollapsed());
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jFormattedTextFieldNumber = new JFormattedTextField(FieldUtilities.getIntegerFormat());
        jLabel3 = new javax.swing.JLabel();
        jTextFieldFileName = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jComboBoxFormat = new javax.swing.JComboBox();
        jCheckBoxFailOnSensorError = new javax.swing.JCheckBox();
        scanPanel1 = new ScanPanel(configuration.getScan());
        collapsibleListContainerVisualization = new CollapsibleListContainer<Visualization>(new VisualizationListItemProvider(configuration.getVisualization()));
        jPanel1 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        collapsibleListContainerNotification = new CollapsibleListContainer<String>(new RecipientListItemProvider(configuration));
        jPanelOptionalNotificaton = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();
        collapsibleListContainerVariables = new CollapsibleListContainer<Variable>(new GlobalVariableListItemProvider(configuration.getVariable()));
        collapsibleListContainerDescription = new CollapsibleListContainer<String>(new DescriptionListItemProvider(configuration));

        jPanel3.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jLabel1.setFont(new java.awt.Font("Lucida Grande", 1, 13));
        jLabel1.setLabelFor(jFormattedTextFieldNumber);
        jLabel1.setText("Number of Execution:");
        jPanel3.add(jLabel1);

        jFormattedTextFieldNumber.setToolTipText("Number of executions");
        jFormattedTextFieldNumber.setName("Number of Execution"); // NOI18N
        jFormattedTextFieldNumber.setPreferredSize(new java.awt.Dimension(40, 28));
        jPanel3.add(jFormattedTextFieldNumber);

        jLabel3.setFont(new java.awt.Font("Lucida Grande", 1, 13));
        jLabel3.setLabelFor(jTextFieldFileName);
        jLabel3.setText("Type:");
        jPanel3.add(jLabel3);

        jTextFieldFileName.setPreferredSize(new java.awt.Dimension(120, 28));
        jPanel3.add(jTextFieldFileName);

        jLabel2.setFont(new java.awt.Font("Lucida Grande", 1, 13));
        jLabel2.setText("Value:");
        jPanel3.add(jLabel2);

        jComboBoxFormat.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "txt" }));
        jPanel3.add(jComboBoxFormat);

        jCheckBoxFailOnSensorError.setFont(new java.awt.Font("Lucida Grande", 1, 13));
        jCheckBoxFailOnSensorError.setText("Fail on Sensor Error");
        jPanel3.add(jCheckBoxFailOnSensorError);

        scanPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 153, 153)));

        collapsibleListContainerVisualization.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 0, 153)));

        jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ch/psi/fda/ui/ce/icons/plus.png"))); // NOI18N
        jButton1.setBorderPainted(false);
        jButton1.setContentAreaFilled(false);
        jPanel1.add(jButton1);

        jPanelOptionalNotificaton.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ch/psi/fda/ui/ce/icons/plus.png"))); // NOI18N
        jButton2.setBorderPainted(false);
        jButton2.setContentAreaFilled(false);
        jPanelOptionalNotificaton.add(jButton2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, 749, Short.MAX_VALUE)
            .addComponent(collapsibleListContainerDescription, javax.swing.GroupLayout.DEFAULT_SIZE, 749, Short.MAX_VALUE)
            .addComponent(collapsibleListContainerNotification, javax.swing.GroupLayout.DEFAULT_SIZE, 749, Short.MAX_VALUE)
            .addComponent(jPanelOptionalNotificaton, javax.swing.GroupLayout.DEFAULT_SIZE, 749, Short.MAX_VALUE)
            .addComponent(collapsibleListContainerVariables, javax.swing.GroupLayout.DEFAULT_SIZE, 749, Short.MAX_VALUE)
            .addComponent(scanPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 749, Short.MAX_VALUE)
            .addComponent(collapsibleListContainerVisualization, javax.swing.GroupLayout.DEFAULT_SIZE, 749, Short.MAX_VALUE)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 749, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(collapsibleListContainerDescription, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(collapsibleListContainerNotification, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanelOptionalNotificaton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(collapsibleListContainerVariables, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scanPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(collapsibleListContainerVisualization, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private ch.psi.fda.ui.ce.panels.CollapsibleListContainer collapsibleListContainerDescription;
    private ch.psi.fda.ui.ce.panels.CollapsibleListContainer collapsibleListContainerNotification;
    private ch.psi.fda.ui.ce.panels.CollapsibleListContainer collapsibleListContainerVariables;
    private ch.psi.fda.ui.ce.panels.CollapsibleListContainer collapsibleListContainerVisualization;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JCheckBox jCheckBoxFailOnSensorError;
    private javax.swing.JComboBox jComboBoxFormat;
    private javax.swing.JFormattedTextField jFormattedTextFieldNumber;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanelOptionalNotificaton;
    private javax.swing.JTextField jTextFieldFileName;
    private ch.psi.fda.ui.ce.panels.model.ScanPanel scanPanel1;
    // End of variables declaration//GEN-END:variables

    @Override
    public Configuration getObject() {
        return(configuration);
    }

    /**
     * WORKAROUND
     * Set the name of the file ()
     * @param string
     */
    public void setFileName(String string){
        jTextFieldFileName.setText(string);
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
