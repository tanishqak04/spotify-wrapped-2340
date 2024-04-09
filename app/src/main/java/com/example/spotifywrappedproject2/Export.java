package com.example.spotifywrappedproject2;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.media.MediaScannerConnection;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Export extends Activity {
    Button exportButton;
    View viewToExport;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wrapped);
        exportButton = (Button) findViewById(R.id.exportButton);
        viewToExport = findViewById(android.R.id.content).getRootView();
        exportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Export.this, "Exported as Image", Toast.LENGTH_SHORT).show();

                // export
                saveViewAsImage(viewToExport);
            }
        });
    }

    private void saveViewAsImage(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        try {
            File directory = new File(Environment.getExternalStorageDirectory() + File.separator + "YourFolderName");
            if (!directory.exists()) {
                directory.mkdirs();
            }
            File imageFile = new File(directory, "exported_view.png");
            FileOutputStream fos = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
            Toast.makeText(Export.this, "Export Successful", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(Export.this, "Export Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }


}
