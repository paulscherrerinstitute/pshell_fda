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
 * DiscreteStepDimensionPanel.java
 *
 * Created on Jan 25, 2011, 9:54:36 AM
 */

package ch.psi.fda.ui.ce.panels.model;

import ch.psi.fda.ProcessorFDA;
import ch.psi.fda.ui.ce.panels.CollapsibleListContainer;
import ch.psi.fda.ui.ce.panels.ComponentMetadata;
import ch.psi.fda.ui.ce.panels.PanelSupport;
import ch.psi.fda.model.v1.Action;
import ch.psi.fda.model.v1.Detector;
import ch.psi.fda.model.v1.DiscreteStepDimension;
import ch.psi.fda.model.v1.DiscreteStepPositioner;
import ch.psi.fda.model.v1.GuardCondition;
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
public class DiscreteStepDimensionPanel extends javax.swing.JPanel implements ObjectProvider<DiscreteStepDimension>, EditableComponent {

    private boolean modified = false;
    private DiscreteStepDimension dimension;
    private PanelSupport panelSupport;

    public DiscreteStepDimensionPanel() {
        this(new DiscreteStepDimension());
    }

    /** Creates new form DiscreteStepDimensionPanel */
    public DiscreteStepDimensionPanel(final DiscreteStepDimension dimension) {
        this.dimension = dimension;

        initComponents();
        
        ProcessorFDA.setIcon(jButton1, getClass().getResource("/ch/psi/fda/ui/ce/icons/plus.png"));        

        HashMap<Component, ComponentMetadata> managedFields = new HashMap<Component,ComponentMetadata>();
        managedFields.put(collapsibleListContainerPreAction, new ComponentMetadata(false));
        managedFields.put(collapsibleListContainerPositioner, new ComponentMetadata(true));
        managedFields.put(collapsibleListContainerAction, new ComponentMetadata(false));
        managedFields.put(collapsibleListContainerGuard, new ComponentMetadata(false));
        managedFields.put(collapsibleListContainerDetector, new ComponentMetadata(false));
        managedFields.put(collapsibleListContainerPostAction, new ComponentMetadata(false));
        managedFields.put(jCheckBoxDataGroup, new ComponentMetadata(false, "false"));
        managedFields.put(jCheckBoxZigZag, new ComponentMetadata(false, "false"));


        this.panelSupport = new PanelSupport();
        this.panelSupport.analyze(managedFields);

        collapsibleListContainerPositioner.setHeader("Positioners");
        collapsibleListContainerPositioner.setName("Positioners");
        collapsibleListContainerPositioner.setIcon("cog");

        collapsibleListContainerGuard.setHeader("Guards");
        collapsibleListContainerGuard.setName("Guards");

        collapsibleListContainerDetector.setHeader("Detectors");
        collapsibleListContainerDetector.setName("Detectors");
        collapsibleListContainerDetector.setIcon("photo");


        collapsibleListContainerPreAction.setHeader("Pre Actions");
        collapsibleListContainerPreAction.setName("Pre Actions");


        collapsibleListContainerPostAction.setHeader("Post Actions");
        collapsibleListContainerPostAction.setName("Post Actions");

        collapsibleListContainerAction.setHeader("Actions");
        collapsibleListContainerAction.setName("Actions");


        // Update view
        jCheckBoxZigZag.setSelected(dimension.isZigzag());
        jCheckBoxDataGroup.setSelected(dimension.isDataGroup());

        // Establish bindings
        jCheckBoxZigZag.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent ce) {
                modified = true;
                dimension.setZigzag(jCheckBoxZigZag.isSelected());
            }
        });
        jCheckBoxDataGroup.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent ce) {
                modified = true;
                dimension.setDataGroup(jCheckBoxDataGroup.isSelected());
            }
        });
        

        this.panelSupport.manage(this, managedFields, jButton1);

        collapsibleListContainerGuard.setCollapsed(DefaultSettings.getInstance().isCollapsed());
        collapsibleListContainerDetector.setCollapsed(DefaultSettings.getInstance().isCollapsed());
        collapsibleListContainerPreAction.setCollapsed(DefaultSettings.getInstance().isCollapsed());
        collapsibleListContainerPostAction.setCollapsed(DefaultSettings.getInstance().isCollapsed());
        collapsibleListContainerAction.setCollapsed(DefaultSettings.getInstance().isCollapsed());
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        collapsibleListContainerPositioner = new CollapsibleListContainer<DiscreteStepPositioner>(new DiscreteStepPositionerListItemProvider(dimension.getPositioner()));
        collapsibleListContainerDetector = new CollapsibleListContainer<Detector>(new DiscreteStepDetectorListItemProvider(dimension.getDetector()));
        collapsibleListContainerGuard = new CollapsibleListContainer<GuardCondition>(new GuardListItemProvider(dimension));
        jPanel1 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jCheckBoxDataGroup = new javax.swing.JCheckBox();
        jCheckBoxZigZag = new javax.swing.JCheckBox();
        collapsibleListContainerPreAction = new CollapsibleListContainer<Action>(new ActionListItemProvider(dimension.getPreAction()));
        collapsibleListContainerPostAction = new CollapsibleListContainer<Action>(new ActionListItemProvider(dimension.getPostAction()));
        collapsibleListContainerAction = new CollapsibleListContainer<Action>(new ActionListItemProvider(dimension.getAction()));

        setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 153, 0)));

        collapsibleListContainerPositioner.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 255)));

        collapsibleListContainerDetector.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 255), 1, true));

        jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ch/psi/fda/ui/ce/icons/plus.png"))); // NOI18N
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
        jCheckBoxZigZag.setToolTipText("ZigZag Dimension");
        jCheckBoxZigZag.setName("ZigZag"); // NOI18N
        jPanel2.add(jCheckBoxZigZag);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 551, Short.MAX_VALUE)
            .addComponent(collapsibleListContainerPreAction, javax.swing.GroupLayout.DEFAULT_SIZE, 551, Short.MAX_VALUE)
            .addComponent(collapsibleListContainerPositioner, javax.swing.GroupLayout.DEFAULT_SIZE, 551, Short.MAX_VALUE)
            .addComponent(collapsibleListContainerAction, javax.swing.GroupLayout.DEFAULT_SIZE, 551, Short.MAX_VALUE)
            .addComponent(collapsibleListContainerGuard, javax.swing.GroupLayout.DEFAULT_SIZE, 551, Short.MAX_VALUE)
            .addComponent(collapsibleListContainerDetector, javax.swing.GroupLayout.DEFAULT_SIZE, 551, Short.MAX_VALUE)
            .addComponent(collapsibleListContainerPostAction, javax.swing.GroupLayout.DEFAULT_SIZE, 551, Short.MAX_VALUE)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 551, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(collapsibleListContainerPreAction, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(collapsibleListContainerPositioner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(collapsibleListContainerAction, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(collapsibleListContainerGuard, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(collapsibleListContainerDetector, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(collapsibleListContainerPostAction, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private ch.psi.fda.ui.ce.panels.CollapsibleListContainer collapsibleListContainerAction;
    private ch.psi.fda.ui.ce.panels.CollapsibleListContainer collapsibleListContainerDetector;
    private ch.psi.fda.ui.ce.panels.CollapsibleListContainer collapsibleListContainerGuard;
    private ch.psi.fda.ui.ce.panels.CollapsibleListContainer collapsibleListContainerPositioner;
    private ch.psi.fda.ui.ce.panels.CollapsibleListContainer collapsibleListContainerPostAction;
    private ch.psi.fda.ui.ce.panels.CollapsibleListContainer collapsibleListContainerPreAction;
    private javax.swing.JButton jButton1;
    private javax.swing.JCheckBox jCheckBoxDataGroup;
    private javax.swing.JCheckBox jCheckBoxZigZag;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    // End of variables declaration//GEN-END:variables

    @Override
    public DiscreteStepDimension getObject() {
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
