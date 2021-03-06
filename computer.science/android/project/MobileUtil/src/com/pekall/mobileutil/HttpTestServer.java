/*
 * Copyright (C) 2013 Capital Alliance Software LTD (Pekall)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pekall.mobileutil;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

/**
 * A simple HTTP server for test, which just returns a simple json string
 * in the response body.
 */
public class HttpTestServer extends Thread {

    private static final int TEST_PORT = 60000;
    private final byte[] content;
    private byte[] header;
    private boolean mRunning = false;

    private HttpTestServer(byte[] data) throws UnsupportedEncodingException {
        this.content = data;
        String header = "HTTP/1.0 200 OK\r\n"
                + "Server: OneFile 1.0\r\n"
                + "Content-length: " + this.content.length + "\r\n"
                + "Content-type: text/plain\r\n\r\n";
        this.header = header.getBytes("ASCII");
    }

    @SuppressWarnings("UnusedDeclaration")
    public void quit() {
        synchronized (this) {
            this.mRunning = false;
        }
    }

    public void run() {
        try {
            mRunning = true;
            ServerSocket server = new ServerSocket(TEST_PORT);
            server.setSoTimeout(5000);
            System.out.println("connection on port " + server.getLocalPort());
            System.out.println("Data to be sent:");
            System.out.write(this.content);
            while (true) {
                synchronized (this) {
                    if (!mRunning) {
                        System.out.println("quit thread");
                        break;
                    }
                }
                Socket connection = null;
                try {
                    connection = server.accept();
                    OutputStream out = new BufferedOutputStream(
                            connection.getOutputStream()
                    );
                    InputStream in   = new BufferedInputStream(
                            connection.getInputStream()
                    );
                    // read the first line only; that's all we need
                    StringBuilder request = new StringBuilder(80);
                    while (true) {
                        int c = in.read();
                        if (c == '\r' || c == '\n' || c == -1) break;
                        request.append((char) c);

                    }
                    // If this is HTTP/1.0 or later send a MIME header
                    if (request.toString().contains("HTTP/")) {
                        out.write(this.header);
                    }
                    out.write(this.content);
                    out.flush();
                } catch (SocketTimeoutException ex) {
                    System.out.println("accept timeout");
                } catch (IOException ex) {
                    System.out.println("io exception: " + ex.toString());
                } finally {
                    if (connection != null) connection.close();
                }
            }
        } catch (IOException ex) {
            System.err.println("Could not start server. Port Occupied");
        }
    }

    private static final String TEST_JSON =
            "{\"lines\":[{\"username\":\"Ray\",\"password\":\"12345\",\"email\":null,\"userType\":null,\"fullName\":null,\"firstName\":null,\"lastName\":null,\"phone\":null,\"deviceType\":null,\"os\":null,\"language\":null,\"securityInfo\":null,\"tmpUuid\":null},{\"username\":\"Tom\",\"password\":null,\"email\":null,\"userType\":null,\"fullName\":null,\"firstName\":null,\"lastName\":null,\"phone\":null,\"deviceType\":null,\"os\":null,\"language\":null,\"securityInfo\":null,\"tmpUuid\":null}]}";

    public static HttpTestServer launch() throws UnsupportedEncodingException {
        HttpTestServer t = new HttpTestServer(TEST_JSON.getBytes("UTF-8"));
        t.start();
        return t;
    }
}
