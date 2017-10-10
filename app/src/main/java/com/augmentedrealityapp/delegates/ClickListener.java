package com.augmentedrealityapp.delegates;

import android.view.View;

/**
 * Created by kavasthi on 8/18/2017.
 */

public interface ClickListener {
    public void onClick(View view, int position);
    public void onLongClick(View view,int position);
}
