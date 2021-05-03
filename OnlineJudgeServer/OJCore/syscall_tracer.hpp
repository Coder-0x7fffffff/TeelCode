#pragma once

#include <sys/reg.h> /* for ORIG_RAX */
#include <sys/syscall.h> /* for syscalls */
#include <sys/ptrace.h> /* for ptrace */

#ifdef __x86_64__
    #define ORIG_EAX ORIG_RAX
#endif

/* see <asm-generic/unistd.h> */
#define NR_syscalls 333

enum class LanguageType : int {
    C, CPP, JAVA, PYTHON2, PYTHON3
};

/* white list table for syscall */
/* from https://github.com/zhblue/hustoj/blob/master/trunk/core/judge_client/okcalls64.h */

// c&cpp
int LANG_CV[NR_syscalls] = { 0,1,2,4,5,9,11,12,21,59,63,89,158,231,240,257,8, SYS_time, SYS_read, SYS_uname, SYS_write, SYS_open,
		SYS_close, SYS_execve, SYS_access, SYS_brk, SYS_munmap, SYS_mprotect,
		SYS_mmap, SYS_fstat, SYS_set_thread_area, 252, SYS_arch_prctl, 231, SYS_openat, -1 };

// java
int LANG_JV[NR_syscalls] = {
        SYS_read, SYS_mprotect, SYS_getuid, SYS_getgid, SYS_geteuid, SYS_getegid, SYS_munmap, SYS_getppid, SYS_getpgrp,
        SYS_brk, SYS_rt_sigaction, SYS_rt_sigprocmask, SYS_prctl, SYS_arch_prctl, SYS_ioctl, SYS_pread64, SYS_open,
        SYS_futex, SYS_set_thread_area, SYS_access, SYS_getdents64, SYS_set_tid_address, SYS_pipe, SYS_exit_group,
        SYS_openat, SYS_set_robust_list, SYS_close, SYS_prlimit64, SYS_dup2, SYS_getpid, SYS_stat, SYS_fstat, SYS_clone,
        SYS_execve, SYS_lstat, SYS_wait4, SYS_uname, SYS_fcntl, SYS_getcwd, SYS_lseek, SYS_readlink, SYS_mmap,
        SYS_getrlimit, SYS_openat, -1 };

// python
int LANG_PV[NR_syscalls] = { 0,39,186, 318,17,41,42,49,72,99,217,302,
        1,2,3,4,5,6,8,9,10,11,12,13,14,16,21,32,59,72,78,79,89,97,102,104,107,108,131,137,158,202,218,228,231,257,273,
        SYS_read, SYS_write, SYS_mprotect, SYS_getuid, SYS_getgid, SYS_geteuid, SYS_getegid, SYS_munmap, SYS_brk,
        SYS_rt_sigaction, SYS_sigaltstack, SYS_rt_sigprocmask, SYS_sched_get_priority_max, SYS_arch_prctl, SYS_ioctl,
        SYS_pread64, SYS_getxattr, SYS_open, SYS_futex, SYS_access, SYS_getdents64, SYS_set_tid_address, SYS_clock_gettime,
        SYS_exit_group, SYS_mremap, SYS_openat, SYS_unshare, SYS_set_robust_list, SYS_close, SYS_prlimit64,
        318, SYS_dup, SYS_getpid, SYS_stat, SYS_socket, SYS_connect, SYS_fstat, SYS_execve, SYS_lstat,
        SYS_exit, SYS_fcntl, SYS_getdents, SYS_getcwd, SYS_lseek, SYS_readlink, SYS_mmap, SYS_getrlimit,
        SYS_sysinfo, -1 };

class SyscallTracer {
private:

    static const int CALL_LIMIT = -1;

    int callCounter[NR_syscalls];

public:
    SyscallTracer(LanguageType languageType) {
        memset(callCounter, 0, NR_syscalls);
        switch (languageType) {
        case LanguageType::C: case LanguageType::CPP:
            for (int i = 0; -1 != LANG_CV[i]; ++i) {
                callCounter[LANG_CV[i]] = CALL_LIMIT;
            }
            break;
        case LanguageType::JAVA:
            for (int i = 0; -1 != LANG_JV[i]; ++i) {
                callCounter[LANG_JV[i]] = CALL_LIMIT;
            }
            break;
        case LanguageType::PYTHON2: case LanguageType::PYTHON3:
            for (int i = 0; -1 != LANG_PV[i]; ++i) {
                callCounter[LANG_PV[i]] = CALL_LIMIT;
            }
            break;
        default:
        /* Error */
            break;
        }
    }

    SyscallTracer(const SyscallTracer& other) = delete;
    SyscallTracer& operator=(const SyscallTracer& other) = delete;

    ~SyscallTracer() {
        memset(callCounter, 0, NR_syscalls);
    }

    /* used in child */
    static inline int traceMe() {
        return ptrace(PTRACE_TRACEME, NULL, NULL, NULL);
    }

    /* used in father */
    inline int trace(pid_t pid) {
        return ptrace(PTRACE_SETOPTIONS, pid, NULL, PTRACE_O_TRACESYSGOOD | PTRACE_O_TRACEEXIT);
    }
    
    inline int traceSyscall(pid_t pid) {
        return ptrace(PTRACE_SYSCALL, pid, NULL, NULL);
    }

    inline int killChild(pid_t pid) {
        return ptrace(PTRACE_KILL, pid, NULL, NULL);
    }

    int& operator[](const int& callid) {
        return callCounter[callid];
    }

};

