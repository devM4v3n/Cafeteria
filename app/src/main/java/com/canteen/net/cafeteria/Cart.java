package com.canteen.net.cafeteria;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.canteen.net.cafeteria.Common.Common;
import com.canteen.net.cafeteria.Database.Database;
import com.canteen.net.cafeteria.Model.Order;
import com.canteen.net.cafeteria.Model.Request;
import com.canteen.net.cafeteria.ViewHolder.CartAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class Cart extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference requests;

    TextView txtTotalPrice;
    Button btnPlace;

    List<Order> cart = new ArrayList<>();
    CartAdapter adapter;

    static List<Request> requestList = new ArrayList<>();
    static float total;

    ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        database = FirebaseDatabase.getInstance();
        requests = database.getReference("Requests");

        recyclerView =(RecyclerView) findViewById(R.id.listCart);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        txtTotalPrice = (TextView)findViewById(R.id.total);
        btnPlace = (Button)findViewById(R.id.btnPlaceOrder);

        btnPlace.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                showAlertDialog();
            }
        });

        loadListFood();
    }

     private void showAlertDialog() {
        final AlertDialog.Builder adb = new AlertDialog.Builder(Cart.this);
        adb.setTitle("Please Fill the Data!");
        adb.setMessage("Your Address : ");
        final EditText etadd = new EditText(Cart.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
        LinearLayout.LayoutParams.MATCH_PARENT,
        LinearLayout.LayoutParams.MATCH_PARENT
        );
        etadd.setLayoutParams(lp);
        adb.setView(etadd);
        adb.setIcon(R.drawable.ic_shopping_cart_black_24dp);

        adb.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Request request = new Request(
                        Common.currentUser.getPhone(),
                        Common.currentUser.getName(),
                        etadd.getText().toString(),
                        txtTotalPrice.getText().toString(),
                        cart
                );

                requestList.add(request);

                requests.child(String.valueOf(System.currentTimeMillis()))
                        .setValue(request);

                new Database(getBaseContext()).cleanCart();
                Toast.makeText(Cart.this, "Thank You, Order has been placed",Toast.LENGTH_SHORT).show();

                finish();
                Intent intent = new Intent();
                PendingIntent pendingIntent = PendingIntent.getActivity(Cart.this,0,intent,0);
                Notification.Builder notificationBuilder = new Notification.Builder(Cart.this)
                        .setTicker("Order Placed").setContentTitle("Order Placed")
                        .setContentText("Your order is processing now").setSmallIcon(R.drawable.icon_cafeteria)
                        .setContentIntent(pendingIntent);
                Notification notification = notificationBuilder.build();
                notification.flags = Notification.FLAG_AUTO_CANCEL;
                NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                assert nm != null;
                nm.notify(0,notification);

            }
        });

        adb.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        adb.show();
    }

    private void loadListFood() {
        cart = new Database(this).getCarts();
        adapter = new CartAdapter(cart, this);
        recyclerView.setAdapter(adapter);

        int total = 0;
        for(Order order:cart)
            total+=(Integer.parseInt(order.getPrice()))*(Integer.parseInt(order.getQuantity()));
        Locale locale = new Locale("en","IN");
        NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);

        txtTotalPrice.setText(fmt.format(total));
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if(item.getTitle().equals(Common.DELETE)){
            deleteFoodItem(item.getOrder());
        }
        return super.onContextItemSelected(item);
    }

    private void deleteFoodItem(int ord) {
        cart = new Database(this).getCarts();
        String order1 = cart.get(ord).getProductId();
        for(Order order:cart){
            if(order.getProductId().equals(order1)){
                new Database(getBaseContext()).removeFromCart(order1);
            }
        }
        loadListFood();
    }
}
