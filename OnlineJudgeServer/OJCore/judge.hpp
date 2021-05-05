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
#include "tiny_json.hpp"

#define CP_NULL ((char*)0)

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

    /* just execute the code, no output */
    int execute(const std::string& code, const std::string& dirPath, const std::string& input, LanguageType languageType) {
        int exitCode = 1;
        createFiles(dirPath, languageType);
        if (0 == this->input(code, dirPath, languageType)) {
            if (0 == compile(dirPath, languageType)) {
                run(input, dirPath, languageType, 0);
                exitCode = 0;
            }
        }
        output();
        return exitCode;
    }

    int judge(const std::string& code, const std::string& dirPath, const std::string& samplePath, LanguageType languageType) {
        int exitCode = 1;
        createFiles(dirPath, languageType);
        if (0 == input(code, dirPath, languageType)) {
            if (0 == compile(dirPath, languageType)) {
                run(samplePath, dirPath, languageType);
                exitCode = 0;
            }
        }
        output();
        return exitCode;
    }

private:
    /* 0. Create Files */
    void createFiles(const std::string& dirPath, LanguageType languageType) {
        const std::string filePath = dirPath + "/Main";
        FileUtil::createFile(logFilePath = FileUtil::logPath(filePath));
        Logger::setLogPath(logFilePath);
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
        case LanguageType::PYTHON2: case LanguageType::PYTHON3:
            srcFilePath = filePath + PYTHON_SUFFIX;
            break;
        default:
            break;
        }
        FileUtil::createFile(srcFilePath);
    }

    /* 1. input code and specify the language */
    int input(const std::string& code, const std::string& dirPath, LanguageType languageType) {
        Logger::log(StringUtil::format("Input!..."));
        /* Create SyscallTracer */
        pSyscallTracer = std::make_unique<SyscallTracer>(languageType);
        /* Write code */
        Logger::log(StringUtil::format("Write code to %s", srcFilePath.c_str()));
        return FileUtil::writeFile(srcFilePath, code);
    }

    void compileMain(char** command) {
        /* Child process */
        /* Create Compile Info File */
        int fd = open(compileErrorFilePath.c_str(), O_WRONLY | O_CREAT, 0666);
        if (-1 == fd) {
            Logger::log(StringUtil::format("Open compileError file error"));
            Logger::log(StringUtil::format("Child process exit(1)"));
            /* Child process abnormal exit */
            exit(1);
        }

        std::string executedCommand;
        for (int i = 0; NULL != command[i]; ++i) {
            executedCommand += command[i];
            executedCommand += " ";
        }

        Logger::log(StringUtil::format("Execute: %s", executedCommand.c_str()));
        /* Execute command */
        dup2(fd, 2);
        execvp(command[0], command);
        Logger::log(StringUtil::format("Child process exit(0)"));
        /* Child process normal exit */
        exit(0);
    }

    int watchCompile(int pid) {
        Logger::log(StringUtil::format("Waiting Child process! pid=%d", pid));
        /* Wait child process */
        waitpid(pid, NULL, 0);
        /* Child process finished */
        Logger::log(StringUtil::format("Child process finished!"));
        /* If compile is success, there muse be an executable file */
        if (!FileUtil::isExist(compileErrorFilePath) || 
            0 != FileUtil::getFileSize(compileErrorFilePath)) {
            Logger::log(StringUtil::format("Compile %s failed!", srcFilePath.c_str()));
            resultSet.resultCode = CE;
            FileUtil::readFile(compileErrorFilePath, resultSet.resultInfo);
            resultSets.push_back(resultSet);
            return 1;
        }
        Logger::log(StringUtil::format("Compile %s OK!", srcFilePath.c_str()));
        return 0;
    }

    /* 2. compile */
    int compile(const std::string& dirPath, LanguageType languageType) {
        if (LanguageType::PYTHON2 == languageType || 
        LanguageType::PYTHON3 == languageType) {
            /* python no compile */
            return 0;
        }
        Logger::log(StringUtil::format("Compile!..."));

        /* Set compile command */
        const char* C_CMD[] = {"gcc", srcFilePath.c_str(), "-o", exeFilePath.c_str(), "-O2", "-fmax-errors=10", "-Wall",
						  "-lm", "--static", "-std=c99", "-DONLINE_JUDGE", CP_NULL};
        const char* CPP_CMD[] = {"g++", "-fno-asm", "-fmax-errors=10", "-Wall",
						  "-lm", "--static", "-std=c++11", "-DONLINE_JUDGE", "-o", exeFilePath.c_str(), srcFilePath.c_str(), CP_NULL};
        const char* JAVA_CMD[] = { "javac", "-J-Xms32m", "-J-Xmx256m", "-encoding", "UTF8", srcFilePath.c_str(), CP_NULL };

        char** command = nullptr;

        if (LanguageType::C == languageType) {
            command = const_cast<char**>(C_CMD);
        }
        else if (LanguageType::CPP == languageType) {
            command = const_cast<char**>(CPP_CMD);
        }
        else if (LanguageType::JAVA == languageType) {
            command = const_cast<char**>(JAVA_CMD);
        }
        Logger::log(StringUtil::format("fork!..."));
        int pid = fork();
        if (0 == pid) {
            compileMain(command);
        }
        return watchCompile(pid);
    }

    void runMain(int timeLimit, int memLimit, LanguageType languageType) {
        /* Child process */
        /* Set limmit */
        rlimit r;
        getrlimit(RLIMIT_CPU, &r);
        r.rlim_cur = timeLimit / 1000;
        r.rlim_max = timeLimit / 1000;
        setrlimit(RLIMIT_CPU, &r);

        alarm(0);

        if (LanguageType::JAVA != languageType) {
            getrlimit(RLIMIT_AS, &r);
            r.rlim_cur = memLimit * 1024;
            r.rlim_max = memLimit * 1024;
            setrlimit(RLIMIT_AS, &r);
        }

        /* Redirect stdin stdout stderr */
        freopen(stdinFilePath.c_str(), "r", stdin);
        freopen(stdoutFilePath.c_str(), "w", stdout);
        freopen(stderrFilePath.c_str(), "w", stderr);

        if (-1 == SyscallTracer::traceMe()) {
            fprintf(stderr, "failed to ptrace : %s\n", strerror(errno));
            /* Chile process abnormal exit */
            exit(1);
        }
        if (LanguageType::C == languageType ||
        LanguageType::CPP == languageType) {
            execl(exeFilePath.c_str(), exeFilePath.c_str(), CP_NULL);
        }
        else if (LanguageType::JAVA == languageType) {
            const char* JAVA_CMD[] = { "java", exeFilePath.c_str(), CP_NULL };
            execvp(JAVA_CMD[0], const_cast<char**>(JAVA_CMD));
        }
        else if (LanguageType::PYTHON2 == languageType) {
            const char* PY2_CMD[] = { "python2", srcFilePath.c_str(), CP_NULL };
            execvp(PY2_CMD[0], const_cast<char**>(PY2_CMD));
        }
        else if (LanguageType::PYTHON3 == languageType) {
            const char* PY3_CMD[] = { "python3", srcFilePath.c_str(), CP_NULL };
            execvp(PY3_CMD[0], const_cast<char**>(PY3_CMD));
        }
        /* Chile process normal exit */
        exit(0);
    }

    void watchRun(int pid, int timeLimit, int memLimit, LanguageType languageType) {
        struct rusage rused;
        int status = 0;

        bool first = true;

        for (;;) {

            if (-1 == wait4(pid, &status, __WALL, &rused)) {
                Logger::log(StringUtil::format("wait4 failed! pid=%d", pid));
                resultSet.resultCode = RE;
                break;

            }

            if (first) {
                first = false;
                if (-1 == pSyscallTracer->trace(pid)) {
                    Logger::log(
                    StringUtil::format("ptrace(PTRACE_SETOPTIONS, %d, 0, PTRACE_O_TRACESYSGOOD | PTRACE_O_TRACEEXIT) error: %s", pid, strerror(errno)));
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
                Logger::log(StringUtil::format("%s is not empty", stderrFilePath.c_str()));
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
                Logger::log(StringUtil::format("exit code=%d", exitCode));
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
                Logger::log(StringUtil::format("WIFSIGNALED(status)=%d", sig));
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
                    Logger::log(StringUtil::format("child process %d received SIGSEGV, killing... ", pid));
                    if (-1 == kill(pid, SIGKILL)) {
                        Logger::log(StringUtil::format("Failed %s", strerror(errno)));
                    }
                    else {
                        Logger::log(StringUtil::format("Done!"));
                    }
                }
                else if (WIFSTOPPED(status) && (WSTOPSIG(status) & 0x80)) {
                    long callid = ptrace(PTRACE_PEEKUSER, pid, WORD_SIZE * ORIG_EAX, NULL); // get syscall num
                    if (callid >= NR_syscalls) {
                        Logger::log(StringUtil::format("syscallid=%ld", callid));
                        resultSet.resultCode = RE;
                        break;
                    }
                    if ((*pSyscallTracer)[callid]) {
                        --(*pSyscallTracer)[callid];
                    }
                    else {
                        Logger::log(StringUtil::format("child process %d is trying to invoke the banned system call, kill it\n", pid));
                        resultSet.resultCode = RE;
                        pSyscallTracer->killChild(pid);
                        break;
                    }
                }
            }

            /* Let child process continue and still ptrace it */
            if (-1 == pSyscallTracer->traceSyscall(pid)) {
                Logger::log(StringUtil::format("ptrace(PTRACE_SYSCALL, %d, 0, 0) error: %s", pid, strerror(errno)));
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

        Logger::log(StringUtil::format("Current: time usage=%d ms, memory usage=%d kb", 
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
        Logger::log(StringUtil::format("Run!..."));
        /* For c & cpp, the time waste limit is 1s, memory limit is 64MB, others are 2s and 128MB */
        int timeLimit = 1000;      // 1 s
        int memLimit = 640 * 1024; // 64 MB
        if (LanguageType::C != languageType && LanguageType::CPP != languageType) {
            timeLimit *= 2;
            memLimit *= 2;
        }
        Logger::log(StringUtil::format("fork!..."));
        /* How many sample(test case) */
        for (int i = 1; FileUtil::isExist(StringUtil::format("%s/%d.in", samplePath.c_str(), i)); ++i) {
            /* clear */
            resultSet = ResultSet();
            std::string sampleOutFilePath = StringUtil::format("%s/%d.out", samplePath.c_str(), i);
            /* write .in file here */
            std::string sampleInFilePath = StringUtil::format("%s/%d.in", samplePath.c_str(), i);
            stdinFilePath = sampleInFilePath;
            int pid = fork();
            if (0 == pid) {
                runMain(timeLimit, memLimit, languageType);
            }
            watchRun(pid, timeLimit, memLimit, languageType);
            judgeMain(sampleOutFilePath);
        }
    }

    /* execute version */
    int run(const std::string& input, const std::string& dirPath, LanguageType languageType, int) {
        Logger::log(StringUtil::format("Run!..."));
        /* For c & cpp, the time waste limit is 1s, memory limit is 64MB, others are 2s and 128MB */
        int timeLimit = 1000;      // 1 s
        int memLimit = 640 * 1024; // 64 MB
        if (LanguageType::C != languageType && LanguageType::CPP != languageType) {
            timeLimit *= 2;
            memLimit *= 2;
        }
        Logger::log(StringUtil::format("fork!..."));
        resultSet = ResultSet();
        FileUtil::writeFile(stdinFilePath, input);
        int pid = fork();
        if (0 == pid) {
            runMain(timeLimit, memLimit, languageType);
        }
        watchRun(pid, timeLimit, memLimit, languageType);
        resultSets.push_back(resultSet);
    }

    /* 4. output the result */
    void output() {
        tinyjson::Object object;
        /* If CE, count = 1 */
        int count = 0;
        for (ResultSet rs : resultSets) {
            ++count;
            Logger::log(StringUtil::format("{ Result Code: %d, Time Used: %d ms, Memory Used: %d kb, Result Info: %s }",
            rs.resultCode, rs.timeUsage, rs.memUsage, rs.resultInfo.c_str()));
            tinyjson::Object sampleObject;
            sampleObject["ResultCode"] = rs.resultCode;
            sampleObject["TimeUsed"] = rs.timeUsage;
            sampleObject["MemoryUsed"] = rs.memUsage;
            sampleObject["ResultInfo"] = rs.resultInfo;
            object[std::to_string(count)] = sampleObject;
        }
        object["count"] = count;
        tinyjson::Json json(object);
        FileUtil::writeFile(resultFilePath, json.serialize());
    }

};