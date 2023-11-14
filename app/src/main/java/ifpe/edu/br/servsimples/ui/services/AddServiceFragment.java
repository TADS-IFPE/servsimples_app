/*
 * Dispositivos Móveis - IFPE 2023
 * Author: Willian Santos
 * Project: ServSimplesApp
 */
package ifpe.edu.br.servsimples.ui.services;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ifpe.edu.br.servsimples.R;
import ifpe.edu.br.servsimples.managers.IServerManagerInterfaceWrapper;
import ifpe.edu.br.servsimples.managers.ServSimplesServerManager;
import ifpe.edu.br.servsimples.model.Cost;
import ifpe.edu.br.servsimples.model.Service;
import ifpe.edu.br.servsimples.model.User;
import ifpe.edu.br.servsimples.util.PersistHelper;
import ifpe.edu.br.servsimples.util.ServSimplesAppLogger;
import ifpe.edu.br.servsimples.util.ServSimplesConstants;


public class AddServiceFragment extends Fragment {

    private static final String TAG = AddServiceFragment.class.getSimpleName();
    private static final int GET_CATEGORIES_OK = 0;
    private static final int GET_CATEGORIES_FAIL = 1;

    private EditText mEtNome;
    private Spinner mSpCategory;
    private EditText mEtDescription;
    private EditText mEtCostValue;
    private EditText mEtCostTime;
    private Button mBtSubmit;
    private static String sAction;
    private List<String> mCategories = new ArrayList<>(Arrays.asList("aaa", "bbb", "cccc"));
    private CategoriesAdapter mCategoriesAdapter;

    private final Handler mHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message message) {
            final int what = message.what;
            switch (what) {
                case GET_CATEGORIES_OK:
                    mCategoriesAdapter = new CategoriesAdapter(getContext(), mCategories);
                    mSpCategory.setAdapter(mCategoriesAdapter);
                    break;
                case GET_CATEGORIES_FAIL:
                    break;
            }

        }
    };


    public AddServiceFragment() {
    }

    public static AddServiceFragment newInstance() {
        return new AddServiceFragment();
    }

    public static AddServiceFragment newInstance(String action) {
        sAction = action;
        return new AddServiceFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new Thread(() -> {
            ServSimplesServerManager.getInstance()
                    .getServiceCategories(PersistHelper.getUser(getContext()),
                            new IServerManagerInterfaceWrapper.ServerCategoriesCallback() {
                                @Override
                                public void onSuccess(List<String> categories) {
                                    mCategories = categories;
                                    mHandler.sendEmptyMessage(GET_CATEGORIES_OK);
                                }

                                @Override
                                public void onFailure(String message) {
                                    mHandler.sendEmptyMessage(GET_CATEGORIES_FAIL);
                                }
                            });
        }).start();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add_service, container, false);
        findViewsById(view);
        if (sAction != null && sAction.equals(ServSimplesConstants.ACTION_EDIT_SERVICE)) {
            setUpEditServiceListeners();
        } else {
            requireActivity().setTitle("Criar serviço");
            setUpRegisterServiceListeners();
        }
        return view;
    }

    private void setUpRegisterServiceListeners() {
        if (ServSimplesAppLogger.ISLOGABLE)
            ServSimplesAppLogger.d(TAG, "setUpRegisterServiceListeners");
        mBtSubmit.setOnClickListener(view -> {
            if (isAnyFieldEmpty()) {
                Toast.makeText(getContext(), "Todos os campos precisam ser preenchidos",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            if (ServSimplesAppLogger.ISLOGABLE)
                ServSimplesAppLogger.d(TAG, "starting register service process");
            User user = PersistHelper.getUser(getContext());
            Service service = getServiceFromView();
            user.addService(service);
            ServSimplesServerManager.getInstance()
                    .registerService(user,
                            new IServerManagerInterfaceWrapper.ServerRequestCallback() {
                                @Override
                                public void onSuccess(User user) {
                                    if (user == null) {
                                        ServSimplesAppLogger.e(TAG, "'user' is null");
                                        return;
                                    }
                                    if (ServSimplesAppLogger.ISLOGABLE)
                                        Toast.makeText(getContext(), "Serviço registrado com sucesso",
                                                Toast.LENGTH_SHORT).show();
                                    PersistHelper.saveUserInfo(user, getContext());
                                    requireActivity().finish();
                                }

                                @Override
                                public void onFailure(String message) {
                                    Toast.makeText(getContext(), "Não foi possível registrar o serviço no momento:"
                                            + message, Toast.LENGTH_SHORT).show();
                                }
                            });
        });
    }

    private Service getServiceFromView() {
        String name = mEtNome.getText().toString();
        Object selectedItem = mSpCategory.getSelectedItem();
        String category = selectedItem == null ? "" : selectedItem.toString();
        String description = mEtDescription.getText().toString();
        String costValue = mEtCostValue.getText().toString();
        String costTime = mEtCostTime.getText().toString();

        Cost cost = new Cost();
        cost.setTime(costTime.isEmpty() ? "não informado" : costTime);
        cost.setValue(costValue.isEmpty() ? "não informado" : costValue);

        Service service = new Service();
        service.setName(name.isEmpty() ? "não informado" : name);
        service.setCategory(category.isEmpty() ? "não informado" : category);
        service.setDescription(description.isEmpty() ? "não informado" : description);
        service.setCost(cost);
        return service;
    }

    private void findViewsById(View view) {
        mEtNome = view.findViewById(R.id.et_service_banner_name);
        mSpCategory = view.findViewById(R.id.sp_service_banner_category);
        mEtDescription = view.findViewById(R.id.et_service_banner_description);
        mEtCostValue = view.findViewById(R.id.et_service_banner_value);
        mEtCostTime = view.findViewById(R.id.et_service_banner_time);
        mBtSubmit = view.findViewById(R.id.bt_service_banner_submit);
    }

    private void setUpEditServiceListeners() {
        mBtSubmit.setOnClickListener(View -> {
            //submitService
        });

    }

    private boolean isAnyFieldEmpty() {
        List<String> fields = new ArrayList<>();
        fields.add(mEtNome.getText().toString());
        //fields.add(mSpCategory.getSelectedItem().toString());
        fields.add(mEtDescription.getText().toString());
        fields.add(mEtCostValue.getText().toString());
        fields.add(mEtCostTime.getText().toString());

        for (String field : fields) {
            if (!isValid(field)) {
                return true;
            }
        }

        return false;
    }

    private boolean isValid(String text) {
        return text != null && !text.trim().isEmpty();
    }

}