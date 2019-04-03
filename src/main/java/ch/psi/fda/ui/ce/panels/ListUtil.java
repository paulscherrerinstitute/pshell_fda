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

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ebner
 */
public class ListUtil {

    public static void moveItemUp(List list, Object object) {
        Logger.getLogger(ListUtil.class.getName()).log(Level.INFO, "Move item up"+object);
        Integer index = null;
        for(int i=0;i<list.size();i++){
            if(list.get(i).equals(object)){
                index = i;
                break;
            }
        }

        if(index!=null && index>0){
            Object a = list.remove((int)index);
            list.add(index-1, a);
        }
    }

    public static void moveItemDown(List list, Object object) {
        Logger.getLogger(ListUtil.class.getName()).log(Level.INFO, "Move item down"+object);
        Integer index = null;
        for(int i=0;i<list.size();i++){
            if(list.get(i).equals(object)){
                index = i;
                break;
            }
        }

        if(index!=null && index<(list.size()-1)){
            Object a = list.remove((int)index);
            list.add(index+1, a);
        }
    }
}
