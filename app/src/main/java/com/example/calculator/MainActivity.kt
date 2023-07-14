package com.example.calculator

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.Button
import com.example.calculator.databinding.ActivityMainBinding
import net.objecthunter.exp4j.Expression
import net.objecthunter.exp4j.ExpressionBuilder

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    var lastNumeric = false
    var stateError = false
    var lastDot = false
    var equ = false

    private lateinit var expression: Expression

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnEqu.setOnClickListener { onEquClick() }
        binding.btnAc.setOnClickListener { onAcClick() }
    }

    fun onEquClick() {
        val currentSize = binding.resultTv.textSize
        val newSize = currentSize + 20
        binding.resultTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, newSize)
        equ = true
    }

    fun onDigitClick(view: View) {
        if (equ){
            binding.dataHisView.text = binding.dataTv.text
            binding.resultHisView.text = binding.resultTv.text
            onAcClick()
            equ = false
        }
        if(stateError){
            binding.dataTv.text = (view as Button).text
            stateError = false
        } else {
            binding.dataTv.append((view as Button).text)
        }

        lastNumeric = true
        onEqual()
    }

    fun onOpClick(view: View) {
        if(!stateError && lastNumeric) {
            binding.dataTv.append((view as Button).text)
            lastDot = false
            lastNumeric = false
            onEqual()
        }
    }

    fun onAcClick() {
        binding.dataTv.text = ""
        binding.resultTv.text = ""
        stateError = false
        lastDot = false
        lastNumeric = false
        binding.resultTv.visibility = View.GONE
    }

    @SuppressLint("SetTextI18n")
    fun onEqual() {
        if(lastNumeric && !this.stateError){
            val txt = binding.dataTv.text.toString()

            expression = ExpressionBuilder(txt).build()

            try {
                val result = expression.evaluate()
                binding.resultTv.visibility = View.VISIBLE
                binding.resultTv.text = "=$result"
            } catch (ex: ArithmeticException){
                Log.e("evaluate error", ex.toString())
                binding.resultTv.text = "Error"
                stateError = true
                lastNumeric = false
            }
        }
    }
}