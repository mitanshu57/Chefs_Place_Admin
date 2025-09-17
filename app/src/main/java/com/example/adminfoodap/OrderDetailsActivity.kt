package com.example.adminfoodap

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.adminfoodap.Adapter.OrderDetailsAdapter
import com.example.adminfoodap.Model.OrderDetails
import com.example.adminfoodap.databinding.ActivityOrderDetailsBinding

class OrderDetailsActivity : AppCompatActivity() {
    private val binding: ActivityOrderDetailsBinding by lazy {
        ActivityOrderDetailsBinding.inflate(layoutInflater)
    }

    private var userName: String? = null
    private var address: String? = null
    private var phoneNumber: String? = null
    private var totalPrice: String? = null

    private var foodNames: ArrayList<String> = arrayListOf()
    private var foodImages: ArrayList<String> = arrayListOf()
    private var foodQuantities: ArrayList<Int> = arrayListOf()
    private var foodPrices: ArrayList<String> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.backButton.setOnClickListener {
            finish()
        }

        getDataFromIntent()
    }

    private fun getDataFromIntent() {
        val receiveOrderDetails = intent.getParcelableExtra<OrderDetails>("userOrderDetails")
        receiveOrderDetails?.let { orderDetails ->
            userName = orderDetails.userName
            foodNames = orderDetails.foodNames as ArrayList<String>
            foodImages = orderDetails.foodImages as ArrayList<String>
            foodQuantities = orderDetails.foodQuantities as ArrayList<Int>
            foodPrices = orderDetails.foodPrices as ArrayList<String>
            address = orderDetails.address
            phoneNumber = orderDetails.phoneNumber
            totalPrice = orderDetails.totalPrice

            setUserDetails()
            setAdapter()
        }
    }

    private fun setAdapter() {
        binding.orderDetailsRV.layoutManager = LinearLayoutManager(this)
        val adapter = OrderDetailsAdapter(this, foodNames, foodImages, foodQuantities, foodPrices)
        binding.orderDetailsRV.adapter = adapter
    }

    private fun setUserDetails() {
        binding.name.text = userName
        binding.address.text = address
        binding.phone.text = phoneNumber
        binding.totalAmount.text = totalPrice
    }
}
