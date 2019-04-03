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

import ch.psi.fda.model.v1.Configuration;
import ch.psi.fda.model.v1.Notification;
import ch.psi.fda.model.v1.Recipient;
import ch.psi.fda.ui.ce.panels.ListItemProvider;
import ch.psi.fda.ui.ce.panels.ListUtil;
import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ebner
 */
public class RecipientListItemProvider implements ListItemProvider<String> {

    private Configuration configuration;

    private final String[] actions = new String[]{"Recipient"};

    public RecipientListItemProvider(Configuration configuration){
        this.configuration = configuration;
    }

    @Override
    public String[] getItemKeys() {
        return(actions);
    }

    @Override
    public Component newItem(String key) {
        if(configuration.getNotification()==null){
            configuration.setNotification(new Notification());
        }
        if(key.equals(actions[0])){
            Recipient s = new Recipient();
            configuration.getNotification().getRecipient().add(s);
            return(getItem(s));
        }
        return null;
    }

    @Override
    public List<Component> getItems() {
        List<Component> l = new ArrayList<Component>();
        if(configuration.getNotification()!=null){
            for(Recipient s: configuration.getNotification().getRecipient()){
                l.add(getItem(s));
            }
        }
        return l;
    }
    
    private Component getItem(Recipient object) {
        RecipientPanel p = new RecipientPanel(object);
        p.setName("Recipient");
        return(p);
    }

    @Override
    public void removeItem(Component component) {
        if(component instanceof RecipientPanel){
            Recipient c = ((RecipientPanel)component).getObject();
            if(configuration.getNotification()!= null){
                configuration.getNotification().getRecipient().remove(c);
                
                // Remove notification object if there are no recipients left
                if(configuration.getNotification().getRecipient().isEmpty()){
                    configuration.setNotification(null);
                }
            }
            // There is nothing to be removed
        }
    }

    @Override
    public boolean isEmpty() {
        if(configuration.getNotification() != null){
            return configuration.getNotification().getRecipient().isEmpty();
        }
        return true;
    }

    @Override
    public int size() {
        if(configuration.getNotification() != null){
            return configuration.getNotification().getRecipient().size();
        }
        return 0;
    }

    @Override
    public void moveItemUp(Component component) {
        ListUtil.moveItemUp(configuration.getNotification().getRecipient(), getObject(component));
    }

    @Override
    public void moveItemDown(Component component) {
        ListUtil.moveItemDown(configuration.getNotification().getRecipient(), getObject(component));
    }

    private Object getObject(Component component){
        if(component instanceof RecipientPanel){
            return ((RecipientPanel)component).getObject();
        }
        return null;
    }

}
