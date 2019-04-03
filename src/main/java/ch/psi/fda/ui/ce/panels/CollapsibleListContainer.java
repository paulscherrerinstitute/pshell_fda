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
 * CollapsibleListContainer.java
 *
 * Created on Jan 19, 2011, 10:03:49 AM
 */

package ch.psi.fda.ui.ce.panels;

import ch.psi.fda.ui.ce.panels.CollapsibleContainer;
import java.awt.event.MouseAdapter;

/**
 *
 * @author ebner
 */
public class CollapsibleListContainer<T> extends javax.swing.JPanel {

    private ListContainer<T> listContainer;

    /**
     * Default constructor - do not use!
     */
    public CollapsibleListContainer() {
        this(null);
    }

    /** Creates new form CollapsibleListContainer */
    public CollapsibleListContainer(ListItemProvider<T> provider) {
        this.listContainer = new ListContainer<T>(provider);
        listContainer.showAdd(false);
        
        initComponents();

        this.collapsibleContainer1.setChildContainer(listContainer);
        this.collapsibleContainer1.activateAddButton(new MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                listContainer.showAddPopup(evt, collapsibleContainer1);
            }
        });

        // set parent container - enable/disable add button
        listContainer.setParentContainer(collapsibleContainer1);

        // Enable or disable add button based on list container
        collapsibleContainer1.setAddEnabled(listContainer.isAddEnabled());
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        collapsibleContainer1 = new CollapsibleContainer(listContainer);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(collapsibleContainer1, javax.swing.GroupLayout.DEFAULT_SIZE, 489, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(collapsibleContainer1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private ch.psi.fda.ui.ce.panels.CollapsibleContainer collapsibleContainer1;
    // End of variables declaration//GEN-END:variables


    /**
     * Get header of this container
     * @return
     */
    public String getHeader(){
        return(this.collapsibleContainer1.getHeader());
    }

    /**
     * Set header of the container
     * @param header
     */
    public void setHeader(String header){
        this.collapsibleContainer1.setHeader(header);
    }

    /**
     * Collapse/open container
     * @param collapsed
     */
    public void setCollapsed(boolean collapsed){
        this.collapsibleContainer1.setCollapsed(collapsed);
    }

    /**
     * Get icon of the container
     * @param icon
     */
    public void setIcon(String icon){
        collapsibleContainer1.setIcon(icon);
    }



    /**
     * Get the list of possible items that can be added to the
     * internal list container.
     * @return
     */
    public String[] getItemKeys(){
        return(listContainer.getItemKeys());
    }

    /**
     * Add an item via its key
     * @param key
     */
    public void newItem(String key){
        this.listContainer.newItem(key);
    }

    /**
     * Check whether list is empty
     * @return
     */
    public boolean isEmpty(){
        return(listContainer.isEmpty());
    }
}