package br.senac.cpfs.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import br.senac.cpfs.R
import br.senac.cpfs.client.EnderecoClient
import br.senac.cpfs.databinding.ActivityMainBinding
import br.senac.cpfs.model.Endereco
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .build()

        val retrofit =  Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl("https://viacep.com.br/ws/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val enderecoClient = retrofit.create(EnderecoClient::class.java)

        binding.btnBusca.setOnClickListener {

            binding.btnBusca.isEnabled = false

            var cep = binding.editCEP.text.toString()

            if(cep.contains(".")) cep = cep.replace(".","")
            if(cep.contains("-")) cep = cep.replace("-", "")

            val call = enderecoClient.getEndereco(cep)
            
            val callback = object : Callback<Endereco> {

                override fun onResponse(call: Call<Endereco>, response: Response<Endereco>) {
                    if(response.isSuccessful) {
                        binding.editLogradouro.setText(response.body().logradouro)
                        binding.editComplemento.setText(response.body().complemento)
                        binding.editBairro.setText(response.body().bairro)
                        binding.editLocalidade.setText(response.body().localidade)
                        binding.editUF.setText(response.body().uf)
                        binding.btnBusca.isEnabled = true
                    }
                }

                override fun onFailure(call: Call<Endereco>?, t: Throwable?) {
                    Toast.makeText(this@MainActivity, "Erro ao buscar o cep", Toast.LENGTH_LONG)
                        .show()

                    binding.btnBusca.isEnabled = true
                }

            }

            call.enqueue(callback)

        }

    }
}