package Buyer;

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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerce.Model.Cart;
import com.example.ecommerce.Prevelent.Prevelent;
import com.example.ecommerce.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import ViewHolder.Cart_View_Holder;

public class CartActivity extends AppCompatActivity {
private RecyclerView   recyclerView;
private RecyclerView.LayoutManager layoutManager;
private Button nextProcessButton;
private TextView totalAmounttxt,txtmsg1;
private int overallTotalPrice=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recyclerView=findViewById(R.id.cartList);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        nextProcessButton=findViewById(R.id.nextProcessButton);
        totalAmounttxt=findViewById(R.id.total_price);
        txtmsg1=findViewById(R.id.msg1);

        nextProcessButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent=new Intent(getApplicationContext(), ConfirmFinalOrderActivity.class);
                intent.putExtra("TotalPrice",String.valueOf(overallTotalPrice));
                startActivity(intent);
                finish();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        CheckOrderState();
        totalAmounttxt.setText("Total Price: $" +String.valueOf(overallTotalPrice));


        final DatabaseReference cartListRef= FirebaseDatabase.getInstance().getReference().child("Cart_List");

        FirebaseRecyclerOptions<Cart> options=new FirebaseRecyclerOptions.Builder<Cart>()
                .setQuery(cartListRef.child("User_View").child(Prevelent.currentOnlineUser.getPhoneNumber())
                        .child("Products"),Cart.class).build();

        FirebaseRecyclerAdapter<Cart, Cart_View_Holder> adapter=
                 new FirebaseRecyclerAdapter<Cart, Cart_View_Holder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull Cart_View_Holder holder, int position, @NonNull final Cart model) {
               holder.textProductQuantity.setText("Quantity: " + model.getQuantity());
               holder.textProductPrice.setText("Total Price: " + model.getProduct_Price()+"$");
               holder.textProductName.setText(model.getProduct_Name());

               int oneTypeProductTotalPrice=((Integer.valueOf(model.getProduct_Price()))) *
                       Integer.valueOf(model.getQuantity());
               overallTotalPrice=oneTypeProductTotalPrice+overallTotalPrice;

               holder.itemView.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       CharSequence options []=new CharSequence[]
                               {

                               "Edit",
                                "Remove"

                               };
                       AlertDialog.Builder builder=new AlertDialog.Builder(CartActivity.this);
                       builder.setTitle("Cart Options");

                       builder.setItems(options, new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(DialogInterface dialog, int which) {
                                if(which==0) {

                                    final Intent intent = new Intent(getApplicationContext(), ProductDetailsActivity.class);
                                    intent.putExtra("pid", model.getPid());
                                    startActivity(intent);
                                }
                                if(which == 1){

                                    cartListRef.child("User_View")
                                            .child(Prevelent.currentOnlineUser.getPhoneNumber())
                                            .child("Products")
                                            .child(model.getPid())
                                            .removeValue()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()){
                                                        Toast.makeText(getApplicationContext(),"Items Deleted",Toast.LENGTH_LONG).show();
                                                        Intent intention=new Intent(getApplicationContext(), HomeActivity.class);
                                                        startActivity(intention);
                                                    }
                                                }
                                            });

                                }
                           }
                       });
                       builder.show();
                   }
               });
            }

            @NonNull
            @Override
            public Cart_View_Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_items_layout,parent,false);
                Cart_View_Holder cart_view_holder=new Cart_View_Holder(view);

                return cart_view_holder;
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }
    private  void CheckOrderState(){

        DatabaseReference orderCheckRef=FirebaseDatabase.getInstance().getReference().child("Orders")
                .child(Prevelent.currentOnlineUser.getPhoneNumber());

        orderCheckRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String shippingState=snapshot.child("Status").getValue().toString();
                    String userName=snapshot.child("name").getValue().toString();

                    if(shippingState.equals("shipped")){
                        totalAmounttxt.setText("Dear "+ userName + "\n Order is Shipped Successfully" );
                        recyclerView.setVisibility(View.GONE);

                        txtmsg1.setVisibility(View.VISIBLE);
                        txtmsg1.setText("Congratulation Your Order Has been Placed.");
                        nextProcessButton.setVisibility(View.GONE);

                        }
                    else if (shippingState.equals("not Shipped")){
                        totalAmounttxt.setText("Shipping State = Not Shipped");
                        recyclerView.setVisibility(View.GONE);

                        txtmsg1.setVisibility(View.VISIBLE);
                        nextProcessButton.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}