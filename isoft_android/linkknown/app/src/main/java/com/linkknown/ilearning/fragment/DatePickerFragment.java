package com.linkknown.ilearning.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.widget.DatePicker;

import androidx.fragment.app.DialogFragment;
import androidx.leanback.app.BaseSupportFragment;

import com.linkknown.ilearning.util.ui.ToastUtil;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private String TAG = DatePickerFragment.class.getCanonicalName();
    public SelectDateListener selectDateListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker view, int _year, int _monthOfYear, int _dayOfMonth) {
        String year = String.valueOf(_year);
        String month = String.valueOf(_monthOfYear+1);
        String day = String.valueOf(_dayOfMonth);
        if (_monthOfYear<10){
            month = "0"+month;
        }
        if (_dayOfMonth<10){
            day = "0"+day;
        }
        selectDateListener.selectDate(year +"-"+ month +"-"+ day);
    }

    public interface SelectDateListener{
        public void selectDate(String date);
    };

    public void setSelectDateListener(SelectDateListener selectDateListener) {
        this.selectDateListener = selectDateListener;
    }
}
