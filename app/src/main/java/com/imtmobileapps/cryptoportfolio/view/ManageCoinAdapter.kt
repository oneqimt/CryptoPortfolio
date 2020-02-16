package com.imtmobileapps.cryptoportfolio.view

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.imtmobileapps.cryptoportfolio.databinding.ItemCoinBinding
import com.imtmobileapps.cryptoportfolio.model.Coin

class ManageCoinAdapter(val coinList : ArrayList<Coin>) : RecyclerView.Adapter<ManageCoinAdapter.ManageCoinViewHolder>() {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ManageCoinViewHolder {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
    
    override fun getItemCount(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
    
    override fun onBindViewHolder(holder: ManageCoinViewHolder, position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
    
    class ManageCoinViewHolder(var view : ItemCoinBinding, var coinList: List<Coin>) : RecyclerView.ViewHolder(view.root), CoinClickListener{
        
        private var selectedCoin : Coin? = null
        
        
        override fun onCoinClicked(v: View) {
            selectedCoin = coinList[adapterPosition]
            
            /*val action = CoinListFragmentDirections.actionCoinDetailFragment()
            action.selectedCoin = selectedCoin
            Navigation.findNavController(v).navigate(action)*/
            
            
        }
    }
    
}