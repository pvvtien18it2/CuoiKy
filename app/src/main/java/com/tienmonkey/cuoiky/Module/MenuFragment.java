package com.tienmonkey.cuoiky.Module;

import android.content.Context;
import android.content.Intent;

import com.tienmonkey.cuoiky.MainActivity;

import io.paperdb.Paper;

import static androidx.core.content.ContextCompat.startActivity;

public class MenuFragment {

    //Function Logout
    private void logout(Context context){
        Paper.book().destroy();
        context.startActivity(new Intent(context, MainActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK ));
        System.exit(0);
    }

    //Function Profile
//    private void profile()

    //Function Order


}
