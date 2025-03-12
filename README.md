# IWish - Gift Sharing Desktop Application

## Abstract
IWish is a desktop application that allows users to add friends, create wishlists, view friends’ wishlists, and contribute financially to help fulfill their friends' wishes. The application provides a user-friendly interface to enhance the experience of gift sharing.

## Features
### Client Features
- **Register/Sign-in**: Users can create an account or log in.
- **Friend Management**: Add or remove friends, accept or decline friend requests.
- **Wishlist Management**: Create, update, or delete wishlists.
- **View Friends’ Wishlists**: Check what friends wish for.
- **Contribute to Gifts**: Donate a specific amount toward a friend's wishlist item.
- **Notifications**:
  - Buyers receive a notification when the total contribution is completed.
  - Recipients are notified when an item from their wishlist is fully funded.
- **User-friendly Interface**: Designed to be intuitive and enjoyable.

### Server Features
- **Server Control**: Start and stop the server.
- **Database Management**:
  - Connect to and query the database.
  - Manage available wishlist items.
- **Client Request Handling**: Process user actions and interactions.
- **Gift Completion Handling**: Ensure proper notifications and transactions when wishlist items are fully funded.
  
## Architecture

IWish application follows a **client-server architecture** with a structured interaction between the user interface and backend services. 
Requests are send between clinet and server as Json messages using print stream. Json messages then recieved and converted to Json objects and processed as required.

### **Client-Side (JavaFX GUI Application)**

- The client interacts with the user through an intuitive **JavaFX-based graphical interface**.
- Controllers handle user interactions and communicate with the server.
- FXML files define the UI structure, and CSS is used for styling.

### **Server-Side (Java Backend with Database Support)**

- The **server (ProjectServer.java)** handles client connections and manages multi-user requests.
- **HandleRequests.java** processes client requests and routes them to appropriate services.
- **DBA.java** manages the database, executing queries for user authentication, wishlist management, and friend interactions.
- **Utilities.java** provides helper functions for data processing and application logic.

### **Database (Oracle Database)**

- Stores user information, wishlists, friend relationships, and transactions.
- Ensures data consistency and integrity with relational tables.

## Installation
### Prerequisites
- Java Development Kit (JDK)
- JavaFX
- Oracle Database

### Setup Instructions
1. Clone the repository.
2. Configure the database connection in `DBA.java`.
3. Compile and run the server (`ProjectServer.java`).
4. Launch the client application.

## File Structure
### Server-side Files

- `DBA.java`: Handles database interactions.
- `HandleRequests.java`: Processes client requests.
- `ProjectServer.java`: Manages server operations and client connections.
- `Utilities.java`: Provides helper functions.
- #### Database Access Layer:
  `FriendInfo.java`, `FriendWishInfo.java`, `ItemDTO.java`, `ItemsInfo.java`, `NotificationInfo.java`, `ServerAccess.java`, `UserInfo.java`, `Utilities.java`, `WishInfo.java`
  
### Client-side Files

- **FXML Files (UI Layouts)**:
    
    - `SignIn.fxml`, `SignUp.fxml`: Login and registration screens.
    - `FriendList.fxml`, `Friendrequest.fxml`: Friend management screens.
    - `WishList.fxml`, `Item.fxml`: Wishlist and item-related UI.
    - `Addbalance.fxml`, `ContributePopup.fxml`: Contribution and balance-related screens.
    - `Notifications.fxml`: Notification UI.
- **Controller Files (Logic Handling)**:
    
    - `SignInController.java`, `SignUpController.java`: Handles authentication.
    - `FriendListController.java`, `FriendrequestController.java`: Manages friend-related actions.
    - `WishListController.java`, `ItemController.java`: Handles wishlist operations.
    - `AddbalanceController.java`, `ContributePopupController.java`: Handles balance and contributions.
    - `NotificationsController.java`: Manages notifications.
-- **Data Transfer Objects (DTOs) & Other Files**:
    
    - `FriendInfo.java`, `FriendWishInfo.java`, `ItemDTO.java`, `ItemsInfo.java`, `NotificationInfo.java`, `ServerAccess.java`, `UserInfo.java`, `Utilities.java`, `WishInfo.java`
    - `ProjectClient.java`: Main client application entry point.
    - `style.css`: UI styling.
 
## Future Work
- Ingest items in databaase using web scraping.
- Improve UI and user experience.

## Contributors
- [Mahmoud Osama](https://github.com/Mahmuuod)
- [Mohamed Hazem Sakr](https://github.com/mohamedsakr9)
- [Randa Yasser](https://github.com/RandaYasser)
- [Rodina Ashraf](https://github.com/Rodinaaashraf)

## License
This project is licensed under the MIT License.

