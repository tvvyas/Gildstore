package a1com.login.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.BoolRes;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.auth0.android.Auth0;
import com.auth0.android.authentication.AuthenticationAPIClient;
import com.auth0.android.authentication.AuthenticationException;
import com.auth0.android.callback.BaseCallback;
import com.auth0.android.management.ManagementException;
import com.auth0.android.management.UsersAPIClient;
import com.auth0.android.result.Delegation;
import com.auth0.android.result.UserProfile;
import com.auth0.android.result.Credentials;
import com.squareup.picasso.Picasso;


public class MainActivity extends AppCompatActivity {
    private AuthenticationAPIClient mAuthenticationClient;
    private Button mEditProfileButton;
    private Button mCancelEditionButton;
    private TextView mUsernameTextView;
    private TextView mUsermailTextView;
    private TextView mUserCountryTextView;
    private EditText mUpdateCountryEditext;
    private UserProfile mUserProfile;
    private TextView mUsermailVTextView;
    private Auth0 mAuth0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth0 = new Auth0(getString(R.string.auth0_client_id), getString(R.string.auth0_domain));
        // The process to reclaim an UserProfile is preceded by an Authentication call.
        Button logoutButton = (Button) findViewById(R.id.logout);
        if (CredentialsManager.getCredentials(this).getIdToken() == null) {

            return;
        }
        App.getInstance().setUserCredentials(CredentialsManager.getCredentials(this));
        AuthenticationAPIClient aClient = new AuthenticationAPIClient(mAuth0);
        aClient.tokenInfo(App.getInstance().getUserCredentials().getIdToken())
                .start(new BaseCallback<UserProfile, AuthenticationException>() {
                    @Override
                    public void onSuccess(final UserProfile payload) {
                        MainActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                mUserProfile = payload;
                                refreshScreenInformation();
                            }
                        });
                    }

                    @Override
                    public void onFailure(AuthenticationException error) {
                        MainActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(MainActivity.this, "Profile Request Failed", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

//        mEditProfileButton = (Button) findViewById(R.id.editButton);
//        mCancelEditionButton = (Button) findViewById(R.id.cancelEditionButton);
        mUsernameTextView = (TextView) findViewById(R.id.userNameTitle);
        mUsermailTextView = (TextView) findViewById(R.id.userEmailTitle);
        mUsermailVTextView = (TextView) findViewById(R.id.userEmailVerified);
//        mUpdateCountryEditext = (EditText) findViewById(R.id.updateCountryEdittext);

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

    }

    private void refreshScreenInformation() {
        mUsernameTextView.setText(String.format(getString(R.string.username), mUserProfile.getName()));
        mUsermailTextView.setText(String.format(getString(R.string.useremail), mUserProfile.getEmail()));
        mUsermailVTextView.setText(String.format(getString(R.string.emailverified),Boolean.toString(mUserProfile.isEmailVerified())));
        ImageView userPicture = (ImageView) findViewById(R.id.userPicture);
        Picasso.with(getApplicationContext()).load(mUserProfile.getPictureURL()).into(userPicture);
        // Not using country update
//        if (!mUserProfile.getUserMetadata().get("country").toString().isEmpty()) {
//            mUserCountryTextView.setVisibility(View.VISIBLE);
//            mUserCountryTextView.setText(String.format(getString(R.string.userCountry), mUserProfile.getUserMetadata().get("country").toString()));
//        }
    }

    private void logout() {
        CredentialsManager.deleteCredentials(this);
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}