package Buyer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ecommerce.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class Register_Activity extends AppCompatActivity {
private Button createAccountButton;
private EditText inputName,inputPassword,inputPhoneNumber;
private ProgressDialog loadingBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_);

        createAccountButton=findViewById(R.id.register_button);
        inputName=findViewById(R.id.register_name_input);
        inputPassword=findViewById(R.id.register_password_input);
        inputPhoneNumber=findViewById(R.id.register_phone_number_input);
        loadingBar=new ProgressDialog(this);

        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateAccount();
            }
        });

    }

    private void CreateAccount() {
        String inputNames= inputName.getText().toString();
        String inputPasswords=inputPassword.getText().toString();
        String inputPhoneNumbers=inputPhoneNumber.getText().toString();

        if(TextUtils.isEmpty(inputNames)){
            Toast.makeText(this, "Please Enter Your Name", Toast.LENGTH_SHORT).show();
        }
     else   if (inputPasswords.length()<6){
            Toast.makeText(this, "Please Enter Your Password more 6 letters", Toast.LENGTH_SHORT).show();
        }
     else   if(TextUtils.isEmpty(inputPasswords)){
            Toast.makeText(this, "Please Enter Your Password", Toast.LENGTH_SHORT).show();
        }
     else   if(TextUtils.isEmpty(inputPhoneNumbers)){
            Toast.makeText(this, "Please Enter Your Phone Number", Toast.LENGTH_SHORT).show();
        }
     else{
         loadingBar.setTitle("Create Account");
         loadingBar.setMessage("Please Wait While we are Checking the Credentials");
         loadingBar.setCanceledOnTouchOutside(false);
         loadingBar.show();

         ValidatePhoneNumber(inputNames,inputPasswords,inputPhoneNumbers);

        }
    }

    private void ValidatePhoneNumber(final String inputNames, final String inputPasswords, final String inputPhoneNumbers) {

        final DatabaseReference RootRef;
        RootRef= FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(!(dataSnapshot.child("Users").child("Phone").exists())){

                    HashMap<String,Object>UserDataMap=new HashMap<>();
                    UserDataMap.put("Name",inputNames);
                    UserDataMap.put("Passwords",inputPasswords);
                    UserDataMap.put("PhoneNumber",inputPhoneNumbers);

                    RootRef.child("Users").child(inputPhoneNumbers).updateChildren(UserDataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(Register_Activity.this,"Congratulations Your Account is Created Successfully", Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();

                                        Intent sendUserToLoginActivity=new Intent(Register_Activity.this,Login_Activity.class);
                                        startActivity(sendUserToLoginActivity);

                                    }
                                    else {
                                        Toast.makeText(Register_Activity.this,"Network Error Please try Again Letter or Check your Internet", Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();
                                        }
                                }
                            });
                }
                else {
                    Toast.makeText(Register_Activity.this,"This"+inputPhoneNumbers+"already Exists", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                    Toast.makeText(Register_Activity.this,"Please User Try again with different Phone number", Toast.LENGTH_SHORT).show();
                    Intent sendUserToMainActivity=new Intent(Register_Activity.this,MainActivity.class);
                    startActivity(sendUserToMainActivity);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


}