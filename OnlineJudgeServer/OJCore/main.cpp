#include "judge.hpp"

int main() {  
    Judge judge;
    std::string code = "#include <iostream>\n\
#include <vector>\n\
using namespace std;\n\
int main() {\n\
    ios::sync_with_stdio(false);\n\
    int a, b;\n\
    while (cin >> a >> b){\n\
        printf(\"%d\\n\", a + b);\n\
    }\n\
    return 0;\n\
}";
    std::string dirPath = "OJCore/tmp";
    std::string samplePath = "OJCore/sample";
    judge.judge(code, samplePath, dirPath, LanguageType::CPP);
    return 0;
}