
if(NOT "D:/Users/Wangzeyu/wzyDiplomaProject/projectV2/app/.cxx/Release/466a2e6h/x86/_deps/llama-subbuild/llama-populate-prefix/src/llama-populate-stamp/llama-populate-gitinfo.txt" IS_NEWER_THAN "D:/Users/Wangzeyu/wzyDiplomaProject/projectV2/app/.cxx/Release/466a2e6h/x86/_deps/llama-subbuild/llama-populate-prefix/src/llama-populate-stamp/llama-populate-gitclone-lastrun.txt")
  message(STATUS "Avoiding repeated git clone, stamp file is up to date: 'D:/Users/Wangzeyu/wzyDiplomaProject/projectV2/app/.cxx/Release/466a2e6h/x86/_deps/llama-subbuild/llama-populate-prefix/src/llama-populate-stamp/llama-populate-gitclone-lastrun.txt'")
  return()
endif()

execute_process(
  COMMAND ${CMAKE_COMMAND} -E rm -rf "D:/Users/Wangzeyu/wzyDiplomaProject/projectV2/app/.cxx/Release/466a2e6h/x86/_deps/llama-src"
  RESULT_VARIABLE error_code
  )
if(error_code)
  message(FATAL_ERROR "Failed to remove directory: 'D:/Users/Wangzeyu/wzyDiplomaProject/projectV2/app/.cxx/Release/466a2e6h/x86/_deps/llama-src'")
endif()

# try the clone 3 times in case there is an odd git clone issue
set(error_code 1)
set(number_of_tries 0)
while(error_code AND number_of_tries LESS 3)
  execute_process(
    COMMAND "D:/Applications/Git/Git/cmd/git.exe"  clone --no-checkout --config "advice.detachedHead=false" "https://github.com/ggml-org/llama.cpp" "llama-src"
    WORKING_DIRECTORY "D:/Users/Wangzeyu/wzyDiplomaProject/projectV2/app/.cxx/Release/466a2e6h/x86/_deps"
    RESULT_VARIABLE error_code
    )
  math(EXPR number_of_tries "${number_of_tries} + 1")
endwhile()
if(number_of_tries GREATER 1)
  message(STATUS "Had to git clone more than once:
          ${number_of_tries} times.")
endif()
if(error_code)
  message(FATAL_ERROR "Failed to clone repository: 'https://github.com/ggml-org/llama.cpp'")
endif()

execute_process(
  COMMAND "D:/Applications/Git/Git/cmd/git.exe"  checkout b6e4ff69b8abd509647b531bd5b4e86950204f66 --
  WORKING_DIRECTORY "D:/Users/Wangzeyu/wzyDiplomaProject/projectV2/app/.cxx/Release/466a2e6h/x86/_deps/llama-src"
  RESULT_VARIABLE error_code
  )
if(error_code)
  message(FATAL_ERROR "Failed to checkout tag: 'b6e4ff69b8abd509647b531bd5b4e86950204f66'")
endif()

set(init_submodules TRUE)
if(init_submodules)
  execute_process(
    COMMAND "D:/Applications/Git/Git/cmd/git.exe"  submodule update --recursive --init 
    WORKING_DIRECTORY "D:/Users/Wangzeyu/wzyDiplomaProject/projectV2/app/.cxx/Release/466a2e6h/x86/_deps/llama-src"
    RESULT_VARIABLE error_code
    )
endif()
if(error_code)
  message(FATAL_ERROR "Failed to update submodules in: 'D:/Users/Wangzeyu/wzyDiplomaProject/projectV2/app/.cxx/Release/466a2e6h/x86/_deps/llama-src'")
endif()

# Complete success, update the script-last-run stamp file:
#
execute_process(
  COMMAND ${CMAKE_COMMAND} -E copy
    "D:/Users/Wangzeyu/wzyDiplomaProject/projectV2/app/.cxx/Release/466a2e6h/x86/_deps/llama-subbuild/llama-populate-prefix/src/llama-populate-stamp/llama-populate-gitinfo.txt"
    "D:/Users/Wangzeyu/wzyDiplomaProject/projectV2/app/.cxx/Release/466a2e6h/x86/_deps/llama-subbuild/llama-populate-prefix/src/llama-populate-stamp/llama-populate-gitclone-lastrun.txt"
  RESULT_VARIABLE error_code
  )
if(error_code)
  message(FATAL_ERROR "Failed to copy script-last-run stamp file: 'D:/Users/Wangzeyu/wzyDiplomaProject/projectV2/app/.cxx/Release/466a2e6h/x86/_deps/llama-subbuild/llama-populate-prefix/src/llama-populate-stamp/llama-populate-gitclone-lastrun.txt'")
endif()

