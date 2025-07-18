package dev.easycloud.terminal.command.completer

import org.jline.reader.LineReader
import org.jline.widget.AutosuggestionWidgets

class CommandSuggestionWidget(reader: LineReader): AutosuggestionWidgets(reader) {

    override fun enable() {
        super.enable()
        setSuggestionType(LineReader.SuggestionType.COMPLETER)
    }

}