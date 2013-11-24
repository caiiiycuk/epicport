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

extern void Epicport_PlayMusic(const char*, int) {
}

extern void Epicport_HaltMusic() {
}

#endif
