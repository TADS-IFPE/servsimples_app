/*
 * Dispositivos Móveis - IFPE 2023
 * Author: Willian Santos
 * Project: ServSimplesApp
 */
package ifpe.edu.br.servsimples.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import ifpe.edu.br.servsimples.R;
import ifpe.edu.br.servsimples.managers.ServSimplesServerManagerImpl;
import ifpe.edu.br.servsimples.managers.ServicesInterfaceWrapper;
import ifpe.edu.br.servsimples.model.User;
import ifpe.edu.br.servsimples.util.CryptoUtils;

public class RegisterActivity extends AppCompatActivity {

    private Button mBtRegister;
    private EditText mEtName;
    private EditText mEtUsername;
    private EditText mEtPassword;
    private EditText mEtPasswordConfirm;
    private EditText mEtCPF;
    private CheckBox mCbIsProfessionalUser;

    private ServSimplesServerManagerImpl mServSimplesServerManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mServSimplesServerManager = ServSimplesServerManagerImpl.getInstance();

        findViews();
        setListeners();
    }

    private void setListeners() {
        mBtRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = mEtName.getText().toString();
                String username = mEtUsername.getText().toString();
                String password = mEtPassword.getText().toString();
                String passwordConfirm = mEtPasswordConfirm.getText().toString();
                String cpf = mEtCPF.getText().toString();

                boolean isProfessionalUser = mCbIsProfessionalUser.isChecked();

                if (name.isEmpty() || username.isEmpty() || password.isEmpty()
                        || passwordConfirm.isEmpty() || cpf.isEmpty()) {
                    Toast.makeText(RegisterActivity.this,
                            "Todos os campos precisam ser preenchidos",
                            Toast.LENGTH_LONG).show();
                    return;
                }

                if (!password.equals(passwordConfirm)) {
                    Toast.makeText(RegisterActivity.this,
                            "Confirmação de senha incorreta",
                            Toast.LENGTH_LONG).show();
                }

                if (username.equals(password)) {
                    Toast.makeText(RegisterActivity.this,
                            "A senha não pode ser igual ao nome de usuário",
                            Toast.LENGTH_LONG).show();
                }

                User user = new User();
                user.setName(name);
                user.setCpf(CryptoUtils.stringToHash(cpf));
                user.setUserName(CryptoUtils.stringToHash(username));
                user.setPassword(CryptoUtils.stringToHash(password));
                user.setUserType(isProfessionalUser ?
                        User.UserType.PROFESSIONAL : User.UserType.USER);

                mServSimplesServerManager.registerUser(user,
                        new ServicesInterfaceWrapper.RegistrationCallback() {
                            @Override
                            public void onSuccess(User device) {
                                // TODO go to main screen
                            }

                            @Override
                            public void onFailure(String message) {
                                Toast.makeText(RegisterActivity.this,
                                        "Não foi possível registrar o usuário",
                                        Toast.LENGTH_LONG).show();
                            }
                        });
            }
        });
    }

    private void findViews() {
        mBtRegister = findViewById(R.id.register_bt_submit);
        mEtName = findViewById(R.id.register_et_name);
        mEtUsername = findViewById(R.id.register_et_username);
        mEtPassword = findViewById(R.id.register_et_password);
        mEtPasswordConfirm = findViewById(R.id.register_et_password_confirmation);
        mEtCPF = findViewById(R.id.register_et_cpf);
        mCbIsProfessionalUser = findViewById(R.id.register_cb_usertype);
    }
}