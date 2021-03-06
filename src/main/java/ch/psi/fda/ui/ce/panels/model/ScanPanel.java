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
 * ScanPanel.java
 *
 * Created on Jan 25, 2011, 11:09:15 AM
 */

package ch.psi.fda.ui.ce.panels.model;

import ch.psi.fda.ProcessorFDA;
import ch.psi.fda.ui.ce.panels.CollapsibleListContainer;
import ch.psi.fda.ui.ce.panels.ComponentMetadata;
import ch.psi.fda.ui.ce.panels.ObjectProvider;
import ch.psi.fda.ui.ce.panels.PanelSupport;
import ch.psi.fda.model.v1.Action;
import ch.psi.fda.model.v1.ContinuousDimension;
import ch.psi.fda.model.v1.DiscreteStepDimension;
import ch.psi.fda.model.v1.Manipulation;
import ch.psi.fda.model.v1.Scan;
import java.awt.Component;
import java.util.HashMap;

/**
 *
 * @author ebner
 */
public class ScanPanel extends javax.swing.JPanel implements ObjectProvider<Scan> {

    private Scan scan;
    private PanelSupport panelSupport;

    public ScanPanel() {
        this(new Scan());
    }

    /** Creates new form ScanPanel */
    public ScanPanel(Scan scan) {
        this.scan = scan;


        initComponents();
        
        ProcessorFDA.setIcon(jButton1, getClass().getResource("/ch/psi/fda/ui/ce/icons/plus.png"));        

        HashMap<Component, ComponentMetadata> managedFields = new HashMap<Component,ComponentMetadata>();
        managedFields.put(collapsibleListContainerPre, new ComponentMetadata(false));
        managedFields.put(collapsibleListContainerContinuousDimension, new ComponentMetadata(false));
        managedFields.put(collapsibleListContainerDimension, new ComponentMetadata(false));
        managedFields.put(collapsibleListContainerPost, new ComponentMetadata(false));
        managedFields.put(collapsibleListContainerManipulation, new ComponentMetadata(false));

        this.panelSupport = new PanelSupport();
        this.panelSupport.analyze(managedFields);

        collapsibleListContainerPre.setHeader("Pre Actions");
        collapsibleListContainerPre.setName("Pre Actions");

        collapsibleListContainerContinuousDimension.setHeader("Continuous Dimension");
        collapsibleListContainerContinuousDimension.setName("Continuous Dimension");
        collapsibleListContainerContinuousDimension.setIcon("dimension");

        collapsibleListContainerDimension.setHeader("Dimensions");
        collapsibleListContainerDimension.setName("Dimensions");
        collapsibleListContainerDimension.setIcon("dimension");

        collapsibleListContainerPost.setHeader("Post Actions");
        collapsibleListContainerPost.setName("Post Actions");

        collapsibleListContainerManipulation.setHeader("Manipulations");
        collapsibleListContainerManipulation.setName("Manipulations");

        // Update view
        // -
        // Establish bindings
        // -

        this.panelSupport.manage(this, managedFields, jButton1);

        collapsibleListContainerPre.setCollapsed(DefaultSettings.getInstance().isCollapsed());
        collapsibleListContainerPost.setCollapsed(DefaultSettings.getInstance().isCollapsed());
        collapsibleListContainerManipulation.setCollapsed(DefaultSettings.getInstance().isCollapsed());

    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        collapsibleListContainerPre = new CollapsibleListContainer<Action>(new ActionListItemProvider(scan.getPreAction()));
        collapsibleListContainerDimension = new CollapsibleListContainer<DiscreteStepDimension>(new DiscreteStepDimensionListItemProvider(scan.getDimension()));
        collapsibleListContainerPost = new CollapsibleListContainer<Action>(new ActionListItemProvider(scan.getPostAction()));
        jPanel1 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        collapsibleListContainerContinuousDimension = new CollapsibleListContainer<ContinuousDimension>(new ContinuousDimensionListItemProvider(scan));
        collapsibleListContainerManipulation = new CollapsibleListContainer<Manipulation>(new ManipulationListItemProvider(scan.getManipulation()));

        collapsibleListContainerPre.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 153, 51)));
        collapsibleListContainerPre.setName("Pre Actions"); // NOI18N

        collapsibleListContainerDimension.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 255, 102)));
        collapsibleListContainerDimension.setToolTipText("Dimensions");
        collapsibleListContainerDimension.setName("Dimensions"); // NOI18N

        collapsibleListContainerPost.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 153, 51)));
        collapsibleListContainerPost.setName("Post Actions"); // NOI18N

        jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ch/psi/fda/ui/ce/icons/plus.png"))); // NOI18N
        jButton1.setToolTipText("Add optional items");
        jButton1.setBorderPainted(false);
        jButton1.setContentAreaFilled(false);
        jPanel1.add(jButton1);

        collapsibleListContainerContinuousDimension.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 0)));

        collapsibleListContainerManipulation.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 153, 153)));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(collapsibleListContainerPre, javax.swing.GroupLayout.DEFAULT_SIZE, 529, Short.MAX_VALUE)
            .addComponent(collapsibleListContainerContinuousDimension, javax.swing.GroupLayout.DEFAULT_SIZE, 529, Short.MAX_VALUE)
            .addComponent(collapsibleListContainerDimension, javax.swing.GroupLayout.DEFAULT_SIZE, 529, Short.MAX_VALUE)
            .addComponent(collapsibleListContainerPost, javax.swing.GroupLayout.DEFAULT_SIZE, 529, Short.MAX_VALUE)
            .addComponent(collapsibleListContainerManipulation, javax.swing.GroupLayout.DEFAULT_SIZE, 529, Short.MAX_VALUE)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 529, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(collapsibleListContainerPre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(collapsibleListContainerContinuousDimension, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(collapsibleListContainerDimension, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(collapsibleListContainerPost, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(collapsibleListContainerManipulation, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents

    @Override
    public Scan getObject() {
        return(scan);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private ch.psi.fda.ui.ce.panels.CollapsibleListContainer collapsibleListContainerContinuousDimension;
    private ch.psi.fda.ui.ce.panels.CollapsibleListContainer collapsibleListContainerDimension;
    private ch.psi.fda.ui.ce.panels.CollapsibleListContainer collapsibleListContainerManipulation;
    private ch.psi.fda.ui.ce.panels.CollapsibleListContainer collapsibleListContainerPost;
    private ch.psi.fda.ui.ce.panels.CollapsibleListContainer collapsibleListContainerPre;
    private javax.swing.JButton jButton1;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables

}
