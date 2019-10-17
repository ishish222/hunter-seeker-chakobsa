# initial record
Version(2)
Include(scripts/common/initiating.sc)
Include(scripts/common/debugging.sc)

DeployFolders:
    # co tutaj?
    Return

Main:
    Call(Intro)
    Call(DeployFolders)

    Call(QemuStartRevert)

    RunCmd(copy /Y c:\Windows\System32 \\10.0.2.4\qemu\output)

    Pause
    goto(finish)

Exception:
finish:
    QemuQuit

