#!/bin/sh
rm -rf ../../../public/js/core
rm -rf ../../../public/js/game
coffee -cbo ../../../public/js/core ./core/
coffee -cbo ../../../public/js/game ./game/
