package com.porfirio.androidprojectpdg.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.porfirio.androidprojectpdg.R
import com.porfirio.androidprojectpdg.databinding.ActivityBienvenidaBinding

class BienvenidaActivity: AppCompatActivity() {

    private lateinit var binding: ActivityBienvenidaBinding

    private var user: FirebaseUser? = null
    private var userId: String? = null
    private var banderaEmailVerificado = true
    private var psw = ""
    private lateinit var firebaseAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBienvenidaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        user = firebaseAuth?.currentUser
        userId = user?.uid

        binding.tvUsuario.text = user?.email

        //Obteniendo el password desde el activity Login
        val bundle: Bundle? = intent.extras
        if(bundle != null) {
            psw = bundle.getString("psw", "")
        }

        //revisamos si el email no está verificado

        if(user?.isEmailVerified != true){
            banderaEmailVerificado = false
            binding.tvCorreoNoVerificado.visibility = View.VISIBLE
            binding.btnReenviarVerificacion.visibility = View.VISIBLE

            binding.btnReenviarVerificacion.setOnClickListener {
                user?.sendEmailVerification()?.addOnSuccessListener {
                    Toast.makeText(this,
                        getString(R.string.CorreoVerificacionEnviado), Toast.LENGTH_SHORT).show()
                }?.addOnFailureListener {
                    Toast.makeText(this,
                        getString(R.string.correoVerificacionNoEnvido), Toast.LENGTH_SHORT).show()
                    Log.d("LOGS", "onFailure: ${it.message}")
                }
            }
        }

        binding.btnAccederApp.setOnClickListener {
            firebaseAuth.signOut()
            startActivity(Intent(this, ReporteActivity::class.java))
            finish()
        }

    }

}
