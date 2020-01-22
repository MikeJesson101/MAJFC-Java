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


import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFormattedTextField;
import javax.swing.JTextField;

import com.mikejesson.majfc.helpers.MAJFCInputVerifier;
import com.mikejesson.majfc.helpers.MAJFCTools;

@SuppressWarnings("serial")
public class MAJFCNumberTextArea extends JFormattedTextField implements KeyListener {
	private double mMinAllowedValue;
	private double mMaxAllowedValue;
	private final double INITIAL_VALUE;
	private final int DECIMAL_PLACES;
	private double mLastValue;
	private String mLastText;
	
	/**
	 * Constructor
	 * @param min Minimum value this text area can hold
	 * @param max Maximum value this text area can hold
	 * @param initialValue Initial value for the text area
	 * @param decimalPlaces The number of decimal places to be displayed
	 */
	public MAJFCNumberTextArea(double min, double max, double initialValue, int decimalPlaces){
		super(MAJFCTools.makeDecimalFormatter(decimalPlaces, false));
		addKeyListener(this);
		addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent theEvent) {
				colourText();
			}
		});
		
		setInputVerifier(new MAJFCInputVerifier());
		
		setHorizontalAlignment(JTextField.RIGHT);

		mMinAllowedValue = min;
		mMaxAllowedValue = max;
		INITIAL_VALUE = initialValue;
		DECIMAL_PLACES = decimalPlaces;
		
		mLastValue = INITIAL_VALUE;
		mLastText = MAJFCTools.formatNumber(mLastValue, DECIMAL_PLACES, true);
		
		setValue(INITIAL_VALUE);
	}
	
	@Override
	/**
	 * Gets the preferred size for this spinner
	 * @return The preferred size
	 */
	public Dimension getPreferredSize() {
		String sizer = MAJFCTools.formatNumber(mMaxAllowedValue, DECIMAL_PLACES, true);
		int width = getFontMetrics(this.getFont()).stringWidth(sizer) + 20;

		return new Dimension(width > 50 ? width : 50, super.getPreferredSize().height);
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
		if (theEvent.getSource().equals(this) == false) {
			return;
		}
		
		int caretPosition = getCaretPosition();
		int textLength = getText().length();
		int reverseCaretPosition = textLength - caretPosition;
		char theChar = theEvent.getKeyChar();
		boolean failed = false;

		try {
			if (textLength > 0) {
				MAJFCTools.parseDouble(getText());
			} else {
				setValue(0, "0", 1);
			}
		} catch (Exception theException) {
			failed = true;
		}
		
		if (DECIMAL_PLACES == 0 && theChar == '.') {
			failed = true;
		}
		
		if (mMinAllowedValue >= 0 && theChar == '-') {
			failed = true;
		}
		
		if (Character.isLetter(theChar)) {
			failed = true;
		}
		
		if (failed == false) {
			double value = MAJFCTools.parseDouble(getText());
			
			if (value > mMaxAllowedValue){
				setForeground(Color.red);
			}
			else if (value < mMinAllowedValue){
				setForeground(Color.red);
			} else {
				setForeground(Color.black);
			}
			
			mLastValue = value;
			mLastText = getText();//MAJFCTools.formatNumber(value, DECIMAL_PLACES, true);
		}
		
		setValue(mLastValue, mLastText, reverseCaretPosition);
	}

	/**
	 * KeyListener implementation
	 * 
	 * @param theEvent the KeyEvent
	 */
	public void keyTyped(KeyEvent theEvent) {
	}

	/**
	 * Resets the value to that specified, and puts the caret back
	 * @param value The value to set
	 * @param valueText The text value to set
	 * @param reverseCaretPosition The caret position to set
	 */
	private void setValue(double value, String valueText, int reverseCaretPosition) {
		setValue(value);
		setText(valueText);
		
		int textLength = getText().length();
		int caretPosition = textLength - reverseCaretPosition;

		if (caretPosition >= 0) {
			setCaretPosition(caretPosition);
		}
	}

	/**
	 * Is the text within the allowed range?
	 * Also sets text red if value is invalid
	 * @return True if it is
	 */
	public boolean verifyContents() {
		double value = MAJFCTools.parseDouble(getText());

		return value >= mMinAllowedValue && value <= mMaxAllowedValue;
	}
	
	/**
	 * Sets the text colour according to whether the value is valid
	 */
	private void colourText() {
		if (verifyContents()) {
			setForeground(Color.black);
		} else {
			setForeground(Color.red);
		}
	}

	/**
	 * Sets the maximum value allowed
	 * @param max The new maximum allowed value
	 */
	public void setMaximumValue(Double max) {
		mMaxAllowedValue = max;
		colourText();
	}

	/**
	 * Sets the minimum value allowed
	 * @param min The new minimum allowed value
	 */
	public void setMinimumValue(Double min) {
		mMinAllowedValue = min;
		colourText();
	}
}

