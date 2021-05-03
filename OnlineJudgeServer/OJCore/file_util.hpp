#pragma once

#include <fcntl.h> /* for open */
#include <dirent.h> /* for mkdir */
#include <string> /* for std::string */
#include <fstream>
#include <unistd.h> /* for access */
#include <iostream> /* for std::ifstream */
#include <sys/time.h> /* for time_t, gettimeofday */
#include <sys/stat.h> /* for struct stat, stat */

#define RD_SUC 0
#define RD_FLT 1
#define WT_SUC 0
#define WT_FLT 1
#define CP_SUC 0
#define CP_FLT 1

#define __IS_OUTPUT_PARAM /* \^_^/ */

class FileUtil final {
private:
    explicit FileUtil() {}

public:
    ~FileUtil() {}

    static int readFile(const std::string& filePath, __IS_OUTPUT_PARAM std::string& s) {
        std::ifstream fin(filePath.c_str(), std::ios::binary);
        if (fin.good()) {
            size_t fileSize = fin.seekg(0, std::ios::end).tellg();
            char* buffer = new char[fileSize];
            fin.seekg(0, std::ios::beg).read(buffer, static_cast<std::streamsize>(fileSize));
            fin.close();
            s = std::string(buffer, fileSize);
            return RD_SUC;
        }
        return RD_FLT;
    }

    static int writeFile(const std::string& filePath, const std::string& content) {
        std::ofstream fout(filePath.c_str());
        if (fout.good()) {
            fout.write(content.c_str(), content.length());
            fout.close();
            return WT_SUC;
        }
        return WT_FLT;
    }

    static int appendFile(const std::string& filePath, const std::string& content) {
        std::ofstream fout(filePath.c_str(), std::ios::app);
        if (fout.good()) {
            fout.write(content.c_str(), content.length());
            fout.close();
            return WT_SUC;
        }
        return WT_FLT;
    }

    static int copyFile(const std::string& srcFilePath, const std::string& dstFilePath) {
        std::ifstream fin(srcFilePath.c_str(), std::ios::binary);
        std::ofstream fout(dstFilePath.c_str());
        if (fin.good() && fout.good()) {
            size_t fileSize = fin.seekg(0, std::ios::end).tellg();
            char* buffer = new char[fileSize];
            fin.seekg(0, std::ios::beg).read(buffer, static_cast<std::streamsize>(fileSize));
            fout.write(buffer, fileSize);
            fout.close();
            fin.close();
            return CP_SUC;
        }
        return CP_FLT;
    }

    static long getFileSize(const std::string& filePath) {
        struct stat st;
        stat(filePath.c_str(), &st);
        return st.st_size;        
    }

    static bool isExist(const std::string& filePath) {
        return -1 != access(filePath.c_str(), 0);
    }

    static int createFile(const std::string& filePath) {
        std::ofstream fout(filePath.c_str());
        fout.close();
    }

    static std::string logPath(const std::string& filePath) {
        return filePath + ".log";
    }

    static std::string compileErrorPath(const std::string& filePath) {
        return filePath + ".compile_error";
    }

    static std::string exePath(const std::string& filePath) {
        return filePath;
    }

    static std::string stdinPath(const std::string& filePath) {
        return filePath + ".in";
    }

    static std::string stdoutPath(const std::string& filePath) {
        return filePath + ".out";
    }

    static std::string stderrPath(const std::string& filePath) {
        return filePath + ".err";
    }

    static std::string resultPath(const std::string& filePath) {
        return filePath + ".res";
    }

};