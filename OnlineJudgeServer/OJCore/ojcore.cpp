#include "judge.hpp"
#include "ojcore.hpp"

void execute(const char* code, const char* dirPath, const char* input, int languageType) {
    Judge judge;
    switch (languageType) {
        case 0:
            judge.execute(code, dirPath, input, LanguageType::C);
            break;
        case 1:
            judge.execute(code, dirPath, input, LanguageType::CPP);
            break;
        case 2:
            judge.execute(code, dirPath, input, LanguageType::JAVA);
            break;
        case 3:
            judge.execute(code, dirPath, input, LanguageType::PYTHON2);
            break;
        case 4:
            judge.execute(code, dirPath, input, LanguageType::PYTHON3);
            break;
    }
}

void judge(const char* code, const char* dirPath, const char* samplePath, int languageType) {
    Judge judge;
    switch (languageType) {
        case 0:
            judge.judge(code, dirPath, samplePath, LanguageType::C);
            break;
        case 1:
            judge.judge(code, dirPath, samplePath, LanguageType::CPP);
            break;
        case 2:
            judge.judge(code, dirPath, samplePath, LanguageType::JAVA);
            break;
        case 3:
            judge.judge(code, dirPath, samplePath, LanguageType::PYTHON2);
            break;
        case 4:
            judge.judge(code, dirPath, samplePath, LanguageType::PYTHON3);
            break;
    }
}