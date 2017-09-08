package com.tddp2.grupo2.linkup;

import android.app.Application;
import android.content.Context;

import com.tddp2.grupo2.linkup.infrastructure.Database;
import com.tddp2.grupo2.linkup.infrastructure.LinkupDatabase;
import com.tddp2.grupo2.linkup.service.factory.ServiceFactory;


public class LinkupApplication extends Application{

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        initConfiguration();
    }

    private void initConfiguration() {
        Database database = new LinkupDatabase();
        ServiceFactory.init(database);
    }

    public static Context getContext() {
        return mContext;
    }

}
