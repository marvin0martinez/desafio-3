package www.desafio3.marvin

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.text.util.Linkify
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.squareup.picasso.Picasso

class MostrarRecurso : AppCompatActivity() {

    private lateinit var api: Api
    private var recurso: Recurso? = null

    private lateinit var TituloText: TextView
    private lateinit var DescripcionText: TextView
    private lateinit var TipoText: TextView
    private lateinit var EnlaceText: TextView
    private lateinit var ImagenView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_mostrar_recurso)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        TituloText = findViewById(R.id.TituloText)
        DescripcionText = findViewById(R.id.DescripcionText)
        TipoText = findViewById(R.id.TipoText)
        EnlaceText = findViewById(R.id.EnlaceText)
        ImagenView = findViewById(R.id.Imagen)

        val recursoId = intent.getIntExtra("recurso_id", -1)
        Log.e("API", "recursoId : $recursoId")

        var titulo = intent.getStringExtra("titulo").toString()
        var descripcion = intent.getStringExtra("descripcion").toString()
        var tipo = intent.getStringExtra("tipo").toString()
        var enlace = intent.getStringExtra("enlace").toString()
        val imagen = intent.getStringExtra("imagen").toString()

        if (titulo == null || titulo == ""){
            titulo = "No tiene titulo"
        }
        if (descripcion == null || descripcion == ""){
            descripcion = "No hay descripción"
        }
        if (tipo == null || tipo ==  ""){
            tipo = "No tiene tipo de recurso"
        }

        TituloText.setText(titulo)
        DescripcionText.setText(descripcion)
        EnlaceText.setText(enlace)
        Linkify.addLinks(EnlaceText, Linkify.WEB_URLS)
        TipoText.setText(tipo)
        Picasso.get().load(imagen).into(ImagenView)
    }
}