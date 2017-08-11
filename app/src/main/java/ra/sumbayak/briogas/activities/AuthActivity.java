package ra.sumbayak.briogas.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ra.sumbayak.briogas.BaseActivity;
import ra.sumbayak.briogas.R;
import ra.sumbayak.briogas.api.ApiInterface;
import ra.sumbayak.briogas.api.QRCallback;
import ra.sumbayak.briogas.api.models.LoginCredentials;
import ra.sumbayak.briogas.api.models.Token;

import static ra.sumbayak.briogas.Constant.SPKEY_TOKEN;
import static ra.sumbayak.briogas.Constant.SPNAME;

public class AuthActivity extends BaseActivity {
    
    @BindView (R.id.username) TextInputEditText username;
    @BindView (R.id.password) TextInputEditText password;
    @BindView (R.id.login) Button btnLogin;
    
    @Override
    protected void onCreate (@Nullable Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        authenticate ();
        setContentView (R.layout.activity_login);
        ButterKnife.bind (this);
    }
    
    @OnClick (R.id.login)
    public void login () {
        LoginCredentials credentials = new LoginCredentials ();
        credentials.username = username.getText ().toString ();
        credentials.password = password.getText ().toString ();
        
        btnLogin.setVisibility (View.INVISIBLE);
        showProgressBar (R.id.login_loading, R.id.login);
    
        ApiInterface.Builder
            .build (this)
            .token (credentials)
            .enqueue (new QRCallback<Token> () {
                @Override
                protected void onSuccessful (@NonNull Token body) {
                    getSharedPreferences (SPNAME, MODE_PRIVATE)
                        .edit ()
                        .putString (SPKEY_TOKEN, body.token)
                        .apply ();
                    authenticate ();
                }
    
                @Override
                protected void onFailure () {
                    Toast.makeText (AuthActivity.this, "Login failed. Try again.", Toast.LENGTH_SHORT).show ();
                }
    
                @Override
                protected void onExit () {
                    btnLogin.setVisibility (View.VISIBLE);
                    dismissProgressBar (R.id.login_loading);
                }
            });
    }
    
    private void authenticate () {
        if (!getSharedPreferences (SPNAME, MODE_PRIVATE).contains (SPKEY_TOKEN))
            return;
        
        startActivity (new Intent (this, MainActivity.class));
        finish ();
    }
}
