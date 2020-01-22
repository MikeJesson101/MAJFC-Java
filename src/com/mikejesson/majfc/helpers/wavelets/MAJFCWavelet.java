package com.mikejesson.majfc.helpers.wavelets;

import java.math.BigDecimal;
import java.util.Vector;

import com.mikejesson.majfc.helpers.MAJFCMaths;

public abstract class MAJFCWavelet {
	private final int mNumberOfScales;
	private final BigDecimal[] mLittleLs;
	private final BigDecimal[] mLittleHs;
	
	protected final static double sqrt2 = Math.sqrt(2);
	protected final static double sqrt15 = Math.sqrt(15);
	
	protected MAJFCWavelet(int numberOfScales) {
		mNumberOfScales = numberOfScales;
		mLittleLs = new BigDecimal[mNumberOfScales];
		mLittleHs = new BigDecimal[mNumberOfScales];

		double scales[] = getScales(mNumberOfScales);
		
		for (int i = 0; i < mNumberOfScales; ++i) {
			// Normalise to square root 2 for orthonormality
			mLittleLs[i] = new BigDecimal(scales[i]);
			mLittleHs[mNumberOfScales - 1 - i] = new BigDecimal(i % 2 == 1 ? -1d * mLittleLs[i].doubleValue() : mLittleLs[i].doubleValue());
		}
	}
	
	protected abstract double[] getScales(int numberOfScales);
	
	public Vector<Double> transform(final Vector<Double> samples) {
		@SuppressWarnings("unchecked")
		Vector<Double> hilbert = (Vector<Double>) samples.clone();

		MAJFCMaths.reduceToPowerOfTwoSize(hilbert, MAJFCMaths.STRIP_FROM_EACH_END);
		int numberOfSamples = hilbert.size();
		int powerOfTwo = MAJFCMaths.nearestPowerOfTwo(numberOfSamples, false);
		
		for (int matrixIndex = powerOfTwo; matrixIndex > 0; --matrixIndex) {
			// Get the samples to multiply by (virtual) L and H matrices. To incorporate wrap-around, if the number of scales exceeds the number of
			// columns in the matrix, wrap the samples to mimic an l(n) + l(n + numborOfColums) entry.
			int numberOfColumns = (int) Math.pow(2, matrixIndex);
			int numberOfRows = (int) Math.pow(2, matrixIndex - 1);

			int numberOfSectionSamples = Math.max(numberOfColumns, mNumberOfScales);
			BigDecimal[] samplesSection = new BigDecimal[numberOfSectionSamples];
			for (int sampleIndex = 0; sampleIndex < numberOfSectionSamples; ++sampleIndex) {
				samplesSection[sampleIndex] = new BigDecimal(hilbert.elementAt(sampleIndex));
			}

			for (int rowIndex = 0; rowIndex < numberOfRows; ++rowIndex) {
				BigDecimal averageValue = new BigDecimal(0);
				BigDecimal detailValue = new BigDecimal(0);
				
				// Multiplication of this row by the column vector of samples
				for (int i = 0; i < mNumberOfScales; ++i) {
					int wrappedSampleIndex = i + 2 * rowIndex;
					
					while (wrappedSampleIndex >= numberOfColumns) {
						wrappedSampleIndex -= numberOfColumns;
					}
					
					averageValue = averageValue.add(mLittleLs[i].multiply(samplesSection[wrappedSampleIndex]));
					detailValue = detailValue.add(mLittleHs[i].multiply(samplesSection[wrappedSampleIndex]));
				}
				
				hilbert.set(rowIndex, averageValue.doubleValue()/2d);
				hilbert.set(rowIndex + numberOfColumns/2, detailValue.doubleValue()/2d);
			}
		}

		return hilbert;
	}
	
	public Vector<Double> inverseTransform(Vector<Double> hilbert) {
		@SuppressWarnings("unchecked")
		Vector<Double> reconstruction = (Vector<Double>) hilbert.clone();

		MAJFCMaths.reduceToPowerOfTwoSize(reconstruction);
		int numberOfSamples = reconstruction.size();
		int powerOfTwo = MAJFCMaths.nearestPowerOfTwo(numberOfSamples, false);
		
		for (int matrixIndex = 1; matrixIndex <= powerOfTwo; ++matrixIndex) {
			int numberOfRows = (int) Math.pow(2, matrixIndex);
			int numberOfColumns = (int) Math.pow(2, matrixIndex - 1);

			int detailsSamplesEnd = (int) Math.pow(2, matrixIndex) - 1;
			int detailsSamplesStart = (int) Math.floor(detailsSamplesEnd/2) + 1;
			BigDecimal[] reconstructionSection = new BigDecimal[numberOfColumns];
			BigDecimal[] detailsSection = new BigDecimal[numberOfColumns];
			for (int detailsOffset = 0; detailsOffset < numberOfColumns; ++detailsOffset) {
				reconstructionSection[detailsOffset] = new BigDecimal(reconstruction.elementAt(detailsOffset));
				detailsSection[detailsOffset] = new BigDecimal(hilbert.elementAt(detailsSamplesStart + detailsOffset));
			}
			
			// Matrix multiply using the transpose of H and L
			for (int rowIndex = 0; rowIndex < numberOfRows; ++rowIndex) {
				BigDecimal reconstructionValue = new BigDecimal(0);
				
				// Multiplication of this row by the column vector of samples
				for (int columnIndex = 0; columnIndex < numberOfColumns; ++columnIndex) {
					int lOrHIndex = rowIndex - 2 * columnIndex;
					
					while (lOrHIndex < 0) {
						lOrHIndex += numberOfRows;
					}
					
					if (lOrHIndex >= mNumberOfScales) {
						continue;
					}
					
					BigDecimal littleLSum = mLittleLs[lOrHIndex];
					BigDecimal littleHSum= mLittleHs[lOrHIndex];
					while ((lOrHIndex = lOrHIndex + numberOfRows) < mNumberOfScales) {
						littleLSum = littleLSum.add(mLittleLs[lOrHIndex]);
						littleHSum = littleHSum.add(mLittleHs[lOrHIndex]);
					}
					
					reconstructionValue = reconstructionValue.add(littleLSum.multiply(reconstructionSection[columnIndex]));
					reconstructionValue = reconstructionValue.add(littleHSum.multiply(detailsSection[columnIndex]));
				}
				
				reconstruction.set(rowIndex, reconstructionValue.doubleValue());
			}
		}
		
		return reconstruction;
	}
}
