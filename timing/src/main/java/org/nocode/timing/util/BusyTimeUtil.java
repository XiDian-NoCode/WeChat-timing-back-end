package org.nocode.timing.util;

import java.util.*;

public class BusyTimeUtil {
    /**
     * @return
     * 功能:
     *      将前端的
     *      [{date=2019-04-01, t1=1, t2=0, t3=1}, {date=2019-04-02, t1=0, t2=1, t3=0}]
     *      转换为 String
     *      1,0,1,0,1,0
     */
    public static String convertListToString(List busyTimeList) {
        StringBuffer stringBuffer = new StringBuffer();

        int length = busyTimeList.size();
        for(int i=0; i<length; i++) {   // 外层循环
            // Map格式:{date=2019-04-01, t1=1, t2=0, t3=1}
            Map<String, String> one_day = (Map<String, String>) busyTimeList.get(i);

            if(i==length-1) {
                stringBuffer.append(String.valueOf(one_day.get("t1")) + ",");
                stringBuffer.append(String.valueOf(one_day.get("t2")) + ",");
                stringBuffer.append(String.valueOf(one_day.get("t3")));
            } else {
                stringBuffer.append(String.valueOf(one_day.get("t1")) + ",");
                stringBuffer.append(String.valueOf(one_day.get("t2")) + ",");
                stringBuffer.append(String.valueOf(one_day.get("t3")) + ",");
            }
        }
//        System.out.println(stringBuffer);
        return stringBuffer.toString();
    }
}
