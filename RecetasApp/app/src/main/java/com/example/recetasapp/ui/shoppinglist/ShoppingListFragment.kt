package com.example.recetasapp.ui.shoppinglist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recetasapp.R
import com.example.recetasapp.ui.shoppinglist.adapters.ShoppingItemAdapter
import com.example.recetasapp.utils.PreferencesHelper
import com.example.recetasapp.utils.showToast

class ShoppingListFragment : Fragment() {

    private lateinit var viewModel: ShoppingListViewModel
    private lateinit var adapter: ShoppingItemAdapter

    private lateinit var recyclerView: RecyclerView
    private lateinit var exportButton: Button
    private lateinit var clearButton: Button
    private lateinit var emptyView: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_shopping_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewModel()
        initViews(view)
        setupRecyclerView()
        setupListeners()
        setupObservers()
    }

    private fun initViewModel() {
        val preferencesHelper = PreferencesHelper(requireContext())
        val factory = ShoppingListViewModelFactory(preferencesHelper)
        viewModel = ViewModelProvider(this, factory)[ShoppingListViewModel::class.java]
    }

    private fun initViews(view: View) {
        recyclerView = view.findViewById(R.id.shopping_recycler_view)
        exportButton = view.findViewById(R.id.export_button)
        clearButton = view.findViewById(R.id.clear_button)
        emptyView = view.findViewById(R.id.empty_view)
    }

    private fun setupRecyclerView() {
        adapter = ShoppingItemAdapter(
            onItemChecked = { item, isChecked ->
                viewModel.updateItemStatus(item, isChecked)
            },
            onItemDeleted = { item ->
                viewModel.deleteItem(item)
            }
        )

        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@ShoppingListFragment.adapter
        }
    }

    private fun setupListeners() {
        exportButton.setOnClickListener {
            viewModel.exportList()
        }

        clearButton.setOnClickListener {
            viewModel.clearList()
        }
    }

    private fun setupObservers() {
        viewModel.shoppingItems.observe(viewLifecycleOwner) { items ->
            adapter.submitList(items)

            if (items.isEmpty()) {
                emptyView.visibility = View.VISIBLE
                recyclerView.visibility = View.GONE
            } else {
                emptyView.visibility = View.GONE
                recyclerView.visibility = View.VISIBLE
            }
        }

        viewModel.message.observe(viewLifecycleOwner) { message ->
            message?.let {
                requireContext().showToast(it)
            }
        }
    }
}