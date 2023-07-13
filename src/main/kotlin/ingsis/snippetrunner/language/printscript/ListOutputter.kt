package ingsis.snippetrunner.language.printscript

import common.io.Outputter

class ListOutputter(private val outputList: MutableList<String>): Outputter {

    override fun output(text: String) {
        outputList.add(text)
    }
}