create database Bankingsystem;
use Bankingsystem;
create table accounts(
acc_no int unique,
name VARCHAR(50) NOT NULL,
email VARCHAR(50) UNIQUE,
password VARCHAR(20) NOT NULL,
balance DECIMAL(10, 2) NOT NULL
);
insert into accounts (name, email, password, balance)
values
    ('venkat', 'venkat@gmail.com', 'venky12', 5000.00),
    ('neeraj', 'neeraj@gmail.com', 'neer@12', 7500.00),
    ('nani', 'nani@gmail.com', 'sknani', 10000.00);

select * from accounts;
