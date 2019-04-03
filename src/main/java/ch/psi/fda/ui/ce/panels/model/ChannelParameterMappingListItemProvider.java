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
import ch.psi.fda.ui.ce.panels.ListUtil;
import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ebner
 */
public class ChannelParameterMappingListItemProvider implements ListItemProvider<ChannelParameterMapping> {

    private List<ChannelParameterMapping> list;

    private final String[] names = new String[]{"Channel Mapping"};

    public ChannelParameterMappingListItemProvider(List<ChannelParameterMapping> list){
        this.list = list;
    }

    @Override
    public String[] getItemKeys() {
        return(names);
    }

    @Override
    public Component newItem(String key) {
        if(key.equals(names[0])){
            ChannelParameterMapping mapping = new ChannelParameterMapping();
            list.add(mapping);
            return(getItem(mapping));
        }
        return null;
    }

    @Override
    public List<Component> getItems() {
        List<Component> l = new ArrayList<Component>();
        for(ChannelParameterMapping m: list){
            l.add(getItem(m));
        }
        return(l);
    }

    private Component getItem(ChannelParameterMapping mapping){
        if(mapping instanceof ChannelParameterMapping){
            ChannelParameterMappingPanel p = new ChannelParameterMappingPanel(mapping);
            p.setName("Channel Mapping");
            return(p);
        }
        return null;
    }

    @Override
    public void removeItem(Component component) {
        if(component instanceof ChannelParameterMappingPanel){
            list.remove(((ChannelParameterMappingPanel)component).getObject());
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
        if(component instanceof ChannelParameterMappingPanel){
            return (((ChannelParameterMappingPanel)component).getObject());
        }
        return null;
    }
}
