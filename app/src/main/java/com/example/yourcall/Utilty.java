package com.example.yourcall;

import android.content.Context;
import android.widget.Toast;

public class Utilty {
        static void showToast(Context context, String message){
            Toast.makeText(context,message, Toast.LENGTH_SHORT).show();
        }
    }
