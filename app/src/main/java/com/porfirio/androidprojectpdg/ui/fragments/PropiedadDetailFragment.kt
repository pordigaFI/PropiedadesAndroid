package com.porfirio.androidprojectpdg.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.porfirio.androidprojectpdg.application.PropiedadesRFApp
import com.porfirio.androidprojectpdg.data.PropiedadRepository
import com.porfirio.androidprojectpdg.data.remote.model.PropiedadDetailDto
import com.porfirio.androidprojectpdg.databinding.FragmentPropiedadDetailBinding
import com.porfirio.androidprojectpdg.util.Constants
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val PROPIEDAD_ID = "propiedad_id"

class PropiedadDetailFragment : Fragment() {

    private var propiedadId: String? = null

    private var _binding: FragmentPropiedadDetailBinding? = null
    private val binding get() = _binding!!

    private lateinit var repository: PropiedadRepository

    //private lateinit var mp: MediaPlayer


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { args ->
            propiedadId = args.getString(PROPIEDAD_ID)

            Log.d(Constants.LOGTAG, "Id recibido: ${propiedadId}")

            repository = (requireActivity().application as PropiedadesRFApp).repository

            lifecycleScope.launch {

                propiedadId?.let { id ->
                    val call: Call<PropiedadDetailDto> = repository.getPropiedadDetailApiary(id)

                    call.enqueue(object: Callback<PropiedadDetailDto> {
                        override fun onResponse(
                            call: Call<PropiedadDetailDto>,
                            response: Response<PropiedadDetailDto>
                        ) {


                            binding.apply {
                                pbLoading.visibility = View.GONE

                                tvCalle.text = response.body()?.calle
                                tvColonia.text = response.body()?.colonia
                                tvMunicipio.text = response.body()?.municipio
                                tvEstado.text = response.body()?.estado
                                tvm2Terreno.text = response.body()?.m2_terreno
                                tvm2Construccion.text = response.body()?.m2_construccion
                                tvDescripcion.text = response.body()?.descripcion
                                tvLatitud.text = response.body()?.lat.toString()
                                tvLongitud.text = response.body()?.lon.toString()


                                Glide.with(requireContext())
                                    .load(response.body()?.imagen)
                                    .into(ivImage)
                            }

                            binding.btnAdelante.setOnClickListener {
                                val dataIntent = Intent(requireContext(),LocalizacionActivity::class.java).apply{
                                    putExtra("EXTRA_LAT", response.body()?.lat)
                                    putExtra("EXTRA_LNG", response.body()?.lon)
                                }
                                startActivity(dataIntent)
                            }
                        }

                        override fun onFailure(call: Call<PropiedadDetailDto>, t: Throwable) {
                            binding.pbLoading.visibility = View.GONE

                            Toast.makeText(requireActivity(), "No hay conexión", Toast.LENGTH_SHORT).show()
                        }

                    })
                }

            }

        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPropiedadDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance(propiedadId: String) =
            PropiedadDetailFragment().apply {
                arguments = Bundle().apply {
                    putString(PROPIEDAD_ID, propiedadId)
                }
            }
    }
}