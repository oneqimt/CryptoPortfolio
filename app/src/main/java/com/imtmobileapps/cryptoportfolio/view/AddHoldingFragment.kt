package com.imtmobileapps.cryptoportfolio.view


import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.imtmobileapps.cryptoportfolio.R
import com.imtmobileapps.cryptoportfolio.util.AppConstants
import com.imtmobileapps.cryptoportfolio.viewmodel.ManageHoldingsViewModel
import kotlinx.android.synthetic.main.fragment_add_holding.*

class AddHoldingFragment : Fragment(), SearchView.OnQueryTextListener {
    
    private lateinit var viewModel: ManageHoldingsViewModel
    private val manageCoinAdapter = ManageCoinAdapter(arrayListOf())
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }
    
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_holding, container, false)
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        
        (activity as AppCompatActivity).supportActionBar?.title = AppConstants.ADD_COIN
        
        viewModel = ViewModelProvider(this).get(ManageHoldingsViewModel::class.java)
        
        viewModel.fetchCoinsFromServer()
        
        addHoldingListView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = manageCoinAdapter
        }
        
        observeViewModel()
    }
    
    private fun observeViewModel() {
        viewModel.coinsFromServer.observe(viewLifecycleOwner, Observer { coins ->
            coins?.let {
                addHoldingListView.visibility = View.VISIBLE
                addHoldingPreloader.visibility = View.GONE
                manageCoinAdapter.updateCoinList(coins)
                
            }
            
        })
        
        viewModel.loadingCoinsFromServer.observe(viewLifecycleOwner, Observer { isLoading ->
            isLoading?.let {
                addHoldingPreloader.visibility = if (it) View.VISIBLE else View.GONE
                if (it) {
                    addHoldingListView.visibility = View.GONE
                }
            }
        })
    }
    
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.add_holding_list_menu, menu)
        
        val menuItem: MenuItem = menu.findItem(R.id.searchBar)
        
        val searchView: SearchView = menuItem.actionView as SearchView
        searchView.queryHint = "Search Coins"
        searchView.setOnQueryTextListener(this)
        searchView.isIconified = false
        
        super.onCreateOptionsMenu(menu, inflater)
        
        
    }
    
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        
        when (item.itemId) {
            R.id.searchBar -> {
                //println("$TAG $TEST_APP SEARCH BAR")
            }
            
        }
        
        return super.onOptionsItemSelected(item)
    }
    
    override fun onQueryTextSubmit(query: String?): Boolean {
        manageCoinAdapter.filter(query!!)
        
        return true
    }
    
    override fun onQueryTextChange(newText: String?): Boolean {
        manageCoinAdapter.filter(newText!!)
        
        return true
    }
    
    companion object {
        private val TAG = AddHoldingFragment::class.java.simpleName
    }
    
    
}
