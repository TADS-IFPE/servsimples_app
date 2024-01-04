package ifpe.edu.br.servsimples.ui.agenda;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import ifpe.edu.br.servsimples.R;
import ifpe.edu.br.servsimples.managers.IServerManagerInterfaceWrapper;
import ifpe.edu.br.servsimples.managers.ServerManager;
import ifpe.edu.br.servsimples.model.Appointment;
import ifpe.edu.br.servsimples.model.Availability;
import ifpe.edu.br.servsimples.model.Service;
import ifpe.edu.br.servsimples.model.User;
import ifpe.edu.br.servsimples.util.DateUtils;
import ifpe.edu.br.servsimples.util.DialogUtils;
import ifpe.edu.br.servsimples.util.PersistHelper;
import ifpe.edu.br.servsimples.util.ServSimplesAppLogger;

public class AppointmentFragment extends Fragment {

    private static final String TAG = AppointmentFragment.class.getSimpleName();
    private Service mCurrentService;
    private Availability mProfessionalAvailability;
    private String mProfCPF;
    private Long mProfID;

    // Service fields
    private TextView mTvServiceName;
    private TextView mTvServiceCategory;
    private TextView mTvServiceDescription;
    private TextView mTvServiceValue;
    private TextView mTvServiceTime;

    // Appointment fields
    private TextView mTvAppointmentStartTime;
    private TextView mTvAppointmentEndTime;
    private Button mBtAppointmentStartTime;
    private Button mBtAppointmentEndTime;
    private Button mBtAppointmentSubmit;

    private String mSelectedStartTimeFormatted;
    private String mSelectedEndTimeFormatted;

    public AppointmentFragment() {

    }

    private AppointmentFragment(Availability availability, String profCPF, Long profID, Service service) {
        this.mProfessionalAvailability = availability;
        this.mProfCPF = profCPF;
        this.mProfID = profID;
        this.mCurrentService = service;
    }


    public static AppointmentFragment newInstance(Availability availability, String profCPF, Long profID, Service mCurrentService) {
        AppointmentFragment fragment = new AppointmentFragment(availability, profCPF, profID, mCurrentService);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        requireActivity().setTitle("Agendar serviço");
        View view = inflater.inflate(R.layout.fragment_appointment, container, false);
        findViewsById(view);
        fillServiceInfo();
        setupListeners();
        return view;
    }

    private void fillServiceInfo() {
        mTvServiceName.setText(mCurrentService.getName());
        mTvServiceCategory.setText(mCurrentService.getCategory());
        mTvServiceDescription.setText(mCurrentService.getDescription());
        mTvServiceValue.setText(mCurrentService.getCost().getValue());
        mTvServiceTime.setText(mCurrentService.getCost().getTime());
    }

    private void setupListeners() {
        mBtAppointmentStartTime.setOnClickListener(v -> DateUtils.getTimeFromDialog(getParentFragmentManager(), (hour, min) -> {
            mTvAppointmentStartTime.setText(mSelectedStartTimeFormatted);
            mSelectedStartTimeFormatted = DateUtils.getTimeFormatString(hour, min);
            mTvAppointmentStartTime.setText(mSelectedStartTimeFormatted);
        }));

        mBtAppointmentEndTime.setOnClickListener(v -> DateUtils.getTimeFromDialog(getParentFragmentManager(), (hour, min) -> {
            mTvAppointmentEndTime.setText(mSelectedEndTimeFormatted);
            mSelectedEndTimeFormatted = DateUtils.getTimeFormatString(hour, min);
            mTvAppointmentEndTime.setText(mSelectedEndTimeFormatted);
        }));

        mBtAppointmentSubmit.setOnClickListener(view -> performAppointment());
    }

    private void performAppointment() {
        if (ServSimplesAppLogger.ISLOGABLE)
            ServSimplesAppLogger.d(TAG, "performAppointment");
        if (mSelectedStartTimeFormatted == null || mSelectedEndTimeFormatted == null) {
            DialogUtils.showErrorDialogDialog(getContext(), "Você precisa preencher o horário do serviço",
                    new DialogUtils.IDialogOkCallback() {
                        @Override
                        public void onOk() {

                        }
                    });
        }
        User profUser = new User();
        profUser.setCpf(mProfCPF);
        User clientUser = PersistHelper.getCurrentUser(getContext());
        Appointment mAppointment = new Appointment();
        mAppointment.setStartTime(DateUtils.dataAndTimeStringToEpochMillis(DateUtils.timestampToDateString(mProfessionalAvailability.getStartTime()) + mSelectedStartTimeFormatted));
        mAppointment.setEndTime(DateUtils.dataAndTimeStringToEpochMillis(DateUtils.timestampToDateString(mProfessionalAvailability.getStartTime()) + mSelectedEndTimeFormatted));
        mAppointment.setSubscriberId(mProfID);

        Availability clientAvailability = new Availability();
        clientAvailability.setAppointment(mAppointment);
        clientUser.getAgenda().getAvailabilities().add(clientAvailability);

        AppointmentWrapper appointmentWrapper = new AppointmentWrapper();
        appointmentWrapper.setClient(clientUser);
        appointmentWrapper.setProfessional(profUser);

        ServerManager.getsInstance().registerAppointment(appointmentWrapper,
                new IServerManagerInterfaceWrapper.AppointmentCallback() {
                    @Override
                    public void onSuccess() {
                        getParentFragmentManager().popBackStack();
                        Toast.makeText(getContext(), "sucesso", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure() {
                        Toast.makeText(getContext(), "falha", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void findViewsById(View view) {
        // Service fields
        mTvServiceName = view.findViewById(R.id.registerappointment_infoservice_tv_name);
        mTvServiceCategory = view.findViewById(R.id.registerappointment_infoservice_tv_category);
        mTvServiceDescription = view.findViewById(R.id.registerappointment_infoservice_tv_description);
        mTvServiceValue = view.findViewById(R.id.registerappointment_infoservice_tv_value);
        mTvServiceTime = view.findViewById(R.id.registerappointment_infoservice_tv_time);
        // Appointment fields
        mTvAppointmentStartTime = view.findViewById(R.id.tv_registerappointment_startime_value);
        mTvAppointmentEndTime = view.findViewById(R.id.tv_registerappointment_endtime_value);
        mBtAppointmentStartTime = view.findViewById(R.id.bt_registerappointment_startime_select);
        mBtAppointmentEndTime = view.findViewById(R.id.bt_registerappointment_endtime_select);
        mBtAppointmentSubmit = view.findViewById(R.id.bt_registerappointment_create_submit);
    }
}