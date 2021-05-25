package br.senac.cpfs.client

import br.senac.cpfs.model.Endereco
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface EnderecoClient  {

    @GET("{cep}/json")
    fun getEndereco(@Path("cep") cep: String) : Call<Endereco>

}