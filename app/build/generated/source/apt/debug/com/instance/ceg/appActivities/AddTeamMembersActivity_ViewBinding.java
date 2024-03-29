// Generated code from Butter Knife. Do not modify!
package com.instance.ceg.appActivities;

import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.instance.ceg.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class AddTeamMembersActivity_ViewBinding implements Unbinder {
  private AddTeamMembersActivity target;

  private View view7f090043;

  @UiThread
  public AddTeamMembersActivity_ViewBinding(AddTeamMembersActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public AddTeamMembersActivity_ViewBinding(final AddTeamMembersActivity target, View source) {
    this.target = target;

    View view;
    target.teamMembersLv = Utils.findRequiredViewAsType(source, R.id.team_members_names_list, "field 'teamMembersLv'", ListView.class);
    view = Utils.findRequiredView(source, R.id.add_btn, "field 'addMemberBtn' and method 'onClicked'");
    target.addMemberBtn = Utils.castView(view, R.id.add_btn, "field 'addMemberBtn'", AppCompatButton.class);
    view7f090043 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClicked(p0);
      }
    });
    target.teamMemberNameEd = Utils.findRequiredViewAsType(source, R.id.team_member_name, "field 'teamMemberNameEd'", EditText.class);
    target.designationEd = Utils.findRequiredViewAsType(source, R.id.team_member_position, "field 'designationEd'", EditText.class);
    target.noMembersTv = Utils.findRequiredViewAsType(source, R.id.no_members_view, "field 'noMembersTv'", TextView.class);
    target.toolbar = Utils.findRequiredViewAsType(source, R.id.toolbar, "field 'toolbar'", Toolbar.class);
    target.membersCountDisplayTv = Utils.findRequiredViewAsType(source, R.id.members_count_display, "field 'membersCountDisplayTv'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    AddTeamMembersActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.teamMembersLv = null;
    target.addMemberBtn = null;
    target.teamMemberNameEd = null;
    target.designationEd = null;
    target.noMembersTv = null;
    target.toolbar = null;
    target.membersCountDisplayTv = null;

    view7f090043.setOnClickListener(null);
    view7f090043 = null;
  }
}
