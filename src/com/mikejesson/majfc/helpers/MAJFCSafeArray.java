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
 * @author mikefedora
 *
 */
public class MAJFCSafeArray<ObjectType extends Object, IndexType extends MAJFCSafeArray.MAJFCSafeArrayIndex> {
	protected ObjectType[] mObjects;
		
	/**
	 * 
	 */
	public MAJFCSafeArray(ObjectType[] objects) {
		mObjects = objects;
	}
	
	/**
	 * Gets the array element specified
	 * @param index The element index
	 * @return The object at the specified index
	 */
	public ObjectType get(IndexType index) {
		return mObjects[index.getIntIndex()];
	}
	
	/**
	 * Sets the array element specified
	 * @param index The element index
	 * @param object The object to set it to
	 */
	public void set(IndexType index, ObjectType object) {
		mObjects[index.getIntIndex()] = object;
	}

	/**
	 * Copies all elements into the given MAJFCSafeArray
	 * @param receiver The array to copy into
	 * @return The array copied into
	 */
	public void copyInto(MAJFCSafeArray<ObjectType, IndexType> receiver) {
		for (int i = 0; i < mObjects.length; ++i) {
			receiver.mObjects[i] = mObjects[i];
		}
	}
	
	/**
	 * Abstract class which objects used to index the safe array must be instantiations of
	 * @author mikefedora
	 *
	 */
	public static abstract class MAJFCSafeArrayIndex implements Comparable<MAJFCSafeArrayIndex> {
		private final int mIndex;

		public MAJFCSafeArrayIndex(int index) {
			mIndex = index;
		}

		public final int getIntIndex() {
			return mIndex;
		}

		public String toString() {
			return Integer.toString(mIndex);
		}
		
		@Override
		/**
		 * Equality check
		 * @param theOtherOne The index to compare to this one
		 * @return True if the index is the same
		 */
		public boolean equals(Object theOtherOne) {
			if (this == theOtherOne) {
				return true;
			}
			
			if (theOtherOne == null) {
				return false;
			}
			
			if (this.getClass() != theOtherOne.getClass()) {
				return false;
			}
			
			return ((MAJFCSafeArrayIndex) theOtherOne).mIndex == mIndex;
		}
		
		@Override
		public int compareTo(MAJFCSafeArrayIndex theOtherOne) {
			if (theOtherOne == null) {
				throw new NullPointerException();
			}
			
			return mIndex - theOtherOne.mIndex;
		}
		
		@Override
		public int hashCode() {
			int hash = 15 * mIndex;
			
			return hash;
		}
	}
}
