package camp.nextstep.edu.calculator

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import camp.nextstep.edu.calculator.domain.Calculator
import camp.nextstep.edu.calculator.domain.Expression
import camp.nextstep.edu.calculator.domain.Operator
import camp.nextstep.edu.calculator.domain.usecase.GetAllResultsUseCase
import camp.nextstep.edu.calculator.domain.usecase.SaveResultUseCase


class CalculatorViewModel(
    private val saveResultUseCase: SaveResultUseCase,
    private val getAllResultsUseCase: GetAllResultsUseCase
) : ViewModel() {

    private val calculator = Calculator()

    private var expression = Expression.EMPTY

    private val _text: MutableLiveData<String> = MutableLiveData("")
    val text: LiveData<String>
        get() = _text

    private val _warning = SingleLiveEvent<Unit>()
    val warning: LiveData<Unit>
        get() = _warning


    fun getResults() = getAllResultsUseCase()

    fun addToExpression(operand: Int) {
        expression += operand
        showExpression(expression)
    }

    fun addToExpression(operator: Operator) {
        expression += operator
        showExpression(expression)
    }

    fun removeLast() {
        expression = expression.removeLast()
        showExpression(expression)
    }

    fun calculate() {
        val result = calculator.calculate(expression.toString())
        if (result == null) {
            showIncompleteExpressionError()
        } else {
            saveResultUseCase(expression, result)
            expression = Expression(listOf(result))
            showResult(result)
        }
    }


    private fun showExpression(expression: Expression) {
        _text.postValue(expression.toString())
    }

    private fun showResult(result: Int) {
        _text.postValue(result.toString())
    }

    private fun showIncompleteExpressionError() {
        _warning.postValue(Unit)
    }
}
