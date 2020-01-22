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

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;

/**
 * @author MAJ727
 *
 */
@SuppressWarnings("serial")
public class MAJFCButton extends JButton {

	/**
	 * 
	 */
	public MAJFCButton() {
		setHideActionText(true);
	}

	/**
	 * @param arg0
	 */
	public MAJFCButton(Icon arg0) {
		super(arg0);
	}

	/**
	 * @param arg0
	 */
	public MAJFCButton(String arg0) {
		super(arg0);
	}

	/**
	 * @param arg0
	 */
	public MAJFCButton(Action arg0) {
		super(arg0);
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public MAJFCButton(String arg0, Icon arg1) {
		super(arg0, arg1);
	}

}
