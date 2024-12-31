import java.io.BufferedReader
import java.io.File
import java.io.FileReader

class LogFileSplitter {
    private enum class LineDateFormatType {
        Unknown,
        FullDateType,
        ShortDateType
    }

    fun splitLogFileGetDifferentDateIndex(file: File): MutableList<Pair<String, Int>> {
        val lastOccurrences = mutableListOf<Pair<String, Int>>()
        var lineNumber = 0
        var currentDate: String? = null

        BufferedReader(FileReader(file)).use { reader ->
            reader.lineSequence().forEach { line ->
                lineNumber++
                val parsedDate = parseLineDate(line)
                if(currentDate != null && parsedDate != currentDate){
//                    println("currentDate:$currentDate, parsedDate:$parsedDate, lineNumber:$lineNumber")
                    lastOccurrences.add(Pair(currentDate!!, lineNumber))
                }
                if(parsedDate != currentDate) {
                    currentDate = parsedDate
                }
            }
            currentDate?.let { lastOccurrences.add(Pair(it, lineNumber)) }
        }
        lastOccurrences.sortBy { it.second }
        return lastOccurrences
    }

    private fun parseLineDate(line: String): String? {
        return when (getDateFormatType(line)) {
            LineDateFormatType.Unknown -> null
            LineDateFormatType.ShortDateType -> SystemLogSharedData.getDateStringFromShortDateHeadLine(line)
            LineDateFormatType.FullDateType -> SystemLogSharedData.getDateStringFromLongDateHeadLine(line)
        }
    }

    private fun getDateFormatType(line : String) : LineDateFormatType {
        return if (isFullDateFormat(line)) {
            LineDateFormatType.FullDateType
        } else if(isShortDateFormat(line)) {
            LineDateFormatType.ShortDateType
        } else {
            LineDateFormatType.Unknown
        }
    }

    private fun isFullDateFormat(line: String) : Boolean {
        val parts = line.split(" ")
        return if(parts.size < 2) {
            false
        } else {
            val dateParts = parts[0].split("-")
            dateParts.size == 3 && dateParts[0].length == 4 &&
                    dateParts[1].length == 2 &&
                    dateParts[2].length == 2 &&
                    isTimeFormat(parts[1])
        }
    }

    private fun isShortDateFormat(line: String) : Boolean {
        val parts = line.split(" ")
        return if(parts.size<2) {
            false
        } else {
            val dateParts = parts[0].split("-")
            dateParts.size == 2 && dateParts[0].length == 2 &&
                    dateParts[1].length == 2 &&
                    isTimeFormat(parts[1])
        }
    }

    private fun isTimeFormat(time: String) : Boolean {
        val parts = time.split(".") //HH:MM:SS.mmm
        return if(parts.size != 2) {
            false
        } else {
            val timeParts = parts[0].split(":")
            timeParts.size == 3 && timeParts[0].length == 2 &&
                    timeParts[1].length == 2 &&
                    timeParts[2].length == 2 &&
                    parts[1].length == 3
        }
    }

    fun removeDuplicatedData(pairs: MutableList<Pair<String, Int>>) {
        if(pairs.isEmpty()) return
        var currentPair = pairs.first()
        val resultPairs = mutableListOf<Pair<String, Int>>()
        for (i in 1..<pairs.size) {
            if(currentPair.first == pairs[i].first) {
                currentPair = pairs[i]
            } else {
                resultPairs.add(currentPair)
                currentPair = pairs[i]
            }
        }
        resultPairs.add(pairs.last())

        println("removeDuplicatedData: $resultPairs")
    }
}