package ViewHolder;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerce.R;


public class AdminOrderViewHolder extends RecyclerView.ViewHolder {

    public TextView userName,phoneNumber, totalPrice,addressCity,dateTime;
    public Button showAllProducts;

    public AdminOrderViewHolder(@NonNull View itemView) {
        super(itemView);

        userName=itemView.findViewById(R.id.orders_username);
        phoneNumber=itemView.findViewById(R.id.order_phone_number);
        totalPrice=itemView.findViewById(R.id.orders_total_price);
        addressCity=itemView.findViewById(R.id.order_address_city);
        dateTime=itemView.findViewById(R.id.date_time_order);
        showAllProducts=itemView.findViewById(R.id.showAll_products_orders);

    }
}
