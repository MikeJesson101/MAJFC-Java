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


import java.util.Collections;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import com.mikejesson.majfc.helpers.MAJFCTools.MAJFCToolsException;

/**
 * @author mikefedora
 *
 */
public class MAJFCMaths {
	public static final int FIND_MAX_AND_MIN_MINIMUM_INDEX = 0;
	public static final int FIND_MAX_AND_MIN_MAXIMUM_INDEX = 1;

	public static final int PDF_VARIABLE_VALUE_INDEX = 0;
	public static final int PDF_PDF_VALUE_INDEX = 1;

	private static int NUMBER_OF_DERIVATIVE_TYPES = 0;
	public static final MAJFCMathsDerivativeType DERIVATIVE_TYPE_FORWARD_DIFF = new MAJFCMathsDerivativeType(NUMBER_OF_DERIVATIVE_TYPES++);
	public static final MAJFCMathsDerivativeType DERIVATIVE_TYPE_CENTRAL_DIFF = new MAJFCMathsDerivativeType(NUMBER_OF_DERIVATIVE_TYPES++);
	public static final MAJFCMathsDerivativeType DERIVATIVE_TYPE_BACKWARD_DIFF = new MAJFCMathsDerivativeType(NUMBER_OF_DERIVATIVE_TYPES++);

	private static int NUMBER_OF_STRIP_FROM_TYPES = 0;
	public static final MAJFCMathsStripFromType STRIP_FROM_START = new MAJFCMathsStripFromType(NUMBER_OF_STRIP_FROM_TYPES++);
	public static final MAJFCMathsStripFromType STRIP_FROM_EACH_END = new MAJFCMathsStripFromType(NUMBER_OF_STRIP_FROM_TYPES++);
	public static final MAJFCMathsStripFromType STRIP_FROM_END = new MAJFCMathsStripFromType(NUMBER_OF_STRIP_FROM_TYPES++);
	
	private static int NUMBER_OF_PSD_WINDOW_TYPES = 0;
	public static final MAJFCMathsPSDWindowType PSD_WINDOW_TYPE_NONE = new MAJFCMathsPSDWindowType(NUMBER_OF_PSD_WINDOW_TYPES++);
	public static final MAJFCMathsPSDWindowType PSD_WINDOW_TYPE_BARTLETT = new MAJFCMathsPSDWindowType(NUMBER_OF_PSD_WINDOW_TYPES++);
	public static final MAJFCMathsPSDWindowType PSD_WINDOW_TYPE_HAMMING = new MAJFCMathsPSDWindowType(NUMBER_OF_PSD_WINDOW_TYPES++);
	
	/**
	 * Calculates the arithmetic mean of a set of data
	 * @param data The data to calculate the mean for
	 * @return The calculated mean
	 */
	public static double mean(List<Double> data) {
		return mean(data, data.size());
	}

	/**
	 * Calculates the arithmetic mean of the specified number of elements from a set of data
	 * @param data The data to calculate the mean for
	 * @param numberOfElementsToUse The number of elements of data (starting with the first) to use for the averaging
	 * @return The calculated mean
	 */
	public static double mean(List<Double> data, int numberOfElementsToUse) {
		double total = 0;
		int numberOfInvalidMeasurements = 0;
		numberOfElementsToUse = Math.min(data.size(), numberOfElementsToUse);

		for (int i = 0; i < numberOfElementsToUse; ++i){
			Double value = data.get(i);
			
			if (value.equals(Double.NaN)) {
				++numberOfInvalidMeasurements;
				continue;
			}
			
			total = total + value;
		}
		
		return total/(numberOfElementsToUse - numberOfInvalidMeasurements);
	}

	/**
	 * Calculates the arithmetic mean of the specified number of elements from a set of data
	 * @param data The data to calculate the mean for
	 * @param firstIndex The index to start at (inclusive)
	 * @param lastIndex The index to end at (exclusive)
	 * @return The calculated mean
	 */	
	public static double mean(Vector<Double> values, int firstIndex, int lastIndex) {
		if (firstIndex < 0 || lastIndex >= values.size()) {
			return Double.NaN;
		}
	
		return MAJFCMaths.mean(values.subList(firstIndex, lastIndex));
	}
	
	/**
	 * Calculates the RMS of a set of data
	 * @param data The data to calculate the RMS for
	 * @return The calculated RMS value
	 */
	public static double rms(List<Double> data) {
		return rms(data, data.size());
	}
	
	/**
	 * Calculates the RMS of the specified number of elements from a set of data
	 * @param data The data to calculate the RMS for
	 * @param numberOfElementsToUse The number of elements of data (starting with the first) to use for the averaging
	 * @return The calculated RMS value
	 */
	public static double rms(List<Double> data, int numberOfElementsToUse) {
		double total = 0;
		int numberOfInvalidMeasurements = 0;
		numberOfElementsToUse = Math.min(data.size(), numberOfElementsToUse);

		for (int i = 0; i < numberOfElementsToUse; ++i){
			Double value = data.get(i);
			
			if (value.equals(Double.NaN)) {
				++numberOfInvalidMeasurements;
				continue;
			}
			
			total = total + Math.pow(value, 2);
		}
		
		return Math.sqrt(total/(numberOfElementsToUse - numberOfInvalidMeasurements));
	}	
	
	/**
	 * Calculates the standard deviation of a set of data
	 * @param data The data to calculate the standard deviation for
	 * @return The calculated standard deviation
	 */
	public static double standardDeviation(List<Double> data) {
		return standardDeviation(data, data.size());
	}
	
	/**
	 * Calculates the standard deviation of a set of data
	 * @param data The data to calculate the standard deviation for
	 * @param numberOfElementsToUse The number of elements of data (starting with the first) to use for the calculation
	 * @return The calculated standard deviation
	 */
	public static double standardDeviation(List<Double> data, int numberOfElementsToUse) {
		double stDevSum = 0;
		double mean = mean(data);
		int numberOfInvalidMeasurements = 0;
		numberOfElementsToUse = Math.min(data.size(), numberOfElementsToUse);
		
		for (int i = 0; i < numberOfElementsToUse; ++i){
			Double value = data.get(i);
			
			if (value.equals(Double.NaN)) {
				++numberOfInvalidMeasurements;
				continue;
			}

			stDevSum = stDevSum + Math.pow(value - mean, 2);
		}
		
		return Math.sqrt(stDevSum/(numberOfElementsToUse - numberOfInvalidMeasurements));
	}
	
	/**
	 * Calculates the mean absolute deviation of a set of data
	 * @param data The data to calculate the mean absolute deviation for
	 * @return The calculated mean absolute deviation
	 */
	public static double meanAbsoluteDeviation(List<Double> data) {
		double meanAbsDevSum = 0;
		double mean = mean(data);
		int numberOfMeasurements = data.size();
		int numberOfInvalidMeasurements = 0;
		
		for (int i = 0; i < numberOfMeasurements; ++i){
			Double value = data.get(i);
			
			if (value.equals(Double.NaN)) {
				++numberOfInvalidMeasurements;
				continue;
			}

			meanAbsDevSum = meanAbsDevSum + Math.abs(value - mean);
		}
		
		return meanAbsDevSum/(numberOfMeasurements - numberOfInvalidMeasurements);
	}
	
	/**
	 * Calculates the median absolute deviation of a set of data
	 * @param data The data to calculate the median absolute deviation for
	 * @return The calculated median absolute deviation
	 */
	public static double medianAbsoluteDeviation(List<Double> data) {
		double medianAbsDevSum = 0;
		int numberOfMeasurements = data.size();
		int numberOfInvalidMeasurements = 0;
		int centreIndex = numberOfMeasurements/2;
		double median = data.get(centreIndex + 1);
		
		// Copy the data otherwise we sort the original list
		Vector<Double> sortedData = new Vector<Double>(data.size());
		sortedData.addAll(data);
		
		Collections.sort(sortedData);

		if (numberOfMeasurements % 2 == 0) { 
			median = (sortedData.get(centreIndex) + median)/2;
		}

		for (int i = 0; i < numberOfMeasurements; ++i){
			Double value = sortedData.get(i);
			
			if (value.equals(Double.NaN)) {
				++numberOfInvalidMeasurements;
				continue;
			}

			medianAbsDevSum = medianAbsDevSum + Math.abs(value - median);
		}
		
		return medianAbsDevSum/(numberOfMeasurements - numberOfInvalidMeasurements);

	}
	
	/**
	 * Calculates the arithmetic mean of a set of data
	 * @param data The data to calculate the mean for
	 * @return The calculated mean
	 */
	public static double skewness(List<Double> data) {
		int numberOfValues = data.size();
		double mean = MAJFCMaths.mean(data, numberOfValues);
		double stDev = MAJFCMaths.standardDeviation(data, numberOfValues);
		
		double skewSum = 0;
		for (int item = 0; item < numberOfValues; ++item) {
			skewSum += Math.pow((data.get(item) - mean)/stDev, 3);
		}
		
		return skewSum/numberOfValues;
	}
	
	/**
	 * Calculates the integral average of a set of values taken over a line of points
	 * @param values The ordered values, in position order
	 * @param points The ordered positions of the points at which the values lie, starting with the closest to pointsLowerBound
	 * @param pointsLowerBound The lower bound for the points (this may be less than points.firstElement(), and must not be greater than points.firstElement())
	 * @param pointsUpperBound The upper bound for the points (this may be greater than points.lastElement(), and must not be less than points.lastElement())
	 * @return The integral average, or Double.NaN if the bounds are invalid or points and values are of different sizes
	 */
	public static double integralAverage(Vector<Double> values, Vector<Integer> points, int pointsLowerBound, int pointsUpperBound) {
		double sum = 0;
		int numberOfValues = values.size();
		
		if (numberOfValues != points.size() || points.firstElement() < pointsLowerBound || points.lastElement() > pointsUpperBound) {
			return Double.NaN;
		}
		
		for (int i = 0; i < numberOfValues; ++i) {
			int pointBefore = pointsLowerBound;
			int thisPoint = points.elementAt(i);
			int pointAfter = pointsUpperBound;
			double cellSizeBefore = thisPoint - pointBefore;
			double cellSizeAfter = pointAfter - thisPoint;
			
			if (i != 0) {
				pointBefore = points.elementAt(i - 1);
				cellSizeBefore = (double) (thisPoint - pointBefore)/2;
			}
			
			if (i != numberOfValues - 1) {
				pointAfter = points.elementAt(i + 1);
				cellSizeAfter = (double) (pointAfter - thisPoint)/2;
			}
			
			double cellSize = cellSizeBefore + cellSizeAfter;
			sum += values.elementAt(i) * cellSize;
		}

		return sum/(pointsUpperBound - pointsLowerBound); 
	}
	
	/**
	 * Multiplies two matrices
	 * @param lhMatrix The left hand matrix
	 * @param rhMatrix The right hand matrix
	 * @return The resulting matrix, or null if the matrices are of incompatible sizes
	 */
	public static double[][] matrixMultiplication(double[][] lhMatrix, double[][] rhMatrix) {
		if (lhMatrix[0].length != rhMatrix.length) {
			return null;
		}
		
		double[][] result = new double[lhMatrix.length][rhMatrix[0].length];
		
		for (int row = 0; row < lhMatrix.length; ++row) {
			for (int column = 0; column < rhMatrix[row].length; ++column) {
				for (int k = 0; k < lhMatrix[0].length; ++k) {
					result[row][column] += lhMatrix[row][k] * rhMatrix[k][column];
				}
			}
		}
		
		return result;
	}

	/**
	 * Translates a point into a new co-ordinate system which is a rotation of the old co-ordinate through the specified angles.
	 * Co-ordinate systems are left-handed
	 * +ve angles => the new co-ordinate system is the old one rotated clockwise when looking in the positive direction along
	 * the axis rotated about.
	 * -ve angles => the new co-ordinate system is the old one rotated anti-clockwise when looking in the positive direction along
	 * the axis rotated about.
	 * @param point The point to translate in 3-D co-ordinates {x, y, z}
	 * @param rotationAngleInYZPlanePhi The rotation angle in the YZ plane (about the X-axis), in radians
	 * @param rotationAngleInXZPlaneTheta The rotation angle in the XZ plane (about the Y-axis), in radians
	 * @param rotationAngleInXYPlaneAlpha The rotation angle in the XY plane (about the Z-axis), in radians
	 * @return
	 */
	public static double[] translateToRotatedAxes(double[] point, double rotationAngleInYZPlanePhi, double rotationAngleInXZPlaneTheta, double rotationAngleInXYPlaneAlpha) {
		if (point.length != 3) {
			return null;
		}
		
		double[][] phiRotationMatrix =
		{	{	1, 											0, 										0										},
			{	0,											Math.cos(rotationAngleInYZPlanePhi),	-Math.sin(rotationAngleInYZPlanePhi)	},
			{	0,										 	Math.sin(rotationAngleInYZPlanePhi),	Math.cos(rotationAngleInYZPlanePhi)		}};
		double[][] thetaRotationMatrix =
		{	{	Math.cos(rotationAngleInXZPlaneTheta),		0, 										Math.sin(rotationAngleInXZPlaneTheta)	},
			{	0, 											1, 										0										},
			{	-Math.sin(rotationAngleInXZPlaneTheta),		0, 										Math.cos(rotationAngleInXZPlaneTheta)	}};
		double[][] alphaRotationMatrix = 
		{	{	Math.cos(rotationAngleInXYPlaneAlpha), 		-Math.sin(rotationAngleInXYPlaneAlpha),	0										},
			{	Math.sin(rotationAngleInXYPlaneAlpha),		Math.cos(rotationAngleInXYPlaneAlpha), 	0										},
			{	0, 											0, 										1										}};
	
		double[][] unrotatedMatrix = {	{point[0]},
										{point[1]},
										{point[2]}};
		
		double[][] rotatedMatrix = MAJFCMaths.matrixMultiplication(phiRotationMatrix, unrotatedMatrix);
		rotatedMatrix = MAJFCMaths.matrixMultiplication(thetaRotationMatrix, rotatedMatrix);
		rotatedMatrix = MAJFCMaths.matrixMultiplication(alphaRotationMatrix, rotatedMatrix);
		
		double[] rotatedPoint = {rotatedMatrix[0][0], rotatedMatrix[1][0], rotatedMatrix[2][0]}; 
		
		return rotatedPoint;
	}
	
	/**
	 * High pass filter
	 * @param inputValues The values to filter
	 * @param adjustToMean If true then the mean value is subtracted from all samples before filtering, then added to the returned values 
	 * @param cutoffFrequency The cutoff frequency below which values should be removed (attenuated)
	 * @param timeStep The time between samples in inputValues
	 * @return The filtered values
	 */
	public static Vector<Double> highPassFilter(Vector<Double> inputValues, boolean adjustToMean, double cutoffFrequency, double timeStep) {
		int numberOfValues = inputValues.size();
		Vector<Double> outputValues = new Vector<Double>(numberOfValues);
		double rc = 1/(2 * Math.PI * cutoffFrequency);
		double scaleFactor = rc/(rc + timeStep);
		double mean = adjustToMean ? mean(inputValues) : 1.0;

		if (adjustToMean) {
			for (int i = 0; i < numberOfValues; ++i) {
				inputValues.set(i, inputValues.elementAt(i) - mean);
			}
		}
		
		outputValues.add(inputValues.firstElement());
		
		for (int i = 1; i < numberOfValues; ++i) {
			outputValues.add(scaleFactor * (outputValues.elementAt(i - 1) + inputValues.elementAt(i) - inputValues.elementAt(i - 1)));
		}

		if (adjustToMean) {
			for (int i = 0; i < numberOfValues; ++i) {
				inputValues.set(i, inputValues.elementAt(i) + mean);
				outputValues.set(i, outputValues.elementAt(i) + mean);
			}
		}

		return outputValues;
	}
	
	/**
	 * Low pass filter
	 * @param inputValues The values to filter
	 * @param cutoffFrequency The cutoff frequency above which values should be removed (attenuated)
	 * @param timeStep The time between samples in inputValues
	 * @return The filtered values
	 */
	public static Vector<Double> lowPassFilter(Vector<Double> inputValues, double cutoffFrequency, double timeStep) {
		Vector<Double> outputValues = new Vector<Double>();
		int numberOfValues = inputValues.size();
		double rc = 1/(2 * Math.PI * cutoffFrequency);
		double smoothingFactor = timeStep/(rc + timeStep);

		outputValues.add(inputValues.firstElement());
		
		for (int i = 1; i < numberOfValues; ++i) {
			outputValues.add(smoothingFactor * inputValues.elementAt(i) + (1 - smoothingFactor) * outputValues.elementAt(i - 1));
		}
		
		return outputValues;
	}

	/**
	 * Bandwidth pass filter
	 * @param inputValues The values to filter
	 * @param lowerCutoffFrequency The cutoff frequency below which values should be removed (attenuated)
	 * @param upperCutoffFrequency The cutoff frequency above which values should be removed (attenuated)
	 * @param timeStep The time between samples in inputValues
	 * @return The filtered values
	 */
	public static Vector<Double> bandWidthPassFilter(Vector<Double> inputValues, double lowerCutoffFrequency, double upperCutoffFrequency, double timeStep) {
		Vector<Double> outputValues = highPassFilter(inputValues, true, lowerCutoffFrequency, timeStep);
		outputValues = lowPassFilter(outputValues, upperCutoffFrequency, timeStep);
		
		return outputValues;
	}
	
	/**
	 * Calculates the next derivative from the derivatives passed in
	 * Uses forward difference at first point, backward difference at last point and central difference in between
	 * @param data The derivatives to calculate the next derivatives for ("y" values)
	 * @param deltaPosition The gap between each of the data values ("dx")
	 * @param derivativeType The method used to calculate the derivative (first and last are always calculated using forward and backward diff. respectively)
	 * @return The calculated next derivatives or null if data contains less than 2 values
	 */
	public static Vector<Double> derivatives(Vector<Double> data, double deltaPosition) {
		return derivatives(data, deltaPosition, DERIVATIVE_TYPE_CENTRAL_DIFF);
	}
	
	/**
	 * Calculates the next derivative from the derivatives passed in
	 * Uses forward difference at first point, backward difference at last point and central difference in between
	 * @param data The derivatives to calculate the next derivatives for ("y" values)
	 * @param deltaPosition The gap between each of the data values ("dx")
	 * @param derivativeType The method used to calculate the derivative (first and last are always calculated using forward and backward diff. respectively)
	 * @return The calculated next derivatives or null if data contains less than 2 values
	 */
	public static Vector<Double> derivatives(Vector<Double> data, double deltaPosition, MAJFCMathsDerivativeType derivativeType) {
		int numberOfData = data.size();
		
		if (numberOfData < 2) {
			return null;
		}
		
		Vector<Double> derivatives = new Vector<Double>();
		int numberOfDataMinusOne = data.size() - 1;
		int i = 0;
		
		// First - forward difference
		double previousData = data.elementAt(i);
		double nextData = data.elementAt(i + 1);
		derivatives.add((nextData - previousData)/deltaPosition);	
		
		if (derivativeType.equals(DERIVATIVE_TYPE_FORWARD_DIFF)) {
			for (i = 1; i < numberOfDataMinusOne; ++i) {
				previousData = data.elementAt(i);
				nextData = data.elementAt(i + 1);
				derivatives.add((nextData - previousData)/deltaPosition);	
			}
		} else if (derivativeType.equals(DERIVATIVE_TYPE_CENTRAL_DIFF)) {
			for (i = 1; i < numberOfDataMinusOne; ++i) {
				previousData = data.elementAt(i - 1);
				nextData = data.elementAt(i + 1);
				derivatives.add((nextData - previousData)/(2 * deltaPosition));	
			}
		} else if (derivativeType.equals(DERIVATIVE_TYPE_BACKWARD_DIFF)) {
			for (i = 1; i < numberOfDataMinusOne; ++i) {
				previousData = data.elementAt(i - 1);
				nextData = data.elementAt(i);
				derivatives.add((nextData - previousData)/deltaPosition);	
			}
		} 
		

		// Last - backward difference. Note that at this point i == numberOfDataMinusOne, i.e. is index of last element
		// of data
		previousData = data.elementAt(i - 1);
		nextData = data.elementAt(i);
		derivatives.add((nextData - previousData)/deltaPosition);	

		return derivatives;
	}
	
	/**
	 * Calculates the next derivative from the derivatives passed in
	 * Uses forward difference at first point, backward difference at last point and central difference in between
	 * @param data The derivatives to calculate the next derivatives for
	 * @param positions The positions/times/whatever corresponding to the data values
	 * @return The calculated next derivatives or null if the calculation fails
	 */
	public static Vector<Double> derivativesIntegerPositions(Vector<Double> data, Vector<Integer> positions) throws MAJFCToolsException {
		int numberOfPositions = positions.size();
		Vector<Double> positionsD = new Vector<Double>(numberOfPositions);
		
		for (int i = 0; i < numberOfPositions; ++i) {
			positionsD.add(positions.elementAt(i).doubleValue());
		}
		
		return derivatives(data, positionsD);
	}
	
	/**
	 * Calculates the next derivative from the derivatives passed in
	 * Uses forward difference at first point, backward difference at last point and central difference in between
	 * @param data The derivatives to calculate the next derivatives for
	 * @param positions The positions/times/whatever corresponding to the data values
	 * @return The calculated next derivatives or null if the calculation fails
	 */
	public static Vector<Double> derivatives(Vector<Double> data, Vector<Double> positions) throws MAJFCToolsException {
		int numberOfData = data.size();
		Vector<Double> derivatives = new Vector<Double>(numberOfData);
		
		if (positions.size() != numberOfData) {
			throw new MAJFCToolsException("Failed to calculated derivatives - incompatibile vector sizes");
		}
		
		if (numberOfData < 2) {
			throw new MAJFCToolsException("Failed to calculated derivatives - too few data points");
		}
		
		int numberOfDataMinusOne = data.size() - 1;
		int i = 0;
		
		// First - forward difference
		double previousData = data.elementAt(i);
		double previousPosition = positions.elementAt(i);
		double nextData = data.elementAt(i + 1);
		double nextPosition = positions.elementAt(i + 1);
		derivatives.add((nextData - previousData)/(nextPosition - previousPosition));	
		
		// Middle - central difference
		for (i = 1; i < numberOfDataMinusOne; ++i) {
			previousData = data.elementAt(i - 1);
			previousPosition = positions.elementAt(i - 1);
			nextData = data.elementAt(i + 1);
			nextPosition = positions.elementAt(i + 1);
			derivatives.add((nextData - previousData)/(nextPosition - previousPosition));	
		}

		// Last - backward difference. Note that at this point i == numberOfDataMinusOne, i.e. is index of last element
		// of data
		previousData = data.elementAt(i - 1);
		previousPosition = positions.elementAt(i - 1);
		nextData = data.elementAt(i);
		nextPosition = positions.elementAt(i);
		derivatives.add((nextData - previousData)/(nextPosition - previousPosition));	

		return derivatives;
	}
	
	/**
	 * Checks whether one set of objects is a subset of another.
	 * Uses the objects' equals method, so make sure this is valid
	 * @param superset The superset
	 * @param subset The subset
	 * @return True if all elements of subset are in superset
	 */
	@SuppressWarnings("rawtypes")
	public static boolean isSubset(Hashtable superset, Vector subset) {
		int numberOfObjectsInSubset = subset.size();
		
		for (int i = 0; i < numberOfObjectsInSubset; ++i) {
			if (superset.get(subset.elementAt(i)) != null) {
				continue;
			}
			
			return false;
		}

		return true;
	}

	/**
	 * Calculates the covariance between two sets of data. Calculates the mean values from the data sets for use in the covariance calculation.
	 * @param dataSet1 The first data set
	 * @param dataSet2 The second data set
	 * @return
	 */
	public static double covariance(List<Double> dataSet1, List<Double> dataSet2) {
		int numberOfData = Math.min(dataSet1.size(), dataSet2.size());
		double mean1 = mean(dataSet1, numberOfData);
		double mean2 = mean(dataSet2, numberOfData);
		double stDev1 = standardDeviation(dataSet1, numberOfData);
		double stDev2 = standardDeviation(dataSet2, numberOfData);
		
		return correlation(dataSet1, mean1, stDev1, dataSet2, mean2, stDev2);
	}

	/**
	 * Calculates the covariance between two sets of data. Uses the supplied mean values in the covariance calculation
	 * @param dataSet1 The first data set
	 * @param dataSet1Mean The mean of the first data set
	 * @param dataSet2 The second data set
	 * @param dataSet2Mean The mean of the second data set
	 * @return
	 */
	public static double covariance(List<Double> dataSet1, double dataSet1Mean, List<Double> dataSet2, double dataSet2Mean) {
		int numberOfData = Math.min(dataSet1.size(), dataSet2.size());
		double fluctuationProductSum = 0;
		
		for (int i = 0; i < numberOfData; ++i) {
			double datum1 = dataSet1.get(i);
			double datum2 = dataSet2.get(i);
			
			fluctuationProductSum += (datum1 - dataSet1Mean) * (datum2 - dataSet2Mean);
		}
		
		return fluctuationProductSum/numberOfData;
	}

	/**
	 * Calculates the correlation between two sets of data. Calculates the mean values from the data sets for use in the correlation calculation.
	 * @param dataSet1 The first data set
	 * @param dataSet2 The second data set
	 * @return
	 */
	public static double correlation(List<Double> dataSet1, List<Double> dataSet2) {
		int numberOfData = Math.min(dataSet1.size(), dataSet2.size());
		double mean1 = mean(dataSet1, numberOfData);
		double mean2 = mean(dataSet2, numberOfData);
		double stDev1 = standardDeviation(dataSet1, numberOfData);
		double stDev2 = standardDeviation(dataSet2, numberOfData);
		
		return correlation(dataSet1, mean1, stDev1, dataSet2, mean2, stDev2);
	}
	
	/**
	 * Calculates the correlation between two sets of data. Uses the supplied mean values in the correlation calculation
	 * @param dataSet1 The first data set
	 * @param dataSet1Mean The mean of the first data set
	 * @param dataSet2 The second data set
	 * @param dataSet2Mean The mean of the second data set
	 * @return
	 */
	public static double correlation(List<Double> dataSet1, double dataSet1Mean, double dataSet1StDev, List<Double> dataSet2, double dataSet2Mean, double dataSet2StDev) {
		return covariance(dataSet1, dataSet1Mean, dataSet2, dataSet2Mean)/(dataSet1StDev * dataSet2StDev);
	}

	/**
	 * Calculates the correlation between two sets of data. Calculates the mean values from the data sets for use in the correlation calculation.
	 * @param dataSet1 The first data set
	 * @param dataSet2 The second data set
	 * @return
	 */
	public static double pseudoCorrelation(List<Double> dataSet1, List<Double> dataSet2) {
		int numberOfData = Math.min(dataSet1.size(), dataSet2.size());
		double fluctuationProductSum = 0;
//		Vector<Double> residue = new Vector<Double>(numberOfData);
		
		for (int i = 0; i < numberOfData; ++i) {
			double datum1 = dataSet1.get(i);
			double datum2 = dataSet2.get(i);
			
//			residue.add(datum2 - datum1);
			fluctuationProductSum += Math.abs(datum2 - datum1);
		}
		
		return fluctuationProductSum/numberOfData * (mean(dataSet1) > mean(dataSet2) ? -1 : 1);
	}
	
	public static Vector<Double> estimatePowerSpectrumBartlett(Vector<Double> inputSignal, double samplingRate, int numberOfSplits) {
		return estimatePowerSpectrumAverage(inputSignal, samplingRate, numberOfSplits, 0, PSD_WINDOW_TYPE_NONE);
	}

	
	public static Vector<Double> estimatePowerSpectrumWelch(Vector<Double> inputSignal, double samplingRate, int numberOfSplits, int percentOverlap, MAJFCMathsPSDWindowType windowType) {
		return estimatePowerSpectrumAverage(inputSignal, samplingRate, numberOfSplits, percentOverlap, windowType);
	}
	
	public static Vector<Double> estimatePowerSpectrumAverage(Vector<Double> inputSignal, double samplingRate, int numberOfSplits, int percentOverlap, MAJFCMathsPSDWindowType windowType) {
		// Split into lengths
		Vector<FourierTransformResults> ftrs = new Vector<FourierTransformResults>(numberOfSplits);
		int numberOfValues = inputSignal.size();
		int splitLength  = (int) Math.floor(((double) numberOfValues)/(numberOfSplits - (numberOfSplits - 1) * ((double) percentOverlap)/100));
		Vector<Double> window = getWindow(windowType, splitLength);
//		double paddingScaler = Math.pow(2, MAJFCMaths.nearestPowerOfTwo(splitLength, true))/(double) splitLength;
		int overlap = (int) Math.ceil((percentOverlap/100d) * splitLength);

		double inputMean = MAJFCMaths.mean(inputSignal);
		
		for (int i = 0; i < numberOfSplits; ++i) {
			Vector<Double> split = new Vector<Double>(splitLength);
			
			for (int j = 0; j < splitLength; ++j) {
				// Remove the mean value, so we are just looking at the fluctuations
				double inputValue = inputSignal.get(i * (splitLength - overlap) + j) - inputMean;
				split.add(inputValue * window.get(j));
			}
			
			ftrs.add(fastFourierTransform(split));
		}
		
		Vector<Double> outputSignal = new Vector<Double>(numberOfSplits);
		
		// ftrs will have been padded to a power of two length, and so may be longer than splitLength.
		// They should all be the same length.
		int ftrLength = ftrs.firstElement().size();
		int ftrLengthToNyquist = ftrLength/2;
		
		// We are only interested in frequencies below the Nyquist frequency
		for (int i = 0; i < numberOfSplits; ++i) {
			FourierTransformResults ftr = ftrs.elementAt(i);
			
			for (int j = 0; j < ftrLengthToNyquist; ++j) {
				if (i == 0) {
					outputSignal.add(Math.pow(ftr.get(j).magnitude(), 2));
				} else {
					outputSignal.set(j, outputSignal.elementAt(j) + Math.pow(ftr.get(j).magnitude(), 2));
				}
			}
		}

		double powerFromPSD = 0;
		double deltaFreq = samplingRate/ftrLength;
		
		for (int j = 0; j < ftrLengthToNyquist; ++j) {
			// Get the mean value at this frequency across the splits
			double outputValue = outputSignal.get(j)/numberOfSplits;
			outputSignal.set(j, outputValue);
			
			// Calculate the energy at this frequency (ignore deltaFreq for now as it is constant and so we can apply it to all values in a minute).
			powerFromPSD += outputValue;
		}

		// Apply deltaFreq here (ignored in loop above)
		powerFromPSD *= deltaFreq;
		
		// We want a power spectral density, i.e. the integral over frequency gives the time domain mean power, which is the variance of the input signal.
		// However, we want a PSD normalised by the variance of the input signal, so ignore it.
		double meanTimeDomainPower = 1; //Math.pow(MAJFCMaths.standardDeviation(inputSignal), 2);
		double powerScalar = meanTimeDomainPower/powerFromPSD;
		
		for (int j = 0; j < ftrLengthToNyquist; ++j) {
			outputSignal.set(j, powerScalar * outputSignal.get(j));
		}
		
		return outputSignal;
	}
	
	private static Vector<Double> getWindow(MAJFCMathsPSDWindowType windowType, int windowLength) {
		Vector<Double> window = new Vector<Double>(windowLength);
		int middle = windowLength/2;
		
		for (int i = 0; i < windowLength; ++i) {
			double windowValue = 1;
			
			if (windowType.equals(PSD_WINDOW_TYPE_BARTLETT)) {
				double iOverMiddle = ((double) i)/middle;
				windowValue = (i < middle) ? 2 * iOverMiddle : 2 - 2 * (iOverMiddle - 1);
			} else if (windowType.equals(PSD_WINDOW_TYPE_HAMMING)) {
				windowValue = 0.54 - 0.46 * Math.cos(2d * Math.PI * ((double) i)/windowLength);
			}
	
			window.add(windowValue);
		}

		return window;
	}
	
	/**
	 * Calculates the Fourier Transform of a signal directly. If the signal has non-zero mean then it is adjusted (by subtracting the mean
	 * value from each value) so that it has zero mean.
	 * @param inputSignal
	 * @return
	 */
	public static FourierTransformResults slowFourierTransform(LinkedList<Double> inputSignal, double samplingRate) {
		LinkedList<ComplexNumber> complexInputSignal = new LinkedList<ComplexNumber>();
		int numberOfValues = inputSignal.size();
		double inputSignalMean = mean(inputSignal);
		
		for (int i = 0; i < numberOfValues; ++i) {
			complexInputSignal.add(new ComplexNumber(inputSignal.get(i) - inputSignalMean, 0));
		}
		
		return slowFourierTransformC(complexInputSignal, samplingRate);
	}
	
	/**
	 * Calculates the Fourier Transform of a signal directly. If the signal has non-zero mean then it is adjusted (by subtracting the mean
	 * value from each value) so that it has zero mean.
	 * @param inputSignal
	 * @return
	 */
	public static FourierTransformResults slowFourierTransformC(LinkedList<ComplexNumber> complexInputSignal, double samplingRate) {
		int numberOfValues = complexInputSignal.size();
		FourierTransformResults outputSignal = new FourierTransformResults();
				
		double minusTwoPiOverN = -2 * Math.PI / numberOfValues;
		for (int k = 0; k < numberOfValues/2; ++k) {
			ComplexNumber transform = new ComplexNumber();
			
			for (int n = 0; n < numberOfValues; ++n) {
				double complexAngle = minusTwoPiOverN * k * n;
				transform = transform.plus(complexInputSignal.get(n).multipliedBy(new ComplexNumber(Math.cos(complexAngle)/(numberOfValues/2), Math.sin(complexAngle)/(numberOfValues/2)))); 
			}
			
			// Sample length may not equal one cycle, so need to convert wavenumber/time
			outputSignal.add(transform);
		}
		
		return outputSignal;
	}
	
	/**
	 * Calculates the Fourier Transform of a signal using a Fast Fourier Transform. If the signal has non-zero mean then it is adjusted (by subtracting the mean
	 * value from each value) so that it has zero mean, and the number of input signal values is reduced to the nearest power of 2 lower
	 * than the number of values by stripping from the start of the data set.
	 * @param inputSignal
	 * @return
	 */
	public static FourierTransformResults fastFourierTransform(Vector<Double> inputSignal) {
		zeroPadToPowerOfTwoSize(inputSignal);

		double inputSignalMean = mean(inputSignal);
		int numberOfValues = inputSignal.size();
		
		FourierTransformResults complexInputSignal = new FourierTransformResults();

		// Make a complex signal from the real signal, and pre-divide by N (numberOfValues) to remove necessity to divide by 2 at
		// each step of the FFT (see Newland D.E. (1993), "An Introduction to Random Vibrations, Spectral & Wavelet Analysis").
		for (int i = 0; i < numberOfValues; ++i) {
			complexInputSignal.add(new ComplexNumber((inputSignal.get(i) - inputSignalMean)/numberOfValues, 0/numberOfValues));
		}
		
		return fastFourierTransformRecursor(complexInputSignal);
	}
	
	/**
	 * Calculates the Fourier Transform of a signal using a Fast Fourier Transform. If the signal has non-zero mean then it is adjusted (by subtracting the mean
	 * value from each value) so that it has zero mean, and the number of input signal values is reduced to the nearest power of 2 lower
	 * than the number of values by stripping from the start of the data set.
	 * @param inputSignal
	 * @return
	 */
//	public static FourierTransformResults fastFourierTransformC(LinkedList<ComplexNumber> complexInputSignal, double samplingRate) {
//		reduceToPowerOfTwoSize(complexInputSignal, true);
//		int numberOfValues = complexInputSignal.size();
//		
//		return fastFourierTransformRecursor(new FourierTransformResults(samplingRate/numberOfValues, complexInputSignal));
//	}

	private static FourierTransformResults fastFourierTransformRecursor(FourierTransformResults inputSignal) {
		int numberOfValues = inputSignal.size();
		FourierTransformResults outputSignal = new FourierTransformResults();
		if (numberOfValues == 1) {
			outputSignal.add(new ComplexNumber(inputSignal.getFirst()));
			return outputSignal;
		}

		FourierTransformResults evenIndexedValues = new FourierTransformResults();
		FourierTransformResults oddIndexedValues = new FourierTransformResults();
		
		for (int i = 0; i < numberOfValues; ++i) {
			evenIndexedValues.add(inputSignal.get(i));
			// Note that i is incremented here too, so it increases by 2 on each pass
			oddIndexedValues.add(inputSignal.get(++i));
		}
		
		FourierTransformResults fftOfEvenIndexedValues = fastFourierTransformRecursor(evenIndexedValues);
		FourierTransformResults fftOfOddIndexedValues = fastFourierTransformRecursor(oddIndexedValues);

		int numberOfValuesInOddAndEvenParts = fftOfEvenIndexedValues.size();
		double factor = -2 * Math.PI / numberOfValues;

		for (int k = 0; k < numberOfValuesInOddAndEvenParts; ++k) {
			outputSignal.add(fftOfEvenIndexedValues.get(k).plus(fftOfOddIndexedValues.get(k).multipliedBy(new ComplexNumber(Math.cos(factor * k), Math.sin(factor * k)))));
		}
		
		for (int k = 0; k < numberOfValuesInOddAndEvenParts; ++k) {
			outputSignal.add(fftOfEvenIndexedValues.get(k).minus(fftOfOddIndexedValues.get(k).multipliedBy(new ComplexNumber(Math.cos(factor * k), Math.sin(factor * k)))));
		}
		
		return outputSignal;
	}
	
	/**
	 * Pads (with zeroes) the size of the input list to the smallest power of 2 which is larger than the number of items in the list.
	 * @return The number of padding zeroes added
	 */
	private static int zeroPadToPowerOfTwoSize(Vector<Double> inputList) {
		int numberOfValues = inputList.size();
		
		// We need numberOfValues to be an integer power of 2, 2^n
		// Find the largest integer power of 2 that is larger than the number of values in the inputSignal...
		int powerOfTwo = (int) Math.ceil(Math.log(numberOfValues)/Math.log(2));
		
		// At this point, 2^powerOfTwo will be the first integer power of 2 that is larger than the number of
		// values in the inputSignal, so add extra values at the end
		int numberOfValuesToAdd = (int) (Math.pow(2, powerOfTwo) - numberOfValues);
		
		for (int i = 0; i < numberOfValuesToAdd; ++i) {
			inputList.add(0d);
		}
		
		return numberOfValuesToAdd;
	}

	public static int nearestPowerOfTwo(double value, boolean exceeding) {
		double logOverLogTwo = Math.log(value)/Math.log(2);
		
		return exceeding ? (int) Math.ceil(logOverLogTwo) : (int) Math.floor(logOverLogTwo);
	}
	
	/**
	 * Reduces the size of the input list to the largest power of 2 which is smaller than the number of items in the list.
	 * @param inputList The list to reduce
	 */
	public static void reduceToPowerOfTwoSize(Vector<Double> inputList) {
		reduceToPowerOfTwoSize(inputList, STRIP_FROM_END);
	}
	
	/**
	 * Reduces the size of the input list to the largest power of 2 which is smaller than the number of items in the list.
	 * @param inputList The list to reduce
	 * @param removeFromStart If true excess items are removed from the start of the list, if false from the end
	 */
	public static void reduceToPowerOfTwoSize(Vector<Double> inputList, MAJFCMathsStripFromType stripFrom) {
		int numberOfValues = inputList.size();
		
		// We need numberOfValues to be an integer power of 2, 2^n
		// Find the largest integer power of 2 that is smaller than the number of values in the inputSignal...
		int powerOfTwo = nearestPowerOfTwo(numberOfValues, false);
		
		// ...and then strip off the excess values at the start of the inputSignal
		int numberOfValuesToStrip = (int) (numberOfValues - Math.pow(2, powerOfTwo));
		
		for (int i = 0; i < numberOfValuesToStrip; ++i) {
			if (stripFrom.equals(STRIP_FROM_START)) {
				// Going to be slow, may need a rewrite if it's used
				inputList.remove(0);
			} else if (stripFrom.equals(STRIP_FROM_EACH_END)) {
				// Make sure we end up with 
				if (i % 2 == 1) {
					inputList.remove(0);
				} else {
					inputList.remove(inputList.size() - 1);
				}
			} else {
				inputList.remove(inputList.size() - 1);
			}
		}		
	}
	
	public static class ComplexNumber implements Comparable<ComplexNumber> {
		double mRe;
		double mIm;
		
		public ComplexNumber() {
			mRe = 0;
			mIm = 0;
		}
		
		public ComplexNumber(double real, double imaginary) {
			mRe = real;
			mIm = imaginary;
		}
		
		public ComplexNumber(ComplexNumber original) {
			mRe = original.mRe;
			mIm = original.mIm;
		}
		
		public ComplexNumber plus(ComplexNumber rhs) {
			return new ComplexNumber(mRe + rhs.mRe, mIm + rhs.mIm);
		}
		
		public ComplexNumber minus(ComplexNumber rhs) {
			return new ComplexNumber(mRe - rhs.mRe, mIm - rhs.mIm);
		}
		
		public ComplexNumber multipliedBy(ComplexNumber rhs) {
			return new ComplexNumber(mRe * rhs.mRe - mIm * rhs.mIm, mRe * rhs.mIm + mIm * rhs.mRe);
		}
		
		public ComplexNumber conjugate() {
			return new ComplexNumber(mRe, -mIm);
		}
		
		public double magnitude() {
			return Math.sqrt(Math.pow(mRe, 2) + Math.pow(mIm, 2)); 
		}

		public boolean equals(ComplexNumber theOtherOne) {
			if (mRe == theOtherOne.mRe && mIm == theOtherOne.mIm) {
				return true;
			}
			
			return false;
		}
		
		@Override
		public int compareTo(ComplexNumber theOtherOne) {
			double magDiff = magnitude() - theOtherOne.magnitude();
			
			return magDiff == 0 ? 0 : magDiff > 0 ? 1 : -1;
		}
	}
	
	@SuppressWarnings("serial")
	public static class FourierTransformResults extends LinkedList<ComplexNumber> {
		private FourierTransformResults() {
		}
	}

	/**
	 * Minimise yComp and zComp by adjusting the rotation angles, theta (xz-plane), alpha (xy-plane) and phi (yz-plane) rotations
	 * @param xComp The x-component
	 * @param yComp The y-component
	 * @param zComp The z-component
	 * @param minRotation The minimum correction angle to be used
	 * @param maxRotation The maximum correction angle to be used
	 * @param minimisationAngles Holder to return the calculated theta, alpha and phi to minimise yComp and zComp, in that order
	 */
	public static void rotateToMinimise(double xComp, double yComp,	double zComp, int minRotation, int maxRotation, double[] minimisationAngles) {
		double minMagnitude = Double.MAX_VALUE;
		double testStep = Math.toRadians(0.1);
		double testUpperLimit = Math.toRadians(maxRotation) + testStep;
		double testLowerLimit = Math.toRadians(minRotation);

		double[] uncorrectedVelocityMatrix = { xComp, yComp, zComp };
		
		// Mis-measurement of the rotation around the y-axis, theta, affects w, the Z-velocity
		for (double thetaCorrectionTestValue = testLowerLimit; thetaCorrectionTestValue < testUpperLimit; thetaCorrectionTestValue += testStep) {
			// The rotation angles are the *clockwise* rotation of the probe to the original axes. Therefore we are translating to a
			// a co-ordinate system which is *anti-clockwise* rotated from the probe axes, i.e. through the negative angles
			double[] correctedVelocities = MAJFCMaths.translateToRotatedAxes(uncorrectedVelocityMatrix, 0, thetaCorrectionTestValue, 0);
			double magnitude = Math.abs(correctedVelocities[2]);
			
			if (magnitude < minMagnitude) {
				minMagnitude = magnitude;
				minimisationAngles[0] = Math.toDegrees(thetaCorrectionTestValue);
			}
		}
		
		minMagnitude = Double.MAX_VALUE;
		// Mis-measurement of the XY-plane rotation, alpha, affects v, the Y-velocity
		for (double alphaCorrectionTestValue = testLowerLimit; alphaCorrectionTestValue < testUpperLimit; alphaCorrectionTestValue += testStep) {
			// The rotation angles are the *clockwise* rotation of the probe to the original axes. Therefore we are translating to a
			// a co-ordinate system which is *anti-clockwise* rotated from the probe axes, i.e. through the negative angles
			double[] correctedVelocities = MAJFCMaths.translateToRotatedAxes(uncorrectedVelocityMatrix, 0, 0, alphaCorrectionTestValue);
			double magnitude = Math.abs(correctedVelocities[1]);
			
			if (magnitude < minMagnitude) {
				minMagnitude = magnitude;
				minimisationAngles[1] = Math.toDegrees(alphaCorrectionTestValue);
			}
		}
		
		// Mis-measurement of the YZ-plane rotation, phi, does not bleed u into v or z so there is no analogous way of calculating a correction
		minimisationAngles[2] = 0;
	}

	/**
	 * Calculates the probability density function of a set of values
	 * @param values The values to calculate the pdf for
	 * @param normalize If true, the pdf is normalized to give an integral of 1 over the whole value range
	 * @param pdfGranularity The number of intervals to split the values range into
	 * @return A list of arrays containing value and occurrence count pairs, value in [0], occurrence count in [1]
	 */
	public static LinkedList<Double[]> probabilityDensityFunction(Vector<Double> values, boolean normalize, int pdfGranularity) {
		LinkedList<Double[]> pdf = new LinkedList<Double[]>();
		final Double[] maxAndMin = findMaxAndMin(values);
		final double maxValue = maxAndMin[FIND_MAX_AND_MIN_MAXIMUM_INDEX];
		final double minValue = maxAndMin[FIND_MAX_AND_MIN_MINIMUM_INDEX];
		final double step = (maxValue - minValue)/pdfGranularity;
		final int numberOfValues = values.size();
		
		for (int valuesIndex = 0; valuesIndex < numberOfValues; ++valuesIndex) {
			Double value = values.get(valuesIndex);
			double lowerBound = minValue;
			double upperBound = lowerBound + step;

			for (int pdfIndex = 0; pdfIndex < pdfGranularity; lowerBound += step, upperBound += step, ++pdfIndex) {
				// Label this part of the velocity range with the mean value of the range
				if (pdf.size() == pdfIndex) {
					pdf.add(new Double[] { 0.0, 0.0 });
					pdf.getLast()[PDF_VARIABLE_VALUE_INDEX] = (lowerBound + upperBound)/2;
				}
				
				// The value is within this part of the range of velocities, so increment the number of occurrences
				if (value >= lowerBound && value < upperBound) {
					pdf.get(pdfIndex)[PDF_PDF_VALUE_INDEX] += 1.0/numberOfValues;
					break;
				}
			}
		}
		
		if (normalize == false) {
			return pdf;
		}
		
		// Normalisation
		double integralSum = 0;
		
		for (int pdfIndex = 1; pdfIndex < pdfGranularity; ++pdfIndex) {
			integralSum += (pdf.get(pdfIndex)[0] - pdf.get(pdfIndex - 1)[0]) * pdf.get(pdfIndex)[1];
		}

		for (int pdfIndex = 0; pdfIndex < pdfGranularity; ++pdfIndex) {
			pdf.get(pdfIndex)[1] /= integralSum;
		}

		return pdf;
	}
	
	/**
	 * Finds the maximum and minimum values from a list
	 * @param values The values to find the maximum and minimum for
	 * @return The maximum and minimum values in a Vector (elementAt(0) is max, elementAt(1) is min)
	 */
	public static Double[] findMaxAndMin(List<Double> values) {
		Double[] maxAndMin = new Double[2];
		maxAndMin[FIND_MAX_AND_MIN_MINIMUM_INDEX] = Double.MAX_VALUE;
		maxAndMin[FIND_MAX_AND_MIN_MAXIMUM_INDEX] = Double.MIN_VALUE;
		int numberOfValues = values.size();
		
		for (int i = 0; i < numberOfValues; ++i) {
			Double value = values.get(i);
			
			if (value < maxAndMin[FIND_MAX_AND_MIN_MINIMUM_INDEX]) {
				maxAndMin[FIND_MAX_AND_MIN_MINIMUM_INDEX] = value;
			}
			
			if (value > maxAndMin[FIND_MAX_AND_MIN_MAXIMUM_INDEX]) {
				maxAndMin[FIND_MAX_AND_MIN_MAXIMUM_INDEX] = value;
			}
		}
		
		return maxAndMin;
	}
	
	public static double round(double value, int dps) {
		  double power = Math.pow(10, dps);
		  value *= power;
		  value = Math.round(value);
		  
		  return value/power;
	}
	
	/**
	 * Inner class
	 * 
	 * @author MAJ727
	 * 
	 */
	public static class MAJFCMathsDerivativeType extends MAJFCSafeArray.MAJFCSafeArrayIndex {
		private MAJFCMathsDerivativeType(int index) {
			super(index);
		}
	}
	
	/**
	 * Inner class
	 * 
	 * @author MAJ727
	 * 
	 */
	public static class MAJFCMathsStripFromType extends MAJFCSafeArray.MAJFCSafeArrayIndex {
		private MAJFCMathsStripFromType(int index) {
			super(index);
		}
	}	
	
	/**
	 * Inner class
	 * 
	 * @author MAJ727
	 * 
	 */
	public static class MAJFCMathsPSDWindowType extends MAJFCSafeArray.MAJFCSafeArrayIndex {
		private MAJFCMathsPSDWindowType(int index) {
			super(index);
		}
	}
	
	/**
	 * Interpolation
	 */
	public static int interpolate(int xInterpolationPoint, int x1, int x2, int y1, int y2) {
		return (int) interpolate((double) xInterpolationPoint, (double) x1, (double) x2, (double) y1, (double) y2);
	}
	
	/**
	 * Interpolation
	 */
	public static double interpolate(double xInterpolationPoint, double x1, double x2, double y1, double y2) {
		return y1 + (xInterpolationPoint - x1) * (y1 - y2)/(x1 - x2);
	}
}
