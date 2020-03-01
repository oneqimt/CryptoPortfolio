package com.imtmobileapps.cryptoportfolio.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.imtmobileapps.cryptoportfolio.R
import com.imtmobileapps.cryptoportfolio.databinding.ItemCoinBinding
import com.imtmobileapps.cryptoportfolio.model.CryptoValue

class CoinListAdapter(val coinList: ArrayList<CryptoValue>) : RecyclerView.Adapter<CoinListAdapter.CoinViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoinViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view = DataBindingUtil.inflate<ItemCoinBinding>(inflater, R.layout.item_coin, parent, false)
        return CoinViewHolder(view, coinList)
    }

    override fun getItemCount() = coinList.size

    override fun onBindViewHolder(holder: CoinViewHolder, position: Int) {

        holder.view.cryptoValue = coinList[position]
        holder.view.listener = holder
    }

    fun updateCoinList(newCoinsList: List<CryptoValue>){
        coinList.clear()
        coinList.addAll(newCoinsList)
        // persist the coinList so that we may use in AddFormFragment
        notifyDataSetChanged()
    }


    class CoinViewHolder(var view : ItemCoinBinding, var coinList: List<CryptoValue>) : RecyclerView.ViewHolder(view.root), CoinClickListener{

        private var selectedCoin : CryptoValue? = null


        override fun onCoinClicked(v: View) {
            selectedCoin = coinList[adapterPosition]

            val action = CoinListFragmentDirections.actionCoinDetailFragment()
            action.selectedCoin = selectedCoin
            Navigation.findNavController(v).navigate(action)


        }
    }


}

