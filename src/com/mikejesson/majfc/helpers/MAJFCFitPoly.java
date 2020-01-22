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

// This file is based on source code taken from:
//		http://www.particle.kth.se/~lindsey/JavaCourse/Book/Part1/Physics/Chapter08/fitPoly.html
// under the following terms:
//		"Java Tech"
//  	Code provided with book for educational purposes only.
//  	No warranty or guarantee implied.
//  	This code freely available. No copyright claimed.
//  	2003
//
// No copyright on the original code is claimed

package com.mikejesson.majfc.helpers;

import Jama.*;

/**
 *  Fit polynomial line to a set of data points.
 *  Implements the Fit interface.
 */
public class MAJFCFitPoly
{
  /**
    *  Use the Least Squares fit method for fitting a
    *  polynomial to 2-D data for measurements
    *  y[i] vs. dependent variable x[i]. This fit assumes
    *  there are errors only on the y measuresments as
    *  given by the sigma_y array.<br><br>
    *
    *  See, e.g. Press et al., "Numerical Recipes..." for details
    *  of the algorithm. <br><br>
    *
    *  The solution to the LSQ fit uses the open source JAMA -
    *  "A Java Matrix Package" classes. See http://math.nist.gov/javanumerics/jama/
    *  for description.<br><br>
    *
    *  @param parameters - first half of the array holds the coefficients for the polynomial.
    *  The second half holds the errors on the coefficients.
    *  @param x - independent variable
    *  @param y - vertical dependent variable
    *  @param sigma_x - std. dev. error on each x value
    *  @param sigma_y - std. dev. error on each y value
    *  @param num_points - number of points to fit. Less than or equal to the
    *  dimension of the x array.
   **/
  public static void fit (double [] parameters, double [] x, double [] y, double [] sigma_x, double [] sigma_y, int num_points) throws Exception {

    // numParams = num coeff + error on each coeff.
    int nk = parameters.length/2;

    double [][] alpha  = new double[nk][nk];
    double [] beta = new double[nk];
    double term = 0;

    for (int k=0; k < nk; k++) {

        // Only need to calculate diagonal and upper half
        // of symmetric matrix.
        for (int j=k; j < nk; j++) {

            // Calc terms over the data points
            term = 0.0;
            alpha[k][j] = 0.0;
            for (int i=0; i < num_points; i++) {

                double prod1 = 1.0;
                // Calculate x^k
                if ( k > 0) for (int m=0; m < k; m++) prod1 *= x[i];

                double prod2 = 1.0;
                // Calculate x^j
                if ( j > 0) for (int m=0; m < j; m++) prod2 *= x[i];

                // Calculate x^k * x^j
                term =  (prod1*prod2);

                if (sigma_y != null && sigma_y[i] != 0.0)
                    term /=  (sigma_y[i]*sigma_y[i]);
                alpha[k][j] += term;
            }
            alpha[j][k] = alpha[k][j];// C will need to be inverted.
        }

        for (int i=0; i < num_points; i++) {
            double prod1 = 1.0;
            if (k > 0) for ( int m=0; m < k; m++) prod1 *= x[i];
            term =  (y[i] * prod1);
            if (sigma_y != null  && sigma_y[i] != 0.0)
                term /=  (sigma_y[i]*sigma_y[i]);
            beta[k] +=term;
        }
    }

    // Use the Jama QR Decomposition classes to solve for
    // the parameters.
    Matrix alpha_matrix = new Matrix (alpha);
    QRDecomposition alpha_QRD = new QRDecomposition (alpha_matrix);
    Matrix beta_matrix = new Matrix (beta,nk);
    Matrix param_matrix;
    try {
       param_matrix = alpha_QRD.solve (beta_matrix);
    }
    catch (Exception e) {
      MAJFCLogger.log("QRD solve failed: "+ e, 10);
      throw e;
    }

    // The inverse provides the covariance matrix.
    Matrix c = alpha_matrix.inverse ();

    for (int k=0; k < nk; k++) {

      parameters[k] = param_matrix.get (k,0);

      // Diagonal elements of the covariance matrix provide
      // the square of the parameter errors. Put in top half
      // of the parametes array.
      parameters[k+nk] = Math.sqrt (c.get (k,k));
    }

  } // fit

} // FitPoly