package www.desafio3.marvin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AgregarRecurso : AppCompatActivity() {
    private lateinit var api: Api
    private var recurso: Recurso? = null

    private lateinit var TituloText: EditText
    private lateinit var DescripcionText: EditText
    private lateinit var TipoText: EditText
    private lateinit var EnlaceText: EditText
    private lateinit var ImagenText: EditText
    private lateinit var AgregarButon: Button

    // Obtener las credenciales de autenticación
    var auth_username = "admin"
    var auth_password = "admin123"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_agregar_recurso)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val datos: Bundle? = intent.getExtras()
        if (datos != null) {
            auth_username = datos.getString("auth_username").toString()
            auth_password = datos.getString("auth_password").toString()
        }

        TituloText = findViewById(R.id.TituloText)
        DescripcionText = findViewById(R.id.DescripcionText)
        TipoText = findViewById(R.id.TipoText)
        EnlaceText = findViewById(R.id.EnlaceText)
        ImagenText = findViewById(R.id.ImagenText)
        AgregarButon = findViewById(R.id.AgregarButton)

        AgregarButon.setOnClickListener {
            val titulo = TituloText.text.toString()
            val descripcion = DescripcionText.text.toString()
            val tipo = TipoText.text.toString()
            var enlace = EnlaceText.text.toString()
            var imagen = ImagenText.text.toString()

            if (enlace == "" || enlace == null){
                enlace = "https://www.google.com"
            }

            if (imagen == "" || imagen == null){
                imagen = "https://i.postimg.cc/K8nP9GMQ/insert-picture-icon.png"
            }

            val recurso = Recurso(0,titulo, descripcion, tipo, enlace, imagen)
            Log.e("API", "auth_username: $auth_username")
            Log.e("API", "auth_password: $auth_password")

            // Crea una instancia de Retrofit con el cliente OkHttpClient
            val retrofit = Retrofit.Builder()
                .baseUrl("https://6708ece5af1a3998ba9fb39c.mockapi.io/api/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            // Crea una instancia del servicio que utiliza la autenticación HTTP básica
            val api = retrofit.create(Api::class.java)

            api.Agregar(recurso).enqueue(object : Callback<Recurso> {
                override fun onResponse(call: Call<Recurso>, response: Response<Recurso>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@AgregarRecurso, "Recurso creado exitosamente", Toast.LENGTH_SHORT).show()
                        val i = Intent(getBaseContext(), MainActivity::class.java)
                        startActivity(i)
                    } else {
                        val error = response.errorBody()?.string()
                        Log.e("API", "Error crear alumno: $error")
                        Toast.makeText(this@AgregarRecurso, "Error al crear el recurso", Toast.LENGTH_SHORT).show()
                    }

                }

                override fun onFailure(call: Call<Recurso>, t: Throwable) {
                    Toast.makeText(this@AgregarRecurso, "Error al crear el recurso", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}