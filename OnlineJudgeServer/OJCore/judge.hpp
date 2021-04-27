#pragma once

#include <math.h> /* for std::max */
#include <string> /* for std::string */
#include <memory> /* for smart ptr */

#include <sys/user.h>
#include <sys/stat.h> /* for stat */
#include <sys/wait.h> /* for wait4, waitpid */
#include <sys/resource.h> /* for RLIMIT_CPU */

#include "proc_util.hpp"
#include "log_util.hpp"
#include "str_util.hpp"
#include "file_util.hpp"
#include "file_compare.hpp"
#include "syscall_tracer.hpp"



#ifdef __x86_64__
    #define WORD_SIZE 8
#elif
    #define WORD_SIZE 4
#endif

/* 0 is wating and 1 is compiling, we will not handle 0 and 1 */
#define CE 2 /* Compile Error */
#define AC 3 /* Accept */
#define RE 4 /* Runtime Error */
#define PE 5 /* Presentation Error */
#define WA 6 /* Wrong Answer */
#define TLE 7 /* Time Limit Error */
#define MLE 8 /* Memory Limit Error */

#define C_SUFFIX ".c"
#define CPP_SUFFIX ".cpp"
#define JAVA_SUFFIX ".java"
#define PYTHON_SUFFIX ".py"
#define CMD_MAX_LEN 10

const char* C_CMD[] = { "gcc", "-O2", "-w", "-fmax-errors=3", "-std=c11", "lm", NULL };
const char* CPP_CMD[] = { "g++", "-O2", "-w", "-fmax-errors=3", "-std=c++11", "lm", NULL };
const char* JAVA_CMD[] = { "javac", "-encoding", "UTF8", NULL };
const char* PYTHON2_CMD[] = { "python", "-m", "py_compile", NULL };
const char* PYTHON3_CMD[] = { "python3", "-m", "py_compile", NULL };

class Judge {
private:
    std::unique_ptr<SyscallTracer> pSyscallTracer;

    struct ResultSet {
        int timeUsage; // time waste (ms)
        int memUsage; // memory waste (kb)
        int resultCode; // AC/CE/...

        /* If compile error, this string will saved the error infomation, otherwise empty */
        std::string resultInfo;

        ResultSet() :
            timeUsage(0), 
            memUsage(0),
            resultCode(-1),
            resultInfo(std::string()) {}
    };

    /* Output this object */
    std::vector<ResultSet> resultSets;

    /* Record current Result Set */
    ResultSet resultSet;

    std::string srcFilePath; // source file path
    std::string exeFilePath; // executable file path
    std::string logFilePath; // log file path
    std::string compileErrorFilePath; // compile error file path
    std::string stdinFilePath; // std input file path
    std::string stdoutFilePath; // std output file path
    std::string stderrFilePath; // std error file path
    std::string resultFilePath; // result file path

public:
    Judge() {

    }

    ~Judge() {

    }

    int judge(const std::string& code, const std::string& samplePath, const std::string& dirPath, LanguageType languageType) {
        createFiles(dirPath, languageType);
        if (0 == input(code, dirPath, languageType)) {
            if (0 == compile(dirPath, languageType)) {
                run(samplePath, dirPath, languageType);
                output();
                return 0;
            }
        }
        /* input error? */
        return 1;
    }

private:
    /* 0. Create Files */
    void createFiles(const std::string& dirPath, LanguageType languageType) {
        const std::string filePath = dirPath + "/main";
        FileUtil::createFile(logFilePath = FileUtil::logPath(filePath));
        FileUtil::createFile(exeFilePath = FileUtil::exePath(filePath));
        FileUtil::createFile(compileErrorFilePath = FileUtil::compileErrorPath(filePath));
        FileUtil::createFile(stdinFilePath = FileUtil::stdinPath(filePath));
        FileUtil::createFile(stdoutFilePath = FileUtil::stdoutPath(filePath));
        FileUtil::createFile(stderrFilePath = FileUtil::stderrPath(filePath));
        FileUtil::createFile(resultFilePath = FileUtil::resultPath(filePath));
        switch (languageType) {
        case LanguageType::C:
            srcFilePath = filePath + C_SUFFIX;
            break;
        case LanguageType::CPP:
            srcFilePath = filePath + CPP_SUFFIX;
            break;
        case LanguageType::JAVA:
            srcFilePath = filePath + JAVA_SUFFIX;
            break;
        case LanguageType::PYTHON:
            srcFilePath = filePath + PYTHON_SUFFIX;
            break;
        default:
            break;
        }
        FileUtil::createFile(srcFilePath);
    }

    /* 1. input code and specify the language */
    int input(const std::string& code, const std::string& dirPath, LanguageType languageType) {
        Logger::log(logFilePath, StringUtil::format("Input!..."));
        /* Create SyscallTracer */
        pSyscallTracer = std::make_unique<SyscallTracer>(languageType);
        /* Write code */
        Logger::log(logFilePath, StringUtil::format("Write code to %s", srcFilePath.c_str()));
        return FileUtil::writeFile(srcFilePath, code);
    }

    void compileMain(char** command) {
        /* Child process */
        /* Create Compile Info File */
        int fd = open(compileErrorFilePath.c_str(), O_WRONLY | O_CREAT, 0666);
        if (-1 == fd) {
            Logger::log(logFilePath, StringUtil::format("Open compileError file error"));
            Logger::log(logFilePath, StringUtil::format("Child process exit(1)"));
            /* Child process abnormal exit */
            exit(1);
        }

        std::string executedCommand;
        for (int i = 0; i < 10 && NULL != command[i]; ++i) {
            executedCommand += command[i];
            executedCommand += " ";
        }

        Logger::log(logFilePath, StringUtil::format("Execute: %s", executedCommand.c_str()));
        /* Execute command */
        dup2(fd, 2);
        execvp(command[0], command);
        Logger::log(logFilePath, StringUtil::format("Child process exit(0)"));
        /* Child process normal exit */
        exit(0);
    }

    int watchCompile(int pid) {
        Logger::log(logFilePath, StringUtil::format("Waiting Child process! pid=%d", pid));
        /* Wait child process */
        waitpid(pid, NULL, 0);
        /* Child process finished */
        Logger::log(logFilePath, StringUtil::format("Child process finished!"));
        struct stat st;
        /* If compile is success, there muse be an executable file */
        if (-1 == (stat(exeFilePath.c_str(), &st))) {
            Logger::log(logFilePath, StringUtil::format("%s not found!", exeFilePath.c_str()));
            return 1;
        }
        if (0 == st.st_size) {
            Logger::log(logFilePath, StringUtil::format("Compile %s failed!", srcFilePath.c_str()));
            resultSet.resultInfo = CE;
            FileUtil::readFile(compileErrorFilePath, resultSet.resultInfo);
            return 1;
        }
        Logger::log(logFilePath, StringUtil::format("Compile %s OK!", srcFilePath.c_str()));
        return 0;
    }

    /* 2. compile */
    int compile(const std::string& dirPath, LanguageType languageType) {
        Logger::log(logFilePath, StringUtil::format("Compile!..."));
        /* Set compile command */
        char* command[CMD_MAX_LEN] = { 0 };
        for (int i = 0; i < 10; ++i) {
            command[i] = new char[50];
        }
        sprintf(command[0], "%s", "g++");
        sprintf(command[1], "%s", srcFilePath.c_str());
        sprintf(command[2], "%s", "-o");
        sprintf(command[3], "%s", exeFilePath.c_str());
        sprintf(command[4], "%s", "-std=c++11");
        command[5] = NULL;

        Logger::log(logFilePath, StringUtil::format("fork!..."));
        int pid = fork();
        if (0 == pid) {
            compileMain(command);
        }
        return watchCompile(pid);
    }

    void runMain(int timeLimit, int memLimit) {
        /* Child process */
        /* Set limmit */
        rlimit r;
        getrlimit(RLIMIT_CPU, &r);
        r.rlim_cur = timeLimit / 1000;
        r.rlim_max = timeLimit / 1000;
        setrlimit(RLIMIT_CPU, &r);

        alarm(0);

        getrlimit(RLIMIT_AS, &r);
        r.rlim_cur = memLimit * 1024;
        r.rlim_max = memLimit * 1024;
        setrlimit(RLIMIT_AS, &r);

        /* Redirect stdin stdout stderr */
        freopen(stdinFilePath.c_str(), "r", stdin);
        freopen(stdoutFilePath.c_str(), "w", stdout);
        freopen(stderrFilePath.c_str(), "w", stderr);

        if (-1 == SyscallTracer::traceMe()) {
            fprintf(stderr, "failed to ptrace : %s\n", strerror(errno));
            /* Chile process abnormal exit */
            exit(1);
        }
        execl(exeFilePath.c_str(), exeFilePath.c_str(), NULL);
        /* Chile process normal exit */
        exit(0);
    }

    void watchRun(int pid, int timeLimit, int memLimit, LanguageType languageType) {
        struct rusage rused;
        int status = 0;

        bool first = true;

        for (;;) {

            if (-1 == wait4(pid, &status, __WALL, &rused)) {
                Logger::log(logFilePath, StringUtil::format("wait4 failed! pid=%d", pid));
                resultSet.resultCode = RE;
                break;
            }

            if (first) {
                first = false;
                if (-1 == pSyscallTracer->trace(pid)) {
                    Logger::log(logFilePath, 
                    StringUtil::format("ptrace(PTRACE_SETOPTIONS, %d, 0, PTRACE_O_TRACEEXIT | PTRACE_O_EXITKILL) error %s", pid, strerror(errno)));
                    resultSet.resultCode = RE;
                    break;
                }
            }

            if (LanguageType::JAVA == languageType) {
                resultSet.memUsage = rused.ru_minflt * getpagesize();
            }
            else {
                resultSet.memUsage = std::max(resultSet.memUsage, ProcUtil::getProcStatus(pid, "VmPeak:"));    
            }

            /* If out of memory, then quit and set MLE fault */
            if (resultSet.memUsage > memLimit * 1024) {
                resultSet.resultCode = MLE;
                pSyscallTracer->killChild(pid);
                break;
            }

            /* If out of time, then quit and set TLE fault */
            if (resultSet.timeUsage > timeLimit) {
                resultSet.resultCode = TLE;
                pSyscallTracer->killChild(pid);
                break;
            }

            /* Normal exit */
            if (WIFEXITED(status)) {
                break;
            }

            /* If error file is not empty, then quit and set RE fault */
            if (FileUtil::getFileSize(stderrFilePath)) {
                resultSet.resultCode = RE;
                pSyscallTracer->killChild(pid);
                break;
            }

            int exitCode = WEXITSTATUS(status);
            /* exitcode = 5 is ok */
            if (0 == exitCode || 0x05 == exitCode || 133 == exitCode) {

            }
            else {
                /*  */
                Logger::log(logFilePath, StringUtil::format("exit code=%d", exitCode));
                switch (exitCode) {
                case SIGCHLD: case SIGALRM:
                    alarm(0);
                case SIGKILL: case SIGXCPU:
                    resultSet.resultCode = TLE;
                    break;
                default:
                    resultSet.resultCode = RE;
                    break;
                }
                break;
            }

            /* If 0 != WIFSIGNALED(status), it means the process exit abnormal */
            if (WIFSIGNALED(status)) {
                int sig = WTERMSIG(status);
                switch (sig) {
                case SIGCHLD: case SIGALRM:
                    alarm(0);
                case SIGKILL: case SIGXCPU:
                    resultSet.resultCode = TLE;
                    break;
                default:
                    resultSet.resultCode = RE;
                    break;
                }
                break;
            }
            else {
                if (SIGSEGV == WSTOPSIG(status)) {
                    Logger::log(logFilePath, StringUtil::format("child process %d received SIGSEGV, killing... ", pid));
                    if (-1 == kill(pid, SIGKILL)) {
                        Logger::log(logFilePath, StringUtil::format("Failed %s", strerror(errno)));
                    }
                    else {
                        Logger::log(logFilePath, StringUtil::format("Done!"));
                    }
                }
                else if (WIFSTOPPED(status) && (WSTOPSIG(status) & 0x80)) {
                    long callid = ptrace(PTRACE_PEEKUSER, pid, WORD_SIZE * ORIG_EAX, NULL); // get syscall num
                    if (callid >= NR_syscalls) {
                        resultSet.resultCode = RE;
                        break;
                    }
                    if ((*pSyscallTracer)[callid]) {
                        --(*pSyscallTracer)[callid];
                    }
                    else {
                        Logger::log(logFilePath, StringUtil::format("child process %d is trying to invoke the banned system call, kill it\n", pid));
                        resultSet.resultCode = RE;
                        pSyscallTracer->killChild(pid);
                        break;
                    }
                }
            }

            /* Let child process continue and still ptrace it */
            if (-1 == pSyscallTracer->traceSyscall(pid)) {
                Logger::log(logFilePath, StringUtil::format("ptrace(PTRACE_SYSCALL, %d, 0, 0) error %s", pid, strerror(errno)));
                resultSet.resultCode = RE;
                break;
            }

        }

        /* If TLE, just set the usage equals the limit */
        if (TLE == resultSet.resultCode) {
            resultSet.timeUsage = timeLimit;
        }
        else {
            resultSet.timeUsage += (rused.ru_utime.tv_sec * 1000 + rused.ru_utime.tv_usec / 1000);
            resultSet.timeUsage += (rused.ru_stime.tv_sec * 1000 + rused.ru_stime.tv_usec / 1000);
        }

        /* If MLE, just set the usage equals the limit */
        if (MLE == resultSet.resultCode) {
            resultSet.memUsage = memLimit * 1024;
        }

        Logger::log(logFilePath,
        StringUtil::format("Current: time usage=%d ms, memory usage=%d kb", 
        resultSet.timeUsage, resultSet.memUsage));
    }

    void judgeMain(const std::string& sampleOutFilePath) {
        /* -1 means the process run normally */
        if (-1 == resultSet.resultCode) {
            if (FileCompareUtil::normalCompare(stdoutFilePath.c_str(), sampleOutFilePath.c_str())) {
                resultSet.resultCode = AC;
            }
            else {
                resultSet.resultCode = WA;
            }
        }
        resultSets.push_back(resultSet);
    }

    /* 3. run and judge the result */
    int run(const std::string& samplePath, const std::string& dirPath, LanguageType languageType) {
        Logger::log(logFilePath, StringUtil::format("Run!..."));
        /* For c & cpp, the time waste limit is 1s, memory limit is 64MB, others are 2s and 128MB */
        int timeLimit = 1000;      // 1 s
        int memLimit = 640 * 1024; // 64 MB
        if (LanguageType::C != languageType && LanguageType::CPP != languageType) {
            timeLimit *= 2;
            memLimit *= 2;
        }
        Logger::log(logFilePath, StringUtil::format("fork!..."));
        /* How many sample(test case) */
        int sampleCount = 1;
        for (int i = 1; i <= sampleCount; ++i) {          
            /* clear */
            resultSet = ResultSet();
            std::string sampleOutFilePath = StringUtil::format("%s/%d.out", samplePath.c_str(), i);
            /* write .in file here */
            std::string sampleInFilePath = StringUtil::format("%s/%d.in", samplePath.c_str(), i);
            stdinFilePath = sampleInFilePath;
            int pid = fork();
            if (0 == pid) {
                runMain(timeLimit, memLimit);
            }
            watchRun(pid, timeLimit, memLimit, languageType);
            judgeMain(sampleOutFilePath);
        }
    }

    /* 4. output the result */
    void output() {
        for (ResultSet rs : resultSets) {
            Logger::log(logFilePath, StringUtil::format("{ \n\tResult Code: %d,\n\
                Time used: %d ms,\n\
                Memory Used: %d kb,\n\
                Result Info: %s\n\
            }", rs.resultCode, rs.timeUsage, rs.memUsage, rs.resultInfo.c_str()));
        }
    }

};