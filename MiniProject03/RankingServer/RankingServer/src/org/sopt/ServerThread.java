package org.sopt;

import java.io.*;
import java.net.Socket;

public class ServerThread extends Thread {
    // 클라이언트와의 접속 소켓
    private Socket connectionSocket = new Socket();
    private User user = new User(0, "", "", 0);
    public ServerThread(Socket connectionSocket, User user) {
        this.user = user;
        this.connectionSocket = connectionSocket;
    }
    @Override
    public void run() {
        try {
            // System.out.println(user.getScore());
            connectionSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}