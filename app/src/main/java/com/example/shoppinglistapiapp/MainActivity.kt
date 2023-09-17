package com.example.shoppinglistapiapp

import com.example.shoppinglistapiapp.retrofit.Authentication
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.shoppinglistapiapp.databinding.ActivityMainBinding
import com.example.shoppinglistapiapp.retrofit.Retrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.generateKeybutton.setOnClickListener {
            Retrofit.retrofitService.getTestKey().enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    if (response.isSuccessful) {
                        binding.keyTextView.text = response.body()
                        binding.keyTextView.visibility = View.VISIBLE
                        binding.textView.visibility = View.INVISIBLE
                    }
                }
                override fun onFailure(call: Call<String>, t: Throwable) {
                    binding.keyTextView.text = t.message
                    binding.keyTextView.visibility = View.VISIBLE
                    binding.textView.visibility = View.INVISIBLE
                }
            })
        }

        binding.authenticateButton.setOnClickListener {
            if(binding.keyEditText.text.isEmpty()) {
                Toast.makeText(this, "Please enter a key", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            Retrofit.retrofitService.authenticate(binding.keyEditText.text.toString()).enqueue(object : Callback<Authentication> {
                var authenticationResult: Boolean = false

                override fun onResponse(call: Call<Authentication>, response: Response<Authentication>) {
                    if (response.isSuccessful) {
                        authenticationResult = response.body()?.success ?: false
                        if(authenticationResult) {
                            startActivity(Intent(this@MainActivity,
                                ShoppingListActivity::class.java))
                        }
                    } else {
                        binding.textView.text = response.message()
                        binding.textView.visibility = View.VISIBLE
                    }
                }
                override fun onFailure(call: Call<Authentication>, t: Throwable) {
                    binding.textView.text = t.message
                    binding.textView.visibility = View.VISIBLE
                    binding.keyTextView.visibility = View.INVISIBLE
                }
            })
        }
    }
}