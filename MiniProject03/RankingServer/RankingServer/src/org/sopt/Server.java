package org.sopt;

import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.FileLock;
import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by jiyoungpark on 15. 9. 14..
 */
public class Server {
    public static void main(String argv[]) throws Exception {
        // 서버소켓을 생성.
        int POOL_SIZE = 50;
        Random random = new Random();
        Map<Integer, User> userMap = new HashMap<Integer, User>();
        List<User> userList = new ArrayList<User>();
        User user;

        Executor executor = Executors.newFixedThreadPool(POOL_SIZE);

        ServerSocket listenSocket = new ServerSocket(8888);
        System.out.println("WebServer Socket Created");

        Socket connectionSocket = null;

        ServerThread serverThread = null;
        // 순환을 돌면서 클라이언트의 접속을 받는다.
        // accept()는 Blocking 메서드이다.

        /*while((connectionSocket = listenSocket.accept()) != null) {
            // 서버 쓰레드를 생성하여 실행한다.
            serverThread = new ServerThread(connectionSocket);
            executor.execute(serverThread);
        }*/

        for (int i = 1; i < 101; i++) {
            user = new User(i, "sopt", "female", random.nextInt(101));
            userMap.put(i, user);
            userList.add(i - 1, user);

            System.out.println(i + " : " + user.getScore() + " 점");
        }

        // 점수 내림차순 정렬
        Collections.sort(userList, new Comparator<User>() {
            @Override
            public int compare(User o1, User o2) {
                if (o1.getScore() > o2.getScore())
                    return -1;
                else if (o1.getScore() < o2.getScore())
                    return 1;
                else
                    return 0;
            }
        });
        System.out.println("=============================");

        for (int i = 0; i < 99; i++) {
            System.out.println(userList.get(i).getId() + " : " + userList.get(i).getScore() + " 점");
        }


        // MyRank 가져오기
        System.out.println("=============================");

        int num = 0;

        for (int i = num - 10 ; i < num + 11 ; i++) {

            if(i < 0)
                i = 0;

            if(i >= userList.size())
                break;

            if(i < num)
                System.out.println("ID : " + userList.get(i).getId() + " / 점수 : " + userList.get(i).getScore() + " 점");

            else if(i == num)
                System.out.println("ID : " + userList.get(i).getId() + " / 내 점수 : " + userList.get(num).getScore());

            else if(i > num && i < num + 11)
                System.out.println("ID : " + userList.get(i).getId() + " / 점수 : " + userList.get(i).getScore() + " 점");

        }
    }

}