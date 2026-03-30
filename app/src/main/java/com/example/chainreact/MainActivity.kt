package com.example.chainreact
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.btnChainGame).setOnClickListener {
            startActivity(Intent(this, SetupActivity::class.java).putExtra("GAME_TYPE", "CHAIN"))
        }
        findViewById<Button>(R.id.btnLudoGame).setOnClickListener {
            startActivity(Intent(this, SetupActivity::class.java).putExtra("GAME_TYPE", "LUDO"))
        }
    }
}
