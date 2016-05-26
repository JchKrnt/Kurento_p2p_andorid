package com.sohu.jch.lambdatest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RunnableTest.runLambdaTest();
    }

    private void testLambda(){

        RunnableTest.runLambdaTest();

    }


}
