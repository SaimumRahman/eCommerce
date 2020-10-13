package Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.ecommerce.R;

import Buyer.HomeActivity;
import Buyer.MainActivity;

public class Admin_Home extends AppCompatActivity {
    private Button logoutButton,checkOrderButton,maintain_products_button,admin_approve;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin__home);

        logoutButton=findViewById(R.id.Admin_LogOutButton);
        checkOrderButton=findViewById(R.id.admin_check_order_button);
        maintain_products_button=findViewById(R.id.admin_products_button);
        admin_approve=findViewById(R.id.admin_approve_button);

        maintain_products_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(getApplicationContext(), HomeActivity.class);
                intent.putExtra("Admin","Admin");
                startActivity(intent);


            }
        });


        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
        checkOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), AdminNewOrderActivity.class);
                startActivity(intent);
            }
        });
        admin_approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), Chech_New_Products.class);
                startActivity(intent);
            }
        });

    }
}