# Personal Budget System
A JavaFX desktop application for managing personal finances with dashboards, forms, and charts. Built in a component-based architecture and used Python/Pandas for data preparation.
___
## Motivation
As a person who works independently, I’ve always had the habit of tracking my finances. It began with a simple Google Sheets table then expanded to a full SQL database paired with Google Looker for visualization and a custom JavaScript web form to insert new records. This setup, however, had limitations:

* The web form only allowed data insertion. It didn’t support updating, deleting, or reading data.
* Google Looker had limits that prevented me from creating some custom charts I needed, such as monthly balance growth over a certain number of months.
* I had to view the data from one place and insert from another place which was bothersome.
* The data was plain open for anyone who enters my laptop to see or edit as there was no login page.

This university project came up at the right time. It gave me a reason to build the system, and so came the 2-in-1 solution: a system that satisfies the project requirements and serves as my own personal budgeting system.

I used Google Looker’s dashboards as inspiration for my UI design but also tried to implement best practices in UI conventions and user experience, taking into consideration good design layout, good user journey, and ease of use.

To make sure it works on my database, I generated mock data that mimics my actual database data, preparing and cleaning it with Python, NumPy, and Pandas then seeding it into SQLite. After submission, I plan to connect the application to my database and use it as my personal budgeting system.

This project is fully functional, not just a prototype, designed to address the limitations of my previous system.

### Summary
* Used SQL + Google Looker + insert-only custom JS web form.
* Hit limits: No CRUD operations, Google Looker chart constraints, insert from one place, view from another, no login page.
* University project came at the right timing.
* Built a 2-in-1 solution: A JavaFX desktop app with login page, full CRUD, and custom dashboards, seeded with mock data prepared via Python/NumPy/Pandas and stored in SQLite.
___
## Features
* **Authentication**: Login page with validation and error handling.
* **Dashboards**: Income, Receivables, Savings, Investments, Payables, Bills.
* **View/Edit Modes**: Switch between dashboards and forms.
* **Forms**: Smart state-changing CRUD user interface with validation and CRUD flows.
* **Settings**: Change name, username, password, full validation.
* **Data Handling**: SQLite embedded DB, seeded with prepared mock data.
* **Minimal UI**: Design inspired by Google Looker following UI/UX best practices.
___
## Architecture & Tech Stack
* **Language & Frameworks**: Java, JavaFX.
* **Database**: SQLite (embedded with seed data).
* **Structure**: Component-based (React-style structure).
* **Testing**: SchemaLoader, DAO testers, DatabaseTester.
* **Data Preparation**: Python, NumPy, Pandas for preparing and cleaning web-generated mock data.
* **Design Patterns**: Abstract classes for reusability, enums for different states such as modes/views.
___
