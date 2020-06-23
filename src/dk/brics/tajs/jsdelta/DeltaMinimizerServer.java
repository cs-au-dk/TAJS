/*
 * Copyright 2009-2020 Aarhus University
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dk.brics.tajs.jsdelta;

import dk.brics.tajs.jsdelta.util.JSDeltaCommandLineInterfaceToJava;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static dk.brics.tajs.util.Collections.newSet;

/**
 * Persistent server for evaluating predicates of reduced sources, without the overhead of a fresh JVM.
 * <p>
 * The server is started in a separate process, so simultaneous server instances are safe.
 * The server is single-threaded.
 */
class DeltaMinimizerServer implements AutoCloseable {

    private static final boolean DEBUG = false;

    private static Set<Integer> takenPorts = newSet();

    private int port;

    private Process serverProcess;

    public static void main(String... args) {
        DeltaMinimizerServer server = new DeltaMinimizerServer();
        server.start(Collections.singletonList(9900));
        server.sendMessage("ECHO STARTUP");
        server.close();
    }

    private static int pickPort(List<Integer> allowedPorts) {
        for (Integer port : allowedPorts) {
            try (@SuppressWarnings("unused") Socket s = new Socket((String) null, port)) {
                /* ignore, try next */
            } catch (Exception ex) {
                synchronized (DeltaMinimizerServer.class) {
                    if (!takenPorts.contains(port)) {
                        takenPorts.add(port);
                        return port;
                    }
                }
            }
        }
        throw new RuntimeException("Could not find available port");
    }

    public void start(List<Integer> allowedPorts) {
        port = pickPort(allowedPorts);

        if (serverProcess != null) {
            throw new RuntimeException("Cannot start server multiple times without stopping!");
        }
        String separator = System.getProperty("file.separator");
        String classpath = System.getProperty("java.class.path");
        String path = System.getProperty("java.home")
                + separator + "bin" + separator + "java";
        ProcessBuilder processBuilder =
                new ProcessBuilder(path, "-cp",
                        classpath,
                        Server.class.getName(), port + "");
        try {
            processBuilder.redirectOutput(ProcessBuilder.Redirect.INHERIT);
            processBuilder.redirectError(ProcessBuilder.Redirect.INHERIT);

            // start other JVM
            Process process = processBuilder.start();

            // wait for JVM & Server to start
            boolean serverIsListening = false;
            while (!serverIsListening) {
                try (@SuppressWarnings("unused") Socket s = new Socket((String) null, port)) {
                    serverIsListening = true;
                } catch (Exception ex) {
                    try {
                        Thread.sleep(250);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            if (!process.isAlive()) {
                throw new RuntimeException("Could not start server");
            }
            this.serverProcess = process;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public int getPort() {
        return port;
    }

    public void sendMessage(String message) {
        try (Socket socket = new Socket((String) null, port);

             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(
                     new InputStreamReader(socket.getInputStream()))
        ) {
            if (DEBUG) {
                System.out.printf("To Server :: %s%n", message);
            }
            out.println(message);
            String line;
            while ((line = in.readLine()) != null) {
                if (DEBUG) {
                    System.out.printf("From Server :: %s%n", line);
                }
            }
        } catch (IOException e) {
            System.err.println("Could not send message to server, assuming it has died by it self: " + e.getMessage());
        }
    }

    @Override
    public void close() {
        if (serverProcess != null) {
            sendMessage("STOP");
            try {
                Thread.sleep(250);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            serverProcess.destroy();
            serverProcess = null;
            synchronized (DeltaMinimizerServer.class) {
                takenPorts.remove(this.port);
            }
        }
    }

    public static class Server {

        public static void main(String... args) throws IOException {
            TAJSRunPredicateProtocol protocol = new TAJSRunPredicateProtocol();

            if (args.length != 1) {
                System.err.printf("Usage: java %s <port number>", Server.class.getName());
                System.exit(1);
            }

            int port = Integer.parseInt(args[0]);
            try (ServerSocket serverSocket = new ServerSocket(port)) {
                while (!protocol.isDone()) {
                    final Socket clientSocket = serverSocket.accept();
                    try (
                            PrintWriter out =
                                    new PrintWriter(clientSocket.getOutputStream(), true);
                            BufferedReader in = new BufferedReader(
                                    new InputStreamReader(clientSocket.getInputStream()))
                    ) {
                        String inputLine;
                        while ((inputLine = in.readLine()) != null) {
                            out.print(protocol.processInput(inputLine));
                            out.flush();
                            break;
                        }
                        Thread.sleep(250);
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            clientSocket.close();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            } catch (IOException e) {
                System.err.println("Could not listen on port " + port);
                System.exit(-1);
            }
        }

        private static class TAJSRunPredicateProtocol {

            private boolean done = false;

            public boolean isDone() {
                return done;
            }

            public String processInput(String input) throws IOException, ClassNotFoundException {
                String[] inputs = input.split(" ");
                String command = inputs[0];
                switch (command) {
                    case "ECHO": // for debugging
                        return input;
                    case "TEST":
                        boolean success;
                        try {
                            success = JSDeltaCommandLineInterfaceToJava.performTestFromStringArgs(inputs[1], inputs[2], inputs[3]);
                        } catch (Throwable t) {
                            t.printStackTrace();
                            success = false;
                        }
                        return success ? "SUCCESS" : "FAILURE";
                    case "STOP":
                        done = true;
                        return "STOPPING";
                    default:
                        throw new RuntimeException("Unsupported command: " + command);
                }
            }
        }
    }
}
