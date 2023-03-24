package com.vendor.salon.activity;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import com.vendor.salon.*;
import com.vendor.salon.R;
import com.vendor.salon.data_Class.profile.UpdateProfileResponse;
import com.vendor.salon.databinding.ActivityProfileBinding;
import com.vendor.salon.networking.RetrofitClient;
import com.vendor.salon.utilityMethod.Compress;
import com.vendor.salon.utilityMethod.FunctionCall;
import com.vendor.salon.utilityMethod.GetFileFromUriUsingBufferReader;
import com.vendor.salon.utilityMethod.NetworkChangeListener;
import com.vendor.salon.utilityMethod.PermissionUtils;
import com.vendor.salon.utilityMethod.loginResponsePref;
import com.github.dhaval2404.imagepicker.ImagePicker;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Profile extends AppCompatActivity {

    private static final int PROFILE_IMG_UPLOAD_REQ_CODE = 109;
    private static final int ID_REQ_CODE = 110;
    private int Check_REQ_CODE = 113;
    private int LICENCE_REQ_CODE = 112;
    private ActivityProfileBinding profileBinding;
    private File Prof_Img_File;
    private File ID_File;
    private File Cancel_Check_File;
    private File Licence_File;
    private String token;
    PermissionUtils takePermissionUtils;
    String imageFilePath;
    String ProfileImageUri, IdProofImgUri, LicenceImgUri, CheckImgUri;
    String ccp="+91";
    String ImageFor="";
    private boolean isGPSEnabled= false;
    private boolean isNetworkEnabled = false;
    private Location location;
    private String latitude;
    private String longitude;
    private LocationManager locationManager;
    private int REQUEST_LOCATION    = 1000 ;
    private boolean isApiCalled = false ;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener() ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        profileBinding = ActivityProfileBinding.inflate(getLayoutInflater());
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(profileBinding.getRoot());
        takePermissionUtils=new PermissionUtils(this,mPermissionResult);

        String phone;
            ccp = getIntent().getStringExtra("ccp");
            token = getIntent().getStringExtra("token");
            phone =  getIntent().getStringExtra("phone");
        profileBinding.phoneNo.setText(phone);


        profileBinding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(loginResponsePref.getInstance(Profile.this).isLogIN()) {
                    Intent backTohomeIntent = new Intent(Profile.this, Home.class);
                    startActivity(backTohomeIntent);
                    finish();
                }
                else {
                    Intent backIntent = new Intent(Profile.this, Login.class);
                    startActivity(backIntent);
                    finish();
                }
            }
        });
        profileBinding.dobCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        profileBinding.editProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (takePermissionUtils.isStorageCameraPermissionGranted()) {
                    ImageFor ="ProfileImage";
                    selectImage();
//                    selectImageUsingImagePickerDependency();
//                    contract.launch("image/*");
                }
                else
                {
                    takePermissionUtils.showStorageCameraPermissionDailog(getString(R.string.we_need_storage_and_camera_permission_for_upload_profile_pic));
                }


             /*   Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PROFILE_IMG_UPLOAD_REQ_CODE);*/
            }
        });
        profileBinding.cardIDProof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (takePermissionUtils.isStorageCameraPermissionGranted()) {
                    ImageFor ="CardIdProof";
                    selectImage();
                }
                else
                {
                    takePermissionUtils.showStorageCameraPermissionDailog(getString(R.string.we_need_storage_and_camera_permission_for_upload_profile_pic));
                }

                //alertDialogInActive(ID_REQ_CODE);
            }
        });
        profileBinding.dobCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog picker = new DatePickerDialog(Profile.this, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        profileBinding.dob.setText(day + "/" + (month + 1) + "/" + year);
                    }
                }, year, month, day);
                picker.show();
                picker.getDatePicker().setMaxDate(cal.getTimeInMillis());
//            dataPicDialog.datePicker.maxDate();
            }
        });
        profileBinding.cardUploadLicence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (takePermissionUtils.isStorageCameraPermissionGranted()) {
                    ImageFor ="CardLicence";
                    selectImage();
                }
                else
                {
                    takePermissionUtils.showStorageCameraPermissionDailog(getString(R.string.we_need_storage_and_camera_permission_for_upload_profile_pic));
                }

            }

        });

        profileBinding.vendorTypeCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (profileBinding.vendorTypeCardItem.getVisibility() == View.VISIBLE) {
                    profileBinding.vendorTypeCardItem.setVisibility(View.GONE);

                    profileBinding.salon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(profileBinding.shopNameCard.getVisibility() == View.GONE) {
                                profileBinding.shopNameCard.setVisibility(View.VISIBLE );
                            }
                            profileBinding.vendorType.setText(profileBinding.salon.getText().toString());
                            if (profileBinding.vendorTypeCardItem.getVisibility() == View.VISIBLE) {
                                profileBinding.vendorTypeCardItem.setVisibility(View.GONE); ;
                            }
                            profileBinding.vendorTypeCard.setVisibility(View.VISIBLE);
                        }
                    });

                    profileBinding.freeLancher.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            profileBinding.vendorType.setText(profileBinding.freeLancher.getText().toString());
                            profileBinding.shopNameCard.setVisibility(View.GONE);
                            if (profileBinding.vendorTypeCardItem.getVisibility() == View.VISIBLE) {
                                profileBinding.vendorTypeCardItem.setVisibility(View.GONE);

                            }
                            profileBinding.vendorTypeCardItem.setVisibility(View.GONE);
                        }
                    }); {

                    }

                } else {
                    profileBinding.vendorTypeCardItem.setVisibility(View.VISIBLE);

                    profileBinding.salon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(profileBinding.shopNameCard.getVisibility() == View.GONE) {
                                profileBinding.shopNameCard.setVisibility(View.VISIBLE );
                            }
                            profileBinding.vendorType.setText(profileBinding.salon.getText().toString());
                            if (profileBinding.vendorTypeCardItem.getVisibility() == View.VISIBLE) {
                                profileBinding.vendorTypeCardItem.setVisibility(View.GONE); ;
                            }
                            profileBinding.vendorTypeCard.setVisibility(View.VISIBLE);
                        }
                    });

                    profileBinding.freeLancher.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            profileBinding.vendorType.setText(profileBinding.freeLancher.getText().toString());
                            profileBinding.shopNameCard.setVisibility(View.GONE);
                            if (profileBinding.vendorTypeCardItem.getVisibility() == View.VISIBLE) {
                                profileBinding.vendorTypeCardItem.setVisibility(View.GONE);

                            }
                            profileBinding.vendorTypeCardItem.setVisibility(View.GONE);
                        }
                    }); {

                    }
                }
            }
        });

        profileBinding.genderCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (profileBinding.genderItemCard.getVisibility() == View.VISIBLE) {
                    profileBinding.genderItemCard.setVisibility(View.GONE);
                    profileBinding.male.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            profileBinding.Gender.setText(profileBinding.male.getText().toString());
                            if (profileBinding.genderItemCard.getVisibility() == View.VISIBLE) {
                                profileBinding.genderItemCard.setVisibility(View.GONE);
                            }
                        }
                    });

                    profileBinding.female.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            profileBinding.Gender.setText(profileBinding.female.getText().toString());
                            if (profileBinding.genderItemCard.getVisibility() == View.VISIBLE) {
                                profileBinding.genderItemCard.setVisibility(View.GONE);
                            }
                        }
                    });
                } else {
                    profileBinding.genderItemCard.setVisibility(View.VISIBLE);
                    profileBinding.male.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            profileBinding.Gender.setText(profileBinding.male.getText().toString());
                            if (profileBinding.genderItemCard.getVisibility() == View.VISIBLE) {
                                profileBinding.genderItemCard.setVisibility(View.GONE);
                            }
                        }
                    });

                    profileBinding.female.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            profileBinding.Gender.setText(profileBinding.female.getText().toString());
                            if (profileBinding.genderItemCard.getVisibility() == View.VISIBLE) {
                                profileBinding.genderItemCard.setVisibility(View.GONE);
                            }
                        }
                    });
                }
            }
        });


        profileBinding.GenderCustomerCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (profileBinding.genderCustomerItemCard.getVisibility() == View.VISIBLE) {
                    profileBinding.genderCustomerItemCard.setVisibility(View.GONE);
                    profileBinding.customerMale.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            profileBinding.GenderCustomer.setText(profileBinding.customerMale.getText().toString());
                            if (profileBinding.genderCustomerItemCard.getVisibility() == View.VISIBLE) {
                                profileBinding.genderCustomerItemCard.setVisibility(View.GONE);
                            }
                        }
                    });

                    profileBinding.customerFemale.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            profileBinding.GenderCustomer.setText(profileBinding.customerFemale.getText().toString());
                            if (profileBinding.genderCustomerItemCard.getVisibility() == View.VISIBLE) {
                                profileBinding.genderCustomerItemCard.setVisibility(View.GONE);
                            }
                        }
                    });

                    profileBinding.transGender.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            profileBinding.GenderCustomer.setText(profileBinding.transGender.getText().toString());
                            if (profileBinding.genderCustomerItemCard.getVisibility() == View.VISIBLE) {
                                profileBinding.genderCustomerItemCard.setVisibility(View.GONE);
                            }
                        }
                    });

                } else {
                    profileBinding.genderCustomerItemCard.setVisibility(View.VISIBLE);
                    profileBinding.customerMale.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            profileBinding.GenderCustomer.setText(profileBinding.customerMale.getText().toString());
                            if (profileBinding.genderCustomerItemCard.getVisibility() == View.VISIBLE) {
                                profileBinding.genderCustomerItemCard.setVisibility(View.GONE);
                            }
                        }
                    });

                    profileBinding.customerFemale.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            profileBinding.GenderCustomer.setText(profileBinding.customerFemale.getText().toString());
                            if (profileBinding.genderCustomerItemCard.getVisibility() == View.VISIBLE) {
                                profileBinding.genderCustomerItemCard.setVisibility(View.GONE);
                            }
                        }
                    });

                    profileBinding.transGender.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            profileBinding.GenderCustomer.setText(profileBinding.transGender.getText().toString());
                            if (profileBinding.genderCustomerItemCard.getVisibility() == View.VISIBLE) {
                                profileBinding.genderCustomerItemCard.setVisibility(View.GONE);
                            }
                        }
                    });

                }
            }
        });

        profileBinding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Profile.this, Login.class);
                startActivity(intent);
                finishAffinity();
            }
        });

        getCurrentLatLong();
        profileBinding.submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isEntriesValidated()) {
                    sendDataUsingApi();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Profile.this, Login.class);
        startActivity(intent);
        finishAffinity();
    }

    private void selectImageUsingImagePickerDependency() {

        ImagePicker.with(this)
                .crop()                    //Crop image(Optional), Check Customization for more option
                .compress(1024)            //Final image size will be less than 1 MB(Optional)
                .maxResultSize(
                        1080,
                        1080
                )   //Final image resolution will be less than 1080 x 1080(Optional)
                .start();
    }



    private void sendDataUsingApi() {
        if (!isApiCalled ) {
            isApiCalled = true;
            FunctionCall.showProgressDialog(Profile.this);
            RequestBody thumbnailBody = null;
            MultipartBody.Part ID_Path_part_val = null;
            if (ID_File != null) {
                thumbnailBody = RequestBody.create(MediaType.parse("id_proof_image"), Compress.images(ID_File.getPath(), 1 * 1024 * 1024));
                ID_Path_part_val = MultipartBody.Part.createFormData("id_proof_image", ID_File.getName(), thumbnailBody);
            }


            RequestBody prof_Img_Body = null;
            MultipartBody.Part prof_image_part_val = null;
            if (Prof_Img_File != null) {
                prof_Img_Body = RequestBody.create(MediaType.parse("owner_image"), Compress.images(Prof_Img_File.getPath(), 1 * 1024 * 1024));
                prof_image_part_val = MultipartBody.Part.createFormData("owner_image", Prof_Img_File.getName(), prof_Img_Body);
            }

            RequestBody Licence_Body = null;
            MultipartBody.Part Licence_part_val = null;
            if (Licence_File != null) {
                Licence_Body = RequestBody.create(MediaType.parse("licence_image"), Compress.images(Licence_File.getPath(), 1 * 1024 * 1024));
                Licence_part_val = MultipartBody.Part.createFormData("licence_image", Licence_File.getName(), Licence_Body);
            }

            getCurrentLatLong();
//
//        RequestBody Cancel_Check_Body = null;
//        MultipartBody.Part cancel_check_part_val = null;
//        if(Cancel_Check_File != null ) {
//            Cancel_Check_Body= RequestBody.create(MediaType.parse("cancel_check"), Cancel_Check_File);
//            cancel_check_part_val = MultipartBody.Part.createFormData("cancel_check", Cancel_Check_File.getName(), Cancel_Check_Body);
//        }
            Call<UpdateProfileResponse> call = RetrofitClient.getVendorService().updateMyProfile(
                    "Bearer " + token,
                    getRequestBody(profileBinding.email.getText().toString()),
                    getRequestBody(profileBinding.name.getText().toString()),
                    getRequestBody(profileBinding.phoneNo.getText().toString()),
                    getRequestBody(ccp),
                    getRequestBody(profileBinding.Gender.getText().toString()),
                    getRequestBody(profileBinding.dob.getText().toString()),
                    getRequestBody(profileBinding.vendorType.getText().toString().toLowerCase() != "salon" ? "freelancer" : "salon"),
//                getRequestBody(profileBinding.vendorType.getText().toString()),
//                getRequestBody(profileBinding.BankName.getText().toString()),
//                cancel_check_part_val,
                    getRequestBody(profileBinding.location.getText().toString()),
                    prof_image_part_val,
                    ID_Path_part_val,
//                getRequestBody(profileBinding.AccountHolderName.getText().toString()),
                    Licence_part_val,
//                getRequestBody(profileBinding.AccountNo.getText().toString()),
                    getRequestBody(profileBinding.GenderCustomer.getText().toString())
//                getRequestBody(profileBinding.ifscCode.getText().toString())
            );
            call.enqueue(new Callback<UpdateProfileResponse>() {
                @Override
                public void onResponse(Call<UpdateProfileResponse> call, Response<UpdateProfileResponse> response) {
                    FunctionCall.DismissDialog(Profile.this);
                    isApiCalled = false;
                    if (response.isSuccessful() && response.body() != null) {
                        if (response.body().isResult()) {
                            Log.d("updateprofilehit", "onResponse: " + response.body());

                            Toast.makeText(Profile.this, " Your Details has been submitted Successfully!.   ", Toast.LENGTH_SHORT).show();

                            startActivity(new Intent(Profile.this, Login.class));
                            finish();
                        } else {
                            Log.d("updateprofilehit", "onResponseResult: " + response.body());
                        }
                    } else {
                        if (response.body() != null) {
                            Toast.makeText(Profile.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        Log.d("updateprofilehit", "onResponse: " + response.body());
                    }
                }

                @Override
                public void onFailure(Call<UpdateProfileResponse> call, Throwable t) {
                    isApiCalled = false;
                    FunctionCall.DismissDialog(Profile.this);
                    Log.d("updateprofilehit", "onFailure: " + t.getMessage());
                }
            });
        }
    }

    private void getCurrentLatLong() {
        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);// getting GPS status
        isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!isGPSEnabled) {
            OnGPS();
        } else {
            getLocation();
        }

    }

    private void OnGPS() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Enable GPS").setCancelable(false).setPositiveButton("Yes", new  DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(
                Profile.this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                Profile.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        } else {
            Location locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (locationGPS != null) {
                double lat = locationGPS.getLatitude();
                double longi = locationGPS.getLongitude();
                latitude = String.valueOf(lat);
                longitude = String.valueOf(longi);
//                Toast.makeText(this, "Lat: "+latitude+ " ---  . Long "+ longitude +" .", Toast.LENGTH_SHORT).show();
            } else {
//                Toast.makeText(this, "Unable to find location.", Toast.LENGTH_SHORT).show();
            }
        }
    }

//    private void alertDialogInActive(int REQ_CODE) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        View view = LayoutInflater.from(this).inflate(R.layout.layout_upload_image, null, false);
//        builder.setView(view);
//        AlertDialog alert = builder.create();
//        alert.show();
//        Button btn_Cancel = view.findViewById(R.id.btn_cancel);
//        Button btn_select_image = view.findViewById(R.id.btn_image);
//        btn_Cancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(Profile.this, "Cancelled. ", Toast.LENGTH_SHORT).show();
//                alert.dismiss();
//            }
//        });
//        btn_select_image.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                alert.dismiss();
//                Intent intent = new Intent();
//                intent.setType("image/*");
//                intent.setAction(Intent.ACTION_PICK);
//                startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQ_CODE);
//            }
//
//        });
//    }

    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PROFILE_IMG_UPLOAD_REQ_CODE) {
                Prof_Img_File = GetFileFromUriUsingBufferReader.getImageFile(this, Uri.parse(data.getData().toString()));
                profileBinding.profileImg.setImageURI(Uri.parse(Prof_Img_File.getPath()));
            } else if (requestCode == ID_REQ_CODE) {
                ID_File = GetFileFromUriUsingBufferReader.getImageFile(this, Uri.parse(data.getData().toString()));
                profileBinding.UploadIDProof.setText(ID_File.getName());
            } else if (requestCode == Check_REQ_CODE) {
                Cancel_Check_File = GetFileFromUriUsingBufferReader.getImageFile(this, Uri.parse(data.getData().toString()));
                profileBinding.cancelCheck.setText(Cancel_Check_File.getName().toString());
            } else if (requestCode == LICENCE_REQ_CODE) {
                Licence_File = GetFileFromUriUsingBufferReader.getImageFile(this, Uri.parse(data.getData().toString()));
                profileBinding.UploadLicense.setText(Licence_File.getName().toString());
            } else {
                Toast.makeText(this, "Cancelled. ", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, " Not Request with right request codes .  ", Toast.LENGTH_SHORT).show();
        }
    }

*/
    private RequestBody getRequestBody(String str) {
        return RequestBody.create(str, MediaType.parse("text/plain"));
    }

    public boolean isEntriesValidated() {
        if (profileBinding.name.getText().toString().isEmpty()) {
            profileBinding.name.setError("Name is Mandatory ");
            profileBinding.name.setHintTextColor(Color.RED);
            profileBinding.name.requestFocus();
        } else if (profileBinding.dob.getText().toString().isEmpty()) {
            profileBinding.dob.setError("Select your dob");
            profileBinding.dob.setHintTextColor(Color.RED);
            profileBinding.dobCard.requestFocus();
            Toast.makeText(this, " Select DOB ", Toast.LENGTH_SHORT).show();
        } else if (profileBinding.Gender.getText().toString().isEmpty()) {
            profileBinding.Gender.setError("Select Your Gender");
            profileBinding.dob.setError(null);
            profileBinding.Gender.requestFocus();
            profileBinding.Gender.setHintTextColor(Color.RED);
            Toast.makeText(this, " Select Your Gender ", Toast.LENGTH_SHORT).show();
        } else if (profileBinding.vendorType.getText().toString().isEmpty()) {
            profileBinding.Gender.setError(null);
            profileBinding.salon.setError("Select Your Salon");
            profileBinding.vendorType.requestFocus();
            profileBinding.vendorType.setHintTextColor(Color.RED);
            Toast.makeText(this, " Select Your Salon Type", Toast.LENGTH_SHORT).show();
        }
        else if(profileBinding.email.getText().toString().isEmpty()) {
            profileBinding.vendorType.setError(null);
            profileBinding.email.setError("Mandatory");
            profileBinding.email.requestFocus();
            profileBinding.email.setHintTextColor(Color.RED);
        }
        else if(profileBinding.location.getText().toString().isEmpty()) {
            profileBinding.location.setError("Mandatory");
            profileBinding.location.requestFocus();
            profileBinding.location.setHintTextColor(Color.RED);
        }
//        else if(profileBinding.BankName.getText().toString().isEmpty()) {
//            profileBinding.BankName.setError("Mandatory");
//            profileBinding.BankName.requestFocus();
//            profileBinding.BankName.setHintTextColor(Color.RED);
//        }
//        else if(profileBinding.AccountHolderName.getText().toString().isEmpty()) {
//            profileBinding.AccountHolderName.setError("Mandatory");
//            profileBinding.AccountHolderName.requestFocus();
//            profileBinding.AccountHolderName.setHintTextColor(Color.RED);
//        }
//        else if(profileBinding.AccountNo.getText().toString().isEmpty()) {
//            profileBinding.AccountNo.setError("Mandatory");
//            profileBinding.AccountNo.requestFocus();
//            profileBinding.AccountNo.setHintTextColor(Color.RED);
//        }
//        else if(profileBinding.ifscCode.getText().toString().isEmpty()) {
//            profileBinding.ifscCode.setError("Mandatory");
//            profileBinding.ifscCode.requestFocus();
//            profileBinding.ifscCode.setHintTextColor(Color.RED);
//        }
        else if (profileBinding.GenderCustomer.getText().toString().isEmpty()) {
            profileBinding.GenderCustomer.setError(" Kindly Select Your Customer's gender. ");
            profileBinding.GenderCustomer.requestFocus();
            profileBinding.GenderCustomer.setHintTextColor(Color.RED);
            Toast.makeText(this, " Select Your Customer's gender. ", Toast.LENGTH_SHORT).show();
        } else if (!profileBinding.termAndCondition.isChecked()) {
            profileBinding.GenderCustomer.setError(null);
            Toast.makeText(this, " Kindly Accept Terms and Conditions to proceed. ", Toast.LENGTH_SHORT).show();
        } else {
            return true;
        }

        profileBinding.progressBar.setVisibility(View.GONE);
        return false;
    }

    private ActivityResultLauncher<String[]> mPermissionResult = registerForActivityResult(
            new ActivityResultContracts.RequestMultiplePermissions(), new ActivityResultCallback<Map<String, Boolean>>() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onActivityResult(Map<String, Boolean> result) {

                    boolean allPermissionClear=true;
                    List<String> blockPermissionCheck=new ArrayList<>();
                    for (String key : result.keySet())
                    {
                        if (!(result.get(key)))
                        {
                            allPermissionClear=false;
                            blockPermissionCheck.add(FunctionsClass.getPermissionStatus(Profile.this,key));
                        }
                    }
                    if (blockPermissionCheck.contains("blocked"))
                    {
                        FunctionsClass.showPermissionSetting(getApplicationContext(),getString(R.string.we_need_storage_and_camera_permission_for_upload_profile_pic));
                    }
                    else
                    if (allPermissionClear)
                    {
                        selectImage();
                    }

                }
            });


    private void selectImage() {
        final CharSequence[] options = {getString(R.string.take_photo),
                getString(R.string.choose_from_gallery), "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(Profile.this, R.style.AlertDialogCustom);
        builder.setTitle(getString(R.string.add_photo_));
        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals(getString(R.string.take_photo))) {
                    openCameraIntent();
                } else if (options[item].equals(getString(R.string.choose_from_gallery))) {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    resultCallbackForGallery.launch(intent);
                } else if (options[item].equals("Cancel")) {

                    dialog.dismiss();

                }
            }
        });

        builder.show();

    }

    private void openCameraIntent() {
        Intent pictureIntent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE);
        if (pictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (Exception ex) {
            }
            if (photoFile != null) {
              /*  Uri photoURI = FileProvider.getUriForFile(getActivity(), getPackageName()+".fileprovider", photoFile);
                pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);*/

                Uri photoURI=   FileProvider.getUriForFile(this,
                        BuildConfig.APPLICATION_ID + ".fileprovider", photoFile);
                pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);

                resultCallbackForCamera.launch(pictureIntent);
            }
        }
    }

    private File createImageFile() throws Exception {
        String timeStamp =
                new SimpleDateFormat("yyyyMMdd_HHmmss",
                        Locale.ENGLISH).format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
        imageFilePath = image.getAbsolutePath();
        return image;
    }


    ActivityResultLauncher<Intent> resultCallbackForGallery = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        Uri selectedImage = data.getData();
                        SetImages(selectedImage);
//                        beginCrop(selectedImage);
                    }
                    else if(result.getResultCode() == Activity.RESULT_CANCELED) {
                        Toast.makeText(Profile.this, " Cancelled. ", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(Profile.this, "Something wrong ", Toast.LENGTH_SHORT).show();
                    }
                }
            });




    ActivityResultLauncher<Intent> resultCallbackForCamera = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK ) {
//                        Matrix matrix = new Matrix();
//                        try {
//                            ExifInterface exif = new ExifInterface(imageFilePath);
//                            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
//                            switch (orientation) {
//                                case ExifInterface.ORIENTATION_ROTATE_90:
//                                    matrix.postRotate(90);
//                                    break;
//                                case ExifInterface.ORIENTATION_ROTATE_180:
//                                    matrix.postRotate(180);
//                                    break;
//                                case ExifInterface.ORIENTATION_ROTATE_270:
//                                    matrix.postRotate(270);
//                                    break;
//                            }
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }

//                        Bitmap  bitmap = (Bitmap) result.getData().getExtras().get("data");
                        Uri selectedImage = (Uri.fromFile(new File(imageFilePath)));
//                        if(selectedImage != null) {
                            SetImages(selectedImage);
//                        }
                 //       beginCrop(selectedImage);
                        // UploadImage(selectedImage);

                    }
                    else if( result.getResultCode() == Activity.RESULT_CANCELED ) {
                        Toast.makeText(Profile.this, "Cancelled. ", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Log.d("camerapick", "onActivityResult: "+ result.getData());
                    }
                }
            });

    public String getRealPathFromURI(Uri uri) {
//        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
//        cursor.moveToFirst();
//        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
//        return cursor.getString(idx);

        String path = null;
        String[] proj = { MediaStore.MediaColumns.DATA };
        Cursor cursor = getContentResolver().query(uri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToLast();
        if (cursor.moveToFirst()) {
//            int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        }            path = cursor.getString(column_index);

        cursor.close();
        return path;

    }

    private void SetImages(Uri selectedImage) {
        if(ImageFor.equals("ProfileImage")){
//            ProfileImageUri = GetFileFromUriUsingBufferReader.getImageFile(this,selectedImage.getPath().toString()));
//            ProfileImageUri = getRealPathFromURI(selectedImage);
//            Log.d("===>>", ProfileImageUri);
            Prof_Img_File = GetFileFromUriUsingBufferReader.getImageFile(this, selectedImage);
//            Prof_Img_File = new File(ProfileImageUri);

//            ParcelFileDescriptor parcelFileDescriptor =
//                    null;
//            try {
//                parcelFileDescriptor = getContentResolver().openFileDescriptor(selectedImage, "r");
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            }
//            FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
//            Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
//            profileBinding.profileImg.setImageBitmap(image);
            profileBinding.profileImg.setImageURI(Uri.parse(Prof_Img_File.getAbsolutePath().toString()));

//            try {
//                parcelFileDescriptor.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }


        }else if(ImageFor.equals("CardIdProof")){
//            IdProofImgUri = getRealPathFromURI(selectedImage);
            ID_File = GetFileFromUriUsingBufferReader.getImageFile(Profile.this, selectedImage);
            profileBinding.UploadIDProof.setText(ID_File.getName());

        }else if(ImageFor.equals("CardLicence")){
//            LicenceImgUri = getRealPathFromURI(selectedImage);
            Licence_File = GetFileFromUriUsingBufferReader.getImageFile(Profile.this, selectedImage);
            profileBinding.UploadLicense.setText(Licence_File.getName());


//        }else if(ImageFor.equals("Check")){
////            CheckImgUri = getRealPathFromURI(selectedImage);
//            Cancel_Check_File = GetFileFromUriUsingBufferReader.getImageFile(Profile.this, selectedImage);
//            profileBinding.cancelCheck.setText(Cancel_Check_File.getName());
        }



    }


   /* private void beginCrop(Uri source) {
        Intent intent= CropImage.activity(source).setCropShape(CropImageView.CropShape.RECTANGLE)
                .setAspectRatio(1,1).getIntent(Profile.this);
        resultCallbackForCrop.launch(intent);
    }

    ActivityResultLauncher<Intent> resultCallbackForCrop = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        CropImage.ActivityResult cropResult = CropImage.getActivityResult(data);

                        if(ImageFor.equals("ProfileImage")){
                            ProfileImageUri = String.valueOf(cropResult.getUri());
                            Log.d("===>>", ProfileImageUri);
                            Prof_Img_File = new File(cropResult.getUri().getPath());

                                    ParcelFileDescriptor parcelFileDescriptor =
                                    null;
                            try {
                                parcelFileDescriptor = getContentResolver().openFileDescriptor(cropResult.getUri(), "r");
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                            FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
                            Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
                            profileBinding.profileImg.setImageBitmap(image);

                            try {
                                parcelFileDescriptor.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }


                        }else if(ImageFor.equals("CardIdProof")){
                            IdProofImgUri =String.valueOf(cropResult.getUri());
                            ID_File = new File(cropResult.getUri().getPath());
                            profileBinding.UploadIDProof.setText(ID_File.getName());

                        }else if(ImageFor.equals("CardLicence")){
                            LicenceImgUri =String.valueOf(cropResult.getUri());
                            Licence_File = new File(cropResult.getUri().getPath());
                            profileBinding.UploadLicense.setText(Licence_File.getName());


                        }else if(ImageFor.equals("Check")){
                            CheckImgUri =String.valueOf(cropResult.getUri());
                            Cancel_Check_File = new File(cropResult.getUri().getPath());
                            profileBinding.cancelCheck.setText(Cancel_Check_File.getName());
                        }





                    }
                }
            });*/



    @Override
    protected void onStart() {
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener , intentFilter );
        super.onStart();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(networkChangeListener);
        super.onStop();
    }



}