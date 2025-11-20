package com.example.dormmamu.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.dormmamu.R;

import java.util.ArrayList;
import java.util.List;

public class FilterDialog {

    private static int lastSelectedBathroomId = -1;
    private static int lastSelectedSortId = -1;
    private static String lastMin = "";
    private static String lastMax = "";

    public interface OnFilterApplyListener {
        void onFilterApplied(int minPrice, int maxPrice, String bathroomType, List<String> selectedCities, String sortBy);
    }

    public static void show(Context context, OnFilterApplyListener listener) {

        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_filters, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(dialogView);
        builder.setTitle("Filter Dorms");

        EditText minPrice = dialogView.findViewById(R.id.minPrice);
        EditText maxPrice = dialogView.findViewById(R.id.maxPrice);

        RadioGroup bathroomGroup = dialogView.findViewById(R.id.bathroomGroup);
        RadioGroup sortGroup = dialogView.findViewById(R.id.sortGroup);


        minPrice.setText(lastMin);
        maxPrice.setText(lastMax);

        if (lastSelectedBathroomId != -1) {
            bathroomGroup.check(lastSelectedBathroomId);
        }

        if (lastSelectedSortId != -1) {
            sortGroup.check(lastSelectedSortId);
        }

        builder.setPositiveButton("Apply", (dialog, which) -> {

            int min = 0;
            int max = Integer.MAX_VALUE;

            String minStr = minPrice.getText().toString().trim();
            String maxStr = maxPrice.getText().toString().trim();

            try {
                if (!minStr.isEmpty()) {
                    min = Integer.parseInt(minStr);
                }
                if (!maxStr.isEmpty()) {
                    max = Integer.parseInt(maxStr);
                }
            } catch (NumberFormatException ignored) {}


            lastMin = minStr;
            lastMax = maxStr;

            String bathroomType = "";
            int bathId = bathroomGroup.getCheckedRadioButtonId();
            lastSelectedBathroomId = bathId; // save selected ID
            if (bathId != -1) {
                RadioButton rb = dialogView.findViewById(bathId);
                bathroomType = rb.getText().toString();
            }


            String sortBy = "";
            int sortId = sortGroup.getCheckedRadioButtonId();
            lastSelectedSortId = sortId;
            if (sortId != -1) {
                RadioButton rb = dialogView.findViewById(sortId);
                sortBy = rb.getText().toString();
            }

            List<String> selectedCities = new ArrayList<>();

            listener.onFilterApplied(min, max, bathroomType, selectedCities, sortBy);
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        Dialog dialog = builder.create();
        dialog.show();
    }
}
