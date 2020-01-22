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

package com.mikejesson.majfc.helpers;

import java.awt.GridBagConstraints;
import java.awt.Insets;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;

import java.util.StringTokenizer;

/**
 * @author mikefedora
 *
 */
public class MAJFCTools {
	// Constant, non-user configurable stuff
	public static final String SYSTEM_NEW_LINE_STRING = System.getProperty("line.separator");
	public static final char SYSTEM_TAB_CHAR = '\t';
	public static final String SYSTEM_FILE_PATH_SEPARATOR = System.getProperty("file.separator");
	public static final String SYSTEM_USER_HOME_DIR = System.getProperty("user.home");
	public static final String SYSTEM_OS = System.getProperty("os.name");
	public static final char SYSTEM_CARRIAGE_RETURN_CHAR = '\r';
	public static final char SYSTEM_NEW_LINE_CHAR = '\n';

	private static final int STRING_SUBSTITUTION_CONTROL_CHAR_INDEX_FOR_PERCENT_SYMBOL = -1;
	public static final String STRING_SUBSTITUTION_PERCENT_SYMBOL = "%" + STRING_SUBSTITUTION_CONTROL_CHAR_INDEX_FOR_PERCENT_SYMBOL + "%";
	
	/**
	 * Creates GridBagConstraint (just gives useful info when adding the parameters)
	 * 
	 * @return a GridBagConstraint with the given parameters
	 */
	public static GridBagConstraints createGridBagConstraint(	int xPos, int yPos,
																int gridWidth, int gridHeight,
																double xWeight, double yWeight,
																int anchor, int fill,
																int internalTopPadding,
																int internaLeftPadding,
																int internaBottomPadding,
																int internalRightPadding,
																int externalXPadding, int externalYPadding){
		return new GridBagConstraints(	xPos, yPos,
										gridWidth, gridHeight,
										xWeight, yWeight,
										anchor, fill,
										new Insets(internalTopPadding, internaLeftPadding, internaBottomPadding, internalRightPadding),
										externalXPadding, externalYPadding);
	}

	/**
	 * Returns a nicely formatted number
	 * @param number The number to format
	 * @param decimal places The number of decimal places to use
	 * then the number is terminated after the last non-zero decimal place  
	 * @return number formatted to the specified number of decimal places
	 */
	public static String formatNumber(double number, int decimalPlaces) {
		return formatNumber(number, decimalPlaces, false, false);
	}
	
	/**
	 * Returns a nicely formatted number
	 * @param number The number to format
	 * @param decimal places The number of decimal places to use
	 * @param includeTrailingZeroes If true then the full number of decimal places is shown, filling with zeroes if necessary. If false
	 * then the number is terminated after the last non-zero decimal place  
	 * @return number formatted to the specified number of decimal places
	 */
	public static String formatNumber(double number, int decimalPlaces, boolean includeTrailingZeroes) {
		return formatNumber(number, decimalPlaces, includeTrailingZeroes, false);
	}
	
	/**
	 * Returns a nicely formatted number
	 * @param number The number to format
	 * @param decimal places The number of decimal places to use
	 * @param includeTrailingZeroes If true then the full number of decimal places is shown, filling with zeroes if necessary. If false
	 * then the number is terminated after the last non-zero decimal place  
	 * @param alwaysScientific If true then the number is always expressed as an exponential
	 * @return number formatted to the specified number of decimal places
	 */
	public static String formatNumber(double number, int decimalPlaces, boolean includeTrailingZeroes, boolean alwaysScientific) {
		if (Double.isNaN(number)) {
			return null;
		}
		
		int additionalPowersOfTen = 0;
		if (number != 0 && (alwaysScientific || Math.abs(number) < Math.pow(10, -decimalPlaces))) {
			while (Math.abs(number) < 1) {
				number *= 10;
				--additionalPowersOfTen;
			}
		}
		
		StringTokenizer st = new StringTokenizer(MAJFCTools.stringValueOf(number), "E");
		String powerOfTenString = "";
		
		if (st.countTokens() == 2) {
			number = Integer.valueOf(st.nextToken());
			int powerOfTen = Integer.valueOf(st.nextToken()) + additionalPowersOfTen;
			powerOfTenString = 'E' + Integer.toString(powerOfTen);
		} else if (additionalPowersOfTen < 0) {
			powerOfTenString = 'E' + Integer.toString(additionalPowersOfTen);
		}
		
		DecimalFormat format = makeDecimalFormatter(decimalPlaces, includeTrailingZeroes);
		String formattedNumber = format.format(number);

		if (includeTrailingZeroes == false && formattedNumber.endsWith(format.getDecimalFormatSymbols().getDecimalSeparator() + " ")) {
			formattedNumber = formattedNumber.substring(0, formattedNumber.indexOf('.'));
		}
		
		return formattedNumber + powerOfTenString; 
	}
	
	/**
	 * Returns a decimal formatter for the required number of decimal places
	 * @param decimalPlaces The number of decimal places to use
	 * @param includeTrailingZeroes If true then the full number of decimal places is shown, filling with zeroes if necessary. If false
	 * then the number is terminated after the last non-zero decimal place  
	 * @return A formatter for the specified number of decimal places
	 */
	public static DecimalFormat makeDecimalFormatter(int decimalPlaces, boolean includeTrailingZeroes) {
		StringBuffer pattern = new StringBuffer("0");
		
		if (decimalPlaces > 0) {
			pattern.append(".0");
		}
		
		for (int i = 1; i < decimalPlaces; ++i) {
			pattern.append(includeTrailingZeroes ? '0' : '#');
		}
		
		return new DecimalFormat(pattern.toString());
	}

	/**
	 * Parses a string to an int and (hopefully) handles locale
	 * @param theString
	 * @return theString as an int 
	 * @throws ParseException 
	 */
	public static int parseInt(String theString) throws ParseException {
		return NumberFormat.getInstance().parse(theString).intValue();
	}
	
	/**
	 * Parses a string to a double and (hopefully) handles locale
	 * @param theString
	 * @return theString as a double 
	 */
	public static double parseDouble(String theString) {
		return parseDouble(theString, NumberFormat.getInstance());
	}
	
	/**
	 * Parses a string to a double and (hopefully) handles locale
	 * @param theString
	 * @param decimalSeparator The decimal separator to use. Overrides locale setting.
	 * @return theString as a double 
	 */
	public static double parseDouble(String theString, char decimalSeparator) {
		DecimalFormatSymbols decFormSymbols = DecimalFormatSymbols.getInstance();
		decFormSymbols.setDecimalSeparator(decimalSeparator);
		
		DecimalFormat decFormat = ((DecimalFormat) DecimalFormat.getInstance());
		decFormat.setDecimalFormatSymbols(decFormSymbols);
		
		return parseDouble(theString, decFormat);
	}
	
	/**
	 * Parses a string to a double and (hopefully) handles locale
	 * @param theString
	 * @param decimalSeparator The decimal separator to use. Overrides locale setting.
	 * @return theString as a double 
	 */
	private static double parseDouble(String theString, NumberFormat numFormat) {
		try {
			return numFormat.parse(theString.trim()).doubleValue();
		} catch (ParseException e) {
			return Double.NaN;
		}
	}
	
	public static String stringValueOf(Object theObject) {
		if (theObject instanceof String ) {
			return (String) theObject;
		} else if (theObject instanceof Number) {
			return stringValueOf((Number) theObject);
		} else if (theObject instanceof Character) {
			return stringValueOf(((Character) theObject).charValue());
		} else {
			return "Unrecognised Object in MAJFCTools.stringValueOf";
		}
	}
	
	public static String stringValueOf(Number theNumber) {
		if (theNumber.equals(Double.NaN)) {
			return String.valueOf(theNumber);
		}
		
		// Create a locale independent version ('.' as decimal separator)
		String theNumberAsString = String.valueOf(theNumber);
		
		int decimalPlaces = theNumberAsString.length() - theNumberAsString.indexOf('.') - 1;
		return makeDecimalFormatter(decimalPlaces, false).format(theNumber);
	}
	
	public static String stringValueOf(char theChar) {
		return String.valueOf(theChar);
	}
	/**
	 * Makes an XML node end tag
	 * @param tag The node tag
	 * @param startTagClosed True if the start tag which this closes is closed
	 * @return The XML node start tag
	 */
	public static String makeXMLEndTag(String tag, boolean startTagClosed) {
		StringBuffer endTag = new StringBuffer();
		
		if (startTagClosed) {
			endTag.append("</");
			endTag.append(tag);
			endTag.append('>');
		} else {
			endTag.append("/>");
		}
		
		endTag.append(SYSTEM_NEW_LINE_STRING);
	
		return endTag.toString();
	}

	/**
	 * Makes an XML node start tag
	 * @param tag The node tag
	 * @param close True if this should be closed with a '>'
	 * @return The XML node start tag
	 */
	public static String makeXMLStartTag(String tag, boolean close) {
		StringBuffer startTag = new StringBuffer();
		
		startTag.append('<');
		startTag.append(tag);
		
		if (close) {
			startTag.append('>');
			startTag.append(SYSTEM_NEW_LINE_STRING);
		} else {
			startTag.append(' ');
		}
	
		return startTag.toString();
	}

	/**
	 * Makes an XML internal attribute from a tag and value
	 * @param tag The attribute tag
	 * @param value The attribute value
	 * @return The XML internal attribute string
	 */
	public static String makeXMLInternalAttribute(String tag, Number value) {
		StringBuffer theAttribute = new StringBuffer();
		
		theAttribute.append(tag);
		theAttribute.append("=\"");
		theAttribute.append(MAJFCTools.stringValueOf(value));
		theAttribute.append("\" ");
		
		return theAttribute.toString();
	}

	/**
	 * Makes an XML internal attribute from a tag and value
	 * @param tag The attribute tag
	 * @param value The attribute value
	 * @return The XML internal attribute string
	 */
	public static String makeXMLInternalAttribute(String tag, String value) {
		StringBuffer theAttribute = new StringBuffer();
		
		theAttribute.append(tag);
		theAttribute.append("=\"");
		theAttribute.append(value);
		theAttribute.append("\" ");
		
		return theAttribute.toString();
	}

	/**
	 * Makes an XML node from the node name and value
	 * @param name The node name
	 * @param value The value
	 * @return The XML node string
	 */
	public static String makeXMLNode(String name, Object value) {
		StringBuffer node = new StringBuffer();
		
		node.append(SYSTEM_TAB_CHAR);
		node.append(makeXMLStartTag(name, true));
		node.append(SYSTEM_TAB_CHAR);
		node.append(SYSTEM_TAB_CHAR);
		node.append(MAJFCTools.stringValueOf(value));
		node.append(SYSTEM_NEW_LINE_STRING);
		node.append(SYSTEM_TAB_CHAR);
		node.append(makeXMLEndTag(name, true));
		
		return node.toString();
	}
	
	/**
	 * Copies a directory into another one, wiping the previous contents of the new directory
	 * @param oldDir The directory to copy
	 * @param newDir The directory to copy into
	 * @param copySubDirectories If true, sub directories are copied
	 * @param wipeNewDir If true, the new directory is wiped clean (if it already exists). If false, the new files are copied into
	 * the new directory, though duplicate files are overwritten with the new version
	 */
	public static void copyDirectory(File oldDir, File newDir, boolean wipeNewDir, boolean copySubDirs) {
		if (newDir.exists() == false) {
			newDir.mkdirs();
		} else if (wipeNewDir) {
			File[] newFiles = newDir.listFiles();
			
			// Delete all files from the new directory
			for (int i = 0; i < newFiles.length && newFiles != null; ++i) {
				newFiles[i].delete();
			}
		}
		
		File[] oldFiles = oldDir.listFiles();
		
		if (oldFiles == null) {
			return;
		}
		
		for (int i = 0; i < oldFiles.length; ++i) {
			if (oldFiles[i].isDirectory()) {
				if (copySubDirs) {
					File newSubDir = new File(newDir.getAbsolutePath() + MAJFCTools.SYSTEM_FILE_PATH_SEPARATOR + oldFiles[i].getName());
					copyDirectory(oldFiles[i], newSubDir, wipeNewDir, copySubDirs);
				}
			} else {
				try {
					copyFile(oldFiles[i], new File(newDir.getAbsolutePath() + MAJFCTools.SYSTEM_FILE_PATH_SEPARATOR + oldFiles[i].getName()));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * Deletes a directory, all its files and all sub-directories
	 * @param dir The directory to delete
	 */
	public static void deleteDirectoryTree(File dir) {
		if (dir.exists() == false) {
			return;
		}
		
		File[] newFiles = dir.listFiles();
			
		// Delete all files from the new directory
		for (int i = 0; i < newFiles.length && newFiles != null; ++i) {
			if (newFiles[i].isDirectory()) {
				deleteDirectoryTree(newFiles[i]);
			} else {
				newFiles[i].delete();
			}
		}
		
		dir.delete();
	}
	
	/**
	 * Makes an unsigned long from the array of bytes passed in.
	 * First byte in the array is the LSByte, last byte in the array is MSByte.
	 * @param bytes The bytes to translate
	 * @return The signed long value represented by the bytes
	 * @throws Exception If the sizeInBytes is greater than the system Long size
	 */
	private static long makeUnsignedLongFromBytes(byte[] bytes) throws Exception {
		if (Long.SIZE < (bytes.length * 8)) {
			throw new MAJFCToolsException("Max. Size of a Long Exceeded");
		}
 
		long number = 0;
		
		for (int i = 0; i < bytes.length; ++i) {
			int byteMask = 0x00FF;
			int shift = 8 * i;
			number = number | ((bytes[i] << shift) & (byteMask << shift));
		}

		return number;
	}
	
	/**
	 * Makes a signed long from the array of bytes passed in.
	 * First byte in the array is the LSByte, last byte in the array is MSByte. The read number is
	 * assumed to be in two's-complement form, with the MSB of the MSByte being the "sign" bit
	 * @param bytes The bytes to translate
	 * @return The signed long value represented by the bytes
	 * @throws Exception If the sizeInBytes is greater than the system Long size
	 */
	public static long makeSignedLongFromBytes(byte[] bytes) throws Exception {
		long number = makeUnsignedLongFromBytes(bytes);
		
		int bytesInALong = Long.SIZE/8;
		
		// If the bytes fill the whole number don't worry about signing.
		if (bytesInALong == bytes.length) {
			return number;
		}

		if ((number & (0x80 << (8 * (bytes.length - 1)))) == 0) {
			// Positive
			return number;
		} else {
			// Negative
			return number | (0xFFFFFFFF << (8 * bytes.length));
		}
	}

	/**
	 * Makes a signed int from the array of bytes passed in.
	 * First byte in the array is the LSByte, last byte in the array is MSByte. The read number is
	 * assumed to be in two's-complement form, with the MSB of the MSByte being the "sign" bit
	 * @param bytes The bytes to translate
	 * @return The signed long value represented by the bytes
	 * @throws Exception If the sizeInBytes is greater than the system Long size
	 */
	public static int makeSignedIntFromBytes(byte[] bytes) throws Exception {
		long number = makeUnsignedLongFromBytes(bytes);

		return (int) ((number & (0x80 << (8 * (bytes.length - 1)))) > 0 ? number | (0xFFFFFFFF << (8 * bytes.length)) : number);
	}

	/**
	 * Makes an unsigned int from the array of bytes passed in.
	 * First byte in the array is the LSByte, last byte in the array is MSByte. The read number is
	 * assumed to be in two's-complement form, with the MSB of the MSByte being the "sign" bit
	 * @param bytes The bytes to translate
	 * @return The signed long value represented by the bytes
	 * @throws Exception If the sizeInBytes is greater than the system Long size
	 */
	public static int makeUnsignedIntFromBytes(byte[] bytes) throws Exception {
		return (int) makeUnsignedLongFromBytes(bytes);
	}
	
	/**
	 * Reads specified number of bytes from the specified input stream
	 * @param bufferedInputStream The stream to read from
	 * @param sizeInBytes The number of bytes to read
	 * @return An array of size sizeInBytes containing the read bytes in the order they were read
	 * @throws Exception If the read fails
	 */
	public static byte[] readBytesFromFile(BufferedInputStream bufferedInputStream, int sizeInBytes) throws Exception {
		byte[] bytes = new byte[sizeInBytes];

		int bytesRead = bufferedInputStream.read(bytes);
			
		if (bytesRead != sizeInBytes) {
			throw new MAJFCToolsEoFException();
		}

		return bytes;
	}
	
	/**
	 * Reads specified number of bytes from the specified input stream, returning the unsigned
	 * integer value. First byte read is LSByte, last byte read is MSByte.
	 * @param fileInputStream The stream to read from
	 * @param sizeInBytes The number of bytes to read
	 * @return The signed integer read
	 * @throws Exception If the read fails or sizeInBytes is greater than the system Long size
	 */
	public static long readUnsignedNumberFromFile(BufferedInputStream bufferedInputStream, int sizeInBytes) throws Exception {
		byte[] bytes = readBytesFromFile(bufferedInputStream, sizeInBytes);
		return makeUnsignedLongFromBytes(bytes);
	}
	
	/**
	 * Reads specified number of bytes from the specified input stream, returning the signed
	 * integer value. First byte read is LSByte, last byte read is MSByte. The read number is
	 * assumed to be in two's-complement form, with the MSB of the MSByte being the "sign" bit
	 * @param fileInputStream The stream to read from
	 * @param sizeInBytes The number of bytes to read
	 * @return The signed integer read
	 * @throws Exception If the read fails or sizeInBytes is greater than the system Long size
	 */
	public static long readSignedNumberFromFile(BufferedInputStream bufferedInputStream, int sizeInBytes) throws Exception {
		byte[] bytes = readBytesFromFile(bufferedInputStream, sizeInBytes);
		return makeSignedLongFromBytes(bytes);
	}
	
	/**
	 * Copies a file
	 * @param sourceFile The file to copy
	 * @param destinationFile The destination file
	 * @throws IOException 
	 */
	public static void copyFile(File sourceFile, File destinationFile) throws IOException {
		final int SIZE = 8*1024; // 8k
		BufferedInputStream fileReader = new BufferedInputStream(new FileInputStream(sourceFile));
		BufferedOutputStream fileWriter = new BufferedOutputStream(new FileOutputStream(destinationFile));
		
		
		byte[] bytes = new byte[SIZE];
		int numberOfBytesRead = fileReader.read(bytes);
		while (numberOfBytesRead == SIZE) {
			fileWriter.write(bytes);
			numberOfBytesRead = fileReader.read(bytes);
		}
		
		if (numberOfBytesRead > 0) {
			fileWriter.write(bytes, 0, numberOfBytesRead);
		}
		
		fileReader.close();
		fileWriter.close();
	}
	
	/**
	 * Moves a file
	 * @param sourceFile The file to move
	 * @param destinationFile The destination file
	 * @throws IOException 
	 */
	public static void moveFile(File sourceFile, File destinationFile) throws IOException {
		copyFile(sourceFile, destinationFile);
		sourceFile.delete();
	}

	@SuppressWarnings("serial")
	/**
	 * Inner class
	 */
	public static class MAJFCToolsException extends Exception {
		MAJFCToolsException(String message) {
			super(message);
		}
	}
	
	@SuppressWarnings("serial")
	/**
	 * Inner class
	 */
	public static class MAJFCToolsEoFException extends MAJFCToolsException {
		MAJFCToolsEoFException() {
			super("End of File Reached");
		}
	}
	
	/**
	 * 
	 */
	private MAJFCTools() {
	}

	/**
	 * Substitute the given strings into the string.
	 * @param stringToInsertInto The string to insert into. Any substring of the form %<integer>% is replaced with inserts[integer]
	 * @param inserts The strings to insert into stringToInsertInto
	 * @return The altered string
	 */
	public static String substituteIntoString(String stringToInsertInto, String ...inserts) {
		StringBuffer sb = new StringBuffer();
		//String[] tokens = stringToInsertInto.split("%{1}[0-9]+%{1}");
		String[] tokens = stringToInsertInto.split("%");
		
		if (tokens.length == 1) {
			return stringToInsertInto;
		}
		
		for (int i = 0; i < tokens.length; ++i) {
			String token = tokens[i];
			int insertIndex = -1;
			
			if (i > 0 && tokens[i - 1].endsWith("\\")) {
				sb.append('%');
			}
			
			try {
				insertIndex = MAJFCTools.parseInt(token);
			} catch (Exception theException) {
				// Token is not a number
				sb.append(token);
				continue;
			}
			
			if (insertIndex > inserts.length) {
				return stringToInsertInto;
			}
			
			if (insertIndex >= 0) {
				sb.append(inserts[insertIndex]);
			} else {
				// Number < 0 is used as a control character
				switch (insertIndex) {
					case STRING_SUBSTITUTION_CONTROL_CHAR_INDEX_FOR_PERCENT_SYMBOL:
						sb.append('%');
						break;
				}
			}
		}
		
		return sb.toString(); 
	}
	
	public static void printMemory(String message) {
		Runtime rt = Runtime.getRuntime();
		System.out.println(message + ": " + '\t' + rt.freeMemory()/(1024d * 1024d) + '\t' + rt.maxMemory()/(1024d * 1024d));
	}
}
