/*
 * Dispositivos Móveis - IFPE 2023
 * Author: Willian Santos
 * Project: ServSimplesApp
 */
package ifpe.edu.br.servsimples.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import ifpe.edu.br.servsimples.R;
import ifpe.edu.br.servsimples.managers.IServerManagerInterfaceWrapper;
import ifpe.edu.br.servsimples.managers.ServerManager;
import ifpe.edu.br.servsimples.model.User;
import ifpe.edu.br.servsimples.ui.home.HomeHolderActivity;
import ifpe.edu.br.servsimples.util.PersistHelper;
import ifpe.edu.br.servsimples.util.ServSimplesAppLogger;
import ifpe.edu.br.servsimples.util.ServSimplesConstants;
import ifpe.edu.br.servsimples.util.ServerResponseCodeParser;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = RegisterActivity.class.getSimpleName();
    private Button mBtSubmit;
    private EditText mEtName;
    private EditText mEtBio;
    private EditText mEtUsername;
    private EditText mEtPassword;
    private EditText mEtPasswordConfirm;
    private EditText mEtCPF;
    private CheckBox mCbIsProfessionalUser;
    private static boolean isUpdateUserAction = false;

    private ServerManager mServSimplesServerManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mServSimplesServerManager = ServerManager.getInstance();
        findViews();
        Intent intent = getIntent();
        if (intent != null && ServSimplesConstants.ACTION_EDIT_PROFILE.equals(intent.getAction())) {
            performEditUser();
        } else {
            performRegisterUser();
        }
    }

    private void performRegisterUser() {
        if (ServSimplesAppLogger.ISLOGABLE)
            ServSimplesAppLogger.d(TAG, "performRegisterUser:");
        setTitle("Registrar");
        mBtSubmit.setText("Registrar");
        mBtSubmit.setOnClickListener(view -> {
            String name = mEtName.getText().toString();
            String bio = mEtBio.getText().toString();
            String username = mEtUsername.getText().toString();
            String password = mEtPassword.getText().toString();
            String passwordConfirm = mEtPasswordConfirm.getText().toString();
            String cpf = mEtCPF.getText().toString();
            boolean isProfessionalUser = mCbIsProfessionalUser.isChecked();
            if (isAnyFieldEmpty()) {
                Toast.makeText(RegisterActivity.this,
                        "Todos os campos precisam ser preenchidos",
                        Toast.LENGTH_LONG).show();
                return;
            }
            if (!password.equals(passwordConfirm)) {
                Toast.makeText(RegisterActivity.this,
                        "Confirmação de senha incorreta",
                        Toast.LENGTH_LONG).show();
                return;
            }
            if (username.equals(password)) {
                Toast.makeText(RegisterActivity.this,
                        "A senha não pode ser igual ao nome de usuário",
                        Toast.LENGTH_LONG).show();
                return;
            }

            User user = new User();
            user.setName(name);
            user.setBio(bio);
            user.setCpf(cpf);
            user.setUserName(username);
            user.setPassword(password);
            user.setUserType(isProfessionalUser ?
                    User.UserType.PROFESSIONAL : User.UserType.USER);

            mServSimplesServerManager.registerUser(user,
                    new IServerManagerInterfaceWrapper.ServerRequestCallback() {
                        @Override
                        public void onSuccess(User user) {
                            if (user == null) {
                                if (ServSimplesAppLogger.ISLOGABLE)
                                    ServSimplesAppLogger.d(TAG, "Algo deu MUITO errado");
                                return;
                            }
                            PersistHelper.saveUserInfo(user, getApplicationContext());
                            PersistHelper.setUserLogged(getApplicationContext(), true);
                            startActivity(new Intent(RegisterActivity.this,
                                    HomeHolderActivity.class));
                            finish();
                        }

                        @Override
                        public void onFailure(String message) {
                            Toast.makeText(RegisterActivity.this,
                                    ServerResponseCodeParser.parseToString(message),
                                    Toast.LENGTH_LONG).show();
                        }
                    });
        });
    }

    private boolean isAnyFieldEmpty() {
        String cpf = mEtCPF.getText().toString();
        return isAnyEditableFieldEmpty() || cpf.isEmpty();
    }

    private void performEditUser() {
        if (ServSimplesAppLogger.ISLOGABLE)
            ServSimplesAppLogger.d(TAG, "performEditUser:");
        setTitle("Editar Usuário");
        User user = PersistHelper.getCurrentUser(getApplicationContext());
        retrieveUserInfo(user);
    }

    private void retrieveUserInfo(User user) {
        mServSimplesServerManager.getUser(user,
                new IServerManagerInterfaceWrapper.ServerRequestCallback() {
                    @Override
                    public void onSuccess(User user) {
                        mEtCPF.setText(user.getCpf());
                        mEtCPF.setEnabled(false);
                        mEtName.setText(user.getName());
                        mEtBio.setText(user.getBio());
                        mEtUsername.setText(user.getUserName());
                        mCbIsProfessionalUser.setChecked(user.getUserType() == User.UserType.PROFESSIONAL);
                        mBtSubmit.setText("Atualizar");
                        mBtSubmit.setOnClickListener(view -> {
                            editUser();
                        });
                    }

                    @Override
                    public void onFailure(String message) {
                        Toast.makeText(RegisterActivity.this,
                                ServerResponseCodeParser.parseToString(message),
                                Toast.LENGTH_LONG).show();
                        finish();
                    }
                });
    }

    private void editUser() {
        User currentUser = PersistHelper.getCurrentUser(getApplicationContext());
        User editUser = new User();
        editUser.setToken(currentUser.getToken());
        editUser.setCpf(currentUser.getCpf());
        if (!isAnyEditableFieldEmpty()) {
            String name = mEtName.getText().toString();
            String bio = mEtBio.getText().toString();
            String username = mEtUsername.getText().toString();
            String password = mEtPassword.getText().toString();

            editUser.setName(name);
            editUser.setBio(bio);
            editUser.setUserName(username);
            editUser.setPassword(password);
            boolean isProfessionalUser = mCbIsProfessionalUser.isChecked();
            editUser.setUserType(isProfessionalUser ?
                    User.UserType.PROFESSIONAL : User.UserType.USER);

            mServSimplesServerManager.updateUser(editUser,
                    new IServerManagerInterfaceWrapper.ServerRequestCallback() {
                        @Override
                        public void onSuccess(User user) {
                            PersistHelper.saveUserInfo(user, getApplicationContext());
                            PersistHelper.setUserLogged(getApplicationContext(), true);
                            finish();
                        }

                        @Override
                        public void onFailure(String message) {
                            Toast.makeText(RegisterActivity.this,
                                    ServerResponseCodeParser.parseToString(message),
                                    Toast.LENGTH_LONG).show();
                        }
                    });
        } else {
            Toast.makeText(RegisterActivity.this,
                    "Todos os campos precisam ser preenchidos",
                    Toast.LENGTH_LONG).show();
        }

    }

    private boolean isAnyEditableFieldEmpty() {
        String name = mEtName.getText().toString();
        String bio = mEtBio.getText().toString();
        String username = mEtUsername.getText().toString();
        String password = mEtPassword.getText().toString();
        String passwordConfirm = mEtPasswordConfirm.getText().toString();
        return name.isEmpty() || username.isEmpty() || password.isEmpty()
                || passwordConfirm.isEmpty() || bio.isEmpty();
    }

    private void findViews() {
        mBtSubmit = findViewById(R.id.register_bt_submit);
        mEtName = findViewById(R.id.register_et_name);
        mEtUsername = findViewById(R.id.register_et_username);
        mEtBio = findViewById(R.id.register_et_bio);
        mEtPassword = findViewById(R.id.register_et_password);
        mEtPasswordConfirm = findViewById(R.id.register_et_password_confirmation);
        mEtCPF = findViewById(R.id.register_et_cpf);
        mCbIsProfessionalUser = findViewById(R.id.register_cb_usertype);
    }
}