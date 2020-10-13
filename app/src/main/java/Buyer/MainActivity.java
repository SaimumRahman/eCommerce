package Buyer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerce.Model.Users;
import com.example.ecommerce.Prevelent.Prevelent;
import com.example.ecommerce.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import Sellers.SellerHomeActivities;
import Sellers.SellerRegistrtionActivity;
import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {
private Button joinNowButton, loginButton;
    private ProgressDialog loadingBar;
    private TextView sellerText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        joinNowButton=findViewById(R.id.main_join_now_button);
        sellerText=findViewById(R.id. seller_begin);
        loginButton=findViewById(R.id.main_login_button);
        loadingBar=new ProgressDialog(this);

        sellerText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), SellerRegistrtionActivity.class);
                startActivity(intent);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendUserToLoginActivity=new Intent(MainActivity.this,Login_Activity.class);
                startActivity(sendUserToLoginActivity);

            }
        });
        joinNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendUserToRegisterActivity= new Intent(MainActivity.this,Register_Activity.class);
                startActivity(sendUserToRegisterActivity);
            }
        });
        Paper.init(this);
        String UserPhoneKey=Paper.book().read(Prevelent.UserPhoneKey);
        String UserPasswordKey=Paper.book().read(Prevelent.UserPasswordKey);

        if(UserPhoneKey!="" && UserPasswordKey!=""){
            if(!TextUtils.isEmpty(UserPhoneKey) && !TextUtils.isEmpty(UserPasswordKey)){
                AllowAccess(UserPhoneKey,UserPasswordKey);
                loadingBar.setTitle("Already Logged In");
                loadingBar.setMessage("Please Wait...");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();

            }
        }

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser !=null){

            Intent intent=new Intent(getApplicationContext(), SellerHomeActivities.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();

        }

    }

    private void AllowAccess(final String phone, final String password) {
        final DatabaseReference RootRef;
        RootRef= FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("Users").child(phone).exists()){

                    Users userData=dataSnapshot.child("Users").child(phone).getValue(Users.class);

                    if(userData.getPhoneNumber().equals(phone)){
                        if(userData.getPasswords().equals(password)){

                            Toast.makeText(MainActivity.this,"Logged In Successfully",Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();

                            Intent intent=new Intent(getApplicationContext(), HomeActivity.class);
                            Prevelent.currentOnlineUser=userData;
                            startActivity(intent);
                        }
                        else {
                            loadingBar.dismiss();
                            Toast.makeText(MainActivity.this,"Password is Incorrect",Toast.LENGTH_SHORT).show();

                        }
                    }
                }
                else {
                    Toast.makeText(MainActivity.this,"Account with this"+phone+"number do not Exists",Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                    Toast.makeText(MainActivity.this,"Please Create a new Account",Toast.LENGTH_SHORT).show();

                    Intent sendUserToHomeActivity=new Intent(MainActivity.this,HomeActivity.class);
                    startActivity(sendUserToHomeActivity);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}