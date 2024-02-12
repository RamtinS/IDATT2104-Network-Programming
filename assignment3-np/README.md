# Assignment 3: Server-Client interaction with TCP and HTTP.

## Task description

### Task 1 - Simple Server/Client

Create a server program that receives two numbers from a
client over TCP connection and performs a simple addition
or subtraction based on the client's choice. The client
program should read the numbers from the user and allow 
for repeated arithmetic operations. The server should 
handle the client's requests, return the answer, or send
an appropriate error message back to the client. Responses
and error messages should be printed to the user.

### Task 2 - Handling Multiple Clients with Threads

Extend the server from Task 1 to handle requests from
multiple clients simultaneously using threads. Create
a thread class that is either a subclass of Thread or
implements the Runnable interface. This thread should 
handle communication with an individual client, and 
the actual processing should be in the run() method.

### Task 3 - Simple Web Server

Create a simple web server based on the knowledge gained
so far. The server should handle only one client. When a
browser connects, the server should return a welcome
message with HTML formatting, including the client's
header information as a bullet list.