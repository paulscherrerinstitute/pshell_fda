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
import ch.psi.fda.model.v1.DiscreteStepDimension;
import ch.psi.fda.ui.ce.panels.ListUtil;
import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ebner
 */
public class DiscreteStepDimensionListItemProvider implements ListItemProvider<DiscreteStepDimension> {

    private List<DiscreteStepDimension> list;

    private final String[] dimensions = new String[]{"Dimension"};

    public DiscreteStepDimensionListItemProvider(List<DiscreteStepDimension> list){
        this.list = list;
    }

    @Override
    public String[] getItemKeys() {
        return(dimensions);
    }

    @Override
    public Component newItem(String key) {
        if(key.equals(dimensions[0])){
            DiscreteStepDimension dsd = new DiscreteStepDimension();
            list.add(dsd);
            return(getItem(dsd));
        }
        return null;
    }

    @Override
    public List<Component> getItems() {
        List<Component> l = new ArrayList<Component>();
        for(DiscreteStepDimension d: list){
            l.add(getItem(d));
        }
        return l;
    }

    private Component getItem(DiscreteStepDimension object) {
        if(object instanceof DiscreteStepDimension){
            DiscreteStepDimensionPanel p = new DiscreteStepDimensionPanel(object);
            p.setName("D");
            return(p);
        }
        return null;
    }

    @Override
    public void removeItem(Component component) {
        if(component instanceof DiscreteStepDimensionPanel){
            list.remove(((DiscreteStepDimensionPanel)component).getObject());
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
        if(component instanceof DiscreteStepDimensionPanel){
            return (((DiscreteStepDimensionPanel)component).getObject());
        }
        return null;
    }

}
