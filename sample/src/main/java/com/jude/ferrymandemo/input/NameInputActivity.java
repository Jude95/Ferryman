package com.jude.ferrymandemo.input;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.jude.ferryman.R;
import com.jude.ferryman.annotations.Page;
import com.jude.ferryman.annotations.Params;
import com.jude.ferryman.annotations.Results;
import com.jude.ferryman.Ferryman;


/**
 * Created by zhuchenxi on 2017/1/21.
 */
@Page("activity://myName")
public class NameInputActivity extends AppCompatActivity {


    @Params("name")
    @Results("name")
    String myName;

    EditText etName;
    Button btnOk;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name_input);
        Ferryman.inject(this);
        btnOk = (Button) findViewById(R.id.submit);
        etName = (EditText) findViewById(R.id.name);
        etName.setText(myName);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myName = etName.getText().toString();
                finish();
            }
        });
    }

    @Override
    public void finish() {
        Ferryman.save(NameInputActivity.this);
        super.finish();
    }
}
