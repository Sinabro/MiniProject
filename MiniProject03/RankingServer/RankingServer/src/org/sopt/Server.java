package org.sopt;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by jiyoungpark on 15. 9. 14..
 */
public class Server {
    public static void main(String argv[]) throws Exception {
        // 서버소켓을 생성.
        int cnt = 0;
        int POOL_SIZE = 100;
        Random random = new Random();
        Map<Integer, User> userMap = new HashMap<Integer, User>();
        List<User> userList = new ArrayList<User>();
        User user;

        Socket clientSocket = null;
        Executor executor = Executors.newFixedThreadPool(POOL_SIZE);

        MyRank myRank = new MyRank(userList, 4);
        TopRank topRank = new TopRank(userList);

        ServerSocket listenSocket = new ServerSocket(8888);
        System.out.println("WebServer Socket Created");

        // 순환을 돌면서 클라이언트의 접속을 받는다.
        // accept()는 Blocking 메서드이다.
        
        while((clientSocket = listenSocket.accept()) != null) {
            // 서버 쓰레드를 생성하여 실행한다.
            user = new User(cnt, "sopt", "female", random.nextInt(1000000001));

            ServerThread serverThread = new ServerThread(clientSocket);

            executor.execute(serverThread);

            userMap.put(cnt, user);
            userList.add(cnt, user);


            //if(cnt % 100 == 99) {
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


            //}

            myRank.run();
            topRank.run();

            //if(cnt % 101 == 100) {

                //myRank.join();
                //myRank.start();
            //}
            //if(cnt % 102 == 101) {

                //topRank.join();
                //topRank.start();
            //}

            cnt++;
        }

        /*
            user = new User(i, "sopt", "female", random.nextInt(1000000001));
            ServerThread serverThread = new ServerThread(connectionSocket, user);
            serverThread.start();
            //executor.execute(serverThread);
            //System.out.println(user.getId());
            userMap.put(i, user);
            userList.add(i - 1, user);
            if(i % 100 == 99) {
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
            }
            if(i % 101 == 100) {
                MyRank myRank = new MyRank(userList, 23);
                myRank.join();
                myRank.start();
            }
            if(i % 102 == 101) {
                TopRank topRank = new TopRank(userList);
                topRank.join();
                topRank.start();
            }
        }*/



        // MyRank 가져오기
       /* System.out.println("=============================");

        int userID = 10;

        for (int i = userID - 10 ; i < userID + 11 ; i++) {
            if(i < 0)                           // 나보다 점수를 잘 받은 사람이 10명 미만
                i = 0;
            if(i >= userList.size())            // 나보다 점수를 못 받은 사람이 10명 미만
                break;

            if(i < userID)                         // 나보다 점수를 잘 받은 사람


            else if(i == userID)                   // 내 점수 출력
                System.out.println("내 등수 : " + (i+1) + " / ID : " + userList.get(i).getId() + " / 내 점수 : " + userList.get(userID).getScore());

            else if(i > userID && i < userID + 11)    // 나보다 점수를 못 받은 사람
                System.out.println("등수 : " + (i+1) + " / ID : " + userList.get(i).getId() + " / 점수 : " + userList.get(i).getScore() + " 점");
        }*/

        /* Top 10 Rank 가져오기 */
        /*for(int i = 0 ; i < 10 ; i ++)
            System.out.println("등수 : " + (i+1) + " / ID : " + userList.get(i).getId() + " / 점수 : " + userList.get(i).getScore() + " 점");*/
    }
}