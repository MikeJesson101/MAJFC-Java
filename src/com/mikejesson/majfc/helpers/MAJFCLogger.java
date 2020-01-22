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

package com.mikejesson.majfc.helpers;

/**
 * @author MAJ727
 *
 */
public class MAJFCLogger {
	private static int sSeverityLimit = 0;
	private static boolean sLogTiming = false;
	
	/**
	 * Sets the limit of severity over which messages will be logged
	 * @param severityLimit The severity limit. Messages logged at less than this severity will not be logged.
	 */
	public static void setSeverityLimit(int severityLimit) {
		sSeverityLimit = severityLimit;
	}
	
	/**
	 * Sets whether or not to log timing messages
	 * @param logTiming
	 */
	public static void setLogTiming(boolean logTiming) {
		sLogTiming = logTiming;
	}
	
	/**
	 * Logs a message
	 * @param text The message to log
	 */
	public static void log(String text){
		log(text, Integer.MAX_VALUE);
	}
	
	/**
	 * Logs a message if the severity is greater than that specified
	 * @param text
	 * @param severity
	 */
	public static void log(String text, int severity){
		if (severity < sSeverityLimit) {
			return;
		}
		
		System.out.println(text);
	}
	
	/**
	 * Logs a timing message (but only if the logger is set to do so)
	 * @see MAJFCLogger#sLogTiming
	 * @see MAJFCLogger#setLogTiming(boolean)
	 * @param message The message to log - the time will be appended to message
	 */
	public static void logTiming(String message) {
		if (sLogTiming) {
			log(message + ": " + System.currentTimeMillis());
		}
	}
	
	/**
	 * Constructor - hidden
	 */
	private MAJFCLogger() {
	}

}
