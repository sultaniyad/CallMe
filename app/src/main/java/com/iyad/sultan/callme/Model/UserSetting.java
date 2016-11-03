package com.iyad.sultan.callme.Model;

import io.realm.RealmObject;

/**
 * Created by sultan on 10/29/16.
 */

public class UserSetting extends RealmObject {
    public int getOperate() {
        return Operate;
    }

    public void setOperate(int operate) {
        Operate = operate;
    }

    //STC 12 , mobily 13 zain 14;
    private  int Operate;

}
