#pragma once

#include <string.h> /* for memset, memcpy */
#include <string> /* for std::string */
#include <fstream> /* for std::ofstream */
#include <iostream> /* for std::ios */
#include <ctime> /* for time, ctime */
#include <mutex> /* for mutex */
#include <memory> /* for smart pointer */

#include "str_util.hpp"

const int DEFAULT_BUF_COUNT = 4;
const int DEFAULT_BUF_SIZE = 4 * 1024 * 1024;

class LoggerCircularQueue {
private:
    struct Node {
        char* buffer;
        uint64_t used; // used length
        uint64_t size;
        std::shared_ptr<Node> next;

        Node() : buffer(nullptr), used(0), size(0), next() {}

        Node(const uint64_t& _size,
            std::shared_ptr<Node> _next = nullptr) :
            used(0),
            size(_size), 
            next(_next) {
                buffer = new char[size];
                memset(buffer, 0, sizeof(buffer));
        }

        ~Node() {
            delete buffer;
            buffer = nullptr;
        }
    };

    std::shared_ptr<Node> head;
    std::shared_ptr<Node> tail;
    FILE* fp;

public:
    LoggerCircularQueue(const int& bufCount = DEFAULT_BUF_COUNT,
        const int& bufSize = DEFAULT_BUF_SIZE) {
        head = std::make_shared<Node>(bufSize);
        tail = std::make_shared<Node>(bufSize);
        head->next = tail;
        for (int i = 2; i < bufCount; ++i) {
            std::shared_ptr<Node> newNode = std::make_shared<Node>(bufSize);
            tail->next = newNode;
            tail = newNode;
        }
        tail->next = head;
        tail = head;
    }

    void setFile(const char* filePath) {
        fp = fopen(filePath, "wa");
    }

    bool isEmpty() { return head == tail && 0 == head->used; }
    bool isFull() { return head == tail && head->used == head->size; }

    void push(const char* data, int len) {
        int pos = 0;
        while (len) {
            if (isFull()) {
                pop();
            }
            int freeLen = head->size - head->used;
            if (len >= freeLen) {
                memcpy(head->buffer + head->used, data + pos, freeLen);
                head->used += freeLen;
                pos += freeLen;
                len -= freeLen;
                head = head->next;
            }
            else {
                memcpy(head->buffer + head->used, data + pos, len);
                head->used += len;
                pos += len;
                len = 0;
            }
        }
    }

    char* pop() {
        int size = tail->used;
        char* ret = new char[size];
        memcpy(ret, tail->buffer, size);
        tail->used = 0;
        tail = tail->next;
        fwrite(ret, size, 1, fp);
        return ret;
    }

    ~LoggerCircularQueue() {
        while (true) {
            if (head == tail) {
                pop();
                tail.reset();
                break;
            }
            std::shared_ptr<Node> pNode = tail;
            pop();
            pNode.reset();
        }
        if (fp) {
            fclose(fp);
        }
    }

};

/* A simple logger, just record the message to log file */
class Logger {
private:
    LoggerCircularQueue lcq;

public:
    ~Logger() {}

    static std::shared_ptr<Logger> getInstance() {
        static std::shared_ptr<Logger> instance = std::make_shared<Logger>();
        return instance;
    }

    static inline void setLogPath(const std::string& logFilePath) {
        getInstance()->lcq.setFile(logFilePath.c_str());
    }

    static inline void log(const std::string& content) {
        time_t now = time(0);
        std::string timeStr(ctime(&now));
        timeStr.erase(timeStr.end() - 1); // erase the \n
        std::string output = StringUtil::format("%s: %s\n", timeStr.c_str(), content.c_str());
        getInstance()->lcq.push(output.c_str(), output.length());
    }

public:
    explicit Logger() {}

};