package org.sopt;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jiyoungpark on 15. 9. 18..
 */
public class MyRank extends Thread {
    List<User> userList = new ArrayList<User>();
    int userID;

    public MyRank(List<User> userList, int userID) {
        this.userList = userList;
        this.userID = userID;
    }
    @Override
    public void run() {
        System.out.println("<<<<<<<<< My Rank >>>>>>>>>>");
        for (int i = userID - 10 ; i < userID + 11 ; i++) {
            if(i < 0)                           // 나보다 점수를 잘 받은 사람이 10명 미만
                i = 0;
            if(i >= userList.size())            // 나보다 점수를 못 받은 사람이 10명 미만
                break;

            if(i < userID)                         // 나보다 점수를 잘 받은 사람
                System.out.println("등수 : " + (i+1) + " / ID : " + userList.get(i).getId() + " / 점수 : " + userList.get(i).getScore() + " 점");

            else if(i == userID)                   // 내 점수 출력
                System.out.println("내 등수 : " + (i+1) + " / ID : " + userList.get(i).getId() + " / 내 점수 : " + userList.get(userID).getScore());

            else if(i > userID && i < userID + 11)    // 나보다 점수를 못 받은 사람
                System.out.println("등수 : " + (i+1) + " / ID : " + userList.get(i).getId() + " / 점수 : " + userList.get(i).getScore() + " 점");
        }
    }

}
