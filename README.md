![Main Info](https://github.com/AntonBabychP1T/books-store-app/blob/add-readme-file/images/main.png)
# Book Store App
## API Endpoints
### Authentication Controller

Endpoints available for all users without authentication.

- POST /api/auth/registration - Registers a new user.
  
*Example of request body*

```json
{
  "email": "email@mail.com",
  "password": "Min8Character",
  "repeatPassword": "Min8Character",
  "firstName": "Anton",
  "lastName": "Babych",
  "shippingAddress": "Kyiv 123 street"
}
```
- POST /api/auth/login - Authenticates a user and returns a JWT token.

*Example of request body*

```json 
{
  "email": "email@mail.com",
  "password": "Min8Character",
}
```
### Book Controller

Endpoints for managing books

- GET /api/books - Retrieves all books with optional pagination.
- GET /api/books/{id} - Fetches a book by its ID.
- GET /api/books/search - Searches books based on given parameters.
- DELETE /api/books/{id} - Soft deletes a book by ID (Admin only).
- POST /api/books - Creates a new book (Admin only).
- PUT /api/books/{id} - Updates a book by ID (Admin only).

*Example of request body to create new book*

```json
  {
    "title": "Some title",
    "author": "Some author",
    "price": "19.99",
    "description": "Description for book",
    "coverImage": "Image for book",
    "isbn": "978-1-23-456-7899",
    "categoryIds": [2, 5]
  }
```

### Category Controller

Endpoints for managing book categories.

- POST /api/categories - Creates a new book category (Admin only).
- GET /api/categories - Lists all categories with optional pagination.
- GET /api/categories/{id} - Retrieves a category by ID.
- DELETE /api/categories/{id} - Soft deletes a category by ID (Admin only).
- PUT /api/categories/{id} - Updates a category by ID (Admin only).
- GET /api/categories/{id}/books - Lists books in a given category.

*Example of request body to create new category*

 ```json
  {
    "name": "Name of categopry",
    "description": "Category description"
  }
  ```

### Shopping Cart Controller

Endpoints for managing the shopping cart. 

POST /api/cart - Adds a book to the shopping cart.
GET /api/cart - Retrieves the current user's shopping cart.
DELETE /api/cart/cart-items/{id} - Removes an item from the shopping cart.
PUT /api/cart/cart-items/{id} - Updates the quantity of an item in the shopping cart.

*Example of request body to add items to the cart*

  ```json
  {
    "bookId": 1,
    "quantity": 1
  }
  ```

*Example of request body to update book quantity in the cart*

  ```json
  {
    "quantity": 5
  }
  ```

### Order Controller

Endpoints for managing orders.

POST /api/orders - Places a new order.
GET /api/orders - Retrieves all orders for the logged-in user.
PATCH /api/orders/{id} - Updates the status of an order (Admin only).
GET /api/orders/{orderId}/items - Lists items in a specific order.
GET /api/orders/{orderId}/items/{orderItemId} - Retrieves a specific item in an order.

## Database structure ##

![Database_Structure.PNG](https://github.com/AntonBabychP1T/books-store-app/blob/add-readme-file/images/database-structure.png)

# How to test this application? #
## Installing Postman and Importing the API Collection ## 
### Step 1: Download and Install Postman ###
Navigate to the Postman website: Go to [Postman's official website](https://www.postman.com/) 

Download Postman: Click on the download link suitable for your operating system (Windows, MacOS, or Linux).

Install Postman: After downloading, run the installer and follow the on-screen instructions to complete the installation.

### Step 2: Launch Postman ###
Open Postman: Locate the Postman application on your computer and open it.

Sign In or Create an Account (optional): You can sign in to your Postman account or create a new one. Alternatively, you can use Postman in Guest mode.

### Step 3: Importing the API Collection ### 
Locate the Import Button: In the Postman application, locate the 'Import' button. This is usually found in the top left corner of the interface.

Click 'Import': Click on the 'Import' button to open the import dialog.

Choose Your File: You can find .json file by [this link](https://github.com/AntonBabychP1T/books-store-app/blob/add-readme-file/images/Book-store-app.postman_collection%20(1).json)

File Upload: Drag and drop your .json file into the dialog box or click 'Upload Files' and navigate to where your .json file is stored, then select it.

Finalize Import: After selecting your .json file, click on the 'Import' button to add the collection to your Postman workspace.

### Step 4: Using the Imported Collection ###
Locate the Collection: After importing, the collection should appear in the left sidebar under 'Collections'.

Explore Requests: Click on the collection to expand it and see all the available requests.

Send a Request: Click on any request to open it. You can modify the request as needed (e.g., changing parameters or the request body).

Send and View Response: Click the 'Send' button to execute the request and view the response in the lower section of the Postman interface.

### Step 5: Saving Changes (Optional) ###
Modify Requests: If you make changes to any requests and wish to save them, click the 'Save' button located near the request name.

Organize Collections: You can create folders, rename, or reorder requests within your collection for better organization.


