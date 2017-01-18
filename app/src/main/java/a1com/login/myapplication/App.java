package a1com.login.myapplication;

/**
 * Created by tvyas on 01/10/2017.
 */

import android.app.Application;

import com.auth0.android.result.Credentials;

public class App extends Application {

    private Credentials mUserCredentials;

    private static App appSingleton;

    public static App getInstance() {
        return appSingleton;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        appSingleton = this;
    }

    public Credentials getUserCredentials() {
        return mUserCredentials;
    }

    public void setUserCredentials(Credentials userCredentials) {
        this.mUserCredentials = userCredentials;
    }
}