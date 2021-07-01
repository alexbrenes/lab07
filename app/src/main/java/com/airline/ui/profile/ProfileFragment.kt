package com.airline.ui.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.lab04.R
import com.lab04.databinding.ActivityProfileBinding
import com.lab04.databinding.FragmentProfileBinding
import com.lab04.logic.User

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    val profileViewModel: ProfileViewModel

    init {
        profileViewModel = ProfileViewModel()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(layoutInflater)

        val bundle = requireActivity().intent.extras
        val user = bundle?.get("user") as User
        profileViewModel.user.value = user

        binding.emailProfile.text = user.email
        binding.newPasswordProfile.setText(user.password)
        binding.birthdayProfile.setText(user.birthday)
        binding.telephoneWorkProfile.setText(user.workphone)
        binding.telephonePersonalProfile.setText(user.phone)
        binding.lastnameProfile.setText(user.lastname)
        binding.nameProfile.setText(user.name)
        binding.addressProfile.setText(user.address)

        profileViewModel.user.observe(viewLifecycleOwner) {
            requireActivity().intent.removeExtra("user")
            requireActivity().intent.putExtra("user", it)
            val toast = Toast.makeText(
                context,
                "Perfil actualizado con éxito",
                Toast.LENGTH_SHORT
            )
            toast.show()
        }

        binding.chnPassProBtn.setOnClickListener {

            val address = binding.addressProfile.text.toString()
            val birthday = binding.birthdayProfile.text.toString()
            val email = binding.emailProfile.text.toString()
            val lastname = binding.lastnameProfile.text.toString()
            val name = binding.nameProfile.text.toString()
            val phoneWork = binding.telephoneWorkProfile.text.toString()
            val phone = binding.telephonePersonalProfile.text.toString()
            val password = binding.newPasswordProfile.text.toString()

            val newUser = User()
            newUser.password = password
            newUser.email = email
            newUser.address = address
            newUser.birthday = birthday + "T06:00:00.000Z"
            newUser.lastname = lastname
            newUser.name = name
            newUser.phone = phone
            newUser.workphone = phoneWork

            profileViewModel.editUser(newUser)


        }
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        profileViewModel.open(lifecycleScope)
    }

    override fun onPause() {
        super.onPause()
        profileViewModel.close()
    }

}