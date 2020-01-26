# favortech-challenge


The application contains the following pages/features:
- Login and sign up page: handles input validation and save the data on device for this challenge. 
- Home page: UnSplash images API was chosen to load a list of images vertically and handled clicks on each item. 
- Room database to save the loaded images offline. 
- The app works if the connection was lost. 

**Things which were considered:** 
1. [x] Architecture design pattern (MVVM)
2. [x]  \(Optional) Dependency Injection (Dagger 2) 
3. [x] Asynchronous tasks and RXJava 
4. [x] Data Binding 
5. [x] Networking using Retrofit 
6. [x] kotlin

**Explanation:**
- Application launches with registration page where user can login or register.
- User can sign up with valid email and password.
- User can login using dummy login credentials for verification.(please find the dummy user login credentials below)
- Once registered, Images are fetched from UnSplash API services, displayed and saved in database with username.
- On scroll more images are also loaded for better user experience.
- User can view/enlarge any photo by clicking on it.

**Dummy Login:**
```
Email: muhammad@favortech.net
Password: 123456
```
