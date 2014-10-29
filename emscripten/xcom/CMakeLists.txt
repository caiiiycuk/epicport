cmake_minimum_required (VERSION 2.8)

message(STATUS "-------------------------------------------------")
message(STATUS " openxcom build script")
message(STATUS " @author caiiiycuk aka Aleksander Guryanov")
message(STATUS " @feedback https://github.com/caiiiycuk/epicport")
message(STATUS "-------------------------------------------------")

project(openxcom)

set(BUILD_ROOT "${PROJECT_BINARY_DIR}/build")
set(ROOT "${CMAKE_SOURCE_DIR}/../..")
set(EMXCOM_REPO "${CMAKE_SOURCE_DIR}/emscriptenXcom")
set(EMXCOM_TAGET_VERSION "emscriptenXcom-v0.9")

execute_process(
  COMMAND git describe --tags --exact-match
  WORKING_DIRECTORY ${EMXCOM_REPO}
  RESULT_VARIABLE EMXCOM_VERSION_CHECK
  OUTPUT_VARIABLE EMXCOM_VERSION
  ERROR_VARIABLE EMXCOM_VERSION_ERROR
)

if(EMXCOM_VERSION AND (EMXCOM_VERSION STREQUAL "${EMXCOM_TAGET_VERSION}\n"))
  message(STATUS "Found emscriptenXcom version '${EMXCOM_TAGET_VERSION}'")
else()
  message(WARNING "Wrong version of emscriptenXcom should be '${EMXCOM_TAGET_VERSION}'")
  message(WARNING "${EMXCOM_VERSION} != ${EMXCOM_TAGET_VERSION} in ${EMXCOM_REPO}\n (${EMXCOM_VERSION_CHECK}, ${EMXCOM_VERSION_ERROR})")
endif()

message(STATUS "Making fresh environment")
execute_process(COMMAND rm -rf ${BUILD_ROOT})
execute_process(COMMAND mkdir -p ${BUILD_ROOT})

message(STATUS "Copying original sources")
execute_process(COMMAND cp -r ${EMXCOM_REPO}/. ${BUILD_ROOT})

message("-- Collecting sources")

file (GLOB_RECURSE SRC
  "${BUILD_ROOT}/src/*.cpp"
)

file (GLOB_RECURSE YAML_SRC
  "${CMAKE_CURRENT_SOURCE_DIR}/3rdparty/yaml-cpp-0.5.0/src/*.cpp"
)

include_directories(
  "${CMAKE_CURRENT_SOURCE_DIR}/3rdparty/"
  "${CMAKE_CURRENT_SOURCE_DIR}/3rdparty/yaml-cpp-0.5.0/include/"
  "${ROOT}/native/includes/"
)

add_executable(openxcom 
  ${SRC}
  ${YAML_SRC}
)

set( CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -O2 -Wno-warn-absolute-paths" )

set( CMAKE_EXE_LINKER_FLAGS "${CMAKE_EXE_LINKER_FLAGS} -s TOTAL_MEMORY=268435456 --pre-js ${ROOT}/emscripten/pre/pre-api.js --js-transform \"perl ${ROOT}/emscripten/js-transform.pl\" --preload-file ${ROOT}/emscripten/xcom/vfs@/")