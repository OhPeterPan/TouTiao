package com.kotlin.toutiao;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

/**
 * Created by Meiji on 2017/8/8.
 */

public class IntentAction {

    public static void send(@NonNull Context context, @NonNull String shareText) {
        Intent shareIntent = new Intent()
                .setAction(Intent.ACTION_SEND)
                .setType("text/plain")
                .putExtra(Intent.EXTRA_TEXT, shareText);
        context.startActivity(Intent.createChooser(shareIntent, context.getString(R.string.share_to)));
    }
}
