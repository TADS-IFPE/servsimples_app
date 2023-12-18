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
import java.util.Collections;
import java.util.List;

import ifpe.edu.br.servsimples.R;
import ifpe.edu.br.servsimples.managers.IServerManagerInterfaceWrapper;
import ifpe.edu.br.servsimples.managers.ServerManager;
import ifpe.edu.br.servsimples.model.Cost;
import ifpe.edu.br.servsimples.model.Service;
import ifpe.edu.br.servsimples.model.User;
import ifpe.edu.br.servsimples.util.PersistHelper;
import ifpe.edu.br.servsimples.util.ServSimplesAppLogger;
import ifpe.edu.br.servsimples.util.ServerResponseCodeParser;


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
    private static Service mCurrentService;
    private List<String> mCategories = new ArrayList<>(Collections.singletonList("Default"));
    private CategoriesAdapter mCategoriesAdapter;

    private final Handler mHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message message) {
            final int what = message.what;
            switch (what) {
                case GET_CATEGORIES_OK:
                    mCategoriesAdapter = new CategoriesAdapter(getContext(), mCategories);
                    mSpCategory.setAdapter(mCategoriesAdapter);
                    if (mCurrentService != null) {
                        int categoryIndex = mCategories.indexOf(mCurrentService.getCategory());
                        mSpCategory.setSelection(categoryIndex);
                        mEtNome.setText(mCurrentService.getName());
                        mEtDescription.setText(mCurrentService.getDescription());
                        mEtCostValue.setText(mCurrentService.getCost().getValue());
                        mEtCostTime.setText(mCurrentService.getCost().getTime());
                        mBtSubmit.setText("Editar");
                    }
                    break;
                case GET_CATEGORIES_FAIL:
                    requireActivity().finish();
                    break;
            }
        }
    };

    public AddServiceFragment() {
    }

    public static AddServiceFragment newInstance() {
        mCurrentService = null;
        return new AddServiceFragment();
    }

    public static AddServiceFragment newInstance(Service service) {
        mCurrentService = service;
        return new AddServiceFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new Thread(() -> ServerManager.getsInstance()
                .getServiceCategories(PersistHelper.getCurrentUser(getContext()),
                        new IServerManagerInterfaceWrapper.ServerCategoriesCallback() {
                            @Override
                            public void onSuccess(List<String> categories) {
                                mCategories = categories;
                                mHandler.sendEmptyMessage(GET_CATEGORIES_OK);
                            }

                            @Override
                            public void onFailure(String message) {
                                Toast.makeText(getContext(),
                                        ServerResponseCodeParser.parseToString(message),
                                        Toast.LENGTH_SHORT).show();
                                mHandler.sendEmptyMessage(GET_CATEGORIES_FAIL);
                            }
                        })).start();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_service, container, false);
        findViewsById(view);
        if (mCurrentService != null) {
            requireActivity().setTitle("Editar Serviço");
            setUpEditServiceListeners();
        } else {
            requireActivity().setTitle("Criar Serviço");
            setUpRegisterServiceListeners();
        }
        return view;
    }

    private void setUpEditServiceListeners() {
        if (ServSimplesAppLogger.ISLOGABLE)
            ServSimplesAppLogger.d(TAG, "setUpEditServiceListeners");
        mBtSubmit.setOnClickListener(View -> {
            if (isAnyFieldEmpty()) {
                Toast.makeText(getContext(), "Todos os campos precisam ser preenchidos",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            if (ServSimplesAppLogger.ISLOGABLE)
                ServSimplesAppLogger.d(TAG, "starting edit service process");
            User user = PersistHelper.getCurrentUser(getContext());
            Service service = getServiceFromView();
            user.addService(service);
            ServerManager.getsInstance()
                    .updateService(user,
                            new IServerManagerInterfaceWrapper.ServerRequestCallback() {
                                @Override
                                public void onSuccess(User user) {
                                    if (user == null) {
                                        ServSimplesAppLogger.e(TAG, "'user' is null");
                                        return;
                                    }
                                    if (ServSimplesAppLogger.ISLOGABLE)
                                        Toast.makeText(getContext(), "Serviço atualizado com sucesso",
                                                Toast.LENGTH_SHORT).show();
                                    PersistHelper.saveUserInfo(user, getContext());
                                    requireActivity().finish();
                                }

                                @Override
                                public void onFailure(String message) {
                                    Toast.makeText(getContext(),
                                            ServerResponseCodeParser.parseToString(message),
                                            Toast.LENGTH_LONG).show();
                                }
                            });
        });
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
            User user = PersistHelper.getCurrentUser(getContext());
            Service service = getServiceFromView();
            user.addService(service);
            ServerManager.getsInstance()
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
                                    Toast.makeText(getContext(),
                                            ServerResponseCodeParser.parseToString(message),
                                            Toast.LENGTH_LONG).show();
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
        service.setId(mCurrentService != null ? mCurrentService.getId() : null);
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

    private boolean isAnyFieldEmpty() {
        List<String> fields = new ArrayList<>();
        fields.add(mEtNome.getText().toString());
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