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
import ch.psi.fda.model.v1.Action;
import ch.psi.fda.model.v1.ChannelAction;
import ch.psi.fda.model.v1.ScriptAction;
import ch.psi.fda.model.v1.ShellAction;
import ch.psi.fda.ui.ce.panels.ListUtil;
import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ebner
 */
public class ActionListItemProvider implements ListItemProvider<Action> {

    private final String[] actions = new String[]{"Shell Action", "Channel Action", "Script Action"};

    private List<Action> list;

    public ActionListItemProvider(List<Action> list){
        this.list = list;
    }

    @Override
    public String[] getItemKeys() {
        return(actions);
    }

    @Override
    public Component newItem(String key) {
        if(key.equals(actions[0])){
            ShellAction action = new ShellAction();
            list.add(action);
            return (getItem(action));
        }
        else if(key.equals(actions[1])){
            ChannelAction action = new ChannelAction();
            list.add(action);
            return (getItem(action));
        }
        else if(key.equals(actions[2])){
            ScriptAction action = new ScriptAction();
            list.add(action);
            return (getItem(action));
        }
        
        return null;
    }



     @Override
    public List<Component> getItems() {
        List<Component> l = new ArrayList<Component>();
        for(Action a: list){
            l.add(getItem(a));
        }
        return l;
    }

    private Component getItem(Action object){
        if(object instanceof ShellAction){
            ShellActionPanel p = new ShellActionPanel((ShellAction)object);
            p.setName("Shell Action");
            return(p);
        }
        else if(object instanceof ChannelAction){
            ChannelActionPanel p = new ChannelActionPanel((ChannelAction)object);
            p.setName("Channel Action");
            return(p);
        }
        else if(object instanceof ScriptAction){
            ScriptActionPanel p = new ScriptActionPanel((ScriptAction)object);
            p.setName("Script Action");
            return(p);
        }
        return null;
    }

    @Override
    public void removeItem(Component component) {
        if(component instanceof ShellActionPanel){
            list.remove(((ShellActionPanel)component).getObject());
        }
        else if(component instanceof ChannelActionPanel){
            list.remove(((ChannelActionPanel)component).getObject());
        }
        else if(component instanceof ScriptActionPanel){
            list.remove(((ScriptActionPanel)component).getObject());
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
        if(component instanceof ShellActionPanel){
            return (((ShellActionPanel)component).getObject());
        }
        else if(component instanceof ChannelActionPanel){
            return (((ChannelActionPanel)component).getObject());
        }
        else if(component instanceof ScriptActionPanel){
            return (((ScriptActionPanel)component).getObject());
        }
        return null;
    }
}
