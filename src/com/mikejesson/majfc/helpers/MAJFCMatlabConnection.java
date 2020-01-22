package com.mikejesson.majfc.helpers;

import java.util.LinkedList;

import javax.swing.JFrame;

import com.mikejesson.majfc.guiComponents.MAJFCProgressDialog;

import matlabcontrol.MatlabConnectionException;
import matlabcontrol.MatlabInvocationException;
import matlabcontrol.MatlabProxy;
import matlabcontrol.MatlabProxyFactory;
import matlabcontrol.MatlabProxy.DisconnectionListener;
import matlabcontrol.MatlabProxyFactory.RequestCallback;
import matlabcontrol.MatlabProxyFactoryOptions.Builder;

public class MAJFCMatlabConnection {
	private boolean mConnectionAvailable;
	private boolean mTestingConnection;
	private MatlabProxyFactory mProxyFactory;
	private MatlabProxy mProxy = null;
	private LinkedList<String> mEvalStack;
	private LinkedList<SetVariableDetails> mSetVariableStack;
	private final JFrame mFrame;
	private final String mConnectionTestDialogTitle;
	private final String mProgressDialogTitle;

	public MAJFCMatlabConnection(JFrame frame, String connectionTestDialogTitle, String progressDialogTitle) {
		mFrame = frame;
		mConnectionTestDialogTitle = connectionTestDialogTitle;
		mProgressDialogTitle = progressDialogTitle;
		mEvalStack = new LinkedList<String>();
		mSetVariableStack = new LinkedList<SetVariableDetails>();

		new ConnectionTestTask();
	}
	
	/**
	 * Is the Matlab connection ok? i.e. can a matlabcontrol proxy be created.
	 * If the connection test is still in progress then this will block until the result is available.
	 * @return
	 */
	public boolean connectionAvailable() {
		while (mTestingConnection) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
			}
		}
		
		return mConnectionAvailable;
	}
	
	public void sendToMatlab(String commandString) throws Exception {
		sendToMatlab(commandString, mFrame);
	}
	
	public void sendToMatlab(String commandString, JFrame frame) throws Exception {
		mEvalStack.push(commandString);

		@SuppressWarnings("unused")
		EvalTask evalThread = new EvalTask(frame, new MatlabCommand() {
			public void execute() throws MatlabInvocationException {
				mProxy.eval(mEvalStack.pollLast());
			}
		});
	}
	
	public void setMatlabVariable(String variable, Object value) throws Exception {
		setMatlabVariable(variable, value, mFrame);
	}
	
	public void setMatlabVariable(String variable, Object value, JFrame frame) throws Exception {
		mSetVariableStack.push(new SetVariableDetails(variable, value));

		@SuppressWarnings("unused")
		EvalTask evalThread = new EvalTask(frame, new MatlabCommand() {
			public void execute() throws MatlabInvocationException {
				SetVariableDetails svd = mSetVariableStack.pollLast();
				mProxy.setVariable(svd.mVariable, svd.mValue);
			}
		});
	}
	
	/**
	 * Helper class
	 */
	private class ConnectionTestTask extends MAJFCProgressDialog {
		private ConnectionTestTask() {
			super(mFrame, mConnectionTestDialogTitle);

			setVisible();
		}

		@Override
		protected Void doInBackground() throws Exception {
			Builder builder = new Builder();
			builder.setHidden(true);

			mProxyFactory = new MatlabProxyFactory(builder.build());
			
			setProgress(50);
			
			try {
				mTestingConnection = true;
				MAJFCLogger.logTiming("Matlab connection test start");
				mProxyFactory.requestProxy(new RequestCallback() {
					@Override
					public void proxyCreated(MatlabProxy proxy) {
						mConnectionAvailable = true;
						try {
							proxy.exit();
						} catch (MatlabInvocationException e) {
						}
					}
				});
				
				// If we successfully create a connection, wait for a few seconds otherwise it all seems clunky as Matlab starts.
				// This is aesthetic only - there is no real need for it!
				for (int i = 0; i < 5; i++) {
					setProgress(50 + 5 * i);
					Thread.sleep(1000);
				}
			} catch (MatlabConnectionException e) {
				mConnectionAvailable = false;
			} finally {
				mProxyFactory = null;
				MAJFCLogger.logTiming("Matlab connection test end");
				mTestingConnection = false;
			}
		
			setProgress(100);
			return null;
		}
	};
	
	/**
	 * Helper class
	 */
	private class EvalTask extends MAJFCProgressDialog {
		private final MatlabCommand mCommand;
		
		private EvalTask(JFrame frame, MatlabCommand command) {
			super(frame, mProgressDialogTitle);

			mCommand = command;
			
			setVisible();
		}

		@Override
		protected Void doInBackground() throws Exception {
			try {
				if (mProxyFactory == null) {
					mProxyFactory = new MatlabProxyFactory();
				}

				setProgress(50);

				if (mProxy == null) {
					mProxy = mProxyFactory.getProxy();
					mProxy.addDisconnectionListener(new DisconnectionListener() {
						@Override
						public void proxyDisconnected(MatlabProxy arg0) {
							mProxy = null;
						}
					});
				}

				setProgress(75);

				mCommand.execute();
				
				setProgress(100);
			} catch (MatlabInvocationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return null;
		}
	};
	
	public void disconnect() {
		if (mProxy != null) {
			mProxy.disconnect();
		}
	}

	/**
	 * Helper interface
	 */
	private interface MatlabCommand {
		abstract void execute() throws MatlabInvocationException;
	}
	
	/**
	 * Helper class
	 */
	private class SetVariableDetails {
		private final String mVariable;
		private final Object mValue;
		
		private SetVariableDetails(String variable, Object value) {
			mVariable = variable;
			mValue = value;
		}
	}
}
