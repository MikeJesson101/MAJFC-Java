/**
 * 
 */
package com.mikejesson.majfc.guiComponents;

import javax.swing.JRadioButton;

/**
 * @author Mike
 *
 */
@SuppressWarnings("serial")
public class MAJFCRadioButton extends JRadioButton {
	public MAJFCRadioButton(String label) {
		super(label);
	}
	
	/**
	 * Sets whether the checkbox is selected and reports whether this is a change of state
	 * @param state
	 * @return true if the state changes, false if not
	 */
	public boolean setSelectedReportChange(boolean state) {
		if (isSelected() != state) {
			setSelected(state);
			return true;
		}
		
		return false;
	}

}
