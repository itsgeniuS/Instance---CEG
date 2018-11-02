// Generated code from Butter Knife. Do not modify!
package com.inspiregeniussquad.handstogether.appActivities;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.inspiregeniussquad.handstogether.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class ProfileUpdationActivity_ViewBinding implements Unbinder {
  private ProfileUpdatingActivity target;

  private View view2131296315;

  private View view2131296524;

  @UiThread
  public ProfileUpdationActivity_ViewBinding(ProfileUpdatingActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public ProfileUpdationActivity_ViewBinding(final ProfileUpdatingActivity target, View source) {
    this.target = target;

    View view;
    view = Utils.findRequiredView(source, R.id.choose_image, "field 'chooseImgBtn' and method 'onBtnClicked'");
    target.chooseImgBtn = Utils.castView(view, R.id.choose_image, "field 'chooseImgBtn'", ImageButton.class);
    view2131296315 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onBtnClicked(p0);
      }
    });
    target.userProfileIv = Utils.findRequiredViewAsType(source, R.id.user_profile, "field 'userProfileIv'", ImageView.class);
    target.userNameEd = Utils.findRequiredViewAsType(source, R.id.name, "field 'userNameEd'", TextInputEditText.class);
    target.userEmailEd = Utils.findRequiredViewAsType(source, R.id.email, "field 'userEmailEd'", TextInputEditText.class);
    target.genderRdGrp = Utils.findRequiredViewAsType(source, R.id.radio_grp, "field 'genderRdGrp'", RadioGroup.class);
    target.maleRdBtn = Utils.findRequiredViewAsType(source, R.id.male_rb, "field 'maleRdBtn'", RadioButton.class);
    target.femaleRdBtn = Utils.findRequiredViewAsType(source, R.id.female_rb, "field 'femaleRdBtn'", RadioButton.class);
    view = Utils.findRequiredView(source, R.id.update_profile, "field 'updateProfileBtn' and method 'onBtnClicked'");
    target.updateProfileBtn = Utils.castView(view, R.id.update_profile, "field 'updateProfileBtn'", AppCompatButton.class);
    view2131296524 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onBtnClicked(p0);
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    ProfileUpdatingActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.chooseImgBtn = null;
    target.userProfileIv = null;
    target.userNameEd = null;
    target.userEmailEd = null;
    target.genderRdGrp = null;
    target.maleRdBtn = null;
    target.femaleRdBtn = null;
    target.updateProfileBtn = null;

    view2131296315.setOnClickListener(null);
    view2131296315 = null;
    view2131296524.setOnClickListener(null);
    view2131296524 = null;
  }
}