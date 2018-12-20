package com.inspiregeniussquad.handstogether.appDialogs;

import android.app.Dialog;
import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.inspiregeniussquad.handstogether.R;
import com.inspiregeniussquad.handstogether.appData.Admin;

import java.util.Objects;

public class AddAdminDialog extends Dialog {

    private EditText adminNameEd, adminMobileEd;
    private RadioGroup desgntRgrp;
    private RadioButton adminRb, superAdminRb, editorRb;
    private FloatingActionButton saveFab;

    private AdminListener adminListener;

    private int designation = 0;


    public AddAdminDialog(final Context context, final AdminListener adminListener) {
        super(context);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
        getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        this.adminListener = adminListener;

        View addAdminView = LayoutInflater.from(context).inflate(R.layout.dialog_add_admin, null);

        adminNameEd = addAdminView.findViewById(R.id.admin_name);
        adminMobileEd = addAdminView.findViewById(R.id.admin_mobile);
        desgntRgrp = addAdminView.findViewById(R.id.admin_desg_grp);
        adminRb = addAdminView.findViewById(R.id.admin_rb);
        superAdminRb = addAdminView.findViewById(R.id.super_admin_rb);
        editorRb = addAdminView.findViewById(R.id.editor_rb);
        saveFab = addAdminView.findViewById(R.id.save_admin);

        desgntRgrp.clearCheck();
        desgntRgrp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                RadioButton radioButton = radioGroup.findViewById(checkedId);

                if (radioButton.getText().equals(context.getString(R.string.super_admin))) {
                    designation = 1;
                } else if (radioButton.getText().equals(context.getString(R.string.admin))) {
                    designation = 2;
                } else if (radioButton.getText().equals(context.getString(R.string.editor))) {
                    designation = 3;
                } else {
                    designation = 0;
                }
            }
        });

        saveFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String adminName = adminNameEd.getText().toString().trim();
                String adminMobile = adminMobileEd.getText().toString().trim();

                if (TextUtils.isEmpty(adminMobile)) {
                    adminMobileEd.setError(context.getString(R.string.enter_mobile));
                    return;
                }

                if (TextUtils.isEmpty(adminName)) {
                    adminNameEd.setError(context.getString(R.string.enter_name));
                    return;
                }

                if (designation == 0) {
                    Toast.makeText(context, context.getString(R.string.choose_position), Toast.LENGTH_SHORT).show();
                    return;
                }

                adminListener.onOkClicked(new Admin(adminName, adminMobile, String.valueOf(designation)));
                dismiss();
            }
        });

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        setContentView(addAdminView, layoutParams);

        setCancelable(true);
    }

    public interface AdminListener {
        void onOkClicked(Admin admin);
    }

}
