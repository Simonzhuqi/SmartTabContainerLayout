package com.doophe.smarttabcontainerlayout;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.doophe.smarttabcontainerlib.SmartTabContainerLayout;

public class MainActivity extends AppCompatActivity {

    SmartTabContainerLayout smartTabContainerLayout;
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        smartTabContainerLayout = findViewById(R.id.tab_container_layout);

        smartTabContainerLayout.addTab("默认房间");
        smartTabContainerLayout.addTab("客厅");
        smartTabContainerLayout.addTab("主卧");
        smartTabContainerLayout.addTab("次卧");
        smartTabContainerLayout.addTab("厨房");
        smartTabContainerLayout.addTab("卫生间");
        smartTabContainerLayout.addTab("餐厅");
        smartTabContainerLayout.addTab("主卧卫生间");

        String[] tabs = new String[]{
                "套间A","套间B"
        };
        smartTabContainerLayout.addTabs(tabs);

        smartTabContainerLayout.addCallback(new SmartTabContainerLayout.Callback() {
            @Override
            public void onTabClickListener(View view, String value) {
                Toast.makeText(MainActivity.this,value,Toast.LENGTH_SHORT).show();
                view.setActivated(!view.isActivated());
            }

            @Override
            public void onTabLongClickListener(View view, String value) {

            }
        });

        editText = findViewById(R.id.edit_text);
        findViewById(R.id.btn_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = editText.getText().toString();
                if (content != null && !content.trim().isEmpty()) {
                    smartTabContainerLayout.addTab(content);
                    editText.setText("");
                }
            }
        });

    }
}
