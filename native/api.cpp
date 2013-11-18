#include <epicport/api.h>

#ifndef EMSCRIPTEN

bool Epicport_CanSave() {
	return true;
}

bool Epicport_CanLoad() {
	return true;
}

void Epicport_PushSave(const char*) {
}

#endif
