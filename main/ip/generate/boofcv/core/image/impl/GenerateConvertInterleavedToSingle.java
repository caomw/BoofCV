/*
 * Copyright (c) 2011-2016, Peter Abeles. All Rights Reserved.
 *
 * This file is part of BoofCV (http://boofcv.org).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package boofcv.core.image.impl;

import boofcv.misc.AutoTypeImage;
import boofcv.misc.CodeGeneratorBase;

import java.io.FileNotFoundException;

/**
 * @author Peter Abeles
 */
public class GenerateConvertInterleavedToSingle extends CodeGeneratorBase {

	String className = "ConvertInterleavedToSingle";


	public GenerateConvertInterleavedToSingle() throws FileNotFoundException {
		setOutputFile(className);
	}

	@Override
	public void generate() throws FileNotFoundException {
		printPreamble();

		for( AutoTypeImage in : AutoTypeImage.getSpecificTypes()) {
			printAverage(in);
		}

		out.print("\n" +
				"}\n");
	}

	private void printPreamble() {
		out.print("import boofcv.struct.image.*;\n" +
				"\n" +
				"/**\n" +
				" * Low level implementations of different methods for converting {@link boofcv.struct.image.ImageInterleaved} into\n" +
				" * {@link boofcv.struct.image.ImageGray}.\n" +
				" *\n" +
				" * <ul>\n" +
				" * <li>Average computes the average value of each pixel across the bands.\n" +
				" * </ul>\n" +
				" *\n" +
				" * <p>\n" +
				" * DO NOT MODIFY: This class was automatically generated by {@link "+getClass().getName()+"}\n" +
				" * </p>\n" +
				" *\n" +
				" * @author Peter Abeles\n" +
				" */\n" +
				"public class "+className+" {\n\n");
	}

	private void printAverage( AutoTypeImage in ) {

		String outputType = in.getSingleBandName();
		String inputType = in.getInterleavedName();
		String sumType = in.getSumType();
		String typecast = in.getTypeCastFromSum();
		String bitwise = in.getBitWise();

		out.print("\tpublic static void average( "+inputType+" from , "+outputType+" to ) {\n" +
				"\t\tfinal int numBands = from.getNumBands();\n" +
				"\n" +
				"\t\tif( numBands == 1 ) {\n" +
				"\t\t\tfor (int y = 0; y < from.height; y++) {\n" +
				"\t\t\t\tint indexFrom = from.getIndex(0, y);\n" +
				"\t\t\t\tint indexTo = to.getIndex(0, y);\n" +
				"\t\t\t\tSystem.arraycopy(from.data,indexFrom,to.data,indexTo,from.width);\n" +
				"\t\t\t}\n" +
				"\t\t} else {\n" +
				"\t\t\tfor (int y = 0; y < from.height; y++) {\n" +
				"\t\t\t\tint indexFrom = from.getIndex(0, y);\n" +
				"\t\t\t\tint indexTo = to.getIndex(0, y);\n" +
				"\n" +
				"\t\t\t\tfor (int x = 0; x < from.width; x++ ) {\n" +
				"\t\t\t\t\t"+sumType+" sum = 0;\n" +
				"\t\t\t\t\tint indexFromEnd = indexFrom + numBands;\n" +
				"\t\t\t\t\twhile( indexFrom < indexFromEnd ) {\n" +
				"\t\t\t\t\t\tsum += from.data[indexFrom++]"+bitwise+";\n" +
				"\t\t\t\t\t}\n" +
				"\t\t\t\t\tto.data[indexTo++] = "+typecast+"(sum/numBands);\n" +
				"\t\t\t\t}\n" +
				"\t\t\t}\n" +
				"\t\t}\n" +
				"\t}\n\n");
	}

	public static void main( String args[] ) throws FileNotFoundException {
		GenerateConvertInterleavedToSingle app = new GenerateConvertInterleavedToSingle();

		app.generate();
	}
}
