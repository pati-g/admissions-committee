DROP DATABASE IF EXISTS admissions_mgmt;
CREATE DATABASE admissions_mgmt;
USE admissions_mgmt;
CREATE TABLE Users (
ID bigint PRIMARY KEY AUTO_INCREMENT,
Username varchar(255) NOT NULL UNIQUE,
Password varchar(255) NOT NULL,
Role varchar(255) DEFAULT 'user'
);

CREATE TABLE Applicants (
ID bigint PRIMARY KEY AUTO_INCREMENT,
FirstName varchar(255) NOT NULL,
LastName varchar(255) NOT NULL,
Email varchar(255) NOT NULL,
City varchar(255) NOT NULL,
Region varchar(255) NOT NULL,
EducationalInstitution varchar(255) NOT NULL,
Certificate varchar(255),
UserID bigint NOT NULL,
FOREIGN KEY (UserID) REFERENCES Users(ID)
);

CREATE TABLE Faculties (
ID bigint PRIMARY KEY AUTO_INCREMENT,
Name varchar(255) NOT NULL,
BudgetPlaces int NOT NULL,
TotalPlaces int NOT NULL
);

CREATE TABLE Registrations (
ID bigint PRIMARY KEY AUTO_INCREMENT,
Username varchar(255) NOT NULL UNIQUE,
Password varchar(255) NOT NULL,
Role varchar(255) DEFAULT 'user',
ApplicantID bigint NOT NULL,
FOREIGN KEY (ApplicantID) REFERENCES Applicants(ID),
FacultyID bigint NOT NULL,
FOREIGN KEY (FacultyID) REFERENCES Faculties(ID)
);