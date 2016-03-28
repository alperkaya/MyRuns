package com.example.alperkaya.myruns;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends Activity {

    private static final int REQUEST_CODE_TAKE_FROM_CAMERA = 213;
    private static final int REQUEST_CODE_CROP_PHOTO = 214;
    private static final int REQUEST_CODE_TAKE_FROM_GALLERY = 215;
    private static final String IMAGE_UNSPECIFIED = "image/*";
    private static final String URI_INSTANCE_STATE_KEY = "saved_uri";
    private ImageView ivProfilePhoto;
    private Uri mImageCaptureUri;
    private boolean isTakenFromCamera;
    private SharedPreferences myPrefs;

    private String mName;
    private String mEmail;
    private String mPhoneNumber;
    private String mGender;
    private String mClass;
    private String mMajor;

    private EditText etName;
    private EditText etEmail;
    private EditText etPhoneNumber;
    private EditText etClass;
    private EditText etMajor;
    private RadioGroup rgGender;
    private RadioButton rbSelectedGender;
    private RadioButton rbMale;
    private RadioButton rbFemale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ivProfilePhoto = (ImageView) findViewById(R.id.ivProfilePhoto);
        etName = (EditText) findViewById(R.id.etName);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPhoneNumber = (EditText) findViewById(R.id.etPhoneNumber);
        etClass = (EditText) findViewById(R.id.etClass);
        etMajor = (EditText) findViewById(R.id.etMajor);
        rgGender = (RadioGroup) findViewById(R.id.rgGender);
        rbFemale = (RadioButton) findViewById(R.id.radioFemale);
        rbMale = (RadioButton) findViewById(R.id.radioMale);


        myPrefs = getPreferences(Activity.MODE_PRIVATE);

        if (savedInstanceState != null) {
            mImageCaptureUri = savedInstanceState
                    .getParcelable(URI_INSTANCE_STATE_KEY);
        }

        loadProfilePhoto();
        loadProfileInfo();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save the image capture uri before the activity goes into background
        outState.putParcelable(URI_INSTANCE_STATE_KEY, mImageCaptureUri);
    }

    // Handle data after activity returns.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK)
            return;

        switch (requestCode) {
            case REQUEST_CODE_TAKE_FROM_CAMERA:
                // Send image taken from camera for cropping
                cropImage();
                break;

            case REQUEST_CODE_CROP_PHOTO:
                // Update image view after image crop
                Bundle extras = data.getExtras();
                // Set the picture image in UI
                if (extras != null) {
                    //get the cropped bitmap
                    ivProfilePhoto
                            .setImageBitmap((Bitmap) extras.getParcelable("data"));
                    saveProfilePhoto();
                }

                // Delete temporary image taken by camera after crop.
                if (isTakenFromCamera) {
                    File f = new File(mImageCaptureUri.getPath());
                    if (f.exists())
                        f.delete();
                }

                break;

            case REQUEST_CODE_TAKE_FROM_GALLERY:
                mImageCaptureUri = data.getData();
                // Send image taken from gallery for cropping
                cropImage();
                break;


        }

    }

    // ****************** button click callback ***************************//
    public void onChangePhotoClicked(View view) {
        // changing the profile image, show the dialog asking the user
        // to choose between taking a picture
        // Go to MyRunsDialogFragment for details.
        displayDialog(MyRunsDialogFragment.DIALOG_ID_PHOTO_PICKER);
    }

    public void displayDialog(int id) {
        DialogFragment dialogFragment = MyRunsDialogFragment.newInstance(id);
        dialogFragment.show(getFragmentManager(), getString(R.string.dialog_fragment_tag_photo_picker));
    }

    public void onPhotoPickerItemSelected(int item) {
        // Construct temporary image path and name to save the taken
        // photo
        mImageCaptureUri = Uri.fromFile(new File(Environment
                .getExternalStorageDirectory(), "tmp_profile_photo.jpg"));
        switch (item) {
            case MyRunsDialogFragment.ID_PHOTO_PICKER_FROM_CAMERA:
                // Take photo from camera
                // Construct an intent with action
                // MediaStore.ACTION_IMAGE_CAPTURE
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT,
                        mImageCaptureUri);
                intent.putExtra("return-data", true);
                try {
                    // Start a camera capturing activity
                    // REQUEST_CODE_TAKE_FROM_CAMERA is an integer tag you
                    // defined to identify the activity in onActivityResult()
                    // when it returns
                    startActivityForResult(intent, REQUEST_CODE_TAKE_FROM_CAMERA);
                } catch (ActivityNotFoundException e) {
                    e.printStackTrace();
                }
                break;

            case MyRunsDialogFragment.ID_PHOTO_PICKER_FROM_GALLERY:
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                galleryIntent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
                galleryIntent.putExtra("return-data", true);

                startActivityForResult(galleryIntent, REQUEST_CODE_TAKE_FROM_GALLERY);
                break;

            default:
                return;
        }

        isTakenFromCamera = true;

    }

    // Crop and resize the image for profile
    private void cropImage() {
        // Use existing crop activity.
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(mImageCaptureUri, IMAGE_UNSPECIFIED);

        // Specify image size
        intent.putExtra("outputX", 100);
        intent.putExtra("outputY", 100);

        // Specify aspect ratio, 1:1
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", true);
        // REQUEST_CODE_CROP_PHOTO is an integer tag you defined to
        // identify the activity in onActivityResult() when it returns
        startActivityForResult(intent, REQUEST_CODE_CROP_PHOTO);
    }

    // ****************** private helper functions ***************************//
    private void loadProfilePhoto() {
        // Load profile photo from internal storage
        try {
            FileInputStream fis = openFileInput(getString(R.string.profile_photo_file_name));
            Bitmap bmap = BitmapFactory.decodeStream(fis);
            ivProfilePhoto.setImageBitmap(bmap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            ivProfilePhoto.setImageResource(R.drawable.default_profile);
        }
    }

    private void saveProfilePhoto() {
        // Commit all the changes into preference file
        // Save profile image into internal storage.
        ivProfilePhoto.buildDrawingCache();
        Bitmap bmap = ivProfilePhoto.getDrawingCache();
        try {
            FileOutputStream fos = openFileOutput(
                    getString(R.string.profile_photo_file_name), MODE_PRIVATE);
            bmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    private void loadProfileInfo() {
        //Load profile information from SharedPreferences
        mName = myPrefs.getString(getString(R.string.name), "");
        mEmail = myPrefs.getString(getString(R.string.email), "");
        mMajor = myPrefs.getString(getString(R.string.major), "");
        mClass = myPrefs.getString(getString(R.string.classname), "");
        mPhoneNumber = myPrefs.getString(getString(R.string.phone), "");
        mGender = myPrefs.getString(getString(R.string.gender), "");

        etName.setText(mName);
        etEmail.setText(mEmail);
        etMajor.setText(mMajor);
        etClass.setText(mClass);
        etPhoneNumber.setText(mPhoneNumber);

        if (getString(R.string.male) == mGender) {
            rbMale.setChecked(true);
        } else if (getString(R.string.female) == mGender) {
            rbFemale.setChecked(true);
        } else {
            rbMale.setChecked(true);
        }
    }

    // Handle when user clicks "Save" button
    public void onSaveButtonClicked(View view) {

        SharedPreferences.Editor mEditor = myPrefs.edit();

        mEditor.putString(getString(R.string.name), etName.getText().toString());
        mEditor.putString(getString(R.string.email), etEmail.getText().toString());
        mEditor.putString(getString(R.string.major), etMajor.getText().toString());
        mEditor.putString(getString(R.string.classname), etClass.getText().toString());
        mEditor.putString(getString(R.string.phone), etPhoneNumber.getText().toString());

        int checkedRadioBtnID = rgGender.getCheckedRadioButtonId();
        rbSelectedGender = (RadioButton) findViewById(checkedRadioBtnID);

        mEditor.putString(getString(R.string.gender), rbSelectedGender.getText().toString());
        mEditor.commit();
        finish();
    }

    // Handle when user clicks "Cancel" button
    public void onCancelButtonClicked(View view) {
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
