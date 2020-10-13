package Buyer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerce.Prevelent.Prevelent;
import com.example.ecommerce.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class ResetPasswordActivity extends AppCompatActivity {
private String check="";
private TextView titile,titileQuestion;
private EditText phoneNumber, question1, question2;
private Button verifyButton,viewAnswers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        check=getIntent().getStringExtra("Check");

        titile=findViewById(R.id.reset_title);
        titileQuestion=findViewById(R.id.title);
        phoneNumber=findViewById(R.id.find_phone_number);
        question1=findViewById(R.id.question_1);
        question2=findViewById(R.id.question_2);
        verifyButton=findViewById(R.id.verify_Button);
        viewAnswers=findViewById(R.id.view_ansnwers_button);




    }

    @Override
    protected void onStart() {
        super.onStart();

        phoneNumber.setVisibility(View.GONE);
        viewAnswers.setVisibility(View.GONE);

        if(check.equals("Setting")){

            titile.setText("Set Questions");
            titileQuestion.setText("Please set the answer for the following questions");
            verifyButton.setText("Set");
            viewAnswers.setVisibility(View.VISIBLE);

            verifyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    SetAnswers();

                }
            });
            viewAnswers.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DisplayPreviousAnswers();
                }
            });


        }
        else if(check.equals("Login")){

            phoneNumber.setVisibility(View.VISIBLE);
            verifyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    VerifyInformation();
                }
            });
        }
    }
    private void SetAnswers(){
        String questionNo1= question1.getText().toString().toLowerCase();
        String questionNo2= question2.getText().toString().toLowerCase();

        if(question1.equals("") && question2.equals("")){
            Toast.makeText(getApplicationContext(),"Please Answer both Questions",Toast.LENGTH_LONG).show();
        }
        else {

            DatabaseReference resetRef= FirebaseDatabase.getInstance().getReference().child("Users")
                    .child(Prevelent.currentOnlineUser.getPhoneNumber());

            HashMap<String,Object> resetHashMap=new HashMap<>();
            resetHashMap.put("Answer_1", questionNo1);
            resetHashMap.put("Answer_2", questionNo2);

            resetRef.child("Security_Questions").updateChildren(resetHashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(getApplicationContext(),"Security Questions Updated Successfully",Toast.LENGTH_LONG).show();
                        Intent intent=new Intent(getApplicationContext(), HomeActivity.class);
                        startActivity(intent);
                    }
                }
            });

        }

    }

    private void DisplayPreviousAnswers(){

        DatabaseReference resetRef= FirebaseDatabase.getInstance().getReference().child("Users")
                .child(Prevelent.currentOnlineUser.getPhoneNumber());

        resetRef.child("Security_Questions").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){

                    String ans1=snapshot.child("Answer_1").getValue().toString();
                    String ans2=snapshot.child("Answer_2").getValue().toString();

                    question1.setText(ans1);
                    question2.setText(ans2);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    private void VerifyInformation(){

     final    String phones=phoneNumber.getText().toString();
     final    String questionNo1= question1.getText().toString().toLowerCase();
        final String questionNo2= question2.getText().toString().toLowerCase();

        if(!phones.equals("") && !questionNo1.equals("") && !questionNo2.equals("")){

            final DatabaseReference resetRef= FirebaseDatabase.getInstance().getReference().child("Users")
                    .child(phones);
            resetRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){

                        String phn=snapshot.child("PhoneNumber").getValue().toString();
                        if(snapshot.hasChild("Security_Questions")){

                            String ans1=snapshot.child("Security_Questions").child("Answer_1").getValue().toString();
                            String ans2=snapshot.child("Security_Questions").child("Answer_2").getValue().toString();

                            if(!ans1.equals(questionNo1)){
                                Toast.makeText(getApplicationContext(),"1st answer is wrong",Toast.LENGTH_LONG).show();
                            }
                            else if(!ans2.equals(questionNo2)){
                                Toast.makeText(getApplicationContext(),"2nd answer is wrong",Toast.LENGTH_LONG).show();
                            }
                            else {

                                final AlertDialog.Builder builder=new AlertDialog.Builder(ResetPasswordActivity.this);
                                builder.setTitle("New Password");

                                final EditText newPassword=new EditText(ResetPasswordActivity.this);
                                newPassword.setHint("Type New Password...");
                                builder.setView(newPassword);

                                builder.setPositiveButton("Change", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if(!newPassword.getText().toString().equals("")){
                                            resetRef.child("Passwords").setValue(newPassword.getText().toString())
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if(task.isSuccessful()){
                                                                Toast.makeText(getApplicationContext(),"Password Changed Successfully",Toast.LENGTH_LONG).show();
                                                                Intent intent=new Intent(getApplicationContext(),Login_Activity.class);
                                                                startActivity(intent);
                                                            }
                                                        }
                                                    });


                                        }

                                    }
                                });
                                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                                builder.show();


                            }

                        }
                        else {
                            Toast.makeText(getApplicationContext(),"Security questions not exists",Toast.LENGTH_LONG).show();
                        }

                    }
                    else {
                        Toast.makeText(getApplicationContext(),"Incorrect Phone Number",Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }
        else {
            Toast.makeText(getApplicationContext(),"Please Complete the Form",Toast.LENGTH_LONG).show();
        }

    }

}