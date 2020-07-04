package com.vasmash.va_smash.HomeScreen.CommentScreen;

import android.app.Dialog;
import android.content.DialogInterface;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.View;
import android.widget.FrameLayout;

import com.vasmash.va_smash.HomeScreen.CommentScreen.CommentAdapters.Adapter_commentusrlist;
import com.vasmash.va_smash.HomeScreen.CommentScreen.CommentModels.Model_Commentlist;
import com.vasmash.va_smash.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;


public class CommentuserListFragment extends BottomSheetDialogFragment {

    RecyclerView commentlst;
    Adapter_commentusrlist commentimgAdapter;
    ArrayList<Model_Commentlist> tags;

    @Override
    public void setupDialog(final Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        final View contentView = View.inflate(getContext(), R.layout.fragment_commentuser_list, null);
        dialog.setContentView(contentView);
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) ((View) contentView.getParent())
                .getLayoutParams();
        CoordinatorLayout.Behavior behavior = params.getBehavior();
        ((View) contentView.getParent()).setBackgroundColor(ContextCompat.getColor(getContext(), android.R.color.white));
      //  params.setMargins(0, 300, 50, 300);

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialog) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        BottomSheetDialog d = (BottomSheetDialog) dialog;
                        FrameLayout bottomSheet = d.findViewById(R.id.design_bottom_sheet);
                        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    }
                },0);
            }
        });
        //new CommentsFragment.KeyboardUtil(getActivity(), contentView);
      //  getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);


/*
        FrameLayout bottomSheet = (FrameLayout) dialog.getWindow().findViewById(R.id.design_bottom_sheet);
        bottomSheet.setBackgroundResource(R.drawable.bottomsheetcurve);
*/

        commentlst=contentView.findViewById(R.id.commentlst);

        commentlst.setHasFixedSize(true);
        // use a linear layout manager
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        commentlst.setLayoutManager(mLayoutManager);


/*
        commentimgAdapter = new Adapter_commentusrlist(userlistL, getActivity());
        commentlst.setAdapter(commentimgAdapter);

*/

    }


}
