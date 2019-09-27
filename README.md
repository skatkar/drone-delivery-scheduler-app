# drone-delivery-scheduler-app

This is an application to implement drone-carried deliveries to the customer. The algorithm is implemented in such a way to maximize the NPS value. More NPS value determines more customer satisfaction for the order delivery.

## Technical details
	Programming langauge: Java (version 1.8.0)
	Testing frameworks: JUnit(version 4.12)
	Build tool: Apache Maven (version 3.6.2)
    
## Assumptions
1) In addition to the expected output file(which contains information about the order id and drone's departure time to deliver it), there will be one more file for undelivered orders with status explaining why this delivery failed.
2) Scenarios for undelivered orders:
    1. If the order is placed after the delivery closed time.
    2. If the estimated delivery time goes beyond the delivery closing time.
    3. If there is an exception while doing time calculations.
3) File operations like
    - Validating the format of each line is not implemented extensively. It is assumed that lines in the files are as mentioned in the requirement document. The expected file format is: <WM####> <Location> <Ordered at>
    - Specifying file location which is nested in multiple directories is not considered. As of now, it picks the file at the location from where the code is executed. For example, if the code is executed from the project root directory, then the input file must be there. In simpler words, the directory from which the code is executed, there must be an input file.
4) Even though it can be inferred from the sample example that the return time for a drone is half of the time required for the delivery. But as per the problem statement, it is assumed that the time required for delivery and returning back to the warehouse is the same. This can be changed by making a minor change in the utility method.
5) If the customer location is N12E3 then the direction of the drone is assumed as 12 blocks to the North from warehouse location then 3 blocks to the East from there. It means, calculate vertical distance first and then add the horizontal distance to it.
6) Even if there are no deliveries, the output file for delivery will contain NPS score with a value of 0.
    
    
## Application design
The application design adheres to the Single responsibility principle, which makes it more readable and testable. 

Classes:-
a) InputDataReader - This is an interface which declares one method. The implementers can read the data from any location.
b) FileInputDataReader - A class which implements InputDataReader interface. This class is supposed to read the file which is provided as it's constructor parameters.
c) Warehouse - A class to represent the warehouse. It has two methods for creating the orders and another method for returning the list of orders.
d) DroneDeliveryService - This is the class which has methods to schedule drone deliveries.
e) DroneDeliveryScedulerUtil - This class consists of several utility methods. 
f) InputReaderFactory - Factory class which creates one of the implementation of the InputDataReader interface depending upon the file type specified. As of now, it just creates an instance of FileInputDataReader.
    
## High-level overview of application flow:
1. The input file is read line by line and based on this data, the list of orders is created.
2. Even though the input file does not have any data about the date on which the order is placed, all of the order objects are created considering the date on which the code is executed.
3. There is a comparator which sorts the list based on two criteria:
    1. Customer location
    2. Order time
    So if two customers are at the same location from the warehouse, then their order time will be considered to sort them.
4. For each record, it is checked whether the estimated time of delivery is within the given range or not. If it is not then it is placed in a separate file having a name as "<Date-Time>_undelivered.txt"
5. If the code execution starts before the delivery start time, then the entire code stops saying code can not be executed at this time.
6. So there are two output files:
    a) <Date-Time>_delivered.txt
    b) <Date-Time>_undelivered.txt
7. These output files will be created in the output directory at the current working directory. Even if the directory does not exist earlier, it will be created by the code.
8. DroneDeliveryService instance which actually starts the execution is made singleton. If the date changes then the same earlier created object will be used but its dependencies are modified to match with the new date.

## How to execute the code
1. Clone the repository.
2. Make sure that JDK 1.8 and Maven 3.6 are installed and environment variables are set correctly.
3. Even if Maven is installed, mvn.cmd can be used to execute maven commands.
4. Create the jar file using the command 
    mvn clean package
5. After the successful build, make sure that two jar files are created in the target folder. Out of them, only drone-scheduler-app-jar-with-dependencies.jar is of importance which contains all the necessary dependencies in it.
6. Command to execute 
### Syntax:
	java -jar <path of a jar file> <input file name>
### Example:
	java -jar target/drone-scheduler-app-jar-with-dependencies.jar input.txt
Note: The code was tested on Windows platform only and not on any Linux platform.
