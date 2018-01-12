package com.jude.ferrymandemo.input;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.jude.ferryman.R;
import com.jude.ferryman.annotations.Page;
import com.jude.ferryman.annotations.Params;
import com.jude.ferryman.annotations.Result;
import com.jude.ferryman.Ferryman;

/**
 * Created by zhuchenxi on 2017/1/21.
 */
@Page("activity://phoneNumber")
public class NumberInputActivity extends AppCompatActivity {

    @Params
    String name;
    @Params
    @Result
    String number;

    TextView tvHint;
    EditText etNumber;
    Button btnOk;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_number_input);
        Ferryman.inject(this);
        btnOk = (Button) findViewById(R.id.submit);
        tvHint = (TextView) findViewById(R.id.hint);
        etNumber = (EditText) findViewById(R.id.number);
        etNumber.setText(number);
        tvHint.setText(String.format("Hello %s ,please input your phone number!",name));
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                number = etNumber.getText().toString();
                Ferryman.save(NumberInputActivity.this);
                finish();
            }
        });
    }
}
