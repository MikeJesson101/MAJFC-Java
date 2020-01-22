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
import java.awt.Frame;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;


import javax.swing.JDialog;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;

/**
 * A simple dialog with a progress bar.
 * @author MAJ727
 *
 */
public abstract class MAJFCProgressDialog extends SwingWorker<Void, Void> {
	private JProgressBar mProgressBar;
	private JDialog mDialog;

	/**
	 * Constructor
	 * @param parent The parent frame of the dialog
	 * @param title The title for the dialog
	 */
	@SuppressWarnings("serial")
	public MAJFCProgressDialog(Frame parent, String title) {
		super();

		mProgressBar = new JProgressBar(0, 100);
		
		addPropertyChangeListener(new PropertyChangeListener() {
    		public void propertyChange(PropertyChangeEvent theEvent) {
    	        if ("progress" == theEvent.getPropertyName()) {
    	            int progress = (Integer)theEvent.getNewValue();
    	            mProgressBar.setValue(progress);
    	        } 
    	    }
    	});
		
		mDialog = new JDialog(parent, true) {
			@Override
			public Dimension getPreferredSize() {
//				Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
				int width = 250;//(int) (screenSize.width * 0.9);
				int height = 50;//(int) width/4;

				return new Dimension(width, height);
			}
		};
		mDialog.setTitle(title);
		mDialog.add(mProgressBar);
		mDialog.validate();
		mDialog.pack();
		mDialog.setLocationRelativeTo(parent);
	}

	/**
	 * Sets the dialog visible.
	 * Use instead of execute.
	 */
	protected void setVisible() {
		execute();
		mDialog.setVisible(true);
	}

	@Override
	/**
	 * The stuff done by this task. The implementation should include calls to setProgress to update the
	 * progress bar (progress value range 0-100)
	 */
	protected abstract Void doInBackground() throws Exception;

	/**
	 * Stuff done when the task is complete. Override this in the child class if any actions are required.
	 */
	public void whenDone() {
	}

    @Override
    /**
     * Stuff to do when the background stuff is complete
     */
    public final void done() {
    	mDialog.setVisible(false);
    	whenDone();
    }
 }
