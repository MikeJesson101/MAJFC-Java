package com.mikejesson.majfc.helpers.wavelets;


public class MAJFCLegendreWavelet extends MAJFCWavelet {
	private static final double[][] scales = {
	    {	-1d, -1d },
		{	-5d/8d, -3d/8d, -3d/8d, -5d/8d },
		{	-63d/128d,	-35d/128d,	-30d/128d,	-30d/128d,	-35d/128d,	-63d/128d }
	};

	public MAJFCLegendreWavelet(int numberOfScales) {
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
