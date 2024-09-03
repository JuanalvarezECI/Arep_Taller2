package org.example;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

/**
 * La clase SimpleHttpServer implementa un servidor HTTP simple que puede manejar solicitudes GET, POST y DELETE.
 * El servidor opera en el puerto especificado en el constructor y sirve archivos desde el directorio "src/webroot".
 */
public class SimpleHttpServer {
    /**
     * El puerto en el que el servidor escucha las solicitudes.
     */
    private final int port;
    private final List<Route> routes = new ArrayList<>();

    /**
     * Crea una nueva instancia de SimpleHttpServer que escucha en el puerto especificado.
     *
     * @param port el puerto en el que el servidor escuchará las solicitudes.
     */
    public SimpleHttpServer(int port) {
        this.port = port;
    }
    /**
     * Inicia el servidor. El servidor continuará escuchando y manejando las solicitudes hasta que el programa se detenga.
     */
    public void get(String path, BiFunction<Request, Response, String> handler) {
        routes.add(new Route(path, handler));
    }
    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                Socket socket = serverSocket.accept();
                new Thread(() -> {
                    try {
                        handleRequest(socket);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            socket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * Maneja una solicitud HTTP entrante. Este método lee la solicitud del socket, determina el tipo de solicitud
     * (GET, POST, DELETE) y responde en consecuencia.
     *
     * @param socket el socket desde el cual se lee la solicitud y se escribe la respuesta.
     * @throws IOException si ocurre un error de entrada/salida al leer la solicitud o escribir la respuesta.
     */
    public void handleRequest(Socket socket) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream());

        String line;
        String requestPath = null;
        while (!(line = in.readLine()).isEmpty()) {
            System.out.println(line);

            if (line.startsWith("GET")) {
                requestPath = line.split(" ")[1];
            }
        }

        if (requestPath != null) {
            boolean routeHandled = false;
            for (Route route : routes) {
                String routePath = route.getPath();
                if (routePath.equals(requestPath.split("\\?")[0])) {
                    Request req = new Request(requestPath);
                    Response res = new Response();
                    String response = route.getHandler().apply(req, res);
                    out.println("HTTP/1.1 200 OK");
                    out.println("Content-Length: " + response.length());
                    out.println();
                    out.println(response);
                    routeHandled = true;
                    break;
                }
            }

            if (!routeHandled) {
                if (requestPath.equals("/index")) {
                    requestPath = "/index.html";
                }
                String filePath = "src/webroot" + requestPath.split("\\?")[0];
                Path path = Paths.get(filePath);

                if (Files.exists(path)) {
                    byte[] data = Files.readAllBytes(path);
                    out.println("HTTP/1.1 200 OK");
                    out.println("Content-Length: " + data.length);
                    out.println();
                    out.flush();
                    socket.getOutputStream().write(data);
                } else {
                    out.println("HTTP/1.1 404 Not Found");
                    out.println();
                }
            }
        }

        out.flush();
    }
    /**
     * El punto de entrada principal del programa. Crea una nueva instancia de SimpleHttpServer y la inicia.
     *
     * @param args los argumentos de la línea de comandos. No se utilizan en este programa.
     */
        public static void main(String[] args) {
            SimpleHttpServer server = new SimpleHttpServer(8080);
            server.get("/hello", (req, res) -> "hello " + req.getValue("name"));
            server.get("/pi", (req, resp) -> {return String.valueOf(Math.PI);});
            server.start();
        }
    }