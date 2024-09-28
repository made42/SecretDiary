package org.hyperskill.secretdiary

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var tvTitle: TextView
    private lateinit var etPin: EditText
    private lateinit var btnLogin: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        tvTitle = findViewById(R.id.tvTitle)
        etPin = findViewById(R.id.etPin)
        btnLogin = findViewById(R.id.btnLogin)

        btnLogin.setOnClickListener {
            if (etPin.text.toString() == "1234") {
                startActivity(Intent(this, MainActivity::class.java))
                this.finish()
            } else {
                etPin.error = getString(R.string.pin_wrong)
            }
        }
    }
}