package com.example.worker.m4l;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextEmail;
    private EditText editTextMove;
    private EditText editTextActive;

    private UtilsPreference pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        findViewById(R.id.buttonSave).setOnClickListener(this);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextActive = findViewById(R.id.editTextActive);
        editTextMove = findViewById(R.id.editTextMove);

        readSetting();
    }

    private void readSetting(){
        pref = new UtilsPreference(this);
        editTextEmail.setText(pref.getSettingEmail());
        editTextMove.setText(pref.getSettingMove());
        editTextActive.setText(pref.getSettingActive());
    }
    private void saveSetting(){
        pref.setSettingEmail(editTextEmail.getText().toString());
        pref.setSettingMove(editTextMove.getText().toString());
        pref.setSettingActive(editTextActive.getText().toString());
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.buttonSave){
            saveSetting();
            setResult(Activity.RESULT_OK);
            finish();
            finish();
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.mybutton:
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://moveforlife.co"));
                startActivity(browserIntent);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // create an action bar button
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settingmenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

}
