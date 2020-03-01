package com.imtmobileapps.cryptoportfolio.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.imtmobileapps.cryptoportfolio.R
import com.imtmobileapps.cryptoportfolio.databinding.ItemManageHoldingsCoinBinding
import com.imtmobileapps.cryptoportfolio.model.Coin
import com.imtmobileapps.cryptoportfolio.util.CoinApp
import java.util.*
import kotlin.collections.ArrayList


class ManageCoinAdapter(val coinList: ArrayList<Coin>) :
    RecyclerView.Adapter<ManageCoinAdapter.ManageCoinViewHolder>() {
    
    var copyList = arrayListOf<Coin>()
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ManageCoinViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view = DataBindingUtil.inflate<ItemManageHoldingsCoinBinding>(
            inflater,
            R.layout.item_manage_holdings_coin,
            parent,
            false
        )
        copyList.addAll(coinList)
        return ManageCoinViewHolder(view, coinList)
    }
    
    override fun getItemCount(): Int {
        return coinList.size
    }
    
    override fun onBindViewHolder(holder: ManageCoinViewHolder, position: Int) {
        holder.view.manageCoin = coinList[position]
        holder.view.listener = holder
    }
    
    fun updateCoinList(newCoinsList: List<Coin>) {
        coinList.clear()
        coinList.addAll(newCoinsList)
        notifyDataSetChanged()
    }
    
    fun filter(text: String) {
        
        coinList.clear()
        if (text.isEmpty()) {
            coinList.addAll(copyList)
        } else {
            val mytext = text.toLowerCase(Locale.getDefault())
            for (coin in copyList) {
                val coinName = coin.coinName?.toLowerCase(Locale.getDefault()) ?: ""
                val coinSymbol = coin.coinSymbol?.toLowerCase(Locale.getDefault()) ?: ""
                if (coinName.contains(mytext) || coinSymbol.contains(mytext)) {
                    if (!coinList.contains(coin)) {
                        coinList.add(coin)
                    }
                    
                }
            }
        }
        
        notifyDataSetChanged()
        
    }
    
    class ManageCoinViewHolder(var view: ItemManageHoldingsCoinBinding, var coinList: List<Coin>) :
        RecyclerView.ViewHolder(view.root), CoinClickListener {
        
        private var coinToAdd: Coin? = null
        
        
        override fun onCoinClicked(v: View) {
            coinToAdd = coinList[adapterPosition]
            
            println("$TAG ${CoinApp.TEST_APP} coin clicked and it is: $coinToAdd")
            
            val action = AddHoldingFragmentDirections.actionAddHoldingFragmentToAddFormFragment()
            action.coinToAdd = coinToAdd
            Navigation.findNavController(v).navigate(action)
            
            
        }
    }
    
    companion object {
        private val TAG = ManageCoinAdapter::class.java.simpleName
    }
    
}