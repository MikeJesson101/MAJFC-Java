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

/**
 * 
 */
package com.mikejesson.majfc.guiComponents;


import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.EventObject;

import javax.swing.JLabel;
import javax.swing.event.ChangeListener;

import com.mikejesson.majfc.helpers.MAJFCTools;

/**
 * @author mikefedora
 *
 */
@SuppressWarnings("serial")
public class MAJFCNumberSpinnerPanel extends MAJFCPanel {
	private JLabel mLabel;
	private MAJFCNumberSpinner mSpinner;

	/**
	 * Constructor 
	 * @param label Text for the label
	 * @param min Minimum value the spinner can hold
	 * @param max Maximum value the spinner can hold
	 * @param step Increment when using the up/down buttons
	 * @param initialValue Initial value for the spinner
	 */
	public MAJFCNumberSpinnerPanel(String label, double min, double max, double step, double initialValue) {
		buildGUI(label, min, max, step, initialValue);
	}
	
	/**
	 * Builds the GUI
	 * @param label Text for the label
	 * @param min Minimum value the spinner can hold
	 * @param max Maximum value the spinner can hold
	 * @param step Increment when using the up/down buttons
	 * @param initialValue Initial value for the spinner
	 */
	private void buildGUI(String label, double min, double max, double step, double initialValue) {
		setLayout(new GridBagLayout());
		
		mLabel = new JLabel(label);
		mSpinner = new MAJFCNumberSpinner(min, max, step, initialValue);
		
		add(mLabel, MAJFCTools.createGridBagConstraint(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.NORTHEAST, GridBagConstraints.NONE, 2, 0, 0, 0, 0, 0));
		add(mSpinner, MAJFCTools.createGridBagConstraint(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, 0, 0, 0, 0, 0, 0));
	}
	
	/**
	 * Adds a change listener to the spinner
	 * @param changeListener
	 */
	public void addChangeListener(ChangeListener changeListener) {
		mSpinner.addChangeListener(changeListener);
	}
	
	/**
	 * Sets the spinner value
	 * @param value The value to set it to 
	 */
	public void setValue(Object value) {
		mSpinner.setValue(value);
	}

	/**
	 * Get the value from the spinner.
	 * Also sets the "has changed" flag to false
	 * @return The spinner value
	 */
	public Object getValue() {
		return mSpinner.getValue();
	}

	/**
	 * Checks whether the source of an event was the number spinner from this panel
	 * @param theEvent The event
	 * @return True if the source was the number spinner from this panel
	 */
	public boolean isSource(EventObject theEvent) {
		Object source = theEvent.getSource();
		Component[] components = getComponents();
		
		for (int i = 0; i < components.length; ++i) {
			if (source.equals(components[i])) {
				return true;
			}
		}
		
		return false;
	}
}
