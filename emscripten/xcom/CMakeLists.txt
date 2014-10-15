cmake_minimum_required (VERSION 2.8)

message("-----------------------------------------------")
message("- openxcom build script")
message("- @author caiiiycuk aka Aleksander Guryanov")
message("- @feedback https://github.com/caiiiycuk/epicport")
message("-----------------------------------------------")

project(openxcom)

set(BUILD_ROOT "${PROJECT_BINARY_DIR}/build")
set(ROOT "${CMAKE_SOURCE_DIR}/../..")

if(NOT EXISTS "$ENV{OPENXCOM_REPO}")
  message(FATAL_ERROR "Please set OPENXCOM_REPO before building")
endif()

message("-- Making fresh environment")
execute_process(COMMAND rm -rf ${BUILD_ROOT})
execute_process(COMMAND mkdir -p ${BUILD_ROOT})

message("-- Copying original sources")
execute_process(COMMAND cp -r $ENV{OPENXCOM_REPO}/. ${BUILD_ROOT})

message("-- Patching openxcom to be emscripten ready")
execute_process(COMMAND cp -r ${CMAKE_CURRENT_SOURCE_DIR}/patch/. ${BUILD_ROOT})

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