package movie.n.sankari.movieimdb.dialogs;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;


import java.util.Calendar;


public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private final DatePickerInterface datePickerInterface;
    private boolean isFromDate;

    public DatePickerFragment() {
        datePickerInterface = null;
    }

    @SuppressLint("ValidFragment")
    public DatePickerFragment(DatePickerInterface datePickerInterface, boolean isFromDate) {
        this.datePickerInterface = datePickerInterface;
        this.isFromDate = isFromDate;

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();

        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(getActivity(), this, year, month, day);

        dialog.setCanceledOnTouchOutside(true);
        return dialog;
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        if (datePickerInterface != null) {
            String selectedDate = String.valueOf(year) + "-" + String.valueOf(++month) + "-" + String.valueOf(day);
            datePickerInterface.onDateSet(selectedDate, isFromDate);
        }
    }

    public interface DatePickerInterface {
        void onDateSet(String date, boolean isFromDate);
    }


}