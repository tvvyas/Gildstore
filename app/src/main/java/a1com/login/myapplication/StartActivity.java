package a1com.login.myapplication;

/**
 * Created by tvyas on 01/04/2017.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        Intent lockIntent = new Intent(this, LoginActivity.class);
        startActivity(lockIntent);
    }
}
