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

package gecv.abst.detect.corner;

import gecv.abst.detect.extract.FactoryFeatureFromIntensity;
import gecv.abst.detect.extract.FeatureExtractor;
import gecv.abst.filter.blur.FactoryBlurFilter;
import gecv.abst.filter.blur.impl.MedianImageFilter;
import gecv.alg.detect.corner.FactoryCornerIntensity;
import gecv.alg.detect.corner.FastCornerIntensity;
import gecv.alg.detect.corner.GradientCornerIntensity;
import gecv.struct.image.ImageBase;

/**
 * Creates a family of interest point detectors which are designed to detect the corners of objects..
 *
 * <p>
 * NOTE: Sometimes the image border is ignored and some times it is not.  If feature intensities are not
 * computed along the image border then it will be full of zeros.  In that case the ignore border region
 * needs to be increased for non-max suppression or else it might generate a false positive.
 * </p>
 *
 * @author Peter Abeles
 */
public class FactoryCornerDetector {

	public static <T extends ImageBase, D extends ImageBase>
	GeneralFeatureDetector<T,D> createHarris( int featureRadius , float cornerThreshold , int maxFeatures , Class<D> derivType )
	{
		// see NOTE in comments
		int r = featureRadius*2;
		GradientCornerIntensity<D> cornerIntensity = FactoryCornerIntensity.createHarris(derivType,featureRadius,0.04f);
		return createGeneral(cornerIntensity,featureRadius,r,cornerThreshold,maxFeatures);
	}

	public static <T extends ImageBase, D extends ImageBase>
	GeneralFeatureDetector<T,D> createKlt( int featureRadius , float cornerThreshold , int maxFeatures , Class<D> derivType )
	{
		// see NOTE in comments
		int r = featureRadius*2;
		GradientCornerIntensity<D> cornerIntensity = FactoryCornerIntensity.createKlt(derivType,featureRadius);
		return createGeneral(cornerIntensity,featureRadius,r,cornerThreshold,maxFeatures);
	}

	public static <T extends ImageBase, D extends ImageBase>
	GeneralFeatureDetector<T,D> createKitRos( int featureRadius , float cornerThreshold , int maxFeatures , Class<D> derivType )
	{
		GeneralFeatureIntensity<T,D> intensity = new WrapperKitRosCornerIntensity<T,D>(derivType);
		return createGeneral(intensity,featureRadius,0,cornerThreshold,maxFeatures);
	}

	public static <T extends ImageBase, D extends ImageBase>
	GeneralFeatureDetector<T,D> createFast( int featureRadius , int pixelTol , int maxFeatures , Class<T> imageType)
	{
		FastCornerIntensity<T> alg = FactoryCornerIntensity.createFast12(imageType,pixelTol,11);
		GeneralFeatureIntensity<T,D> intensity = new WrapperFastCornerIntensity<T,D>(alg);
		return createGeneral(intensity,featureRadius,0,pixelTol,maxFeatures);
	}

	public static <T extends ImageBase, D extends ImageBase>
	GeneralFeatureDetector<T,D> createMedian( int featureRadius , int pixelTol , int maxFeatures , Class<T> imageType)
	{
		MedianImageFilter<T> medianFilter = FactoryBlurFilter.median(imageType,featureRadius);
		GeneralFeatureIntensity<T,D> intensity = new WrapperMedianCornerIntensity<T,D>(medianFilter,imageType);
		return createGeneral(intensity,featureRadius,0,pixelTol,maxFeatures);
	}

	protected static <T extends ImageBase, D extends ImageBase>
	GeneralFeatureDetector<T,D> createGeneral( GradientCornerIntensity<D> cornerIntensity ,
											  int minSeparation , int ignoreBorder , float cornerThreshold , int maxFeatures ) {
		GeneralFeatureIntensity<T, D> intensity = new WrapperGradientCornerIntensity<T,D>(cornerIntensity);
		return createGeneral(intensity,minSeparation,ignoreBorder,cornerThreshold,maxFeatures);
	}

	protected static <T extends ImageBase, D extends ImageBase>
	GeneralFeatureDetector<T,D> createGeneral( GeneralFeatureIntensity<T,D> intensity ,
											  int minSeparation , int ignoreBorder , float cornerThreshold , int maxFeatures ) {
		FeatureExtractor extractor = FactoryFeatureFromIntensity.create(minSeparation,cornerThreshold,ignoreBorder,false,false,false);
		return new GeneralFeatureDetector<T,D>(intensity,extractor,maxFeatures);
	}
}
