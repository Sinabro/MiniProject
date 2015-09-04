package org.sopt;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * <pre>
 * �������� �����ϴ� Ŭ����
 * </pre>
 *
 * @author eye
 * @since 2010.11.15
 */
class WebServer {
    public static void main(String argv[]) throws Exception {
        // 서버소켓을 생성.
        int POOL_SIZE = 50;
        Executor executor = Executors.newFixedThreadPool(POOL_SIZE);

        ServerSocket listenSocket = new ServerSocket(8080);
        System.out.println("WebServer Socket Created");

        Socket connectionSocket = null;
        ServerThread serverThread = null;
        // 순환을 돌면서 클라이언트의 접속을 받는다.
        // accept()는 Blocking 메서드이다.

        while((connectionSocket = listenSocket.accept()) != null) {
            // 서버 쓰레드를 생성하여 실행한다.
            serverThread = new ServerThread(connectionSocket);
            executor.execute(serverThread);
        }
    }
}