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
- `DBA.java`: Handles database interactions.
- `HandleRequests.java`: Processes client requests.
- `ProjectServer.java`: Manages server operations and client connections.
- `Utilities.java`: Provides helper functions.
- **Client files**: (To be documented)

## Future Work
- Ingest items by web scraping.
- Improve UI and user experience.

## Contributors
- Mahmoud Osama
- Mohamed Hazem Sakr
- Randa Yasser Yousif
- Rodina Ashraf

## License
This project is licensed under the MIT License.

