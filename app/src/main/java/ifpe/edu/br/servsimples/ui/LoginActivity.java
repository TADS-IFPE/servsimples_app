/*
 * Dispositivos Móveis - IFPE 2023
 * Author: Willian Santos
 * Project: ServSimplesApp
 */
package ifpe.edu.br.servsimples.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import ifpe.edu.br.servsimples.R;
import ifpe.edu.br.servsimples.managers.ServSimplesServerManagerImpl;
import ifpe.edu.br.servsimples.managers.ServicesInterfaceWrapper;
import ifpe.edu.br.servsimples.model.User;
import ifpe.edu.br.servsimples.ui.home.HomeHolderActivity;
import ifpe.edu.br.servsimples.util.CryptoUtils;
import ifpe.edu.br.servsimples.util.PersistHelper;
import ifpe.edu.br.servsimples.util.ServSimplesAppLogger;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();
    private TextView mTVRegister;
    private EditText mTvUsername;
    private EditText mTvPassword;
    private Button mBtLogin;

    private ServSimplesServerManagerImpl mServSimplesServerManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mServSimplesServerManager = ServSimplesServerManagerImpl.getInstance();
        findViews();
        setListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (PersistHelper.isUserLoggedIn(getApplicationContext())) {
            startActivity(new Intent(LoginActivity.this, HomeHolderActivity.class));
            finish();
        }
    }

    private void setListeners() {
        mBtLogin.setOnClickListener(view -> {
            String userName = mTvUsername.getText().toString();
            String password = mTvPassword.getText().toString();
            if (password.isEmpty() || userName.isEmpty()) {
                Toast.makeText(LoginActivity.this,
                        "Os campos devem estar preenchidos", Toast.LENGTH_SHORT).show();
                return;
            }
            User user = new User();
            user.setUserName(userName);
            user.setPassword(password);
            mServSimplesServerManager.loginUser(user,
                    new ServicesInterfaceWrapper.RegistrationCallback() {
                @Override
                public void onSuccess(User user) {
                    if (ServSimplesAppLogger.ISLOGABLE)
                        ServSimplesAppLogger.d(TAG, "onSuccess: cpf:" + user.getCpf());
                    PersistHelper.saveUserInfo(user, getApplicationContext());
                    PersistHelper.setUserLogged(getApplicationContext(), true);
                    startActivity(new Intent(LoginActivity.this,
                            HomeHolderActivity.class));
                    finish();
                }

                @Override
                public void onFailure(String message) {

                }
            });
        });

        mTVRegister.setOnClickListener(view -> startActivity(new Intent(LoginActivity.this, RegisterActivity.class)));
    }

    private void findViews() {
        mTVRegister = findViewById(R.id.login_tv_register);
        mTvUsername = findViewById(R.id.login_et_username);
        mTvPassword = findViewById(R.id.login_et_password);
        mBtLogin = findViewById(R.id.login_bt_submit);
    }
}