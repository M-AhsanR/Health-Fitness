package com.giggly.app.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.giggly.app.AnalyticsTrackers;
import com.giggly.app.ApiStructure.ApiModelClass;
import com.giggly.app.ApiStructure.Constants;
import com.giggly.app.ApiStructure.ServerCallback;
import com.giggly.app.ApiStructure.VolleyMultipartRequest;
import com.giggly.app.ApiStructure.VolleySingleton;
import com.giggly.app.Fragments.SettingFragment;
import com.giggly.app.Models.ProfileApiModel.Profile;
import com.giggly.app.R;
import com.soundcloud.android.crop.Crop;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileActivity extends AppCompatActivity {

    ImageView back,tick_img;
    CheckBox male_chkbox,female_chkbox;
    CircleImageView feed_profile_img;
    RelativeLayout tick_rel,back_rel;
    EditText full_name,about_et;
    LinearLayout parent_linear;
    ProgressBar progress_bar;
    TextView date_of_birth;

    private static int RESULT_LOAD_IMAGE = 1;
    SharedPreferences mPrefs;
    String url_from_server;
    Bitmap selected_img_bitmap;
    private static final int MY_SOCKET_TIMEOUT_MS = 100000;
    Uri resultUri = null ;
    boolean photo_chnage=false;
    public static boolean FROM_EDIT_PROFILE;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    String date_remain_part;
    Uri imageUri;
    Profile  profile_data;

    String mImageFileLocation;
    Uri file;
    boolean imageFromCamera=false;

    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        initialization();

        // analytics code

        AnalyticsTrackers.initialize(EditProfileActivity.this);
//        Log.d("analyticsID", );
        AnalyticsTrackers.getInstance().get(AnalyticsTrackers.Target.APP, "UA-126547086-1");
        AnalyticsTrackers.getInstance().trackScreenView("Edit Profile Activity");


        // analytics code



        male_chkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                male_chkbox.setChecked(true);
                female_chkbox.setChecked(false);
            }
        });
        female_chkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                female_chkbox.setChecked(true);
                male_chkbox.setChecked(false);
            }
        });

        back_rel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        parent_linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(v);
            }
        });

        // get profile

        profile_data=SettingFragment.profile_data;



        full_name.setText(profile_data.getFull_name());
        full_name.setSelection(full_name.getText().length());


//        if(profile_data.getGender().equals("male")){
//
//            male_chkbox.setChecked(true);
//            female_chkbox.setChecked(false);
//        }
//        if(profile_data.getGender().equals("female")) {
//
//            female_chkbox.setChecked(true);
//            male_chkbox.setChecked(false);
//        }


        Picasso.with(EditProfileActivity.this).load(Constants.URL.BASE_URL+profile_data.getActive_dp())
                .error(R.drawable.profile_icon)
                .into(feed_profile_img);



        feed_profile_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder mbuilder=new AlertDialog.Builder(EditProfileActivity.this);
                View mview = LayoutInflater.from(EditProfileActivity.this).inflate(R.layout.image_dialogbox_layout,null);
                RelativeLayout camera_rel=mview.findViewById(R.id.camera_rel);
                RelativeLayout gallery_rel=mview.findViewById(R.id.gallery_rel);
                mbuilder.setView(mview);
                final AlertDialog dialog= mbuilder.create();
                dialog.show();



                FROM_EDIT_PROFILE=true;
                // fro croping//////////////////

                gallery_rel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (checkAndRequestPermissions()) {

                            Crop.pickImage(EditProfileActivity.this);
                            dialog.dismiss();
                            imageFromCamera = false;
                        }
                    }
                });


                camera_rel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        imageFromCamera=true;






                        if (checkAndRequestPermissions()) {

                            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
//                                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//                                startActivityForResult(cameraIntent, CAMERA_REQUEST);

                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                file = Uri.fromFile(getOutputMediaFile());
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, file);

                                mImageFileLocation = file.getPath();
                                startActivityForResult(intent, 100);

//                                oldVersons = true;

                            } else {



                                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                                if (intent.resolveActivity(getPackageManager()) != null) {
                                    File photoFile = new File(Environment.getExternalStorageDirectory(), "Pic.jpg");
                                    mImageFileLocation = photoFile.getAbsolutePath();
                                    imageUri = FileProvider.getUriForFile(EditProfileActivity.this,
                                            "com.positivevibes.app.fileprovider",
                                            photoFile);


                                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                                    startActivityForResult(intent, 100);
                                }

                            }
                        }
                    }
                });




            }
        });


        tick_rel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                tick_img.setVisibility(View.GONE);
                progress_bar.setVisibility(View.VISIBLE);
                if (photo_chnage) {
                    photo_chnage=false;
                    UploadDp();

                } else {

                    String gender;
                    if (male_chkbox.isChecked()) {
                        gender = "male";

                    } else {

                        gender = "female";
                    }

//                hit api----------------------------

                    mPrefs = EditProfileActivity.this.getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
                    String token = mPrefs.getString("USER_TOKEN", "");

                    Map<String, String> postParam = new HashMap<String, String>();
                    postParam.put("full_name", full_name.getText().toString());
                    postParam.put("gender", "");


                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("x-sh-auth", token);


                    ApiModelClass.GetApiResponse(Request.Method.POST, Constants.URL.UPDATE_PROFILE, EditProfileActivity.this, postParam, headers, new ServerCallback() {
                        @Override
                        public void onSuccess(JSONObject result, String ERROR) {

                            if (ERROR.isEmpty()) {

                                try {




                                    JSONObject jsonObject = new JSONObject(String.valueOf(result));

                                    String id = jsonObject.getString("_id");
                                    String Email = jsonObject.getString("email");
                                    boolean confirmed=jsonObject.getBoolean("confirmed");
                                    String fullName = jsonObject.getString("full_name");
                                    String Gender = jsonObject.getString("gender");
                                    boolean admin_access=jsonObject.getBoolean("admin_access");
                                    String dp_active_file = jsonObject.getString("dp_active_file");


                                    Profile obj=new Profile();
                                    obj.setProfile_id(id);
                                    obj.setEmail(Email);
                                    obj.setConfirmed(confirmed);
                                    obj.setFull_name(fullName);
                                    obj.setGender(Gender);
                                    obj.setAdmin_access(admin_access);
                                    obj.setActive_dp(dp_active_file);


                                    profile_data=obj;

                                    SettingFragment.profile_data=obj;


                                    // saving user data   in shared pref......

                                    SharedPreferences mPrefs = getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor mEditor = mPrefs.edit();
                                    mEditor.putString("USER_DP", dp_active_file);
                                    mEditor.putString("USER_NAME", fullName);
                                    mEditor.apply();

                                    // saving user data   in shared pref......

//                                    ProfileFragment.profile_data=profile_data;
                                    finish();

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                tick_img.setVisibility(View.VISIBLE);
                                progress_bar.setVisibility(View.GONE);
                            } else {

                                progress_bar.setVisibility(View.GONE);
                                tick_img.setVisibility(View.VISIBLE);
                                if (ERROR.equals("401")) {



                        SharedPreferences mPrefs = getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor mEditor = mPrefs.edit();
                                    mEditor.putString("USER_EMAIL", "");
                                    mEditor.putString("USER_PASSWORD", "");
                                    mEditor.putString("USER_TOKEN", "");
                                    mEditor.putString("USER_PIC", "");
                                    mEditor.apply();
                                    Intent intt = new Intent(EditProfileActivity.this, FirstActivity.class);
                                    startActivity(intt);
                                    EditProfileActivity.this.finish();
                                } else {

                                    Toast.makeText(EditProfileActivity.this, ERROR, Toast.LENGTH_SHORT).show();

                                }

                            }

                        }
                    });

                }
            }





//                hit api----------------------------


        });


    }


    private void UploadDp() {



//            api hit for uploading img---------------------------


        try {


//            selected_img_bitmap=MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),resultUri);
            selected_img_bitmap=MediaStore.Images.Media.getBitmap(EditProfileActivity.this.getContentResolver(),resultUri);


        } catch (IOException e) {
            e.printStackTrace();
        }
        mPrefs = EditProfileActivity.this.getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
        final String token = mPrefs.getString("USER_TOKEN", "");


        RequestQueue queue = Volley.newRequestQueue(EditProfileActivity.this);


        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, Constants.URL.UPLOAD_DP, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {

                String resultResponse = new String(response.data);
                try {
                    JSONObject result = new JSONObject(resultResponse);
                    int code = result.getInt("code");
                    if (code == 200) {

                        JSONObject user=result.getJSONObject("user");

                        String dp_active_file = user.getString("dp_active_file");
                        url_from_server=dp_active_file;

                        // send api hit------------------------


                        String gender;
                        if (male_chkbox.isChecked()) {
                            gender = "male";

                        } else {

                            gender = "female";
                        }

//                hit api----------------------------

                        mPrefs = EditProfileActivity.this.getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
                        String token = mPrefs.getString("USER_TOKEN", "");

                        Map<String, String> postParam = new HashMap<String, String>();
                        postParam.put("full_name", full_name.getText().toString());
                        postParam.put("gender", "");


                        HashMap<String, String> headers = new HashMap<String, String>();
                        headers.put("x-sh-auth", token);


                        ApiModelClass.GetApiResponse(Request.Method.POST,  Constants.URL.UPDATE_PROFILE, EditProfileActivity.this, postParam, headers, new ServerCallback() {
                            @Override
                            public void onSuccess(JSONObject result, String ERROR) {

                                if (ERROR.isEmpty()) {

                                    try {


                                        tick_img.setVisibility(View.VISIBLE);
                                        progress_bar.setVisibility(View.GONE);

                                        JSONObject jsonObject = new JSONObject(String.valueOf(result));


                                        String id = jsonObject.getString("_id");
                                        String Email = jsonObject.getString("email");
                                        boolean confirmed=jsonObject.getBoolean("confirmed");
                                        String fullName = jsonObject.getString("full_name");
                                        String Gender = jsonObject.getString("gender");
                                        boolean admin_access=jsonObject.getBoolean("admin_access");
                                        String dp_active_file = jsonObject.getString("dp_active_file");


                                        Profile obj=new Profile();
                                        obj.setProfile_id(id);
                                        obj.setEmail(Email);
                                        obj.setConfirmed(confirmed);
                                        obj.setFull_name(fullName);
                                        obj.setGender(Gender);
                                        obj.setAdmin_access(admin_access);
                                        obj.setActive_dp(dp_active_file);


                                        profile_data=obj;

                                        SettingFragment.profile_data=obj;


//                                        SharedPreferences.Editor mEditor = mPrefs.edit();
//                                        mEditor.putString("USER_DP", dp_active_file);
//                                        mEditor.apply();

                                        // saving user data   in shared pref......

                                        SharedPreferences mPrefs = getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
                                        SharedPreferences.Editor mEditor = mPrefs.edit();
                                        mEditor.putString("USER_DP", dp_active_file);
                                        mEditor.putString("USER_NAME", fullName);
                                        mEditor.apply();

                                        // saving user data   in shared pref......

//                                        ProfileFragment.profile_data=profile_data;

                                        finish();


                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }


                                } else {
                                    progress_bar.setVisibility(View.GONE);
                                    tick_img.setVisibility(View.VISIBLE);

                                    if (ERROR.equals("401")) {

//                        SharedPreferences mPrefs = getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
                                        SharedPreferences.Editor mEditor = mPrefs.edit();
                                        mEditor.putString("USER_EMAIL", "");
                                        mEditor.putString("USER_PASSWORD", "");
                                        mEditor.putString("USER_TOKEN", "");
                                        mEditor.putString("USER_PIC", "");
                                        mEditor.apply();
                                        Intent intt = new Intent(EditProfileActivity.this, FirstActivity.class);
                                        startActivity(intt);
                                        EditProfileActivity.this.finish();
                                    } else {

                                        Toast.makeText(EditProfileActivity.this, ERROR, Toast.LENGTH_SHORT).show();

                                    }

                                }

                            }
                        });
//                        replaceFragment(new SettingFragment(), "SettingFragment");



                        // send api hit------------------------

//                        Toast.makeText(getActivity(),image_link,Toast.LENGTH_LONG).show();
//
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {


            @Override
            public void onErrorResponse(VolleyError error) {

                progress_bar.setVisibility(View.GONE);
                tick_img.setVisibility(View.VISIBLE);

                NetworkResponse networkResponse = error.networkResponse;
                String errorMessage = "Unknown error";
                if (networkResponse == null) {
                    if (error.getClass().equals(TimeoutError.class)) {
                        errorMessage = "Request timeout";
                    } else if (error.getClass().equals(NoConnectionError.class)) {
                        errorMessage = "Failed to connect server";
                    }
                } else {
                    String result = new String(networkResponse.data);
                    try {
                        JSONObject response = new JSONObject(result);
                        String status = response.getString("status");
                        String message = response.getString("message");

                        Log.e("Error Status", status);
                        Log.e("Error Message", message);

                        if (networkResponse.statusCode == 404) {
                            errorMessage = "Resource not found";
                        } else if (networkResponse.statusCode == 401) {
                            errorMessage = message + " Please login again";
                        } else if (networkResponse.statusCode == 400) {
                            errorMessage = message + " Check your inputs";
                        } else if (networkResponse.statusCode == 500) {
                            errorMessage = message + " Something is getting wrong";
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                Log.i("Error", errorMessage);
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                return params;
            }

            @Override
            public Map<String, String> getHeaders()  {
                Map<String,String> params = new HashMap<String, String>();
                params.put("x-sh-auth", token);

                return params;
            }
            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                // file name could found file base or direct access from real path
                // for now just get bitmap data from ImageView
                params.put("dp", new DataPart( "dp.jpeg",getFileDataFromDrawable(EditProfileActivity.this,selected_img_bitmap), "image/jpeg"));

                return params;
            }
        };

        multipartRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


        VolleySingleton.getInstance(EditProfileActivity.this).addToRequestQueue(multipartRequest);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Crop.REQUEST_PICK && resultCode == RESULT_OK) {

            beginCrop(data.getData());
        } else if (requestCode == Crop.REQUEST_CROP) {
            handleCrop(resultCode, data);
        }
        else  if(requestCode == 100){



            if (resultCode == Activity.RESULT_OK) {


                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {

                    if (resultCode == RESULT_OK) {

                        photo_chnage = true;
                        beginCrop(file);
                    }

                } else {

                    Uri selectedImage = imageUri;
                    EditProfileActivity.this.getContentResolver().notifyChange(selectedImage, null);
                    ContentResolver cr = EditProfileActivity.this.getContentResolver();
                    Bitmap bitmap = null;

                    try {
                        bitmap = android.provider.MediaStore.Images.Media
                                .getBitmap(cr, selectedImage);

//                            feed_profile_img.setImageBitmap(bitmap);
//                            Toast.makeText(EditProfileActivity.this, selectedImage.toString(),
//                                    Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(EditProfileActivity.this, "Failed to load", Toast.LENGTH_SHORT)
                                .show();
                        Log.e("Camera", e.toString());
                    }

                    photo_chnage = true;

                    beginCrop(selectedImage);

                }
            }


        }
    }



    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    private void beginCrop(Uri source) {

        if(imageFromCamera == true) {

            /// for removing img auto landscape mode in samsung
            Uri yourUri = null;
            try {
                Bitmap bMap = MediaStore.Images.Media.getBitmap(EditProfileActivity.this.getContentResolver(), source);

//                File f = new File(mImageFileLocation);
////             yourUri = Uri.fromFile(f);
                bMap = rotate_img(bMap);
                yourUri = getImageUri(EditProfileActivity.this, bMap);


            } catch (IOException e) {
                e.printStackTrace();
            }


            Uri destination = Uri.fromFile(new File(EditProfileActivity.this.getCacheDir(), "cropped"));
            Crop.of(yourUri, destination).asSquare().start(EditProfileActivity.this);

            /// for removing img auto landscape mode in samsung

        }
        else {

            Uri destination = Uri.fromFile(new File(EditProfileActivity.this.getCacheDir(), "cropped"));
            Crop.of(source, destination).asSquare().start(EditProfileActivity.this);
        }
    }


    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == RESULT_OK) {
            photo_chnage=true;
            resultUri= Crop.getOutput(result);
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(EditProfileActivity.this.getContentResolver(), resultUri);
                mImageFileLocation=resultUri.getPath();
                bitmap=rotate_img(bitmap);

                resultUri = getImageUri(EditProfileActivity.this, bitmap);


                feed_profile_img.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }

//            Picasso.with(getActivity()).load(resultUri)
//                    .placeholder(R.drawable.loader)
//                    .error(R.drawable.profile_icon)
//                    .into(feed_profile_img);
//            feed_profile_img.setImageURI(Crop.getOutput(result));
        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(EditProfileActivity.this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private static File getOutputMediaFile(){
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "Positivity");

        if (!mediaStorageDir.exists()){
            if (!mediaStorageDir.mkdirs()){
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return new File(mediaStorageDir.getPath() + File.separator +
                "IMG_"+ timeStamp + ".jpg");
    }
    private void initialization() {

        tick_img=findViewById(R.id.tick_img);
        back=findViewById(R.id.back);
        male_chkbox=findViewById(R.id.male_chkbox);
        female_chkbox=findViewById(R.id.female_chkbox);
        feed_profile_img=findViewById(R.id.feed_profile_img);
        tick_rel=findViewById(R.id.tick_rel);
        back_rel=findViewById(R.id.back_rel);
        full_name=findViewById(R.id.full_name);
        parent_linear=findViewById(R.id.parent_linear);
        progress_bar=findViewById(R.id.progress_bar);


    }

    private  Bitmap rotate_img(Bitmap img){
        ExifInterface exifInterface =null;
        try {
            exifInterface=new ExifInterface(mImageFileLocation);
//            exifInterface=new ExifInterface(mImageFileLocation);
        } catch (IOException e) {
            e.printStackTrace();
        }

        int orientation =exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,ExifInterface.ORIENTATION_UNDEFINED);
        Matrix matrix=new Matrix();

        switch (orientation){
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.setRotate(90);
                break;

            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.setRotate(180);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.setRotate(270);
                break;

            default:


        }

        Bitmap rotatedBitmap=Bitmap.createBitmap(img,0,0,img.getWidth(),img.getHeight(),matrix,true);

        return rotatedBitmap;

    }

    public static byte[] getFileDataFromDrawable(Context context, Bitmap myBitmap) {
        Bitmap bitmap = myBitmap;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }
    protected void hideKeyboard(View view)
    {

//
//        InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//        in.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

    }

    private  boolean checkAndRequestPermissions() {
        int camera = ContextCompat.checkSelfPermission(EditProfileActivity.this, android.Manifest.permission.CAMERA);
        int storage = ContextCompat.checkSelfPermission(EditProfileActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int loc = ContextCompat.checkSelfPermission(EditProfileActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE);
        List<String> listPermissionsNeeded = new ArrayList<>();

        if (camera != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.CAMERA);
        }
        if (storage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (loc != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty())
        {
            ActivityCompat.requestPermissions(EditProfileActivity.this,listPermissionsNeeded.toArray
                    (new String[listPermissionsNeeded.size()]),REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }



}
