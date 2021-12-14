create table if not exists Card (
  id varchar(4) not null,
  number varchar(25) not null,
  expiration DATE not null,
  cvc varchar(3) not null
);