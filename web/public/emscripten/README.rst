Emscripten build
================

OpenXCOM
~~~~~~~~

::

  mkdir xcom
  cd xcom
  git clone git@github.com:caiiiycuk/emscriptenXcom.git emscriptenXcom
  emconfigure cmake ../../../../emscripten/xcom/
  emmake make -j3