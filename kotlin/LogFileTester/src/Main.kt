import java.io.File

fun main() {

    val testDir = "E:\\DOWNLOADS\\Pro-2\\DOS-1643-10daysSystemLogFile\\test\\"
    val fileNameList = mutableListOf("duplicate-backup-20241218-0721.txt", "duplicate-backup-20241217-2340.txt",
        "duplicate-backup-20241217-2107.txt", "duplicate-backup-20241216-2243.txt",
        "duplicate-backup-20241216-1923.txt", "duplicate-backup-20241216-1014.txt",
        "duplicate-backup-20241215-2328.txt")
//    val testFile = File("E:\\DOWNLOADS\\Pro-2\\DOS-1643-10daysSystemLogFile\\test\\duplicate-backup-20241218-0721.txt")
//    var result = logFileSplitter.splitLogFileGetDifferentDateIndex(testFile)
//    println("result $result")
//    logFileSplitter.removeDuplicatedData(result)
    for(fileName in fileNameList) {
        test(testDir.plus(fileName))
    }
}

fun test(filePath: String) {
    val logFileSplitter = LogFileSplitter()
    val testFile = File(filePath)
    val result = logFileSplitter.splitLogFileGetDifferentDateIndex(testFile)
    println("result $result")
    logFileSplitter.removeDuplicatedData(result)
}