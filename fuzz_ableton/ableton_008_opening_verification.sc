# initial record
Version(2)
Include(scripts/common_2/initiating.sc)
Include(scripts/common_2/debugging.sc)

DeployFolders:
    GlobPattern(fuzz_ableton)
    HostDeployInputGlob

    Return

Main:
    RegisterSignals(Exception)

    Call(Intro)
    Call(DeployFolders)

    SetInDir(C:\ProgramData\Ableton\Live 9 Trial\Program)
    SetSampleFile(Ableton Live 9 Trial.exe)

    Call(QemuStart)

    Spawn(C:\ProgramData\Ableton\Live 9 Trial\Program\Ableton Live 9 Trial.exe)
    Pause

    GetPIDByMatch(Ableton Live 9 Trial.exe)
    Push
    SetPID

    Call(AttachFileLog)

    #RegisterReactions(self+0xe93211,ST,0x0)
    RegisterReactions(self+0xdabfe5,ST,0x0)
    RegisterReactions(self+0xdabfe8,finish,0x0)
    EnableReaction(ST)
    EnableReaction(finish)

    Continue=

ST:
    DisableReaction(ST)
    CurrentTID
    Push
    SetPriorityHigh

    DumpMemory
    SecureAllSections
    StartTrace

    Call(RegisterEnableBuiltin)

    Continue=

Default:
    Continue=

EX:
    Continue(0x80010001)=

Exception:
    Interrupt
finish:
    QemuQuit

