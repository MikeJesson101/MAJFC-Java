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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.Icon;

/**
 * @author MAJ727
 *
 */
@SuppressWarnings("serial")
public class MAJFCLinkedGUIComponentsAction extends AbstractAction {
	private AbstractButton[] mLinkedGUIComponents;
	private ActionListener mActionListener;

	/**
	 * Creates a DAAction and assigns a load of listeners to it
	 * @param name
	 * @param icon
	 * @param description
	 * @param linkedGUIComponent the button which causes this action
	 * @param actionListener
	 */
	public MAJFCLinkedGUIComponentsAction(String name, Icon icon, String description, AbstractButton linkedGUIComponent, ActionListener actionListener) {
		super(name, icon);
		AbstractButton[] linkedGUIComponents = {linkedGUIComponent};
		initialise(description, linkedGUIComponents, actionListener);
	}
	
	/**
	 * Creates a DAAction and assigns a load of listeners to it
	 * @param name
	 * @param icon
	 * @param description
	 * @param linkedGUIComponents the buttons which cause this action
	 * @param actionListener
	 */
	public MAJFCLinkedGUIComponentsAction(String name, Icon icon, String description, AbstractButton[] linkedGUIComponents, ActionListener actionListener) {
		super(name, icon);
		initialise(description, linkedGUIComponents, actionListener);
	}
	
	/**
	 * Initialises stuff. Called from the constructor
	 * @param description
	 * @param linkedGUIComponents the buttons which cause this action
	 * @param actionListener
	 */
	private void initialise(String description, AbstractButton[] linkedGUIComponents, ActionListener actionListener) {
		mLinkedGUIComponents = linkedGUIComponents;
		mActionListener = actionListener;
		setupLinkedGUIComponents();
		putValue(SHORT_DESCRIPTION, description);
	}

	private void setupLinkedGUIComponents(){
		for (int i = 0; i < mLinkedGUIComponents.length; ++i){
			if (mLinkedGUIComponents[i] == null)
				break;
			
			mLinkedGUIComponents[i].setAction(this);
		}
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent theEvent) {
		mActionListener.actionPerformed(theEvent);
	}

}
