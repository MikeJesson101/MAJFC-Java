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


import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;

import com.mikejesson.majfc.helpers.MAJFCTools;

/**
 * @author MAJ727
 *
 */
@SuppressWarnings("serial")
public abstract class MAJFCAbstractButtonTab extends MAJFCPanel {
	private TabButton mCloseGUI;
	private JLabel mLabel;
	private final ImageIcon mImage;
	private final MAJFCAbstractButtonTab mTabComponent;
	
	/**
	 * Constructor
	 * @param title The title for the tab
	 */
	protected MAJFCAbstractButtonTab(String title, ImageIcon image) {
		super(new GridBagLayout());
		
		mImage = image;
		
		setOpaque(false);
		
		buildGUI(title);
		
		mTabComponent = this;
	}
	
	/**
	 * Builds the GUI
	 */
	private void buildGUI(String title) {
		mCloseGUI = new TabButton();
		mLabel = new JLabel(title);
		add(mLabel, MAJFCTools.createGridBagConstraint(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.LINE_START, GridBagConstraints.NONE, 2, 5, 3, 0, 0, 0));
		add(mCloseGUI, MAJFCTools.createGridBagConstraint(1, 0, 1, 1, 0.0, 1.0, GridBagConstraints.LINE_START, GridBagConstraints.NONE, 0, 10, 0, 0, 0, 0));
	}
	
	public void setTitle(String title) {
		mLabel.setText(title);
	}
	
	/**
	 * Helper class
	 * @author MAJ727
	 *
	 */
	private class TabButton extends JButton implements ActionListener {
		/**
		 * Constructor
		 * @param yCoord The y-coordinate of the point this button applies to
		 * @param zCoord The z-coordinate of the point this button applies to
		 */
		private TabButton() {
			setPreferredSize(new Dimension(mImage.getIconWidth(), mImage.getIconHeight()));
			//setIcon(mImage);
			setBorderPainted(false);
			addActionListener(this);
			addMouseListener(new MouseAdapter() {
				@Override
				public void mouseExited(MouseEvent theEvent) {
					mTabComponent.repaint();
				}
			});
		}

	    /**
	     * Paint the button
	     * @param graphics The graphics object to use
	     */
		protected void paintComponent(Graphics graphics) {
            Graphics2D graphics2 = (Graphics2D) graphics.create();
            
            if (getModel().isPressed()) {
                graphics2.translate(1, 1);
            }
            
            if (getModel().isRollover()) {
            	super.paintComponent(graphics);
            }
	           
            graphics2.drawImage(mImage.getImage(), 0, 0, this);

            graphics2.dispose();
        }

		@Override
		/**
		 * ActionListener implementation
		 * @param theEvent The action event
		 */
		public void actionPerformed(ActionEvent theEvent) {
			onButtonPressed();
		}
	}
	
	/**
	 * Tab button pressed
	 */
	protected abstract void onButtonPressed();
}
