/**
 * 
 */
package com.mikejesson.majfc.guiComponents;

import java.awt.event.ActionListener;

/**
 * @author Mike
 *
 */
@SuppressWarnings("serial")
public abstract class MAJFCAbstractCallbackButton<CallbackObject extends Object> extends MAJFCDialogButton {
	public MAJFCAbstractCallbackButton(String title) {
		super(title);
	}

	public MAJFCAbstractCallbackButton(String title, ActionListener actionListener) {
		super(title, actionListener);
	}
	
	public abstract void callback(CallbackObject co);
}
