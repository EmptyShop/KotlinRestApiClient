# KotlinRestApiClient
Cliente de API Rest (Retrofit2)

Este proyecto lo hice como parte de un conjunto de aplicaciones que consumen una API creada en Python para las operaciones básicas (CRUD) de una lista de contactos.
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
  - En la implementación de la clase MainActivity usamos esta referencia para ZXing:
    
    ```sh
    import com.google.zxing.integration.android.IntentIntegrator
    ```
    
  - Con Code Scanner incializamos algunas cosas:
  
    ```sh
    val scannerView = findViewById<CodeScannerView>(R.id.scanner_view)

    codeScanner = CodeScanner(this, scannerView)

    // Parameters (default values)
    codeScanner.camera = CodeScanner.CAMERA_BACK
    codeScanner.formats = CodeScanner.ALL_FORMATS

    codeScanner.autoFocusMode = AutoFocusMode.SAFE
    codeScanner.scanMode = ScanMode.SINGLE
    codeScanner.isAutoFocusEnabled = true
    codeScanner.isFlashEnabled = false

    scannerView.isVisible = false
    ```
        
  - Agregamos callbacks de decodificación y error para Code Scanner:

    ```sh
    // Callbacks
    codeScanner.decodeCallback = DecodeCallback {
        runOnUiThread{
            scannerView.isVisible = false
            messageText.text = it.text
            messageFormat.text = it.barcodeFormat.toString()
        }
    }

    codeScanner.errorCallback = ErrorCallback {
        runOnUiThread{
            Toast.makeText(this, "Camera Initialization error: ${it.message}",
                Toast.LENGTH_LONG).show()
            messageText.text = String()
            messageFormat.text = String()
            scannerView.isVisible = false
        }
    }
    ```
    
    En el código, `messageText` es el TextView para obtener el texto del código QR; `messageFormat` es el Text View para obtener el formato del código.

  - En el caso de ZXing, hice la implementación de su funcionalidad en el evento `onClick` del botón `scanBtnZXing`:

    ```sh
    // adding listener to the button for ZXing
    scanBtnZXing.setOnClickListener(this)

    override fun onClick(v: View?) {
        // we need to create the object
        // of IntentIntegrator class
        // which is the class of ZXing QR library
        val intentIntegrator = IntentIntegrator(this)
        intentIntegrator.setPrompt("Scan a barcode or QR Code")
        intentIntegrator.setOrientationLocked(true)
        intentIntegrator.initiateScan()
    }
    ```

    Eso anterior fue para iniciar la captura del código. Para procesar el resultado lo hice en este método:
    
    ```sh
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        // if the intentResult is null then
        // toast a message as "cancelled"
        if (intentResult != null){
            if (intentResult.contents.isNullOrEmpty()){
                Toast.makeText(baseContext, "Cancelled", Toast.LENGTH_SHORT).show()
            }
            else{
                // if the intentResult is not null we'll set
                // the content and format of scan message
                messageText.text = intentResult.contents
                messageFormat.text = intentResult.formatName
            }
        }
        else{
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
    ```
     
    Aquí uso el intent que creé en el evento `onClick`. Tomo los datos devueltos usando el método `parseActivityResult` y los asigno a los TextViews de formato y texto.
    
## La ayuda que utilicé:
Para este proyecto encontré en internet los siguientes recursos que me parecieron los más confiables y útiles:

  * [¿Cómo leer el código QR usando la biblioteca ZXing en Android?](https://es.acervolima.com/como-leer-el-codigo-qr-usando-la-biblioteca-zxing-en-android/)
  
  * [La documentación oficial de Code Scanner](https://github.com/yuriy-budiyev/code-scanner)

  * [Zxing – Leer QR y códigos de barras en Kotlin](https://cursokotlin.com/zxing-leer-qr-codigos-de-barras-en-kotlin/)

# Lo que sigue
El alcance de este proyecto sólo es la comparación de ambas librerías así que no tengo mucho más qué agregar. Tal vez darle una vista más atractiva y jugar con las propiedades y atributos de cada librería.

Siéntete libre de comentar y sugerir cosas para esta app.
