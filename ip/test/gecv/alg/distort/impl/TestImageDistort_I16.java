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

package gecv.alg.distort.impl;

import gecv.alg.distort.ImageDistort;
import gecv.alg.interpolate.InterpolatePixel;
import gecv.struct.distort.PixelDistort;
import gecv.struct.image.ImageSInt16;


/**
 * @author Peter Abeles
 */
public class TestImageDistort_I16 extends GeneralImageDistortTests<ImageSInt16>{

	public TestImageDistort_I16() {
		super(ImageSInt16.class);
	}

	@Override
	public ImageDistort<ImageSInt16> createDistort(PixelDistort dstToSrc, InterpolatePixel<ImageSInt16> interp) {
		return new ImageDistort_I16<ImageSInt16>(dstToSrc,interp);
	}
}