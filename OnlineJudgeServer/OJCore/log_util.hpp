#pragma once

#include <string> /* for std::string */
#include <fstream> /* for std::ofstream */
#include <iostream> /* for std::ios */
#include <ctime> /* for time, ctime */

#include "str_util.hpp"

/* A simple logger, just record the message to log file */
class Logger {
private:
    explicit Logger() {}

public:
    ~Logger() {}

    static inline void log(const std::string& logFilePath, const std::string& content) {
        std::ofstream fout(logFilePath.c_str(), std::ios::app);
        if (fout.good()) {
            time_t now = time(0);
            std::string timeStr(ctime(&now));
            timeStr.erase(timeStr.end() - 1); // erase the \n
            std::string output = StringUtil::format("%s: %s\n", timeStr.c_str(), content.c_str());
            fout.write(output.c_str(), output.length());
            fout.close();
        }
    }

};