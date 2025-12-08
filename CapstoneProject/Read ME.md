##### Steps to Run this project :


1. Import all the microservices and update your sql password in all the applications.yml files.
---



##### 2\. When you run all the microservices it will create all the needed databases.



#### 3\. Since we add admin from the backend (Security Purposes) follow the commands:





	-- 1. Switch to the target database

      USE pizza_admin;



	--2. INSERT INTO admins (email, password, full_name, role, active, created_at, updated_at) VALUES ('admin@pizza.com', '$2a$10$cstnYrM7jKIDdgXz/kKOLeQZZVMKy32znXvNloIwNcmL82wmt8kWq', 'Admin User', 'ADMIN', TRUE, NOW(), NOW());





	     IMPORTANT - This hashed password is for admin123 but in your system it can be something different so i recommend verifying by running following java code				

			import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;



			 public class PasswordGenerator {



 			  public static void main(String\[] args) {

      				 // Create an instance of the encoder

       		         BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(); 

       

      				 String rawPassword = "admin123";

      

     				  // The encode() method generates the unique hash, including a random salt and cost factor.

   				   String hashedPassword = encoder.encode(rawPassword);

       

     			 // This output is the string you insert into the SQL database.

      	            System.out.println("Hashed Password for 'admin123': " + hashedPassword);

 			 }

		   }





	 -- 3. Verify the final data (Select)

		SELECT \* FROM admins;


4. Now you can login into the admin dashboard and the remaining database operations can be easily done with the front end easily.
---

#### 

5\. Thank you


HERE IS THE FLOW OF THE PROJECT FOR REFERENCE 
Capstone Project
---

│

├── eureka-server\\

│   ├── src\\main\\java\\com\\pizza\\eurekaserver\\

│   │   └── EurekaServerApplication.java

│   ├── src\\main\\resources\\

│   │   └── application.yml

│   └── pom.xml

├── api-gateway\\

│   ├── src\\main\\java\\com\\pizza\\apigateway\\

│   │   └── ApiGatewayApplication.java

│   ├── src\\main\\resources\\

│   │   └── application.yml

│   └── pom.xml

│

├── user-service\\

│   ├── src\\main\\java\\com\\pizza\\userservice\\

│   │   ├── UserServiceApplication.java

│   │   ├── config\\

│   │   │   ├── SecurityConfig.java

│   │   │   ├── SwaggerConfig.java

│   │   │   └── CorsConfig.java

│   │   ├── entity\\

│   │   │   └── User.java

│   │   ├── dto\\

│   │   │   ├── UserDTO.java

│   │   │   ├── LoginRequest.java

│   │   │   ├── LoginResponse.java

│   │   │   └── RegisterRequest.java

│   │   ├── repository\\

│   │   │   └── UserRepository.java

│   │   ├── service\\

│   │   │   ├── UserService.java

│   │   │   └── JwtService.java

│   │   ├── controller\\

│   │   │   └── UserController.java

│   │   ├── exception\\

│   │   │   ├── GlobalExceptionHandler.java

│   │   │   ├── UserNotFoundException.java

│   │   │   ├── UserAlreadyExistsException.java

│   │   │   └── InvalidCredentialsException.java

│   │   └── client\\

│   │       └── NotificationClient.java

│   ├── src\\main\\resources\\

│   │   └── application.yml

│   └── pom.xml

│

├── admin-service\\

│   ├── src\\main\\java\\com\\pizza\\adminservice\\

│   │   ├── AdminServiceApplication.java

│   │   ├── config\\

│   │   │   ├── SecurityConfig.java

│   │   │   └── SwaggerConfig.java

│   │   ├── entity\\

│   │   │   ├── Admin.java

│   │   │   └── Revenue.java

│   │   ├── dto\\

│   │   │   ├── AdminDTO.java

│   │   │   ├── AdminLoginRequest.java

│   │   │   ├── AdminLoginResponse.java

│   │   │   ├── RevenueDTO.java

│   │   │   └── UserManagementDTO.java

│   │   ├── repository\\

│   │   │   ├── AdminRepository.java

│   │   │   └── RevenueRepository.java

│   │   ├── service\\

│   │   │   ├── AdminService.java

│   │   │   └── JwtService.java

│   │   ├── controller\\

│   │   │   └── AdminController.java

│   │   └── exception\\

│   │       ├── GlobalExceptionHandler.java

│   │       ├── AdminNotFoundException.java

│   │       └── InvalidCredentialsException.java

│   ├── src\\main\\resources\\

│   │   └── application.yml

│   └── pom.xml

│

├── menu-service\\

│   ├── src\\main\\java\\com\\pizza\\menuservice\\

│   │   ├── MenuServiceApplication.java

│   │   ├── entity\\

│   │   │   └── MenuItem.java

│   │   ├── dto\\

│   │   │   └── MenuItemDTO.java

│   │   ├── repository\\

│   │   │   └── MenuItemRepository.java

│   │   ├── service\\

│   │   │   └── MenuItemService.java

│   │   ├── controller\\

│   │   │   └── MenuItemController.java

│   │   └── exception\\

│   │       ├── GlobalExceptionHandler.java

│   │       └── MenuItemNotFoundException.java

│   ├── src\\main\\resources\\

│   │   └── application.yml

│   └── pom.xml

│

├── order-service\\

│   ├── src\\main\\java\\com\\pizza\\orderservice\\

│   │   ├── OrderServiceApplication.java

│   │   ├── entity\\

│   │   │   ├── Order.java

│   │   │   └── OrderItem.java

│   │   ├── dto\\

│   │   │   ├── OrderDTO.java

│   │   │   ├── OrderItemDTO.java

│   │   │   ├── CreateOrderRequest.java

│   │   │   ├── OrderItemRequest.java

│   │   │   └── MenuItemDTO.java

│   │   ├── repository\\

│   │   │   └── OrderRepository.java

│   │   ├── service\\

│   │   │   └── OrderService.java

│   │   ├── controller\\

│   │   │   └── OrderController.java

│   │   ├── client\\

│   │   │   ├── MenuClient.java

│   │   │   └── NotificationClient.java

│   │   └── exception\\

│   │       ├── GlobalExceptionHandler.java

│   │       └── OrderNotFoundException.java

│   ├── src\\main\\resources\\

│   │   └── application.yml

│   └── pom.xml

│

├── billing-service\\

│   ├── src\\main\\java\\com\\pizza\\billingservice\\

│   │   ├── BillingServiceApplication.java

│   │   ├── entity\\

│   │   │   └── Bill.java

│   │   ├── dto\\

│   │   │   ├── BillDTO.java

│   │   │   ├── CreateBillRequest.java

│   │   │   ├── PaymentRequest.java

│   │   │   └── OrderDTO.java

│   │   ├── repository\\

│   │   │   └── BillRepository.java

│   │   ├── service\\

│   │   │   └── BillingService.java

│   │   ├── controller\\

│   │   │   └── BillingController.java

│   │   ├── client\\

│   │   │   ├── OrderClient.java

│   │   │   ├── AdminClient.java

│   │   │   └── NotificationClient.java

│   │   └── exception\\

│   │       ├── GlobalExceptionHandler.java

│   │       └── BillNotFoundException.java

│   ├── src\\main\\resources\\

│   │   └── application.yml

│   └── pom.xml

│

├── notification-service\\

│   ├── src\\main\\java\\com\\pizza\\notificationservice\\

│   │   ├── NotificationServiceApplication.java

│   │   ├── entity\\

│   │   │   └── Notification.java

│   │   ├── dto\\

│   │   │   ├── NotificationDTO.java

│   │   │   └── SendNotificationRequest.java

│   │   ├── repository\\

│   │   │   └── NotificationRepository.java

│   │   ├── service\\

│   │   │   └── NotificationService.java

│   │   ├── controller\\

│   │   │   └── NotificationController.java

│   │   └── exception\\

│   │       ├── GlobalExceptionHandler.java

│   │       └── NotificationNotFoundException.java

│   ├── src\\main\\resources\\

│   │   └── application.yml

│   └── pom.xml

│

└── frontend\\

   ├── login.html
   ├── index.html
   ├── register.html
   ├── admin-login.html
   ├── user-dashboard.html
   ├── admin-dashboard.html
   ├── menu.html
   ├── cart.html
   ├── orders.html
   ├── billing.html
   ├── css\\
   │   └── style.css
   └── js\\
       ├── app.js
       ├── auth.js
       ├── menu.js
       ├── cart.js
       ├── orders.js
       └── billing.js

