/*
 * Copyright 2011 Peter Abeles
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package gecv.alg.detect.interest;

import gecv.alg.detect.interest.impl.ImplIntegralImageFeatureIntensity;
import gecv.struct.image.ImageFloat32;
import gecv.struct.image.ImageSInt32;


/**
 * Routines for computing the intensity of the fast hessian features in an image.
 *
 * @author Peter Abeles
 */
public class IntegralImageFeatureIntensity {

	/**
	 * Computes an approximation to the Hessian's determinant.
	 *
	 * @param integral Integral image transform of input image. Not modified.
	 * @param skip How many pixels should it skip over.
	 * @param size Hessian kernel's size.
	 * @param intensity Output intensity image.
	 */
	public static void hessian( ImageFloat32 integral, int skip , int size ,
								ImageFloat32 intensity)
	{
		// todo check size with skip
//		InputSanityCheck.checkSameShape(integral,intensity);

		ImplIntegralImageFeatureIntensity.hessianBorder(integral,skip,size,intensity);
		ImplIntegralImageFeatureIntensity.hessianInner(integral,skip,size,intensity);
	}

	/**
	 * Computes an approximation to the Hessian's determinant.
	 *
	 * @param integral Integral image transform of input image. Not modified.
	 * @param skip How many pixels should it skip over.
	 * @param size Hessian kernel's size.
	 * @param intensity Output intensity image.
	 */
	public static void hessian( ImageSInt32 integral, int skip , int size ,
								ImageFloat32 intensity)
	{
		// todo check size with skip
//		InputSanityCheck.checkSameShape(integral,intensity);

		ImplIntegralImageFeatureIntensity.hessianBorder(integral,skip,size,intensity);
		ImplIntegralImageFeatureIntensity.hessianInner(integral,skip,size,intensity);
	}
}
