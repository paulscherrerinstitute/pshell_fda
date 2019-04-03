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

import ch.psi.fda.model.v1.DiscreteStepDimension;
import ch.psi.fda.model.v1.Guard;
import ch.psi.fda.ui.ce.panels.ListItemProvider;
import ch.psi.fda.model.v1.GuardCondition;
import ch.psi.fda.ui.ce.panels.ListUtil;
import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ebner
 */
public class GuardListItemProvider implements ListItemProvider<GuardCondition> {

    DiscreteStepDimension dimension;

    private final String[] positioners = new String[]{"Guard"};

    public GuardListItemProvider(DiscreteStepDimension dimension){
        this.dimension = dimension;
    }

    @Override
    public String[] getItemKeys() {
        return(positioners);
    }

    @Override
    public Component newItem(String key) {
        if(dimension.getGuard()== null){
            dimension.setGuard(new Guard());
        }

        if(key.equals(positioners[0])){
            GuardCondition gc = new GuardCondition();
            dimension.getGuard().getCondition().add(gc);
            return(getItem(gc));
        }
        return null;
    }

    @Override
    public List<Component> getItems() {
        List<Component> l = new ArrayList<Component>();
        if(dimension.getGuard()!=null){
            for(GuardCondition c: dimension.getGuard().getCondition()){
                l.add(getItem(c));
            }
        }
        return l;
    }

    private Component getItem(GuardCondition object) {
        if(object instanceof GuardCondition){
            GuardConditionPanel p = new GuardConditionPanel(object);
            p.setName("Guard");
            return(p);
        }
        return null;
    }

    @Override
    public void removeItem(Component component) {
        if(component instanceof GuardConditionPanel){
            GuardCondition c = ((GuardConditionPanel)component).getObject();
            if(dimension.getGuard()!=null){
                dimension.getGuard().getCondition().remove(c);

                // Remove guard from dimension if there is no condition left
                if(dimension.getGuard().getCondition().isEmpty()){
                    dimension.setGuard(null);
                }
            }
            // There is nothing to be removed
        }
    }

    @Override
    public boolean isEmpty() {
        if(dimension.getGuard()!=null){
            return dimension.getGuard().getCondition().isEmpty();
        }
        return true;
    }

    @Override
    public int size() {
        if(dimension.getGuard()!=null){
            return dimension.getGuard().getCondition().size();
        }
        return 0;
    }

    @Override
    public void moveItemUp(Component component) {
        ListUtil.moveItemUp(dimension.getGuard().getCondition(), getObject(component));
    }

    @Override
    public void moveItemDown(Component component) {
        ListUtil.moveItemDown(dimension.getGuard().getCondition(), getObject(component));
    }

    private Object getObject(Component component){
        if(component instanceof GuardConditionPanel){
            return ((GuardConditionPanel)component).getObject();
        }
        return null;
    }
}
