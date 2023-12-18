/*
 * Dispositivos Móveis - IFPE 2023
 * Author: Willian Santos
 * Project: ServSimplesApp
 */
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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import ifpe.edu.br.servsimples.R;
import ifpe.edu.br.servsimples.managers.IServerManagerInterfaceWrapper;
import ifpe.edu.br.servsimples.managers.ServerManager;
import ifpe.edu.br.servsimples.model.Availability;
import ifpe.edu.br.servsimples.model.User;
import ifpe.edu.br.servsimples.ui.UIInterfaceWrapper;
import ifpe.edu.br.servsimples.util.DateUtils;
import ifpe.edu.br.servsimples.util.DialogUtils;
import ifpe.edu.br.servsimples.util.PersistHelper;
import ifpe.edu.br.servsimples.util.ServSimplesAppLogger;

public class RegisterAvailabilityFragment extends Fragment {
    private String TAG = RegisterAvailabilityFragment.class.getSimpleName();

    // handler response code
    static final int SERVER_ERROR = -500;
    static final int ADD_AVAILABILITY_OK = 0;

    // Availability bundle keys
    static final String KEY_SELECTED_START_TIME = "key-selected-start-time";
    static final String KEY_SELECTED_DATE = "key-selected-date";
    static String KEY_REGISTER_STATE_CODE = "key-register-state-code";

    // Availability state constants
    static final int INVALID = -1;
    static final int PICK_DATE = 0;
    static final int PICK_START_TIME = 1;
    static final int PICK_END_TIME = 2;
    static final int SUBMIT = 3;

    // Availability data
    private Bundle mAvailabilityBundle;
    private String mSelectedDateFormatted = "";
    private String mSelectedStartTimeFormatted = "";
    private String mSelectedEndTimeFormatted = "";

    // UI components
    private ProgressDialog mProgressDialog;
    private TextView mTvLabel;
    private TextView mTvValue;
    private TextView mTVCardLabel;
    private Button mBtSelect;
    private Button mBtSubmit;

    private UIInterfaceWrapper.FragmentUtil mFragmentUtil;
    private final Handler mHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message message) {
            final int what = message.what;
            switch (what) {
                case ADD_AVAILABILITY_OK:
                    delay();
                    mProgressDialog.dismiss();
                    Toast.makeText(getContext(), "Sucesso", Toast.LENGTH_SHORT).show();
                    requireActivity().finish();
                    break;
                default:
                    delay();
                    mProgressDialog.dismiss();
                    Toast.makeText(getContext(), "Não foi possível registrar disponibilidade",
                            Toast.LENGTH_SHORT).show();
            }
        }
    };

    public RegisterAvailabilityFragment() {
    }

    private RegisterAvailabilityFragment(Bundle args) {
        this.mAvailabilityBundle = args;
    }

    public static RegisterAvailabilityFragment newInstance(Bundle args) {
        return new RegisterAvailabilityFragment(args);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        requireActivity().setTitle("Criar disponibilidade");
        View view = inflater.inflate(R.layout.fragment_register_availability, container, false);
        findViewsById(view);
        setup();
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        if (context instanceof UIInterfaceWrapper.FragmentUtil) {
            mFragmentUtil = (UIInterfaceWrapper.FragmentUtil) context;
        }
        super.onAttach(context);
    }

    private void setup() {
        if (mAvailabilityBundle == null) return;
        int stateCode = mAvailabilityBundle.getInt(KEY_REGISTER_STATE_CODE, INVALID);
        if (stateCode == INVALID) return;

        switch (stateCode) {
            case PICK_DATE -> fillDateCard();
            case PICK_START_TIME -> fillStartTimeCard();
            case PICK_END_TIME -> fillEndTimeCard();
            case SUBMIT -> submit();
        }
    }

    private void fillDateCard() {
        if (ServSimplesAppLogger.ISLOGABLE)
            ServSimplesAppLogger.d(TAG, "fillDateCard()");
        mTVCardLabel.setText("Escolha a data");
        mTvLabel.setText("Data:");
        mTvValue.setText(mSelectedDateFormatted);
        mBtSelect.setText("Escolher");
        mBtSelect.setOnClickListener(v -> showGetDateDialog());
        mBtSubmit.setText("Próximo");
        mBtSubmit.setOnClickListener(v -> {
            if (mSelectedDateFormatted.isBlank()) {
                Toast.makeText(getContext(), "Você precisa selecionar uma data",
                        Toast.LENGTH_LONG).show();
                return;
            }
            Bundle bundle = new Bundle();
            bundle.putInt(KEY_REGISTER_STATE_CODE, PICK_START_TIME);
            bundle.putString(KEY_SELECTED_DATE, mSelectedDateFormatted);
            mFragmentUtil.openFragment(newInstance(bundle), true);
        });
    }

    private void fillStartTimeCard() {
        if (ServSimplesAppLogger.ISLOGABLE)
            ServSimplesAppLogger.d(TAG, "fillStartTimeCard()");
        mTVCardLabel.setText("Escolha o início");
        mTvLabel.setText("Hora início:");
        mTvValue.setText(mSelectedStartTimeFormatted);
        mBtSelect.setText("Escolher");
        mBtSelect.setOnClickListener(v -> getTimeFromDialog((hour, min) -> {
            mSelectedStartTimeFormatted = DateUtils.getTimeFormatString(hour, min);
            mTvValue.setText(mSelectedStartTimeFormatted);
        }));
        mBtSubmit.setText("Próximo");
        mBtSubmit.setOnClickListener(v -> {
            if (mSelectedStartTimeFormatted.isBlank()) {
                Toast.makeText(getContext(), "Você precisa selecionar um horário de início",
                        Toast.LENGTH_LONG).show();
                return;
            }
            Bundle bundle = new Bundle();
            bundle.putInt(KEY_REGISTER_STATE_CODE, PICK_END_TIME);
            bundle.putString(KEY_SELECTED_DATE, mAvailabilityBundle.getString(KEY_SELECTED_DATE));
            bundle.putString(KEY_SELECTED_START_TIME, mSelectedStartTimeFormatted);
            mFragmentUtil.openFragment(newInstance(bundle), true);
        });
    }

    private void fillEndTimeCard() {
        if (ServSimplesAppLogger.ISLOGABLE)
            ServSimplesAppLogger.d(TAG, "fillEndTimeCard()");
        mTVCardLabel.setText("Escolha o término");
        mTvLabel.setText("Hora término:");
        mTvValue.setText(mSelectedEndTimeFormatted);
        mBtSelect.setText("Escolher");
        mBtSelect.setOnClickListener(v -> getTimeFromDialog((hour, min) -> {
                    mSelectedEndTimeFormatted = DateUtils.getTimeFormatString(hour, min);
                    mTvValue.setText(mSelectedEndTimeFormatted);
                }
        ));
        mBtSubmit.setText("Concluir");
        mBtSubmit.setOnClickListener(v -> {
            if (mSelectedEndTimeFormatted.isBlank()) {
                Toast.makeText(getContext(), "Você precisa selecionar um horário de término",
                        Toast.LENGTH_LONG).show();
                return;
            }
            mSelectedDateFormatted = mAvailabilityBundle.getString(KEY_SELECTED_DATE);
            mSelectedStartTimeFormatted = mAvailabilityBundle.getString(KEY_SELECTED_START_TIME);
            DialogUtils.showDialog(getContext(),
                    "Confirmar informações?",
                    "Data:" + mSelectedDateFormatted +
                            "\nHora de início:" + mSelectedStartTimeFormatted +
                            "\nHora de término:" + mSelectedEndTimeFormatted,
                    new DialogUtils.IDialogYesNoCallback() {
                        @Override
                        public void onNo() {

                        }

                        @Override
                        public void onOk() {
                            mProgressDialog = DialogUtils.getProgressDialog(getContext(), "Aguarde");
                            mProgressDialog.show();
                            submit();
                        }
                    });
        });
    }

    private void submit() {
        User currentUser = PersistHelper.getCurrentUser(getContext());
        long startTimestamp = DateUtils.dataAndTimeStringToEpochMillis(mSelectedDateFormatted + mSelectedStartTimeFormatted);
        long endTimestamp = DateUtils.dataAndTimeStringToEpochMillis(mSelectedDateFormatted + mSelectedEndTimeFormatted);

        Availability availability = new Availability();
        availability.setStartTime(startTimestamp);
        availability.setEndTime(endTimestamp);
        availability.setState(Availability.AVAILABLE);
        currentUser.getAgenda().getAvailabilities().add(availability);

        ServerManager.getsInstance().registerAvailability(currentUser,
                new IServerManagerInterfaceWrapper.RegisterAvailabilityCallback() {
                    @Override
                    public void onSuccess(int response) {
                        mHandler.sendEmptyMessage(response);
                    }

                    @Override
                    public void onFailure() {
                        mHandler.sendEmptyMessage(SERVER_ERROR);
                    }
                });
    }

    private void showGetDateDialog() {
        MaterialDatePicker<Long> materialDatePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Selecione a Data")
                .build();
        materialDatePicker.addOnPositiveButtonClickListener(
                selectedDate -> {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(selectedDate);
                    mSelectedDateFormatted = DateUtils.calendarToDateString(calendar);
                    mTvValue.setText(mSelectedDateFormatted);
                });
        materialDatePicker.show(getParentFragmentManager(), "tag");
    }

    private void getTimeFromDialog(DateUtils.TimePickerCallback callback) {
        MaterialTimePicker materialTimePicker = new MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setTitleText("Selecione o horário")
                .build();
        materialTimePicker.addOnPositiveButtonClickListener(v ->
                callback.onGet(materialTimePicker.getHour(),
                        materialTimePicker.getMinute()));
        materialTimePicker.show(getParentFragmentManager(), "tag");
    }

    private void findViewsById(View view) {
        mTVCardLabel = view.findViewById(R.id.addavilability_label);
        mTvLabel = view.findViewById(R.id.tv_addavailability_label);
        mTvValue = view.findViewById(R.id.tv_addavailability_value);
        mBtSelect = view.findViewById(R.id.bt_addavailability_select);
        mBtSubmit = view.findViewById(R.id.bt_addavailability_bubmit);
    }
    private void delay() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}