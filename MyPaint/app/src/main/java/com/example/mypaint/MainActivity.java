package com.example.mypaint;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;
import yuku.ambilwarna.AmbilWarnaDialog;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public PaintView paintView;
    private int defaultColor;
    private static final int REQUEST_READ_EXTERNAL_STORAGE = 0;
    private static final int REQUEST_WRITE_EXTERNAL_STORAGE = 1;
    private static final int PICK_IMAGE = 3;

    private View selectedColorBtn;

    private boolean isEraserSelected = false;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton drawBtn, erase, undo, redo, delete, save;
        AppCompatImageView open;

        drawBtn = findViewById(R.id.draw_btn);
        erase = findViewById(R.id.erase_btn);
        undo = findViewById(R.id.undo_btn);
        redo = findViewById(R.id.redo_btn);
        delete = findViewById(R.id.delete_btn);
        save = findViewById(R.id.save_btn);
        open = findViewById(R.id.open_btn);
        selectedColorBtn = findViewById(R.id.selectedColor);

        drawBtn.setOnClickListener(this);
        erase.setOnClickListener(this);
        undo.setOnClickListener(this);
        redo.setOnClickListener(this);
        delete.setOnClickListener(this);
        save.setOnClickListener(this);
        open.setOnClickListener(this);
        selectedColorBtn.setOnClickListener(this);

        paintView = findViewById(R.id.paintView);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        SeekBar seekBar = findViewById(R.id.seekBar);
        final TextView textView = findViewById(R.id.current_pen_size);

        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        paintView.initialise(displayMetrics);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int actualProgress = ++progress;
                paintView.setStrokeWidth(actualProgress);
                textView.setText("Толщина кисти: " + actualProgress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

    }

    private void requestStoragePermission() {
        new AlertDialog.Builder(this)
                .setTitle("Разрешение")
                .setMessage("Приложению необходим доступ к файлам для сохранения вашего изображения.\nПредоставить доступ?")
                .setPositiveButton("Ок", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_EXTERNAL_STORAGE);
                    }
                })
                .setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create()
                .show();
    }

    private void requestOpenPermission() {
        new AlertDialog.Builder(this)
                .setTitle("Разрешение")
                .setMessage("Приложению необходим доступ к файлам для открытия вашего изображения.\nПредоставить доступ?")
                .setPositiveButton("Ок", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_READ_EXTERNAL_STORAGE);
                    }
                })
                .setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create()
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode)
        {
            case REQUEST_WRITE_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        saveImageToGallery();
                } else {
                    Toast.makeText(this, "Доступ отклонен", Toast.LENGTH_LONG).show();
                    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                }
                break;
            case REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openImage();
                } else {
                    Toast.makeText(this, "Доступ отклонен", Toast.LENGTH_LONG).show();
                    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                }
                break;
        }
    }

    public boolean isSavePermissionGranted() {
        return (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
    }

    public boolean isOpenPermissionGranted() {
        return (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
    }

    public void openColourPicker() {

        AmbilWarnaDialog ambilWarnaDialog = new AmbilWarnaDialog(this, defaultColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {

            @Override
            public void onCancel(AmbilWarnaDialog dialog) {
            }

            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                defaultColor = color;
                selectedColorBtn.setBackgroundColor(color);
                paintView.setColor(color);
            }
        });

        ambilWarnaDialog.show(); // add
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.erase_btn: {
                isEraserSelected = true;
                paintView.setColor(Color.WHITE);
                break;
            }
            case R.id.undo_btn: {
                paintView.undo();
                break;
            }
            case R.id.delete_btn: {
                paintView.clear();
                break;
            }
            case R.id.redo_btn: {
                paintView.redo();
                break;
            }
            case R.id.selectedColor: {
                openColourPicker();
                break;
            }
            case R.id.save_btn: {
                if (isSavePermissionGranted()) {
                    saveImageToGallery();
                } else {
                    requestStoragePermission();
                }
                break;
            }
            case R.id.open_btn: {
                if (isOpenPermissionGranted()) {
                    openImage();
                } else {
                    requestOpenPermission();
                }
                break;
            }
            case R.id.draw_btn: {
                if (isEraserSelected) {
                    paintView.setPrevBrushColor();
                    isEraserSelected = false;
                }
                break;
            }
        }
    }

    public void saveImageToGallery() {
        final View filenameField = View.inflate(this, R.layout.file_name_field, null);
        new AlertDialog.Builder(this)
                .setView(filenameField)
                .setTitle("Сохранить")
                .setPositiveButton("Сохранить", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String fileName = ((EditText) filenameField.findViewById(R.id.fileNameField)).getText().toString();
                        MediaStore.Images.Media.insertImage(
                                getContentResolver(), paintView.getmBitmap(), fileName, "drawing"
                        );
                        dialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Изображение сохранено", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    private void openImage() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        //Тип получаемых объектов - image:
        photoPickerIntent.setType("image/*");
        //Запускаем переход с ожиданием обратного результата в виде информации об изображении:
        startActivityForResult(photoPickerIntent, PICK_IMAGE);
    }
}