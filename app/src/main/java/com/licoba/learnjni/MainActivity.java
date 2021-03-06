package com.licoba.learnjni;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.licoba.learnjni.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    // Used to load the 'learnjni' library on application startup.
//    static {
//        System.loadLibrary("learnjni");
//    }

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Example of a call to a native method
        TextView tv = binding.sampleText;
        MyJni myJni = new MyJni();
        tv.setText(myJni.getMyHelloString());
        tv.setText(String.valueOf(myJni.add(5,8)) );

//        tv.setText(stringFromJNI());
    }

    /**
     * A native method that is implemented by the 'learnjni' native library,
     * which is packaged with this application.
     */
//    public native String stringFromJNI();
}