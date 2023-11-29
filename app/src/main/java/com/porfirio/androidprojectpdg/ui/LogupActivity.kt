/*package com.porfirio.androidprojectpdg.ui

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.porfirio.androidprojectpdg.application.PropiedadesRFApp
import com.porfirio.androidprojectpdg.data.PropiedadRepository
import com.porfirio.androidprojectpdg.data.remote.model.PropiedadDetailDto
import com.porfirio.androidprojectpdg.databinding.ActivityLogupBinding
import kotlinx.coroutines.launch
import java.io.IOException


class LogupActivity(
    private val newRegistro: Boolean = true,
    //inicialización de variables
    private var registro: PropiedadDetailDto = PropiedadDetailDto(
        name = "",
        surname = "",
        user = "",
        pass = "",
        email = ""
    ),
    private val updateUI: () -> Unit,
    private val message: (String) -> Unit
): DialogFragment(){
    private var _binding: ActivityLogupBinding? = null  //Tomamos la info de activity_logup.xml
    private val binding get() = _binding!!
    private lateinit var builder: AlertDialog.Builder
    private lateinit var dialog: Dialog
    private var saveButton: Button? = null
    private lateinit var repository: PropiedadRepository

    //Configuramos dialogo inicial
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog{
        _binding = ActivityLogupBinding.inflate(requireActivity().layoutInflater)
        repository = (requireContext().applicationContext as PropiedadesRFApp).repository
        builder = AlertDialog.Builder(requireContext())

        binding.apply{
            tietName.setText(registro.name)
            tietSurname.setText(registro.surname)
            tietUser.setText(registro.user)
            tietPass.setText(registro.pass)
            tietEmailHelp.setText(registro.email)
        }

        dialog = if (newRegistro){
            buildDialog("@string/guardar", "@string/cancelar",{
                registro.name = binding.tietName.text.toString()
                registro.surname= binding.tietSurname.text.toString()
                registro.user = binding.tietUser.text.toString()
                registro.pass= binding.tietPass.text.toString()
                registro.email= binding.tietEmailHelp.text.toString()

                try{
                    lifecycleScope.launch {
                        repository.insertRegistro(registro)
                    }

                    message("@string/guardar_con_exito")

                    //Se actualiza la UI
                    updateUI()

                } catch (e: IOException){
                    e.printStackTrace()
                    message("@string/error_al_guardar")
                }
            }, {
                //Cancelar
                message("@string/accion_cancelada")
            })
        } else{
            buildDialog("@string/actualizar", "@string/borrar",{
                registro.name = binding.tietName.text.toString()
                registro.surname= binding.tietSurname.text.toString()
                registro.user = binding.tietUser.text.toString()
                registro.pass= binding.tietPass.text.toString()
                registro.email= binding.tietEmailHelp.text.toString()

                try{
                    lifecycleScope.launch {
                        repository.updateRegistro(registro)
                    }

                    message("@string/actualizacion_con_exito")

                    //Se actualiza la UI
                    updateUI()

                } catch (e: IOException){
                    e.printStackTrace()
                    message("@string/error_actualizar")
                }
            }, {
                //Delete
               AlertDialog.Builder(requireContext())
                   .setTitle("@string/confirmacion")
                   .setMessage("@string/realmente_borrar ${registro.name}?")
                   .setPositiveButton("@string/aceptar"){_,_ ->
                       try{
                           lifecycleScope.launch {
                               repository.deleteRegistro(registro)
                           }
                           message("@string/borrado_con_exito")

                           //Actualizar la UI
                           updateUI()
                       } catch (e: IOException){
                           e.printStackTrace()
                           message("@string/error_eliminar")
                       }
                   }
                   .setNegativeButton("@string/cancelar"){ dialog, _ ->
                       dialog.dismiss()
                   }
                   .create()
                   .show()
            })

        }
        return dialog
    }

    //Función para destruir el fragment
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    //Función para iniciar el dialogo
    override fun onStart() {
        super.onStart()

        val alertDialog =
            dialog as AlertDialog   //se usa para implementar el método getButton
        saveButton = alertDialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE)
        saveButton?.isEnabled = false

        binding.tietName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                TODO("Not yet implemented")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                TODO("Not yet implemented")
            }

            override fun afterTextChanged(s: Editable?) {
                saveButton?.isEnabled = validateFields()
            }
        })
        binding.tietSurname.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                TODO("Not yet implemented")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                TODO("Not yet implemented")
            }

            override fun afterTextChanged(s: Editable?) {
                saveButton?.isEnabled = validateFields()
            }
        })
        binding.tietUser.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                TODO("Not yet implemented")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                TODO("Not yet implemented")
            }

            override fun afterTextChanged(s: Editable?) {
                saveButton?.isEnabled = validateFields()
            }
        })

        binding.tietPass.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                TODO("Not yet implemented")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                TODO("Not yet implemented")
            }

            override fun afterTextChanged(s: Editable?) {
                saveButton?.isEnabled = validateFields()
            }
        })

        binding.tietEmailHelp.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                TODO("Not yet implemented")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                TODO("Not yet implemented")
            }

            override fun afterTextChanged(s: Editable?) {
                saveButton?.isEnabled = validateFields()
            }
        })
    }

    private fun validateFields()=
        (binding.tietName.text.toString().isNotEmpty() && binding.tietSurname.text.toString()
            .isNotEmpty() && binding.tietUser.text.toString().isNotEmpty() && binding.tietPass.text.toString()
                .isNotEmpty() && binding.tietEmailHelp.text.toString().isNotEmpty())

    private fun buildDialog(
        btn1Text : String,
        btn2Text : String,
        positiveButton: () -> Unit,
        negativeButton: () -> Unit
    ): Dialog =
        builder.setView(binding.root)
            .setTitle("@string/registro")
            .setPositiveButton(btn1Text,DialogInterface.OnClickListener { dialog, wich ->
                positiveButton()
            })
            .setNegativeButton(btn2Text) { _,_ ->
                negativeButton()
            }
            .create()
}

*/