/**
 * 
 */
package com.mikejesson.majfc.guiComponents;

import java.awt.event.ActionListener;
import java.io.File;

import com.mikejesson.majfc.helpers.MAJFCTools;

/**
 * @author Mike
 *
 */
@SuppressWarnings("serial")
public class MAJFCAbstractFileOrDirChooserButton extends MAJFCAbstractCallbackButton<File> {
	private String mFileOrDirNameWithPath;
	private String mFileOrDirName;
	
	public MAJFCAbstractFileOrDirChooserButton(String fileOrDirNameWithPath) {
		super("");
		
		setFileOrDir(fileOrDirNameWithPath);
	}

	public MAJFCAbstractFileOrDirChooserButton(String fileOrDirNameWithPath, ActionListener actionListener) {
		super("", actionListener);
		
		setFileOrDir(fileOrDirNameWithPath);
	}

	@Override
	public void callback(File file) {
      	setFileOrDir(file.getAbsolutePath());
	}
	
	public void setFileOrDir(String fileOrDirNameWithPath) {
		mFileOrDirNameWithPath = fileOrDirNameWithPath;
		
		int lastSeparatorIndex = mFileOrDirNameWithPath.lastIndexOf(MAJFCTools.SYSTEM_FILE_PATH_SEPARATOR);
		mFileOrDirName = lastSeparatorIndex == -1 ? mFileOrDirNameWithPath : mFileOrDirNameWithPath.substring(lastSeparatorIndex + 1);
		
		super.setText(makeText(mFileOrDirName));
		setToolTipText(mFileOrDirNameWithPath);
	}

	protected String makeText(String mFileOrDirName) {
		return mFileOrDirName;
	}
	
	public String getFileOrDir() {
		return getToolTipText();
	}
	
	/**
	 * @deprecated Use {@link #setFileOrDir(String)}
	 */
	@Override
	public void setText(String text) {
	}
	
	/**
	 * @deprecated Use {@link #getFileOrDir()}
	 */
	@Override
	public String getText() {
		return super.getText();
	}
}
