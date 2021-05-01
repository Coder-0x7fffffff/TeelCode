#include "judge.hpp"
#include "ojcore.hpp"

void judge(const char* code, const char* dirPath, const char* samplePath, int languageType) {
    Judge judge;
    switch (languageType) {
        case 0:
            judge.judge(code, samplePath, dirPath, LanguageType::C);
            break;
        case 1:
            judge.judge(code, samplePath, dirPath, LanguageType::CPP);
            break;
        case 2:
            judge.judge(code, samplePath, dirPath, LanguageType::JAVA);
            break;
        case 3:
            judge.judge(code, samplePath, dirPath, LanguageType::PYTHON2);
            break;
        case 4:
            judge.judge(code, samplePath, dirPath, LanguageType::PYTHON3);
            break;
    }
}