package com.android.exsell.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import java.util.Date;
import java.util.Calendar;

public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {
    private int year;
    private int month;
    private int day;
    private Date date;
    private OnDateSetListener callback;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        date = c.getTime();

        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        callback = (OnDateSetListener) context;
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        if(callback != null) {
            callback.onDate(date,year,month,day);
            return;
        }
        Log.i("calendar","DOB: year "+year+"/"+month+"/"+day);
        this.year = year;
        this.month = month;
        this.day = day;

    }
    public interface OnDateSetListener {
        void onDate(Date date, int year, int month, int day);
    }
}