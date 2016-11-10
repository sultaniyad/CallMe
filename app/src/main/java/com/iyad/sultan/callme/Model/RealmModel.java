package com.iyad.sultan.callme.Model;

import java.util.List;

import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by salkhmis on 10/22/2016.
 */


public class RealmModel {
    //Var

    //Realm
    private Realm realm;


    public RealmModel() {
        realm = Realm.getDefaultInstance();

    }

    public Boolean addContacts(final List<Contacts> contactsList) {


        Boolean isFaild = false;
        try {

            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.delete(Contacts.class);
                    realm.insert(contactsList);
                }
            });

        } catch (Exception e) {
            isFaild = true;
        }

        return isFaild;
    }//end method

    public RealmResults<Contacts> getContacts() {
        final RealmResults<Contacts> realmResults = realm.where(Contacts.class).findAll();
        return realmResults;
    }

    public RealmResults<Contacts> getQueryConcats(String query) {
        final RealmResults<Contacts> realmResults = realm.where(Contacts.class).contains("name", query, Case.INSENSITIVE).findAll();
        return realmResults;
    }


    public void onDestroyed() {
        realm.close();
    }


    //Deal with user settings


    public int getUserOperator() {

        UserSetting userSetting = realm.where(UserSetting.class).findFirst();
        if (userSetting != null)
            return userSetting.getOperate();

        return 0;
    }

    public void insertUserOperator(final int userSelect) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.delete(UserSetting.class);
                UserSetting setting = new UserSetting();
                setting.setOperate(userSelect);
                realm.insert(setting);
            }
        });

    }


    //end user setting
}


