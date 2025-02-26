package com.emptyshop.kotlinrestapiclient

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.emptyshop.kotlinrestapiclient.ui.theme.KotlinRestApiClientTheme
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.emptyshop.kotlinrestapiclient.databinding.ActivityMainBinding
import kotlinx.coroutines.*

class MainActivity : ComponentActivity(), ContactoAdapter.OnItemClicked {

    // el layout para MainActivity
    lateinit var binding: ActivityMainBinding

    // la instancia del adaptador para RecyclerView
    lateinit var adaptador: ContactoAdapter

    var listaContactos = arrayListOf<Contacto>()
    var contacto = Contacto(-1, "", "", "")
    var isEditando = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()  // habilita toda la pantalla

        // asigna a la variable el layout de ActivityMain
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // asigna el tipo de layout para el contenedor del RecyclerView
        binding.rvContactos.layoutManager = LinearLayoutManager(this)
        // se llama el método para llenar el RecyclerView
        setUpRecyclerView()

        obtenerContactos()

        // asigna la funcionalidad al listener del evento onClick del botón agregar/actualizar
        binding.btnAddUpdate.setOnClickListener{
            var isValido = validarCampos()

            if (isValido){
                if (!isEditando){
                    agregarContacto()
                }
                else{
                    actualizarContacto()
                }
            }
            else{
                Toast.makeText(this,"Llena todos los campos.", Toast.LENGTH_LONG).show()
            }
        }

        // Jetpack Compose sustituye la UI clásica de Android
        /*setContent {
            KotlinRestApiClientTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }*/
    }

    // Llena el RecyclerView con los datos actuales
    fun setUpRecyclerView(){
        adaptador = ContactoAdapter(listaContactos)
        adaptador.setOnClick(this@MainActivity)
        binding.rvContactos.adapter = adaptador
    }

    // Invocación del servicio REST para obtener toda la lista de contactos
    fun obtenerContactos(){
        CoroutineScope(Dispatchers.IO).launch {
            val call = RestService.RetrofitClient.restService.obtenerContactos()
            runOnUiThread{
                if (call.isSuccessful){
                    // actualiza el arreglo con la lista de contactos
                    listaContactos = call.body()!!
                    // refresca el control para mostrar la lista actual
                    setUpRecyclerView()
                }
                else{
                    Toast.makeText(this@MainActivity, "Error al consultar los datos", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    fun validarCampos(): Boolean{
        return !(
                binding.etFullname.text.isNullOrEmpty() ||
                binding.etEmail.text.isNullOrEmpty() ||
                binding.etPhone.text.isNullOrEmpty()
                )
    }

    // Invocación del servicio REST para agregar un contacto
    fun agregarContacto(){
        this.contacto.id = -1
        this.contacto.fullname = binding.etFullname.text.toString()
        this.contacto.email = binding.etEmail.text.toString()
        this.contacto.phone = binding.etPhone.text.toString()

        CoroutineScope(Dispatchers.IO).launch {
            val call = RestService.RetrofitClient.restService.agregarContacto(contacto)
            runOnUiThread{
                if (call.isSuccessful){
                    Toast.makeText(this@MainActivity, "Se agregó a " + call.body()!!.fullname, Toast.LENGTH_LONG).show()

                    // Refresca la lista y limpia los controles de edición
                    obtenerContactos()
                    resetCampos()
                    resetObjeto()
                }
                else{
                    Toast.makeText(this@MainActivity, "Error al agregar", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    // Invocación del servicio REST para actualizar un contacto
    fun actualizarContacto(){
        this.contacto.fullname = binding.etFullname.text.toString()
        this.contacto.email = binding.etEmail.text.toString()
        this.contacto.phone = binding.etPhone.text.toString()

        CoroutineScope(Dispatchers.IO).launch {
            val call = RestService.RetrofitClient.restService.actualizarContacto(contacto.id, contacto)
            runOnUiThread {
                if (call.isSuccessful){
                    Toast.makeText(this@MainActivity,call.body()!!.success,Toast.LENGTH_LONG).show()

                    // Refresca la lista y limpia los controles de edición
                    obtenerContactos()
                    resetCampos()
                    resetObjeto()

                    binding.btnAddUpdate.setText("Agregar Contacto")
                    binding.btnAddUpdate.backgroundTintList = resources.getColorStateList(R.color.green, null)
                    isEditando = false
                }
            }
        }
    }

    override fun editarContacto(contacto: Contacto) {
        binding.etFullname.setText(contacto.fullname)
        binding.etEmail.setText(contacto.email)
        binding.etPhone.setText(contacto.phone)
        binding.btnAddUpdate.setText("Actualizar Contacto")
        binding.btnAddUpdate.backgroundTintList = resources.getColorStateList(R.color.purple_500, null)
        this.contacto = contacto
        isEditando = true
    }

    // Invocación del servicio REST para eliminar un contacto
    override fun borrarContacto(id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val call = RestService.RetrofitClient.restService.eliminarContacto(id)
            runOnUiThread{
                if (call.isSuccessful){
                    Toast.makeText(this@MainActivity,call.body()!!.success,Toast.LENGTH_LONG).show()

                    // Actualiza la lista de contactos
                    obtenerContactos()
                }
            }
        }
    }

    fun resetCampos(){
        binding.etFullname.setText("")
        binding.etEmail.setText("")
        binding.etPhone.setText("")
    }

    fun resetObjeto(){
        this.contacto.id = -1
        this.contacto.fullname = ""
        this.contacto.email = ""
        this.contacto.phone = ""
    }
}

// @Composable define un elemento de UI en Jetpack Compose
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

// @Preview Permite ver cómo se verá el Composable en el editor de
// Android Studio sin ejecutar la app.
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    KotlinRestApiClientTheme {
        Greeting("Android")
    }
}