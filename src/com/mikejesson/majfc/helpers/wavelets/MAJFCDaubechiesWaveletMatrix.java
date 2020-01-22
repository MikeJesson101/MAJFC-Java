package com.mikejesson.majfc.helpers.wavelets;

import java.util.Vector;

import com.mikejesson.majfc.helpers.MAJFCMaths;

import Jama.Matrix;

public class MAJFCDaubechiesWaveletMatrix {
	private final double[] mLittleLs;
	private double[] mLittleHs;
	private Vector<Matrix> mHMatrices = new Vector<Matrix>(20); 
	private Vector<Matrix> mLMatrices = new Vector<Matrix>(20);
	
	private static double[][] scales = {
		{1,				1},																		
		{(1d + Math.sqrt(3d))/4, (3d + Math.sqrt(3d))/4,	(3d - Math.sqrt(3d))/4,	(1d - Math.sqrt(3d))/4},
		{0.47046721,	1.14111692,	0.650365,	-0.19093442,	-0.12083221,	0.0498175},	
		{0.32580343,	1.01094572,	0.8922014,	-0.03957503,	-0.26450717,	0.0436163,		0.0465036,		-0.01498699},	
		{0.22641898,	0.85394354,	1.02432694,	0.19576696,		-0.34265671,	-0.04560113,	0.10970265,		-0.0088268,	-0.01779187,	0.00471742793},	
		{0.15774243,	0.69950381,	1.06226376,	0.44583132,		-0.3199866,		-0.18351806,	0.13788809,		0.03892321,	-0.04466375,	7.83E-04,		6.76E-03,		-1.52E-03},	
		{0.11009943,	0.56079128,	1.03114849,	0.66437248,		-0.20351382,	-0.31683501,	0.1008467,		0.11400345,	-0.05378245,	-0.02343994,	0.01774979,		6.08E-04,	-2.55E-03,	5.00E-04},	
		{0.07695562,	0.44246725,	0.95548615,	0.82781653,		-0.02238574,	-0.40165863,	6.68E-04,		0.18207636,	-0.0245639,		-0.06235021,	0.01977216,		0.01236884,	-6.89E-03,	-5.54E-04,	9.55E-04,	-1.66E-04},	
		{0.05385035,	0.3448343,	0.85534906,	0.92954571,		0.18836955,		-0.41475176,	-0.13695355,	0.21006834,	0.043452675,	-0.09564726,	3.55E-04,		0.03162417,	-6.68E-03,	-6.05E-03,	2.61E-03,	3.26E-04,	-3.56E-04,	5.56E-05},	
		{0.037717157593,	0.266122182794,	0.74557507,	0.97362811,		0.39763774,		-0.3533362,		-0.27710988,	0.18012745,	0.13160299,		-0.10096657,	-0.04165925,	0.04696981,	5.10E-03,	-0.015179,	1.97E-03,	2.82E-03,	-9.70E-04,	-1.65E-04,	1.32E-04,	-1.88E-05}
	};

	public MAJFCDaubechiesWaveletMatrix(int numberOfScales) {
		mLittleLs = new double[numberOfScales];
		mLittleHs = new double[numberOfScales];

//		double sqrt2 = Math.sqrt(2d);
		for (int i = 0; i < numberOfScales; ++i) {
			// Normalise to square root 2 for orthonormality
			mLittleLs[i] = scales[numberOfScales/2 - 1][i];///sqrt2;
			mLittleHs[numberOfScales - 1 - i] = i % 2 == 1 ? -1 * mLittleLs[i] : mLittleLs[i];
		
			System.out.println(i + ": scale: " + mLittleLs[i] + " coeffs[" + (numberOfScales - 1 - i) + "]: " + mLittleHs[numberOfScales - 1 - i]);
		}
	}
	
	public Vector<Double> transform(Vector<Double> samples) {
		MAJFCMaths.reduceToPowerOfTwoSize(samples);
		int numberOfSamples = samples.size();
		int powerOfTwo = MAJFCMaths.nearestPowerOfTwo(numberOfSamples, false);
		
		setupMatrices(powerOfTwo);
		
		double[] theSamples = new double[numberOfSamples];
		
		for (int i = 0; i < numberOfSamples; ++i) {
			theSamples[i] = samples.elementAt(i);
		}

		Matrix hilbert = new Matrix(theSamples, numberOfSamples);
		
		for (int matrixIndex = powerOfTwo - 1; matrixIndex > -1; --matrixIndex) {
			Matrix H = mHMatrices.elementAt(matrixIndex);
			Matrix L = mLMatrices.elementAt(matrixIndex);
			Matrix hilbertSection = hilbert.getMatrix(0, (int) Math.pow(2,  matrixIndex + 1) - 1, 0, 0);
			
			Matrix averaging = L.times(hilbertSection).times(0.5);
			Matrix detail = H.times(hilbertSection).times(0.5);
			
			int halfLength = (int) Math.pow(2,  matrixIndex);
			for (int hilbertIndex = 0; hilbertIndex < halfLength; ++hilbertIndex) {
				hilbert.set(hilbertIndex, 0, averaging.get(hilbertIndex, 0));
				hilbert.set(hilbertIndex + halfLength, 0, detail.get(hilbertIndex, 0));
			}
		}
		
		Vector<Double> hilbertV = new Vector<Double>(numberOfSamples);
		for (int i = 0; i < numberOfSamples; ++i) {
			hilbertV.add(hilbert.get(i, 0));
		}
		
		return hilbertV;
	}
	
	public Vector<Double> inverseTransform(Vector<Double> hilberts) {
		MAJFCMaths.reduceToPowerOfTwoSize(hilberts);
		int numberOfSamples = hilberts.size();
		int powerOfTwo = MAJFCMaths.nearestPowerOfTwo(numberOfSamples, false);
		
		setupMatrices(powerOfTwo);
		
		double[] theHilberts = new double[numberOfSamples];
		
		for (int i = 0; i < numberOfSamples; ++i) {
			theHilberts[i] = hilberts.elementAt(i);
		}

		Matrix theHilbertsM = new Matrix(theHilberts, numberOfSamples);
		Matrix reconstruction = new Matrix(1, 1, hilberts.firstElement());
		
		for (int matrixIndex = 0; matrixIndex < powerOfTwo; ++matrixIndex) {
			Matrix H = mHMatrices.elementAt(matrixIndex);
			Matrix L = mLMatrices.elementAt(matrixIndex);
			int detailsSamplesEnd = (int) Math.pow(2, matrixIndex + 1) - 1;
			int detailsSamplesStart = (int) Math.floor(detailsSamplesEnd/2) + 1;
			Matrix detailSection = theHilbertsM.getMatrix(detailsSamplesStart, detailsSamplesEnd, 0, 0);
			
			reconstruction = L.transpose().times(reconstruction).plus(H.transpose().times(detailSection));
		}
		
		Vector<Double> reconstructionV = new Vector<Double>(numberOfSamples);
		for (int i = 0; i < numberOfSamples; ++i) {
			reconstructionV.add(reconstruction.get(i, 0));
		}
		
		return reconstructionV;
	}
	
	private void setupMatrices(int numberOfLevels) {
		while (mHMatrices.size() < numberOfLevels) {
			int level = mHMatrices.size() + 1;
			int numberOfRows = (int) Math.pow(2,level - 1);
			int numberOfCols = (int) Math.pow(2,level);
			Matrix H = new Matrix(numberOfRows, numberOfCols);
			Matrix L = new Matrix(numberOfRows, numberOfCols);
			
			for (int littleLOrHIndex = 0; littleLOrHIndex < mLittleLs.length; ++littleLOrHIndex) {
				double littleL = mLittleLs[littleLOrHIndex];
				double littleH = mLittleHs[littleLOrHIndex];
				
				for (int rowIndex = 0; rowIndex < numberOfRows; ++rowIndex) {
					int colIndex = (2 * rowIndex) + littleLOrHIndex;
					
					while (colIndex >= numberOfCols) {
						colIndex -= numberOfCols;
					}
					
					H.set(rowIndex, colIndex, H.get(rowIndex, colIndex) + littleH);
					L.set(rowIndex, colIndex, L.get(rowIndex, colIndex) + littleL);
				}
			}
			
			mHMatrices.add(H);//.times(0.5));
			mLMatrices.add(L);//.times(0.5));
		}
	}
}
