package ingsis.snippetrunner.language.printscript

import interpreter.input.Inputter

class StringListInputter(private val inputList: List<String>) : Inputter {
    private var cursor: Int = 0

    override fun getInputLine(): String? {
        if (cursor >= inputList.size) {
            return null
        }

        return inputList[cursor++]
    }
}