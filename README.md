![Main-Infp-PNG](images%main.PNG](https://github.com/AntonBabychP1T/books-store-app/blob/add-readme-file/images/main.png)
### Book Store App
##API Endpoints
#Authentication Controller
Endpoints available for all users without authentication.
POST /api/auth/registration - Registers a new user.
Example of request body 
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
POST /api/auth/login - Authenticates a user and returns a JWT token.
Example of request body 
  ```json
{
  "email": "email@mail.com",
  "password": "Min8Character",
}
```
#Book Controller
Endpoints for managing books.
GET /api/books - Retrieves all books with optional pagination.
GET /api/books/{id} - Fetches a book by its ID.
GET /api/books/search - Searches books based on given parameters.
DELETE /api/books/{id} - Soft deletes a book by ID (Admin only).
POST /api/books - Creates a new book (Admin only).
PUT /api/books/{id} - Updates a book by ID (Admin only).

Example of request body to create new book
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

#Category Controller
Endpoints for managing book categories.
POST /api/categories - Creates a new book category (Admin only).
GET /api/categories - Lists all categories with optional pagination.
GET /api/categories/{id} - Retrieves a category by ID.
DELETE /api/categories/{id} - Soft deletes a category by ID (Admin only).
PUT /api/categories/{id} - Updates a category by ID (Admin only).
GET /api/categories/{id}/books - Lists books in a given category.
Example of request body to create new category

 ```json
  {
    "name": "Name of categopry",
    "description": "Category description"
  }
  ```

#Shopping Cart Controller
Endpoints for managing the shopping cart.
POST /api/cart - Adds a book to the shopping cart.
GET /api/cart - Retrieves the current user's shopping cart.
DELETE /api/cart/cart-items/{id} - Removes an item from the shopping cart.
PUT /api/cart/cart-items/{id} - Updates the quantity of an item in the shopping cart.
  Example of request body to add items to the cart:

  ```json
  {
    "bookId": 1,
    "quantity": 1
  }
  ```

  Example of request body to update book quantity in the cart:

  ```json
  {
    "quantity": 5
  }
  ```

#Order Controller
Endpoints for managing orders.
POST /api/orders - Places a new order.
GET /api/orders - Retrieves all orders for the logged-in user.
PATCH /api/orders/{id} - Updates the status of an order (Admin only).
GET /api/orders/{orderId}/items - Lists items in a specific order.
GET /api/orders/{orderId}/items/{orderItemId} - Retrieves a specific item in an order.



