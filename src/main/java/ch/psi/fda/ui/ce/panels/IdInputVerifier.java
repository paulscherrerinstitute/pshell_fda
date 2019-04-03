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

import ch.psi.pshell.ui.App;
import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JTextField;
//import org.openide.DialogDisplayer;
//import org.openide.NotifyDescriptor;

/**
 * Input verifier for JTextFields holding ids
 * If the id contains wrong characters a dialog box will be shown
 * @author ebner
 */
public class IdInputVerifier extends InputVerifier {

    @Override
    public boolean verify(JComponent jc) {
        JTextField tf = (JTextField) jc;
        boolean v = tf.getText().matches("^[a-zA-Z]+[a-zA-Z0-9-_]*$");

        if (!v) {
            /*      
            NotifyDescriptor d;
            if(!tf.getText().matches("^[a-zA-Z].*")){
                d = new NotifyDescriptor.Message("Id need to start with an character", NotifyDescriptor.WARNING_MESSAGE);
            }
            else{
                d = new NotifyDescriptor.Message("Invalid characters in Id - only accepting a-z,A-Z,0-9,-,_", NotifyDescriptor.WARNING_MESSAGE);
            }
            DialogDisplayer.getDefault().notify(d);
            */
            String message = null;
            if(!tf.getText().matches("^[a-zA-Z].*")){
                message = "Id need to start with an character";
            }
            else{
                message = "Invalid characters in Id - only accepting a-z,A-Z,0-9,-,_";
            }
            App.getInstance().getMainFrame().showMessage("Error", message);
        }

        return (v);
    }
}
