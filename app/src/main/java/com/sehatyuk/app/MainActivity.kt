package com.sehatyuk.app

import android.os.Bundle
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var etNama: EditText
    private lateinit var etBerat: EditText
    private lateinit var etTinggi: EditText
    private lateinit var etUmur: EditText
    private lateinit var btnHitungBMI: TextView
    private lateinit var btnHitungBMR: TextView
    private lateinit var tvHasil: TextView
    private lateinit var layoutHasil: LinearLayout
    private lateinit var scrollView: ScrollView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etNama = findViewById(R.id.etNama)
        etBerat = findViewById(R.id.etBerat)
        etTinggi = findViewById(R.id.etTinggi)
        etUmur = findViewById(R.id.etUmur)
        btnHitungBMI = findViewById(R.id.btnHitungBMI)
        btnHitungBMR = findViewById(R.id.btnHitungBMR)
        tvHasil = findViewById(R.id.tvHasil)
        layoutHasil = findViewById(R.id.layoutHasil)
        scrollView = findViewById(R.id.scrollView)

        // Auto scroll ke field saat fokus
        val fields = listOf(etNama, etBerat, etTinggi, etUmur)
        fields.forEach { field ->
            field.setOnFocusChangeListener { view, hasFocus ->
                if (hasFocus) {
                    scrollView.post {
                        scrollView.smoothScrollTo(0, view.bottom)
                    }
                }
            }
        }

        btnHitungBMI.setOnClickListener {
            if (validateInput()) {
                val nama = etNama.text.toString()
                val berat = etBerat.text.toString().toDouble()
                val tinggi = etTinggi.text.toString().toDouble()
                val hasil = calculateBMI(berat, tinggi)
                tampilkanHasil("Halo, $nama!\n\n$hasil")
                scrollView.post {
                    scrollView.smoothScrollTo(0, layoutHasil.top)
                }
            }
        }

        btnHitungBMR.setOnClickListener {
            if (validateInput()) {
                val nama = etNama.text.toString()
                val berat = etBerat.text.toString().toDouble()
                val tinggi = etTinggi.text.toString().toDouble()
                val umur = etUmur.text.toString().toInt()
                val hasil = calculateBMR(berat, tinggi, umur)
                val hasilFormatted = String.format("%.2f", hasil)
                tampilkanHasil(
                    "Halo, $nama!\n\n" +
                            "BMR Anda: $hasilFormatted kkal/hari\n\n" +
                            "Artinya, tubuh Anda membutuhkan sekitar $hasilFormatted kkal\n" +
                            "per hari hanya untuk fungsi dasar tubuh (istirahat total)."
                )
                scrollView.post {
                    scrollView.smoothScrollTo(0, layoutHasil.top)
                }
            }
        }
    }

    // FUNCTION 1: Validasi Input
    private fun validateInput(): Boolean {
        val nama = etNama.text.toString().trim()
        val berat = etBerat.text.toString().trim()
        val tinggi = etTinggi.text.toString().trim()
        val umur = etUmur.text.toString().trim()

        if (nama.isEmpty() || berat.isEmpty() || tinggi.isEmpty() || umur.isEmpty()) {
            Toast.makeText(this, "Harap isi semua kolom terlebih dahulu!", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    // FUNCTION 2: Hitung BMI & Kategori
    private fun calculateBMI(weight: Double, height: Double): String {
        val heightInMeter = height / 100
        val bmi = weight / (heightInMeter * heightInMeter)
        val bmiFormatted = String.format("%.2f", bmi)

        val kategori = when {
            bmi < 18.5 -> "Kurus (Underweight)"
            bmi < 25.0 -> "Normal"
            bmi < 30.0 -> "Kelebihan Berat Badan (Overweight)"
            else       -> "Obesitas"
        }

        return "BMI Anda: $bmiFormatted\nKategori: $kategori"
    }

    // FUNCTION 3: Hitung BMR
    private fun calculateBMR(weight: Double, height: Double, age: Int): Double {
        return (10 * weight) + (6.25 * height) - (5 * age) + 5
    }

    // HELPER: Tampilkan hasil di card
    private fun tampilkanHasil(pesan: String) {
        tvHasil.text = pesan
        layoutHasil.visibility = android.view.View.VISIBLE
    }
}