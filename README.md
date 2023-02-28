# Paypal Api Shopping Cart Example

## Versions and Info

### versions

tomcat 10

openjdk 17

spring boot 3.0.3

paypal sdk 1.14.0

postgres 15

managed by jpa, jdbc and hibernate, database can easily be swapped in application.properties

### info

#### usage of the Product Api

Create Product

POST:

contextPath/api/createProduct
```json
{
  "name": "Butter",
  "price": 356.27
}
```

Delete

GET: 

contextPath/api/deleteProduct?id=[Id Of Existing Item]

Update:

POST: 

contextPath/api/updateProduct
```json
{
  "id": 4,
  "name": "Butter",
  "price": 346.27
}
```

Get All Products

GET:

contextPath/api/getProducts

## Setup

### Jenkins

!! first setup your postgresql database.

Be careful with this script, it is made for a Linux Operating System and ment to be an example,
I don't know how to make such a script for a Windows machine. This script should work on a MacOS system
cause of its Unix kernel

!! Maven has to be installed and accessible from jenkins user

```
pipeline {
    agent any
    stages {
        stage('clone from git') {
            steps {
                git url: 'https://github.com/Kreuterkeule/ShoppingCartPaypal.git'
            }
        }
        stage ('change server port') {
            steps {
                sh " sed -i 's/server.port=8080/server.port=<your server port>/' src/main/resources/application.properties"
                sh " sed -i 's/spring.datasource.password=123/spring.datasource.password=<your password>/' src/main/resources/application.properties"
                sh " sed -i 's/localhost/<your domain>/' src/main/java/com/kreuterkeule/ShoppingCartPaypal/controller/WebController.java"
            }
        }
        stage('maven build') {
            steps {
                sh "mvn clean package"
            }
        }
        stage('copy to tomcat10') {
            steps {
                sh "cp ./target/ShoppingCartPaypal.war /opt/tomcat10/webapps/ShoppingCartPaypal.war"
            }
        }
    }
}
```