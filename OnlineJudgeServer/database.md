# OJ系统数据库设计 v0.1

## 登陆表(Login)

| 字段名 | 字段类型 | 字段说明 |
| ------ | -------- | -------- |
| u_id   | VARCHAR  | 用户id   |
| u_pwd  | CHAR(16) | 用户密码 |
| u_question   | VARCHAR  | 安全问题   |
| u_answer  | VARCHAR | 问题答案 |

## 用户表(OJUser)

| 字段名 | 字段类型 | 字段说明 |
| ------ | -------- | -------- |
| u_id   | VARCHAR  | 用户id   |
| u_name | VARCHAR  | 用户昵称 |
| u_sex  | INTEGER  | 用户性别 |
| u_dscp | VARCHAR  | 用户描述 |

## 题目表(Problem)

| 字段名       | 字段类型 | 字段说明     |
| ------------ | -------- | ------------ |
| p_id         | INTEGER  | 题目id       |
| p_name       | VARCHAR  | 题目名称     |
| p_difficulty | INTEGER  | 题目难度     |
| p_pass       | INTEGER  | 题目通过次数 |
| p_submit     | INTEGER  | 题目提交次数 |
| p_dscp       | TEXT     | 题目描述     |
| p_inputs     | VARCHAR  | 题目输入     |
| p_outputs    | VARCHAR  | 题目输出     |

## 分类表(Classification)

| 字段名 | 字段类型 | 字段说明 |
| ------ | -------- | -------- |
| c_id   | INTEGER  | 分类id   |
| c_name | VARCHAR  | 分类名   |

## 题目分类表(ProblemClassification)

| 字段名 | 字段类型 | 字段说明 |
| ------ | -------- | -------- |
| p_id   | INTEGER  | 题目id   |
| c_id   | INTEGER  | 分类id   |

## 用户题目表(UserProblem)

| 字段名  | 字段类型 | 字段说明                |
| ------- | -------- | ----------------------- |
| u_id    | VARCHAR  | 用户id                  |
| p_id    | INTEGER  | 题目id                  |
| p_state | INTEGER  | 题目状态（完成/未完成） |

说明:如果某个题目的解答被用户提交过就会被记录到该表中

## 历史记录表(Record)

| 字段名      | 字段类型 | 字段说明       |
| ----------- | -------- | -------------- |
| p_id        | INTEGER  | 题目id         |
| u_id        | VARCHAR  | 用户id         |
| p_state     | INTEGER  | 题目状态       |
| r_time      | DATE     | 提交时间       |
| r_timewaste | INTEGER  | 时间消耗       |
| r_memory    | INTEGER  | 内存消耗       |
| r_code      | TEXT     | 用户提交的代码 |
| r_code_type | INTEGER  | 用户使用的语言 |
| r_info      | TEXT     | 提交后的信息   |

说明:该表用来表示每个题目的提交历史记录,题目状态和用户题目表中的不一样,提交后的信息是指提交后的成功或者报错信息