package com.courseudemy.organizzeclone.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.helper.widget.Carousel
import com.courseudemy.organizzeclone.R
import com.courseudemy.organizzeclone.databinding.ActivitySliderBinding
import com.courseudemy.organizzeclone.util.SettingsFirebase
import com.google.firebase.auth.FirebaseAuth


class SliderActivity : AppCompatActivity() {

    private val binding by lazy { ActivitySliderBinding.inflate(layoutInflater) }
    private var authentication: FirebaseAuth? = null
    private var images = intArrayOf(R.drawable.um, R.drawable.dois, R.drawable.tres, R.drawable.quatro)
    private var titulo = arrayListOf("Saiba para onde está indo seu dinheiro",
                                     "Organize suas contas de onde estiver",
                                     "Tudo organizado, no celular ou computador",
                                     "Nunca mais esqueça de pagar uma conta",
                                     )
    private var descricao = arrayListOf("Categotizando seus lançamentos e vendo o destino de cada centavo",
                                        "Simples, fácil de usar e grátis",
                                        "Use no seu smartphone e acessando o Organizze pelo site",
                                        "Receba alertas quando quiser")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        authentication = SettingsFirebase().getFirebaseAuth()
        if (authentication?.currentUser != null) {
            startActivity(Intent(this, HomeActivity::class.java))
        }

        //iniciando texto padrão
        binding.tvTitleDescription.text = "Bem Vindo ao Organizze"
        binding.tvDescription.text = "Gerencie o seu dinheiro de forma prática e rápida"

        Handler().postDelayed({
            kotlin.run {
                binding.carousel.setAdapter(object : Carousel.Adapter {
                    override fun count(): Int {
                        // need to return the number of items we have in the carousel
                        return images.size
                    }

                    override fun populate(view: View, index: Int) {
                        // need to implement this to populate the view at the given index
                        (view as ImageView).setImageResource(images[index])
                    }

                    override fun onNewItem(index: Int) {
                        // called when an item is set
                        binding.tvTitleDescription.text = titulo.get(index)
                        binding.tvDescription.text = descricao.get(index)
                        if (index == images.lastIndex){
                            binding.btnNext.visibility = View.VISIBLE
                            binding.ivArrow.visibility = View.GONE
                        }
                    }
                })
            }
        }, 1000)

        binding.btnNext.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}