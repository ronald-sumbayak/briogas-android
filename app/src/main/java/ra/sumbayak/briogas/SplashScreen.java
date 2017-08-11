package ra.sumbayak.briogas;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import ra.sumbayak.briogas.activities.AuthActivity;

public class SplashScreen extends AppCompatActivity {
    
    @Override
    protected void onCreate (@Nullable Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.splash_screen);
        Runnable fin = new Runnable () {
            @Override
            public void run () {
                startActivity (new Intent (SplashScreen.this, AuthActivity.class));
                finish ();
            }
        };
        new Handler ().postDelayed (fin, 3000);
    }
    
    @Override
    public void onBackPressed () {
        
    }
}
