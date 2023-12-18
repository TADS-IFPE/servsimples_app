package ifpe.edu.br.servsimples.ui.agenda;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import ifpe.edu.br.servsimples.R;
import ifpe.edu.br.servsimples.managers.IServerManagerInterfaceWrapper;
import ifpe.edu.br.servsimples.managers.ServerManager;
import ifpe.edu.br.servsimples.model.Availability;
import ifpe.edu.br.servsimples.model.User;
import ifpe.edu.br.servsimples.ui.UIInterfaceWrapper;
import ifpe.edu.br.servsimples.util.DialogUtils;
import ifpe.edu.br.servsimples.util.PersistHelper;
import ifpe.edu.br.servsimples.util.ServSimplesAppLogger;

public class ShowAvailabilitiesFragment extends Fragment {

    private static final String TAG = ShowAvailabilitiesFragment.class.getSimpleName();
    private static final int GET_USER_NOT_OK = 1;
    private static final int GET_USER_OK = 0;

    private ProgressDialog mProgressDialog;
    private User mcurrentUser;
    private UIInterfaceWrapper.FragmentUtil mFragmentUtil;
    private ListView mAvailabilitiesListView;
    private List<Availability> availabilities = new ArrayList<>();
    private final Handler mHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message message) {
            final int what = message.what;
            switch (what) {
                case GET_USER_OK:
                    mProgressDialog.dismiss();
                    availabilities = mcurrentUser.getAgenda().getAvailabilities();
                    if (availabilities.isEmpty()) {
                        Toast.makeText(getContext(),
                                "Usuário não possui disponibilidade", Toast.LENGTH_LONG).show();
                        mAvailabilitiesListView.setVisibility(View.GONE);
                    } else {
                        AvailabilityAdapter availabilityAdapter =
                                new AvailabilityAdapter(getContext(),
                                        mcurrentUser.getAgenda().getAvailabilities());
                        mAvailabilitiesListView.setAdapter(availabilityAdapter);
                    }
                    break;
                case GET_USER_NOT_OK:
                    Toast.makeText(getContext(),
                                    "Não foi possível recuperar informações", Toast.LENGTH_SHORT)
                            .show();
                    mProgressDialog.dismiss();
                    break;
            }
        }
    };

    public ShowAvailabilitiesFragment() {
    }

    public static ShowAvailabilitiesFragment newInstance() {
        ShowAvailabilitiesFragment fragment = new ShowAvailabilitiesFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mProgressDialog = DialogUtils.getProgressDialog(getContext(), "Aguarde");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        requireActivity().setTitle("Exibir disponibilidades");
        View view = inflater.inflate(R.layout.fragment_show_availabilities, container, false);
        findViewsById(view);
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        if (context instanceof UIInterfaceWrapper.FragmentUtil) {
            mFragmentUtil = (UIInterfaceWrapper.FragmentUtil) context;
        }
        super.onAttach(context);
    }

    private void findViewsById(View view) {
        mAvailabilitiesListView = view.findViewById(R.id.lv_showavailability);
    }

    @Override
    public void onResume() {
        super.onResume();
        retrieveAvailabilityInfo();
        mProgressDialog.show();
    }

    private void retrieveAvailabilityInfo() {
        if (ServSimplesAppLogger.ISLOGABLE)
            ServSimplesAppLogger.d(TAG, "retrieveAvailabilityInfo");
        new Thread(() -> ServerManager.getsInstance()
                .getUser(PersistHelper.getCurrentUser(getContext()),
                        new IServerManagerInterfaceWrapper.ServerRequestCallback() {
                            @Override
                            public void onSuccess(User user) {
                                if (user == null) {
                                    mHandler.sendEmptyMessage(GET_USER_NOT_OK);
                                    return;
                                }
                                mcurrentUser = user;
                                mHandler.sendEmptyMessage(GET_USER_OK);
                            }

                            @Override
                            public void onFailure(String message) {
                                mHandler.sendEmptyMessage(GET_USER_NOT_OK);
                            }
                        })).start();
    }
}