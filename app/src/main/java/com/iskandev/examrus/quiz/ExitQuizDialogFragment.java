package com.iskandev.examrus.quiz;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.iskandev.examrus.R;

public final class ExitQuizDialogFragment extends DialogFragment {

    private AlertDialog dialog;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder
                .setMessage(R.string.exit_exam)
                .setPositiveButton(R.string.exit, ((dialog, which) -> requireActivity().finish()))
                .setNegativeButton(R.string.cancel, ((dialog, which) -> dismiss()));

        dialog = builder.create();

        return dialog;
    }

    boolean isDialogShowing() {
        if (dialog != null)
            return dialog.isShowing();
        return false;
    }
}
