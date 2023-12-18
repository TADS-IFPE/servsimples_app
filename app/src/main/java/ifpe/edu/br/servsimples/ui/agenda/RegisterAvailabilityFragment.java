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
import ifpe.edu.br.servsimples.model.User;
import ifpe.edu.br.servsimples.ui.UIInterfaceWrapper;
import ifpe.edu.br.servsimples.util.DialogUtils;
import ifpe.edu.br.servsimples.util.PersistHelper;

public class RegisterAvailabilityFragment extends Fragment {
    private String TAG = RegisterAvailabilityFragment.class.getSimpleName();

    // handler response code
    static final int SERVER_ERROR = -500;
    static final int ADD_AVAILABILITY_OK = 555;

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
                    mProgressDialog.dismiss();
                    break;
                default:
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

    public static RegisterAvailabilityFragment newInstance() {
        return new RegisterAvailabilityFragment();
    }

    public static RegisterAvailabilityFragment newInstance(Bundle args) {
        return new RegisterAvailabilityFragment(args);
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
        mTVCardLabel.setText("Escolha o início");
        mTvLabel.setText("Hora início:");
        mTvValue.setText(mSelectedStartTimeFormatted);
        mBtSelect.setText("Escolher");
        mBtSelect.setOnClickListener(v -> getTimeFromDialog((hour, min) -> {
            mSelectedStartTimeFormatted = getTimeFormatString(hour, min);
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
        mTVCardLabel.setText("Escolha o término");
        mTvLabel.setText("Hora término:");
        mTvValue.setText(mSelectedEndTimeFormatted);
        mBtSelect.setText("Escolher");
        mBtSelect.setOnClickListener(v -> getTimeFromDialog((hour, min) -> {
                    mSelectedEndTimeFormatted = getTimeFormatString(hour, min);
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
        // TODO add info da disponibilidade
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        requireActivity().setTitle("Criar disponibilidade");
        View view = inflater.inflate(R.layout.fragment_register_availability, container, false);
        findViewsById(view);
        setup();
        return view;
    }

    private void showGetDateDialog() {
        MaterialDatePicker<Long> materialDatePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Selecione a Data")
                .build();
        materialDatePicker.addOnPositiveButtonClickListener(
                selectedDate -> {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(selectedDate);
                    mSelectedDateFormatted = getDateString(calendar);
                    mTvValue.setText(mSelectedDateFormatted);
                });
        materialDatePicker.show(getParentFragmentManager(), "tag");
    }

    @NonNull
    private static String getDateString(Calendar calendar) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return dateFormat.format(calendar.getTime());
    }

    private void getTimeFromDialog(TimePickerCallback callback) {
        MaterialTimePicker materialTimePicker = new MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setTitleText("Selecione o horário")
                .build();
        materialTimePicker.addOnPositiveButtonClickListener(v ->
                callback.onGet(materialTimePicker.getHour(),
                        materialTimePicker.getMinute()));
        materialTimePicker.show(getParentFragmentManager(), "tag");
    }

    private String getTimeFormatString(int hour, int minute) {
        return String.format(Locale.getDefault(), "%02d:%02d", hour, minute);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        if (context instanceof UIInterfaceWrapper.FragmentUtil) {
            mFragmentUtil = (UIInterfaceWrapper.FragmentUtil) context;
        }
        super.onAttach(context);
    }

    private Calendar getDate(int hour, int min) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, min);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar;
    }

    public static long getTimestamp(int hour, int minute) {
        Calendar calendar = Calendar.getInstance();

//        calendar.set(Calendar.MONTH, );
//        calendar.set(Calendar.YEAR, );
//        calendar.set(Calendar.DAY_OF_MONTH, );

        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    public static Calendar getCalendar(int hour, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar;
    }

    private void performAvailabilityRegistration() {
//        if (mSelectedDateMilli < 0 || mSelectedHourStartMilli < 0 || mSelectedEndHourMilli == -1) {
//            DialogUtils.showErrorDialogDialog(getContext(),
//                    "Você precisa selecionar todos os campos", () -> {});
//        }

//        mDateCalendar.set(Calendar.HOUR_OF_DAY, mStartDate.get(Calendar.HOUR_OF_DAY));
//        mDateCalendar.set(Calendar.MINUTE, mStartDate.get(Calendar.MINUTE));
//        mDateCalendar.set(Calendar.SECOND, mStartDate.get(Calendar.SECOND));
//        mDateCalendar.set(Calendar.MILLISECOND, mStartDate.get(Calendar.MILLISECOND));
//
//        long startTimestamp = mDateCalendar.getTimeInMillis();
//        long end = mSelectedDateMilli + mSelectedEndHourMilli;

//        Calendar calendar = Calendar.getInstance();
//        calendar.set(Calendar.DAY_OF_MONTH, 20);
//        calendar.set(Calendar.MONTH, 7);
//        calendar.set(Calendar.YEAR, 2023);
//        calendar.set(Calendar.HOUR_OF_DAY, 16);
//        calendar.set(Calendar.MINUTE, 20);
//        calendar.set(Calendar.SECOND, 0);
//        calendar.set(Calendar.MILLISECOND, 0);
//
//
//        ServSimplesAppLogger.e(TAG, "start:" + calendar.getTimeInMillis());
        Fragment f = RegisterAvailabilityFragment.newInstance();
        mFragmentUtil.openFragment(f, true);

    }

    private void findViewsById(View view) {
        mTVCardLabel = view.findViewById(R.id.addavilability_label);
        mTvLabel = view.findViewById(R.id.tv_addavailability_label);
        mTvValue = view.findViewById(R.id.tv_addavailability_value);
        mBtSelect = view.findViewById(R.id.bt_addavailability_select);
        mBtSubmit = view.findViewById(R.id.bt_addavailability_bubmit);
    }

    interface TimePickerCallback {
        void onGet(int hour, int min);
    }
}