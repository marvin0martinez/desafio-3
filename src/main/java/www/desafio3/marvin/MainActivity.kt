package www.desafio3.marvin

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adaptador: Adaptador
    private lateinit var api: Api

    // Obtener las credenciales de autenticación
    val auth_username = "admin"
    val auth_password = "admin123"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val agregar: FloatingActionButton =
            findViewById<FloatingActionButton>(R.id.floatingActionButton)

        recyclerView = findViewById(R.id.mostrarRecursos)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Crea una instancia de Retrofit con el cliente OkHttpClient
        val retrofit = Retrofit.Builder()
            .baseUrl("https://6708ece5af1a3998ba9fb39c.mockapi.io/api/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        // Crea una instancia del servicio que utiliza la autenticación HTTP básica
        api = retrofit.create(Api::class.java)

        mostrarDatos(api)

        // Cuando el usuario quiere agregar un nuevo registro
        agregar.setOnClickListener(View.OnClickListener {
            val i = Intent(getBaseContext(), AgregarRecurso::class.java)
            i.putExtra("auth_username", auth_username)
            i.putExtra("auth_password", auth_password)
            startActivity(i)
        })
    }

    override fun onResume() {
        super.onResume()
        mostrarDatos(api)
    }

    private fun mostrarDatos(api: Api) {
        val call = api.obtenerRecursos()
        call.enqueue(object : Callback<List<Recurso>> {
            override fun onResponse(call: Call<List<Recurso>>, response: Response<List<Recurso>>) {
                if (response.isSuccessful) {
                    val recursos = response.body()
                    if (recursos != null) {
                        adaptador = Adaptador(recursos)
                        recyclerView.adapter = adaptador

                        // Establecemos el escuchador de clics en el adaptador
                        adaptador.setOnItemClickListener(object : Adaptador.OnItemClickListener {
                            override fun onItemClick(recurso: Recurso) {
                                val opciones = arrayOf("Mostrar Recurso", "Modificar Recurso", "Eliminar Recurso")

                                AlertDialog.Builder(this@MainActivity)
                                    .setTitle(recurso.titulo)
                                    .setItems(opciones) { dialog, index ->
                                        when (index) {
                                            0 -> Mostrar(recurso)
                                            1 -> Modificar(recurso)
                                            2 -> Eliminar(recurso, api)
                                        }
                                    }
                                    .setNegativeButton("Cancelar", null)
                                    .show()
                            }
                        })
                    }
                } else {
                    val error = response.errorBody()?.string()
                    Log.e("API", "Error al obtener los recursos: $error")
                    Toast.makeText(
                        this@MainActivity,
                        "Error al obtener los recursos 1",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<List<Recurso>>, t: Throwable) {
                Log.e("API", "Error al obtener los recursos: ${t.message}")
                Toast.makeText(
                    this@MainActivity,
                    "Error al obtener los recursos 2",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun Mostrar(recurso: Recurso){
        // Creamos un intent para ir a la actividad de ver recursos
        val i = Intent(getBaseContext(), MostrarRecurso::class.java)
        // Pasamos el ID del recurso seleccionado a la actividad de actualización
        i.putExtra("recurso_id", recurso.id)
        i.putExtra("titulo", recurso.titulo)
        i.putExtra("descripcion", recurso.descripcion)
        i.putExtra("tipo", recurso.tipo)
        i.putExtra("enlace", recurso.enlace)
        i.putExtra("imagen", recurso.imagen)
        // Iniciamos la actividad de actualización de recursos
        startActivity(i)
    }

    private fun Modificar(recurso: Recurso) {
        // Creamos un intent para ir a la actividad de actualización de recursos
        val i = Intent(getBaseContext(), ActualizarRecurso::class.java)
        // Pasamos el ID del recurso seleccionado a la actividad de actualización
        i.putExtra("recurso_id", recurso.id)
        i.putExtra("titulo", recurso.titulo)
        i.putExtra("descripcion", recurso.descripcion)
        i.putExtra("tipo", recurso.tipo)
        i.putExtra("enlace", recurso.enlace)
        i.putExtra("imagen", recurso.imagen)
        // Iniciamos la actividad de actualización de recursos
        startActivity(i)
    }

    private fun Eliminar(recurso: Recurso, api: Api) {
        Log.e("API", "id : $recurso")
        val llamada = api.Eliminar(recurso.id)
        llamada.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@MainActivity, "Recurso eliminado", Toast.LENGTH_SHORT).show()
                    mostrarDatos(api)
                } else {
                    val error = response.errorBody()?.string()
                    Log.e("API", "Error al eliminar recurso : $error")
                    Toast.makeText(this@MainActivity, "Error al eliminar recurso 1", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("API", "Error al eliminar recurso : $t")
                Toast.makeText(this@MainActivity, "Error al eliminar recurso 2", Toast.LENGTH_SHORT).show()
            }
        })
    }
}