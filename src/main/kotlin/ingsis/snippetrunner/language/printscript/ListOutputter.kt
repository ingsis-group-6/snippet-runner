package ingsis.snippetrunner.language.printscript

import interpreter.output.Outputter

class ListOutputter(private val outputList: MutableList<String>): Outputter {

    override fun output(text: String) {
        outputList.add(text)
    }
}