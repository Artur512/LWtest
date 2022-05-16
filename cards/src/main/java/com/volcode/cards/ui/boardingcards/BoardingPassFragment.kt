package com.volcode.cards.ui.boardingcards

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.volcode.cards.R
import com.volcode.cards.databinding.BoardingPassFragmentBinding
import com.volcode.cards.ui.boardingcards.BoardingViewState.InProgress
import com.volcode.cards.ui.boardingcards.BoardingViewState.ShowCardsState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BoardingPassFragment : Fragment() {

    private val viewModel: BoardingCardsViewModel by viewModels()
    private var _binding: BoardingPassFragmentBinding? = null
    private val binding get() = _binding!!
    private val cardAdapter = CardAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = BoardingPassFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            stopList.adapter = cardAdapter
            sortButton.setOnClickListener {
                viewModel.sortStops()
            }
            stopList.layoutManager = LinearLayoutManager(requireContext())
        }

        viewModel.state.observe(viewLifecycleOwner) {
            renderView(it)
        }
    }

    private fun renderView(state: BoardingViewState) {
        with(binding) {
            title.text =
                if (state is ShowCardsState) getString(R.string.cards_unsorted_elements_title)
                else getString(R.string.cards_sorted_elements_title)
            sortButton.isEnabled = state is ShowCardsState
        }
        when (state) {
            is ShowCardsState -> {
                showCards(state)
            }
            InProgress -> {
                showProgressBar()
            }
        }
    }

    private fun showProgressBar() {
        with(binding) {
            sortButton.isEnabled = false
            sortButton.visibility = View.GONE
            stopList.visibility = View.GONE
            progressBar.visibility = View.VISIBLE
        }
    }

    private fun showCards(state: ShowCardsState) {
        val titleRes = if (state.sorted) R.string.boarding_cards_title_sorted else R.string.boarding_cards_title

        with(binding) {
            title.text = getString(titleRes)
            sortButton.isEnabled = !state.sorted
            sortButton.visibility = View.VISIBLE
            stopList.visibility = View.VISIBLE
            progressBar.visibility = View.GONE
        }
        cardAdapter.submitList(state.cards)
    }
}