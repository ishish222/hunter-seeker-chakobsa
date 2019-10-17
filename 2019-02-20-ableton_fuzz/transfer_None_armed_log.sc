# initial record
Version(2)
Include(scripts/common/initiating.sc)
Include(scripts/common/debugging.sc)

DeployFolders:
    # co tutaj?
    Return

Main:
    RegisterSignals(Exception)

    Call(Intro)
    Call(DeployFolders)

    Call(QemuStartRevert)

    # RR
    Spawn(C:\ProgramData\Ableton\Live 9 Trial\Program\Ableton Live 9 Trial.exe)

    Pause

    GetPIDByMatch(Trial)
    Push
    SetPID

    Call(AttachFileLog)

    Pause

    QemuSave(armed)
    goto(finish)

finish:
    QemuQuit

