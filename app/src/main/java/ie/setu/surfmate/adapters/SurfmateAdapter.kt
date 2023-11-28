package ie.setu.surfmate.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import ie.setu.surfmate.R
import ie.setu.surfmate.databinding.CardSurfmateBinding
import ie.setu.surfmate.models.SurfmateModel

interface SurfmateListener {
    fun onSurfspotClick(surfspot: SurfmateModel)
}

class SurfmateAdapter constructor(
    private var surfspots: List<SurfmateModel>,
    private val listener: SurfmateListener
) : RecyclerView.Adapter<SurfmateAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardSurfmateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MainHolder(binding)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val surfspot = surfspots[holder.adapterPosition]
        holder.bind(surfspot, listener)
    }

    override fun getItemCount(): Int = surfspots.size

    inner class MainHolder(private val binding: CardSurfmateBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(surfspot: SurfmateModel, listener: SurfmateListener) {
            binding.name.text = surfspot.name

            if (surfspot.image != Uri.EMPTY) {
                Picasso.get().load(surfspot.image).resize(400, 400).into(binding.imageIcon)
            } else {
                binding.imageIcon.setImageResource(R.drawable.surfmate_logo)
            }

            binding.root.setOnClickListener { listener.onSurfspotClick(surfspot) }
        }
    }
}
