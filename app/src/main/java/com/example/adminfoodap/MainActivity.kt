// File: MainActivity.kt
package com.example.adminfoodap

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.adminfoodap.Model.OrderDetails
import com.example.adminfoodap.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private lateinit var database: FirebaseDatabase
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        enableEdgeToEdge()
        setContentView(binding.root)

        binding.addMenu.setOnClickListener {
            val intent = Intent(this, AddItemActivity::class.java)
            startActivity(intent)
        }
        binding.allItemMenu.setOnClickListener {
            val intent = Intent(this, AllItemActivity::class.java)
            startActivity(intent)
        }
        binding.outForDelivery.setOnClickListener {
            val intent = Intent(this, OutForDeliveryActivity::class.java)
            startActivity(intent)
        }
        binding.profile.setOnClickListener {
            val intent = Intent(this, AdminProfileActivity::class.java)
            startActivity(intent)
        }
        binding.createUser.setOnClickListener {
            val intent = Intent(this, CreateUserActivity::class.java)
            startActivity(intent)
        }
        binding.pendingOrdertv.setOnClickListener {
            val intent = Intent(this, PendingOrderActivity::class.java)
            startActivity(intent)
        }
        binding.logOut.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        pendingOrders()
        completedOrders()
        wholeTimeEarning()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun wholeTimeEarning() {
        val completedOrderReference = database.reference.child("CompletedOrders")
        completedOrderReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var totalEarnings = 0
                for (orderSnapshot in snapshot.children) {
                    val completeOrder = orderSnapshot.getValue(OrderDetails::class.java)
                    val totalPriceString = completeOrder?.totalPrice
                    val amount = totalPriceString?.replace("₹", "")?.trim()?.toIntOrNull()
                    if (amount != null) {
                        totalEarnings += amount
                    } else {
                        Log.d("MainActivity", "Invalid total price for order: ${completeOrder?.itemPushKey}")
                    }
                }
                binding.wholeTimeEarning.text = "₹ $totalEarnings"
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("MainActivity", "Failed to load completed orders: ${error.message}")
                Toast.makeText(this@MainActivity, "Failed to load total earnings.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun completedOrders() {
        val completedOrderReference = database.reference.child("CompletedOrders")
        completedOrderReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val completedOrderItemCount = snapshot.childrenCount.toInt()
                binding.completedOrder.text = completedOrderItemCount.toString()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("MainActivity", "Failed to load completed orders count: ${error.message}")
                Toast.makeText(this@MainActivity, "Failed to load completed orders count.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun pendingOrders() {
        val pendingOrderReference = database.reference.child("OrderDetails")
        pendingOrderReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val pendingOrderItemCount = snapshot.childrenCount.toInt()
                binding.pendingOrders.text = pendingOrderItemCount.toString()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("MainActivity", "Failed to load pending orders count: ${error.message}")
                Toast.makeText(this@MainActivity, "Failed to load pending orders count.", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
