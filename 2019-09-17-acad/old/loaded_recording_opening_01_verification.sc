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

#    RegisterReactions(accore.dll+0x710a05,ST,0x0)
#    RegisterReactions(accore.dll+0x710a0a,EN,0x0)
#   ten generuje wyjatki w jakiejs funkcji byc moze przygotowawczej
#    RegisterReactions(accore.dll+0x8948b,ST,0x0) 
    RegisterReactions(accore.dll+0x89594,ST,0x0)
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
    #DumpMemory
    #SecureAllSections
    #AddScannedLocation(ESP:0x50)

    #Call(RegisterEnableBuiltin)

    ResizeOutBuffer(0x10000000)
    ResizeModBuffer(0x1000000)
    ChangeInterval(10000000)

    #Pause
    #TracerStartTrace
    #TracerStartTraceLight
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
    ReadTime
finish:
    FlushFiles
Exception:
    QemuQuit
