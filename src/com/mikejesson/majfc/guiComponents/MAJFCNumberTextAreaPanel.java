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
import java.beans.PropertyChangeListener;
import java.util.EventObject;

import javax.swing.JLabel;

import com.mikejesson.majfc.helpers.MAJFCTools;

/**
 * @author mikefedora
 *
 */
@SuppressWarnings("serial")
public class MAJFCNumberTextAreaPanel extends MAJFCPanel {
	private JLabel mLabel;
	private MAJFCNumberTextArea mText;

	/**
	 * Constructor 
	 * @param label Text for the label
	 * @param min Minimum value this text area can hold
	 * @param max Maximum value this text area can hold
	 * @param initialValue Initial value for the text area
	 * @param decimalPlaces The number of decimal places to be displayed
	 */
	public MAJFCNumberTextAreaPanel(String label, double min, double max, double initialValue, int decimalPlaces) {
		this(label, min, max, initialValue, decimalPlaces, null);
	}
	
	/**
	 * Constructor 
	 * @param label Text for the label
	 * @param min Minimum value this text area can hold
	 * @param max Maximum value this text area can hold
	 * @param initialValue Initial value for the text area
	 * @param decimalPlaces The number of decimal places to be displayed
	 * @param pcListener The PropertyChangeListener to add to the TextField in this panel
	 */
	public MAJFCNumberTextAreaPanel(String label, double min, double max, double initialValue, int decimalPlaces, PropertyChangeListener pcListener) {
		buildGUI(label, min, max, initialValue, decimalPlaces);
		
		if (pcListener != null) {
			addPropertyChangeListenerToTextField(pcListener);
		}
	}
	
	/**
	 * Builds the GUI
	 * @param label Text for the label
	 * @param min Minimum value this text area can hold
	 * @param max Maximum value this text area can hold
	 * @param initialValue Initial value for the text area
	 * @param decimalPlaces The number of decimal places to be displayed
	 */
	private void buildGUI(String label, double min, double max, double initialValue, int decimalPlaces) {
		setLayout(new GridBagLayout());
		
		mLabel = new JLabel(label);
		mText = new MAJFCNumberTextArea(min, max, initialValue, decimalPlaces);
		mText.setColumns(10);
		
		add(mLabel, MAJFCTools.createGridBagConstraint(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHEAST, GridBagConstraints.NONE, 5, 0, 0, 0, 0, 0));
		add(mText, MAJFCTools.createGridBagConstraint(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, 0, 0, 0, 0, 0, 0));
	}
	
	/**
	 * Adds a PropertyChangeListener to the text area
	 * @param listener The listener
	 */
	public void addPropertyChangeListenerToTextField(PropertyChangeListener listener) {
		mText.addPropertyChangeListener("value", listener);
	}
	
	/**
	 * Sets the label text
	 * @param text The new text for the label
	 */
	public void setText(String text) {
		mLabel.setText(text);
	}
	
	/**
	 * Sets the spinner value
	 * @param value The value to set it to 
	 */
	public boolean setValue(Object value) {
		if (mText.getValue().equals(value) == false) {
			mText.setValue(value);
			return true;
		}
		
		return false;
	}

	/**
	 * Is the text within the allowed range?
	 * @return True if it is
	 */
	public boolean verifyContents() {
		return mText.verifyContents();
	}

	/**
	 * Get the value from the spinner.
	 * Also sets the "has changed" flag to false
	 * @return The spinner value
	 */
	public Double getValue() {
		Object value = mText.getValue();
		
		if (value.getClass().equals(Double.class)) {
			return (Double) mText.getValue();
		} else if (value.getClass().equals(Long.class)) {
			return ((Long) mText.getValue()).doubleValue();
		} else if (value.getClass().equals(Integer.class)) {
			return ((Integer) mText.getValue()).doubleValue();
		}

		return null;
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

	/**
	 * Sets the maximum value allowed
	 * @param max The new maximum allowed value
	 */
	public void setMaximumValue(Double max) {
		mText.setMaximumValue(max);
	}

	/**
	 * Sets the minimum value allowed
	 * @param min The new minimum allowed value
	 */
	public void setMinimumValue(Double min) {
		mText.setMinimumValue(min);
	}
}
