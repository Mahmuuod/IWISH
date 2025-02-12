-- User Table
CREATE TABLE Users (
    User_id NUMBER,
    First_name VARCHAR2(50),
    Last_name VARCHAR2(50),
    Username VARCHAR2(50),
    Password VARCHAR2(255),
    Birthdate DATE,
    Email VARCHAR2(100),
    Phone VARCHAR2(15),
	Bank_card VARCHAR2(16),
    User_balance NUMBER(10, 2)
);

-- Item Table
CREATE TABLE Item (
    Item_id NUMBER,
    Name VARCHAR2(100),
    Price NUMBER(10, 2),
    Category VARCHAR2(50)
);

-- Wish Table
CREATE TABLE Wish (
    Wish_id NUMBER,
    User_id NUMBER,
    Item_id NUMBER,
    Wish_Date DATE,
    Wish_Time TIMESTAMP,
    Status VARCHAR2(50)
);

-- Contribution Table
CREATE TABLE Contribution (
    Contribution_id NUMBER,
    Wish_id NUMBER,
    Contribution_amount NUMBER(10, 2),
    Contribution_Date DATE,
    Contribution_Time TIMESTAMP,
    Contributor_id NUMBER
);

-- Notification Table
CREATE TABLE Notification (
    Notification_id NUMBER,
    Context VARCHAR2(255),
    Is_read CHAR(1) CHECK (Is_read IN ('Y', 'N')),
    Type VARCHAR2(50)
);

-- User_Notification Table
CREATE TABLE User_Notification (
    Reciever_id NUMBER,
    Notification_id NUMBER,
    Notification_Date DATE,
    Notification_Time TIMESTAMP
);

-- FriendRequest Table
CREATE TABLE FriendRequest (
    Request_id NUMBER,
    Requester_id NUMBER,
    Reciever_id NUMBER,
    Status VARCHAR2(50)
);

-- FriendList Table
CREATE TABLE FriendList (
    User_id NUMBER,
    Friend_id NUMBER,
    friends_since DATE
);

--------------------
----Contraints------
--------------------

-- User Table Constraints
ALTER TABLE Users ADD CONSTRAINT pk_user PRIMARY KEY (User_id);
ALTER TABLE Users ADD CONSTRAINT uk_user_username UNIQUE (Username);
ALTER TABLE Users ADD CONSTRAINT uk_user_email UNIQUE (Email);

-- Item Table Constraints
ALTER TABLE Item ADD CONSTRAINT pk_item PRIMARY KEY (Item_id);

-- Wish Table Constraints
ALTER TABLE Wish ADD CONSTRAINT pk_wish PRIMARY KEY (Wish_id);
ALTER TABLE Wish ADD CONSTRAINT fk_wish_user FOREIGN KEY (User_id) REFERENCES Users(User_id);
ALTER TABLE Wish ADD CONSTRAINT fk_wish_item FOREIGN KEY (Item_id) REFERENCES Item(Item_id);

-- Contribution Table Constraints
ALTER TABLE Contribution ADD CONSTRAINT pk_contribution PRIMARY KEY (Contribution_id);
ALTER TABLE Contribution ADD CONSTRAINT fk_contribution_wish FOREIGN KEY (Wish_id) REFERENCES Wish(Wish_id);
ALTER TABLE Contribution ADD CONSTRAINT fk_contribution_user FOREIGN KEY (Contributor_id) REFERENCES Users(User_id);

-- Notification Table Constraints
ALTER TABLE Notification ADD CONSTRAINT pk_notification PRIMARY KEY (Notification_id);

-- User_Notification Table Constraints
ALTER TABLE User_Notification ADD CONSTRAINT pk_user_notification PRIMARY KEY (Reciever_id, Notification_id);
ALTER TABLE User_Notification ADD CONSTRAINT fk_user_notification_user FOREIGN KEY (Reciever_id) REFERENCES Users(User_id);
ALTER TABLE User_Notification ADD CONSTRAINT fk_user_notification FOREIGN KEY (Notification_id) REFERENCES Notification(Notification_id);

-- FriendRequest Table Constraints
ALTER TABLE FriendRequest ADD CONSTRAINT pk_friendrequest PRIMARY KEY (Request_id);
ALTER TABLE FriendRequest ADD CONSTRAINT fk_friendrequest_user2 FOREIGN KEY (Reciever_id) REFERENCES Users(User_id);
ALTER TABLE FriendRequest ADD CONSTRAINT fk_friendrequest_user1 FOREIGN KEY (Requester_id) REFERENCES Users(User_id);

-- FriendList Table Constraints
ALTER TABLE FriendList ADD CONSTRAINT pk_friendlist PRIMARY KEY (User_id, Friend_id);
ALTER TABLE FriendList ADD CONSTRAINT fk_friendlist_user1 FOREIGN KEY (User_id) REFERENCES Users(User_id);
ALTER TABLE FriendList ADD CONSTRAINT fk_friendlist_user2 FOREIGN KEY (Friend_id) REFERENCES Users(User_id);

-- contibution amount default 0
ALTER TABLE Contribution 
MODIFY Contribution_amount DEFAULT 0;
commit
-----inserts-----


-- Insert Users
INSERT INTO Users (User_id, First_name, Last_name, Username, Password, Birthdate, Email, Phone, Bank_card, User_balance) VALUES
(1, 'Ahmed', 'Ali', 'ahmedali', 'hashedpassword1', TO_DATE('1995-06-15', 'YYYY-MM-DD'), 'ahmed.ali@example.com', '01234567891','5987684152259658', 500.00);
INSERT INTO Users VALUES (2, 'Omar', 'Hassan', 'omarhassan', 'hashedpassword2', TO_DATE('1998-04-20', 'YYYY-MM-DD'), 'omar.hassan@example.com', '01122334455','5123456112369658', 300.00);
INSERT INTO Users VALUES (3, 'Fatma', 'Mahmoud', 'fatmamahmoud', 'hashedpassword3', TO_DATE('1992-09-10', 'YYYY-MM-DD'), 'fatma.mahmoud@example.com', '01011223344','5987684152259658', 450.00);
INSERT INTO Users VALUES (4, 'Youssef', 'Kamel', 'youssefkamel', 'hashedpassword4', TO_DATE('2000-01-05', 'YYYY-MM-DD'), 'youssef.kamel@example.com', '01566778899','4897684152259658', 600.00);
INSERT INTO Users VALUES (5, 'Nour', 'Saeed', 'noursaeed', 'hashedpassword5', TO_DATE('1997-11-30', 'YYYY-MM-DD'), 'nour.saeed@example.com', '01099887766', '4525522552255289', 700.00);

-- Insert Items
INSERT INTO Item (Item_id, Name, Price, Category) VALUES (1, 'Smartphone', 1000.00, 'Electronics');
INSERT INTO Item VALUES (2, 'Laptop', 1500.00, 'Electronics');
INSERT INTO Item VALUES (3, 'Watch', 250.00, 'Accessories');
INSERT INTO Item VALUES (4, 'Headphones', 150.00, 'Electronics');
INSERT INTO Item VALUES (5, 'Backpack', 100.00, 'Fashion');

-- Insert Wishes
INSERT INTO Wish (Wish_id, User_id, Item_id, Wish_Date, Wish_Time, Status) VALUES (1, 1, 2, TO_DATE('2024-01-15', 'YYYY-MM-DD'), SYSTIMESTAMP, 'New');
INSERT INTO Wish VALUES (2, 2, 3, TO_DATE('2024-01-16', 'YYYY-MM-DD'), SYSTIMESTAMP, 'In progress');
INSERT INTO Wish VALUES (3, 3, 4, TO_DATE('2024-01-17', 'YYYY-MM-DD'), SYSTIMESTAMP, 'Completed');
INSERT INTO Wish VALUES (4, 4, 1, TO_DATE('2024-01-18', 'YYYY-MM-DD'), SYSTIMESTAMP, 'New');
INSERT INTO Wish VALUES (5, 5, 5, TO_DATE('2024-01-19', 'YYYY-MM-DD'), SYSTIMESTAMP, 'In progress');

-- Insert Contributions
INSERT INTO Contribution (Contribution_id, Wish_id, Contribution_amount, Contribution_Date, Contribution_Time, Contributor_id) VALUES (1, 1, 200.00, TO_DATE('2024-01-20', 'YYYY-MM-DD'), SYSTIMESTAMP, 2);
INSERT INTO Contribution VALUES (2, 2, 100.00, TO_DATE('2024-01-21', 'YYYY-MM-DD'), SYSTIMESTAMP, 3);
INSERT INTO Contribution VALUES (3, 3, 50.00, TO_DATE('2024-01-22', 'YYYY-MM-DD'), SYSTIMESTAMP, 4);
INSERT INTO Contribution VALUES (4, 4, 300.00, TO_DATE('2024-01-23', 'YYYY-MM-DD'), SYSTIMESTAMP, 5);
INSERT INTO Contribution VALUES (5, 5, 75.00, TO_DATE('2024-01-24', 'YYYY-MM-DD'), SYSTIMESTAMP, 1);

-- Insert Notifications
INSERT INTO Notification (Notification_id, Context, Is_read, Type) VALUES (1, 'Ahmed Ali sent you a friend request', 'N', 'friend request');
INSERT INTO Notification VALUES (2, 'Omar Hassan contributed to your wish', 'N', 'gift bought');
INSERT INTO Notification VALUES (3, 'Fatma Mahmoud accepted your friend request', 'Y', 'friend request');
INSERT INTO Notification VALUES (4, 'Youssef Kamel added balance to his account', 'N', 'balance added');
INSERT INTO Notification VALUES (5, 'Nour Saeed completed a wish', 'Y', 'gift bought');

-- Insert User Notifications
INSERT INTO User_Notification (Reciever_id, Notification_id, Notification_Date, Notification_Time) VALUES (1, 2, TO_DATE('2024-01-25', 'YYYY-MM-DD'), SYSTIMESTAMP);
INSERT INTO User_Notification VALUES (2, 3, TO_DATE('2024-01-26', 'YYYY-MM-DD'), SYSTIMESTAMP);
INSERT INTO User_Notification VALUES (3, 4, TO_DATE('2024-01-27', 'YYYY-MM-DD'), SYSTIMESTAMP);
INSERT INTO User_Notification VALUES (4, 5, TO_DATE('2024-01-28', 'YYYY-MM-DD'), SYSTIMESTAMP);
INSERT INTO User_Notification VALUES (5, 1, TO_DATE('2024-01-29', 'YYYY-MM-DD'), SYSTIMESTAMP);

-- Insert Friend Requests
INSERT INTO FriendRequest (Request_id, Requester_id, Reciever_id, Status) VALUES (1, 1, 2, 'Pending');
INSERT INTO FriendRequest VALUES (2, 2, 3, 'Accepted');
INSERT INTO FriendRequest VALUES (3, 3, 4, 'Declined');
INSERT INTO FriendRequest VALUES (4, 4, 5, 'Pending');
INSERT INTO FriendRequest VALUES (5, 5, 1, 'Accepted');

-- Insert Friend List
INSERT INTO FriendList (User_id, Friend_id, friends_since) VALUES (1, 5, TO_DATE('2023-12-01', 'YYYY-MM-DD'));
INSERT INTO FriendList VALUES (2, 3, TO_DATE('2023-12-02', 'YYYY-MM-DD'));
INSERT INTO FriendList VALUES (3, 2, TO_DATE('2023-12-03', 'YYYY-MM-DD'));
INSERT INTO FriendList VALUES (4, 5, TO_DATE('2023-12-04', 'YYYY-MM-DD'));
INSERT INTO FriendList VALUES (5, 1, TO_DATE('2023-12-05', 'YYYY-MM-DD'));

commit