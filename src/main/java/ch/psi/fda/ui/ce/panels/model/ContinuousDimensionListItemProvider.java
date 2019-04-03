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

package ch.psi.fda.ui.ce.panels.model;

import ch.psi.fda.ui.ce.panels.ListItemProvider;
import ch.psi.fda.model.v1.ContinuousDimension;
import ch.psi.fda.model.v1.Scan;
//import ch.psi.fda.ui.ee.api.ExecutionService;
import ch.psi.fda.ui.ce.panels.model.util.ModelUtil;
import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
//import org.openide.util.Lookup;

/**
 *
 * @author ebner
 */
public class ContinuousDimensionListItemProvider implements ListItemProvider<ContinuousDimension> {

    private final String[] dimensions = new String[]{"Continous Dimension"};

    private Scan scan;

    public ContinuousDimensionListItemProvider(Scan scan){
        this.scan = scan;
    }

    @Override
    public String[] getItemKeys() {

        ///TODO: 
        // Only show this option if OTFSCAN is configured - workaround
        //ExecutionService eservice = Lookup.getDefault().lookup(ExecutionService.class);
        //if(eservice != null && ! eservice.supportsFeature("ch.psi.aq.feature.otfscan")){
        //    return new String[]{};
        //}

        // If no continuous dimension is specified return its key. Otherwise return no key
        // (Ensures that only one continuous dimension can be added)
        if(scan.getCdimension()==null){
            return(dimensions);
        }
        else{
            return new String[] {};
        }
    }

    @Override
    public Component newItem(String key) {
        if(key.equals(dimensions[0])){
            ContinuousDimension d = new ContinuousDimension();
            scan.setCdimension(d);
            return(getItem(d));
        }
        return null;
    }

    @Override
    public List<Component> getItems() {
        List<Component> l = new ArrayList<Component>();
        if(scan.getCdimension()!=null){
            l.add(getItem(scan.getCdimension()));
        }
        return l;
    }

    private Component getItem(ContinuousDimension object) {
        if(object instanceof ContinuousDimension){
            ContinuousDimensionPanel p = new ContinuousDimensionPanel(object);
            p.setName("D");
            return(p);
        }
        return null;
    }

    @Override
    public void removeItem(Component component) {
        if(component instanceof ContinuousDimensionPanel){
            ContinuousDimension d = ((ContinuousDimensionPanel)component).getObject();
            ModelUtil.getInstance().findInMappingAndRemove(d.getPositioner());
            scan.setCdimension(null); // Remove continuous dimension from scan
        }
    }

    @Override
    public boolean isEmpty() {
        return (scan.getCdimension()==null);
    }

    @Override
    public int size() {
        if(scan.getCdimension()==null){
            return 0;
        }
        return 1;
    }

    @Override
    public void moveItemUp(Component component) {
        // Not supported as there is only one continuous dimension
    }

    @Override
    public void moveItemDown(Component component) {
        // Not supported as there is only one continuous dimension
    }
}
