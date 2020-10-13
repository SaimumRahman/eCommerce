package Sellers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ecommerce.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SellerLoginActivity extends AppCompatActivity {

    private Button seller_login_button;
    private EditText seller_email_login,seller_password_login;
    private ProgressDialog progressDialog;
    private FirebaseAuth AuthsellerLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_login);

        AuthsellerLogin=FirebaseAuth.getInstance();

        seller_email_login=findViewById(R.id.email_seller_login);
        seller_password_login=findViewById(R.id.password_seller_login);
        progressDialog=new ProgressDialog(this);
        seller_login_button=findViewById(R.id.login_seller_btn);

        seller_login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LoginSeller();

            }
        });

    }

    private void LoginSeller() {

        String slEmail=seller_email_login.getText().toString();
        String slPassword=seller_password_login.getText().toString();

        if(!slEmail.equals("") && !slPassword.equals("")){

            progressDialog.setTitle("Logging In as a Seller");
            progressDialog.setMessage("Please wait, While we are checking Login Credentials");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            AuthsellerLogin.signInWithEmailAndPassword(slEmail,slPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){

                        if(AuthsellerLogin.getCurrentUser().isEmailVerified()){

                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(),"Please Complete the Form",Toast.LENGTH_LONG).show();

                            Intent intent=new Intent(getApplicationContext(), SellerHomeActivities.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        }
                        else{
                            Toast.makeText(getApplicationContext(),"Email not Verified",Toast.LENGTH_LONG).show();
                        }



                    }
                }
            });

        }
     else {
            Toast.makeText(getApplicationContext(),"Please Complete the Form",Toast.LENGTH_LONG).show();
        }
    }
}