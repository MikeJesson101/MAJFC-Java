/**
 * 
 */
package com.mikejesson.majfc.helpers;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;

/**
 * @author MAJ727
 *
 */
public class MAJFCSorted3DList implements Comparator<MAJFCSorted3DList.Comparable3DValuesHolder> {
	private final Vector<Comparable3DValuesHolder> mVelocities;

	public MAJFCSorted3DList(List<Double> uVelocities, List<Double> vVelocities, List<Double> wVelocities) throws Exception {
		int numberOfVelocities = uVelocities.size();
		
		if (vVelocities.size() != numberOfVelocities || wVelocities.size() != numberOfVelocities) {
			throw new Exception("SortedVelocitiesHolder constructor - velocities size mismatch");
		}
		
		mVelocities = new Vector<Comparable3DValuesHolder>(numberOfVelocities);
		for (int i = 0; i < numberOfVelocities; ++i) {
			mVelocities.add(new Comparable3DValuesHolder(uVelocities.get(i), vVelocities.get(i), wVelocities.get(i)));
		}
		
		Collections.sort(mVelocities, this);
	}
	
	public int size() {
		return mVelocities.size();
	}
	
	public double getU(int item) {
		return mVelocities.get(item).mU;
	}

	public double getV(int item) {
		return mVelocities.get(item).mV;
	}
	
	public double getW(int item) {
		return mVelocities.get(item).mW;
	}
	
	@Override
	public int compare(Comparable3DValuesHolder first, Comparable3DValuesHolder second) {
		return Double.compare(first.mU, second.mU);
	}
	
	protected class Comparable3DValuesHolder {
		private final Double mU;
		private final Double mV;
		private final Double mW;
		
		private Comparable3DValuesHolder(double u, double v, double w) {
			mU = u;
			mV = v;
			mW = w;
		}
	}
}
