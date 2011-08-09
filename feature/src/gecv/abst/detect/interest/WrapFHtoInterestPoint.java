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

package gecv.abst.detect.interest;

import gecv.alg.detect.interest.FastHessianFeatureDetector;
import gecv.alg.detect.interest.ScalePoint;
import gecv.alg.transform.ii.IntegralImageOps;
import gecv.struct.image.ImageBase;
import gecv.struct.image.ImageFloat32;
import jgrl.struct.point.Point2D_I32;

import java.util.List;


/**
 * Wrapper around {@link gecv.alg.detect.interest.FastHessianFeatureDetector} for {@link InterestPointDetector}.
 *
 * @author Peter Abeles
 */
public class WrapFHtoInterestPoint<T extends ImageBase> implements InterestPointDetector<T>{

	FastHessianFeatureDetector detector;
	List<ScalePoint> location;
	ImageFloat32 integral;

	public WrapFHtoInterestPoint(FastHessianFeatureDetector detector) {
		this.detector = detector;
	}

	@Override
	public void detect(T input) {
		integral = IntegralImageOps.transform((ImageFloat32)input,integral);

		detector.detect(integral);

		location = detector.getFoundPoints();
	}

	@Override
	public int getNumberOfFeatures() {
		return location.size();
	}

	@Override
	public Point2D_I32 getLocation(int featureIndex) {
		return location.get(featureIndex);
	}

	@Override
	public double getScale(int featureIndex) {
		return location.get(featureIndex).scale;
	}

	@Override
	public double getOrientation(int featureIndex) {
		throw new IllegalArgumentException("Not supported");
	}

	@Override
	public boolean hasScale() {
		return true;
	}

	@Override
	public boolean hasOrientation() {
		return false;
	}
}
