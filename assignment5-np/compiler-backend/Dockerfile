# Use gcc image as the base image.
FROM gcc:latest
# Create a new app directory for my application files.
WORKDIR /app
# Copy the source code into the container.
COPY src/main/resources/main.cpp /app
# Compile the c++ code.
RUN g++ -o main main.cpp
# Run the compiled program.
CMD ["./main"]