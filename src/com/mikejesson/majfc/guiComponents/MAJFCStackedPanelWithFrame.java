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
import java.awt.Image;
import java.awt.LayoutManager;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import java.util.LinkedList;

import javax.swing.JFrame;

/**
 * @author Mike
 *
 */
@SuppressWarnings("serial")
public class MAJFCStackedPanelWithFrame extends MAJFCPanel {
	private MAJFCStackedPanelWithFrame mParent;
	private LinkedList<MAJFCStackedPanelWithFrame> mChildren;
	protected JFrame mFrame;
	private static Image sDefaultIconImage = null;

	/**
	 * Constructor - top panel in stack (no parent)
	 * @param layout
	 */
	public MAJFCStackedPanelWithFrame(LayoutManager layout) {
		super(layout);
		
		mParent = null;
		mChildren = new LinkedList<MAJFCStackedPanelWithFrame>();
	}
	
	/**
	 * Constructor - child panel
	 * @param layout
	 * @param parent The parent frame
	 */
	public MAJFCStackedPanelWithFrame(MAJFCStackedPanelWithFrame parent, LayoutManager layout) throws NullPointerException {
		this(layout);
		
		setParent(parent);
	}
	
	public void setParent(MAJFCStackedPanelWithFrame parent) {
		mParent = parent;
		
		if (mParent != null) {
			mParent.mChildren.add(this);
		}
	}

	/**
	 * Show this panel in its own frame
	 * @param frameTitle The title for the frame
	 * @return The frame the panel is shown in
	 */
	public JFrame showInFrame(String frameTitle) {
		return showInFrame(frameTitle, true);
	}

	/**
	 * Show this panel in its own frame
	 * @param frameTitle The title for the frame
	 * @return The frame the panel is shown in
	 */
	private JFrame showInFrame(String frameTitle, boolean makeVisibleImmediately) {
		mFrame = new JFrame(frameTitle);
		mFrame.add(this);
		mFrame.validate();
		mFrame.pack();
		mFrame.setVisible(makeVisibleImmediately);
		mFrame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				close();
			}
			@Override
			public void windowClosed(WindowEvent e) {
				close();
			}
		});
		
		if (sDefaultIconImage != null) {
			mFrame.setIconImage(sDefaultIconImage);
		}

		return mFrame;
	}
	
	/**
	 * Sets the default icon image for any frames
	 */
	public static void setDefaultIconImage(Image image) {
		sDefaultIconImage = image;
	}
	
	/**
	 * Show this panel in its own frame
	 * @param frameTitle The title for the frame
	 * @return The frame the panel is shown in
	 */
	public JFrame showInFrame(String frameTitle, WindowListener windowListener, Component relativeTo) {
		return showInFrame(frameTitle, windowListener, relativeTo, true);
	}
	
	/**
	 * Show this panel in its own frame
	 * @param frameTitle The title for the frame
	 * @return The frame the panel is shown in
	 */
	public JFrame showInFrame(String frameTitle, WindowListener windowListener, Component relativeTo, boolean makeVisibleImmediately) {
		mFrame = showInFrame(frameTitle, false);
		mFrame.addWindowListener(windowListener);
		mFrame.setLocationRelativeTo(relativeTo);
		mFrame.setVisible(makeVisibleImmediately);
				
		return mFrame;
	}
	
	public void close() {
		if (mParent != null) {
			mParent.mChildren.remove(this);
		}

        if (mFrame != null) {
        	mFrame.setVisible(false);
        }
        
        while (mChildren.size() > 0) {
        	mChildren.getFirst().close();
        }
    }

	public MAJFCStackedPanelWithFrame getStackParent() {
		return mParent;
	}
	
	/**
	 * Updates the display and the display of any children
	 * Any overrides of this should (probably) call this at the end
	 */
	public void updateDisplay() {
		int numberOfChildren = mChildren.size();
		
		for (int i = 0; i < numberOfChildren; ++i) {
        	mChildren.get(i).updateDisplay();
        }
	}

	public JFrame getFrame() {
		return mFrame;
	}
}
