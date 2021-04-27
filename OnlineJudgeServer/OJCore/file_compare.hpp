#pragma once

#include <stdio.h> /* for BUFSIZE */
#include <memory.h> /* for memcmp */

#include <string> /* for std::string */
#include <fstream> /* for File, fopen, fread, feof */
#include <iostream> /* for std::ifstream, getline */

#include "str_util.hpp" /* for StringUtil::trim */

#ifndef BUF_SIZE
    #define BUF_SIZE BUFSIZE
#endif

class FileCompareUtil final {
private:
    explicit FileCompareUtil() {}

public:
    ~FileCompareUtil() {}

    // ignore the \n and blank at begin and end
    static inline bool normalCompare(const char* file_name1, const char* file_name2) {
        std::ifstream fin1(file_name1, std::ifstream::in), fin2(file_name2, std::ifstream::in);
        fin1.sync_with_stdio(false), fin2.sync_with_stdio(false);
        std::string str1; str1.reserve(BUF_SIZE);
        std::string str2; str2.reserve(BUF_SIZE);
        while (fin1.good() || fin2.good()) {
            fin1.good() ? (getline(fin1, str1), StringUtil::trim(str1)) : (str1.clear());
            fin2.good() ? (getline(fin2, str2), StringUtil::trim(str2)) : (str2.clear());
            if (str1 != str2){
                return false;
            }
        }
        return true;
    }

    static inline bool strictCompare(const char* file_name1, const char* file_name2) {
        FILE *fp1 = fopen(file_name1, "rb"), *fp2 = fopen(file_name2, "rb");
        char buf1[BUF_SIZE], buf2[BUF_SIZE];
        while (!feof(fp1) && !feof(fp2)) {
            size_t len = fread(buf1, 1, sizeof(buf1), fp1);
            fread(buf2, 1, sizeof(buf2), fp2);
            if (memcmp(buf1, buf2, len)) { 
                return false;
            }
        }
        return true;
    }

};