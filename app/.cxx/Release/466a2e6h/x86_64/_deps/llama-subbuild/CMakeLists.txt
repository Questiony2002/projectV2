# Distributed under the OSI-approved BSD 3-Clause License.  See accompanying
# file Copyright.txt or https://cmake.org/licensing for details.

cmake_minimum_required(VERSION 3.22.1-g37088a8-dirty)

# We name the project and the target for the ExternalProject_Add() call
# to something that will highlight to the user what we are working on if
# something goes wrong and an error message is produced.

project(llama-populate NONE)


# Pass through things we've already detected in the main project to avoid
# paying the cost of redetecting them again in ExternalProject_Add()
set(GIT_EXECUTABLE [==[D:/Applications/Git/Git/cmd/git.exe]==])
set(GIT_VERSION_STRING [==[2.48.1.windows.1]==])
set_property(GLOBAL PROPERTY _CMAKE_FindGit_GIT_EXECUTABLE_VERSION
  [==[D:/Applications/Git/Git/cmd/git.exe;2.48.1.windows.1]==]
)


include(ExternalProject)
ExternalProject_Add(llama-populate
                     "UPDATE_DISCONNECTED" "False" "GIT_REPOSITORY" "https://github.com/ggml-org/llama.cpp" "GIT_TAG" "b6e4ff69b8abd509647b531bd5b4e86950204f66"
                    SOURCE_DIR          "D:/Users/Wangzeyu/wzyDiplomaProject/projectV2/app/.cxx/Release/466a2e6h/x86_64/_deps/llama-src"
                    BINARY_DIR          "D:/Users/Wangzeyu/wzyDiplomaProject/projectV2/app/.cxx/Release/466a2e6h/x86_64/_deps/llama-build"
                    CONFIGURE_COMMAND   ""
                    BUILD_COMMAND       ""
                    INSTALL_COMMAND     ""
                    TEST_COMMAND        ""
                    USES_TERMINAL_DOWNLOAD  YES
                    USES_TERMINAL_UPDATE    YES
)


