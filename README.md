## Chat Client & Server Applications


### **Project Details**
This project involved building two Java applications, a chat client application and a chat server application which can pass messages back and forth over a local host connection. Both applications may be executed from the command line.

### **Running Details**
Both the chatClient and chatServer application do not take any additional command line arguments, they instead request and validate information before starting the chat connection. When the chatServer Application starts it will request for you to select a port number. If an integer is given it will the display the message “Waiting for connection” and will attempt to make a connection on the chosen port number. Once a connection is made the chatServer will display that a connection was made. When a second is made the chatServer will tell you the chat has started. The chatServer will the read and write to the connection until the program is ended. 

When the chatClient is started it will ask if you want to scan ports, it will only progress if a Y is given. It will then request a host name and begin scanning ports. It will display a list of available ports and as if you want to connect to any of the available ports. It will only progress if a Y is given. It will then ask which available port to connect to. Once a connection is made the chatClient will tell you that the chat has started. The chatClient will then read input and write output until the chat is ended. The chat can be ended by typing “exit” into the chatClient.

### **Built With**
- Java
- Eclipse

### **Chat Application**
![Table](/assets/chat.png)

