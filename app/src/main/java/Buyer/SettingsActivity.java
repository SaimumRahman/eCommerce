package Buyer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerce.Prevelent.Prevelent;
import com.example.ecommerce.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity {

    private CircleImageView profileImageView;
    private EditText fullNameEditText,userPhoneEditText,addressEditText;
    private TextView profileChangeTextButton,closeTextButton,saveTextButton;
    private Button securityQuestionsButton;

    private Uri imageUri;
    private String myUrl="";
    private StorageReference storageProfilePictureRef;
    private String checker= "";
    private StorageTask uploadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        storageProfilePictureRef= FirebaseStorage.getInstance().getReference().child("Profile Pictures");

        profileImageView=findViewById(R.id.settings_profile_image);
        fullNameEditText=findViewById(R.id.settings_full_name);
        userPhoneEditText=findViewById(R.id.settings_phone_number);
        addressEditText=findViewById(R.id.settings_address);
        profileChangeTextButton=findViewById(R.id.profile_image_change_btn);
        closeTextButton=findViewById(R.id.close_settings);
        securityQuestionsButton=findViewById(R.id.security_questions_button);
        saveTextButton=findViewById(R.id.update_account_settings_button);

        UserInfoDisplay(profileImageView,fullNameEditText,userPhoneEditText,addressEditText);

        closeTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        saveTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checker.equals("Clicked")){
                    UserInfoSaved();
                }
                else{
                    updateOnlyUserInfo();
                }
            }
        });
        profileChangeTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checker="Clicked";

                CropImage.activity(imageUri).setAspectRatio(1,1)
                        .start(SettingsActivity.this);

            }
        });

        securityQuestionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),ResetPasswordActivity.class);
                intent.putExtra("Check","Setting");
                startActivity(intent);
            }
        });
    }

    private void updateOnlyUserInfo() {

        DatabaseReference databaseReference=FirebaseDatabase.getInstance().
                getReference().child("Users");

        HashMap<String, Object> userMap=new HashMap<>();
        userMap.put("Name", fullNameEditText.getText().toString());
        userMap.put("address", addressEditText.getText().toString());
        userMap.put("PhoneOrder", userPhoneEditText.getText().toString());
        databaseReference.child(Prevelent.currentOnlineUser.getPhoneNumber()).updateChildren(userMap);


        startActivity(new Intent(SettingsActivity.this, HomeActivity.class));
        Toast.makeText(getApplicationContext(), "Profile Info Updated Successfully",Toast.LENGTH_LONG).show();
        finish();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode==RESULT_OK && data!=null){

            CropImage.ActivityResult result=CropImage.getActivityResult(data);
            imageUri=result.getUri();
            profileImageView.setImageURI(imageUri);

        }
        else{
            Toast.makeText(this,"Error Please try Again",Toast.LENGTH_LONG).show();
            startActivity(new Intent(SettingsActivity.this,SettingsActivity.class));
            finish();

        }

    }

    private void UserInfoSaved() {

        if(TextUtils.isEmpty(fullNameEditText.getText().toString())){
            Toast.makeText(this,"Please Enter Name",Toast.LENGTH_LONG).show();
        }
       else if(TextUtils.isEmpty(addressEditText.getText().toString())){
            Toast.makeText(this,"Please Enter address",Toast.LENGTH_LONG).show();
        }
       else if(TextUtils.isEmpty(userPhoneEditText.getText().toString())){
            Toast.makeText(this,"Please Enter Phone Number",Toast.LENGTH_LONG).show();
        }
       else if(checker.equals("Clicked")){
           UploadImage();
        }

    }

    private void UploadImage() {

        final ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Update Profile");
        progressDialog.setMessage("Please Wait, While Updating");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        if(imageUri!=null){
            final StorageReference fileRef=storageProfilePictureRef.child(Prevelent.currentOnlineUser.getPhoneNumber()+"jpg");
            uploadTask=fileRef.putFile(imageUri);
            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {

                    if(!task.isSuccessful()){
                        throw task.getException();

                    }

                    return fileRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful()){

                      Uri downloadUrl=task.getResult();
                      myUrl=downloadUrl.toString();

                      DatabaseReference databaseReference=FirebaseDatabase.getInstance().
                              getReference().child("Users");

                        HashMap<String, Object> userMap=new HashMap<>();
                        userMap.put("Name", fullNameEditText.getText().toString());
                        userMap.put("address", addressEditText.getText().toString());
                        userMap.put("PhoneOrder", userPhoneEditText.getText().toString());
                        userMap.put("image", myUrl);
                        databaseReference.child(Prevelent.currentOnlineUser.getPhoneNumber()).updateChildren(userMap);

                        progressDialog.dismiss();

                        startActivity(new Intent(SettingsActivity.this, HomeActivity.class));
                        Toast.makeText(getApplicationContext(), "Profile Info Updated Successfully",Toast.LENGTH_LONG).show();
                        finish();

                    }
                    else {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Error: ",Toast.LENGTH_LONG).show();
                    }
                }
            });

        }
        else {
            Toast.makeText(getApplicationContext(), "Image is not Selected",Toast.LENGTH_LONG).show();
        }

    }

    private void UserInfoDisplay(final CircleImageView profileImageView, final EditText fullNameEditText, final EditText userPhoneEditText, final EditText addressEditText) {

        DatabaseReference UserRef= FirebaseDatabase.getInstance().getReference().child("Users").child(Prevelent.currentOnlineUser.getPhoneNumber());
        UserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()){

                    if(snapshot.child("image").exists()){

                        String image=snapshot.child("image").getValue().toString();
                        String name=snapshot.child("Name").getValue().toString();
                        String phone=snapshot.child("PhoneNumber").getValue().toString();
                        String address=snapshot.child("address").getValue().toString();

                        Picasso.get().load(image).into(profileImageView);
                        fullNameEditText.setText(name);
                        userPhoneEditText.setText(phone);
                        addressEditText.setText(address);

                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}