
CREATE TABLE Customer
( customer_id  varchar2(50) NOT NULL,
  customer_name varchar2(50) NOT NULL,
  customer_password varchar2(50),
  CONSTRAINT customers_pk PRIMARY KEY (customer_id)
);

