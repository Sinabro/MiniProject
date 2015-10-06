package org.sopt;

import javax.activation.MimetypesFileTypeMap;
import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.util.StringTokenizer;

public class ServerThread extends Thread {
    private static final String DEFAULT_FILE_PATH = "index.html";
    Socket clientSocket;

    public ServerThread(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        System.out.println("WebServer Thread Created");
        BufferedReader inFromClient = null;
        DataOutputStream outToClient = null;
        URL url = null;
        File file = null;

        System.out.println("WebServer Thread Created");
        BufferedReader inFromClinet = null;

        try {
            // 클라이언트와 통신을 위한 입/출력 2개의 스트림을 생성한다.
            inFromClient = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));
            outToClient = new DataOutputStream(
                    clientSocket.getOutputStream());

            // 클라이언트로의 메시지중 첫번째 줄을 읽어들인다.
            String requestMessageLine = inFromClient.readLine();

            // 파싱을 위한 토큰을 생성한다.
            StringTokenizer tokenizedLine = new StringTokenizer(
                    requestMessageLine);

            // 첫번째 토큰이 GET으로 시작하는가? ex) GET /green.jpg
            if(tokenizedLine.nextToken().equals("GET")) {
                // 다음의 토큰은 파일명이다.
                String fileName = DEFAULT_FILE_PATH;


                file = new File(fileName);
                // 요청한 파일이 존재하는가?
                if(file.exists()) {
                    // 존재하는 파일의 MIME타입을 분석한다.
                    String mimeType = new MimetypesFileTypeMap()
                            .getContentType(file);

                    // 파일의 바이트수를 찾아온다.
                    int numOfBytes = (int) file.length();

                    // 파일을 스트림을 읽어들일 준비를 한다.
                    FileInputStream inFile = new FileInputStream(fileName);
                    byte[] fileInBytes = new byte[numOfBytes];
                    inFile.read(fileInBytes);

                    // 정상적으로 처리가 되었음을 나타내는 200 코드를 출력한다.
                    outToClient.writeBytes("HTTP/1.0 200 Document Follows \r\n");
                    outToClient.writeBytes("Content-Type: " + mimeType + "\r\n");

                    // 출력할 컨텐츠의 길이를 출력
                    outToClient.writeBytes("Content-Length: " + numOfBytes + "\r\n");
                    outToClient.writeBytes("\r\n");

                    // 요청 파일을 출력한다.
                    outToClient.write(fileInBytes, 0, numOfBytes);
                    clientSocket.close();
                }

                else {
                    // 파일이 존재하지 않는다는 에러인 404 에러를 출력하고 접속을 종료한다.
                    System.out.println("Requested File Not Found : " + fileName);

                    outToClient.writeBytes("HTTP/1.0 404 Not Found \r\n");
                    outToClient.writeBytes("Connection: close\r\n");
                    outToClient.writeBytes("\r\n");
                }
            }
            else {
                // 잘못된 요청임을 나타내는 400 에러를 출력하고 접속을 종료한다.
                System.out.println("Bad Request");

                outToClient.writeBytes("HTTP/1.0 400 Bad Request Message \r\n");
                outToClient.writeBytes("Connection: close\r\n");
                outToClient.writeBytes("\r\n");
            }

            clientSocket.close();
            System.out.println("Connection Closed");
        }
        // 예외 처리
        catch(IOException ioe) {
            ioe.printStackTrace();
        }
    }
}