package com.example.alperkaya.myruns;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends Activity {

    private ImageView ivProfilePhoto;
    private Uri mImageCaptureUri;
    private boolean isTakenFromCamera;

    private static final int REQUEST_CODE_TAKE_FROM_CAMERA = 213;
    private static final int REQUEST_CODE_CROP_PHOTO = 214;

    private static final String IMAGE_UNSPECIFIED = "image/*";
    private static final String URI_INSTANCE_STATE_KEY = "saved_uri";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ivProfilePhoto = (ImageView) findViewById(R.id.ivProfilePhoto);

        if (savedInstanceState != null) {
            mImageCaptureUri = savedInstanceState
                    .getParcelable(URI_INSTANCE_STATE_KEY);
        }

        loadProfilePhoto();
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
        switch (item) {
            case MyRunsDialogFragment.ID_PHOTO_PICKER_FROM_CAMERA:
                // Take photo from camera
                // Construct an intent with action
                // MediaStore.ACTION_IMAGE_CAPTURE
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // Construct temporary image path and name to save the taken
                // photo
                mImageCaptureUri = Uri.fromFile(new File(Environment
                        .getExternalStorageDirectory(), "tmp_profile_photo.jpg"));
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
