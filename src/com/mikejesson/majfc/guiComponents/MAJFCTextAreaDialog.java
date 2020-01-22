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
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.mikejesson.majfc.helpers.MAJFCTools;

@SuppressWarnings("serial")
public abstract class MAJFCTextAreaDialog extends MAJFCStackedPanelWithFrame {
	private JTextArea mTextArea;
	private JButton mSaveButton;
	private JButton mCloseButton;
	
	public MAJFCTextAreaDialog(MAJFCStackedPanelWithFrame parent, String saveButtonLabel, String closeButtonLabel, String text) {
		super(parent, new GridBagLayout());

		buildGUI(saveButtonLabel, closeButtonLabel);

		mTextArea.setText(text);

		showInFrame("");
	}
	
	protected abstract String getDefaultSaveDirectory();

	private void doClose() {
		close();
	}
	
	private void buildGUI(String saveButtonLabel, String closeButtonLabel) {
		MAJFCPanel scrollPaneInterior = new MAJFCPanel(new GridBagLayout());
		JScrollPane scrollPane = new JScrollPane(scrollPaneInterior);
		scrollPane.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		mTextArea = new JTextArea();
		mTextArea.setEditable(false);
		
		scrollPaneInterior.add(mTextArea, MAJFCTools.createGridBagConstraint(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, 0, 5, 0, 0, 3, 3));

		mSaveButton = new JButton(saveButtonLabel);
		mSaveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				save();
			}
		});
		
		mCloseButton = new JButton(closeButtonLabel);
		mCloseButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				doClose();
			}
		});
		
		MAJFCPanel buttonsPanel = new MAJFCPanel(new GridBagLayout());
		
		int x = 0, y = 0;
		buttonsPanel.add(mSaveButton, MAJFCTools.createGridBagConstraint(x++, y, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, 0, 5, 0, 0, 3, 3));
		buttonsPanel.add(mCloseButton, MAJFCTools.createGridBagConstraint(x, y, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, 0, 5, 0, 0, 3, 3));
		
		x = 0;
		y = 0;
		add(scrollPane, MAJFCTools.createGridBagConstraint(x, y++, 1, 1, 1.0, 1.0, GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, 10, 10, 10, 10, 0, 0));
		add(buttonsPanel, MAJFCTools.createGridBagConstraint(x, y, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, 0, 0, 10, 0, 0, 0));
	}
	
	/**
	 * Writes the Matlab export text to a file
	 */
	private void save() {
		JFileChooser fileChooser = new JFileChooser(getDefaultSaveDirectory());
		fileChooser.setApproveButtonText(mSaveButton.getText());
			
		int returnVal = fileChooser.showOpenDialog(getStackParent());

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
				FileWriter writer = new FileWriter(file);
				String text = mTextArea.getText();
				//text.replace(MAJFCTools.stringValueOf(MAJFCTools.SYSTEM_NEW_LINE_CHAR), MAJFCTools.SYSTEM_NEW_LINE_STRING);
				//writer.write(text);

				for (int i = 0; i < text.length(); ++i) {
					char ch = text.charAt(i);
					
					if (ch == MAJFCTools.SYSTEM_NEW_LINE_CHAR) {
						writer.append(MAJFCTools.SYSTEM_NEW_LINE_STRING);
					}
					writer.append(text.charAt(i));
				}
			
				writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
	}

	@Override
	public Dimension getPreferredSize() {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		
		return new Dimension((int) (screenSize.width * 0.9), (int) (screenSize.height * 0.9));
	}
}
