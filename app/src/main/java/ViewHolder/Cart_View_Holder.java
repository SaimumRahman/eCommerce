package ViewHolder;

import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerce.R;

import Interface.itemOnClickListener;

public class Cart_View_Holder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView textProductName,textProductPrice,textProductQuantity;
    private itemOnClickListener itemOnClickListener;



    public Cart_View_Holder(@NonNull View itemView) {
        super(itemView);

       textProductName=itemView.findViewById(R.id.product_name_cart);
       textProductPrice=itemView.findViewById(R.id.product_price_cart);
       textProductQuantity=itemView.findViewById(R.id.product_quantity_cart);

    }


    @Override
    public void onClick(View v) {
        itemOnClickListener.onClick(v,getAdapterPosition(),false);
    }

    public void setItemOnClickListener(Interface.itemOnClickListener itemOnClickListener) {
        this.itemOnClickListener = itemOnClickListener;

    }
}
