# Information Retrieval System

## Overview

This Employee Management System, developed for the 'Introduction to Database' course at KAU in 2023, enables efficient employee data management through a user-friendly interface.

### Demo Video

To understand more about how to use this system, please watch the following video

⬇️ click the thumbnail to watch video

[![Demo Video](https://github.com/Wendy-Nam/JDBC_DB_Application/blob/main/images/thumbnail.png)](http://www.youtube.com/watch?v=MUagyuR_w_A "Information Retrieval System")

The system is organized into four main navigation bars: Main screen, Database viewing/search, Add new employee, and Delete/Update employee data.

## Course Project Information

- **Project Title:** JDBC Term Project
- **Course:** Introduction to Database
- **Institution:** Korea Aerospace University (KAU)
- **Year:** 2023

### Development Environment and Technologies

- **Programming Language:** Java
- **Database Management:** MySQL with JDBC (Java Database Connectivity)
- **GUI Design:** Eclipse Java Swing Designer (Window Builder) & Figma

### Setting Up Database Connection

Before using the system, ensure that the database connection is properly configured. In the `connectDB()` method, update the SQL database credentials (username, password, database name) to match your own setup.

- **Username:** Your MySQL username
- **Password:** Your MySQL password
- **Database Name:** The name of your database in MySQL

Ensure these details are correct to establish a successful connection to your MySQL database.

### 1. Main Screen

The Main screen provides an overview of the system, team member details, and guidance on how to use each sidebar.

- **Overview:** Describes the functionalities and benefits of the system.
- **Members:** Lists information about each team member.
- **How to Use:** Shows functionalities available in each sidebar. This is not a part of the core assignment but serves as a guide for first-time users.

### 2. Database Viewing / Search

This section allows users to view reports and perform searches based on various criteria.

- **Default Range Setting:** "All" is set as the default value.
- **Search Conditions:** Checkboxes for search conditions are set to true by default. Clicking the 'Search' button displays all employees in the EMPLOYEE table.

Users can select specific ranges in the combo box. The selection will affect the search results.

### 3. Add New Employee

This section is dedicated to adding new employees to the system. Users can input necessary information and confirm to add a new employee.

### 4. Delete / Update Employee Data

This section enables users to delete or update existing employee data.

- **Delete:** To delete an employee's information, select the desired employee and press the red delete button.
- **Update:** To update, select an employee and enter the new data, then press the green update button.

Icons are used for delete (trash can) and update (upward arrow) functions. The update functionality was added beyond the original assignment requirements.

### Additional Functionalities

- **Search Button:** Executes a SELECT SQL query. The final query depends on the selected checkboxes.
- **Combining Names:** The system combines `fname`, `minit`, and `lname` from the database to display a full name.
- **Search by Attributes:** Users can select an attribute to search for and input a specific value in the search box.
- **Error Handling:** If no matching results are found, or if data format mismatches, the system returns a 'no results' message.
- **Primary Key Deletion:** Deletion is based on the primary key (ssn). If ssn is not selected, deletion is disabled to prevent issues.

### Error Handling and Notifications

The system informs users about various situations through different types of windows:

1. **SQL Error:** Triggered by issues in SQL query execution, such as syntax errors or connection problems.

2. **Input Validation Error:** Appears when user inputs do not match expected formats or criteria.

3. **Database Connection Error:** Displayed when there is a failure in connecting to the database, possibly due to incorrect credentials or server issues.

4. **Unspecified Behavior Error:** Used for situations not covered by other errors, like attempting updates without necessary data (e.g., missing employee's SSN).

## Team Members

- [@남서아](https://github.com/Wendy-Nam)
- [@허진혁](https://github.com/jinhyeok0117)
- [@안희수](heesuya617@gmail.com)

