# Filmland

## Why did I build this project?

- This is the Sogeti Back-end developer assessment. So I built this project to go to the next step in the hiring process.

## What does this application do?

This is a Filmland company back-end application created by Java / Maven / Spring Boot. You can do the below operations by using this application.

- Registration,
- Login,
- Getting categories,
- Subscribe to a category,
- Share a category with another customer.

## Installation

- Clone this repository
- Make sure you are using JDK 11 and Maven 3.x
- You can build the project and run it by using your own IDE.

## Which technologies are used in this app

- Java
- Spring Boot
- H2 Database
- Lombok
- DiffBlue
## Usage

Created sample data for testing. You can use the below data by Postman.
- Login

`URL: http://localhost:8080/api/login`
The port number (8080) can be different according to your own app settings.

`POST request`:
```json
{
    "email":"ibra@gmail.com",
    "password": "12345"
}
```


If the request succeeds, the code returns a key in the response JSON object. You need this key for other requests.

Response:

```json
{
    "status": "Login Successful",
    "message": "Authorisation key is 9f01b798-a565-44ff-8d93-3c285bc04b2d"
}
```
- Getting categories

`URL: http://localhost:8080/api/category/ibra@gmail.com`

`GET request`:

Enter the HTTP header for the request. Note: The key is changing for each login. And one key is valid for 30 minutes after creation.

`KEY: Authorisation-key, VALUE:369c8479-2934-4623-8f7a-31f114f4552b`

Response:
```json
{
    "availableCategories": [
        {
            "name": "Dutch Films",
            "availableContent": 10,
            "price": 4.0
        },
        {
            "name": "International Films",
            "availableContent": 30,
            "price": 8.0
        }
    ],
    "subscribedCategories": [
        {
            "name": "Dutch Series",
            "remainingContent": 20,
            "price": 3.0,
            "startDate": "2021-12-22"
        }
    ]
}
```

- Subscribe to a category
  `URL: http://localhost:8080/api/subscription`

Header:

`KEY: Authorisation-key, VALUE:369c8479-2934-4623-8f7a-31f114f4552b`

`POST Request`:

```json
{
    "email": "ibra@gmail.com",
    "availableCategory":"Dutch Films"
}
```

Response:

```json
{
    "status": "Successful!",
    "message": "User is subscribed to Dutch Films"
}
```

- Share a category with another customer

`URL: http://localhost:8080/api/subscription/share`

Header:

`KEY: Authorisation-key, VALUE:369c8479-2934-4623-8f7a-31f114f4552b`

`POST Request`:

```json
{
    "email":"ibra@gmail.com",
    "customer":"johan@gmail.com",
    "subscribedCategory":"Dutch Films"
}
```

Response:

```json
{
    "status": "Successful!",
    "message": "Subscription is shared with johan@gmail.com"
}
```

## Challenges
It was a bit difficult for me to think about the project at a certain time period and implement it.

"Simple football is the most beautiful. But playing simple football is the hardest thing" as said Johan Cruyff. I mean as in his saying when I write the project I tried playing simple ad it took me a long time. Because I like football and I believe in simplicity.
