package com.android.onmarcy.campaign;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.onmarcy.MainActivity;
import com.android.onmarcy.PreviewActivity;
import com.android.onmarcy.R;
import com.google.android.material.textfield.TextInputEditText;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import model.Campaign;
import model.City;
import model.SocialMedia;

public class UpdateActivity extends AppCompatActivity {
    public static final String EXTRA_CAMPAIGN = "campaign";
    private TextInputEditText edtTitle, edtNotes, edtMax, edtMin, edtDuration, edtPrice, edtTime, edtDate, edtCaption, edtBio;
    private ImageButton btnPost, btnStory;
    private Button btnPreviewPost, btnPreviewStory;
    private AutoCompleteTextView autoCompleteTextView;
    private RadioButton rbMale, rbFemale, rbAll;
    private SearchableSpinner spCity;
    private String selectedCategory = "";
    private int categoryCode = 0;
    private ArrayList<City> cities = new ArrayList<>();
    private ArrayAdapter<City> adapter;
    private int cityCode = 0;
    private Campaign campaign;
    private String username_ig;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private String base64String = "";
    private int permission = 0;
    private String picturePath = "";
    private ArrayList<String> picturePathList;
    private ArrayList<String> picturePathListPost = new ArrayList<>();
    private ArrayList<String> picturePathListStory = new ArrayList<>();
    private ArrayList<String> base64StringPost = new ArrayList<>();
    private ArrayList<String> base64StringStory = new ArrayList<>();
    private boolean isPost = true;
    private int limit = 0;
    private int limitPost = 10;
    private int limitStory = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        if (getIntent().hasExtra(EXTRA_CAMPAIGN))
            campaign = getIntent().getParcelableExtra(EXTRA_CAMPAIGN);
        bindView();
        bindData();

        registerForContextMenu(autoCompleteTextView);
        autoCompleteTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                autoCompleteTextView.showContextMenu();
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.update_campaign);

        adapter = new ArrayAdapter<>(UpdateActivity.this, android.R.layout.simple_list_item_1, cities);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCity.setAdapter(adapter);
        loadCity();

        edtTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar currentTime = Calendar.getInstance();
                SimpleDateFormat mFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
                TimePickerDialog timePickerDialog = new TimePickerDialog(UpdateActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                        currentTime.set(Calendar.HOUR_OF_DAY, hour);
                        currentTime.set(Calendar.MINUTE, minute);
                        edtTime.setText(mFormat.format(currentTime.getTime()));
                    }
                }, currentTime.get(Calendar.HOUR_OF_DAY), currentTime.get(Calendar.MINUTE), true);
                timePickerDialog.setTitle(getString(R.string.select_time));
                timePickerDialog.show();
            }
        });

        edtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar currentDate = Calendar.getInstance();
                SimpleDateFormat mFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                DatePickerDialog datePickerDialog = new DatePickerDialog(UpdateActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int date) {
                        currentDate.set(Calendar.DAY_OF_MONTH, date);
                        currentDate.set(Calendar.MONTH, month);
                        currentDate.set(Calendar.YEAR, year);
                        edtDate.setText(mFormat.format(currentDate.getTime()));
                    }
                }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });

        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isPost = true;
                limit = 10;
                picturePathListPost.clear();
                verifyStoragePermissions(UpdateActivity.this);
            }
        });

        btnStory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isPost = false;
                limit = 3;
                picturePathListStory.clear();
                verifyStoragePermissions(UpdateActivity.this);
            }
        });

        setButtonPreview();

        btnPreviewPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UpdateActivity.this, PreviewActivity.class);
                intent.putStringArrayListExtra(PreviewActivity.TAG, picturePathListPost);
                startActivity(intent);
            }
        });

        btnPreviewStory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UpdateActivity.this, PreviewActivity.class);
                intent.putStringArrayListExtra(PreviewActivity.TAG, picturePathListStory);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.context_menu_category, menu);
        menu.setHeaderTitle("Choose Category");

        MenuItem.OnMenuItemClickListener listener = new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                onContextItemSelected(menuItem);
                return true;
            }
        };

        for (int i = 0; i < menu.size(); i++) {
            menu.getItem(i).setOnMenuItemClickListener(listener);
        }
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        selectedCategory = item.toString();
        autoCompleteTextView.setText(selectedCategory);
        setCategoryCode(selectedCategory);
        return super.onContextItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.update_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.item_update:
                boolean isValid = true;

                if (TextUtils.isEmpty(edtTitle.getText().toString())) {
                    edtTitle.setError(getResources().getString(R.string.please_fill_out_this_field));
                    isValid = false;
                }
                if (TextUtils.isEmpty(edtMax.getText().toString())) {
                    edtMax.setError(getResources().getString(R.string.please_fill_out_this_field));
                    isValid = false;
                }
                if (TextUtils.isEmpty(edtMin.getText().toString())) {
                    edtMin.setError(getResources().getString(R.string.please_fill_out_this_field));
                    isValid = false;
                }
                if (TextUtils.isEmpty(edtTime.getText().toString())) {
                    edtTime.setError(getResources().getString(R.string.please_fill_out_this_field));
                    isValid = false;
                }
                if (TextUtils.isEmpty(edtDuration.getText().toString())) {
                    edtDuration.setError(getResources().getString(R.string.please_fill_out_this_field));
                    isValid = false;
                }
                if (TextUtils.isEmpty(edtPrice.getText().toString())) {
                    edtPrice.setError(getResources().getString(R.string.please_fill_out_this_field));
                    isValid = false;
                }
                if (TextUtils.isEmpty(edtDate.getText().toString())) {
                    edtDate.setError(getResources().getString(R.string.please_fill_out_this_field));
                    isValid = false;
                }

                if (spCity.getSelectedItem() != null) {
                    for (int i = 0; i < cities.size(); i++) {
                        if (cities.get(i).getName().equals(spCity.getSelectedItem().toString())) {
                            cityCode = cities.get(i).getCode();
                        }
                    }
                }

                if (cityCode == 0) {
                    isValid = false;
                    Toast.makeText(this, getString(R.string.please_choose_city), Toast.LENGTH_SHORT).show();
                }

                if (isValid) {
                    String title = edtTitle.getText().toString();
                    String notes = edtNotes.getText().toString();
                    int min = Integer.parseInt(edtMin.getText().toString());
                    int max = Integer.parseInt(edtMax.getText().toString());
                    int gender = 0;
                    if (rbMale.isChecked()) gender = 1;
                    if (rbFemale.isChecked()) gender = 2;
                    if (rbAll.isChecked()) gender = 3;
                    int duration = Integer.parseInt(edtDuration.getText().toString());
                    String[] date = edtDate.getText().toString().split("/");
                    String formattedDate = date[2] + "-" + date[1] + "-" + date[0];
                    String time = edtTime.getText().toString();
                    String caption = edtCaption.getText().toString();
                    String bio = edtBio.getText().toString();

                    if (picturePathListPost.size() < limitPost) {
                        int total = limitPost - picturePathListPost.size();
                        for (int i = 0; i < total; i++) {
                            picturePathListPost.add("");
                        }
                    }

                    if (picturePathListStory.size() < limitStory) {
                        int total = limitStory - picturePathListStory.size();
                        for (int i = 0; i < total; i++) {
                            picturePathListStory.add("");
                        }
                    }

                    try {
                        convertToBase64();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Campaign.update(this, campaign.getCodeString(), username_ig, categoryCode, cityCode, title, notes, min, max, gender, formattedDate, time, duration, false, new Campaign.Callback() {
                        @Override
                        public void success() {
                            Toast.makeText(UpdateActivity.this, getString(R.string.success), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void error() {
                            Toast.makeText(UpdateActivity.this, getString(R.string.fail), Toast.LENGTH_SHORT).show();
                        }
                    });

                    Campaign.updateContent(this, campaign.getCodeString(), username_ig, caption, getArrayOfPath(base64StringPost), getArrayOfPath(base64StringStory), bio, false, new Campaign.Callback() {
                        @Override
                        public void success() {
                            Toast.makeText(UpdateActivity.this, getString(R.string.success), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void error() {
                            Toast.makeText(UpdateActivity.this, getString(R.string.fail), Toast.LENGTH_SHORT).show();
                        }
                    });

                    Intent intent = new Intent(UpdateActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Log.d("RUNNN", "attempt failed");
                }
        }
        return super.onOptionsItemSelected(item);
    }

    private String[] getArrayOfPath(ArrayList<String> base64String){
        String[] arr = new String[base64String.size()];
        for (int i = 0; i < base64String.size(); i++) {
            arr[i] = base64String.get(i);
        }

        return arr;
    }

    private void convertToBase64() throws IOException {
        for (int i = 0; i < picturePathListPost.size(); i++) {
            if (picturePathListPost.get(i).equals("")) {
                base64StringPost.add("");
            } else {
                Uri imageUri = Uri.fromFile(new File(picturePathListPost.get(i)));
                Bitmap thumbnail = MediaStore.Images.Media.getBitmap(UpdateActivity.this.getContentResolver(), imageUri);
                thumbnail = getResizedBitmap(thumbnail, 400);
                base64StringPost.add(BitMapToString(thumbnail));
            }
        }

        for (int i = 0; i < picturePathListStory.size(); i++) {
            if (picturePathListStory.get(i).equals("")) {
                base64StringStory.add("");
            } else {
                Uri imageUri = Uri.fromFile(new File(picturePathListStory.get(i)));
                Bitmap thumbnail = MediaStore.Images.Media.getBitmap(UpdateActivity.this.getContentResolver(), imageUri);
                thumbnail = getResizedBitmap(thumbnail, 400);
                base64StringStory.add(BitMapToString(thumbnail));
            }
        }
    }

    private void setButtonPreview() {
        if (picturePathListPost.size() == 0) {
            btnPreviewPost.setVisibility(View.GONE);
        } else {
            btnPreviewPost.setVisibility(View.VISIBLE);
        }

        if (picturePathListStory.size() == 0) {
            btnPreviewStory.setVisibility(View.GONE);
        } else {
            btnPreviewStory.setVisibility(View.VISIBLE);
        }
    }

    public void verifyStoragePermissions(Activity activity) {
        permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
        } else {
            selectImage();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            selectImage();
        }
    }

    public void selectImage() {
        final CharSequence[] options = {"Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(UpdateActivity.this);
        builder.setTitle("Insert Picture");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Choose from Gallery")) {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                    intent.setAction(Intent.ACTION_PICK);
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == UpdateActivity.this.RESULT_OK) {
            if (requestCode == 1) {
                String[] filePath = {MediaStore.Images.Media.DATA};
                picturePathList = new ArrayList<>();

                if (data.getData() != null) {
                    Uri selectedImage = data.getData();
                    Cursor c = UpdateActivity.this.getContentResolver().query(selectedImage, filePath, null, null, null);
                    c.moveToFirst();
                    int columnIndex = c.getColumnIndex(filePath[0]);
                    picturePath = c.getString(columnIndex);
                    c.close();
                } else {
                    if (data.getClipData() != null) {
                        ClipData mClipData = data.getClipData();
                        ArrayList<Uri> mArrayUri = new ArrayList<>();

                        for (int i = 0; i < mClipData.getItemCount(); i++) {
                            if (i < limit) {
                                ClipData.Item item = mClipData.getItemAt(i);
                                Uri selectedImage = item.getUri();
                                mArrayUri.add(selectedImage);
                                Cursor c = UpdateActivity.this.getContentResolver().query(selectedImage, filePath, null, null, null);
                                c.moveToFirst();
                                int columnIndex = c.getColumnIndex(filePath[0]);
                                picturePath = c.getString(columnIndex);
                                picturePathList.add(picturePath);
                                c.close();
                            }
                        }

                        if (mClipData.getItemCount() > limit) {
                            Toast.makeText(UpdateActivity.this, getString(R.string.msg_limit, limit), Toast.LENGTH_SHORT).show();
                        }

                        if (isPost) {
                            picturePathListPost.clear();
                            for (int i = 0; i < picturePathList.size(); i++) {
                                picturePathListPost.add(picturePathList.get(i));
                            }

                            for (int i = 0; i < picturePathListPost.size(); i++) {
                                System.out.println("POST: " + picturePathListPost.get(i));
                            }
                        } else {
                            picturePathListStory.clear();
                            for (int i = 0; i < picturePathList.size(); i++) {
                                picturePathListStory.add(picturePathList.get(i));
                            }

                            for (int i = 0; i < picturePathListStory.size(); i++) {
                                System.out.println("STORY: " + picturePathListStory.get(i));
                            }
                        }
                    }
                }

                setButtonPreview();
            }
        }
    }

    public String BitMapToString(Bitmap userImage1) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        userImage1.compress(Bitmap.CompressFormat.PNG, 60, byteArrayOutputStream);
        byte[] b = byteArrayOutputStream.toByteArray();
        base64String = Base64.encodeToString(b, Base64.DEFAULT);
        return base64String;
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    private void bindData() {
        edtTitle.setText(campaign.getTitle());
        edtNotes.setText(campaign.getNotes());
        autoCompleteTextView.setText(campaign.getCategoryName());
        edtMax.setText(String.valueOf(campaign.getAgeMax()));
        edtMin.setText(String.valueOf(campaign.getAgeMin()));
        edtDuration.setText(String.valueOf(campaign.getDuration()));
        edtPrice.setText(String.valueOf(campaign.getPrice()));
        edtTime.setText(campaign.getTime());
        String[] date = campaign.getDate().split("-");
        String formattedDate = date[2] + "/" + date[1] + "/" + date[0];
        edtDate.setText(formattedDate);
        if (campaign.getGender() == 1) rbMale.setChecked(true);
        if (campaign.getGender() == 2) rbFemale.setChecked(true);
        if (campaign.getGender() == 3) rbAll.setChecked(true);
        edtTime.setText(campaign.getTime());

        loadCity();
        setCategoryCode(campaign.getCategoryName());

        SocialMedia.select(this, new SocialMedia.CallbackSelect() {
            @Override
            public void success(JSONObject jsonObject) {
                try {
                    SocialMedia socialMedia = new SocialMedia(jsonObject);
                    username_ig = socialMedia.getId();
                } catch (Exception ex) {
                    Toast.makeText(UpdateActivity.this, ex + "", Toast.LENGTH_SHORT).show();
                    Log.d("RUNNN", ex + "");
                }
            }

            @Override
            public void error() {
            }
        });
    }

    public void loadCity() {
        City.select(this, new City.CallbackSelect() {
            @Override
            public void success(JSONArray data) {
                cities.clear();
                int index = 0;
                for (int i = 0; i < data.length(); i++) {
                    try {
                        City city = new City(data.getJSONObject(i));
                        cities.add(city);
                        if (cities.get(i).getCode() == campaign.getCityCode()) {
                            index = i;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                adapter.notifyDataSetChanged();
                spCity.setSelection(index, true);
            }

            @Override
            public void error() {

            }
        });
    }

    private void setCategoryCode(String selectedCategory) {
        if (selectedCategory.equals(getString(R.string.category1))) categoryCode = 1;
        if (selectedCategory.equals(getString(R.string.category2))) categoryCode = 2;
        if (selectedCategory.equals(getString(R.string.category3))) categoryCode = 3;
        if (selectedCategory.equals(getString(R.string.category4))) categoryCode = 4;
        if (selectedCategory.equals(getString(R.string.category5))) categoryCode = 5;
        if (selectedCategory.equals(getString(R.string.category6))) categoryCode = 6;
        if (selectedCategory.equals(getString(R.string.category7))) categoryCode = 7;
        if (selectedCategory.equals(getString(R.string.category8))) categoryCode = 8;
        if (selectedCategory.equals(getString(R.string.category9))) categoryCode = 9;
    }

    private void bindView() {
        edtTitle = findViewById(R.id.edt_title);
        edtNotes = findViewById(R.id.edt_notes);
        edtMax = findViewById(R.id.edt_max_age);
        edtMin = findViewById(R.id.edt_min_age);
        edtDuration = findViewById(R.id.edt_duration);
        edtPrice = findViewById(R.id.edt_price);
        rbMale = findViewById(R.id.rb_male);
        rbFemale = findViewById(R.id.rb_female);
        rbAll = findViewById(R.id.rb_all);
        edtTime = findViewById(R.id.edt_time);
        autoCompleteTextView = findViewById(R.id.autoComplete);
        edtDate = findViewById(R.id.edt_date);
        spCity = findViewById(R.id.sp_city);
        edtCaption = findViewById(R.id.edt_caption);
        edtBio = findViewById(R.id.edt_bio);
        btnPost = findViewById(R.id.btn_post);
        btnStory = findViewById(R.id.btn_story);
        btnPreviewPost = findViewById(R.id.btn_preview_post);
        btnPreviewStory = findViewById(R.id.btn_preview_story);
    }
}