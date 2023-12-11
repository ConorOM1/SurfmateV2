package ie.setu.surfmate.adapters

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
    private var surfspots: ArrayList<SurfmateModel>,
    private val listener: SurfmateListener
) : RecyclerView.Adapter<SurfmateAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardSurfmateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MainHolder(binding)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val surfspot = surfspots[holder.adapterPosition]
        holder.itemView.tag = surfspot
        holder.bind(surfspot, listener)
    }

    fun removeAt(position: Int) {
        surfspots.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun getItemCount(): Int = surfspots.size

    inner class MainHolder(private val binding: CardSurfmateBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(surfspot: SurfmateModel, listener: SurfmateListener) {
            binding.name.text = surfspot.name

            surfspot.image?.let {
                Picasso.get()
                    .load(it)
                    .placeholder(com.google.android.material.R.drawable.mtrl_switch_thumb_unchecked)
                    .error(R.drawable.surfmate_logo)
                    .resize(400, 400)
                    .into(binding.imageIcon)
            } ?: binding.imageIcon.setImageResource(R.drawable.surfmate_logo)

            binding.root.setOnClickListener { listener.onSurfspotClick(surfspot) }
        }
    }
}
