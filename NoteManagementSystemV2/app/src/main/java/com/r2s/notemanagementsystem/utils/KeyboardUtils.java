package com.r2s.notemanagementsystem.utils;

import static android.content.Context.INPUT_METHOD_SERVICE;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class KeyboardUtils {

    public static void openKeyboard(View view)  {
        InputMethodManager imm =  (InputMethodManager)view.getContext()
                .getSystemService(INPUT_METHOD_SERVICE);

        imm.toggleSoftInputFromWindow(view.getWindowToken(),
                InputMethodManager.SHOW_FORCED, 0);
    }

    public static void hideKeyboard(View view)  {
        InputMethodManager imm = (InputMethodManager)view.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
