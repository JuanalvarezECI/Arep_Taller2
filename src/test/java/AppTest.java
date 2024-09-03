
import org.example.SimpleHttpServer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.*;
import java.net.Socket;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

class AppTest {

    private SimpleHttpServer server;
    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;
    private ByteArrayOutputStream outputStream;

    @BeforeEach
    void setUp() throws IOException {
        server = new SimpleHttpServer(8080);
        socket = mock(Socket.class);
        outputStream = new ByteArrayOutputStream();
        writer = new PrintWriter(outputStream, true);
        when(socket.getOutputStream()).thenReturn(outputStream);
    }

    @Test
    void handleRequest_GET() throws IOException {
        String request = "GET / HTTP/1.1\r\n\r\n";
        reader = new BufferedReader(new StringReader(request));
        when(socket.getInputStream()).thenReturn(new ByteArrayInputStream(request.getBytes()));

        server.handleRequest(socket);

        writer.flush();
        String response = new String(outputStream.toByteArray());
        assertTrue(response.contains("HTTP/1.1 200 OK"));
    }

    @Test
    void handleRequest_POST() throws IOException {
        String request = "POST / HTTP/1.1\r\n\r\n";
        reader = new BufferedReader(new StringReader(request));
        when(socket.getInputStream()).thenReturn(new ByteArrayInputStream(request.getBytes()));

        server.handleRequest(socket);

        writer.flush();
        String response = new String(outputStream.toByteArray());
        assertTrue(response.contains("HTTP/1.1 200 OK"));
    }

    @Test
    void handleRequest_DELETE() throws IOException {
        String request = "DELETE / HTTP/1.1\r\n\r\n";
        reader = new BufferedReader(new StringReader(request));
        when(socket.getInputStream()).thenReturn(new ByteArrayInputStream(request.getBytes()));

        server.handleRequest(socket);

        writer.flush();
        String response = new String(outputStream.toByteArray());
        assertTrue(response.contains("HTTP/1.1 200 OK"));
    }
}