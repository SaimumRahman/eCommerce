package Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

//import com.example.ecommerce.Model.AdminOrders;
import com.example.ecommerce.Model.AdminOrders;
import com.example.ecommerce.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import ViewHolder.AdminOrderViewHolder;

public class AdminNewOrderActivity extends AppCompatActivity {
private RecyclerView ordersList;
private DatabaseReference ordersRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_new_order);

        ordersRef= FirebaseDatabase.getInstance().getReference().child("Orders");

        ordersList=findViewById(R.id.cartList_orders);
        ordersList.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<AdminOrders> ordersOptions=new FirebaseRecyclerOptions.Builder<AdminOrders>()
                .setQuery(ordersRef,AdminOrders.class).build();

        FirebaseRecyclerAdapter<AdminOrders,AdminOrderViewHolder> adapter=new FirebaseRecyclerAdapter<AdminOrders, AdminOrderViewHolder>(ordersOptions) {
            @Override
            protected void onBindViewHolder(@NonNull AdminOrderViewHolder holder, final int position, @NonNull final AdminOrders model) {
                holder.userName.setText("Name: "+ model.getName());
                holder.phoneNumber.setText("Phone_Number: "+ model.getPhone_Number());
                holder.totalPrice.setText("Total_Amount: "+ model.getTotal_Amount());
                holder.dateTime.setText("Date: "+ model.getDate() + "Time: "+model.getTime());
                holder.addressCity.setText("Address: "+ model.getAddress()+ "City: " + model.getCity());
                holder.userName.setText("Name: "+ model.getName());

                holder.showAllProducts.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String uid=getRef(position).getKey();
                        Intent intent=new Intent(getApplicationContext(),adminUserProductsActivity.class);
                        intent.putExtra("uid",uid);
                        startActivity(intent);

                    }
                });

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        CharSequence charSequence[]=new CharSequence[]
                                {
                                        "Yes",
                                        "No"
                                };
                        AlertDialog.Builder builder=new AlertDialog.Builder(AdminNewOrderActivity.this);
                        builder.setTitle("Have You Shipped these Products ?");
                        builder.setItems(charSequence, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(which==0){
                                    String uid=getRef(position).getKey();
                                    RemoveOrder(uid);
                                }
                                else {finish();}
                            }
                        });
                        builder.show();
                    }
                });
            }

            @NonNull
            @Override
            public AdminOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.orders_layout,parent,false);
                return new AdminOrderViewHolder(view);
            }
        };
        ordersList.setAdapter(adapter);
        adapter.startListening();

    }

    private void RemoveOrder(String uid) {

    ordersRef.child(uid).removeValue();

    }

}