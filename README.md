# 🖼️ Image Viewer

Visor de imágenes de escritorio desarrollado en Java, siguiendo el patrón de arquitectura **MVP (Model-View-Presenter)**. Permite navegar entre imágenes de una carpeta y añadir nuevas imágenes desde cualquier ubicación del sistema.

---

## 📁 Estructura del proyecto

```
src/
└── app/
│      └── Main.java 
└── io/
│     ├── ImageLoader.java 
│     └── FileImageLoader.java
├── model/
│     └── Image.java
└── presenter/
│     └── CircularIterator.java
│     └── ImageViewerPresenter.java
└── view/
      ├── ImageDisplay.java
      ├── SwingImageDisplay.java
      └── ImageViewerView.java
```

---

## 🏛️ Arquitectura MVP + Iterator Pattern

Este proyecto sigue el patrón **Model-View-Presenter**, con el uso de Iterator Pattern para la gestión de lógica de navegación circular de imágenes.

En una arquitectura tradicional sin separación de capas, la UI mezcla lógica de negocio con componentes visuales, lo que hace el código difícil de testear y mantener.

Con MVP:

- El **Modelo** es un dato inmutable. Solo existe para transportar información entre capas.
- La **Vista** solo sabe pintar. No contiene ninguna lógica. Se comunica con el Presenter a través de interfaces, lo que permite sustituir Swing por cualquier otra tecnología sin tocar el resto.
- El **Presenter** orquesta toda la lógica: qué imagen mostrar, cómo navegar, qué hacer al añadir un fichero. No tiene ninguna dependencia con Swing, por lo que se puede testear de forma unitaria con mocks.
- La capa **IO** encapsula el acceso al sistema de ficheros detrás de una interfaz, lo que permite testear el Presenter sin tocar el disco.

---

## 📦 Descripción de cada clase

### `app/Main.java`
Punto de entrada de la aplicación. Se encarga de instanciar y conectar todas las piezas: crea el `FileImageLoader` apuntando a la carpeta `/images`, instancia la vista y el presenter, y arranca la aplicación.

---

### `model/Image.java`
Record inmutable que representa una imagen cargada en memoria. Contiene el `BufferedImage` con los datos de píxeles y el nombre del fichero original. No tiene lógica propia.

---

### `io/ImageLoader.java`
Interfaz que define el contrato para la capa de acceso a imágenes. Cualquier fuente de imágenes (disco, red, base de datos) debe implementar estos tres métodos:

---

### `io/FileImageLoader.java`
Implementación de `ImageLoader` sobre el sistema de ficheros local. Recibe por constructor la carpeta donde se almacenan las imágenes, por lo que no tiene ninguna ruta hardcodeada. Filtra ficheros por extensión (`.jpg`, `.jpeg`, `.png`) en `listImages()`, valida el contenido con `ImageIO` en `load()`, y copia el fichero al directorio destino en `save()`.

---

### `presenter/CircularIterator.java`
Estructura de datos que implementa un iterador circular sobre una lista genérica.

Se utiliza para gestionar la navegación de imágenes, evitando tener que manejar manualmente condiciones de borde (inicio y final de la lista).

Permite avanzar y retroceder sin límites lógicos: cuando se alcanza el final de la lista, la navegación continúa desde el principio, y viceversa.

---

### `presenter/ImageViewerPresenter.java`
Núcleo de la aplicación. Conecta el acceso a imágenes, la gestión de navegación y el display, utilizando el iterador para lo último. Expone cuatro métodos públicos:

- `init()` — Carga la lista de imágenes y muestra la primera. Si la lista está vacía, limpia la vista.
- `next()` — Avanza al siguiente índice con navegación circular.
- `prev()` — Retrocede al índice anterior con navegación circular.
- `add(File)` — Valida el fichero (no nulo, existente, MIME tipo imagen), delega el guardado en el `ImageLoader` y muestra la nueva imagen como actual. Si el guardado falla, el estado interno no se ve afectado.

No tiene ninguna dependencia con Swing. Se comunica con la vista exclusivamente a través de la interfaz `ImageDisplay`.

---

### `view/ImageDisplay.java`
Interfaz mínima que la vista debe implementar para que el Presenter pueda comunicarse con ella:

Es clave para que el Presenter sea testeable: en los tests se sustituye por un mock.

---

### `view/SwingImageDisplay.java`
Implementación de `ImageDisplay` como `JPanel`. Contiene un `JLabel` centrado donde se renderiza la imagen como `ImageIcon`. Si la imagen es nula o sus datos están vacíos, llama a `clear()`.

---

### `view/ImageViewerView.java`
Ventana principal de la aplicación (`JFrame`). Contiene los tres botones de control (**Prev**, **Add**, **Next**) y el panel de visualización. Sus responsabilidades son:

- Construir el layout de la interfaz.
- Conectar los eventos de los botones al Presenter mediante `setPresenter()`.
- Abrir el `JFileChooser` cuando el usuario pulsa **Add** y pasar el fichero seleccionado al Presenter.

No contiene lógica de negocio.

---

## ✨ Funcionalidades

- Navegar entre imágenes con los botones **Siguiente** y **Anterior**
- Navegación circular (al llegar al final vuelve al principio y viceversa)
- **Añadir** imágenes desde cualquier carpeta del sistema (se copian al directorio `images/`)
- Validación por tipo MIME, no solo por extensión

---

## 🚀 Requisitos para ejecutar el proyecto

### Versiones

- Java 22+
- Maven 3.8+

### Clonación del respositorio
```
git clone https://github.com/eeriick-j/Image-Viewer.git
```

---

## 🧪 Tests

Los tests cubren todos los casos del `ImageViewerPresenter` usando **JUnit 5** y **Mockito**. El Presenter se testea de forma completamente aislada gracias al uso de interfaces: `ImageLoader` e `ImageDisplay` se sustituyen por mocks, sin tocar disco ni UI.


### Casos cubiertos

`init()`: Lista vacía, carga la primera imagen, `IOException` al cargar 
`next()`: Antes de `init`, lista vacía, avanza al siguiente, wrap al final
`prev()`: Antes de `init`, lista vacía, retrocede, wrap al inicio 
`add()` : `null`, fichero inexistente, fichero no-imagen, imagen válida guardada y mostrada, se convierte en imagen actual, `IOException` al guardar no corrompe el estado 

---

## 📌 Limitaciones por diseño

- Las imágenes no se escalan al tamaño de la ventana; imágenes grandes pueden quedar recortadas.
- Si se añade una imagen con el mismo nombre que una existente, se sobreescribe sin aviso.
- No hay opción de eliminar imágenes desde la UI.

---
