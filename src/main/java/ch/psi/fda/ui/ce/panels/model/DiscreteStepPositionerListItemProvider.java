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
import ch.psi.fda.model.v1.ArrayPositioner;
import ch.psi.fda.model.v1.DiscreteStepPositioner;
import ch.psi.fda.model.v1.FunctionPositioner;
import ch.psi.fda.model.v1.LinearPositioner;
import ch.psi.fda.model.v1.PseudoPositioner;
import ch.psi.fda.model.v1.RegionPositioner;
import ch.psi.fda.ui.ce.panels.ListUtil;
import ch.psi.fda.ui.ce.panels.model.util.ModelUtil;
import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ebner
 */
public class DiscreteStepPositionerListItemProvider implements ListItemProvider<DiscreteStepPositioner> {

    private List<DiscreteStepPositioner> list;

    private final String[] positioners = new String[]{"Linear Positioner", "Region Positioner", "Array Positioner", "Pseudo Positioner", "Function Positioner"};

    public DiscreteStepPositionerListItemProvider(List<DiscreteStepPositioner> list){
        this.list = list;
    }

    @Override
    public String[] getItemKeys() {
        return(positioners);
    }

    @Override
    public Component newItem(String key) {
        if(key.equals(positioners[0])){
            LinearPositioner lp = new LinearPositioner();
            list.add(lp);
            return(getItem(lp));
        }
        else if(key.equals(positioners[1])){
            RegionPositioner rp = new RegionPositioner();
            list.add(rp);
            return(getItem(rp));
        }
         else if(key.equals(positioners[2])){
            ArrayPositioner ap = new ArrayPositioner();
            list.add(ap);
            return(getItem(ap));
        }
        else if(key.equals(positioners[3])){
            PseudoPositioner pp = new PseudoPositioner();
            list.add(pp);
            return(getItem(pp));
        }
        else if(key.equals(positioners[4])){
            FunctionPositioner pp = new FunctionPositioner();
            list.add(pp);
            return(getItem(pp));
        }
        return null;
    }

     @Override
    public List<Component> getItems() {
        List<Component> l = new ArrayList<Component>();
        for(DiscreteStepPositioner p: list){
            l.add(getItem(p));
        }
        return l;
    }

    private Component getItem(DiscreteStepPositioner object) {
        if(object instanceof LinearPositioner){
            LinearPositionerPanel p = new LinearPositionerPanel((LinearPositioner)object);
            p.setName("Linear P");
            return(p);
        }
        else if(object instanceof RegionPositioner){
            RegionPositionerPanel p = new RegionPositionerPanel((RegionPositioner)object);
            p.setName("Region P");
            return(p);
        }
        else if(object instanceof ArrayPositioner){
            ArrayPositionerPanel p = new ArrayPositionerPanel((ArrayPositioner)object);
            p.setName("Array P");
            return(p);
        }
        else if(object instanceof PseudoPositioner){
            PseudoPositionerPanel p = new PseudoPositionerPanel((PseudoPositioner)object);
            p.setName("Pseudo P");
            return(p);
        }
        else if(object instanceof FunctionPositioner){
            FunctionPositionerPanel p = new FunctionPositionerPanel((FunctionPositioner)object);
            p.setName("Function P");
            return(p);
        }

        return null;
    }

    @Override
    public void removeItem(Component component) {
        DiscreteStepPositioner o = null;

        if(component instanceof LinearPositionerPanel){
            o = ((LinearPositionerPanel)component).getObject();
            list.remove(o);
        }
        else if(component instanceof RegionPositionerPanel){
            o = ((RegionPositionerPanel)component).getObject();
            list.remove(o);
        }
        else if(component instanceof ArrayPositionerPanel){
            o = ((ArrayPositionerPanel)component).getObject();
            list.remove(o);
        }
        else if(component instanceof PseudoPositionerPanel){
            o = ((PseudoPositionerPanel)component).getObject();
            list.remove(o);
        }
        else if(component instanceof FunctionPositionerPanel){
            o = ((FunctionPositionerPanel)component).getObject();
            list.remove(o);
        }

        // Find references to this object and remove the reference
        if(o != null){
            ModelUtil.getInstance().findInMappingAndRemove(o);
            ModelUtil.getInstance().refreshAll();
        }
    }

    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public void moveItemUp(Component component) {
        ListUtil.moveItemUp(list, getObject(component));
    }

    @Override
    public void moveItemDown(Component component) {
        ListUtil.moveItemDown(list, getObject(component));
    }

    private Object getObject(Component component){
        if(component instanceof LinearPositionerPanel){
            return ((LinearPositionerPanel)component).getObject();
        }
        else if(component instanceof RegionPositionerPanel){
            return ((RegionPositionerPanel)component).getObject();
        }
        else if(component instanceof ArrayPositionerPanel){
            return ((ArrayPositionerPanel)component).getObject();
        }
        else if(component instanceof PseudoPositionerPanel){
            return ((PseudoPositionerPanel)component).getObject();
        }
        else if(component instanceof FunctionPositionerPanel){
            return ((FunctionPositionerPanel)component).getObject();
        }
        return null;
    }
}
