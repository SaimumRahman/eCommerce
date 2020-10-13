package Buyer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerce.Prevelent.Prevelent;
import com.example.ecommerce.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ConfirmFinalOrderActivity extends AppCompatActivity {
private EditText nameEditText,phoneEditText,addressEditText,cityEditText;
private Button confirmOrderButton;
private String totalAmount="";
private TextView showTotalPrice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_final_order);

        totalAmount=getIntent().getStringExtra("TotalPrice");

        nameEditText=findViewById(R.id.shipment_name);
        phoneEditText=findViewById(R.id.shipment_phone_number);
        addressEditText=findViewById(R.id.shipment_address);
        cityEditText=findViewById(R.id.shipment_city);
        confirmOrderButton=findViewById(R.id.confirm_final_order_button);
        showTotalPrice=findViewById(R.id.totalfinalAmount);

        showTotalPrice.setText("Total Payable Amount: "+totalAmount+"$");

        confirmOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckInputFields();
            }
        });

    }

    private void CheckInputFields() {

        if(TextUtils.isEmpty(nameEditText.getText().toString())){
            Toast.makeText(getApplicationContext(),"Please Write Your Full Name",Toast.LENGTH_LONG).show();
        }
        else if(TextUtils.isEmpty(phoneEditText.getText().toString())){
            Toast.makeText(getApplicationContext(),"Please Phone Number",Toast.LENGTH_LONG).show();
        }
        else if(TextUtils.isEmpty(addressEditText.getText().toString())){
                    Toast.makeText(getApplicationContext(),"Please Give Your Address",Toast.LENGTH_LONG).show();
                }
         else if(TextUtils.isEmpty(cityEditText.getText().toString())){
                            Toast.makeText(getApplicationContext(),"Please Give Your City Name",Toast.LENGTH_LONG).show();
                        }

         else {

             ConfirmOrder();

        }

    }

    private void ConfirmOrder() {

      final   String saveCurrentTime, saveCurrentDate;

        Calendar callForDate=Calendar.getInstance();

        SimpleDateFormat currentDate=new SimpleDateFormat("MM dd, yyyy");
        saveCurrentDate=currentDate.format(callForDate.getTime());

        SimpleDateFormat currentTime=new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime=currentTime.format(callForDate.getTime());

        DatabaseReference confirmOrderRef= FirebaseDatabase.getInstance().getReference().child("Orders")
                .child(Prevelent.currentOnlineUser.getPhoneNumber());

        HashMap<String,Object> orders=new HashMap<>();
        orders.put("Total_Amount", totalAmount);
        orders.put("name", nameEditText.getText().toString());
        orders.put("Phone_Number", phoneEditText.getText().toString());
        orders.put("City", cityEditText.getText().toString());
        orders.put("Address", addressEditText.getText().toString());
        orders.put("Date", saveCurrentDate);
        orders.put("Time", saveCurrentTime);
        orders.put("Status","not Shipped");

        confirmOrderRef.updateChildren(orders).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){

                    FirebaseDatabase.getInstance().getReference().child("Cart_List")
                            .child("User_View").child(Prevelent.currentOnlineUser.getPhoneNumber())
                            .removeValue()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                             if(task.isSuccessful()){
                                 Toast.makeText(getApplicationContext(),"Final Order is Placed Successfully",Toast.LENGTH_LONG).show();

                                 Intent intent=new Intent(getApplicationContext(), HomeActivity.class);
                                 intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                 startActivity(intent);
                                 finish();
                             }
                                }
                            });

                }
            }
        });


    }
}