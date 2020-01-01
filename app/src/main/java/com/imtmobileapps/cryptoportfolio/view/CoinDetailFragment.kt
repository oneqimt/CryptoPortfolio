package com.imtmobileapps.cryptoportfolio.view


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation

import com.imtmobileapps.cryptoportfolio.R
import com.imtmobileapps.cryptoportfolio.model.CryptoValue
import kotlinx.android.synthetic.main.fragment_coin_detail.*

/**
 * A simple [Fragment] subclass.
 */
class CoinDetailFragment : Fragment() {

    var selectedCoin : CryptoValue? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_coin_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        // if args are not null do this
        arguments?.let {
            selectedCoin = CoinDetailFragmentArgs.fromBundle(it).selectedCoin
            println(" in DETAIL and selectedCoin is : ${selectedCoin.toString()}")
        }


        toListBtn.setOnClickListener {
            val action = CoinDetailFragmentDirections.actionCoinListFragment()
            Navigation.findNavController(it).navigate(action)
        }
    }
}
