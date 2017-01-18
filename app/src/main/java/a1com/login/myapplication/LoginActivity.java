package a1com.login.myapplication; /**
 * Created by tvyas on 01/04/2017.
 */

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.auth0.android.Auth0;
import com.auth0.android.authentication.AuthenticationException;
import com.auth0.android.callback.BaseCallback;
import com.auth0.android.lock.Lock;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.auth0.android.Auth0;
import com.auth0.android.lock.AuthenticationCallback;
import com.auth0.android.lock.Lock;
import com.auth0.android.lock.LockCallback;
import com.auth0.android.lock.utils.LockException;
import com.auth0.android.result.Credentials;
import com.auth0.android.authentication.AuthenticationAPIClient;
import com.auth0.android.result.UserProfile;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends Activity {

    private Lock mLock;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Auth0 auth0 = new Auth0(getString(R.string.auth0_client_id), getString(R.string.auth0_domain));
        //Request a refresh token along with the id token.
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("scope", "openid offline_access");
        mLock = Lock.newBuilder(auth0, mCallback)
                .withAuthenticationParameters(parameters)
                //Add parameters to the build
                .build(this);
        // start the login widget, call:
        if (CredentialsManager.getCredentials(this).getIdToken() == null) {
            startActivity(mLock.newIntent(this));
            return;
        }
        // save the credential detail to avoid error of null value of getCredentials in main activity
        App.getInstance().setUserCredentials(CredentialsManager.getCredentials(this));

        AuthenticationAPIClient aClient = new AuthenticationAPIClient(auth0);
        aClient.tokenInfo(CredentialsManager.getCredentials(this).getIdToken())
                .start(new BaseCallback<UserProfile, AuthenticationException>() {
                    @Override
                    public void onSuccess(final UserProfile payload) {
                        LoginActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(LoginActivity.this, "Automatic Login Success", Toast.LENGTH_SHORT).show();
                            }
                        });


                        startActivity(new Intent(getApplicationContext(), MainActivity.class));

                        finish();
                    }

                    @Override
                    public void onFailure(AuthenticationException error) {
                        LoginActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(LoginActivity.this, "Session Expired, please Log In", Toast.LENGTH_SHORT).show();
                            }
                        });
                        CredentialsManager.deleteCredentials(getApplicationContext());
                        startActivity(mLock.newIntent(LoginActivity.this));
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLock.onDestroy(this);
        mLock = null;
    }
    // Callback method for authentication
    private final LockCallback mCallback = new AuthenticationCallback() {
        @Override
        public void onAuthentication(Credentials credentials) {
            Toast.makeText(getApplicationContext(), "Log In - Success", Toast.LENGTH_SHORT).show();
            CredentialsManager.saveCredentials(getApplicationContext(), credentials);
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }

        @Override
        public void onCanceled() {
            Toast.makeText(getApplicationContext(), "Log In - Cancelled", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(LockException error) {
            Toast.makeText(getApplicationContext(), "Log In - Error Occurred", Toast.LENGTH_SHORT).show();
        }
    };

}
