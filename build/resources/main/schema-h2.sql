CREATE TABLE User_Master
( userid  varchar2(50),
  emailaddress varchar2(50),
  contactnumber varchar2(50),
  firstname varchar2(50),
  middlename varchar2(50),
  lastname varchar2(50),
  usertype varchar2(10),
  loginpassword varchar2(50),
  createdon varchar2(10),
  passwordchangedOn varchar2(10),  
  CONSTRAINT customers_pk PRIMARY KEY (UserId),
  CONSTRAINT USER_EmailAddress UNIQUE(EmailAddress,ContactNumber)
);


