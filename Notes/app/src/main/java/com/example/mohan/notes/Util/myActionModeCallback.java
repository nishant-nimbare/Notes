package com.example.mohan.notes.Util;

import android.support.v7.view.ActionMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.mohan.notes.R;

import java.util.ArrayList;

public class myActionModeCallback implements ActionMode.Callback {
    private static final String TAG = "myActionModeCallback";

    boolean isVisible=false ;
    contextMenuListener listener;

    public myActionModeCallback(contextMenuListener listener) {
        this.listener = listener;

    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {

        mode.getMenuInflater().inflate(R.menu.context_action_bar,menu);
        this.isVisible=true;
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {

        switch (item.getItemId()){
            case  R.id.context_delete:
                Log.e(TAG, "onActionItemClicked: delete multiple clicked");
                if (mode!=null){
                    listener.deleteMultipleClicked();
                    mode.finish();
                }
                break;
                
        }
        return true;
    }

    public boolean isVisible() {
        return isVisible;

    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        this.isVisible=false;
        listener.clearToBeDeleted();
    }
}
