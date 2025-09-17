package com.example.adminfoodap

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.adminfoodap.Model.UserModel
import com.example.adminfoodap.databinding.ActivitySignUpBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database

class SignUpActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var email:String
    private lateinit var password:String
    private lateinit var userName:String
    private lateinit var nameOfRestaurant:String
    private lateinit var database: DatabaseReference

    private val binding:ActivitySignUpBinding by lazy {
        ActivitySignUpBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        auth= Firebase.auth
        database=Firebase.database.reference

        val locationList= arrayOf("Jaipur","Delhi","Gurgaon","Noida")
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1,locationList)
        val autoCompleteTextView=binding.listOfLocation
        autoCompleteTextView.setAdapter(adapter)

        binding.btnnreg.setOnClickListener {

            email=binding.etEmail.text.toString().trim()
            userName=binding.etName.text.toString().trim()
            nameOfRestaurant=binding.etResName.text.toString().trim()
            password=binding.etPass.text.toString().trim()

            if(userName.isBlank() || nameOfRestaurant.isBlank() || email.isBlank() || password.isBlank()){
                Toast.makeText(this,"Please fill all details.",Toast.LENGTH_SHORT).show()
            }
            else{
                createAcount(email,password)
            }
        }

        binding.textLogin.setOnClickListener {
            val intent=Intent(this,LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun createAcount(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener {task->
            if(task.isSuccessful){
                Toast.makeText(this,"Account created successfully.",Toast.LENGTH_SHORT).show()
                val intent=Intent(this,LoginActivity::class.java)
                startActivity(intent)
                finish()

                saveUserData()
            }
            else{
                Toast.makeText(this,"Account creation failed.",Toast.LENGTH_SHORT).show()
                Log.d("Error",task.exception.toString())
            }
        }
    }

    private fun saveUserData() {
        email=binding.etEmail.text.toString().trim()
        userName=binding.etName.text.toString().trim()
        nameOfRestaurant=binding.etResName.text.toString().trim()
        password=binding.etPass.text.toString().trim()

        val user=UserModel(userName,nameOfRestaurant,email,password)
        val userId:String= FirebaseAuth.getInstance().currentUser!!.uid
        database.child("users").child(userId).setValue(user)

    }
}