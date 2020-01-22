/**
 * 
 */
package com.mikejesson.majfc.helpers;

import java.util.StringTokenizer;
import java.util.Vector;

/**
 * @author Mike
 *
 */
public class MAJFCNumberExtractorTokenizer {
	StringTokenizer mTokenizer;
	private final boolean mUseTokenizer;
	private final String mDelimiters;
	private int mNumberOfTokens;
	private int mReadIndex;
	Vector<String> mTokens;
	
	private final char MINUS_SIGN = '-';
	
	public MAJFCNumberExtractorTokenizer(String input, String delimiters) {
		mDelimiters = delimiters;
		mUseTokenizer = mDelimiters.contains("" + MINUS_SIGN) == false;
		
		if (mUseTokenizer) {
			mTokenizer = new StringTokenizer(input, delimiters);
		} else {
			parseInput(input, delimiters);
		}
	}
	
	private void parseInput(String input, String delimiters) {
		mTokens = new Vector<String>(10);
		int inputLength = input.length();
		StringBuffer token = new StringBuffer();
		Character lastChar = null;
		
		for (int i = 0; i < inputLength; ++i) {
			char theChar = input.charAt(i);
			
			if (isDelimiter(theChar)) {
				if (lastChar != null && isDelimiter(lastChar) == false) {				// This isn't the first character and the last character wasn't a delimiter
					mTokens.add(token.toString());										// so it's a true delimiter
					token.setLength(0);
					lastChar = theChar;
					continue;
				} else if (i + 1 < inputLength && isDelimiter(input.charAt(i + 1))) {	// The next character is a delimiter as well so ignore this one
					continue;
				}
			}
			
			if (isAllowedChar(theChar)) {
				token.append(theChar);
			}
			
			lastChar = theChar;
		}
		
		mTokens.add(token.toString());
		mNumberOfTokens = mTokens.size();
	}
	
	private boolean isAllowedChar(char theChar) {
		return theChar == MINUS_SIGN || Character.isDigit(theChar);
	}

	private boolean isDelimiter(char theChar) {
		return mDelimiters.indexOf(theChar) >= 0;
	}

	public boolean hasMoreTokens() {
		if (mUseTokenizer) {
			return mTokenizer.hasMoreTokens();
		}
		
		return mReadIndex == mNumberOfTokens;
	}
	
	public String nextToken() {
		if (mUseTokenizer) {
			return mTokenizer.nextToken();
		}
		
		return mTokens.elementAt(mReadIndex++);
	}
	
	public int countTokens() {
		if (mUseTokenizer) {
			return mTokenizer.countTokens();
		}
		
		return mNumberOfTokens;
	}
}
