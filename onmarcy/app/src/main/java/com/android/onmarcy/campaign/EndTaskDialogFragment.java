package com.android.onmarcy.campaign;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.android.onmarcy.PreviewActivity;
import com.android.onmarcy.R;
import com.theartofdev.edmodo.cropper.CropImage;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.Locale;

import model.Campaign;
import model.SocialMedia;

public class EndTaskDialogFragment extends DialogFragment {
    public static final String TAG = "end_task_dialog";
    public static final String STATE_CAMPAIGN = "campaign";
    private TextView tvTitle, tvDesc, tvBrand, tvPrice;
    private Button btnView, btnPreviewPost, btnPreviewStory, btnPreviewBio;
    private EditText edtLike, edtComment, edtSave, edtReach, edtImpression, edtNotes, edtEngagement;
    private ImageButton btnPost, btnStory, btnBio;
    private Activity activity;
    private Toolbar toolbar;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private String base64String = "";
    private int permission = 0;
    private String[] base64StringList = new String[3];
    private Uri[] imageUri = new Uri[3];
    private int mode = 0;
    private Campaign campaign;

    public EndTaskDialogFragment() {

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(STATE_CAMPAIGN, campaign);
    }

    public EndTaskDialogFragment(Campaign campaign) {
        this.campaign = campaign;
    }

    public static EndTaskDialogFragment display(FragmentManager fragmentManager, Campaign campaign) {
        EndTaskDialogFragment endTaskDialogFragment = new EndTaskDialogFragment(campaign);
        endTaskDialogFragment.show(fragmentManager, TAG);
        return endTaskDialogFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme_FullScreenDialog);
        if (savedInstanceState != null) {
            campaign = savedInstanceState.getParcelable(STATE_CAMPAIGN);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_end_task, container, false);
        toolbar = view.findViewById(R.id.toolbar);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        activity = getActivity();
        bindView(getView());
        bindData();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });
            toolbar.inflateMenu(R.menu.menu_crop);
            toolbar.setOnMenuItemClickListener(item -> {
                boolean isValid = true;

                if (TextUtils.isEmpty(edtNotes.getText().toString())) {
                    edtNotes.setError(getResources().getString(R.string.please_fill_out_this_field));
                    isValid = false;
                }

                if (TextUtils.isEmpty(edtLike.getText().toString())) {
                    edtLike.setError(getResources().getString(R.string.please_fill_out_this_field));
                    isValid = false;
                }

                if (TextUtils.isEmpty(edtReach.getText().toString())) {
                    edtReach.setError(getResources().getString(R.string.please_fill_out_this_field));
                    isValid = false;
                }

                if (TextUtils.isEmpty(edtImpression.getText().toString())) {
                    edtImpression.setError(getResources().getString(R.string.please_fill_out_this_field));
                    isValid = false;
                }

                if (TextUtils.isEmpty(edtSave.getText().toString())) {
                    edtSave.setError(getResources().getString(R.string.please_fill_out_this_field));
                    isValid = false;
                }

                if (TextUtils.isEmpty(edtComment.getText().toString())) {
                    edtComment.setError(getResources().getString(R.string.please_fill_out_this_field));
                    isValid = false;
                }

                if (TextUtils.isEmpty(edtEngagement.getText().toString())) {
                    edtEngagement.setError(getResources().getString(R.string.please_fill_out_this_field));
                    isValid = false;
                }

                if (isValid) {
                    String notes = edtNotes.getText().toString();
                    int like = Integer.parseInt(edtLike.getText().toString());
                    int save = Integer.parseInt(edtSave.getText().toString());
                    int comment = Integer.parseInt(edtComment.getText().toString());
                    int impression = Integer.parseInt(edtImpression.getText().toString());
                    int engagement = Integer.parseInt(edtEngagement.getText().toString());
                    int reach = Integer.parseInt(edtReach.getText().toString());

                    Campaign.insertResult(activity, campaign.getCode(), like, comment, save, impression, reach, engagement, notes, false, new Campaign.Callback() {
                        @Override
                        public void success() {
                            for (int i = 0; i < base64StringList.length; i++) {
                                int ctr = i + 1;
                                String result = "result" + ctr;
                                Campaign.uploadResultPicture(activity, campaign.getCode(), result, base64StringList[i], false, new Campaign.Callback() {
                                    @Override
                                    public void success() {
                                    }

                                    @Override
                                    public void error() {
                                    }
                                });
                            }

                            Toast.makeText(activity, getString(R.string.success), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(activity, TaskActivity.class);
                            startActivity(intent);
                            dismiss();
                            activity.finish();
                        }

                        @Override
                        public void error() {

                        }
                    });
                } else {
                    Log.d("RUNNN", "attempt failed");
                }

                return true;
            });
        }

        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mode = 1;
                verifyStoragePermissions(activity);
            }
        });

        btnStory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mode = 2;
                verifyStoragePermissions(activity);
            }
        });

        btnBio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mode = 3;
                verifyStoragePermissions(activity);
            }
        });

        setButtonPreview();

        btnPreviewPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, PreviewActivity.class);
                intent.putExtra(PreviewActivity.TAG2, imageUri[0].toString());
                startActivity(intent);
            }
        });

        btnPreviewStory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, PreviewActivity.class);
                intent.putExtra(PreviewActivity.TAG2, imageUri[1].toString());
                startActivity(intent);
            }
        });

        btnPreviewBio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, PreviewActivity.class);
                intent.putExtra(PreviewActivity.TAG2, imageUri[2].toString());
                startActivity(intent);
            }
        });

        btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, ContentActivity.class);
                intent.putExtra(ContentActivity.EXTRA_CAMPAIGN, campaign);
                intent.putExtra(ContentActivity.EXTRA_APPROVAL, true);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            dialog.getWindow().setWindowAnimations(R.style.AppTheme_Slide);
        }
    }

    private void bindData() {
        tvTitle.setText(campaign.getTitle());
        tvBrand.setText(campaign.getBrandName());
        if (campaign.getNotes().equals("")) tvDesc.setVisibility(View.GONE);
        tvDesc.setText(campaign.getNotes());
        NumberFormat nf = NumberFormat.getInstance(new Locale("da", "DK"));
        tvPrice.setText(getString(R.string.price_format, nf.format(campaign.getPrice())));
    }

    private void setButtonPreview() {
        if (base64StringList[0] == null) {
            btnPreviewPost.setVisibility(View.GONE);
        } else {
            btnPreviewPost.setVisibility(View.VISIBLE);
        }

        if (base64StringList[1] == null) {
            btnPreviewStory.setVisibility(View.GONE);
        } else {
            btnPreviewStory.setVisibility(View.VISIBLE);
        }

        if (base64StringList[2] == null) {
            btnPreviewBio.setVisibility(View.GONE);
        } else {
            btnPreviewBio.setVisibility(View.VISIBLE);
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
        final CharSequence[] options = {getString(R.string.choose_from_gallery), getString(R.string.cancel)};
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(R.string.insert_picture);
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals(getString(R.string.choose_from_gallery))) {
                    pickFromGallery();
                } else if (options[item].equals(getString(R.string.cancel))) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void pickFromGallery() {
        CropImage.activity().start(getContext(), this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == activity.RESULT_OK) {
            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                Uri resultUri = result.getUri();

                if (mode == 1) imageUri[0] = resultUri;
                if (mode == 2) imageUri[1] = resultUri;
                if (mode == 3) imageUri[2] = resultUri;

                Bitmap thumbnail;

                try {
                    thumbnail = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), resultUri);
                    thumbnail = getResizedBitmap(thumbnail, 600);

                    if (mode == 1) base64StringList[0] = BitMapToString(thumbnail);
                    if (mode == 2) base64StringList[1] = BitMapToString(thumbnail);
                    if (mode == 3) base64StringList[2] = BitMapToString(thumbnail);

                    System.out.println(BitMapToString(thumbnail));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                setButtonPreview();
            }
        }
    }

    private String BitMapToString(Bitmap userImage1) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        userImage1.compress(Bitmap.CompressFormat.PNG, 60, byteArrayOutputStream);
        byte[] b = byteArrayOutputStream.toByteArray();
        base64String = Base64.encodeToString(b, Base64.DEFAULT);
        return base64String;
    }

    private Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();
        float bitmapRatio = (float) width / (float) height;

        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }

        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    private void bindView(View view) {
        tvTitle = view.findViewById(R.id.tv_title);
        tvDesc = view.findViewById(R.id.tv_description);
        tvPrice = view.findViewById(R.id.tv_price);
        tvBrand = view.findViewById(R.id.tv_brand);
        btnView = view.findViewById(R.id.btn_view);
        btnPost = view.findViewById(R.id.btn_post);
        btnStory = view.findViewById(R.id.btn_story);
        btnBio = view.findViewById(R.id.btn_bio);
        btnPreviewPost = view.findViewById(R.id.btn_preview_post);
        btnPreviewStory = view.findViewById(R.id.btn_preview_story);
        btnPreviewBio = view.findViewById(R.id.btn_preview_bio);
        edtComment = view.findViewById(R.id.edt_comment);
        edtEngagement = view.findViewById(R.id.edt_engagement);
        edtLike = view.findViewById(R.id.edt_like);
        edtSave = view.findViewById(R.id.edt_save);
        edtImpression = view.findViewById(R.id.edt_impression);
        edtReach = view.findViewById(R.id.edt_reach);
        edtNotes = view.findViewById(R.id.edt_notes);
    }
}
