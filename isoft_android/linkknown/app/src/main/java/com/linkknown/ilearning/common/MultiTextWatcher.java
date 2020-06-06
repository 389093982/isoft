package com.linkknown.ilearning.common;

import android.text.Editable;
import android.text.TextWatcher;

import androidx.appcompat.widget.AppCompatEditText;

import java.util.List;

/**
 * 多输入框同时监听类
 * 待进一步设计
 */
public class MultiTextWatcher implements TextWatcher {

    private AppCompatEditText[] editTexts;

    public void register (AppCompatEditText... editTexts) {
        this.editTexts = editTexts;
        for (AppCompatEditText editText : editTexts) {
            editText.addTextChangedListener(this);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
