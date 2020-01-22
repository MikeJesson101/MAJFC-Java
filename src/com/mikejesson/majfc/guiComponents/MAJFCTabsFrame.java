// This file is part of MAJ's Velocity Signal Analyser 
// Copyright (C) 2009 - 2016 Michael Jesson
// 
// MAJ's Velocity Signal Analyser is free software; you can redistribute it and/or
// modify it under the terms of the GNU General Public License
// as published by the Free Software Foundation; either version 3
// of the License, or (at your option) any later version.
// 
// MAJ's Velocity Signal Analyser is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
// 
// You should have received a copy of the GNU General Public License
// along with MAJ's Velocity Signal Analyser.  If not, see <http://www.gnu.org/licenses/>.

package com.mikejesson.majfc.guiComponents;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import com.mikejesson.majfc.guiComponents.MAJFCTabbedPanel.MAJFCTabContents;
import com.mikejesson.majfc.helpers.MAJFCTools;


/**
 * @author MAJ727
 *
 */
@SuppressWarnings("serial")
public class MAJFCTabsFrame extends MAJFCStackedPanelWithFrame {
	private final MAJFCTabbedPanel mTabbedPanel;

	/**
	 * Constructor
	 * 
	 * @param parent The parent panel
	 * @param tabContents Any additional panels to add
	 */
	public MAJFCTabsFrame(MAJFCStackedPanelWithFrame parent, MAJFCTabContents ... tabContents) {
		super(parent, new GridBagLayout());
		
		for (int i = 0; i < tabContents.length; ++i) {
			MAJFCPanel panel = tabContents[i].getPanel();
			if (panel instanceof MAJFCStackedPanelWithFrame) {
				((MAJFCStackedPanelWithFrame) panel).setParent(this);
			}
		}
		
		mTabbedPanel = new MAJFCTabbedPanel(tabContents);
		
		add(mTabbedPanel, MAJFCTools.createGridBagConstraint(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.BOTH, 0, 0, 0, 0, 0, 0));
	}
}
