package com.example.perfectcalc.util

enum class Operator(val isBinary: Boolean){
    ADD( true),
    SUB( true),
    MUL( true),
    DIV( true),
    POW(true),

    SQRT(false),
    ROOT(false),
    REVERSE( false),

    NONE(false),
    EQUALS(false)
}

enum class EditorCommands {
    CHANGE_SIGN,
    BACKSPACE,
    CLEAR,
    ADD_DOT,
    ADD_DIVIDER,
    ADD_DIGIT,
}

enum class MemoryCommands {
    MCLEAR,
    MSAVE,
    MRESTORE,
    MADD
}

abstract class Command

class MemoryCommand (val command: MemoryCommands) : Command() {}

class EditorCommand (val command: EditorCommands) : Command() {}
