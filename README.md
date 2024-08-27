### Proposal
The project EStore is a proposed web application to provides services for business-to-business (B2B)
and business-to-customer (B2C) domains. Following DDD methodology, there are two core modules
to match B2B and B2C business domain. The modules will provide the necessary functionality to
support customers, vendors, and administrators in their respective e-commerce activities. Unit tests
and logging are also introduced in this project to support maintainability and debugging during
development.
The proposal focus is to provide the complete minimum viable product (MVP) ecommerce that is
easily customizable to fit various potential businesses solutions such as online electronics shop,
online book shop, etc.
This project can also be used as referential materials for future migration to Micro-services
architecture and newer front-end technology such as Angular and React.

### Project Application Layers

The application layers of EStore ecommerce site include:
+ Presentation Layer: This layer is responsible for displaying information to
customers and facilitating user interactions, such as product browsing and
checkout.
+ Business Logic Layer: This layer contains the logic for handling business
processes, such as product selection, pricing calculations, and order
fulfillment.
+ Data Access Layer: This layer manages the database and provides the
necessary data for the business logic layer.
+ Database Layer: This layer stores the ecommerce site’s data, such as product
information, customer information, and order information.
+ Security Layer: This layer provides authentication and authorization for
access to the application and handles password encryption.

![image](https://github.com/user-attachments/assets/6fbcfeda-6722-4c1f-b588-41a481be5333)

### Modules design 

Based on DDD methodology and functional requirements, EStore monolith architecture are
modular that is composed of several independent modules that are integrated into a single,
unified system.

EStore modules consist of 5 modules and folder for storing images locally as depicted in the
following image:

![image](https://github.com/user-attachments/assets/ffd139b7-4634-4c09-8754-c246b024fd64)

### Advanced functional requirements
## Security Design
End users of EStore B2B can be divided into 4 roles: administer, editor, salesperson, shipper.
Therefore, it is important to implement role-based authentication to restrict access to
resources.

![image](https://github.com/user-attachments/assets/b251b975-4f1c-4811-9e53-7d3f521de664)

Despite having the same access to a module, there are some CRUD restriction and UI
differences based on the role of end-user. The restrictions are marked by the use-case
diagram:

![image](https://github.com/user-attachments/assets/534c24c4-0f78-4763-bf89-f79e416b20ef)

## Implementation of OAuth 2.0 with Google and Facebook

EStore uses OAuth 2.0 to access customers information on Google and Facebook without
needing them to register an account. This provides a secure and reliable way to access user
data without requiring users to share their credentials.

The following diagram explain how OAuth 2.0 works in EStore conceptually:

![image](https://github.com/user-attachments/assets/cef83866-1624-4d2d-95f8-d49dfb7ce788)

End users of EStore B2C are visitor and customer illustrated by the following use-case
diagram:

![image](https://github.com/user-attachments/assets/8c2648a2-89bf-408b-9c14-023a0d850d69)

## Sending Email using SMTP Gmail
EStore use this server to send emails to customers from a Gmail address for registration
confirmation, order confirmation.

Example Email received by end-users:
![image](https://github.com/user-attachments/assets/70c17f82-1008-4133-a586-5276ad63b14b)




## Payment processing using Paypal sandbox

EStore uses Paypal Sandbox for online transaction besides physical transaction which is cash-
on-delivery (COD).

![image](https://github.com/user-attachments/assets/b3dd8346-08c2-4ccf-ae05-958843cc57b7)


## Displaying sales report using Google Chart
Sales report in ecommerce is a report that provides detailed information about the sales of
products or services on an ecommerce platform. 

The online documentation for Google Chart API can be found at
https://developers.google.com/chart.

EStore uses bar chart, pie chart and table chart to display sales by number of orders, sales by
category and sales by product.

![image](https://github.com/user-attachments/assets/196ebfdc-5db1-4767-913c-4786ddc6418a)

![image](https://github.com/user-attachments/assets/1d2ad57b-572c-431c-a4da-50b54bc8bccd)


