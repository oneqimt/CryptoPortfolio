package com.imtmobileapps.cryptoportfolio.view


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.imtmobileapps.cryptoportfolio.R
import com.imtmobileapps.cryptoportfolio.util.CoinApp
import com.imtmobileapps.cryptoportfolio.viewmodel.ManageHoldingsViewModel

class AddHoldingFragment : Fragment() {
    
    private lateinit var viewModel: ManageHoldingsViewModel
    
    
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_holding, container, false)
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
    
        viewModel = ViewModelProviders.of(this).get(ManageHoldingsViewModel::class.java)
        
        viewModel.fetchCoinsFromServer()
        
        observeViewModel()
    }
    
    fun observeViewModel() {
        viewModel.coinsFromServer.observe(this, Observer { coins ->
            coins?.let {
                println("$TAG ${CoinApp.TEST_APP} in observer and coins are HERE!!!")
                
            }
            
        })
    }
    
    companion object {
        private val TAG = AddHoldingFragment::class.java.simpleName
    }
    
    
}
