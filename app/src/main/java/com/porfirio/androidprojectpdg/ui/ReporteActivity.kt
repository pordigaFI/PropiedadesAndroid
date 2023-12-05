package com.porfirio.androidprojectpdg.ui

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Telephony
import android.telephony.SmsManager
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.porfirio.androidprojectpdg.DataPickerFragment
import com.porfirio.androidprojectpdg.R
import com.porfirio.androidprojectpdg.TimePickerFragment
import com.porfirio.androidprojectpdg.databinding.ActivityReporteBinding

class ReporteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReporteBinding
    private val SMS_PERMISSION_CODE = 123
    private lateinit var phone: EditText
    private lateinit var message: EditText
    private lateinit var btnSendSms: Button
    var txtFecha:EditText? = null
    var txtInicio:EditText? = null
    var txtFin:EditText? = null
    var txtAsesor:EditText? = null
    var txtPropiedad:EditText? = null
    var txtCliente:EditText? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        super.onCreate(savedInstanceState)
        binding = ActivityReporteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.etDate.setOnClickListener{showDatePickerDialog()}

        binding.etTime.setOnClickListener{ showTimePickerDialog()}

        binding.etTimeFin.setOnClickListener{ showTimePickerDialog2()}

        txtFecha = binding.etDate
        txtInicio = binding.etTime
        txtFin = binding.etTimeFin
        txtAsesor = binding.etAsesor
        txtPropiedad = binding.etProperty
        txtCliente = binding.etCliente
        message = binding.etMensaje
        phone = binding.etCelular
        message = binding.etMensaje
        btnSendSms = binding.btnSend

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED){
            //Si el permiso no esta otorgado, entonces solicitamos al usuario su permiso, tanto para recibir como enviar Sms
            ActivityCompat.requestPermissions(this, arrayOf(
                Manifest.permission.RECEIVE_SMS,
                Manifest.permission.SEND_SMS),SMS_PERMISSION_CODE )
            //Se el codigo de solicitud es igual a 123, se entiende que el permiso solicitado es otorgado

        }else{
            //Si el permiso ya esta otorgado, entonces llamamos a la función receiveMsg()
            receiveMsg()
            btnSendSms.setOnClickListener{
                var sms = SmsManager.getDefault()
                sms.sendTextMessage(phone.text.toString(), "ME", message.text.toString(),null, null)

            }
        }
    }

    private fun showTimePickerDialog() {
        val timePicker = TimePickerFragment{ onTimeSelected(it)}
        timePicker.show(supportFragmentManager, "timePicker")
    }

    private fun onTimeSelected(time: String) {
        binding.etTime.setText("$time")
    }

    private fun showTimePickerDialog2() {
        val timePicker = TimePickerFragment{ onTimeSelectedFin(it)}
        timePicker.show(supportFragmentManager, "timePicker")
    }

    private fun onTimeSelectedFin(time: String){
        binding.etTimeFin.setText("$time")
    }

    private fun showDatePickerDialog(){
        val datePicker = DataPickerFragment{day, month, year -> onDateSelected(day, month, year)}
        datePicker.show(supportFragmentManager, "datePicker")
    }

    private fun onDateSelected(day: Int, month: Int, year: Int) {
        binding.etDate.setText("$day / $month / $year")
    }

    //Se verifica el resultado de la solicitud de permisos
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == SMS_PERMISSION_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            receiveMsg()

    }

    private fun receiveMsg() {
        //La Transmisión del receptor es continuamente observado para ver los eventos que ocurren
        var br = object: BroadcastReceiver(){
            override fun onReceive(p0: Context?, p1: Intent?) {
                //El SDK debe ser mínimo igual a Lollipop nivel de api 21-22, para cubrir los dispositivos que
                //actualmente pudieran estar en uso.
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    for(sms in Telephony.Sms.Intents.getMessagesFromIntent(p1)){
                        //Toast.makeText(applicationContext,sms.displayMessageBody,Toast.LENGTH_SHORT).show()
                        phone.setText(sms.originatingAddress)
                        message.setText(sms.displayMessageBody)
                    }
                }
            }
        }
        registerReceiver(br, IntentFilter("android.provider.Telephony.SMS_RECEIVED"))
    }

    //sección manejo del apiary
    fun clickBtnInsert(view: View){
        val url2 = "https://private-494528-bitacoracitas.apiary-mock.com/bitacoraCitas/bitacoraCitas_list"
        val queue = Volley.newRequestQueue(this)
        var resultadoPost = object : StringRequest(
            Request.Method.POST,url2,
            Response.Listener<String> { response ->
                Toast.makeText(this, "Bitacora insertada exitosamente", Toast.LENGTH_SHORT).show()
            }, Response.ErrorListener { error ->
                Toast.makeText(this, "Error $error", Toast.LENGTH_SHORT).show()
            }
        ){
            //va a regresar una tabla
            override fun getParams(): MutableMap<String, String> {
                val parametros = HashMap<String,String>()
                parametros.put("fecha", txtFecha?.text.toString())
                parametros.put("hora_inicio", txtInicio?.text.toString())
                parametros.put("hora_fin", txtFin?.text.toString())
                parametros.put("asesor", txtAsesor?.text.toString())
                parametros.put("propiedad", txtPropiedad?.text.toString())
                parametros.put("cliente", txtCliente?.text.toString())
                parametros.put("mensaje", message.text.toString())
                return parametros
            }
        }
        queue.add(resultadoPost)
    }
}