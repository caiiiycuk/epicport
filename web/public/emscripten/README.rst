Emscripten build
================

OpenXCOM
~~~~~~~~

::

  mkdir xcom
  cd xcom
  OPENXCOM_REPO=<path-to-xcom-repo> emconfigure cmake ../../../../emscripten/xcom/
  emmake make -j3