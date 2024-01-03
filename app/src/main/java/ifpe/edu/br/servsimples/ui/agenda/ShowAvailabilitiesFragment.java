package ifpe.edu.br.servsimples.ui.agenda;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import ifpe.edu.br.servsimples.util.ServSimplesConstants;

public class ShowAvailabilitiesFragment extends Fragment {

    private static final String TAG = ShowAvailabilitiesFragment.class.getSimpleName();
    private static final int GET_USER_NOT_OK = 1;
    private static final int GET_USER_OK = 0;
    private static final int DELETE_AVAILABILITY_OK = 2;
    private static final int DELETE_AVAILABILITY_FAIL = 3;
    private static final int GET_AVAILABILITIES_FOR_PROFESSIONAL_OK = 4;
    private static final int GET_AVAILABILITIES_FOR_PROFESSIONAL_NOT_OK = 5;

    private ProgressDialog mProgressDialog;
    private User mcurrentUser;
    private String profCPF = null;
    private UIInterfaceWrapper.FragmentUtil mFragmentUtil;
    private ListView mAvailabilitiesListView;
    private List<Availability> availabilities = new ArrayList<>();
    private AvailabilityAdapter availabilityAdapter;
    private int mAvailabilityPosition;
    private final Handler mHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message message) {
            final int what = message.what;
            switch (what) {
                case GET_USER_OK:
                    delay();
                    mProgressDialog.dismiss();
                    availabilities = mcurrentUser.getAgenda().getAvailabilities();
                    if (availabilities.isEmpty()) {
                        Toast.makeText(getContext(),
                                "Usuário não possui disponibilidade", Toast.LENGTH_LONG).show();
                        mAvailabilitiesListView.setVisibility(View.GONE);
                    } else {
                        availabilityAdapter =
                                new AvailabilityAdapter(getContext(),
                                        mcurrentUser.getAgenda().getAvailabilities());
                        mAvailabilitiesListView.setAdapter(availabilityAdapter);
                    }
                    break;
                case GET_USER_NOT_OK:
                    delay();
                    mProgressDialog.dismiss();
                    Toast.makeText(getContext(),
                                    "Não foi possível recuperar informações", Toast.LENGTH_SHORT)
                            .show();
                    break;

                case DELETE_AVAILABILITY_OK:
                    delay();
                    availabilities.remove(mAvailabilityPosition);
                    availabilityAdapter.notifyDataSetChanged();
                    Toast.makeText(getContext(), "Item excluído", Toast.LENGTH_SHORT).show();
                    mProgressDialog.dismiss();
                    break;

                case DELETE_AVAILABILITY_FAIL:
                    delay();
                    Toast.makeText(getContext(), "Não foi possível excluir", Toast.LENGTH_SHORT).show();
                    mProgressDialog.dismiss();
                    break;

                case GET_AVAILABILITIES_FOR_PROFESSIONAL_OK:
                    delay();
                    mProgressDialog.dismiss();
                    break;

                case GET_AVAILABILITIES_FOR_PROFESSIONAL_NOT_OK:
                    delay();
                    Toast.makeText(getContext(), "Não foi possível recuperar as disponibilidades do profissional",
                            Toast.LENGTH_SHORT).show();
                    mProgressDialog.dismiss();
                    break;
            }
        }
    };

    public ShowAvailabilitiesFragment() {
    }

    public ShowAvailabilitiesFragment(Bundle bundle) {
        profCPF = bundle.getString(ServSimplesConstants.USER_CPF);
    }

    public static ShowAvailabilitiesFragment newInstance() {
        return new ShowAvailabilitiesFragment();
    }

    public static ShowAvailabilitiesFragment newInstance(Bundle b) {
        return new ShowAvailabilitiesFragment(b);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mProgressDialog = DialogUtils.getProgressDialog(getContext(), "Aguarde");
        mProgressDialog.show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        requireActivity().setTitle("Exibir disponibilidades");
        View view = inflater.inflate(R.layout.fragment_show_availabilities, container, false);
        findViewsById(view);
        setupListeners();
        return view;
    }

    private void setupListeners() {
        if (profCPF == null) {
            registerForContextMenu(mAvailabilitiesListView);
            mAvailabilitiesListView.setOnItemLongClickListener((parent, view, position, id) -> false);
        } else {

        }
    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId() == R.id.lv_showavailability) {
            menu.setHeaderTitle("Menu");
            menu.add("Excluir");
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int position = info.position;
        switch (item.getTitle().toString()) {
            case "Excluir":
                return removeAvailability(position);
            default:
                return super.onContextItemSelected(item);
        }
    }

    private boolean removeAvailability(int position) {
        if (ServSimplesAppLogger.ISLOGABLE) {
            ServSimplesAppLogger.d(TAG, "removeAvailability");
        }
        mProgressDialog.show();
        User currentUser = PersistHelper.getCurrentUser(getContext());
        currentUser.getAgenda().getAvailabilities().add(availabilities.get(position));
        new Thread(() -> ServerManager.getsInstance()
                .deleteAvailability(currentUser,
                        new IServerManagerInterfaceWrapper.RegisterAvailabilityCallback() {
                            @Override
                            public void onSuccess(int code) {
                                mAvailabilityPosition = position;
                                mHandler.sendEmptyMessage(DELETE_AVAILABILITY_OK);
                            }

                            @Override
                            public void onFailure() {
                                mHandler.sendEmptyMessage(DELETE_AVAILABILITY_FAIL);
                            }
                        })).start();
        return false;
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
        if (profCPF == null) {
            retrieveAvailabilityInfo();
        } else {
            retrieveProfessionalAvailabilitiesInfo();
        }
        mProgressDialog.show();
    }

    private void retrieveProfessionalAvailabilitiesInfo() {
        if (ServSimplesAppLogger.ISLOGABLE)
            ServSimplesAppLogger.d(TAG, "retrieveProfessionalAvailabilitiesInfo");
        User professional = new User();
        professional.setCpf(profCPF);

        AppointmentWrapper appointmentWrapper = new AppointmentWrapper();
        appointmentWrapper.setClient(PersistHelper.getCurrentUser(getContext()));
        appointmentWrapper.setProfessional(professional);

        new Thread(() -> ServerManager.getsInstance().getAvailabilitiesForProfessional(
                appointmentWrapper, new IServerManagerInterfaceWrapper.AvailabilityCallback() {
                    @Override
                    public void onSuccess(List<Availability> availabilities) {
                        availabilityAdapter =
                                new AvailabilityAdapter(getContext(), availabilities);
                        mAvailabilitiesListView.setAdapter(availabilityAdapter);
                        mHandler.sendEmptyMessage(GET_AVAILABILITIES_FOR_PROFESSIONAL_OK);
                    }

                    @Override
                    public void onFailure(String message) {
                        mHandler.sendEmptyMessage(GET_AVAILABILITIES_FOR_PROFESSIONAL_NOT_OK);
                    }
                }
        )).start();
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

    private void delay() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}