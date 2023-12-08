package com.example.letschatbygopalgupta;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class signup extends AppCompatActivity {

    TextView LLogin;
    EditText Username,Email,pass,repass;
    Button signupbtn;
    ImageView img;
    FirebaseAuth auth;
    Uri imageuri;
    String imageUri;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    FirebaseDatabase database;
    FirebaseStorage storage;
    ProgressDialog progressDialog;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Establishing The Account");
        progressDialog.setCancelable(false);

        auth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        database = FirebaseDatabase.getInstance();

        LLogin = findViewById(R.id.Login);
        Username = findViewById(R.id.username);
        Email = findViewById(R.id.email);
        pass = findViewById(R.id.password);
        repass = findViewById(R.id.repassword);
        img = findViewById(R.id.profiledp);
        signupbtn = findViewById(R.id.signupbutton);

        signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String namee = Username.getText().toString();
                String emaill = Email.getText().toString();
                String passs = pass.getText().toString();
                String repasss = repass.getText().toString();
                String statuss = "Hey I'm Using This Application";

                if(TextUtils.isEmpty(namee) || TextUtils.isEmpty(namee) || TextUtils.isEmpty(passs) || TextUtils.isEmpty(repasss)){
                    progressDialog.dismiss();
                    Toast.makeText(signup.this, "Please Enter Valid Information", Toast.LENGTH_SHORT).show();
                }
                else if(!emaill.matches(emailPattern)){
                    progressDialog.dismiss();
                    Email.setError("Type a valid email");
                } else if (passs.length()<6) {
                    progressDialog.dismiss();
                    pass.setError("password must be greater than or equals to six");
                } else if (!passs.equals(repasss)) {
                    progressDialog.dismiss();
                    pass.setError("password doesn't match");
                }
                else{
                    auth.createUserWithEmailAndPassword(emaill,passs).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                String id = task.getResult().getUser().getUid();
                                DatabaseReference reference = database.getReference().child("user").child(id);
                                StorageReference storageReference = storage.getReference().child("upload").child(id);

                                if(imageuri!=null){
                                    storageReference.putFile(imageuri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                            if(task.isSuccessful()){
                                                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                    @Override
                                                    public void onSuccess(Uri uri) {
                                                        imageUri = uri.toString();
                                                        Users users = new Users(id,namee,emaill,passs,repasss,imageUri,statuss);
                                                        reference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if(task.isSuccessful()){
                                                                    progressDialog.show();
                                                                    Intent intent = new Intent(signup.this,MainActivity.class);
                                                                    startActivity(intent);
                                                                    finish();
                                                                }
                                                                else {
                                                                    Toast.makeText(signup.this, "error in creating your account", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        });
                                                    }
                                                });
                                            }else{

                                            }
                                        }
                                    });
                                }else {
                                    String statuss = "Hey I'm Using This Application";
                                    imageUri = "https://firebasestorage.googleapis.com/v0/b/let-s-chat-bce19.appspot.com/o/loginuser.png?alt=media&token=49c567fe-61bf-439b-ae8b-de976c90b042";
                                    Users user = new Users(id,namee,emaill,passs,repasss,imageUri,statuss);
                                    reference.setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                progressDialog.show();
                                                Intent intent = new Intent(signup.this,MainActivity.class);
                                                startActivity(intent);
                                                finish();
                                            }
                                            else {
                                                Toast.makeText(signup.this, "error in creating your account", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            }else{
                                Toast.makeText(signup.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        LLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(signup.this, login.class);
                startActivity(intent);
                finish();
            }
        });

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"),10);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==10){
            if(data!=null){
                imageuri = data.getData();
                img.setImageURI(imageuri);
            }
        }
    }
}