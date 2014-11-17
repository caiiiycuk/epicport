Emscripten build
================

OpenXCOM
~~~~~~~~

::

  mkdir xcom
  cd xcom
  git clone git@github.com:caiiiycuk/xcom-emscripten.git xcom-emscripten
  emconfigure cmake ../../../../emscripten/xcom/
  emmake make -j3


Caesaria
~~~~~~~~

::

  mkdir caesaria
  cd caesaria
  git clone https://bitbucket.org/caiiiycuk/caesaria-emscripten caesaria-emscripten
  EPICPORT_API=1 emconfigure cmake caesaria-emscripten
  emmake make -j3
  cp source/caesaria* ./