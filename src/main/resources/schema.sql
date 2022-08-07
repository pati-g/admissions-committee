--DROP DATABASE IF EXISTS admissions_mgmt;
--CREATE DATABASE admissions_mgmt;
--USE admissions_mgmt;
DROP TABLE IF EXISTS Users, Applicants, Faculties, Scores, Registrations, Statements;
CREATE TABLE Users (
ID bigint PRIMARY KEY AUTO_INCREMENT,
email varchar(255) NOT NULL UNIQUE,
password varchar(255) NOT NULL,
role varchar(255) DEFAULT 'USER',
is_blocked boolean DEFAULT 'false'
);

CREATE TABLE Applicants (
ID bigint PRIMARY KEY AUTO_INCREMENT,
first_name varchar(255) NOT NULL,
last_name varchar(255) NOT NULL,
city varchar(255) NOT NULL,
region varchar(255) NOT NULL,
educational_institution varchar(255) NOT NULL,
certificate varchar(255),
user_ID bigint NOT NULL UNIQUE,
FOREIGN KEY (user_ID) REFERENCES Users(ID)
);

CREATE TABLE Faculties (
ID bigint PRIMARY KEY AUTO_INCREMENT,
name varchar(255) NOT NULL UNIQUE,
budget_places int NOT NULL,
total_places int NOT NULL
);

CREATE TABLE Scores (
ID bigint PRIMARY KEY AUTO_INCREMENT,
applicant_ID bigint NOT NULL,
subject_name varchar(255) NOT NULL,
grade_or_score char(1) NOT NULL,
result varchar(4) NOT NULL,
FOREIGN KEY (applicant_ID) REFERENCES Applicants(ID)
);
CREATE TABLE Application_Requests (
ID bigint PRIMARY KEY AUTO_INCREMENT,
applicant_ID bigint NOT NULL,
faculty_ID bigint NOT NULL,
requested_on timestamp NOT NULL,
FOREIGN KEY (applicant_ID) REFERENCES Applicants(ID),
FOREIGN KEY (faculty_ID) REFERENCES Faculties(ID)
);

CREATE TABLE Statements (
ID bigint PRIMARY KEY AUTO_INCREMENT,
request_ID bigint NOT NULL,
points int NOT NULL,
enrollment char(1) NOT NULL,
FOREIGN KEY (request_ID) REFERENCES Application_Requests (ID)
);