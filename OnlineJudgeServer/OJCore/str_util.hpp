#pragma once

#include <stdio.h> /* for BUFSIZE */
#include <vector> /* for std::vector */
#include <string> /* for std::string, std::snprintf */

#define BUF_SIZE BUFSIZ

class StringUtil final {
private:
    explicit StringUtil() {}

public:
    ~StringUtil() {}

    inline static std::vector<std::string> split(const std::string& s, const std::string& spliter) {
        std::string::size_type lastPos = s.find_first_not_of(spliter);
        std::string::size_type pos = s.find_first_of(spliter, lastPos);
        std::vector<std::string> result;
        while (std::string::npos != lastPos || std::string::npos != pos) {
            result.emplace_back(s.substr(lastPos, pos - lastPos));
            lastPos = s.find_first_not_of(spliter, pos);
            pos = s.find_first_of(spliter, lastPos);
        }
        return result;
    }

    inline static void trim(std::string& s) {
        trimLeft(s); trimRight(s);
    }

    inline static void trimRight(std::string& s) {
        int end = s.length() - 1;
        while (end >= 0 && isspace(s[end])) {
            --end;
        }
        s = s.substr(0, end + 1);
    }

    inline static void trimLeft(std::string& s) {
        int start = 0, len = s.length();
        while (start < len && isspace(s[start])) { 
            ++start;
        }
        s = s.substr(start);
    }

    template<typename ...Args>
    inline static std::string format(const char* format, Args ... args){
        /* plus 1 for \0 */
        size_t size = std::snprintf(nullptr, 0, format, args ...) + 1;
        char bytes[size];
        std::snprintf(bytes, size, format, args ...);
        return std::string(bytes);
    }

};