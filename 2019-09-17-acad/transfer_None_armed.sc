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
    Spawn(C:\Program Files\Autodesk\AutoCAD 2018\acad.exe)

    Pause

    ListProcesses
    GetPIDByMatch(acad.exe)
    Push
    SetPID

    Call(AttachScrLog)

    Pause

    QemuSave(armed)
    goto(finish)

Exception:
finish:
    QemuQuit

