CREATE TABLE User_Master
( UserId  varchar2(50),
  EmailAddress varchar2(50),
  ContactNumber number(20),
  FirstName varchar2(50),
  MiddleName varchar2(50),
  LastName varchar2(50),
  UserType varchar2(10),
  LoginPassword varchar2(50),
  CreatedOn DATE,
  PasswordChangedOn DATE,
  CONSTRAINT customers_pk PRIMARY KEY (UserId),
  CONSTRAINT USER_EmailAddress UNIQUE(EmailAddress,ContactNumber)
);