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
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.porfirio.androidprojectpdg.DataPickerFragment
import com.porfirio.androidprojectpdg.R
import com.porfirio.androidprojectpdg.TimePickerFragment
import com.porfirio.androidprojectpdg.data.remote.PropiedadesApi
import com.porfirio.androidprojectpdg.data.remote.model.PostModelRequest
import com.porfirio.androidprojectpdg.data.remote.model.PostModelResponse
import com.porfirio.androidprojectpdg.databinding.ActivityReporteBinding
import kotlinx.coroutines.launch
import okhttp3.*
import java.io.IOException
import okhttp3.FormBody
import okhttp3.Callback
import okhttp3.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create


class ReporteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReporteBinding
    private val SMS_PERMISSION_CODE = 123
    private lateinit var phone: EditText
    private lateinit var message: EditText
    private lateinit var btnSendSms: Button
    private lateinit var btnVerPropiedad: Button
    private lateinit var btnInsertarBitacora: Button
    var txtFecha: EditText? = null
    var txtInicio: EditText? = null
    var txtFin: EditText? = null
    var txtAsesor: EditText? = null
    var txtPropiedad: EditText? = null
    var txtCliente: EditText? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityReporteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.etDate.setOnClickListener { showDatePickerDialog() }

        binding.etTime.setOnClickListener { showTimePickerDialog() }

        binding.etTimeFin.setOnClickListener { showTimePickerDialog2() }

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
        btnVerPropiedad = binding.btnPropiedad
        btnInsertarBitacora = binding.btnInsertar

        btnVerPropiedad.setOnClickListener { irPropiedades() }
        btnInsertarBitacora.setOnClickListener { clickBtnInsert() }
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.RECEIVE_SMS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            //Si el permiso no esta otorgado, entonces solicitamos al usuario su permiso, tanto para recibir como enviar Sms
            ActivityCompat.requestPermissions(
                this, arrayOf(
                    Manifest.permission.RECEIVE_SMS,
                    Manifest.permission.SEND_SMS
                ), SMS_PERMISSION_CODE
            )
            //Se el codigo de solicitud es igual a 123, se entiende que el permiso solicitado es otorgado

        } else {
            //Si el permiso ya esta otorgado, entonces llamamos a la función receiveMsg()
            receiveMsg()
            btnSendSms.setOnClickListener {
                var sms = SmsManager.getDefault()
                sms.sendTextMessage(
                    phone.text.toString(),
                    "ME",
                    message.text.toString(),
                    null,
                    null
                )

            }
        }
    }

    private fun showTimePickerDialog() {
        val timePicker = TimePickerFragment { onTimeSelected(it) }
        timePicker.show(supportFragmentManager, "timePicker")
    }

    private fun onTimeSelected(time: String) {
        binding.etTime.setText("$time")
    }

    private fun showTimePickerDialog2() {
        val timePicker = TimePickerFragment { onTimeSelectedFin(it) }
        timePicker.show(supportFragmentManager, "timePicker")
    }

    private fun onTimeSelectedFin(time: String) {
        binding.etTimeFin.setText("$time")
    }

    private fun showDatePickerDialog() {
        val datePicker = DataPickerFragment { day, month, year -> onDateSelected(day, month, year) }
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
        var br = object : BroadcastReceiver() {
            override fun onReceive(p0: Context?, p1: Intent?) {
                //El SDK debe ser mínimo igual a Lollipop nivel de api 21-22, para cubrir los dispositivos que
                //actualmente pudieran estar en uso.
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    for (sms in Telephony.Sms.Intents.getMessagesFromIntent(p1)) {
                        //Toast.makeText(applicationContext,sms.displayMessageBody,Toast.LENGTH_SHORT).show()
                        phone.setText(sms.originatingAddress)
                        message.setText(sms.displayMessageBody)
                    }
                }
            }
        }
        registerReceiver(br, IntentFilter("android.provider.Telephony.SMS_RECEIVED"))
    }

    private fun irPropiedades() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    //sección manejo del apiary
    private fun clickBtnInsert() {

        val url2_base = "https://private-494528-bitacoracitas.apiary-mock.com/"

        //Procedimiento para descargar datos del apiary
        /*
        val retrofit = Retrofit.Builder()
            .baseUrl(url2_base)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(PropiedadesApi::class.java)
        lifecycleScope.launch {
            val response = service.getBitacoraById("1")
            if (response.isSuccessful){
                runOnUiThread {
                    val respuesta = findViewById<TextView>(R.id.etProperty)
                    print("${response.body()?.id} = ${response.body()?.propiedadCita}")
                }
            }else{
                Log.e("Error: ", "${response.code()}")
            }
        }
         */

        //Procedimiento para subir datos al apiary
        val retrofit = Retrofit.Builder()
            .baseUrl(url2_base)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(PropiedadesApi::class.java)
        lifecycleScope.launch {
            val objRequest = PostModelRequest(
                fecha = txtFecha?.text.toString(),
                inicio = txtInicio?.text.toString(),
                fin = txtFin?.text.toString(),
                asesor = txtAsesor?.text.toString(),
                propiedadCita = txtPropiedad?.text.toString(),
                clienteCita = txtCliente?.text.toString(),
                mensaje = message.text.toString()
            )
            val response = service.postBitacora(objRequest)
            if (response.isSuccessful) {
                Toast.makeText(applicationContext, "Se enviaron los datos correctamente", Toast.LENGTH_SHORT).show()

            } else {
                Log.e("Error: ", "${response.code()}")
            }
        }

        /*
        //Datos que se enviaran en el cuerpo de la solicitud
        val requestBody = FormBody.Builder()
            .add("fecha", txtFecha?.text.toString())
            .add("hora_inicio", txtInicio?.text.toString())
            .add("hora_fin", txtFin?.text.toString())
            .add("asesor", txtAsesor?.text.toString())
            .add("propiedad", txtPropiedad?.text.toString())
            .add("cliente", txtCliente?.text.toString())
            .add("mensaje", message.text.toString())
            .build()

        //Realizamos la solicitud POST
        val request = Request.Builder()
            .url(url2)
            .post(requestBody)
            .build()

        //Creamos un cliente OkHttp
        val client = OkHttpClient()

        //Realizamos la solicitud de forma asíncrona
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                print("Error al realizar la solicitud: ${e.message}")
            }

            override fun onResponse(call: Call, response: okhttp3.Response) {
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    print("Solicitud exitosa. Respuesta del servidor: ")
                    print(responseBody)
                } else {
                    print("Error en la solicitud. Código de estado: ${response.code}")
                    print(response.body?.string())
                }
            }
        })
        //Agregamos un tiempo de espera para recibir la respuesta
        Thread.sleep(5000)
    }

        = post(url2, data = requestData)

        //Manejo de la respuesta
        if (response.statusCode == 200){
            print("Solicitud exitosa")
            print(response.text)
        } else {
            print("Error en la solicitud, código de estado: ${response.statusCode}")
            print(response.text)
        }


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
*/
    }
}