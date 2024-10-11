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
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonSyntaxException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ActualizarRecurso : AppCompatActivity() {
    private lateinit var api: Api
    private var recurso: Recurso? = null

    private lateinit var TituloText: EditText
    private lateinit var DescripcionText: EditText
    private lateinit var TipoText: EditText
    private lateinit var EnlaceText: EditText
    private lateinit var ImagenText: EditText
    private lateinit var ActualizarButon: Button

    val auth_username = "admin"
    val auth_password = "admin123"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_actualizar_recurso)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        TituloText = findViewById(R.id.TituloText)
        DescripcionText = findViewById(R.id.DescripcionText)
        TipoText = findViewById(R.id.TipoText)
        EnlaceText = findViewById(R.id.EnlaceText)
        ImagenText = findViewById(R.id.ImagenText)
        ActualizarButon = findViewById(R.id.ActualizarButton)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://6708ece5af1a3998ba9fb39c.mockapi.io/api/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(Api::class.java)

        val recursoId = intent.getIntExtra("recurso_id", -1)
        Log.e("API", "recursoId : $recursoId")

        val titulo = intent.getStringExtra("titulo").toString()
        val descripcion = intent.getStringExtra("descripcion").toString()
        val tipo = intent.getStringExtra("tipo").toString()
        val enlace = intent.getStringExtra("enlace").toString()
        var imagen = intent.getStringExtra("imagen").toString()

        TituloText.setText(titulo)
        DescripcionText.setText(descripcion)
        TipoText.setText(tipo)
        EnlaceText.setText(enlace)
        ImagenText.setText(imagen)

        val recurso = Recurso(0, titulo, descripcion, tipo, enlace, imagen)

        ActualizarButon.setOnClickListener {
            if (recurso != null) {
                if (ImagenText.text.toString() == "" || ImagenText.text.toString() == null){
                    ImagenText.setText("https://i.postimg.cc/K8nP9GMQ/insert-picture-icon.png")
                }
                if (EnlaceText.text.toString() == "" || EnlaceText.text.toString() == null){
                    EnlaceText.setText("https://www.google.com")
                }
                val recursoModificado = Recurso(
                    recursoId,
                    TituloText.text.toString(),
                    DescripcionText.text.toString(),
                    TipoText.text.toString(),
                    EnlaceText.text.toString(),
                    ImagenText.text.toString()
                )


                val jsonRecursoActualizado = Gson().toJson(recursoModificado)
                Log.d("API", "JSON enviado: $jsonRecursoActualizado")

                val gson = GsonBuilder()
                    .setLenient()
                    .create()

                api.Actualizar(recursoId, recursoModificado).enqueue(object :
                    Callback<Recurso> {
                    override fun onResponse(call: Call<Recurso>, response: Response<Recurso>) {
                        if (response.isSuccessful && response.body() != null) {
                            // Si la solicitud es exitosa, mostrar un mensaje de éxito en un Toast
                            Toast.makeText(this@ActualizarRecurso, "Recurso actualizado correctamente", Toast.LENGTH_SHORT).show()
                            val i = Intent(getBaseContext(), MainActivity::class.java)
                            startActivity(i)
                        } else {
                            // Si la respuesta del servidor no es exitosa, manejar el error
                            try {
                                val errorJson = response.errorBody()?.string()
                                val errorObj = JSONObject(errorJson)
                                val errorMessage = errorObj.getString("message")
                                Toast.makeText(this@ActualizarRecurso, errorMessage, Toast.LENGTH_SHORT).show()
                            } catch (e: Exception) {
                                // Si no se puede parsear la respuesta del servidor, mostrar un mensaje de error genérico
                                Toast.makeText(this@ActualizarRecurso, "Error al actualizar el recurso", Toast.LENGTH_SHORT).show()
                                Log.e("API", "Error al parsear el JSON: ${e.message}")
                            }
                        }
                    }

                    override fun onFailure(call: Call<Recurso>, t: Throwable) {
                        // Si la solicitud falla, mostrar un mensaje de error en un Toast
                        Log.e("API", "onFailure : $t")
                        Toast.makeText(this@ActualizarRecurso, "Error al actualizar el recurso", Toast.LENGTH_SHORT).show()

                        // Si la respuesta JSON está malformada, manejar el error
                        try {
                            val gson = GsonBuilder().setLenient().create()
                            val error = t.message ?: ""
                            val recurso = gson.fromJson(error, Recurso::class.java)
                        } catch (e: JsonSyntaxException) {
                            Log.e("API", "Error al parsear el JSON: ${e.message}")
                        } catch (e: IllegalStateException) {
                            Log.e("API", "Error al parsear el JSON: ${e.message}")
                        }
                    }
                })
            }
        }
    }
}