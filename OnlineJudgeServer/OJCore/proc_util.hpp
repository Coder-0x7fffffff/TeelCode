#pragma once

#include <cstring>
#include <string> /* for std::string */
#include <fstream> /* for FILE */

#include "str_util.hpp"

class ProcUtil final {
private:
    explicit ProcUtil() {}

public:

    static int getProcStatus(int pid, const std::string& mark) {
        FILE * fp;
        char buf[BUF_SIZE];
        int ret = 0;
        sprintf(buf, "/proc/%d/status", pid);
        if (fp = fopen(buf, "re")) {
            int len = mark.length();
            while (fp && fgets(buf, BUF_SIZE - 1, fp)) {
                buf[strlen(buf) - 1] = 0;
                if (0 == strncmp(buf, mark.c_str(), len)) {
                    sscanf(buf + len + 1, "%d", &ret);
                }
            }
            fclose(fp);
        }
        return ret;
    }

};