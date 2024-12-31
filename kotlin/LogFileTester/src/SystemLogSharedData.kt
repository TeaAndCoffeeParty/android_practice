import java.io.File
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoField
import java.time.temporal.TemporalAccessor
import java.util.Date
import java.util.Locale

object SystemLogSharedData {
    private val sdcardDirectoryPath = "./"
    const val logFileSuffix = ".txt";
    /* fetch logcat */
    const val tempDirectoryName = "systemLogTemp"
    val tempDirectoryPath = sdcardDirectoryPath.plus("/$tempDirectoryName")

    const val duplicateTempFileHeader = "duplicate-backup-";
    const val duplicateTmepFileDateFormatString = "yyyyMMdd-HHmm"
    val duplicateTempFileNameLength = duplicateTempFileHeader.length +
            duplicateTmepFileDateFormatString.length + logFileSuffix.length

    fun isDuplicateTempFileNameFormat(fileName: String):Boolean {
        return fileName.startsWith(duplicateTempFileHeader) &&
                fileName.endsWith(logFileSuffix) &&
                fileName.length == duplicateTempFileNameLength
    }
    fun getDuplicateTempFileNameToDate(name: String) : Date? {
        val dateFormat = SimpleDateFormat(duplicateTmepFileDateFormatString, Locale.getDefault())
        val fileName = name.drop(duplicateTempFileHeader.length)
        val dateTimeString = fileName.substring(0, fileName.length - logFileSuffix.length)
        return dateFormat.parse(dateTimeString)
    }

    fun generateDuplicateTempFileNamePathInCurrentDate() : String {
        val dateFormat = SimpleDateFormat(duplicateTmepFileDateFormatString, Locale.getDefault())
        val currentDataTime = dateFormat.format(Date())
        val tempLogFileName = "$duplicateTempFileHeader$currentDataTime$logFileSuffix"
        return tempDirectoryPath + File.separator + tempLogFileName
    }

    val systemLogFilePath = sdcardDirectoryPath.plus("/system_log.txt")
    const val maxByteForSystemLogFile = 100.0 * 1024 * 1024

    fun getOutputFileNamePath(dateString: String) : String {
        val outputFileName = backupLogFileNameHead + dateString.replace("-", "") + logFileSuffix
        val outputFileNamePath = backupDirectoryPath.plus("/$outputFileName")
        return outputFileNamePath
    }

    /* split logcat backup file */
    const val backupDirectoryName = "systemLogBackup"
    val backupDirectoryPath = sdcardDirectoryPath.plus("/$backupDirectoryName")

    const val backupLogFileNameHead = "SystemLogFile_"
    const val backupDateFormatString = "yyyyMMdd"
    val backupLogFileNameLength = backupLogFileNameHead.length + backupDateFormatString.length +
            logFileSuffix.length
    val backupDateFormat = SimpleDateFormat(backupDateFormatString, Locale.getDefault())
    const val maxBackupDirectoryStorageLimitInByte = 400.0 * 1024 * 1024

    fun isOutputLogFileNameFormat(fileName: String) : Boolean {
        return fileName.startsWith(backupLogFileNameHead) &&
                fileName.endsWith(logFileSuffix) &&
                fileName.length == backupLogFileNameLength
    }

    fun getOutputLogFileNameToDate(name: String) : Date? {
        val dateFormat = SimpleDateFormat(backupDateFormatString, Locale.getDefault())
        val fileName = name.drop(backupLogFileNameHead.length)
        val dateTimeString = fileName.substring(0, fileName.length - logFileSuffix.length)
        return dateFormat.parse(dateTimeString)
    }


    /* Compatible with old dates */
    const val linelongDateFormatHeadString = "yyyy-MM-dd"
    const val lineShortDateFormatHeadString = "MM-dd"

    fun getDateStringFromShortDateHeadLine(line: String) : String {
        return convertShortDateHeadToLingHead(line).substring(0, linelongDateFormatHeadString.length)
    }

    fun getDateStringFromLongDateHeadLine(line: String) : String {
        return line.substring(0, linelongDateFormatHeadString.length)
    }

    private fun convertShortDateHeadToLingHead(line: String) : String {
        val dateString = line.substring(0, lineShortDateFormatHeadString.length)
        val contentString = line.substring(lineShortDateFormatHeadString.length)
        val newLine = completeDate(dateString) + contentString
        return newLine
    }

    private fun completeDate(dateStr: String): String {
        val formatter = DateTimeFormatter.ofPattern(lineShortDateFormatHeadString)
        val temporalAccessor: TemporalAccessor = formatter.parse(dateStr)

        val now = LocalDate.now()
        val currentYear = now.year

        val givenDate = LocalDate.of(currentYear,
            temporalAccessor[ChronoField.MONTH_OF_YEAR],
            temporalAccessor[ChronoField.DAY_OF_MONTH]
        )

        val completedDate = if (givenDate.isAfter(now)) {
            LocalDate.of(currentYear - 1, givenDate.month, givenDate.dayOfMonth)
        } else {
            givenDate
        }

        return completedDate.format(DateTimeFormatter.ofPattern(linelongDateFormatHeadString))
    }

}