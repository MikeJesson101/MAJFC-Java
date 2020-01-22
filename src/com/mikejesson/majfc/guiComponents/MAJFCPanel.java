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
import java.awt.Container;
import java.awt.LayoutManager;

import javax.swing.JComponent;
import javax.swing.JPanel;

/**
 * @author mikefedora
 *
 */
@SuppressWarnings("serial")
public class MAJFCPanel extends JPanel {
	/**
	 * Constructor
	 */
	public MAJFCPanel() {
	}

	/**
	 * Constructor
	 * @param layout
	 */
	public MAJFCPanel(LayoutManager layout) {
		super(layout);
	}

	/**
	 * Enables all components on this panel. Calls setGUIStates at the end, which should be used to do any state-based
	 * enabling of GUI components (overriding the blanket enabling done by this method).
	 * @see #setGUIStates() 
	 * @param enabled True if enabling, false if disabling
	 */
	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		
		Component[] components = getComponents();
		
		for (int i = 0; i < components.length; ++i) {
			setEnabled(components[i], enabled);
		}
		
		setGUIStates();
	}
	
	/**
	 * Set a whole tree of components enabled.
	 * @param component The component to be enabled
	 * @param enabled True if enabling, false if disabling
	 */
	private void setEnabled(Component component, boolean enabled) {
		if (component instanceof Container) {
			Component[] components = ((Container) component).getComponents();
			
			for (int i = 0; i < components.length; ++i) {
				if (components[i] instanceof Container) {
					setEnabled((Container) components[i], enabled);
				}
			}
		}

		component.setEnabled(enabled);
	}
	
	/**
	 * Sets GUI component enabling based on state rather than just "enable/disable everything"
	 * Called at the end of setEnabled
	 * @see #setEnabled(boolean)
	 */
	protected void setGUIStates() {
	}
	
	/**
	 * An internal component has changed
	 */
	public void childChanged() {
		setGUIStates();
	}
	
	/**
	 * Validates all components on this panel.
	 */
	@Override
	public void validate() {
		super.validate();
		
		Component[] components = getComponents();
		
		for (int i = 0; i < components.length; ++i) {
			validate(components[i]);
		}
	}
	
	/**
	 * Validates a whole tree of components.
	 * @param component The component to be valdiated
	 */
	private void validate(Component component) {
		if (component instanceof Container) {
			Component[] components = ((Container) component).getComponents();
			
			for (int i = 0; i < components.length; ++i) {
				if (components[i] instanceof Container) {
					validate((Container) components[i]);
				}
			}
		}

		component.validate();
	}
	
	/**
	 * Revalidates all components on this panel.
	 */
	@Override
	public void revalidate() {
		super.revalidate();
		
		Component[] components = getComponents();
		
		for (int i = 0; i < components.length; ++i) {
			revalidate(components[i]);
		}
	}
	
	/**
	 * Revalidates a whole tree of components.
	 * @param component The component to be valdiated
	 */
	private void revalidate(Component component) {
		if (component instanceof Container) {
			Component[] components = ((Container) component).getComponents();
			
			for (int i = 0; i < components.length; ++i) {
				if (components[i] instanceof Container) {
					revalidate((Container) components[i]);
				}
			}
		}

		if (component instanceof JComponent) {
			((JComponent) component).revalidate();
		}
	}

	/**
	 * Verifies values in those MAJFC... components that have a verification method (boolean verifyContents())
	 * @return True if all values in the component and it's child components are valid
	 */
	public boolean componentValuesValid() {
		return componentValuesValid(this);
	}
	
	/**
	 * Verifies values in those MAJFC... components that have a verification method (boolean verifyContents())
	 * @param component The component to verify the contents for
	 * @return True if all values in the component and it's child components are valid
	 */
	private boolean componentValuesValid(Component component) {
		if (component instanceof MAJFCNumberTextArea) {
			if (((MAJFCNumberTextArea) component).verifyContents() == false) {
				return false;
			}
		} else if (component instanceof MAJFCNumberTextAreaPanel) {
			if (((MAJFCNumberTextAreaPanel) component).verifyContents() == false) {
				return false;
			}
		} else if (component instanceof Container) {
			Component[] components = ((Container) component).getComponents();
			for (int i = 0; i < components.length; ++i) {
				if (componentValuesValid(components[i]) == false){
					return false;
				}
			}
		}
			
		return true;
	}
}
