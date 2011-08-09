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

package gecv.alg.transform.ii.impl;

import gecv.alg.transform.ii.IntegralKernel;
import gecv.struct.ImageRectangle;
import gecv.struct.image.ImageFloat32;
import gecv.struct.image.ImageSInt32;
import gecv.struct.image.ImageUInt8;


/**
 * <p>
 * Compute the integral image for different types of input images.
 * </p>
 * 
 * <p>
 * DO NOT MODIFY: Generated by {@link GenerateImplIntegralImageOps}.
 * </p>
 * 
 * @author Peter Abeles
 */
public class ImplIntegralImageOps {

	public static void transform( final ImageFloat32 input , final ImageFloat32 transformed )
	{
		int indexSrc = input.startIndex;
		int indexDst = transformed.startIndex;
		int end = indexSrc + input.width;

		float total = 0;
		for( ; indexSrc < end; indexSrc++ ) {
			transformed.data[indexDst++] = total += input.data[indexSrc];
		}

		for( int y = 1; y < input.height; y++ ) {
			indexSrc = input.startIndex + input.stride*y;
			indexDst = transformed.startIndex + transformed.stride*y;
			int indexPrev = indexDst - transformed.stride;

			end = indexSrc + input.width;

			total = 0;
			for( ; indexSrc < end; indexSrc++ ) {
				total +=  input.data[indexSrc];
				transformed.data[indexDst++] = transformed.data[indexPrev++] + total;
			}
		}
	}

	public static void transform( final ImageUInt8 input , final ImageSInt32 transformed )
	{
		int indexSrc = input.startIndex;
		int indexDst = transformed.startIndex;
		int end = indexSrc + input.width;

		int total = 0;
		for( ; indexSrc < end; indexSrc++ ) {
			transformed.data[indexDst++] = total += input.data[indexSrc]& 0xFF;
		}

		for( int y = 1; y < input.height; y++ ) {
			indexSrc = input.startIndex + input.stride*y;
			indexDst = transformed.startIndex + transformed.stride*y;
			int indexPrev = indexDst - transformed.stride;

			end = indexSrc + input.width;

			total = 0;
			for( ; indexSrc < end; indexSrc++ ) {
				total +=  input.data[indexSrc]& 0xFF;
				transformed.data[indexDst++] = transformed.data[indexPrev++] + total;
			}
		}
	}

	public static void transform( final ImageSInt32 input , final ImageSInt32 transformed )
	{
		int indexSrc = input.startIndex;
		int indexDst = transformed.startIndex;
		int end = indexSrc + input.width;

		int total = 0;
		for( ; indexSrc < end; indexSrc++ ) {
			transformed.data[indexDst++] = total += input.data[indexSrc];
		}

		for( int y = 1; y < input.height; y++ ) {
			indexSrc = input.startIndex + input.stride*y;
			indexDst = transformed.startIndex + transformed.stride*y;
			int indexPrev = indexDst - transformed.stride;

			end = indexSrc + input.width;

			total = 0;
			for( ; indexSrc < end; indexSrc++ ) {
				total +=  input.data[indexSrc];
				transformed.data[indexDst++] = transformed.data[indexPrev++] + total;
			}
		}
	}

	public static void convolve( ImageFloat32 integral ,
								 ImageRectangle[] blocks , int scales[],
								 ImageFloat32 output )
	{
		for( int y = 0; y < integral.height; y++ ) {
			for( int x = 0; x < integral.width; x++ ) {
				float total = 0;
				for( int i = 0; i < blocks.length; i++ ) {
					ImageRectangle b = blocks[i];
					total += block_zero(integral,x+b.x0,y+b.y0,x+b.x1,y+b.y1)*scales[i];
				}
				output.set(x,y,total);
			}
		}
	}

	public static void convolveBorder( ImageFloat32 integral ,
									   ImageRectangle[] blocks , int scales[],
									   ImageFloat32 output , int borderX , int borderY )
	{
		for( int x = 0; x < integral.width; x++ ) {
			for( int y = 0; y < borderY; y++ ) {
				float total = 0;
				for( int i = 0; i < blocks.length; i++ ) {
					ImageRectangle b = blocks[i];
					total += block_zero(integral,x+b.x0,y+b.y0,x+b.x1,y+b.y1)*scales[i];
				}
				output.set(x,y,total);
			}
			for( int y = integral.height-borderY; y < integral.height; y++ ) {
				float total = 0;
				for( int i = 0; i < blocks.length; i++ ) {
					ImageRectangle b = blocks[i];
					total += block_zero(integral,x+b.x0,y+b.y0,x+b.x1,y+b.y1)*scales[i];
				}
				output.set(x,y,total);
			}
		}

		int endY = integral.height-borderY;
		for( int y = borderY; y < endY; y++ ) {
			for( int x = 0; x < borderX; x++ ) {
				float total = 0;
				for( int i = 0; i < blocks.length; i++ ) {
					ImageRectangle b = blocks[i];
					total += block_zero(integral,x+b.x0,y+b.y0,x+b.x1,y+b.y1)*scales[i];
				}
				output.set(x,y,total);
			}
			for( int x = integral.width-borderX; x < integral.width; x++ ) {
				float total = 0;
				for( int i = 0; i < blocks.length; i++ ) {
					ImageRectangle b = blocks[i];
					total += block_zero(integral,x+b.x0,y+b.y0,x+b.x1,y+b.y1)*scales[i];
				}
				output.set(x,y,total);
			}
		}
	}

	public static float convolveSparse( ImageFloat32 integral , IntegralKernel kernel , int x , int y )
	{
		float ret = 0;
		int N = kernel.getNumBlocks();

		for( int i = 0; i < N; i++ ) {
			ImageRectangle r = kernel.blocks[i];
			ret += block_zero(integral,x+r.x0,y+r.y0,x+r.x1,y+r.y1)*kernel.scales[i];
		}

		return ret;
	}

	public static float block_unsafe( ImageFloat32 integral , int x0 , int y0 , int x1 , int y1 )
	{
		float br = integral.data[ integral.startIndex + y1*integral.stride + x1 ];
		float tr = integral.data[ integral.startIndex + y0*integral.stride + x1 ];
		float bl = integral.data[ integral.startIndex + y1*integral.stride + x0 ];
		float tl = integral.data[ integral.startIndex + y0*integral.stride + x0 ];

		return br-tr-bl+tl;
	}

	public static float block_zero( ImageFloat32 integral , int x0 , int y0 , int x1 , int y1 )
	{
		x0 = Math.min(x0,integral.width-1);
		y0 = Math.min(y0,integral.height-1);
		x1 = Math.min(x1,integral.width-1);
		y1 = Math.min(y1,integral.height-1);

		float br=0,tr=0,bl=0,tl=0;

		if( x1 >= 0 && y1 >= 0)
			br = integral.data[ integral.startIndex + y1*integral.stride + x1 ];
		if( y0 >= 0 && x1 >= 0)
			tr = integral.data[ integral.startIndex + y0*integral.stride + x1 ];
		if( x0 >= 0 && y1 >= 0)
			bl = integral.data[ integral.startIndex + y1*integral.stride + x0 ];
		if( x0 >= 0 && y0 >= 0)
			tl = integral.data[ integral.startIndex + y0*integral.stride + x0 ];

		return br-tr-bl+tl;
	}

	public static void convolve( ImageSInt32 integral ,
								 ImageRectangle[] blocks , int scales[],
								 ImageSInt32 output )
	{
		for( int y = 0; y < integral.height; y++ ) {
			for( int x = 0; x < integral.width; x++ ) {
				int total = 0;
				for( int i = 0; i < blocks.length; i++ ) {
					ImageRectangle b = blocks[i];
					total += block_zero(integral,x+b.x0,y+b.y0,x+b.x1,y+b.y1)*scales[i];
				}
				output.set(x,y,total);
			}
		}
	}

	public static void convolveBorder( ImageSInt32 integral ,
									   ImageRectangle[] blocks , int scales[],
									   ImageSInt32 output , int borderX , int borderY )
	{
		for( int x = 0; x < integral.width; x++ ) {
			for( int y = 0; y < borderY; y++ ) {
				int total = 0;
				for( int i = 0; i < blocks.length; i++ ) {
					ImageRectangle b = blocks[i];
					total += block_zero(integral,x+b.x0,y+b.y0,x+b.x1,y+b.y1)*scales[i];
				}
				output.set(x,y,total);
			}
			for( int y = integral.height-borderY; y < integral.height; y++ ) {
				int total = 0;
				for( int i = 0; i < blocks.length; i++ ) {
					ImageRectangle b = blocks[i];
					total += block_zero(integral,x+b.x0,y+b.y0,x+b.x1,y+b.y1)*scales[i];
				}
				output.set(x,y,total);
			}
		}

		int endY = integral.height-borderY;
		for( int y = borderY; y < endY; y++ ) {
			for( int x = 0; x < borderX; x++ ) {
				int total = 0;
				for( int i = 0; i < blocks.length; i++ ) {
					ImageRectangle b = blocks[i];
					total += block_zero(integral,x+b.x0,y+b.y0,x+b.x1,y+b.y1)*scales[i];
				}
				output.set(x,y,total);
			}
			for( int x = integral.width-borderX; x < integral.width; x++ ) {
				int total = 0;
				for( int i = 0; i < blocks.length; i++ ) {
					ImageRectangle b = blocks[i];
					total += block_zero(integral,x+b.x0,y+b.y0,x+b.x1,y+b.y1)*scales[i];
				}
				output.set(x,y,total);
			}
		}
	}

	public static int convolveSparse( ImageSInt32 integral , IntegralKernel kernel , int x , int y )
	{
		int ret = 0;
		int N = kernel.getNumBlocks();

		for( int i = 0; i < N; i++ ) {
			ImageRectangle r = kernel.blocks[i];
			ret += block_zero(integral,x+r.x0,y+r.y0,x+r.x1,y+r.y1)*kernel.scales[i];
		}

		return ret;
	}

	public static int block_unsafe( ImageSInt32 integral , int x0 , int y0 , int x1 , int y1 )
	{
		int br = integral.data[ integral.startIndex + y1*integral.stride + x1 ];
		int tr = integral.data[ integral.startIndex + y0*integral.stride + x1 ];
		int bl = integral.data[ integral.startIndex + y1*integral.stride + x0 ];
		int tl = integral.data[ integral.startIndex + y0*integral.stride + x0 ];

		return br-tr-bl+tl;
	}

	public static int block_zero( ImageSInt32 integral , int x0 , int y0 , int x1 , int y1 )
	{
		x0 = Math.min(x0,integral.width-1);
		y0 = Math.min(y0,integral.height-1);
		x1 = Math.min(x1,integral.width-1);
		y1 = Math.min(y1,integral.height-1);

		int br=0,tr=0,bl=0,tl=0;

		if( x1 >= 0 && y1 >= 0)
			br = integral.data[ integral.startIndex + y1*integral.stride + x1 ];
		if( y0 >= 0 && x1 >= 0)
			tr = integral.data[ integral.startIndex + y0*integral.stride + x1 ];
		if( x0 >= 0 && y1 >= 0)
			bl = integral.data[ integral.startIndex + y1*integral.stride + x0 ];
		if( x0 >= 0 && y0 >= 0)
			tl = integral.data[ integral.startIndex + y0*integral.stride + x0 ];

		return br-tr-bl+tl;
	}


}
