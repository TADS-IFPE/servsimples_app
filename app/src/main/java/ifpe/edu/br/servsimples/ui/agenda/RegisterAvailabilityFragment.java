/*
 * Dispositivos Móveis - IFPE 2023
 * Author: Willian Santos
 * Project: ServSimplesApp
 */
package ifpe.edu.br.servsimples.ui.agenda;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import ifpe.edu.br.servsimples.R;

public class RegisterAvailabilityFragment extends Fragment {

    private String TAG = RegisterAvailabilityFragment.class.getSimpleName();
    private TextView mTvDate;
    private TextView mTvStartTime;
    private TextView mTvEndTime;
    private Button mBtSelectDate;
    private Button mBtSelectStartTime;
    private Button mBtSelectEndTime;
    private Button mBtSubmit;

    public RegisterAvailabilityFragment() {

    }

    public static RegisterAvailabilityFragment newInstance() {
        return new RegisterAvailabilityFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register_availability, container, false);
        findViewsById(view);
        setUpListeners();
        showMaterialDatePicker();
        return view;
    }

    private void showMaterialDatePicker() {
        MaterialDatePicker<Long> materialDatePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select Date")
                .build();

        materialDatePicker.addOnPositiveButtonClickListener(
                new MaterialPickerOnPositiveButtonClickListener<Long>() {
                    @Override
                    public void onPositiveButtonClick(Long selectedDate) {
                        // 'selectedDate' é o timestamp da data selecionada em milissegundos
                        // Faça o que precisar com o timestamp
                        // Por exemplo, converta para uma representação de data formatada
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTimeInMillis(selectedDate);
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                        String formattedDate = dateFormat.format(calendar.getTime());

                        // Utilize 'formattedDate' conforme necessário
                    }
                });

        materialDatePicker.show(getParentFragmentManager(), "tag");
    }

    private void showMaterialTimePicker() {
        MaterialTimePicker materialTimePicker = new MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .build();

        materialTimePicker.addOnPositiveButtonClickListener(v -> {
            int hour = materialTimePicker.getHour();
            int minute = materialTimePicker.getMinute();
            // Use 'hour' e 'minute' conforme necessário
        });

        materialTimePicker.show(getParentFragmentManager(), "tag");
    }

    private void showNumberPicker() {
        final NumberPicker numberPicker = new NumberPicker(requireContext());
        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(23);

        // Optionally, you can set a formatter to display leading zeros
        numberPicker.setFormatter(value -> String.format(Locale.getDefault(), "%02d", value));

        new AlertDialog.Builder(requireContext())
                .setTitle("Select Hour")
                .setView(numberPicker)
                .setPositiveButton("OK", (dialog, which) -> {
                    int selectedHour = numberPicker.getValue();
                    // Use 'selectedHour' conforme necessário
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void setUpListeners() {

    }

    private void findViewsById(View view) {
        mTvDate = view.findViewById(R.id.tv_addavailability_date);
        mTvStartTime = view.findViewById(R.id.tv_addavailability_timeStart);
        mTvEndTime = view.findViewById(R.id.tv_addavailability_timeEnd);
        mBtSelectDate = view.findViewById(R.id.bt_addavailability_date);
        mBtSelectStartTime = view.findViewById(R.id.bt_addavailability_timeStart);
        mBtSelectEndTime = view.findViewById(R.id.bt_addavailability_timeEnd);
        mBtSubmit = view.findViewById(R.id.bt_addavailability_bubmit);
    }
}