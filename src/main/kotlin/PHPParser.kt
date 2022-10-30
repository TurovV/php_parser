class PHPFunctionInfo(val argCount: Int, val name : String, val args : List<String>, val comment: String) : java.io.Serializable {
    val type: String = "function"
}

class PHPParser(val text : String) {

    private val regex : Regex
    private val argRegex : Regex = Regex("[A-Za-z0-9_]+")
    private val argsRegex : Regex = Regex("\\(($argRegex)?(, *$argRegex)*\\)")

    init {
        regex = Regex("function $argRegex${argsRegex}[ \t\n]*\\{[ \t]*(//)?[^\n]*" )
    }

    private fun getFunctionLines() : List<String> {
        return regex.findAll(text).map { element -> element.value }.toList()
    }

    private fun getFunctionInfo(line : String) : PHPFunctionInfo {
        val args = argsRegex.find(line)?.value ?: ""
        val argList = argRegex.findAll(args).map{element -> element.value}.toList()
        val name = argRegex.findAll(line).map{element -> element.value}.toList()[1]
        val comment = Regex("(//)[^\n]*").find(line)?.value
        return PHPFunctionInfo(argList.count(), name, argList, comment?.drop(2) ?: "")
    }

    fun getAllFunctionsInfo() : List<PHPFunctionInfo> {
        var functions = getFunctionLines()
        return functions.map {element -> getFunctionInfo(element)}
    }
}