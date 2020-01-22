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

/**
 * Helper class
 * @author Mike
 *
 */
public abstract class MAJFCIndex {
	private final int mIndex;
		
	public MAJFCIndex(int termId) {
		mIndex = termId;
	}
		
	public boolean equals(MAJFCIndex theOtherOne) {
		return mIndex == theOtherOne.mIndex;
	}
}