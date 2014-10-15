/*
 * Surface_ep.cpp
 *
 *  Created on: 24 нояб. 2013 г.
 *      Author: caiiiycuk
 */

#include "Surface_ep.h"

/*
 * Surface_ep.h
 *
 *  Created on: 23 окт. 2013 г.
 *      Author: caiiiycuk
 */

#include <assert.h>
#include <stdlib.h>
#include <iostream>
#include "Logger.h"
#include "../lodepng.h"

namespace OpenXcom {
	extern void* NewAligned(int bpp, int width, int height);
	extern int GetPitch(int bpp, int width);
}

static int *gfxPrimitivesPolyIntsGlobal = NULL;
static int gfxPrimitivesPolyAllocatedGlobal = 0;


int _gfxPrimitivesCompareInt(const void *a, const void *b)
{
	return (*(const int *) a) - (*(const int *) b);
}

Uint8 ep_SDL_FindColor(SDL_Palette *pal, const SDL_Color &color)
{
	/* Do colorspace distance matching */
	unsigned int smallest;
	unsigned int distance;
	int rd, gd, bd;
	int i;
	Uint8 pixel=0;

	smallest = ~0;
	for ( i=0; i<pal->ncolors; ++i ) {
		rd = pal->colors[i].r - color.r;
		gd = pal->colors[i].g - color.g;
		bd = pal->colors[i].b - color.b;
		distance = (rd*rd)+(gd*gd)+(bd*bd);
		if ( distance < smallest ) {
			pixel = i;
			if ( distance == 0 ) { /* Perfect match! */
				break;
			}
			smallest = distance;
		}
	}
	return(pixel);
}

/* Helper functions */
/*
 * Allocate a pixel format structure and fill it according to the given info.
 */
SDL_PixelFormat *ep_SDL_AllocFormat() {
	SDL_PixelFormat *format;

	/* Allocate an empty pixel format structure */
	format = (SDL_PixelFormat*) malloc(sizeof(*format));
	memset(format, 0, sizeof(*format));

#ifndef EMSCRIPTEN
	format->alpha = SDL_ALPHA_OPAQUE;
#endif

	/* Set up the format */
	format->BitsPerPixel = 8;
	format->BytesPerPixel = 1;
	format->Rloss = 8;
	format->Gloss = 8;
	format->Bloss = 8;
	format->Aloss = 8;
	format->Rshift = 0;
	format->Gshift = 0;
	format->Bshift = 0;
	format->Ashift = 0;
	format->Rmask = 0;
	format->Gmask = 0;
	format->Bmask = 0;
	format->Amask = 0;
	format->palette = (SDL_Palette *) malloc(sizeof(SDL_Palette));
	(format->palette)->ncolors = 256;
	(format->palette)->colors = (SDL_Color *) malloc(
			(format->palette)->ncolors * sizeof(SDL_Color));
	memset((format->palette)->colors, 0,
			(format->palette)->ncolors * sizeof(SDL_Color));
	return (format);
}

bool ep_SDL_IntersectRect(const SDL_Rect *A, const SDL_Rect *B,
		SDL_Rect *intersection) {
	int Amin, Amax, Bmin, Bmax;

	/* Horizontal intersection */
	Amin = A->x;
	Amax = Amin + A->w;
	Bmin = B->x;
	Bmax = Bmin + B->w;
	if (Bmin > Amin)
		Amin = Bmin;
	intersection->x = Amin;
	if (Bmax < Amax)
		Amax = Bmax;
	intersection->w = Amax - Amin > 0 ? Amax - Amin : 0;

	/* Vertical intersection */
	Amin = A->y;
	Amax = Amin + A->h;
	Bmin = B->y;
	Bmax = Bmin + B->h;
	if (Bmin > Amin)
		Amin = Bmin;
	intersection->y = Amin;
	if (Bmax < Amax)
		Amax = Bmax;
	intersection->h = Amax - Amin > 0 ? Amax - Amin : 0;

	return (intersection->w && intersection->h);
}

SDL_Surface * ep_SDL_CreateRGBSurfaceFrom(void *pixels, int width, int height,
		int depth, int pitch, Uint32 Rmask, Uint32 Gmask, Uint32 Bmask,
		Uint32 Amask) {
//	return SDL_CreateRGBSurfaceFrom(pixels, width, height, depth, pitch, Rmask,
//			Gmask, Bmask, Amask);

	assert(!(Rmask || Gmask || Bmask || Amask));
	assert(depth == 8);

	SDL_Surface *surface = (SDL_Surface*) malloc(sizeof(SDL_Surface));
	surface->flags = SDL_SWSURFACE | SDL_PREALLOC;
	surface->pixels = pixels;
	surface->w = width;
	surface->h = height;
	surface->pitch = pitch;
	surface->format = ep_SDL_AllocFormat();
	surface->locked = 0;
	surface->map = 0;
	surface->clip_rect.x = 0;
	surface->clip_rect.y = 0;
	surface->clip_rect.w = width;
	surface->clip_rect.h = height;

#ifndef EMSCRIPTEN
	surface->offset = 0;
	surface->hwdata = NULL;
	surface->unused1 = 0;
#endif


	return surface;
}

void ep_SDL_FreeSurface(SDL_Surface *surface) {
//	SDL_FreeSurface(surface);
//	return;
	free(surface->format);
	free(surface);
}

int ep_SDL_FillRect(SDL_Surface *dst, SDL_Rect *dstrect, Uint8 color) {
	if (dstrect) {
		if (!ep_SDL_IntersectRect(dstrect, &dst->clip_rect, dstrect)) {
			return 0;
		}
	} else {
		dstrect = &dst->clip_rect;
	}

	Uint8 *row = (Uint8*) dst->pixels + dstrect->y * dst->pitch
			+ dstrect->x * dst->format->BytesPerPixel;

	for (int y = dstrect->h; y; --y) {
		memset(row, color, dstrect->w * sizeof(Uint8));
		row += dst->pitch;
	}

	return 0;
}

int ep_SDL_LowerBlit (SDL_Surface *src, SDL_Rect *srcrect,
				SDL_Surface *dst, SDL_Rect *dstrect) {
	assert(srcrect->w == dstrect->w && srcrect->h == dstrect->h);

	Uint8 *srcrow = (Uint8*) src->pixels + srcrect->y * src->pitch
					+ srcrect->x * src->format->BytesPerPixel;
	Uint8 *dstrow = (Uint8*) dst->pixels + dstrect->y * dst->pitch
				+ dstrect->x * dst->format->BytesPerPixel;

	Uint8 blitmap[256] = {0};
	SDL_Color *srcColors = src->format->palette->colors;
	SDL_Color *dstColors = dst->format->palette->colors;
	SDL_Palette *dstPalette = dst->format->palette;

	for (int y = dstrect->h; y; --y) {
		Uint8 *srcColorKey = srcrow;
		Uint8 *dstColorKey = dstrow;

		for (int x = dstrect->w; x; --x) {
			if (*srcColorKey != 0) { //TRANSPARENCY KEY
				if (blitmap[*srcColorKey] == 0) { // NOT INITIALZIED
					SDL_Color srcColor = srcColors[*srcColorKey];
					SDL_Color dstColor = dstColors[*srcColorKey];
					if (srcColor.r == dstColor.r &&
							srcColor.g == dstColor.g &&
							srcColor.b == dstColor.b) {
						blitmap[*srcColorKey] = *srcColorKey;
					} else {
						blitmap[*srcColorKey] = ep_SDL_FindColor(dstPalette, srcColor);
					}
				}

				*dstColorKey = blitmap[*srcColorKey];
			}
			srcColorKey++;
			dstColorKey++;
		}

		srcrow += src->pitch;
		dstrow += dst->pitch;
	}

	return 0;
}


int ep_SDL_UpperBlit(SDL_Surface *src, SDL_Rect *srcrect, SDL_Surface *dst,
		SDL_Rect *dstrect) {
	SDL_Rect fulldst;
	int srcx, srcy, w, h;

	/* Make sure the surfaces aren't locked */
	if (!src || !dst) {
		using namespace OpenXcom;
		Log(LOG_ERROR) << "SDL_UpperBlit: passed a NULL surface";
		return (-1);
	}
	if (src->locked || dst->locked) {
		using namespace OpenXcom;
		Log(LOG_ERROR) << "Surfaces must not be locked during blit";
		return (-1);
	}

	/* If the destination rectangle is NULL, use the entire dest surface */
	if (dstrect == NULL) {
		fulldst.x = fulldst.y = 0;
		dstrect = &fulldst;
	}

	/* clip the source rectangle to the source surface */
	if (srcrect) {
		int maxw, maxh;

		srcx = srcrect->x;
		w = srcrect->w;
		if (srcx < 0) {
			w += srcx;
			dstrect->x -= srcx;
			srcx = 0;
		}
		maxw = src->w - srcx;
		if (maxw < w)
			w = maxw;

		srcy = srcrect->y;
		h = srcrect->h;
		if (srcy < 0) {
			h += srcy;
			dstrect->y -= srcy;
			srcy = 0;
		}
		maxh = src->h - srcy;
		if (maxh < h)
			h = maxh;

	} else {
		srcx = srcy = 0;
		w = src->w;
		h = src->h;
	}

	/* clip the destination rectangle against the clip rectangle */
	{
		SDL_Rect *clip = &dst->clip_rect;
		int dx, dy;

		dx = clip->x - dstrect->x;
		if (dx > 0) {
			w -= dx;
			dstrect->x += dx;
			srcx += dx;
		}
		dx = dstrect->x + w - clip->x - clip->w;
		if (dx > 0)
			w -= dx;

		dy = clip->y - dstrect->y;
		if (dy > 0) {
			h -= dy;
			dstrect->y += dy;
			srcy += dy;
		}
		dy = dstrect->y + h - clip->y - clip->h;
		if (dy > 0)
			h -= dy;
	}

	if (w > 0 && h > 0) {
		SDL_Rect sr;
		sr.x = srcx;
		sr.y = srcy;
		sr.w = dstrect->w = w;
		sr.h = dstrect->h = h;
		return ep_SDL_LowerBlit(src, &sr, dst, dstrect);
	}
	dstrect->w = dstrect->h = 0;
	return 0;
}

int ep_SDL_BlitSurface(SDL_Surface *src, SDL_Rect *srcrect, SDL_Surface *dst,
		SDL_Rect *dstrect) {
//	return SDL_UpperBlit(src, srcrect, dst, dstrect);
	return ep_SDL_UpperBlit(src, srcrect, dst, dstrect);
}

int ep_SDL_SetPalette(SDL_Surface *screen, int which,
		   SDL_Color *colors, int firstcolor, int ncolors) {
//	return SDL_SetPalette(screen, which, colors, firstcolor, ncolors);
	SDL_Palette *pal = screen->format->palette;

	if ( colors != (pal->colors + firstcolor) ) {
		memcpy(pal->colors + firstcolor, colors,
		       ncolors * sizeof(*colors));
	}

	return 1;
}

int ep_SDL_SetColors(SDL_Surface *surface, SDL_Color *colors, int firstcolor, int ncolors) {
	return ep_SDL_SetPalette(surface, SDL_LOGPAL | SDL_PHYSPAL,
		      colors, firstcolor, ncolors);
}

int ep_HLineTextured(SDL_Surface * dst, Sint16 x1, Sint16 x2, Sint16 y, SDL_Surface *texture, int texture_dx, int texture_dy)
{
	Sint16 left, right, top, bottom;
	Sint16 w;
	Sint16 xtmp;
	int result = 0;
	int texture_x_walker;
	int texture_y_start;
	SDL_Rect source_rect,dst_rect;
	int pixels_written,write_width;

	/*
	* Check visibility of clipping rectangle
	*/
	if ((dst->clip_rect.w==0) || (dst->clip_rect.h==0)) {
		return(0);
	}

	/*
	* Swap x1, x2 if required to ensure x1<=x2
	*/
	if (x1 > x2) {
		xtmp = x1;
		x1 = x2;
		x2 = xtmp;
	}

	/*
	* Get clipping boundary and
	* check visibility of hline
	*/
	left = dst->clip_rect.x;
	if (x2<left) {
		return(0);
	}
	right = dst->clip_rect.x + dst->clip_rect.w - 1;
	if (x1>right) {
		return(0);
	}
	top = dst->clip_rect.y;
	bottom = dst->clip_rect.y + dst->clip_rect.h - 1;
	if ((y<top) || (y>bottom)) {
		return (0);
	}

	/*
	* Clip x
	*/
	if (x1 < left) {
		x1 = left;
	}
	if (x2 > right) {
		x2 = right;
	}

	/*
	* Calculate width to draw
	*/
	w = x2 - x1 + 1;

	/*
	* Determine where in the texture we start drawing
	*/
	texture_x_walker =   (x1 - texture_dx)  % texture->w;
	if (texture_x_walker < 0){
		texture_x_walker = texture->w + texture_x_walker ;
	}

	texture_y_start = (y + texture_dy) % texture->h;
	if (texture_y_start < 0){
		texture_y_start = texture->h + texture_y_start;
	}

	// setup the source rectangle; we are only drawing one horizontal line
	source_rect.y = texture_y_start;
	source_rect.x = texture_x_walker;
	source_rect.h = 1;

	// we will draw to the current y
	dst_rect.y = y;

	// if there are enough pixels left in the current row of the texture
	// draw it all at once
	if (w <= texture->w -texture_x_walker){
		source_rect.w = w;
		source_rect.x = texture_x_walker;
		dst_rect.x= x1;
		result = (ep_SDL_BlitSurface  (texture, &source_rect , dst, &dst_rect) == 0);
	} else { // we need to draw multiple times
		// draw the first segment
		pixels_written = texture->w  - texture_x_walker;
		source_rect.w = pixels_written;
		source_rect.x = texture_x_walker;
		dst_rect.x= x1;
		result |= (ep_SDL_BlitSurface (texture, &source_rect , dst, &dst_rect) == 0);
		write_width = texture->w;

		// now draw the rest
		// set the source x to 0
		source_rect.x = 0;
		while (pixels_written < w){
			if (write_width >= w - pixels_written) {
				write_width =  w - pixels_written;
			}
			source_rect.w = write_width;
			dst_rect.x = x1 + pixels_written;
			result  |= (ep_SDL_BlitSurface  (texture,&source_rect , dst, &dst_rect) == 0);
			pixels_written += write_width;
		}
	}

	return result;
}

int ep_texturedPolygonMT(SDL_Surface * dst, const Sint16 * vx, const Sint16 * vy, int n,
	SDL_Surface * texture, int texture_dx, int texture_dy, int **polyInts, int *polyAllocated)
{
	int result;
	int i;
	int y, xa, xb;
	int minx,maxx,miny, maxy;
	int x1, y1;
	int x2, y2;
	int ind1, ind2;
	int ints;
	int *gfxPrimitivesPolyInts = NULL;
	int gfxPrimitivesPolyAllocated = 0;

	/*
	* Check visibility of clipping rectangle
	*/
	if ((dst->clip_rect.w==0) || (dst->clip_rect.h==0)) {
		return(0);
	}

	/*
	* Sanity check number of edges
	*/
	if (n < 3) {
		return -1;
	}

	/*
	* Map polygon cache
	*/
	if ((polyInts==NULL) || (polyAllocated==NULL)) {
		/* Use global cache */
		gfxPrimitivesPolyInts = gfxPrimitivesPolyIntsGlobal;
		gfxPrimitivesPolyAllocated = gfxPrimitivesPolyAllocatedGlobal;
	} else {
		/* Use local cache */
		gfxPrimitivesPolyInts = *polyInts;
		gfxPrimitivesPolyAllocated = *polyAllocated;
	}

	/*
	* Allocate temp array, only grow array
	*/
	if (!gfxPrimitivesPolyAllocated) {
		gfxPrimitivesPolyInts = (int *) malloc(sizeof(int) * n);
		gfxPrimitivesPolyAllocated = n;
	} else {
		if (gfxPrimitivesPolyAllocated < n) {
			gfxPrimitivesPolyInts = (int *) realloc(gfxPrimitivesPolyInts, sizeof(int) * n);
			gfxPrimitivesPolyAllocated = n;
		}
	}

	/*
	* Check temp array
	*/
	if (gfxPrimitivesPolyInts==NULL) {
		gfxPrimitivesPolyAllocated = 0;
	}

	/*
	* Update cache variables
	*/
	if ((polyInts==NULL) || (polyAllocated==NULL)) {
		gfxPrimitivesPolyIntsGlobal =  gfxPrimitivesPolyInts;
		gfxPrimitivesPolyAllocatedGlobal = gfxPrimitivesPolyAllocated;
	} else {
		*polyInts = gfxPrimitivesPolyInts;
		*polyAllocated = gfxPrimitivesPolyAllocated;
	}

	/*
	* Check temp array again
	*/
	if (gfxPrimitivesPolyInts==NULL) {
		return(-1);
	}

	/*
	* Determine X,Y minima,maxima
	*/
	miny = vy[0];
	maxy = vy[0];
	minx = vx[0];
	maxx = vx[0];
	for (i = 1; (i < n); i++) {
		if (vy[i] < miny) {
			miny = vy[i];
		} else if (vy[i] > maxy) {
			maxy = vy[i];
		}
		if (vx[i] < minx) {
			minx = vx[i];
		} else if (vx[i] > maxx) {
			maxx = vx[i];
		}
	}
	if (maxx <0 || minx > dst->w){
		return -1;
	}
	if (maxy <0 || miny > dst->h){
		return -1;
	}

	/*
	* Draw, scanning y
	*/
	result = 0;
	for (y = miny; (y <= maxy); y++) {
		ints = 0;
		for (i = 0; (i < n); i++) {
			if (!i) {
				ind1 = n - 1;
				ind2 = 0;
			} else {
				ind1 = i - 1;
				ind2 = i;
			}
			y1 = vy[ind1];
			y2 = vy[ind2];
			if (y1 < y2) {
				x1 = vx[ind1];
				x2 = vx[ind2];
			} else if (y1 > y2) {
				y2 = vy[ind1];
				y1 = vy[ind2];
				x2 = vx[ind1];
				x1 = vx[ind2];
			} else {
				continue;
			}
			if ( ((y >= y1) && (y < y2)) || ((y == maxy) && (y > y1) && (y <= y2)) ) {
				gfxPrimitivesPolyInts[ints++] = ((65536 * (y - y1)) / (y2 - y1)) * (x2 - x1) + (65536 * x1);
			}
		}

		qsort(gfxPrimitivesPolyInts, ints, sizeof(int), _gfxPrimitivesCompareInt);

		for (i = 0; (i < ints); i += 2) {
			xa = gfxPrimitivesPolyInts[i] + 1;
			xa = (xa >> 16) + ((xa & 32768) >> 15);
			xb = gfxPrimitivesPolyInts[i+1] - 1;
			xb = (xb >> 16) + ((xb & 32768) >> 15);
			result |= ep_HLineTextured(dst, xa, xb, y, texture, texture_dx, texture_dy);
		}
	}

	return (result);
}

int ep_texturedPolygon(SDL_Surface * dst, const Sint16 * vx, const Sint16 * vy, int n, SDL_Surface *texture, int texture_dx, int texture_dy) {
	return (ep_texturedPolygonMT(dst, vx, vy, n, texture, texture_dx, texture_dy, NULL, NULL));
}

int ep_hlineColor(SDL_Surface * dst, Sint16 x1, Sint16 x2, Sint16 y, Uint8 color)
{
	Sint16 left, right, top, bottom;
	Uint8 *pixel;
	int dx;
	int pixx, pixy;
	Sint16 xtmp;

	/*
	* Check visibility of clipping rectangle
	*/
	if ((dst->clip_rect.w==0) || (dst->clip_rect.h==0)) {
		return(0);
	}

	/*
	* Swap x1, x2 if required to ensure x1<=x2
	*/
	if (x1 > x2) {
		xtmp = x1;
		x1 = x2;
		x2 = xtmp;
	}

	/*
	* Get clipping boundary and
	* check visibility of hline
	*/
	left = dst->clip_rect.x;
	if (x2<left) {
		return(0);
	}
	right = dst->clip_rect.x + dst->clip_rect.w - 1;
	if (x1>right) {
		return(0);
	}
	top = dst->clip_rect.y;
	bottom = dst->clip_rect.y + dst->clip_rect.h - 1;
	if ((y<top) || (y>bottom)) {
		return (0);
	}

	/*
	* Clip x
	*/
	if (x1 < left) {
		x1 = left;
	}
	if (x2 > right) {
		x2 = right;
	}

	/*
	* Calculate width difference
	*/
	dx = x2 - x1;

	/*
	* More variable setup
	*/
	pixx = dst->format->BytesPerPixel;
	pixy = dst->pitch;
	pixel = ((Uint8 *) dst->pixels) + pixx * (int) x1 + pixy * (int) y;

	/*
	* Draw
	*/
	assert(dst->format->BytesPerPixel == 1);
	memset(pixel, color, dx + 1);

	return 0;
}

int ep_pixelColor(SDL_Surface * dst, Sint16 x, Sint16 y, Uint8 color)
{
	Uint8 *row = (Uint8*) dst->pixels + y * dst->pitch
				+ x * dst->format->BytesPerPixel;
	*row = color;
	return 0;
}

int ep_filledCircleColor(SDL_Surface * dst, Sint16 x, Sint16 y, Sint16 rad, Uint8 color)
{
	Sint16 left, right, top, bottom;
	int result;
	Sint16 x1, y1, x2, y2;
	Sint16 cx = 0;
	Sint16 cy = rad;
	Sint16 ocx = (Sint16) 0xffff;
	Sint16 ocy = (Sint16) 0xffff;
	Sint16 df = 1 - rad;
	Sint16 d_e = 3;
	Sint16 d_se = -2 * rad + 5;
	Sint16 xpcx, xmcx, xpcy, xmcy;
	Sint16 ypcy, ymcy, ypcx, ymcx;

	/*
	* Check visibility of clipping rectangle
	*/
	if ((dst->clip_rect.w==0) || (dst->clip_rect.h==0)) {
		return(0);
	}

	/*
	* Sanity check radius
	*/
	if (rad < 0) {
		return (-1);
	}

	/*
	* Special case for rad=0 - draw a point
	*/
	if (rad == 0) {
		return (ep_pixelColor(dst, x, y, color));
	}

	/*
	* Get circle and clipping boundary and
	* test if bounding box of circle is visible
	*/
	x2 = x + rad;
	left = dst->clip_rect.x;
	if (x2<left) {
		return(0);
	}
	x1 = x - rad;
	right = dst->clip_rect.x + dst->clip_rect.w - 1;
	if (x1>right) {
		return(0);
	}
	y2 = y + rad;
	top = dst->clip_rect.y;
	if (y2<top) {
		return(0);
	}
	y1 = y - rad;
	bottom = dst->clip_rect.y + dst->clip_rect.h - 1;
	if (y1>bottom) {
		return(0);
	}

	/*
	* Draw
	*/
	result = 0;
	do {
		xpcx = x + cx;
		xmcx = x - cx;
		xpcy = x + cy;
		xmcy = x - cy;
		if (ocy != cy) {
			if (cy > 0) {
				ypcy = y + cy;
				ymcy = y - cy;
				result |= ep_hlineColor(dst, xmcx, xpcx, ypcy, color);
				result |= ep_hlineColor(dst, xmcx, xpcx, ymcy, color);
			} else {
				result |= ep_hlineColor(dst, xmcx, xpcx, y, color);
			}
			ocy = cy;
		}
		if (ocx != cx) {
			if (cx != cy) {
				if (cx > 0) {
					ypcx = y + cx;
					ymcx = y - cx;
					result |= ep_hlineColor(dst, xmcy, xpcy, ymcx, color);
					result |= ep_hlineColor(dst, xmcy, xpcy, ypcx, color);
				} else {
					result |= ep_hlineColor(dst, xmcy, xpcy, y, color);
				}
			}
			ocx = cx;
		}
		/*
		* Update
		*/
		if (df < 0) {
			df += d_e;
			d_e += 2;
			d_se += 2;
		} else {
			df += d_se;
			d_e += 2;
			d_se += 4;
			cy--;
		}
		cx++;
	} while (cx <= cy);

	return (result);
}

int ep_vlineColor(SDL_Surface * dst, Sint16 x, Sint16 y1, Sint16 y2, Uint8 color)
{
	Sint16 left, right, top, bottom;
	Uint8 *pixel, *pixellast;
	int dy;
	int pixx, pixy;
	Sint16 h;
	Sint16 ytmp;

	/*
	* Check visibility of clipping rectangle
	*/
	if ((dst->clip_rect.w==0) || (dst->clip_rect.h==0)) {
		return(0);
	}

	/*
	* Swap y1, y2 if required to ensure y1<=y2
	*/
	if (y1 > y2) {
		ytmp = y1;
		y1 = y2;
		y2 = ytmp;
	}

	/*
	* Get clipping boundary and
	* check visibility of vline
	*/
	left = dst->clip_rect.x;
	right = dst->clip_rect.x + dst->clip_rect.w - 1;
	if ((x<left) || (x>right)) {
		return (0);
	}
	top = dst->clip_rect.y;
	if (y2<top) {
		return(0);
	}
	bottom = dst->clip_rect.y + dst->clip_rect.h - 1;
	if (y1>bottom) {
		return(0);
	}

	/*
	* Clip x
	*/
	if (y1 < top) {
		y1 = top;
	}
	if (y2 > bottom) {
		y2 = bottom;
	}

	/*
	* Calculate height
	*/
	h = y2 - y1;

	/*
	* More variable setup
	*/
	dy = h;
	pixx = dst->format->BytesPerPixel;
	pixy = dst->pitch;
	pixel = ((Uint8 *) dst->pixels) + pixx * (int) x + pixy * (int) y1;
	pixellast = pixel + pixy * dy;

	/*
	* Draw
	*/
	assert(dst->format->BytesPerPixel == 1);
	for (; pixel <= pixellast; pixel += pixy) {
		*(Uint8 *) pixel = color;
	}

	return 0;
}

/* --------- Clipping routines for line */

/* Clipping based heavily on code from                       */
/* http://www.ncsa.uiuc.edu/Vis/Graphics/src/clipCohSuth.c   */

#define CLIP_LEFT_EDGE   0x1
#define CLIP_RIGHT_EDGE  0x2
#define CLIP_BOTTOM_EDGE 0x4
#define CLIP_TOP_EDGE    0x8
#define CLIP_INSIDE(a)   (!a)
#define CLIP_REJECT(a,b) (a&b)
#define CLIP_ACCEPT(a,b) (!(a|b))

/*!
\brief Internal clip-encoding routine.

Calculates a segement-based clipping encoding for a point against a rectangle.

\param x X coordinate of point.
\param y Y coordinate of point.
\param left X coordinate of left edge of the rectangle.
\param top Y coordinate of top edge of the rectangle.
\param right X coordinate of right edge of the rectangle.
\param bottom Y coordinate of bottom edge of the rectangle.
*/
int ep_clipEncode(Sint16 x, Sint16 y, Sint16 left, Sint16 top, Sint16 right, Sint16 bottom)
{
	int code = 0;

	if (x < left) {
		code |= CLIP_LEFT_EDGE;
	} else if (x > right) {
		code |= CLIP_RIGHT_EDGE;
	}
	if (y < top) {
		code |= CLIP_TOP_EDGE;
	} else if (y > bottom) {
		code |= CLIP_BOTTOM_EDGE;
	}
	return code;
}

/*!
\brief Clip line to a the clipping rectangle of a surface.

\param dst Target surface to draw on.
\param x1 Pointer to X coordinate of first point of line.
\param y1 Pointer to Y coordinate of first point of line.
\param x2 Pointer to X coordinate of second point of line.
\param y2 Pointer to Y coordinate of second point of line.
*/
int ep_clipLine(SDL_Surface * dst, Sint16 * x1, Sint16 * y1, Sint16 * x2, Sint16 * y2)
{
	Sint16 left, right, top, bottom;
	int code1, code2;
	int draw = 0;
	Sint16 swaptmp;
	float m;

	/*
	* Get clipping boundary
	*/
	left = dst->clip_rect.x;
	right = dst->clip_rect.x + dst->clip_rect.w - 1;
	top = dst->clip_rect.y;
	bottom = dst->clip_rect.y + dst->clip_rect.h - 1;

	while (1) {
		code1 = ep_clipEncode(*x1, *y1, left, top, right, bottom);
		code2 = ep_clipEncode(*x2, *y2, left, top, right, bottom);
		if (CLIP_ACCEPT(code1, code2)) {
			draw = 1;
			break;
		} else if (CLIP_REJECT(code1, code2))
			break;
		else {
			if (CLIP_INSIDE(code1)) {
				swaptmp = *x2;
				*x2 = *x1;
				*x1 = swaptmp;
				swaptmp = *y2;
				*y2 = *y1;
				*y1 = swaptmp;
				swaptmp = code2;
				code2 = code1;
				code1 = swaptmp;
			}
			if (*x2 != *x1) {
				m = (float)(*y2 - *y1) / (float)(*x2 - *x1);
			} else {
				m = 1.0f;
			}
			if (code1 & CLIP_LEFT_EDGE) {
				*y1 += (Sint16) ((left - *x1) * m);
				*x1 = left;
			} else if (code1 & CLIP_RIGHT_EDGE) {
				*y1 += (Sint16) ((right - *x1) * m);
				*x1 = right;
			} else if (code1 & CLIP_BOTTOM_EDGE) {
				if (*x2 != *x1) {
					*x1 += (Sint16) ((bottom - *y1) / m);
				}
				*y1 = bottom;
			} else if (code1 & CLIP_TOP_EDGE) {
				if (*x2 != *x1) {
					*x1 += (Sint16) ((top - *y1) / m);
				}
				*y1 = top;
			}
		}
	}

	return draw;
}

int ep_lineColor(SDL_Surface * dst, Sint16 x1, Sint16 y1, Sint16 x2, Sint16 y2, Uint8 color)
{
	int pixx, pixy;
	int x, y;
	int dx, dy;
	int sx, sy;
	int swaptmp;
	Uint8 *pixel;

	/*
	* Clip line and test if we have to draw
	*/
	if (!(ep_clipLine(dst, &x1, &y1, &x2, &y2))) {
		return (0);
	}

	/*
	* Test for special cases of straight lines or single point
	*/
	if (x1 == x2) {
		if (y1 < y2) {
			return (ep_vlineColor(dst, x1, y1, y2, color));
		} else if (y1 > y2) {
			return (ep_vlineColor(dst, x1, y2, y1, color));
		} else {
			return (ep_pixelColor(dst, x1, y1, color));
		}
	}
	if (y1 == y2) {
		if (x1 < x2) {
			return (ep_hlineColor(dst, x1, x2, y1, color));
		} else if (x1 > x2) {
			return (ep_hlineColor(dst, x2, x1, y1, color));
		}
	}

	/*
	* Variable setup
	*/
	dx = x2 - x1;
	dy = y2 - y1;
	sx = (dx >= 0) ? 1 : -1;
	sy = (dy >= 0) ? 1 : -1;

	/*
	* More variable setup
	*/
	dx = sx * dx + 1;
	dy = sy * dy + 1;
	pixx = dst->format->BytesPerPixel;
	pixy = dst->pitch;
	pixel = ((Uint8 *) dst->pixels) + pixx * (int) x1 + pixy * (int) y1;
	pixx *= sx;
	pixy *= sy;
	if (dx < dy) {
		swaptmp = dx;
		dx = dy;
		dy = swaptmp;
		swaptmp = pixx;
		pixx = pixy;
		pixy = swaptmp;
	}

	x = 0;
	y = 0;
	assert(dst->format->BytesPerPixel == 1);
	for (; x < dx; x++, pixel += pixx) {
		*pixel = color;
		y += dy;
		if (y >= dx) {
			y -= dx;
			pixel += pixy;
		}
	}

	return 0;
}


SDL_Surface *ep_IMG_Load_PNG(const char *filename) {
//	return IMG_Load(filename);
	std::vector<unsigned char> buffer;
	std::vector<unsigned char> image;
	unsigned w, h;

	lodepng::load_file(buffer, filename); //load the image file with given filename

	lodepng::State state;
	state.decoder.color_convert = false;
	unsigned error = lodepng::decode(image, w, h, state, buffer);

	if (error) {
		using namespace OpenXcom;
		Log(LOG_ERROR) <<  "decoder error " << error << ": " << lodepng_error_text(error);
		return 0;
	}

	LodePNGColorMode& color = state.info_png.color;

	assert(color.bitdepth == 8);
	assert(color.colortype == LCT_PALETTE);
	assert(w*h == image.size());

	void *alignedBuffer = OpenXcom::NewAligned(8, w, h);

	SDL_Surface *surface = ep_SDL_CreateRGBSurfaceFrom(alignedBuffer, w, h, 8, OpenXcom::GetPitch(8, w), 0, 0, 0, 0);

	Uint8 *row = (Uint8*) surface->pixels;
	Uint8 *png = (Uint8*) &image[0];

	for (int y = h; y; --y) {
		Uint8 *pixels = row;
		for (int x = w; x; --x) {
			*pixels = *png;
			++pixels;
			++png;
		}
		row += surface->pitch;
	}

	ep_SDL_SetColors(surface, (SDL_Color*)color.palette, 0, 256);

	return surface;
}
