#include "judge.hpp"
#include "ojcore.hpp"

void judge(const char* code, const char* dirPath, const char* samplePath, int languageType) {
    Judge judge;
    judge.judge(code, samplePath, dirPath, LanguageType::CPP);
}