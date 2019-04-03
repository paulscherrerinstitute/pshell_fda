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

package ch.psi.fda.ui.ce.panels;

import java.awt.datatransfer.DataFlavor;
//import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.logging.Level;
//import java.util.logging.Logger;

/**
 *
 * @author ebner
 */
public class ListContainerDropTargetListener implements DropTargetListener {

    private final ListContainer panel;
    private  DataFlavor flavor;

    public ListContainerDropTargetListener(ListContainer targetPanel) {

        // Create supported flavor
        try {
            flavor = new DataFlavor(DataFlavor.javaJVMLocalObjectMimeType + ";class=" + ListItem.class.getCanonicalName());
        } catch (ClassNotFoundException ex) {
//            throw new RuntimeException(ex);

        }

        this.panel = targetPanel;
    }

    @Override
    public void dragEnter(DropTargetDragEvent dtde) {
    }

    @Override
    public void dragOver(DropTargetDragEvent dtde) {
    }

    @Override
    public void dropActionChanged(DropTargetDragEvent dtde) {
    }

    @Override
    public void dragExit(DropTargetEvent dte) {
    }

    /**
     * <p>The user drops the item. Performs the drag and drop calculations and layout.</p>
     * @param dtde
     */
    public void drop(DropTargetDropEvent dtde) {
//        Logger.getLogger(ListContainerDropTargetListener.class.getName()).log(Level.INFO, "Step 5 of 7: The user dropped the panel. The drop(...) method will compare the drops location with other panels and reorder the panels accordingly.");
//
//        Object transferableObj = null;
//
//        // What does the Transferable support
//        if (dtde.getTransferable().isDataFlavorSupported(this.flavor)) {
//            try {
//                transferableObj = dtde.getTransferable().getTransferData(this.flavor);
//            } catch (UnsupportedFlavorException ex) {
//                Logger.getLogger(ListContainerDropTargetListener.class.getName()).log(Level.SEVERE, null, ex);
//            } catch (IOException ex) {
//                Logger.getLogger(ListContainerDropTargetListener.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
//
//
//        // If didn't find an item, bail
//        if (transferableObj == null) {
//            return;
//        }
//
//        // Cast it to the panel. By this point, we have verified it is
//        // a RandomDragAndDropPanel.
//        ListItem droppedPanel = (ListItem) transferableObj;
//
//        // Get the y offset from the top of the WorkFlowSheetPanel
//        // for the drop option (the cursor on the drop)
//        int dropYLocation = dtde.getLocation().y;
//
//        // We need to map the Y axis values of drop as well as other
//        // RandomDragAndDropPanel so can sort by location.
//        Map<Integer, ListItem> yLocationMap = new HashMap<Integer, ListItem>();
//        yLocationMap.put(dropYLocation, droppedPanel);
//
//        List<ListItem> panels = panel.getPanels();
//
//        // Iterate through the panels and going to find their locations.
//        for (ListItem p : panels) {
//
//            // Grab the y value
//            int y = p.getY();
//
//            // If the dropped panel, skip
//            if (!p.equals(droppedPanel)) {
//                yLocationMap.put(y, p);
//            }
//        }
//
//        // Grab the Y values and sort them
//        List<Integer> keys = new ArrayList<Integer>();
//        keys.addAll(yLocationMap.keySet());
//        Collections.sort(keys);
//
//        // Put the panels in list in order of appearance
//        List<ListItem> orderedPanels = new ArrayList<ListItem>();
//
//        panels.clear();
//        for (Integer i : keys) {
//            panels.add(yLocationMap.get(i));
//        }
//
//        // Request relayout of the panel
//        this.panel.relayout();
    }
}
