package com.iskandev.examrus.quiz;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.iskandev.examrus.R;

public final class ExitQuizDialogFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder
                .setMessage(R.string.exit_quiz)
                .setPositiveButton(R.string.exit, ((dialog, which) -> requireActivity().finish()))
                .setNegativeButton(R.string.cancel, ((dialog, which) -> dismiss()));

        return builder.create();
    }
}
