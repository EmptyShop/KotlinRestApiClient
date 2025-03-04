# KotlinRestApiClient
Cliente de API Rest (Retrofit2)

Este proyecto lo hice como parte de un conjunto de aplicaciones que consumen una API que programé en Python para las operaciones básicas (CRUD) de una lista de contactos: [Contact List App](https://github.com/EmptyShop/FlaskSqlAlchemyApp).
Esta app (KotlinRestApiClient) es un cliente para Android, codificada en Kotlin y uso Retrofit2 para consumir los servicios Restfull.
La app contiene 3 campos de texto correspondientes a nombre, email y teléfono. Además contiene la lista de contactos registrados en un control
RecyclerView y conteniendo elementos CardView.

# Cómo lo Hice

## En este proyecto utilicé:
  - el sdk 35 como target y el sdk 29 como versión mínima.
  - Kotlin versión 1.9.24.
  - Gradle 8.10.2
  - Retrofit2 4.1.0

## Lo implementé así:
  - En las dependencias agregué la implementación de Retrofit2: `com.squareup.retrofit2:retrofit:2.11.0`
  - También las de Gson: `com.google.code.gson:gson:2.12.1`
  - Y de el convertidor de Gson: `com.squareup.retrofit2:converter-gson:2.11.0`
  - También añadí la implementación de Coroutines: `org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3`
  - Y las implementaciones de RecyclerView y CardView: `androidx.recyclerview:recyclerview:1.4.0`, `androidx.cardview:cardview:1.0.0`
  - Luego, en el manifest, asigné permisos para usar internet:
  
    ```sh
    <uses-permission android:name="android.permission.INTERNET" />
    ```
    Y además agregué la siguiente propiedad al tag `<application>`:

    ```sh
    android:usesCleartextTraffic="true"
    ```

  - El modelo de datos está contenido en la clase `Contacto`.
  
  - El layout de la app contiene 2 archivos XML:
    * `activity_main.xml`, el cual contiene los campos para editar cada contacto y el contenedor del RecyclerView.
    * `item_rv_contacto.xml`, que contiene la distribución de la información de cada contacto y los botones para editar y eliminar.
  - Para utilizar el RecyclerView implementé la clase `ContactoAdapter` la cual vincula el layout creado en `item_rv_contacto.xml` con la clase `Contacto` y define los métodos necesarios para configurar el funcionamiento de cada item de `CardView`.

  - Para manejar las llamadas a la API Rest generé la interfaz `RestService`. Aquí usé las clases y métodos de Retrofit. También está declarada una constante (`BASE_URL`) con la ruta de la API. De acuerdo a los parámetros de entrada y salida solicitados por la API, esta fue mi implementación de los métodos:
    
    ```sh
    @GET("/contact")
    suspend fun obtenerContactos(): Response<ArrayList<Contacto>>

    @GET("/contact/{id}")
    suspend fun obtenerContacto(
        @Path("id") id: Int
    ): Response<Contacto>

    @POST("/contact")
    suspend fun agregarContacto(
        @Body contacto: Contacto
    ): Response<Contacto>

    @PUT("/contact/{id}")
    suspend fun actualizarContacto(
        @Path("id") id: Int,
        @Body contacto: Contacto
    ): Response<APIResponse>

    @DELETE("/contact/{id}")
    suspend fun eliminarContacto(
        @Path("id") id: Int
    ): Response<APIResponse>
    ```
  - La clase `APIResponse` la utilizo para recibir la respuesta JSON de la API para los métodos PUT y DELETE.
  
  - En la implementación de la clase MainActivity tengo una variable `binding` que contiene el layout de `activity_main.xml`. También tengo la variable `adaptador` que es la instancia de la clase `ContactoAdapter`. Tengo además, un arreglo para almacenar la lista de contactos, `listaContactos`. 

    * El método `obtenerContactos` solicita a la API, usando corrutinas, la lista completa de contactos y la almacena en el arreglo `listaContactos`.
    * El método `setUpRecyclerView` actualiza este control con la lista completa de contactos, por lo que es invocada cuando se realiza alguna operación sobre los datos.
    * El método `agregarContacto` envía a la API un nuevo contacto (actualizando el objeto `this.contacto`) con los datos capturados por el usuario.
    * De forma similar, los métodos `actualizarContacto` y `borrarContacto` usan corrutinas para usar los métodos de la interfaz `RestService` y realizar las operaciones correspondientes.
    * El método `editarContacto` llena los campos de texto con los del contacto seleccionado por el botón `btnEditar` de cada elemento de contacto.
    
## La ayuda que utilicé:
Para este proyecto me basé principalmente en un video que muestra cómo consumir servicios Restfull con Retrofit y Kotlin. Yo lo adapté a mi propia API y además me apoyé en información complementaria:

  * [CRUD usando MySQL, Express.js, Retrofit, Kotlin y Android Studio Parte 2](https://www.youtube.com/watch?v=lUN0Ge6atz4)
  
  * [Coroutines | Documentación de Kotlin](https://kotlinlang.org/docs/coroutines-overview.html#documentation)

  * [Gradle Scripts, Dependencies, Retrofit & Coroutine](https://medium.com/@sruthicsankar/gradle-scripts-dependencies-retrofit-coroutine-in-android-development-7221d0af234a)
  * [Extract Data From JSON Array using Retrofit Library](https://www.geeksforgeeks.org/android-extract-data-from-json-array-using-retrofit-library-with-kotlin/)

# Lo que sigue
El alcance de este proyecto es comparar el desempeño y las implementaciones de cada versión de app cliente para consumo de servicios Restful que tengo en este repositorio ([Angular](https://github.com/EmptyShop/AngularRestApiClient), [React](https://github.com/EmptyShop/ReactRestApiClient), [Vue](https://github.com/EmptyShop/VueRestApiClient) y Kotlin). Por lo que no tengo planeado agregar o modificar funcionalidades.

Siéntete libre de comentar y sugerir cosas para esta app.
