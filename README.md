# 🧩 Movie Management System – Java & MS SQL

This project is a **comprehensive movie management system** built with Java and MS SQL. It manages movies, users, ratings, genres, tags, and personalized recommendations, showcasing advanced database design, business logic implementation, and data integrity enforcement.

## ⚙️ About the Project

The system maintains records of users (username, number of rewards) and movies (title, director) with multiple genres and tags. Users can create watchlists, rate movies, and receive personalized film recommendations based on their favorite genres.  

The system enforces **behavior rules for ratings**, preventing users from overusing extreme scores in a single genre. Rewards are automatically granted for evaluating undervalued movies within a user's preferred genres. User specializations and descriptions are dynamically determined from their ratings and movie tags.

## 🧩 Key Features

* **User Management** – tracks usernames, rewards, and personal watchlists.  
* **Movie & Genre Management** – movies can belong to multiple genres and have multiple tags.  
* **Rating System with Rules** – prevents excessive extreme ratings per genre.  
* **Personalized Recommendations** – suggests movies from favorite genres not yet rated or in watchlists, including “hidden gems”.  
* **Rewards System** – users earn rewards for rating undervalued movies.  
* **User Specialization & Profile** – tracks thematic specializations and provides dynamic user descriptions (“curious”, “focused”, “undefined”).

## 🛠️ Technology Stack

* **Language**: Java  
* **Database**: MS SQL Server  
* **Database Features**: Triggers (`TR_BLOCK_EXTREME_`), Stored Procedures (`SP_REWARD_USER_`), Referential Integrity (`ON UPDATE CASCADE`, `ON DELETE NO ACTION`)  
* **Data Access**: JDBC or JPA  
* **Data Types**: `DECIMAL(10,3)` for numeric values, default text length 100 characters  

## 🎯 Learning Outcomes

* Designing and implementing a relational database with advanced constraints and triggers  
* Implementing business rules and automated reward systems in SQL  
* Building Java classes interfacing with the database via JDBC/JPA  
* Creating algorithms for personalized recommendations based on user behavior  
* Applying object-oriented design for complex systems with multiple interrelated entities  

## 📂 Project Structure

* **`student/`** – Java classes implementing all interfaces with prefix `piggbbbb_`  
* **`studentMain.java`** – main class demonstrating functionality  
* **SQL Files**:  
  * `piggbbbb.sql` – database creation scripts with triggers and procedures  
  * `piggbbbb-tsql.sql` – triggers and stored procedures  
  * `piggbbbb.bak` – database backup  
* **Database Model**: `piggbbbb.png`  
