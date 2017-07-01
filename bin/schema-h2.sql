CREATE TABLE Customer_Master
( customer_id  varchar2(50),
  customer_name varchar2(50) NOT NULL,
  customer_password varchar2(50)NOT NULL,
  SSO   varchar2(50),
  First_Name varchar2(50) ,
  Middle_Name varchar2(50),
  Last_Name varchar2(50) ,
  Mobile_Number varchar2(50) ,
  Email_Id varchar2(50) ,
  ID_card_Type varchar2(50) ,
  ID_card_Number varchar2(50) ,
  City varchar2(50) ,
  Country varchar2(50) ,   
  User_Photo varchar2(30000),  
  CONSTRAINT customers_pk PRIMARY KEY (customer_id)
);