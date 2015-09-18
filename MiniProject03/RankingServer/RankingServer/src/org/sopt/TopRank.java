package org.sopt;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jiyoungpark on 15. 9. 18..
 */
public class TopRank extends Thread {
    List<User> userList = new ArrayList<User>();

    public TopRank(List<User> userList) {
        this.userList = userList;
    }

    @Override
    public void run() {
        System.out.println("<<<<<<<<< Top Rank >>>>>>>>>>");
        for(int i = 0 ; i < 10 && i < userList.size() ; i ++)
            System.out.println("등수 : " + (i+1) + " / ID : " + userList.get(i).getId() + " / 점수 : " + userList.get(i).getScore() + " 점");
    }
}
