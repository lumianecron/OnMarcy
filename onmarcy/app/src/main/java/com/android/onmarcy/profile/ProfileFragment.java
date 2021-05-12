package com.android.onmarcy.profile;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.text.TextUtils;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.util.Base64;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.onmarcy.CropActivity;
import com.android.onmarcy.Global;
import com.android.onmarcy.HomeActivity;
import com.android.onmarcy.MainActivity;
import com.android.onmarcy.campaign.PendingCampaignActivity;
import com.android.onmarcy.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import model.Category;
import model.City;
import model.SocialMedia;
import model.User;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    private Activity activity;
    CircleImageView imageView;
    TextInputEditText edtName, edtUsername, edtEmail, edtPhone, edtPassword, edtConfirm, edtVerification, edtInstagram;
    TextInputLayout textInputLayoutInstagram, textInputLayoutVerification, textInputLayoutCategory;
    AutoCompleteTextView autoCompleteTextView;
    TextView tvStatus, tvFollower, tvCategory, tvFollowing, tvUsername, tvTotalPost, tvTotalComment, tvTotalLike, tvMinAge, tvMaxAge, tvMale, tvFemale, tvTimePosting, tvServiceType;
    SearchableSpinner spCity;
    Button btnSend, arrow, btnVerify, btnEdit;
    LinearLayout hiddenView, linearSendInstagram;
    CardView cardView;
    LinearLayout layoutVerification;
    MaterialCardView baseCardview;

    private ArrayList<City> cities = new ArrayList<>();
    private ArrayList<Category> categories = new ArrayList<>();
    private ArrayAdapter<City> adapter;
    private User user;
    private int code = 0;
    private String cityName = "";
    private String selectedCategory = "";
    private int categoryCode = 0;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private String base64String = "";
    private int permission = 0;
    private Button btnAddPhoto;
    private View view;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private String photoURL = "";
    private Uri imageUri;

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.activity = getActivity();
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;
        bindView();
        bindData();

        if(activity.getIntent().hasExtra(CropActivity.TAG)){
            imageUri = Uri.parse(activity.getIntent().getStringExtra(CropActivity.TAG));
            Bitmap thumbnail = null;
            try {
                thumbnail = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), imageUri);
                thumbnail = getResizedBitmap(thumbnail, 400);
                uploadPicture(BitMapToString(thumbnail));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isValid = true;
                code = getCode(spCity.getSelectedItem().toString());

                if(TextUtils.isEmpty(edtInstagram.getText().toString())){
                    edtInstagram.setError(getString(R.string.please_fill_out_this_field));
                    isValid = false;
                }
                if(selectedCategory.equals("")){
                    autoCompleteTextView.setError(getString(R.string.please_choose_category));
                    isValid = false;
                }

                if(isValid){
                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    builder.setCancelable(false);
                    builder.setTitle(R.string.confirmation);
                    builder.setMessage(getString(R.string.msg_verification_ig, edtInstagram.getText().toString()));
                    builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            registerInstagramAccount();
                            linearSendInstagram.setVisibility(View.GONE);
                        }
                    });
                    builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });

        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SocialMedia.verify(getActivity(), edtVerification.getText().toString(), true, new SocialMedia.Callback() {
                    @Override
                    public void success() {
                        Toast.makeText(getActivity(), getString(R.string.verification_success), Toast.LENGTH_SHORT).show();
                        bindData();
                    }

                    @Override
                    public void error() {
                        Toast.makeText(getActivity(), getString(R.string.verification_fail), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        arrow.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {
                if (hiddenView.getVisibility() == View.VISIBLE) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        TransitionManager.beginDelayedTransition(cardView, new AutoTransition());
                    }
                    hiddenView.setVisibility(View.GONE);
                    arrow.setText(R.string.expand);
                } else {
                    TransitionManager.beginDelayedTransition(cardView, new AutoTransition());
                    hiddenView.setVisibility(View.VISIBLE);
                    arrow.setText(R.string.collapse);
                }
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, UpdateInstagramActivity.class);
                startActivity(intent);
            }
        });

        btnAddPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifyStoragePermissions(activity);
            }
        });
    }

    private void bindView() {
        imageView = view.findViewById(R.id.image_view);
        edtName = view.findViewById(R.id.edt_name);
        edtUsername = view.findViewById(R.id.edt_username);
        edtEmail = view.findViewById(R.id.edt_email);
        edtPhone = view.findViewById(R.id.edt_phone);
        edtPassword = view.findViewById(R.id.edt_password);
        edtConfirm = view.findViewById(R.id.edt_confirm);
        spCity = view.findViewById(R.id.sp_city);
        edtVerification = view.findViewById(R.id.edt_verification);
        btnSend = view.findViewById(R.id.btn_send);
        edtInstagram = view.findViewById(R.id.edt_instagram);
        tvStatus = view.findViewById(R.id.tv_status);
        textInputLayoutInstagram = view.findViewById(R.id.txt_input_layout_instagram);
        textInputLayoutVerification = view.findViewById(R.id.txt_input_layout_verification);
        cardView = view.findViewById(R.id.base_cardview);
        arrow = view.findViewById(R.id.arrow_button);
        hiddenView = view.findViewById(R.id.hidden_view);
        tvFollower = view.findViewById(R.id.tv_followers);
        tvFollowing = view.findViewById(R.id.tv_following);
        tvUsername = view.findViewById(R.id.tv_username);
        tvTotalPost = view.findViewById(R.id.tv_total_post);
        tvTotalComment = view.findViewById(R.id.tv_total_comment);
        tvTotalLike = view.findViewById(R.id.tv_total_like);
        tvMinAge = view.findViewById(R.id.tv_min_age);
        tvMaxAge = view.findViewById(R.id.tv_max_age);
        tvMale = view.findViewById(R.id.tv_male);
        tvFemale = view.findViewById(R.id.tv_female);
        tvTimePosting = view.findViewById(R.id.tv_time_posting);
        tvServiceType = view.findViewById(R.id.tv_service_type);
        tvCategory = view.findViewById(R.id.tv_category);
        btnVerify = view.findViewById(R.id.btn_verify);
        layoutVerification = view.findViewById(R.id.layout_verification);
        baseCardview = view.findViewById(R.id.base_cardview);
        autoCompleteTextView = view.findViewById(R.id.autoComplete);
        textInputLayoutCategory = view.findViewById(R.id.txt_input_layout_category);
        btnEdit = view.findViewById(R.id.btn_edit);
        linearSendInstagram = view.findViewById(R.id.linear_send_instagram);
        btnAddPhoto = view.findViewById(R.id.btn_add_photo);
    }

    private void bindData() {
        try {
            user = new User(new JSONObject(Global.getShared(Global.SHARED_INDEX.USER, "{}")));
            edtName.setText(user.getName());
            edtUsername.setText(user.getUsername());
            edtEmail.setText(user.getEmail());
            edtPhone.setText(user.getPhone());
            code = user.getCityCode();
            cityName = user.getCityName();

            if(user.getPhotoUrl().equals("")){
                setImage("");
            }else{
                setImage(user.getPhotoUrl());
            }

            if(photoURL.equals("")){
                photoURL = user.getPhotoUrl();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        registerForContextMenu(autoCompleteTextView);
        autoCompleteTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                autoCompleteTextView.showContextMenu();
            }
        });

        baseCardview.setVisibility(View.GONE);
        tvStatus.setVisibility(View.GONE);
        SocialMedia.select(getActivity(), new SocialMedia.CallbackSelect() {
            @Override
            public void success(JSONObject jsonObject) {
                try {
                    SocialMedia socialMedia = new SocialMedia(jsonObject);
                    tvStatus.setVisibility(View.VISIBLE);

                    if (socialMedia.getStatusVerify() == 0) {
                        tvStatus.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_pending_24, 0, 0, 0);
                        tvStatus.setText(getString(R.string.pending));
                        tvStatus.setTextColor(getResources().getColor(R.color.yellow));
                        linearSendInstagram.setVisibility(View.GONE);
                    } else {
                        tvStatus.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_check_circle_outline_24, 0, 0, 0);
                        tvStatus.setText(getString(R.string.verified));
                        tvStatus.setTextColor(getResources().getColor(R.color.green));
                        tvFollower.setText(String.valueOf(socialMedia.getTotalFollower()));
                        tvFollowing.setText(String.valueOf(socialMedia.getTotalFollowing()));
                        tvUsername.setText(socialMedia.getId());
                        tvTotalPost.setText(String.valueOf(socialMedia.getTotalPost()));
                        tvTotalComment.setText(String.valueOf(socialMedia.getTotalComment()));
                        tvTotalLike.setText(String.valueOf(socialMedia.getTotalLike()));
                        tvMinAge.setText(String.valueOf(socialMedia.getMarketAgeMin()));
                        tvMaxAge.setText(String.valueOf(socialMedia.getMarketAgeMax()));
                        tvMale.setText(String.valueOf(socialMedia.getMarketMale()));
                        tvFemale.setText(String.valueOf(socialMedia.getMarketFemale()));
                        tvTimePosting.setText(String.valueOf(socialMedia.getTimePosting()));
                        tvCategory.setText(socialMedia.getCategoryName());
                        layoutVerification.setVisibility(View.GONE);
                        baseCardview.setVisibility(View.VISIBLE);

                        String txtService = "";
                        ArrayList<String> services = new ArrayList<>();
                        if(socialMedia.getServiceBio() == 1){
                            services.add(getString(R.string.bio));
                        }
                        if(socialMedia.getServicePost() == 1){
                            services.add(getString(R.string.post));
                        }
                        if(socialMedia.getServiceStory() == 1){
                            services.add(getString(R.string.story));
                        }
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            txtService = String.join(", ", services);
                        }
                        tvServiceType.setText(txtService);
                    }
                } catch (Exception ex) {
                    Toast.makeText(getActivity(), ex + "", Toast.LENGTH_SHORT).show();
                    Log.d("RUNNN", ex + "");
                }
            }

            @Override
            public void error() {
            }
        });

        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, cities);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCity.setAdapter(adapter);

        loadCity();
        loadCategory();
    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        activity.getMenuInflater().inflate(R.menu.context_menu_category, menu);
        menu.setHeaderTitle(getString(R.string.choose_category));
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        selectedCategory = item.toString();
        textInputLayoutCategory.setPlaceholderText(selectedCategory);
        if (selectedCategory.equals(getString(R.string.category1))) categoryCode = 1;
        if (selectedCategory.equals(getString(R.string.category2))) categoryCode = 2;
        if (selectedCategory.equals(getString(R.string.category3))) categoryCode = 3;
        if (selectedCategory.equals(getString(R.string.category4))) categoryCode = 4;
        if (selectedCategory.equals(getString(R.string.category5))) categoryCode = 5;
        if (selectedCategory.equals(getString(R.string.category6))) categoryCode = 6;
        if (selectedCategory.equals(getString(R.string.category7))) categoryCode = 7;
        if (selectedCategory.equals(getString(R.string.category8))) categoryCode = 8;
        if (selectedCategory.equals(getString(R.string.category9))) categoryCode = 9;
        return super.onContextItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.profile_menu, menu);
        if(user.getUserType() == 2){
            MenuItem menuItem = menu.findItem(R.id.item_pending_campaign);
            menuItem.setVisible(false);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        boolean isValid = true;
        switch (item.getItemId()) {
            case R.id.item_save:
                if (TextUtils.isEmpty(edtName.getText().toString())) {
                    edtName.setError(getResources().getString(R.string.please_fill_out_this_field));
                    isValid = false;
                }
                if (TextUtils.isEmpty(edtUsername.getText().toString())) {
                    edtUsername.setError(getResources().getString(R.string.please_fill_out_this_field));
                    isValid = false;
                }
                if (TextUtils.isEmpty(edtEmail.getText().toString())) {
                    edtEmail.setError(getResources().getString(R.string.please_fill_out_this_field));
                    isValid = false;
                }
                if (TextUtils.isEmpty(edtPhone.getText().toString())) {
                    edtPhone.setError(getResources().getString(R.string.please_fill_out_this_field));
                    isValid = false;
                }
                if (!edtConfirm.getText().toString().equals(edtPassword.getText().toString())) {
                    edtConfirm.setError(getResources().getString(R.string.password_doesnt_match));
                    isValid = false;
                }

                if (isValid) {
                    if (user.getUserType() == 1) {
                        code = getCode(spCity.getSelectedItem().toString());
                        User.updateBrand(getActivity(), edtName.getText().toString(), edtPhone.getText().toString(), code, false, new User.Callback() {
                            @Override
                            public void success() {
                                update();
                            }

                            @Override
                            public void error() {
                                Toast.makeText(activity, getString(R.string.update_unsuccessful), Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        code = getCode(spCity.getSelectedItem().toString());
                        User.updateMarketer(activity, edtName.getText().toString(), edtPhone.getText().toString(), code, false, new User.Callback() {
                            @Override
                            public void success() {
                                update();
                            }

                            @Override
                            public void error() {
                                Toast.makeText(activity, getString(R.string.update_unsuccessful), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
                break;
            case R.id.item_logout:
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setCancelable(false);
                builder.setTitle(R.string.confirmation);
                builder.setMessage(R.string.msg_logout);
                builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        User.logout();
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        startActivity(intent);
                        getActivity().finishAffinity();
                    }
                });
                builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
                break;
            case R.id.item_pending_campaign:
                Intent intent = new Intent(activity, PendingCampaignActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void registerInstagramAccount(){
        SocialMedia.insert(getActivity(), user.getUsername(), 1, categoryCode, code, edtInstagram.getText().toString(), 0, 0, 0, 0, 0, 0, 0, 0, 0, "0", 1, 0, 0, "", true, new SocialMedia.Callback() {
            @Override
            public void success() {
//                Global.showLoading(getContext(), getString(R.string.success), getString(R.string.info));
            }

            @Override
            public void error() {
//                Global.showLoading(getContext(), getString(R.string.error), getString(R.string.info));
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(true);
        builder.setTitle(R.string.info);
        builder.setMessage(R.string.msg_verification);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void update(){
        Toast.makeText(activity, getString(R.string.update_successful), Toast.LENGTH_SHORT).show();
        user.setName(edtName.getText().toString());
        user.setPhone(edtPhone.getText().toString());
        user.setCityCode(code);
        user.setPhotoUrl(photoURL);
        Global.setShared(Global.SHARED_INDEX.USER, new Gson().toJson(user));
        HomeActivity homeActivity = (HomeActivity) activity;
        homeActivity.profileFragment();
    }

    public void loadCity() {
        City.select(getActivity(), new City.CallbackSelect() {
            @Override
            public void success(JSONArray data) {
                cities.clear();
                int idx = 0;
                for (int i = 0; i < data.length(); i++) {
                    try {
                        City city = new City(data.getJSONObject(i));
                        cities.add(city);
                        if (cities.get(i).getName().equalsIgnoreCase(cityName)) {
                            idx = i;
                        }
//                        Log.d("RUNNNNN", "idx : " + i);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                /*Log.d("RUNNNNN", "city : " + cityName);
                Log.d("RUNNNNN", "idxCity : " + idx);*/
                adapter.notifyDataSetChanged();
                spCity.setSelection(idx, true);
            }

            @Override
            public void error() {

            }
        });
    }

    public void loadCategory() {
        Category.select(getActivity(), new Category.CallbackSelect() {
            @Override
            public void success(JSONArray data) {
                categories.clear();
                for (int i = 0; i < data.length(); i++) {
                    try {
                        Category category = new Category(data.getJSONObject(i));
                        categories.add(category);
//                        Log.d("RUNNNNN", "idx : " + i);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                Log.d("RUNNN", "Category count : " + categories.size());
            }

            @Override
            public void error() {

            }
        });
    }

    public int getCode(String cityName) {
        int index = -1;
        for (int i = 0; i < cities.size(); i++) {
            if (cities.get(i).getName().equals(cityName)) {
                index = i;
                break;
            }
        }
        return cities.get(index).getCode();
    }

    private void setImage(String img){
        if(img.equals("")){
            Glide.with(view.getContext())
                    .load(R.drawable.person)
                    .apply(new RequestOptions().override(120, 120))
                    .into(imageView);
        }else{
            Glide.with(view.getContext())
                    .load(img)
                    .apply(new RequestOptions().override(120, 120))
                    .into(imageView);
        }
    }

    public void selectImage() {
        final CharSequence[] options = { getString(R.string.take_a_photo), getString(R.string.choose_from_gallery), getString(R.string.remove_photo), getString(R.string.cancel) };
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(R.string.insert_picture);
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals(getString(R.string.take_a_photo))) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (activity.checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                        {
                            requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
                        }
                        else
                        {
                            ContentValues values = new ContentValues();
                            values.put(MediaStore.Images.Media.TITLE, "New Picture");
                            values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
                            imageUri = activity.getContentResolver().insert(
                                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            cameraIntent.putExtra("android.intent.extras.CAMERA_FACING", 1);
                            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                            startActivityForResult(cameraIntent, 1);
                        }
                    }
                }else if (options[item].equals(getString(R.string.choose_from_gallery))) {
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);
                }else if (options[item].equals(getString(R.string.remove_photo))) {
                    uploadPicture("");
                }
                else if (options[item].equals(getString(R.string.cancel))) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    public void verifyStoragePermissions(Activity activity) {
        permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
        }else{
            selectImage();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (requestCode == MY_CAMERA_PERMISSION_CODE) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.TITLE, "New Picture");
                values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
                imageUri = activity.getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                cameraIntent.putExtra("android.intent.extras.CAMERA_FACING", 1);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(cameraIntent, 1);
            } else {
                selectImage();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                try {
//                    Bitmap thumbnail = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), imageUri);
//                    thumbnail = getResizedBitmap(thumbnail, 400);
//                    uploadPicture(BitMapToString(thumbnail));
                    cropImage(imageUri);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else if (requestCode == 2) {
                Uri selectedImage = data.getData();
                String[] filePath = { MediaStore.Images.Media.DATA };
                Cursor c = activity.getContentResolver().query(selectedImage, filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                c.close();
//                Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
//                thumbnail = getResizedBitmap(thumbnail, 400);
//                uploadPicture(BitMapToString(thumbnail));
                cropImage(Uri.fromFile(new File(picturePath)));
            }
        }
    }

    private void cropImage(Uri imageUri){
        Intent intent = new Intent(activity, CropActivity.class);
        intent.putExtra(CropActivity.TAG, imageUri.toString());
        startActivity(intent);
    }

    private void uploadPicture(String img){
        User.uploadPicture(activity, img, false, new User.CallbackSelect() {
            @Override
            public void success(JSONObject data) {
                User user = new User(data);
                Global.setShared(Global.SHARED_INDEX.USER, new Gson().toJson(user));
                setImage(user.getPhotoUrl());
                photoURL = user.getPhotoUrl();
            }

            @Override
            public void error() {

            }
        });
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
}