package ksp.kos.ideaplugin

import ksp.kos.ideaplugin.psi.KerboScriptTypes

object Magic {
    val keywords = hashSetOf(
            KerboScriptTypes.NOT,
            KerboScriptTypes.AND,
            KerboScriptTypes.OR,
            KerboScriptTypes.TRUEFALSE,
            KerboScriptTypes.SET,
            KerboScriptTypes.TO,
            KerboScriptTypes.IS,
            KerboScriptTypes.IF,
            KerboScriptTypes.ELSE,
            KerboScriptTypes.UNTIL,
            KerboScriptTypes.STEP,
            KerboScriptTypes.DO,
            KerboScriptTypes.LOCK,
            KerboScriptTypes.UNLOCK,
            KerboScriptTypes.PRINT,
            KerboScriptTypes.AT,
            KerboScriptTypes.ON,
            KerboScriptTypes.TOGGLE,
            KerboScriptTypes.WAIT,
            KerboScriptTypes.WHEN,
            KerboScriptTypes.THEN,
            KerboScriptTypes.OFF,
            KerboScriptTypes.STAGE,
            KerboScriptTypes.CLEARSCREEN,
            KerboScriptTypes.ADD,
            KerboScriptTypes.REMOVE,
            KerboScriptTypes.LOG,
            KerboScriptTypes.BREAK,
            KerboScriptTypes.PRESERVE,
            KerboScriptTypes.DECLARE,
            KerboScriptTypes.DEFINED,
            KerboScriptTypes.LOCAL,
            KerboScriptTypes.GLOBAL,
            KerboScriptTypes.PARAMETER,
            KerboScriptTypes.FUNCTION,
            KerboScriptTypes.RETURN,
            KerboScriptTypes.SWITCH,
            KerboScriptTypes.COPY,
            KerboScriptTypes.FROM,
            KerboScriptTypes.RENAME,
            KerboScriptTypes.VOLUME,
            KerboScriptTypes.FILE,
            KerboScriptTypes.DELETE,
            KerboScriptTypes.EDIT,
            KerboScriptTypes.RUN,
            KerboScriptTypes.ONCE,
            KerboScriptTypes.COMPILE,
            KerboScriptTypes.LIST,
            KerboScriptTypes.REBOOT,
            KerboScriptTypes.SHUTDOWN,
            KerboScriptTypes.FOR,
            KerboScriptTypes.UNSET,
            KerboScriptTypes.CHOOSE,
            KerboScriptTypes.ATSIGN,
            KerboScriptTypes.LAZYGLOBAL
    )
}