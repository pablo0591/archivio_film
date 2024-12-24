package it.example.archiviofilm

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView


class FilmAdapter(private val movieList: MutableList<Film>, private val context: Context, private val PREFS_NAME: String) :
    RecyclerView.Adapter<FilmAdapter.FilmViewHolder>() {


    class FilmViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titolo = itemView.findViewById<TextView>(R.id.tvTitolo)
        val regista = itemView.findViewById<TextView>(R.id.tvRegista)
        val anno = itemView.findViewById<TextView>(R.id.tvAnno)
        val rating = itemView.findViewById<RatingBar>(R.id.ratingBar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.cella_lista, parent, false)

        return FilmViewHolder(view)
    }

    override fun getItemCount(): Int {
        return movieList.size
    }

    override fun onBindViewHolder(holder: FilmViewHolder, position: Int) {

        val film = movieList[position]
        val currentPosition = holder.adapterPosition

        holder.titolo.text = film.titolo
        holder.regista.text = film.regista
        holder.anno.text = film.anno.toString()
        holder.rating.rating = film.rating

        holder.itemView.setOnLongClickListener {
            AlertDialog.Builder(context)
                .setTitle("Cancellare il film selezionato?")
                .setPositiveButton("Si"){dialog, _ ->
                movieList.removeAt(position)
                    notifyItemRemoved(currentPosition)
                    cancella()
                    dialog.dismiss()
                }
                .setNegativeButton("No"){dialog, _ ->
                    dialog.dismiss()

                }
                .show()
            true
        }
    }

    private fun cancella() {
        val sharedPrefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor = sharedPrefs.edit()

        editor.clear()
        editor.apply()

        Toast.makeText(context, "Film cancellato con successo!", Toast.LENGTH_SHORT).show()
    }

    fun addMovie(movie: Film){
        movieList.add(movie)
        notifyItemInserted(movieList.size -1)
    }
}