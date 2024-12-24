package it.example.archiviofilm

import android.content.Context
import android.os.Bundle
import android.widget.EditText
import android.widget.RatingBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var btnAdd: FloatingActionButton
    private lateinit var adapter: FilmAdapter
    private var filmList = mutableListOf<Film>()

    val PREFS_NAME = "Archivio Film Prefs"
    var keySet: MutableSet<String> = mutableSetOf()
    val KEY_LIST = "KEY_LIST"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        btnAdd = findViewById(R.id.btnAdd)

        adapter = FilmAdapter(filmList, this, PREFS_NAME)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        loadMovies()
        setupAddButton()
    }

    private fun loadMovies() {

        val sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
         keySet = sharedPreferences.getStringSet(KEY_LIST, keySet)!!
         val temp = sharedPreferences.all
        for (k in keySet){
            val film = sharedPreferences.getString(k, "default")
            val filmObject = Gson().fromJson(film, Film::class.java)
            filmList.add(filmObject)

        }
    }

    fun setupAddButton() {

        btnAdd.setOnClickListener {
            val dialogView = layoutInflater.inflate(R.layout.add_movie, null)
            val titoloInput = dialogView.findViewById<EditText>(R.id.etTitolo)
            val registaInput = dialogView.findViewById<EditText>(R.id.etRegista)
            val annoInput = dialogView.findViewById<EditText>(R.id.etAnno)
            val ratingInput = dialogView.findViewById<RatingBar>(R.id.inputRatingBar)

            AlertDialog.Builder(this)
                .setTitle("Aggiungi un nuovo film")
                .setView(dialogView)
                .setPositiveButton("Aggiungi") { _, _ ->
                    val nuovoFilm = Film(
                        regista = registaInput.text.toString(),
                        titolo = titoloInput.text.toString(),
                        anno = annoInput.text.toString().toIntOrNull() ?: 2024,
                        rating = ratingInput.rating
                    )
                    adapter.addMovie(nuovoFilm)
                    save(nuovoFilm)
                }
                .setNegativeButton("Annulla", null)
                .show()

        }
    }

    private fun save(nuovoFilm: Film) {

        val keyTitolo = nuovoFilm.titolo
        keySet.add(keyTitolo)

        val sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putStringSet(KEY_LIST, keySet)
        val convertJsonString = Gson().toJson(nuovoFilm)

        editor.putString(keyTitolo, convertJsonString)

        editor.apply()

        Toast.makeText(this, "Film salvato con successo!", Toast.LENGTH_SHORT).show()

    }

}
