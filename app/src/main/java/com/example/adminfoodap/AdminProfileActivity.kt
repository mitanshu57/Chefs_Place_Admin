package com.example.adminfoodap

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.adminfoodap.Model.UserModel
import com.example.adminfoodap.databinding.ActivityAdminProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AdminProfileActivity : AppCompatActivity() {
    private val binding : ActivityAdminProfileBinding by lazy {
        ActivityAdminProfileBinding.inflate(layoutInflater)
    }

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var adminReferece: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        auth=FirebaseAuth.getInstance()
        database=FirebaseDatabase.getInstance()
        adminReferece=database.reference.child("users")

        binding.backButton.setOnClickListener {
            finish()
        }

        binding.saveData.setOnClickListener {
            updateUserData()
        }


        binding.name.isEnabled = false
        binding.email.isEnabled = false
        binding.phone.isEnabled = false
        binding.password.isEnabled = false
        binding.address.isEnabled=false
        binding.saveData.isEnabled=false

        var isEnable=false
        binding.editButton.setOnClickListener {
            isEnable=!isEnable
            binding.name.isEnabled = isEnable
            binding.email.isEnabled = isEnable
            binding.phone.isEnabled = isEnable
            binding.password.isEnabled = isEnable
            binding.address.isEnabled=isEnable

            if(isEnable)
            {
                binding.name.requestFocus()
            }
            binding.saveData.isEnabled=isEnable

        }

        retrieveUserData()

    }

    private fun updateUserData() {

        var updateName=binding.name.text.toString()
        var updateEmail= binding.email.text.toString()
        var updatePhone=binding.phone.text.toString()
        var updatePassword=binding.password.text.toString()
        var updateAddress= binding.address.text.toString()
        val currentUserUid=auth.currentUser?.uid
        if(currentUserUid!=null)
        {
            val userReference=adminReferece.child(currentUserUid)
            userReference.child("name").setValue(updateName)
            userReference.child("email").setValue(updateEmail)
            userReference.child("phone").setValue(updatePhone)
            userReference.child("password").setValue(updatePassword)
            userReference.child("address").setValue(updateAddress)
            Toast.makeText(this,"Data Updated",Toast.LENGTH_SHORT).show()
            auth.currentUser?.updateEmail(updateEmail)
            auth.currentUser?.updatePassword(updatePassword)
        }
        else {
            Toast.makeText(this,"Data Not Updated",Toast.LENGTH_SHORT).show()
        }

    }

    private fun retrieveUserData() {
        val currentUserUid=auth.currentUser?.uid
        if(currentUserUid!=null)
        {
            val userReference=adminReferece.child(currentUserUid)
            userReference.addListenerForSingleValueEvent(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists())
                    {
                        var ownerName=snapshot.child("name").getValue()
                        var ownerEmail=snapshot.child("email").getValue()
                        var ownerPhone=snapshot.child("phone").getValue()
                        var ownerPassword=snapshot.child("password").getValue()
                        var ownerAddress=snapshot.child("address").getValue()
                        setDataToTextView(ownerName,ownerEmail,ownerPhone,ownerPassword,ownerAddress)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        }

    }

    private fun setDataToTextView(
        ownerName: Any?,
        ownerEmail: Any?,
        ownerPhone: Any?,
        ownerPassword: Any?,
        ownerAddress: Any?
    ) {
        binding.name.setText(ownerName.toString())
        binding.email.setText(ownerEmail.toString())
        binding.phone.setText(ownerPhone.toString())
        binding.password.setText(ownerPassword.toString())
        binding.address.setText(ownerAddress.toString())

    }
}