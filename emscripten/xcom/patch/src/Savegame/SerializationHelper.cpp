/*
 * Copyright 2010-2013 OpenXcom Developers.
 *
 * This file is part of OpenXcom.
 *
 * OpenXcom is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * OpenXcom is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with OpenXcom.  If not, see <http://www.gnu.org/licenses/>.
 */
#include "SerializationHelper.h"
#include <assert.h>

namespace OpenXcom
{

int unserializeInt(Uint8 **buffer, Uint8 sizeKey)
{
	int ret = 0;
	switch(sizeKey)
	{
	case 1:
		ret = (Sint8) **buffer;
		break;
	case 2:
		ret = (Sint16) (**buffer
			| *(*buffer + 1) << 8);
		break;
	case 3:
		assert(false); // no.
		break;
	case 4:
		ret = (Sint32) (**buffer
			| *(*buffer + 1) << 8
			| *(*buffer + 2) << 16
			| *(*buffer + 3) << 24);
		break;
	default:
		assert(false); // get out.
	}

	*buffer += sizeKey;

	return ret;
}

void serializeInt(Uint8 **buffer, Uint8 sizeKey, int value)
{
	switch(sizeKey)
	{
	case 1:
		assert(value < 256);
		**buffer = value & 0xFF;
		break;
	case 2:
		assert(value < 65536);
        *(*buffer + 0) = (value & 0x00FF);
        *(*buffer + 1) = (value & 0xFF00) >> 8;
		break;
	case 3:
		assert(false); // no.
		break;
	case 4:
		*(*buffer + 0) = (value & 0x000000FF);
		*(*buffer + 1) = (value & 0x0000FF00) >> 8;
		*(*buffer + 2) = (value & 0x00FF0000) >> 16;
		*(*buffer + 3) = (value & 0xFF000000) >> 24;
		break;
	default:
		assert(false); // get out.
	}

	*buffer += sizeKey;
}

}
