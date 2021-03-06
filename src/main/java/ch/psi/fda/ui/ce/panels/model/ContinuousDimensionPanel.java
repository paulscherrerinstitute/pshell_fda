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
 * ContinuousDimensionPanel.java
 *
 * Created on Jan 25, 2011, 10:35:17 AM
 */

package ch.psi.fda.ui.ce.panels.model;

import ch.psi.fda.ProcessorFDA;
import ch.psi.fda.ui.ce.panels.CollapsibleListContainer;
import ch.psi.fda.ui.ce.panels.ComponentMetadata;
import ch.psi.fda.ui.ce.panels.PanelSupport;
import ch.psi.fda.model.v1.Action;
import ch.psi.fda.model.v1.ContinuousDimension;
import ch.psi.fda.model.v1.ContinuousPositioner;
import ch.psi.fda.model.v1.Detector;
import ch.psi.fda.ui.ce.panels.EditableComponent;
import ch.psi.fda.ui.ce.panels.ObjectProvider;
import java.awt.Component;
import java.util.HashMap;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author ebner
 */
public class ContinuousDimensionPanel extends javax.swing.JPanel implements ObjectProvider<ContinuousDimension>, EditableComponent {

    private boolean modified = false;
    private ContinuousDimension dimension;
    private PanelSupport panelSupport;

    public ContinuousDimensionPanel() {
        this(new ContinuousDimension());
    }

    /** Creates new form ContinuousDimensionPanel */
    public ContinuousDimensionPanel(final ContinuousDimension dimension) {

        if(dimension.getPositioner()==null){
            dimension.setPositioner(new ContinuousPositioner());
        }
        this.dimension = dimension;


        initComponents();
        
        ProcessorFDA.setIcon(jButton1, getClass().getResource("/ch/psi/fda/ui/ce/icons/plus.png"));        

        HashMap<Component, ComponentMetadata> managedFields = new HashMap<Component,ComponentMetadata>();
        managedFields.put(collapsibleListContainerPreActions, new ComponentMetadata(false));
        managedFields.put(collapsibleListContainer1, new ComponentMetadata(false));
        managedFields.put(collapsibleListContainerPostActions, new ComponentMetadata(false));
        managedFields.put(jCheckBoxDataGroup, new ComponentMetadata(false,"false"));
        managedFields.put(jCheckBoxZigZag, new ComponentMetadata(false,"false"));

        this.panelSupport = new PanelSupport();
        this.panelSupport.analyze(managedFields);

        collapsibleListContainerPreActions.setHeader("Pre Actions");
        collapsibleListContainerPreActions.setName("Pre Actions");

        collapsibleListContainerPostActions.setHeader("Post Actions");
        collapsibleListContainerPostActions.setName("Post Actions");

        collapsibleListContainer1.setHeader("Detectors");
        collapsibleListContainer1.setName("Detectors");


        // Update view
        jCheckBoxDataGroup.setSelected(dimension.isDataGroup());
        jCheckBoxZigZag.setSelected(dimension.isZigzag());

        // Establish bindings
        jCheckBoxDataGroup.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent ce) {
                modified = true;
                dimension.setDataGroup(jCheckBoxDataGroup.isSelected());
            }
        });

        jCheckBoxZigZag.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent ce) {
                modified = true;
                dimension.setZigzag(jCheckBoxZigZag.isSelected());
            }
        });
        

        this.panelSupport.manage(this, managedFields, jButton1);

        collapsibleListContainerPreActions.setCollapsed(DefaultSettings.getInstance().isCollapsed());
        collapsibleListContainerPostActions.setCollapsed(DefaultSettings.getInstance().isCollapsed());
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

        continuousPositionerPanel1 = new ContinuousPositionerPanel(dimension.getPositioner());
        collapsibleListContainer1 = new CollapsibleListContainer<Detector>(new ContinuousDetectorListItemProvider(dimension));
        jPanel1 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        collapsibleListContainerPreActions = new CollapsibleListContainer<Action>(new ActionListItemProvider(dimension.getPreAction()));
        collapsibleListContainerPostActions = new CollapsibleListContainer<Action>(new ActionListItemProvider(dimension.getPostAction()));
        jPanel2 = new javax.swing.JPanel();
        jCheckBoxDataGroup = new javax.swing.JCheckBox();
        jCheckBoxZigZag = new javax.swing.JCheckBox();

        setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 153, 0)));
        setPreferredSize(new java.awt.Dimension(500, 100));

        jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ch/psi/fda/ui/ce/icons/plus.png"))); // NOI18N
        jButton1.setToolTipText("Add optional items");
        jButton1.setBorderPainted(false);
        jButton1.setContentAreaFilled(false);
        jPanel1.add(jButton1);

        jPanel2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jCheckBoxDataGroup.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        jCheckBoxDataGroup.setText("Data Group");
        jCheckBoxDataGroup.setName("Data Group"); // NOI18N
        jPanel2.add(jCheckBoxDataGroup);

        jCheckBoxZigZag.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        jCheckBoxZigZag.setText("ZigZag");
        jCheckBoxZigZag.setToolTipText("ZigZag");
        jCheckBoxZigZag.setName("ZigZag"); // NOI18N
        jPanel2.add(jCheckBoxZigZag);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 566, Short.MAX_VALUE)
            .addComponent(collapsibleListContainerPreActions, javax.swing.GroupLayout.DEFAULT_SIZE, 566, Short.MAX_VALUE)
            .addComponent(continuousPositionerPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 566, Short.MAX_VALUE)
            .addComponent(collapsibleListContainer1, javax.swing.GroupLayout.DEFAULT_SIZE, 566, Short.MAX_VALUE)
            .addComponent(collapsibleListContainerPostActions, javax.swing.GroupLayout.DEFAULT_SIZE, 566, Short.MAX_VALUE)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 566, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(collapsibleListContainerPreActions, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(continuousPositionerPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(collapsibleListContainer1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(collapsibleListContainerPostActions, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private ch.psi.fda.ui.ce.panels.CollapsibleListContainer collapsibleListContainer1;
    private ch.psi.fda.ui.ce.panels.CollapsibleListContainer collapsibleListContainerPostActions;
    private ch.psi.fda.ui.ce.panels.CollapsibleListContainer collapsibleListContainerPreActions;
    private ch.psi.fda.ui.ce.panels.model.ContinuousPositionerPanel continuousPositionerPanel1;
    private javax.swing.JButton jButton1;
    private javax.swing.JCheckBox jCheckBoxDataGroup;
    private javax.swing.JCheckBox jCheckBoxZigZag;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    // End of variables declaration//GEN-END:variables

    @Override
    public ContinuousDimension getObject() {
        return dimension;
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
