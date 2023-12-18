package com.mshukshina.shoppinglistapiapp.presentation

import com.mshukshina.shoppinglistapiapp.retrofit.Authentication
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.mshukshina.shoppinglistapiapp.R
import com.mshukshina.shoppinglistapiapp.databinding.ActivityMainBinding
import com.mshukshina.shoppinglistapiapp.retrofit.Retrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var key: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.generateKeybutton.setOnClickListener {
            Retrofit.retrofitService.getTestKey().enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    if (response.isSuccessful) {
                        key = response.body() ?: ""
                        binding.keyTextView.text = key
                        binding.keyTextView.visibility = View.VISIBLE
                        binding.textView.visibility = View.INVISIBLE
                    }
                }
                override fun onFailure(call: Call<String>, t: Throwable) {
                    binding.keyTextView.text = getString(R.string.key_not_generated)
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
            key = binding.keyEditText.text.toString()

            Retrofit.retrofitService.authenticate(binding.keyEditText.text.toString())
                .enqueue(object : Callback<Authentication> {
                var authenticationResult: Boolean = false

                override fun onResponse(call: Call<Authentication>, response:
                Response<Authentication>) {
                    if (response.isSuccessful) {
                        authenticationResult = response.body()?.success ?: false
                        if(authenticationResult) {
                            val intent = Intent(this@MainActivity,
                                ShoppingListActivity::class.java)
                            intent.putExtra("key", key)
                            startActivity(intent)
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