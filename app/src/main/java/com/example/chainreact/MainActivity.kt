package com.example.chainreact

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random
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

    private fun startGame(mode: String, roomCode: String) {
        val intent = Intent(this, GameActivity::class.java).apply {
            putExtra("MODE", mode)
            putExtra("ROOM_CODE", roomCode)
        }
        startActivity(intent)
    }
}
