package com.correro.alejandro.tfg.ui.medic.searchfragment


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.text.TextUtils
import android.view.*
import android.widget.LinearLayout
import com.correro.alejandro.tfg.R
import com.correro.alejandro.tfg.data.api.models.medicusersresponse.MedicUser
import com.correro.alejandro.tfg.ui.medic.MainMedicActivityViewModel
import kotlinx.android.synthetic.main.fragment_search.view.*

class SearchFragment : Fragment() {

    private lateinit var mviewmodel: MainMedicActivityViewModel

    private lateinit var adapter: SearchAdapter

    private lateinit var searchView: SearchView

    private lateinit var listOld: ArrayList<MedicUser>

    private var endCall: Boolean = false
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_search, container, false)
        mviewmodel = ViewModelProviders.of(this).get(MainMedicActivityViewModel::class.java)
        adapter = SearchAdapter(ArrayList(), click())
        (activity as AppCompatActivity).supportActionBar!!.title="Pacientes"

        adapter.items.add(null)
        adapter.notifyItemInserted(adapter.items.size - 1)
        mviewmodel.getUsers(null).observe(this, Observer { setList(it) })
        view.rcySearch.layoutManager = LinearLayoutManager(activity, LinearLayout.VERTICAL, false)
        view.rcySearch.adapter = adapter
        return view
    }


    fun click(): (MedicUser) -> Unit {
        return {
            SearchDetailActivity.start(activity!!, it.id.toInt())
        }

    }

    private fun setList(list: ArrayList<MedicUser>?) {
        adapter.items.remove(null)
        adapter.notifyItemRemoved(adapter.items.size)



        if (list != null) {
            adapter.newItems(ArrayList(list))
            listOld = list
            endCall = true;
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater!!.inflate(R.menu.search_menu, menu)
        searchView = menu!!.findItem(R.id.search).actionView as SearchView
        //searchView.isIconified = true
        searchView.queryHint="Paciente..."
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
        if (endCall) {
            mviewmodel.composite.dispose()
            mviewmodel.users.removeObservers(this@SearchFragment)
            if (TextUtils.isEmpty(query)) {
                if (::listOld.isInitialized)
                    adapter.newItems(ArrayList(listOld))
            } else {
                var newvalues = ArrayList<MedicUser>()
                if (::listOld.isInitialized) {
                    for (it in listOld) {

                        if (String.format("%s %s", it.nombre, it.apellido).contains(query!!, true)) {
                            newvalues.add(it)
                        }
                    }
                }

                adapter.newItems(ArrayList(newvalues))
                adapter.items.add(null)
                adapter.notifyItemInserted(adapter.items.size - 1)
                mviewmodel.getUsers(query).observe(this, android.arch.lifecycle.Observer { it -> addItems(it) })
            }
        }
    }

    private fun addItems(it: ArrayList<MedicUser>?) {
        adapter.items.remove(null)
        adapter.notifyItemRemoved(adapter.items.size)


        var removeitems = ArrayList<MedicUser>()
        for (it3 in it!!) {
            for (it2 in adapter.items) {

                if (it2!!.id == it3.id) {
                    removeitems.add(it2)
                }
            }
        }
        it.removeAll(removeitems)
        if (it.size > 0)
            adapter.lastitems(it)

    }

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

}
