// This file is part of MAJFC 
// Copyright (C) 2009 - 2016 Michael Jesson
// 
// MAJFC is free software; you can redistribute it and/or
// modify it under the terms of the GNU General Public License
// as published by the Free Software Foundation; either version 3
// of the License, or (at your option) any later version.
// 
// MAJFC is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
// 
// You should have received a copy of the GNU General Public License
// along with MAJFC.  If not, see <http://www.gnu.org/licenses/>.

package com.mikejesson.majfc.helpers;


import java.text.ParseException;

import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField.AbstractFormatter;

import com.mikejesson.majfc.guiComponents.MAJFCNumberTextArea;

/**
 * @author MAJ727
 *
 */
public class MAJFCInputVerifier extends InputVerifier {
	@Override
	public boolean shouldYieldFocus(JComponent input) {
		return verify(input);
	}

	/* (non-Javadoc)
	 * @see javax.swing.InputVerifier#verify(javax.swing.JComponent)
	 */
	@Override
	public boolean verify(JComponent input) {
        if (input instanceof MAJFCNumberTextArea) {
       	 MAJFCNumberTextArea textArea = (MAJFCNumberTextArea) input;
            AbstractFormatter formatter = textArea.getFormatter();
            String text = textArea.getText();
            
            if (formatter != null) {
                try {
                     formatter.stringToValue(text);
                 } catch (ParseException theParserException) {
                     return false;
                 }
            }
            
 			return textArea.verifyContents();
         }
        
         return true;
	}

}
