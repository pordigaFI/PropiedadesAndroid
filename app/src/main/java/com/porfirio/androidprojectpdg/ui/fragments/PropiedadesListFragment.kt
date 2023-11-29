package com.porfirio.androidprojectpdg.ui.fragments

import android.os.Bundle
import com.porfirio.androidprojectpdg.util.Constants
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.porfirio.androidprojectpdg.R
import com.porfirio.androidprojectpdg.application.PropiedadesRFApp
import com.porfirio.androidprojectpdg.data.PropiedadRepository
import com.porfirio.androidprojectpdg.data.remote.model.PropiedadDto
import com.porfirio.androidprojectpdg.databinding.FragmentPropiedadesListBinding
import com.porfirio.androidprojectpdg.ui.adapter.PropiedadesAdapter
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PropiedadesListFragment : Fragment() {

    private var _binding: FragmentPropiedadesListBinding? = null
    private val binding get() = _binding!!
    private lateinit var repository: PropiedadRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentPropiedadesListBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        repository = (requireActivity().application as PropiedadesRFApp).repository

        lifecycleScope.launch {
            //val call: Call<List<GameDto>> = repository.getGames("cm/games/games_list.php")
            val call: Call<List<PropiedadDto>> = repository.getPropiedadesApiary()  //("tenistas/tenistas_list")

            call.enqueue(object: Callback<List<PropiedadDto>> {
                override fun onResponse(
                    call: Call<List<PropiedadDto>>,
                    response: Response<List<PropiedadDto>>
                ) {

                    binding.pbLoading.visibility = View.GONE

                    Log.d(Constants.LOGTAG, "Respuesta del servidor: ${response.body()}")

                    response.body()?.let{ tenistas ->
                        binding.rvPropiedades.apply {
                            layoutManager = LinearLayoutManager(requireContext())
                            adapter = PropiedadesAdapter(tenistas){ propiedad ->
                                propiedad.id?.let { id ->
                                    //Aquí va el código para la operación para ver los detalles
                                    requireActivity().supportFragmentManager.beginTransaction()
                                        .replace(R.id.fragment_container, PropiedadDetailFragment.newInstance(id))
                                        .addToBackStack(null)
                                        .commit()
                                }
                            }
                        }
                    }

                }

                override fun onFailure(call: Call<List<PropiedadDto>>, t: Throwable) {
                    Log.d(Constants.LOGTAG, "Error: ${t.message}")

                    Toast.makeText(requireActivity(), "No hay conexión", Toast.LENGTH_SHORT).show()

                    binding.pbLoading.visibility = View.GONE

                }

            })
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}