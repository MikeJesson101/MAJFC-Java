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

package com.mikejesson.majfc.guiComponents;


import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFormattedTextField;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.mikejesson.majfc.helpers.MAJFCTools;

@SuppressWarnings("serial")
public class MAJFCNumberSpinner extends JSpinner implements KeyListener, ChangeListener{
	private JFormattedTextField mMyTextField;
	private final double MIN;
	private final double MAX;
	@SuppressWarnings("unused")
	private final double STEP;
	@SuppressWarnings("unused")
	private final double INITIAL_VALUE;
	private double mLastValue;
	
	/**
	 * Constructor
	 * @param min Minimum value this spinner can hold
	 * @param max Maximum value this spinner can hold
	 * @param step Increment when using the up/down buttons
	 * @param initialValue Initial value for the spinner
	 */
	public MAJFCNumberSpinner(double min, double max, double step, double initialValue){
		super(new SpinnerNumberModel(initialValue, min, max, step));

		MIN = min;
		MAX = max;
		STEP = step;
		INITIAL_VALUE = initialValue;
		
		mLastValue = initialValue;
		
		getModel().addChangeListener(this);
		mMyTextField = ((JSpinner.DefaultEditor)getEditor()).getTextField();
		mMyTextField.addKeyListener(this);
	}

	@Override
	/**
	 * Gets the preferred size for this spinner
	 * @return The preferred size
	 */
	public Dimension getPreferredSize() {
		return new Dimension(50, super.getPreferredSize().height);
	}
	
	/**
	 * ChangeListener implementation
	 * @param theEvent
	 */
	public void stateChanged(ChangeEvent theEvent) {
	}

	/**
	 * KeyListener implementation
	 * 
	 * @param theEvent the KeyEvent
	 */
	public void keyPressed(KeyEvent theEvent) {
	}

	/**
	 * KeyListener implementation
	 * 
	 * @param theEvent the KeyEvent
	 */
	public void keyReleased(KeyEvent theEvent) {
		try {
			double value = MAJFCTools.parseDouble(mMyTextField.getText());
		
			if (value > MAX){
				mMyTextField.setText(MAJFCTools.stringValueOf(MAX));
			}
			else if (value < MIN){
				mMyTextField.setText(MAJFCTools.stringValueOf(MIN));
			}
			
			mLastValue = MAJFCTools.parseDouble(mMyTextField.getText());
		} catch (Exception theException) {
			mMyTextField.setText(MAJFCTools.stringValueOf(mLastValue));
		}
	}

	/**
	 * KeyListener implementation
	 * 
	 * @param theEvent the KeyEvent
	 */
	public void keyTyped(KeyEvent theEvent) {
	}
}

