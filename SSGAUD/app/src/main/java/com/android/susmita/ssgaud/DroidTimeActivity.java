package com.android.susmita.ssgaud;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.io.IOException;
import java.io.InputStream;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class DroidTimeActivity extends AppCompatActivity {
    Intent intentMessage = new Intent();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_droid_time);

        /*String value = "test";



        intentMessage.putExtra("Message", value);
        setResult(2,intentMessage);
        finish();*/
        //readExcel();

    }

    public void readExcel(View view){
        try{
            AssetManager assetManager = getAssets();
            InputStream in = assetManager.open("excel.xls");
            Workbook workbook = Workbook.getWorkbook(in);
            Sheet sheet = workbook.getSheet(0);



            int row = sheet.getRows();
            int col = sheet.getColumns();

            Cell cell = sheet.getCell(19,2);

            String droidString = cell.getContents();

            intentMessage.putExtra("Message", droidString);
            setResult(2,intentMessage);
            finish();


        } catch (IOException e) {
            e.printStackTrace();
        } catch (BiffException e) {
            e.printStackTrace();
        }
    }






}
