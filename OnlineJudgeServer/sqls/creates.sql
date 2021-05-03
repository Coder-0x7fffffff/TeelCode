CREATE TABLE Login(
	u_id VARCHAR(16) NOT NULL PRIMARY KEY,
	u_pwd CHAR(16),
    u_problem VARCHAR(100),
    u_answer VARCHAR(100),
    u_type INTEGER DEFAULT 0
) DEFAULT CHARSET=utf8;

CREATE TABLE OJUser(
	u_id VARCHAR(16) NOT NULL PRIMARY KEY,
	u_name VARCHAR(16) NOT NULL,
	u_sex INTEGER,
	u_dscp VARCHAR(100),
    u_img BLOB,
	FOREIGN KEY(u_id) REFERENCES Login(u_id)
) DEFAULT CHARSET=utf8;

CREATE TABLE Problem(
	p_id INTEGER PRIMARY KEY,
	p_name VARCHAR(100) NOT NULL,
	p_difficulty INTEGER NOT NULL,
	p_pass INTEGER NOT NULL,
	p_submit INTEGER NOT NULL,
	p_dscp TEXT NOT NULL,
	p_inputs VARCHAR(1024) NOT NULL,
	p_outputs VARCHAR(1024) NOT NULL
) DEFAULT CHARSET=utf8;

CREATE TABLE Classification(
	c_id INTEGER,
	c_name VARCHAR(32),
    PRIMARY KEY(c_id, c_name)
) DEFAULT CHARSET=utf8;

CREATE TABLE ProblemClassification(
	p_id INTEGER,
	c_id INTEGER,
	FOREIGN KEY(p_id) REFERENCES Problem(p_id),
	FOREIGN KEY(c_id) REFERENCES Classification(c_id)
) DEFAULT CHARSET=utf8;

CREATE TABLE UserProblem(
	u_id VARCHAR(16),
	p_id INTEGER,
	p_state INTEGER,
	FOREIGN KEY(u_id) REFERENCES Login(u_id)
) DEFAULT CHARSET=utf8;;

CREATE TABLE Record(
	p_id INTEGER,
	u_id VARCHAR(16),
	p_state INTEGER,
	r_time DATETIME,
	r_time_usage INTEGER,
	r_mem_usage INTEGER,
	r_code TEXT,
	r_code_type INTEGER,
	r_info TEXT,
	FOREIGN KEY(p_id) REFERENCES Problem(p_id),
	FOREIGN KEY(u_id) REFERENCES Login(u_id)
) DEFAULT CHARSET=utf8;

CREATE TABLE Comments(
	c_id INTEGER PRIMARY KEY,
    u_id VARCHAR(16) NOT NULL,
    p_id INTEGER,
    c_fa INTEGER,
    c_details TEXT,
    c_time DATETIME,
	FOREIGN KEY(p_id) REFERENCES Problem(p_id),
	FOREIGN KEY(u_id) REFERENCES Login(u_id)
) DEFAULT CHARSET=utf8;
