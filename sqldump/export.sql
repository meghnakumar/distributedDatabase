CREATE DATABASE IF NOT EXISTS demo;
USE demo;
DROP TABLE IF EXISTS persons;
CREATE table persons(name varchar(255), id int, primary key (id), foreign key (id) references orders(id);
