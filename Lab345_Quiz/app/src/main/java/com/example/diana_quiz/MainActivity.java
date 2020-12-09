package com.example.diana_quiz;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.diana_quiz.fragment.LoginFragment;
import com.example.diana_quiz.fragment.QuizFragment;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                getSupportFragmentManager().
                        beginTransaction().
                        add(R.id.frame_content,
                                mAuth.getCurrentUser()!=null?new QuizFragment():new LoginFragment()
                        ).
                        commit();
            }
        });
    }
}