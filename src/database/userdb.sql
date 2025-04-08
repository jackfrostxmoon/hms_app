-- Drop the database if it exists to start fresh (Use with caution!)
DROP DATABASE IF EXISTS userdb;

-- Create the database
CREATE DATABASE userdb;

-- Select the database to use
USE userdb;

-- Create the users table
CREATE TABLE users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,             -- Remember to hash passwords in a real application!
    role VARCHAR(20) DEFAULT 'user' NOT NULL,
    CHECK (CHAR_LENGTH(password) >= 8)          -- Basic password length check
);

-- Populate the users table with some initial data
INSERT INTO users (name, email, password, role) VALUES
('Admin', 'Admin@email.com', 'admin123', 'admin'),          
('Mike Johnson', 'mike.j@email.com', 'mikepass789', 'user'), 
('Test 2', 'test2@email.com', 'test1234', 'user');

INSERT INTO users (name, email, password, role) VALUES
('t k', 't.k@email.com', 'test1234', 'user');


-- Describe the users table structure (for verification)
DESC users;

-- Table to store user room selections (Stores room type as string directly)
-- This structure matches the provided room_controller.java which saves the room type string
CREATE TABLE user_rooms (
    user_room_id INT AUTO_INCREMENT PRIMARY KEY,       -- Unique ID for each user room selection
    user_id INT NOT NULL,                              -- ID of the user who made the selection (foreign key to users table)
    room_type VARCHAR(50) NOT NULL,                     -- Type of room selected (e.g., 'Single', 'Double') - Stored as string
    selection_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- Date and time when the selection was made
    FOREIGN KEY (user_id) REFERENCES users(id)         -- Establishes a relationship with the users table
);

-- Describe the user_rooms table structure (for verification)
DESC user_rooms;

-- Table for amenity requests (Keep this if needed for amenities_controller)
CREATE TABLE amenity_requests (
    request_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    amenity_type VARCHAR(100) NOT NULL,
    request_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(50) DEFAULT 'Pending', -- e.g., Pending, Delivered, Cancelled
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Describe the amenity_requests table structure (for verification)
DESC amenity_requests;

-- Show all tables in the database (for verification)
SHOW TABLES;

-- Optionally, select all data from the tables (for verification after running the app)
SELECT * FROM users;
SELECT * FROM user_rooms;
SELECT * FROM amenity_requests;

