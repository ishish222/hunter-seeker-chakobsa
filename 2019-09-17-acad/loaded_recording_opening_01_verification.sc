# initial record
Version(2)
Include(scripts/common/initiating.sc)
Include(scripts/common/debugging.sc)

DeployFolders:
    GlobPattern(_FUZZING_SAMPLES/acad/originals_01/*.dwg)
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
        RunCommand(copy \\10.0.2.4\qemu\input\origin01.dwg c:\users\john\desktop\sample.dwg)
        CheckStrStr(cannot)=(Y:Main_Sleep)
        CheckStrStr(network)=(Y:Main_Sleep)

#   ten generuje wyjatki w jakiejs funkcji byc moze przygotowawczej
#    RegisterReactions(accore.dll+0x710a05,ST,0x0)
#    RegisterReactions(accore.dll+0x710a0a,EN,0x0)

    RegisterReactions(accore.dll+0x89599,ST,0x0)
    RegisterReactions(accore.dll+0x89792,EN,0x0)
    EnableReaction(ST)
    EnableReaction(EN)

    RunCommand(start c:\users\john\desktop\sample.dwg)
    Continue=

    Main_Sleep:
        Wait(1)
        goto(Main_Copy)

ST:
    DisableReaction(ST)
    Call(StartAnalysis)
    ResetTime
    Continue=

StartAnalysis:
    TracerDebugLogEnable

    SuspendAll

    ReleaseThread(0xa88)
    SetPriorityHigh(0xa88)

    ReleaseThread(0xe24)
    SetPriorityHigh(0xe24)

    ReleaseThread(0xd00)
    SetPriorityHigh(0xd00)

    #DumpMemory
    #SecureAllSections
    #AddScannedLocation(ESP:0x50)

    #Call(RegisterEnableBuiltin)

    #ResizeOutBuffer(0x10000000)
    #ResizeModBuffer(0x1000000)
    #ChangeInterval(10000000)

    #Pause
    #TracerStartTrace
    #TracerStartTraceLight
    #TracerStartTraceDebug
    Return

RE:
    TracerGetExceptionCode
    CheckStrStr(0xc0000004)=(Y:RE_crash)
    CheckStrStr(0xc0000005)=(Y:RE_crash)
    TracerGetExceptionChance
    CheckStrStr(0x00000000)=(Y:RE_crash)
    goto(RE_continue)

RE_crash:
    FlushFiles
    Beep(ACAD%20crashed)
    Pause

RE_continue:
    Continue(0x80010001)=

Default:
    Continue=

RC:
    FlushFiles
    Beep(Tracer%20crashed)
    Pause

EN:
    ReadTime
finish:
    FlushFiles
Exception:
    QemuQuit
