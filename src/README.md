Project Overview

This project was created as part of the Accounting Ledger Capstone Project for the Year Up United Software Development track.

It is a simple Command Line Interface (CLI) application that allows users to manage their financial transactions. The application provides functionalities to deposit, payments, and view the transaction history. It also includes features to calculate the current balance and generate reports based on the transactions.

The application runs in a terminal and is designed to be user-friendly, allowing users to easily navigate through the options and perform their desired actions. It is built using Java and utilizes a simple data structure to store and manage the transactions. Along with storing all transactions data in a csv file for persistence.

The ledger comes with some pre-generated sample data to demonstrate how the application works.
sz
Features

- Deposit: Users can add a deposit transaction by providing the amount, date, and description.
- Payment: Users can add a payment transaction by providing the amount, date, and description.
- View Transactions: Users can view a list of all transactions, including deposits and payments, along with their details.
- Calculate Balance: The application calculates the current balance based on the transactions recorded.
- Generate Reports: Users can generate reports based on the transactions, such as monthly summaries or category

How it works

All transactions are stored in a CSV file, which allows for data persistence.

Transactions are stored in a CSV file using the format:


When the application starts, it reads the transactions from the CSV file and loads them into memory.

When a new transaction is added, it is stored in an ArrayList and it is appended to the CSV file. 





