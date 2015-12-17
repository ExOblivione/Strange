import java.io.*;
import java.net.*;
import java.util.*;
/**
 *
 * @author Eva P. got help from: java2s
 *
 */
public class Server extends Thread {

    public static void main(String[] args) {
        Server serv = new Server();
        serv.start();
    }

    public void start() {
        int port = 6666;
        // open server socket
        ServerSocket socket = null;
        try {
            socket = new ServerSocket(port);
        } catch (IOException e) {
            System.err.println("Ouch, something went wrong.. " + e);
            System.exit(-1);
        }
        System.out.println("Welcome to my precious little WebServer!!");
        System.out.println("It accepts connection on port " + port);

        // request handler loop
        while (true) {
            Socket connection = null;
            try {
                // wait for request
                connection = socket.accept();
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                OutputStream out = new BufferedOutputStream(connection.getOutputStream());
                PrintStream pout = new PrintStream(out);
                // read first line of request
                String request = in.readLine();
                if (request == null) {
                    continue;
                }
                info(connection, request);
                while (true) {
                    String req = in.readLine();
                    if (req == null || req.length() == 0) {
                        break;
                    }
                }
                // parse the line
                if (!request.startsWith("GET") || request.length() < 14
                        || !(request.endsWith("HTTP/1.0") || request.endsWith("HTTP/1.1"))) {
                    // bad request
                    problemDescr(pout, connection, "400", "Bad Request");
                } else {
                    String req = request.substring(4, request.length() - 9).trim();
                    if (req.indexOf("..") != -1
                            || req.indexOf("/.ht") != -1 || req.endsWith("~")) {
                        problemDescr(pout, connection, "403", "Forbidden");
                    } else {
                        String path = "src/" + req;
                        File f = new File(path);
                        if (f.isDirectory()) {
                            path = path + "index.html";
                            f = new File(path);
                        }
                        try {
                            InputStream file = new FileInputStream(f);
                            pout.print("HTTP/1.0 200 OK\r\n"
                                    + "Content-Type: " + getType(path) + "\r\n"
                                    + "Date: " + new Date() + "\r\n"
                                    + "Server: EvaServer 1.0110\r\n\r\n");
                            System.out.println("*******************************************************************************");
                            System.out.print("HTTP/1.0 200 OK\r\n"
                                    + "Content-Type: " + getType(path) + "\r\n"
                                    + "Date: " + new Date() + "\r\n"
                                    + "Server: EvaServer 1.0110\r\n\r\n");
                            sendFile(file, out);
                            info(connection, "200 OK");
                        } catch (FileNotFoundException e) {
                            String path2 = "src/error404.html";
                            File f2 = new File(path2);
                            InputStream file2 = new FileInputStream(f2);
                            pout.print("HTTP/1.0 404 Not Found\r\n"
                                    + "Content-Type: " + getType(path2) + "\r\n"
                                    + "Date: " + new Date() + "\r\n"
                                    + "Server: EvaServer 1.0110\r\n\r\n");
                            System.out.println("°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°°");
                            System.out.print("HTTP/1.0 404 Not Found\r\n"
                                    + "Content-Type: " + getType(path2) + "\r\n"
                                    + "Date: " + new Date() + "\r\n"
                                    + "Server: EvaServer 1.0110\r\n\r\n");
                            sendFile(file2, out);
                            //problemDescr(pout, connection, "404", "Not Found");
                        }
                    }
                }
                out.flush();
            } catch (IOException e) {
                System.err.println(e);
            }
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (IOException e) {
                System.err.println(e);
            }
        }
    }

    private static String getType(String path) {
        if (path.endsWith(".html") || path.endsWith(".htm")) {
            return "text/html";
        } else if (path.endsWith(".txt") || path.endsWith(".java")) {
            return "text/plain";
        } else if (path.endsWith(".gif")) {
            return "image/gif";
        } else if (path.endsWith(".png")) {
            return "image/png";
        } else if (path.endsWith(".jpg") || path.endsWith(".jpeg")) {
            return "image/jpeg";
        } else {
            return "text/plain";
        }
    }

    private static void sendFile(InputStream file, OutputStream out) {
        try {            

            byte[] buffer = new byte[1000];
            while (file.available() > 0) {
                out.write(buffer, 0, file.read(buffer));
                for (int i=0; i<1000;i++)
                {
                    char c = (char) buffer[i];
                    System.out.print(c);
                }
            }
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    private static void info(Socket connection, String msg) {
        System.err.println("Port: [" + connection.getPort() + "] " + msg);
    }

    private static void problemDescr(PrintStream pout, Socket connection,
            String code, String title) {
        pout.print("HTTP/1.0 " + code + " " + title + "\r\n"
                + " Port " + connection.getLocalPort());
        info(connection, code + " " + title);
    }
}
