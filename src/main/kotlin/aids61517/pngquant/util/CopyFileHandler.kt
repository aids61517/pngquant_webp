package aids61517.pngquant.util

import aids61517.pngquant.OSSourceChecker
import aids61517.pngquant.PngquantHelper
import aids61517.pngquant.data.ExePath
import aids61517.pngquant.data.OSSource
import okio.buffer
import okio.sink
import okio.source
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

abstract class CopyFileHandler {
    companion object {
        fun create(osSource: OSSource): CopyFileHandler {
            return when (osSource) {
                OSSource.WINDOWS -> WindowsCopyFileHandler()
                OSSource.MAC -> MacCopyFileHandler()
                else -> throw IllegalStateException("Unsupported OS")
            }
        }
    }

    abstract fun execute(exePath: ExePath): Path

    protected fun copyExe(exePath: ExePath): Path {
        val exeFilePath = Paths.get(exePath.targetPath)
        javaClass.getResourceAsStream(exePath.source)
            ?.let {
                println("CopyFileHandler read file successfully")
                if (Files.notExists(exeFilePath)) {
                    Files.createFile(exeFilePath)
                }

                it.source()
                    .buffer()
                    .use { source ->
                        Files.newOutputStream(exeFilePath)
                            .sink()
                            .buffer()
                            .use { sink ->
                                sink.writeAll(source)
                                println("CopyFileHandler create exe successfully, path = $exeFilePath")
                            }
                    }
            }

        return exeFilePath
    }
}