/*
 * Dispositivos Móveis - IFPE 2023
 * Author: Willian Santos
 * Project: ServSimplesApp
 */
package ifpe.edu.br.servsimples.ui.services;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ifpe.edu.br.servsimples.R;
import ifpe.edu.br.servsimples.managers.IServerManagerInterfaceWrapper;
import ifpe.edu.br.servsimples.managers.ServerManager;
import ifpe.edu.br.servsimples.model.Service;
import ifpe.edu.br.servsimples.model.User;
import ifpe.edu.br.servsimples.ui.UIInterfaceWrapper;
import ifpe.edu.br.servsimples.util.PersistHelper;
import ifpe.edu.br.servsimples.util.ServSimplesAppLogger;
import ifpe.edu.br.servsimples.util.ServerResponseCodeParser;

public class SearchServiceFragment extends Fragment {

    private static final String TAG = SearchServiceFragment.class.getSimpleName();

    private UIInterfaceWrapper.FragmentUtil mFragmentUtil;
    private static final int GET_CATEGORIES_OK = 0;
    private static final int GET_CATEGORIES_FAIL = 1;
    private static final int GET_SERVICES_OK = 2;
    private int mLastCategorySelected = 0;
    private CategoriesAdapter mCategoriesAdapter;
    private ServiceListViewAdapter mServiceAdapter;
    private ListView mServiceListView;
    private List<String> mCategories = new ArrayList<>(Collections.singletonList("Default"));
    private List<Service> mServices = new ArrayList<>();
    private Spinner mSpCategories;

    private final Handler mHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message message) {
            final int what = message.what;
            switch (what) {
                case GET_CATEGORIES_OK -> {
                    mCategoriesAdapter = new CategoriesAdapter(getContext(), mCategories);
                    mSpCategories.setAdapter(mCategoriesAdapter);
                    mSpCategories.setSelection(mLastCategorySelected);
                }
                case GET_CATEGORIES_FAIL -> requireActivity().finish();
                case GET_SERVICES_OK -> {
                    mServiceAdapter = new ServiceListViewAdapter(getContext(), mServices);
                    mServiceListView.setAdapter(mServiceAdapter);
                }
            }
        }
    };

    public SearchServiceFragment() {

    }

    public static SearchServiceFragment newInstance() {
        SearchServiceFragment fragment = new SearchServiceFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        retrieveCategoriesInfo();
    }

    private void retrieveServicesInfoForCategory(String category) {
        Service service = new Service();
        service.setCategory(category);
        User currentUser = PersistHelper.getCurrentUser(getContext());
        currentUser.addService(service);

        new Thread(() -> ServerManager.getsInstance()
                .getServicesByCategory(currentUser,
                        new IServerManagerInterfaceWrapper.ServerServicesCallback() {
                            @Override
                            public void onSuccess(List<Service> services) {
                                mServices = services;
                                mHandler.sendEmptyMessage(GET_SERVICES_OK);
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

    private void retrieveCategoriesInfo() {
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
        View view = inflater.inflate(R.layout.fragment_search_service, container, false);
        requireActivity().setTitle("Buscar serviço");
        findViewsById(view);
        setListeners();
        return view;
    }

    private void setListeners() {
        setServiceCategorySelectionListener();
        setServiceListItemListener();
    }

    private void setServiceListItemListener() {
        mServiceListView.setOnItemClickListener((parent, view, position, id) -> {
            if (ServSimplesAppLogger.ISLOGABLE)
                ServSimplesAppLogger.d(TAG, "onItemSelected: pos:" + position
                        + " service name:" + mServices.get(position).getName());
            Fragment serviceDetail = ServiceDetailFragment.newInstance(mServices.get(position));
            mFragmentUtil.openFragment(serviceDetail, true);
        });
    }

    private void setServiceCategorySelectionListener() {
        mSpCategories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                retrieveServicesInfoForCategory(mCategories.get(position));
                mLastCategorySelected = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void findViewsById(View view) {
        mSpCategories = view.findViewById(R.id.sp_findservice);
        mServiceListView = view.findViewById(R.id.lv_showavailability);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        if (context instanceof UIInterfaceWrapper.FragmentUtil) {
            mFragmentUtil = (UIInterfaceWrapper.FragmentUtil) context;
        }
        super.onAttach(context);
    }
}