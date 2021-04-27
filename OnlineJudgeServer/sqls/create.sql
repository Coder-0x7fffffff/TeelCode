CREATE TABLE Login(
	u_id VARCHAR PRIMARY KEY,
	u_pwd CHAR(16)
)

CREATE TABLE User(
	u_id VARCHAR PRIMARY KEY，
	u_name VARCHAR,
	u_sex INTEGER,
	u_dscp VARCHAR
	FOREIGN KEY(u_id) REFERENCES User(u_id),
)

CREATE TABLE Problem(
	p_id INTEGER PRIMARY KEY,
	p_name VARCHAR,
	p_difficulty INTEGER,
	p_pass INTEGER,
	p_submit INTEGER,
	p_dscp VARCHAR,
	p_inputs VARCHAR,
	p_outputs VARCHAR
)

CREATE TABLE Classification(
	c_id INTEGER PRIMARY KEY,
	c_name VARCHAR
)

CREATE TABLE ProblemClassification(
	p_id INTEGER,
	c_id INTEGER,
	FOREIGN KEY(p_id) REFERENCES Problem(p_id),
	FOREIGN KEY(c_id) REFERENCES Classification(c_id),
)

CREATE TABLE UserProblem(
	u_id VARCHAR,
	p_id INTEGER,
	p_state INTEGER,
	FOREIGN KEY(u_id) REFERENCES User(u_id),
)

CREATE TABLE Record(
	p_id INTEGER,
	u_id VARCHAR,
	p_state INTEGER,
	r_time DATE,
	r_timewaste INTEGER,
	r_memory INTEGER,
	r_code TEXT,
	r_code_type INTEGER,
	r_info TEXT,
	FOREIGN KEY(p_id) REFERENCES Problem(p_id),
	FOREIGN KEY(u_id) REFERENCES User(u_id),
)