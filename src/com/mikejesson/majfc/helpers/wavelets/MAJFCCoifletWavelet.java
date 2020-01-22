package com.mikejesson.majfc.helpers.wavelets;

public class MAJFCCoifletWavelet extends MAJFCWavelet {
	private static final double[][] scales = {
		null,
		null,
		{	sqrt2 * (sqrt15 - 3d)/32d, sqrt2 * (1d - sqrt15)/32d, sqrt2 * (6d - 2 * sqrt15)/32d, sqrt2 * (2d * sqrt15 + 6d)/32d, sqrt2 * (sqrt15 + 13d)/32d, sqrt2 * (9d - sqrt15)/32d }
	};

	public MAJFCCoifletWavelet(int numberOfScales) {
		super(numberOfScales);
	}
	
	@Override
	protected double[] getScales(int numberOfScales) {
		int halfNumberOfScales = numberOfScales/2;
		
		if (halfNumberOfScales > scales.length) {
			return null;
		}
		
		return scales[halfNumberOfScales - 1];
	}
}
