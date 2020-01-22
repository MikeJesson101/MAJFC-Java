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
import java.awt.event.ActionListener;
import java.util.EventObject;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import com.mikejesson.majfc.helpers.MAJFCTools;

/**
 * @author mikefedora
 *
 */
@SuppressWarnings("serial")
public class MAJFCTranslatingDropDownPanel<Type extends Object> extends MAJFCPanel {
	private JLabel mLabel;
	private JComboBox<String> mDropdown;
	private Hashtable<String, Type> mLookup;
	private Hashtable<Type, String> mReverseLookup;
	private Hashtable<Type, Integer> mIndexLookup;
	
	/**
	 * 
	 * @param labelText The text for the label
	 * @param options The options to be put in the drop-down ("<option_text 1>:<option 1>;<option_text 2>:<option 2>;..."
	 * @param initialChoice The initial choice
	 */
	public MAJFCTranslatingDropDownPanel(String labelText, String options, Type initialChoice) {
		this(labelText, options, initialChoice, SwingConstants.CENTER);
	}
	
	/**
	 * 
	 * @param labelText The text for the label
	 * @param options The options to be put in the drop-down ("<option_text 1>:<option 1>;<option_text 2>:<option 2>;..."
	 * @param initialChoice The initial choice
	 */
	public MAJFCTranslatingDropDownPanel(String labelText, String options, Type initialChoice, int alignment) {
		this(labelText);
		fillDropDownWithOptions(options, initialChoice);
		
		((JLabel) mDropdown.getRenderer()).setHorizontalAlignment(alignment);
	}

	/**
	 * 
	 * @param labelText The text for the label
	 * @param options The options to be put in the drop-down
	 * @param displayTextList The display text for the options (must be same size as options)
	 * @param initialChoice The initial choice
	 */
	public MAJFCTranslatingDropDownPanel(String labelText, Vector<Type> options, Vector<String> displayTextList, Type initialChoice) {
		this(labelText);
		fillDropDownWithOptions(options, displayTextList, initialChoice);
	}
	
	/**
	 * private constructor
	 * @param labelText The text for the label
	 */
	private MAJFCTranslatingDropDownPanel(String labelText) {
		super(new GridBagLayout());
		buildGUI(labelText);
	}
	
	/**
	 * Builds the GUI
	 * @param labelText The text for the label
	 */
	private void buildGUI(String labelText) {
		mLabel = new JLabel(labelText);
		mDropdown = new JComboBox<String>();
		
		int x = 0;
		int y = 0;
		add(mLabel, MAJFCTools.createGridBagConstraint(x++, y, 1, 1, 0.0, 0.0, GridBagConstraints.LINE_START, GridBagConstraints.NONE, 0, 0, 0, 0, 0, 0));
		add(mDropdown, MAJFCTools.createGridBagConstraint(x++, y, 1, 1, 1.0, 0.0, GridBagConstraints.LINE_START, GridBagConstraints.HORIZONTAL, 0, 0, 0, 0, 0, 0));
	}

	/**
	 * Fills the drop-down
	 * @param options The options to be put in the drop-down ("<option_text 1>:<option 1>;<option_text 2>:<option 2>;..."
	 * @param initialChoice The initial selection
	 */
	@SuppressWarnings("unchecked")
	public void fillDropDownWithOptions(String options, Type initialChoice) {
		StringTokenizer st1 = new StringTokenizer(options, ";");
		int numberOfOptions = st1.countTokens();
		mLookup = new Hashtable<String, Type>(numberOfOptions);
		mReverseLookup = new Hashtable<Type, String>(numberOfOptions);
		mIndexLookup = new Hashtable<Type, Integer>(numberOfOptions);
		mDropdown.removeAllItems();
		
		int index = 0;
		while (st1.hasMoreTokens()) {
			StringTokenizer st2 = new StringTokenizer(st1.nextToken(), ":");
			
			if (st2.countTokens() != 2) {
				continue;
			}
			
			String displayText = st2.nextToken();
			
			Type value;
			
			if (initialChoice.getClass().equals(Integer.class)) {
				value = (Type) new Integer(st2.nextToken());
			} else if (initialChoice.getClass().equals(Double.class)) {
				value = (Type) new Double(st2.nextToken());
			} else if (initialChoice.getClass().equals(Character.class)) {
				value = (Type) new Character(st2.nextToken().charAt(0));
			} else if (initialChoice.getClass().equals(String.class)) {
				value = (Type) st2.nextToken();
			} else {
				System.out.println("Unrecognised option type in MAJFCTranslatingDropdown::fillDataFileUnitOptions: " + initialChoice.getClass());
				continue;
			}
			
			mLookup.put(displayText, value);
			mReverseLookup.put(value, displayText);
			mIndexLookup.put(value, index);
			
			mDropdown.addItem(displayText);
			
			++index;
		}
		
		if (initialChoice != null) {
			mDropdown.setSelectedItem(mReverseLookup.get(initialChoice));
		}
	}


	/**
	 * Fills the drop-down
	 * @param options The options to be put in the drop-down ("<option_text 1>:<option 1>;<option_text 2>:<option 2>;..."
	 */
	public void fillDropDownWithOptions(Vector<Type> options, Vector<String> displayTextList, Type initialChoice) {
		int numberOfEntries = options.size();
		
		if (numberOfEntries != displayTextList.size()) {
			return;
		}

		mLookup = new Hashtable<String, Type>(numberOfEntries);
		mReverseLookup = new Hashtable<Type, String>(numberOfEntries);
		mDropdown.removeAllItems();
		
		for (int i = 0; i < numberOfEntries; ++i) {
			Type option = options.elementAt(i);
			String displayText = displayTextList.elementAt(i);
			mLookup.put(displayText, option);
			mReverseLookup.put(option, displayText);
			
			mDropdown.addItem(displayText);
		}
		
		mDropdown.setSelectedItem(mReverseLookup.get(initialChoice));
	}

	/**
	 * Adds an ActionListener to the drop-down
	 * @param actionListener The listener to add
	 */
	public void addActionListener(ActionListener actionListener) {
		mDropdown.addActionListener(actionListener);
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
	 * Sets the selected item. An event is not thrown - if it should be, use @see {@link #setSelectedIndex(int)}
	 * @param selectedItem The item to select
	 */
	public boolean setSelectedItem(Type selectedItem) {
		@SuppressWarnings("unchecked")
		Type currentSelection = (Type) mDropdown.getSelectedItem();
		if (mDropdown.getSelectedItem() == null || currentSelection.equals(selectedItem) == false) {
			mDropdown.setSelectedItem(mReverseLookup.get(selectedItem));
			return true;
		}
		
		return false;
	}

	/**
	 * Gets the selected item.
	 * @return The selected item or null if nothing selected
	 */
	public Type getSelectedItem() {
		Object selectedItem = mDropdown.getSelectedItem();
		
		if (selectedItem == null) {
			return null;
		}
		
		return mLookup.get(selectedItem);
	}

	/**
	 * Sets the selected index directly.
	 * An event is thrown - if it shouldn't be, use @see {@link #setSelectedItem(Object)}
	 * @param selectedIndex
	 */
	public void setSelectedIndex(int selectedIndex) {
		mDropdown.setSelectedIndex(selectedIndex);
	}

	/**
	 * Sets the selected item by setting the selected index.
	 * An event is thrown - if it shouldn't be, use @see {@link #setSelectedItem(Object)}
	 * @param selectedIndex
	 */
	public void setSelectedItemViaIndex(Type selectedItem) {
		mDropdown.setSelectedIndex(mIndexLookup.get(selectedItem));
	}
}
