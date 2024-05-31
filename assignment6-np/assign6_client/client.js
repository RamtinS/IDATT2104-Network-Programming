const net = require('net');

// Simple HTTP server responds with a simple WebSocket client test
const httpServer = net.createServer((connection) => {
  connection.on('data', () => {
    let content = `<!DOCTYPE html>
<html lang="">
  <head>
    <meta charset="UTF-8" /><title>WebSocketClient</title>
  </head>
  <body>
    WebSocket test page
    <form onsubmit="submitForm(event)">
        <input type="text" id="chat-box"/>
        <button type="submit">Submit</button>
    </form>
    <textarea id="forum" readonly></textarea>
    <script>
    
      const chatBox = document.getElementById('chat-box');
      
      const forum = document.getElementById('forum');
      
      let ws = new WebSocket('ws://localhost:3001');
      
      ws.onopen = () => {
        console.log('WebSocket connection opened.'); 
      };
      
      ws.onmessage = event => {
        console.log('Message from server: ' + event.data);
        forum.value = event.data;
      };
      
      function submitForm(event) {
        event.preventDefault();
        ws.send(chatBox.value);
        console.log('Message sent to server: ' + chatBox.value);
      }   
      
    </script>
  </body>
</html>
`;
    connection.write('HTTP/1.1 200 OK\r\nContent-Length: ' + content.length + '\r\n\r\n' + content);
  });
});
httpServer.listen(3000, () => {
  console.log('HTTP server listening on port 3000');
});