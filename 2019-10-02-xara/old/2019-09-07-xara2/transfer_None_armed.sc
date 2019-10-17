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
    Spawn(C:\Program Files\Xara\Xara Web Designer 11 Premium\WebDesigner.exe)

    Pause

    GetPIDByMatch(WebDesigner)
    Push
    SetPID

    Call(AttachScrLog)

    Pause

    QemuSave(armed)
    goto(finish)

Exception:
finish:
    QemuQuit

