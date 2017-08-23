package xyz.dongaboomin.greet.job;

import xyz.dongaboomin.etc.db.JedisUse;

import java.util.Calendar;

class SetSchedule  {

    void setJedisHomeText()  {
        JedisUse use = new JedisUse();
        Calendar date = Calendar.getInstance();

        switch (date.get(Calendar.DAY_OF_WEEK)){
            case 1:
                use.setContent("homeText", "내일이 걱정이다..");
                break;
            case 2:
                use.setContent("homeText", "월공 있음? 있음?! 엄청 부럽네..");
                System.out.println("..");
                break;
            case 3:
                use.setContent("homeText", "오늘도 토닥토닥^ㅇ^");
                break;
            case 4:
                use.setContent("homeText", "이제 반인지.. 아직 반인지ㅜ");
                break;
            case 5:
                use.setContent("homeText", "\'목\'빠져라 기다린 주말이 코앞!");
                break;
            case 6:
                use.setContent("homeText", "설마 학교는 아니죠??!");
                break;
            case 7:
                use.setContent("homeText", "이런 앱 보지말고 푹 쉬세용XD");
                break;
            default:
                use.setContent("homeText", "안녕하세요!!");
                break;
        }

    }
}