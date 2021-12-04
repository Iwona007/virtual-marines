Virtual-Marines - application for CTM's concurs which is highly recommended by Przemysław Bykowski.
Please be aware that Token is valid only by 1 hour. After that time please go to Postman under url:
https://id.barentswatch.no/connect/token -> POST method and take a new one.
Next change this Token in TrackService.java Class in 28 line. 
This application shows a line from start to destination. A red circle belongs to start point and yellow to the 
destination point.
This project has been uploaded in Github and Dockerhub. However, without correct Token docker image cannot work.
In short video it is show how this application works, how start it and stop by Docker compose. 

Stack technology:
Spring Boot: 2.6.1
Java: 11
Postgresql: 13 (psql)
Hibernate
Spring-Data-JPA
Docker
Thymeleaf
Maven

Tools:
InteliJ Ultimet Idea,
Git,
wsl2 & Ubuntu 20.04 LTS & CMD
Docker for Windows
Postman
Mozilla Firefox

Repository:
GitHub: https://github.com/Iwona007/virtual-marines
DeckerHub: https://hub.docker.com/repository/docker/iwona007/virtualmarines
YT: https://bit.ly/virtual-marines
