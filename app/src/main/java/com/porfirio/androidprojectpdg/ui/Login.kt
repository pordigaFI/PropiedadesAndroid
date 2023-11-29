package com.porfirio.androidprojectpdg.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.porfirio.androidprojectpdg.R
import com.porfirio.androidprojectpdg.databinding.ActivityLoginBinding

class Login : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    //Para Firebase
    private lateinit var firebaseAuth: FirebaseAuth

    //Inicialización variables
    private var email = ""
    private var contrasenia = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.btnLogin.setOnClickListener {

            //se llama a la función validaCampos para revisar que se cumple con las restricciones
            if(!validaCampos()) return@setOnClickListener

            binding.progressBar.visibility = View.VISIBLE

            //autenticando al usuario, se llama a la función autenticaUsuario
            autenticaUsuario(email, contrasenia)
        }

        binding.btnRegistrarse.setOnClickListener {
            if(!validaCampos()) return@setOnClickListener

            binding.progressBar.visibility = View.VISIBLE

            //Registrando al usuario
            firebaseAuth.createUserWithEmailAndPassword(email, contrasenia).addOnCompleteListener { authResult->
                if(authResult.isSuccessful){
                    //Enviar correo para verificación de email
                    val user_fb = firebaseAuth.currentUser
                    user_fb?.sendEmailVerification()?.addOnSuccessListener {
                        Toast.makeText(this, getString(R.string.correoVerifEnviado), Toast.LENGTH_SHORT).show()
                    }?.addOnFailureListener {
                        Toast.makeText(this, getString(R.string.correoVerifNoEnviado), Toast.LENGTH_SHORT).show()
                    }

                    Toast.makeText(this, getString(R.string.usuarioCreado), Toast.LENGTH_SHORT).show()

                    val intent = Intent(this, BienvenidaActivity::class.java)
                    intent.putExtra("psw", contrasenia)
                    startActivity(intent)
                    finish()


                }else{
                    binding.progressBar.visibility = View.GONE
                    manejaErrores(authResult)
                }
            }
        }

        binding.tvRestablecerPassword.setOnClickListener {
            val resetMail = EditText(it.context)
            resetMail.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS

            val passwordResetDialog = AlertDialog.Builder(it.context)
                .setTitle(getString(R.string.restablecerContrasenia))
                .setMessage(getString(R.string.recibirEnlace))
                .setView(resetMail)
                .setPositiveButton(getString(R.string.enviar)) { _, _ ->
                    val mail = resetMail.text.toString()
                    if (mail.isNotEmpty()) {
                        firebaseAuth.sendPasswordResetEmail(mail).addOnSuccessListener {
                            Toast.makeText(
                                this,
                                getString(R.string.enlaceEnviado),
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }.addOnFailureListener {
                            Toast.makeText(
                                this,
                                "getString(R.string.enlaceNoEnviado) ${it.message}",
                                Toast.LENGTH_SHORT
                            ).show() //it tiene la excepción
                        }
                    } else {
                        Toast.makeText(
                            this,
                            getString(R.string.ingresarCorreo),
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }.setNegativeButton(getString(R.string.cancelar)) { dialog, _ ->
                    dialog.dismiss()
                }
                .create()
                .show()
        }
    }

    private fun validaCampos(): Boolean{
        email = binding.tietEmail.text.toString().trim() //para que quite espacios en blanco
        contrasenia = binding.tietContrasenia.text.toString().trim()

        if(email.isEmpty()){
            binding.tietEmail.error = getString(R.string.correoRequerido)
            binding.tietEmail.requestFocus()
            return false
        }

        if(contrasenia.isEmpty() || contrasenia.length < 6){
            binding.tietContrasenia.error = getString(R.string.passNoCumpleTamanio)
            binding.tietContrasenia.requestFocus()
            return false
        }

        return true
    }

    private fun manejaErrores(task: Task<AuthResult>){
        var errorCode = ""

        try{
            errorCode = (task.exception as FirebaseAuthException).errorCode
        }catch(e: Exception){
            e.printStackTrace()
        }

        when(errorCode){
            "ERROR_INVALID_EMAIL" -> {
                Toast.makeText(this, getString(R.string.formatoCorreoIncorrecto), Toast.LENGTH_SHORT).show()
                binding.tietEmail.error = getString(R.string.formatoCorreoIncorrecto2)
                binding.tietEmail.requestFocus()
            }
            "ERROR_WRONG_PASSWORD" -> {
                Toast.makeText(this, getString(R.string.contraseniaNoValida), Toast.LENGTH_SHORT).show()
                binding.tietContrasenia.error = getString(R.string.contraseniaNoValida2)
                binding.tietContrasenia.requestFocus()
                binding.tietContrasenia.setText("")

            }
            "ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL" -> {
                //An account already exists with the same email address but different sign-in credentials. Sign in using a provider associated with this email address.
                Toast.makeText(this, getString(R.string.correoDuplicadoConDatosDiferentes), Toast.LENGTH_SHORT).show()
            }
            "ERROR_EMAIL_ALREADY_IN_USE" -> {
                Toast.makeText(this, getString(R.string.correoEnUsoOtraCuenta), Toast.LENGTH_LONG).show()
                binding.tietEmail.error = (getString(R.string.correoEnUsoOtraCuenta))
                binding.tietEmail.requestFocus()
            }
            "ERROR_USER_TOKEN_EXPIRED" -> {
                Toast.makeText(this, getString(R.string.sesionxpiro), Toast.LENGTH_LONG).show()
            }
            "ERROR_USER_NOT_FOUND" -> {
                Toast.makeText(this, getString(R.string.noExisteUsuario), Toast.LENGTH_LONG).show()
            }
            "ERROR_WEAK_PASSWORD" -> {
                Toast.makeText(this, getString(R.string.contraseniaInvalida), Toast.LENGTH_LONG).show()
                binding.tietContrasenia.error = getString(R.string.tamanioContrasenia)
                binding.tietContrasenia.requestFocus()
            }
            "NO_NETWORK" -> {
                Toast.makeText(this, getString(R.string.redNoDisponible), Toast.LENGTH_LONG).show()
            }
            else -> {
                Toast.makeText(this, getString(R.string.autenticacionNoExitosa), Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun autenticaUsuario(usr: String, psw: String){

        firebaseAuth.signInWithEmailAndPassword(usr, psw).addOnCompleteListener { authResult ->
            if(authResult.isSuccessful){
                Toast.makeText(this, getString(R.string.autenticacionExito), Toast.LENGTH_SHORT).show()

                val intent = Intent(this, BienvenidaActivity::class.java)
                intent.putExtra("psw", psw)
                startActivity(intent)
                finish()
            }else{
                binding.progressBar.visibility = View.GONE
                //Se llama a la función manejaErrores
                manejaErrores(authResult)
            }
        }
    }
}

