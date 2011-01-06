Foxtrot Mike
============

FM is a rails app that allows gliding clubs or companies running glider sites to manage 
flight data.

Client
------

The client is only used to enter flight data in an efficient way. 
It is written in Scala using swing. Data is stored in a H2 database using JPA. 
Client and server are synchronized using a restful JSON API provided by the server.
