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


import javax.swing.JTabbedPane;

import com.mikejesson.majfc.guiComponents.MAJFCPanel;

/**
 * @author MAJ727
 *
 */
@SuppressWarnings("serial")
public class MAJFCTabbedPanel extends JTabbedPane {
	/**
	 * Constructor - bit of a hack to allow velocity component panels in the original child class
	 * @param tabContents1
	 * @param tabContents2
	 * @param tabContents3
	 * @param tabContents
	 */
	public MAJFCTabbedPanel(int unused, MAJFCTabContents tabContents1, MAJFCTabContents tabContents2, MAJFCTabContents tabContents3, MAJFCTabContents ... tabContents) {
		addTab(tabContents1.mLabel, tabContents1.mPanel);
		addTab(tabContents2.mLabel, tabContents2.mPanel);
		addTab(tabContents3.mLabel, tabContents3.mPanel);
		
		if (tabContents != null) {
			for (int i = 0; i < tabContents.length; ++i) {
				addTab(tabContents[i].mLabel, tabContents[i].mPanel);
			}
		}
		
		validate();
	}
	
	/**
	 * Constructor
	 * 
	 * @param tabContents Any panels to add
	 */
	public MAJFCTabbedPanel(MAJFCTabContents ... tabContents) {
		for (int i = 0; i < tabContents.length; ++i) {
			addTab(tabContents[i].mLabel, tabContents[i].mPanel);
		}
		
		validate();
	}
	
	/**
	 * Helper class
	 * @author MAJ727
	 *
	 */
	public static class MAJFCTabContents {
		private final String mLabel;
		private final MAJFCPanel mPanel;
	
		/**
		 * Constructor
		 * @param label Label for the additional tab
		 * @param panel The frame to show in the additional tab
		 */
		public MAJFCTabContents(String label, MAJFCPanel panel) {
			mLabel = label;
			mPanel = panel;
		}
		
		public MAJFCPanel getPanel() {
			return mPanel;
		}
	}
}
