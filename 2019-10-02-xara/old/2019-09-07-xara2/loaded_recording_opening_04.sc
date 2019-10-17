# initial record
Version(2)
Include(scripts/common/initiating.sc)
Include(scripts/common/debugging.sc)

DeployFolders:
    GlobPattern(_FUZZING_SAMPLES/xara/originals_01/*.xar)
    HostDeployInputGlob

    Return

Main:
    RegisterSignals(Exception)

    Call(Intro)
    Call(DeployFolders)

    QemuStart
    QemuLoad(loaded)

    GetPIDByMatch(acad.exe)
    Push
    SetPID

    Call(AttachFileLog)

    Main_Copy:
        RunCommand(copy \\10.0.2.4\qemu\input\origin04.xar c:\users\john\desktop\sample.xar)
        CheckStrStr(cannot)=(Y:Main_Sleep)
        CheckStrStr(network)=(Y:Main_Sleep)

    RegisterReactions(accore.dll+0x710a05,ST,0x0)
    RegisterReactions(accore.dll+0x710a0a,EN,0x0)
    EnableReaction(ST)
    EnableReaction(EN)

    RunCommand(start c:\users\john\desktop\sample.xar)
    Continue=

    Main_Sleep:
        Wait(1)
        goto(Main_Copy)

ST:
    Call(StartAnalysis)
    Continue=

StartAnalysis:
    TracerDebugLogEnable
    DumpMemory
    SecureAllSections
    #AddScannedLocation(ESP:0x50)

    #Call(RegisterEnableBuiltin)

    #ResizeOutBuffer(0x10000000)
    #ResizeModBuffer(0x1000000)
    #ChangeInterval(10000000)

    #Pause
    #TracerStartTrace
    TracerStartTraceLight
    #TracerStartTraceDebug
    Return

RE:
    TracerGetExceptionCode
    CheckStrStr(0xe06d7363)=(Y:RE_continue)
    CheckStrStr(0x406d1388)=(Y:RE_continue)
    CheckStrStr(0xe0434352)=(Y:RE_continue)
    CheckStrStr(0x80010108)=(Y:RE_continue)
    CheckStrStr(0x800706b5)=(Y:RE_continue)
    goto(RE_continue)

RE_crash:
    FlushFiles
    Pause

RE_continue:
    Continue(0x80010001)=

Default:
    Continue=

EN:
finish:
    FlushFiles
Exception:
    QemuQuit
