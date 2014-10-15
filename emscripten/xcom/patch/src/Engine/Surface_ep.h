/*
 * Surface_ep.h
 *
 *  Created on: 23 окт. 2013 г.
 *      Author: caiiiycuk
 */

#ifndef SURFACE_EP_H_
#define SURFACE_EP_H_

#include <SDL.h>
#include <assert.h>
#include <stdlib.h>
#include <iostream>
#include "Logger.h"
#include "../lodepng.h"

SDL_Surface * ep_SDL_CreateRGBSurfaceFrom(void *pixels, int width, int height,
		int depth, int pitch, Uint32 Rmask, Uint32 Gmask, Uint32 Bmask,
		Uint32 Amask);

void ep_SDL_FreeSurface(SDL_Surface *surface);

int ep_SDL_FillRect(SDL_Surface *dst, SDL_Rect *dstrect, Uint8 color);

int ep_SDL_BlitSurface(SDL_Surface *src, SDL_Rect *srcrect, SDL_Surface *dst,
		SDL_Rect *dstrect);

int ep_SDL_SetPalette(SDL_Surface *screen, int which,
		   SDL_Color *colors, int firstcolor, int ncolors);

int ep_SDL_SetColors(SDL_Surface *surface, SDL_Color *colors, int firstcolor, int ncolors);

int ep_texturedPolygon(SDL_Surface * dst, const Sint16 * vx, const Sint16 * vy, int n, SDL_Surface *texture, int texture_dx, int texture_dy);

int ep_pixelColor(SDL_Surface * dst, Sint16 x, Sint16 y, Uint8 color);

int ep_filledCircleColor(SDL_Surface * dst, Sint16 x, Sint16 y, Sint16 rad, Uint8 color);

int ep_lineColor(SDL_Surface * dst, Sint16 x1, Sint16 y1, Sint16 x2, Sint16 y2, Uint8 color);

SDL_Surface *ep_IMG_Load_PNG(const char *filename);

#endif /* SURFACE_EP_H_ */
