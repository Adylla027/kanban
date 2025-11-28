package com.adylla.task.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.adylla.task.R
import com.adylla.task.databinding.FragmentFormTaskBinding
import com.adylla.task.util.initToolbar
import com.adylla.task.util.showBottomSheet
import com.google.firebase.firestore.FirebaseFirestore

class FormTaskFragment : Fragment() {
    private var _binding: FragmentFormTaskBinding? = null
    private val binding get() = _binding!!

    private lateinit var firestore: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFormTaskBinding.inflate(inflater, container, false)

        firestore = FirebaseFirestore.getInstance()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar(binding.toolbar)
        initListener()
    }

    private fun initListener(){
        binding.buttonSave.setOnClickListener {
            valideData()
        }
    }

    private fun valideData(){
        val description = binding.editTextDescricao.text.toString().trim()
        if (description.isNotBlank()){
            saveTask(description)
            Toast.makeText(requireContext(),"Tudo OK!", Toast.LENGTH_SHORT).show()
        } else {
            showBottomSheet(message = getString(R.string.description_empty_form_task_fragment))
        }
    }

    private fun saveTask(description: String){
        val id = firestore.collection("tasks").document().id

        val task = hashMapOf(
            "id" to id,
            "description" to description
        )

        firestore.collection("tasks")
            .document(id)
            .set(task)
            .addOnSuccessListener {
                Toast.makeText(requireContext(),"tarefa salva", Toast.LENGTH_SHORT).show()
                binding.editTextDescricao.setText("")



            }
            .addOnFailureListener {
                erro->
                Toast.makeText(requireContext(),"Erro: ${erro.message}", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
