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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.firebaseloginauth.DTO.UserDTO;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class StatusUser extends AppCompatActivity {

    TextView txtName,numberOfSeen;
    EditText editTextEmail,editTextPhone,editTextAdress,editTextAge,editTextInformation;
    RadioButton radioButtonMale,radioButtonFamele;
    ImageView imageView;
    FirebaseDatabase database;
    DatabaseReference myRef;
    public static UserDTO userDTO;
    ProgressDialog progressDialog;
    private static final int MY_REQUEST_CODE_IMAGE = 321;
    private static final int REQUEST_GET_SINGLE_FILE = 99;
    private FirebaseAuth mAuth;
    FirebaseStorage storage = FirebaseStorage.getInstance("gs://fir-loginauth-3808b.appspot.com");
    Uri downloadUri;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_user);
        txtName = findViewById(R.id.txtNameUserInformation);
        imageView = findViewById(R.id.imageViewUserInformation);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPhone = findViewById(R.id.editTextPhone);
        editTextAdress = findViewById(R.id.editTextAdress);
        editTextAge = findViewById(R.id.editTextAge);
        editTextInformation = findViewById(R.id.editText2);
        radioButtonFamele = findViewById(R.id.radioButtonFamele);
        radioButtonMale = findViewById(R.id.radioButtonMale);
        numberOfSeen = findViewById(R.id.editTextP);
        createProgressDialog();
        mAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // [END config_signin]

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            database = FirebaseDatabase.getInstance();
            myRef = database.getReference("users").child(user.getUid());
            progressDialog.show();
            getDataInFireBase();

        }
    }

    @Override
    public void onBackPressed() {
        showAlertDialogSignOut();
    }

    public void getDataInFireBase(){
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                if(dataSnapshot.getValue(UserDTO.class) == null){
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    myRef.setValue(new UserDTO(user.getUid().toString(),user.getEmail().toString()
                            ,user.getDisplayName().toString(),"No Data","No Data","No Data"
                            ,true,"No Data",user.getPhotoUrl().toString()));
                }else{
                    userDTO = dataSnapshot.getValue(UserDTO.class);
                    updateUI();
                    if(progressDialog.isShowing()){
                        progressDialog.hide();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Data    ", "Failed to read value.", error.toException());
            }
        });
    }

    public void updateUI(){
        txtName.setText(userDTO.getDisplayname());
        editTextEmail.setText(userDTO.getEmail());
        editTextPhone.setText(userDTO.getPhone());
        editTextAge.setText(userDTO.getAge());
        editTextAdress.setText(userDTO.getAdress());
        editTextInformation.setText(userDTO.getInformation());
        if(userDTO.getGender()){
            radioButtonMale.setChecked(true);
        }else{
            radioButtonFamele.setChecked(true);
        }
        Uri myUri = Uri.parse(userDTO.getPhotoUri());
        Picasso.with(this).load(myUri).into(imageView);
    }

    public void saveDataToFireBase(View view) {
//        Intent intent = new Intent(StatusUser.this,ScrollView.class);
//        startActivity(intent);
        myRef.child("email").setValue(editTextEmail.getText().toString());
        myRef.child("phone").setValue(editTextPhone.getText().toString());
        myRef.child("age").setValue(editTextAge.getText().toString());
        myRef.child("adress").setValue(editTextAdress.getText().toString());
        myRef.child("information").setValue(editTextInformation.getText().toString());
        if(radioButtonMale.isChecked()){
            myRef.child("gender").setValue(true);
        }else{
            myRef.child("gender").setValue(false);
        }
        Toast.makeText(this,"Save is complete !!!",Toast.LENGTH_LONG).show();
    }

    public void createProgressDialog(){
        progressDialog = ProgressDialog.show(this, null, null, true);
        progressDialog.setContentView(R.layout.custom_progress_dialog);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressDialog.hide();
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
            progressDialog.show();
            saveImageFireBase();
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
                    progressDialog.show();
                    imageView.setImageURI(selectedImageUri);
                    saveImageFireBase();
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

    public void saveImageFireBase(){
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
                Toast.makeText(StatusUser.this, "Fail", Toast.LENGTH_LONG).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
                Toast.makeText(StatusUser.this, "Sucess", Toast.LENGTH_LONG).show();

            }
        });final StorageReference ref = storageRef.child("images/" + user.getEmail() + ".jpg");

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
                    myRef.child("photoUri").setValue(downloadUri.toString());

                } else {
                    // Handle failures
                    // ...
                }
            }
        });
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

    public void showAlertDialogSignOut(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Sign Out");
        builder.setMessage("Do you want Sign out ?");
        builder.setCancelable(true); //đây là thuộc tính nếu set false thì khi show dialog lên người dùng click ra bên ngoài dialog thì nó vẫn không bị mất, nếu set true thì sẽ mất khi click vào bất kì đâu ngoài dialog.
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mAuth.signOut();
                LoginManager.getInstance().logOut();
                mGoogleSignInClient.signOut();
                Intent intent = new Intent(StatusUser.this,LoginActivity.class);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    public void intentPlayMusic(View view) {
        Intent intent =  new Intent(StatusUser.this,BottomController.class);
        startActivity(intent);
    }
}
