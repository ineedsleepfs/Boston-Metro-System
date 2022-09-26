# Boston-Metro-System
###### CS308 Group Assignment

##### Completed by Liam McGloin, Sudeep Dhakal, Karmen Tsang, Aqsa Hamid, Erin Allen

The aim of this exercise is to design and implement a multi-graph ADT and to use the multi-graph to model the Boston Metro System in a program that is able to provide directions to passengers on how to get from one station to another. The system must have a graphical user interface, and follows a properly decoupled Model-View-Controller software architecture.

You'll need to decide on the basic functionality of your program, design a GUI for it. At the minimum, the system should be possible to:

- enter a start location

- enter a destination

- display the planned route

Your GUI should be easy to use and attractive. You will need to design the internal structure of your back-end, and test the program as a whole. 
**A key requirement is that both your design and implementation decouples the multi-graph ADT from the rest of the system.  Both in your design and implementation the GUI and the back-end of the system are fully decoupled.**  
You need to specify an application programmer's interface (API) through which the GUI will access the components of the code. The benefit of decoupling this application into two parts (a front-end GUI and the back-end implementation) by creating a well-defined API is to allow for different types of front-end clients to access your system and use its functionality. 

The design must take the form of **UML class diagram** that focuses on the functionality presented by the multi-graph ADT and must clearly show how the Boston Metro System will use the ADT. It should be an attempt at decoupling the multi-graph from the Boston Metro System with some notes about how to represent the graph in code and how to implement the graph search.

The design of the API should show all the types that API exposes and a brief description of the functionality it supports. Two issues are: 1) whether the API will be passive, in the sense that the application simply treats it as a source of information, or whether it will be active, by having functionality to make periodic call-backs; 2) what types will be exposed at the interface. Your API specification will be a complete description of the functionality of the program.

So you will need to devise a reasonable testing strategy that gives you some confidence that the application works reliably. 

This exercise also expects that you put in practice all the things that have been covered in the class, i.e. method specifications, Java assertions and JUnit tests.
