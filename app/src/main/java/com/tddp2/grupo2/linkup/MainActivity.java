package com.tddp2.grupo2.linkup;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.tddp2.grupo2.linkup.service.factory.ServiceFactory;

import static com.tddp2.grupo2.linkup.service.factory.ServiceFactory.getLoginService;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean isUserRegistered = ServiceFactory.getLoginService().isUserRegistered();
        if (!isUserRegistered){
            if (AccessToken.getCurrentAccessToken() == null){
                goLoginScreen();
            }else{
                LoginManager.getInstance().logOut();
                goLoginScreen();
            }
        } else {
            goLinksScreen();
        }
    }

    private void goLoginScreen() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void goLinksScreen() {
        Intent intent = new Intent(this, LinksActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
