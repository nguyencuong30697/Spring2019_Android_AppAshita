package com.example.firebaseloginauth;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.List;

public class RegisterActivity extends AppCompatActivity {

//    private static final int MY_REQUEST_CODE = 123 ;
    private static final int MY_REQUEST_CODE_IMAGE = 321;
    private static final int REQUEST_GET_SINGLE_FILE = 99;
    List<AuthUI.IdpConfig> providers;
    Button signout;
    private FirebaseAuth mAuth;
    private StorageReference mStorageRef;
    ImageView imageView;
    FirebaseStorage storage = FirebaseStorage.getInstance("gs://fir-loginauth-3808b.appspot.com");
    Uri downloadUri;
    EditText txtUsername,txtPassword,txtConfirmPassword,txtDisplayName;
    String Username,Password,ConfirmPassword,DisplayName;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.imageView);
        txtUsername = findViewById(R.id.txtUserName);
        txtPassword = findViewById(R.id.txtPassword);
        txtConfirmPassword = findViewById(R.id.txtConfirmPassword);
        txtDisplayName = findViewById(R.id.txtDisplayName);
        signout = findViewById(R.id.btnSignOut);
        mAuth = FirebaseAuth.getInstance();
        createProgressDialog();
    }

    public void getDataFromEditText(){
        Username = txtUsername.getText().toString();
        Password = txtPassword.getText().toString();
        ConfirmPassword = txtConfirmPassword.getText().toString();
        DisplayName = txtDisplayName.getText().toString();
    }

    public void resetEditText(){
        txtUsername.setText("");
        txtPassword.setText("");
        txtConfirmPassword.setText("");
        txtDisplayName.setText("");
    }

    public void onClick(View v) {
        try {
            getDataFromEditText();
            progressDialog.show();
            if (Password.equalsIgnoreCase(ConfirmPassword)) {
                mAuth.createUserWithEmailAndPassword(Username, Password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information

                                    FirebaseUser user = mAuth.getCurrentUser();
                                    StorageReference storageRef = storage.getReference();
                                    StorageReference mountainImagesRef = storageRef.child("images/" + user.getEmail() + ".jpg");
                                    // Get the data from an ImageView as bytes
                                    imageView.setDrawingCacheEnabled(true);
                                    imageView.buildDrawingCache();
                                    Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                                    byte[] data = baos.toByteArray();
                                    UploadTask uploadTask = mountainImagesRef.putBytes(data);
                                    uploadTask.addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception exception) {
                                            // Handle unsuccessful uploads
                                            Toast.makeText(RegisterActivity.this, "Fail", Toast.LENGTH_LONG).show();
                                        }
                                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                                            // ...
                                            Toast.makeText(RegisterActivity.this, "Sucess", Toast.LENGTH_LONG).show();

                                        }
                                    });

                                    final StorageReference ref = storageRef.child("images/" + user.getEmail() + ".jpg");

                                    Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                                        @Override
                                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                            if (!task.isSuccessful()) {
                                                throw task.getException();
                                            }
                                            // Continue with the task to get the download URL
                                            return ref.getDownloadUrl();
                                        }


                                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Uri> task) {
                                            if (task.isSuccessful()) {
                                                downloadUri = task.getResult();
                                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                                        .setDisplayName(DisplayName)
                                                        .setPhotoUri(downloadUri)
                                                        .build();
                                                FirebaseUser user = mAuth.getCurrentUser();
                                                user.updateProfile(profileUpdates)
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {
                                                                    resetEditText();
                                                                    progressDialog.hide();
                                                                    Intent intent = new Intent(RegisterActivity.this, StatusUser.class);
                                                                    startActivity(intent);
                                                                }
                                                            }
                                                        });
                                            } else {
                                                // Handle failures
                                                // ...
                                            }
                                        }
                                    });

                                } else {
                                    resetEditText();
                                    Toast.makeText(RegisterActivity.this, "Success", Toast.LENGTH_LONG).show();
                                }

                                // ...
                            }
                        });
            } else {
                Toast.makeText(RegisterActivity.this, "Password and ConfirmPassword are diffirent", Toast.LENGTH_LONG).show();
                txtConfirmPassword.setText("");
                txtPassword.setText("");
            }
        }catch(Exception e){
            progressDialog.hide();
            Toast.makeText(RegisterActivity.this, "Error please do again", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if(requestCode == MY_REQUEST_CODE){
//            IdpResponse response = IdpResponse.fromResultIntent(data);
//            if(requestCode == RESULT_OK){
//                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//                Toast.makeText(RegisterActivity.this,"Sucess",Toast.LENGTH_LONG).show();
//                signout.setEnabled(true);
//            }
//        }

        if(requestCode == MY_REQUEST_CODE_IMAGE && resultCode == RESULT_OK && data != null){

            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(thumbnail);
        }
        try {
            if (resultCode == RESULT_OK) {
                if (requestCode == REQUEST_GET_SINGLE_FILE) {
                    Uri selectedImageUri = data.getData();
                    // Get the path from the Uri
                    final String path = getPathFromURI(selectedImageUri);
                    if (path != null) {
                        File f = new File(path);
                        selectedImageUri = Uri.fromFile(f);
                    }
                    imageView.setImageURI(selectedImageUri);
                }
            }
        } catch (Exception e) {
            Log.e("FileSelectorActivity", "File select error", e);
        }

    }

    public String getPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }

    public void onClickImage(View view) {
            showAlertDialog();
    }

    public void showAlertDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Image Choose");
        builder.setMessage("Take picture from ?");
        builder.setCancelable(true); //đây là thuộc tính nếu set false thì khi show dialog lên người dùng click ra bên ngoài dialog thì nó vẫn không bị mất, nếu set true thì sẽ mất khi click vào bất kì đâu ngoài dialog.
        builder.setPositiveButton("My Camera", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,MY_REQUEST_CODE_IMAGE);
            }
        });
        builder.setNegativeButton("My Ablums", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Select Picture"),REQUEST_GET_SINGLE_FILE);
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void createProgressDialog(){
        progressDialog = ProgressDialog.show(this, null, null, true);
        progressDialog.setContentView(R.layout.custom_progress_dialog);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressDialog.hide();
    }

}


