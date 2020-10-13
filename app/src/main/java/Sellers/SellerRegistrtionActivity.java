package Sellers;

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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SellerRegistrtionActivity extends AppCompatActivity {

private Button alreadyHaveAccount_seller_button,sellerRegistterButton;
private EditText sellerName, sellerEmail, sellerPhone,sellerPassword,sellerAddress;
private FirebaseAuth sellerAuth;
private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_registrtion);

        sellerAuth=FirebaseAuth.getInstance();

        alreadyHaveAccount_seller_button=findViewById(R.id.hadAccount_seller_button);
        sellerRegistterButton=findViewById(R.id.register_seller_btn);
        sellerName=findViewById(R.id.name_seller);
        sellerEmail=findViewById(R.id.email_seller);
        sellerPhone=findViewById(R.id.phone_seller);
        sellerAddress=findViewById(R.id.address_seller);
        sellerPassword=findViewById(R.id.password_seller);
        progressDialog=new ProgressDialog(this);


        alreadyHaveAccount_seller_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),SellerLoginActivity.class);
                startActivity(intent);
            }
        });

      sellerRegistterButton.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              RegisterSeller();
          }
      });

    }

    private void RegisterSeller() {

        final String sName=sellerName.getText().toString();
        final String sPhone=sellerPhone.getText().toString();
        final String sEmail=sellerEmail.getText().toString();
        final String sPassword=sellerPassword.getText().toString();
        final String sAddress=sellerAddress.getText().toString();

        if(!sName.equals("") && !sPhone.equals("") && !sPassword.equals("") && !sAddress.equals("") && !sEmail.equals("")){

            progressDialog.setTitle("Creating Seller Account");
            progressDialog.setMessage("Please wait, While we are creating Account");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            sellerAuth.createUserWithEmailAndPassword(sEmail,sPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                  if(task.isSuccessful()){

                      sellerAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                          @Override
                          public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    final  DatabaseReference sellerRef= FirebaseDatabase.getInstance().getReference();

                                    String Seller_Id=sellerAuth.getCurrentUser().getUid();

                                    HashMap<String,Object> sellerHashMap=new HashMap<>();
                                    sellerHashMap.put("Seller_Id",Seller_Id);
                                    sellerHashMap.put("Seller_Name",sName);
                                    sellerHashMap.put("Seller_Password",sPassword);
                                    sellerHashMap.put("Seller_Email",sEmail);
                                    sellerHashMap.put("Seller_Address",sAddress);
                                    sellerHashMap.put("Seller_Phone",sPhone);

                                    sellerRef.child("Seller").child(Seller_Id).updateChildren(sellerHashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){

                                                progressDialog.dismiss();
                                                Toast.makeText(getApplicationContext(),"Registered Successfully",Toast.LENGTH_LONG).show();

                                                Intent intent=new Intent(getApplicationContext(),SellerLoginActivity.class);
                                                startActivity(intent);
                                            }
                                        }
                                    });

                                }
                                else{  Toast.makeText(getApplicationContext(),"Email not Verified, Retry",Toast.LENGTH_LONG).show();}

                          }
                      });



                  }
                }
            });


        }

        else {
            Toast.makeText(getApplicationContext(),"Please the Form",Toast.LENGTH_LONG).show();
        }

    }
}