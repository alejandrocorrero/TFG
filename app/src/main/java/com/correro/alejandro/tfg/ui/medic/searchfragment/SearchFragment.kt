package com.correro.alejandro.tfg.ui.medic.searchfragment


import android.app.SearchManager
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import com.correro.alejandro.tfg.BR

import com.correro.alejandro.tfg.R

import android.content.Context
import android.support.annotation.Nullable
import android.support.v7.widget.LinearLayoutManager
import android.widget.LinearLayout
import android.widget.SearchView
import com.correro.alejandro.tfg.data.api.models.medicusersresponse.MedicUser
import com.correro.alejandro.tfg.ui.medic.MainMedicActivityViewModel
import com.correro.alejandro.tfg.utils.GenericAdapter

import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.fragment_search.view.*

class SearchFragment : Fragment() {

    private lateinit var mviewmodel: MainMedicActivityViewModel

    private lateinit var adapter: GenericAdapter<MedicUser>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_search, container, false)
        mviewmodel = ViewModelProviders.of(this).get(MainMedicActivityViewModel::class.java)
        mviewmodel.getUsers(null).observe(this, android.arch.lifecycle.Observer { setList(it) })
        adapter = GenericAdapter(BR.medicuser, R.layout.fragment_search_item)
        view.rcySearch.layoutManager = LinearLayoutManager(activity, LinearLayout.VERTICAL, false)
        view.rcySearch.adapter = adapter
        return view
    }

    private fun setList(list: ArrayList<MedicUser>?) {
        // view!!.progressBar2.visibility = View.GONE
        if (list != null) {
            adapter.newItems(list)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater!!.inflate(R.menu.search_menu, menu)
        val searchManager = activity!!.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu!!.findItem(R.id.search).actionView as SearchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo(activity!!.componentName))
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                processQuery(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                processQuery(newText)
                return true
            }
        })
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun processQuery(query: String?) {
        var values = mviewmodel.users.value
        var newvalues = ArrayList<MedicUser>()
        if (values != null) {
            for (it in values) {

                if (String.format("%s %s", it.nombre, it.apellido).contains(query!!, true)) {
                    newvalues.add(it)
                }
            }
        }
        if (newvalues.size == 0) {
            progressBar6.visibility = View.VISIBLE
        }
        adapter.newItems(newvalues)
        mviewmodel.getUsers(query).observe(this, android.arch.lifecycle.Observer { adapter.lastitems(it!!); progressBar6.visibility = View.INVISIBLE })
    }

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

}
