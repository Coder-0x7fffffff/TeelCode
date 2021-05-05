#pragma once

#ifdef __cplusplus
extern "C" {
#endif

void execute(const char* code, const char* dirPath, const char* input, int languageType);
void judge(const char* code, const char* dirPath, const char* samplePath, int languageType);

#ifdef __cplusplus
}
#endif