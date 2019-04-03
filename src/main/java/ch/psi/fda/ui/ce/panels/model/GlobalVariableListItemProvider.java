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
import ch.psi.fda.model.v1.Variable;
import ch.psi.fda.ui.ce.panels.ListUtil;
import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ebner
 */
public class GlobalVariableListItemProvider implements ListItemProvider<Variable> {

    private List<Variable> list;

    private final String[] actions = new String[]{"Variable"};

    public GlobalVariableListItemProvider(List<Variable> list){
        this.list = list;
    }

    @Override
    public String[] getItemKeys() {
        return(actions);
    }

    @Override
    public Component newItem(String key) {
        if(key.equals(actions[0])){
            Variable r = new Variable();
            list.add(r);
            return(getItem(r));
        }
        return null;
    }

    @Override
    public List<Component> getItems() {
        List<Component> l = new ArrayList<Component>();
        for(Variable r: list){
            l.add(getItem(r));
        }
        return l;
    }

    private Component getItem(Variable object) {
        GlobalVariablePanel p = new GlobalVariablePanel(object);
        p.setName("Variable");
        return p;
    }

    @Override
    public void removeItem(Component component) {
        if(component instanceof GlobalVariablePanel){
            list.remove(((GlobalVariablePanel)component).getObject());
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
        if(component instanceof GlobalVariablePanel){
            return (((GlobalVariablePanel)component).getObject());
        }
        return null;
    }
}
