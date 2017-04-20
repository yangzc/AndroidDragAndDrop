package com.hyena.dd;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.hyean.dd.DragAndDropLayout;
import com.hyean.dd.IDroppable;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_main);

        final DragAndDropLayout layout = (DragAndDropLayout) findViewById(R.id.dd);
        final View drop = findViewById(R.id.drop);
        drop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.flyBack((IDroppable) drop);
            }
        });
    }
}
