
# 🎌 Anime Recommendation System – Java + Neo4j 📺

Este proyecto es un sistema inteligente de **recomendación de animes** desarrollado en **Java** con una interfaz gráfica en **Swing**, conectado a una base de datos **Neo4j**. Usa un enfoque mixto de recomendación basado en contenido y colaboración para sugerir animes relevantes a cada usuario.

---

## 📁 Estructura del Proyecto

```
PROYECTO2-AED/
├── src/
│   └── main/
│       └── java/
│           └── uvg/
│               └── edu/
│                   └── gt/
│                       ├── Anime.java
│                       ├── App.java
│                       ├── BaseDeDatos.java
│                       ├── Busqueda.java
│                       ├── Genero.java
│                       ├── I.java
│                       ├── RecomendadorColaborativo.java
│                       ├── RecomendadorContenido.java
│                       └── Usuario.java
├── test/
├── target/
├── .gitignore
├── anime.csv
├── pom.xml
└── README.md
```

---

## 🧠 ¿Qué hace este sistema?

- Permite a los usuarios buscar animes por nombre o género.
- Recomienda animes usando dos enfoques:
  - 🔍 **Recomendación por Contenido**: basado en los géneros y características del anime.
  - 👥 **Recomendación Colaborativa**: basado en similitudes entre usuarios y lo que han visto.
- Gestiona usuarios y guarda sus preferencias.
- Conecta con una base de datos en grafo (Neo4j) para explorar relaciones complejas entre animes, géneros y usuarios.
- Ofrece una interfaz gráfica interactiva y moderna usando **Swing**.

---

## 🌐 ¿Qué es Neo4j?

**Neo4j** es una base de datos orientada a grafos que permite modelar información altamente conectada de forma natural y eficiente.

### En este proyecto:
- Los **nodos** representan:
  - `Anime`
  - `Usuario`
  - `Genero`
- Las **relaciones** modelan:
  - `:TIENE_GENERO` — Relaciona animes con sus géneros.
  - `:VISTO` — Usuario ha visto un anime.
  - `:SIMILAR_A` — Usuarios con gustos similares.

Esto permite hacer consultas tipo:

> "Usuarios que vieron `Naruto` también vieron otros animes `Shonen` que tú no has visto."

---

## ⚙️ Instalación y requisitos

### Requisitos del sistema:
- Java JDK 11 o superior
- Apache Maven
- Conexión a internet
- Neo4j Desktop o Neo4j Server

### Pasos de instalación:
1. Clona el repositorio:
    ```bash
    git clone https://github.com/usuario/proyecto2-aed.git
    cd proyecto2-aed
    ```

2. Configura Neo4j:
    - Crea una base de datos vacía en Neo4j Desktop o conéctate a `bolt://localhost:7687`
    - Asegúrate de que el usuario y contraseña coincidan con los de `BaseDeDatos.java`

3. Importa los datos de anime:
    - Asegúrate de tener el archivo `anime.csv` en el directorio raíz.
    - Ejecuta el script de carga de nodos si se provee.

4. Compila y ejecuta el programa:
    ```bash
    mvn clean compile
    mvn exec:java -Dexec.mainClass="uvg.edu.gt.App"
    ```

---

## 🧪 Pruebas realizadas con usuarios

Para validar la utilidad del sistema, se realizaron pruebas con distintos usuarios (compañeros de clase y familiares):

- 🔍 Buscaron animes conocidos y recibieron resultados esperados.
- 💡 Marcaron animes como favoritos y obtuvieron recomendaciones personalizadas.
- ✅ El **80% de los usuarios** consideraron que las recomendaciones fueron acertadas.
- 😄 Comentarios destacaron la interfaz amigable, el diseño moderno y la utilidad para descubrir nuevos animes.
- 🛠️ Con base en el feedback, se mejoró la visualización y la lógica de filtrado por género.

---

## 💾 Datos utilizados

El archivo `anime.csv` contiene datos reales de animes populares:
- Títulos de animes
- Géneros asociados
- Descripciones
- Puntuaciones

Se cuidó la privacidad y no se incluyeron datos personales de usuarios.

---


