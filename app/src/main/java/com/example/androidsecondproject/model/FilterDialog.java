package com.example.androidsecondproject.model;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.androidsecondproject.R;

public class FilterDialog extends DialogFragment {
    String[] Items;
    boolean[] CheckedItems;

    public FilterDialog(String[] items, boolean[] checkedItems) {
        Items = items;
        CheckedItems = checkedItems;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View alertView = getLayoutInflater().inflate(R.layout.filter_dialog, null);
        builder.setView(alertView);
        builder.setMultiChoiceItems(Items, CheckedItems, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
            }
        });
        // Create the AlertDialog object and return it
        return builder.create();

    }


}
