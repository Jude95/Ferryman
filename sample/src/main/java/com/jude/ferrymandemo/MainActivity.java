package com.jude.ferrymandemo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jude.ferryman.Ferryman;
import com.jude.ferryman.OnDataResultListener;
import com.jude.ferryman.R;
import com.jude.ferryman.annotations.Page;
import com.jude.ferrymandemo.input.NameInputActivityResult;
import com.jude.ferrymandemo.input.NumberInputActivityResult;


@Page
public class MainActivity extends AppCompatActivity {

    TextView tvName;
    TextView tvNumber;
    Button btnInputName;
    Button btnInputNumber;
    Button btnAnalysis;

    String name;
    String number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvName = (TextView) findViewById(R.id.tv_name);
        tvNumber = (TextView) findViewById(R.id.tv_number);
        btnInputName = (Button) findViewById(R.id.btn_name);
        btnInputNumber = (Button) findViewById(R.id.btn_number);
        btnAnalysis = (Button) findViewById(R.id.btn_analysis);
        btnInputName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Ferryman.from(MainActivity.this)
                        .gotoNameInputActivity(tvName.getText().toString())
                        .onResultWithData(new OnDataResultListener<NameInputActivityResult>() {
                            @Override
                            public void fullResult(@NonNull NameInputActivityResult data) {
                                name = data.getName();
                                tvName.setText(data.getName());
                            }

                            @Override
                            public void emptyResult() {

                            }

                        });
            }
        });

        btnInputNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Ferryman.from(MainActivity.this)
                        .gotoNumberInputActivity(name,number)
                        .onResultWithData(new OnDataResultListener<NumberInputActivityResult>() {
                            @Override
                            public void fullResult(@NonNull NumberInputActivityResult data) {
                                number = data.getNumber();
                                tvNumber.setText(data.getNumber());
                            }

                            @Override
                            public void emptyResult() {

                            }

                        });
            }
        });

        btnAnalysis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Ferryman.from(MainActivity.this).gotoAnalysisActivity(new Person(name,number));
            }
        });
    }

}