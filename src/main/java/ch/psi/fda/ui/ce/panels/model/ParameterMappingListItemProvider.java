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
import ch.psi.fda.model.v1.ChannelParameterMapping;
import ch.psi.fda.model.v1.IDParameterMapping;
import ch.psi.fda.model.v1.ParameterMapping;
import ch.psi.fda.model.v1.VariableParameterMapping;
import ch.psi.fda.ui.ce.panels.ListUtil;
import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ebner
 */
public class ParameterMappingListItemProvider implements ListItemProvider<ParameterMapping> {

    private List<ParameterMapping> list;

    private final String[] names = new String[]{"ID Mapping", "Channel Mapping", "Variable Mapping"};

    public ParameterMappingListItemProvider(List<ParameterMapping> list){
        this.list = list;
    }

    @Override
    public String[] getItemKeys() {
        return(names);
    }

    @Override
    public Component newItem(String key) {
        if(key.equals(names[0])){
            IDParameterMapping pm = new IDParameterMapping();
            list.add(pm);
            return(getItem(pm));
        }
        else if(key.equals(names[1])){
            ChannelParameterMapping cpm = new ChannelParameterMapping();
            list.add(cpm);
            return(getItem(cpm));
        }
        else if(key.equals(names[2])){
            VariableParameterMapping cpm = new VariableParameterMapping();
            list.add(cpm);
            return(getItem(cpm));
        }
        return null;
    }

    @Override
    public List<Component> getItems() {
        List<Component> l = new ArrayList<Component>();
        for(ParameterMapping m: list){
            l.add(getItem(m));
        }
        return l;
    }

    private Component getItem(ParameterMapping object) {
        if(object instanceof IDParameterMapping){
            IDParameterMappingPanel p = new IDParameterMappingPanel((IDParameterMapping)object);
            p.setName("ID Mapping");
            return(p);
        }
        else if(object instanceof ChannelParameterMapping){
            ChannelParameterMappingPanel p = new ChannelParameterMappingPanel((ChannelParameterMapping)object);
            p.setName("Channel Mapping");
            return(p);
        }
        else if(object instanceof VariableParameterMapping){
            GlobalVariableParameterMappingPanel p = new GlobalVariableParameterMappingPanel((VariableParameterMapping)object);
            p.setName("Variable Mapping");
            return(p);
        }
        return null;
    }

    @Override
    public void removeItem(Component component) {
        if(component instanceof IDParameterMappingPanel){
            list.remove(((IDParameterMappingPanel)component).getObject());
        }
        else if(component instanceof ChannelParameterMappingPanel){
            list.remove(((ChannelParameterMappingPanel)component).getObject());
        }
        else if(component instanceof GlobalVariableParameterMappingPanel){
            list.remove(((GlobalVariableParameterMappingPanel)component).getObject());
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
        if(component instanceof IDParameterMappingPanel){
            return(((IDParameterMappingPanel)component).getObject());
        }
        else if(component instanceof ChannelParameterMappingPanel){
            return(((ChannelParameterMappingPanel)component).getObject());
        }
        else if(component instanceof GlobalVariableParameterMappingPanel){
            return(((GlobalVariableParameterMappingPanel)component).getObject());
        }
        return null;
    }
}
