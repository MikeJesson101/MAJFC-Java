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

import java.util.Vector;

/**
 * @author mikefedora
 *
 */
public abstract class MAJFCSafeArrayWithKeys<ObjectType extends Object, IndexType extends MAJFCSafeArray.MAJFCSafeArrayIndex> extends MAJFCSafeArray<ObjectType, IndexType> {
	private Vector<IndexType> mKeys = new Vector<IndexType>();
	
	/**
	 * 
	 */
	public MAJFCSafeArrayWithKeys(ObjectType[] objects) {
		super(objects);
		
		for (int i = 0; i < objects.length; ++i) {
			mKeys.add(makeKey(i));
		}
	}
	
	/**
	 * Makes the key for the specified index
	 * @param index
	 * @return A key object
	 */
	protected abstract IndexType makeKey(int index);
	
	/**
	 * Gets a list of the keys for this summary (note, this will only return keys for values which
	 * have been set using set(...) 
	 */
	public Vector<IndexType> getKeys() {
		return mKeys;
	}

	/**
	 * Copies all elements into the given MAJFCSafeArray
	 * @param receiver The array to copy into
	 * @return The array copied into
	 */
	public void copyInto(MAJFCSafeArrayWithKeys<ObjectType, IndexType> receiver) {
		super.copyInto(receiver);

		receiver.mKeys = getKeys();
	}
}
